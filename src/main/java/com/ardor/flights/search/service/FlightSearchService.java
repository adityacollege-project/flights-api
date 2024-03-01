package com.ardor.flights.search.service;

/**
 * Copyright (c) 2023, Ardor Technologies All rights reserved.
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

import com.ardor.flights.cache.redis.RedisCacheManager;
import com.ardor.flights.mapper.FlightSearchRequestMapper;
import com.ardor.flights.entity.search.SearchRequestEntity;
import com.ardor.flights.model.search.FlightSearchRequest;
import com.ardor.flights.model.search.FlightSearchRequest.OriginDestination;
import com.ardor.flights.model.search.FlightSearchResponse;
import com.ardor.flights.repository.mongo.search.SearchRequestRepository;
import com.ardor.flights.search.supplier.mondee.MondeeSupplier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class FlightSearchService {

  private final FlightSearchRequestMapper searchRequestMapper;
  private final MondeeSupplier mondeeSupplier;
  private final SearchRequestRepository searchRequestRepository;
  private final RedisCacheManager redisCacheManager;
  private final ObjectMapper mapper;
  private final FlightSearchHelper flightSearchHelper;

  public FlightSearchService(MondeeSupplier mondeeSupplier,
      SearchRequestRepository searchRequestRepository,
      FlightSearchRequestMapper searchRequestMapper, RedisCacheManager redisCacheManager,
      FlightSearchHelper flightSearchHelper) {
    this.mondeeSupplier = mondeeSupplier;
    this.searchRequestRepository = searchRequestRepository;
    this.searchRequestMapper = searchRequestMapper;
    this.redisCacheManager = redisCacheManager;
    this.flightSearchHelper = flightSearchHelper;
    this.mapper = new ObjectMapper();
  }

  public FlightSearchResponse flightSearch(FlightSearchRequest request) {

    if (!StringUtils.isBlank(request.getOriginDestinations().get(0).getReturnDate())) {
      OriginDestination onwardJourney = request.getOriginDestinations().get(0);
      OriginDestination returnJourney = OriginDestination.builder().from(onwardJourney.getTo())
          .to(onwardJourney.getFrom()).onwardDate(onwardJourney.getReturnDate())
          .cabinClass(onwardJourney.getCabinClass()).build();
      request.getOriginDestinations().add(returnJourney);
    }

    // Insert in Mongodb
    SearchRequestEntity searchRequestEntity = searchRequestMapper.mapSearchModelToEntity(request);
    searchRequestEntity = searchRequestRepository.save(searchRequestEntity);

    // Checking from cache
    Optional<FlightSearchResponse> cacheResponse = getValueFromCache(request);
    if (!cacheResponse.isEmpty()) {
      return cacheResponse.get();
    }
    // if not available in cache making call to supplier
    FlightSearchResponse searchResponse = mondeeSupplier.flightSearch(request);

    flightSearchHelper.updateSearchRequestAndSaveResponse(searchRequestEntity, searchResponse);

    //putting in cache
    if (!(CollectionUtils.isEmpty(searchResponse.getFlights()) && CollectionUtils.isEmpty(searchResponse.getBrandedFlights()))) {
      flightSearchHelper.putValueInCache(request, searchResponse);
    }

    return searchResponse;
  }

  //Method to get the value from Cache
  private Optional<FlightSearchResponse> getValueFromCache(FlightSearchRequest searchRequest) {
    String cacheResponse = redisCacheManager.getResultsFromCache(searchRequest);
    FlightSearchResponse flightSearchResponse = null;
    if (StringUtils.isNotEmpty(cacheResponse)) {
      log.debug("cache Response :: {} ", cacheResponse);
      try {
        flightSearchResponse = mapper.readValue(cacheResponse, FlightSearchResponse.class);
        log.info("search response count from Cache {}", flightSearchResponse.getFlights().size());
      } catch (JsonProcessingException e) {
        throw new RuntimeException(e);
      }
    }
    return Optional.ofNullable(flightSearchResponse);
  }

}
