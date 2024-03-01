package com.ardor.flights.search.service;

import com.ardor.flights.cache.redis.RedisCacheManager;
import com.ardor.flights.entity.search.SearchRequestEntity;
import com.ardor.flights.entity.search.SearchResponseEntity;
import com.ardor.flights.entity.search.SearchResponseEntity.FlightsResponse;
import com.ardor.flights.model.search.Fares;
import com.ardor.flights.model.search.FlightItinerary;
import com.ardor.flights.model.search.FlightSearchRequest;
import com.ardor.flights.model.search.FlightSearchResponse;
import com.ardor.flights.repository.mongo.search.SearchRequestRepository;
import com.ardor.flights.repository.mongo.search.SearchResponseRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) 2024, Ardor Technologies All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of Ardor Technologies. You shall
 * not disclose such Confidential Information and shall use it only in accordance with the terms of
 * the license agreement you entered into with Ardor Technologies.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

@Component
@Slf4j
public class FlightSearchHelper {

  private final RedisCacheManager redisCacheManager;
  private final SearchResponseRepository searchResponseRepository;
  private final SearchRequestRepository searchRequestRepository;

  public FlightSearchHelper(RedisCacheManager redisCacheManager,
      SearchResponseRepository searchResponseRepository,
      SearchRequestRepository searchRequestRepository) {
    this.redisCacheManager = redisCacheManager;
    this.searchResponseRepository = searchResponseRepository;
    this.searchRequestRepository = searchRequestRepository;
  }


  @Async
  public void putValueInCache(FlightSearchRequest request, FlightSearchResponse searchResponse) {
    try {
      redisCacheManager.putSearchResultsToCache(request, searchResponse);
    } catch (JsonProcessingException jpe) {
      log.error("Failing to put the response in cache", jpe);
    }
  }

  @Async
  public void updateSearchRequestAndSaveResponse(SearchRequestEntity searchRequestEntity,
      FlightSearchResponse searchResponse) {
    if (searchRequestEntity.isBrandedfares()) {
      searchRequestEntity.setBrandNoOfResults(searchResponse.getBrandedFlights().entrySet().stream()
          .collect(Collectors.toMap(Entry::getKey, entry -> entry.getValue().size())));

    } else {
      List<Map<String, String>> lowestFareByAirline = new ArrayList<>(
          searchResponse.getFlights().stream()
              .collect(Collectors.groupingBy(FlightItinerary::getValidatingCarrierCode,
                  Collectors.collectingAndThen(
                      Collectors.minBy(Comparator.comparingDouble(this::getLowestfare)),
                      opt -> opt.map(obj -> {
                        Map<String, String> strMap = new HashMap<>();
                        strMap.put("airlineName", obj.getValidatingCarrierCode());
                        strMap.put("baseFare", String.valueOf(obj.getFares().get(0).getBaseFare()));
                        strMap.put("taxes", String.valueOf(obj.getFares().get(0).getTaxes()));
                        strMap.put("supplier", "Mondee");
                        return strMap;
                      }).orElse(null)
                  )
              )).values());
      searchRequestEntity.setLowestFareByAirline(lowestFareByAirline);
      searchRequestEntity.setNoOfResults(searchResponse.getFlights().size());
    }

    searchRequestEntity.setSearchKey(searchRequestEntity.getSearchKey());
    searchRequestRepository.save(searchRequestEntity);
    saveSearchResponse(searchRequestEntity, searchResponse);
  }

  private double getLowestfare(FlightItinerary itinerary) {
    return itinerary.getFares().stream().mapToDouble(fare -> (fare.getBaseFare() + fare.getTaxes()))
        .min().orElse(0.0);
  }

  private void saveSearchResponse(SearchRequestEntity searchRequestEntity,
      FlightSearchResponse searchResponse) {
    if (searchRequestEntity.isBrandedfares()) {

    } else {
      List<FlightItinerary> flights = searchResponse.getFlights();
    }

    SearchResponseEntity responseEntity = SearchResponseEntity.builder()
        .searchKey(searchRequestEntity.getSearchKey())
        .responseKey(searchResponse.getResponseKey())
        .flights(Optional.ofNullable(searchResponse.getFlights())
            .map(f -> this.prepareResponseObject(f, searchRequestEntity))
            .orElse(Collections.emptyList()))
        .brandFlights(Optional.ofNullable(searchResponse.getBrandedFlights())
            .map(f -> f.entrySet()
                .stream().collect(Collectors.toMap(Entry::getKey,
                    entry -> this.prepareResponseObject(entry.getValue(), searchRequestEntity)))
            ).orElse(Collections.emptyMap()))
        .created(new Date())
        .build();
    this.searchResponseRepository.save(responseEntity);
  }

