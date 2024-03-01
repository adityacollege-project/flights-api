package com.ardor.flights.mapper;

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

import com.ardor.flights.model.search.BaggageAllowance;
import com.ardor.flights.model.search.Brands;
import com.ardor.flights.model.search.CityPairs;
import com.ardor.flights.model.search.Fares;
import com.ardor.flights.model.search.FlightItinerary;
import com.ardor.flights.model.search.FlightSearchResponse;
import com.ardor.flights.model.search.FlightSegment;
import com.ardor.flights.supplier.mondee.model.search.MondeeBaggageAllowance;
import com.ardor.flights.supplier.mondee.model.search.MondeeBrandFares;
import com.ardor.flights.supplier.mondee.model.search.MondeeCityPairs;
import com.ardor.flights.supplier.mondee.model.search.MondeeFares;
import com.ardor.flights.supplier.mondee.model.search.MondeeFlightItinerary;
import com.ardor.flights.supplier.mondee.model.search.MondeeSearchResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface SearchResponseMapper {

  @Mappings({
      @Mapping(target = "flights", expression = "java(mapFlightItineraries(response.getFlights()))")
  })
  FlightSearchResponse mapResponsefromMondeeToArdor(MondeeSearchResponse response);

  default List<FlightItinerary> mapFlightItineraries(List<MondeeFlightItinerary> itineraries) {
    return Optional.ofNullable(itineraries)
        .map(f -> f.stream().map(this::mapFlightItinerary).collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }

  default FlightItinerary mapFlightItinerary(MondeeFlightItinerary itinerary) {
    return FlightItinerary.builder()
        .itineraryId(itinerary.getItineraryId())
        .jId(itinerary.getJId())
        .validatingCarrierName(itinerary.getValidatingCarrierName())
        .validatingCarrierCode(itinerary.getValidatingCarrierCode())
        .brandName(itinerary.getBrandName())
        .cabinClass(itinerary.getCabinClass())
        .fareType(itinerary.getFareType())
        .fares(this.mapFares(itinerary.getFares()))
        .segments(this.mapSegements(itinerary))
        .brands(this.mapBrands(itinerary.getBrands()))
        .build();
  }

  default List<CityPairs> mapSegements(MondeeFlightItinerary itinerary) {
    List<CityPairs> citypairs = new ArrayList<>();
    for (MondeeCityPairs segments : itinerary.getSegments()) {
      CityPairs segment = mapSegment(segments);
      for (FlightSegment flightSegment : segment.getFlightSegments()) {
        List<BaggageAllowance> baggageAllowances = Optional.ofNullable(itinerary.getFares())
            .map(f -> f.stream()
                .filter(item -> item.getPaxType().equalsIgnoreCase("ADT")).flatMap(
                    map -> map.getBaggageAllowance().entrySet().stream()
                        .filter(entry -> entry.getKey()
                            .equalsIgnoreCase(String.join("-", flightSegment.getFromAirportCode(),
                                flightSegment.getToAirportCode())))
                        .flatMap(entry -> entry.getValue().stream())).map(this::mapBaggageAllowance)
                .collect(Collectors.toList())).orElse(Collections.emptyList());
        flightSegment.setBaggageAllowance(baggageAllowances);
      }
      citypairs.add(segment);
    }
    return citypairs;
  }

  CityPairs mapSegment(MondeeCityPairs cityPairs);

  default List<Fares> mapFares(List<MondeeFares> fares) {
    return Optional.ofNullable(fares)
        .map(f -> f.stream().map(this::mapFare).collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }

  default List<Brands> mapBrands(List<MondeeBrandFares> brands) {
    return Optional.ofNullable(brands)
        .map(f -> f.stream().map(this::mapBrand).collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }

  default Brands mapBrand(MondeeBrandFares brands) {
    return Optional.ofNullable(brands).map(f -> Brands.builder()
        .brandId(f.getBrandId())
        .name(f.getName())
        .brandAttributeId(f.getBrandAttributeId())
        .fares(this.mapFares(f.getFares()))
        .baggageAllowance(Optional.ofNullable(f.getFares()).map(fareList -> {
          return fareList.get(0).getBaggageAllowance().entrySet().stream().collect(
              Collectors.toMap(Map.Entry::getKey,
                  entry -> entry.getValue().stream().map(this::mapBaggageAllowance)
                      .collect(Collectors.toList())));
        }).orElse(Collections.emptyMap())).build()).orElse(null);
  }

  @Mapping(source = "currencyCode", target = "currencyCode")
  Fares mapFare(MondeeFares fare);

  BaggageAllowance mapBaggageAllowance(MondeeBaggageAllowance allowance);


}