  private List<FlightsResponse> prepareResponseObject(List<FlightItinerary> flights,
      SearchRequestEntity request) {
    return flights.stream().map(obj -> {
      return FlightsResponse.builder()
          .jId(obj.getJId())
          .supplierItineraryId(obj.getItineraryId())
          .airlineCode(obj.getValidatingCarrierCode())
          .cabinClass(obj.getCabinClass())
          .bookingClasses(Optional.ofNullable(obj.getFares())
              .map(fares -> fares.isEmpty() ? null : fares.get(0))
              .map(Fares::getBookingClasses).orElse(null))
          .fareBasisCodes(Optional.ofNullable(obj.getFares())
              .map(fares -> fares.isEmpty() ? null : fares.get(0))
              .map(Fares::getFareBasisCodes).orElse(null))
          .totalBaseFare(Optional.ofNullable(obj.getFares()).map(f -> f.stream()
                  .mapToDouble(item -> this.calculateTotalBaseFare(item, request)).sum())
              .orElse(this.calculateBrandTotalBaseFare(obj, request)))
          .totalTaxes(Optional.ofNullable(obj.getFares()).map(f -> f.stream()
                  .mapToDouble(item -> this.calculateTotalTaxes(item, request)).sum())
              .orElse(this.calculateBrandTotalTaxes(obj, request)))
          .brands(Optional.ofNullable(obj.getBrands()).map(f -> f.stream().map(m -> {
            Map<String, Object> brand = new LinkedHashMap<>();
            brand.put("brand", m.getName());
            brand.put("brandId", m.getBrandId());
            brand.put("totalBaseFare", Optional.ofNullable(m.getFares()).map(i -> i.stream()
                    .mapToDouble(item -> this.calculateTotalBaseFare(item, request)).sum())
                .orElse(0.0));
            brand.put("totalTaxes", Optional.ofNullable(m.getFares()).map(i -> i.stream()
                    .mapToDouble(item -> this.calculateTotalTaxes(item, request)).sum())
                .orElse(0.0));
            brand.put("bookingClasses", m.getFares().get(0).getBookingClasses());
            brand.put("fareBasisCodes", m.getFares().get(0).getFareBasisCodes());
            return brand;
          }).collect(Collectors.toList())).orElse(Collections.emptyList()))
          .build();
    }).collect(Collectors.toList());
  }

  private double calculateBrandTotalTaxes(FlightItinerary flight, SearchRequestEntity request) {
    flight.getBrands().sort(Comparator.comparingDouble(brand1 -> brand1.getFares().get(0).getBaseFare()));
    return flight.getBrands().get(0).getFares().stream().mapToDouble(item -> this.calculateTotalTaxes(item, request)).sum();
  }

  private double calculateBrandTotalBaseFare(FlightItinerary flight, SearchRequestEntity request) {
    flight.getBrands().sort(Comparator.comparingDouble(brand1 -> brand1.getFares().get(0).getBaseFare()));
    return flight.getBrands().get(0).getFares().stream().mapToDouble(item -> this.calculateTotalBaseFare(item, request)).sum();
  }

  private double calculateTotalBaseFare(Fares fare, SearchRequestEntity request) {
    double totalBaseFare = 0.0;
    if (fare.getPaxType().equals("ADT")) {
      totalBaseFare += fare.getBaseFare() * request.getPaxDetails().getAdultCount();
    } else if (request.getPaxDetails().getChildCount() > 0 && fare.getPaxType()
        .equals("CHD")) {
      totalBaseFare += fare.getBaseFare() * request.getPaxDetails().getChildCount();
    } else if (request.getPaxDetails().getInfantCount() > 0 && fare.getPaxType()
        .equals("INF")) {
      totalBaseFare += fare.getBaseFare() * request.getPaxDetails().getInfantCount();
    }
    return totalBaseFare;
  }

  private double calculateTotalTaxes(Fares fare, SearchRequestEntity request) {
    double totalTaxes = 0.0;
    if (fare.getPaxType().equals("ADT")) {
      totalTaxes += fare.getTaxes() * request.getPaxDetails().getAdultCount();
    } else if (request.getPaxDetails().getChildCount() > 0 && fare.getPaxType().equals("CHD")) {
      totalTaxes += fare.getTaxes() * request.getPaxDetails().getChildCount();
    } else if (request.getPaxDetails().getInfantCount() > 0 && fare.getPaxType().equals("INF")) {
      totalTaxes += fare.getTaxes() * request.getPaxDetails().getInfantCount();
    }
    return totalTaxes;
  }

}
