package com.ardor.flights.cache.redis;

import com.ardor.flights.model.search.FlightSearchRequest;
import com.ardor.flights.model.search.FlightSearchRequest.OriginDestination;
import com.ardor.flights.model.search.FlightSearchRequest.PaxDetails;
import com.ardor.flights.model.search.FlightSearchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RedisCacheManager {

  public static final String DELIMITER = "-";

  @Autowired
  RedisService redisService;

  public String getResultsFromCache(FlightSearchRequest searchRequest) {
    String key = prepareKey(searchRequest);
    log.debug("looking in cache with key {} ", key);
    String valueFromCache = redisService.getFromCache(key);
    return Optional.ofNullable(valueFromCache).orElse(StringUtils.EMPTY);
  }

  private String prepareKey(FlightSearchRequest searchRequest) {

    StringBuilder odKey = new StringBuilder();
    for (int i = 0; i < searchRequest.getOriginDestinations().size(); i++) {
      OriginDestination originDestination = searchRequest.getOriginDestinations().get(i);
      if (i == 1 && StringUtils.isNotBlank(
          searchRequest.getOriginDestinations().get(i - 1).getReturnDate())) {
        continue;
      }
      odKey.append(DELIMITER).append(this.prepareOriginDestinationKey(originDestination));
    }
    odKey = new StringBuilder(odKey.substring(1));
    String cabinClassKey = searchRequest.getOriginDestinations().get(0).getCabinClass();
    String paxDetailsKey = preparePaxDetailsKey(searchRequest.getPaxDetails());
    return String.join(DELIMITER, odKey.toString(), cabinClassKey, paxDetailsKey,
        String.valueOf(searchRequest.isBrandedfares()));
  }

  private String prepareOriginDestinationKey(OriginDestination originDestination) {
    String key = originDestination.getFrom() + DELIMITER + originDestination.getTo()
        + DELIMITER + originDestination.getOnwardDate();
    if (StringUtils.isNotBlank(originDestination.getReturnDate())) {
      key += DELIMITER + originDestination.getReturnDate();
    }
    return key;
  }

  private String preparePaxDetailsKey(PaxDetails paxDetails) {
    return (paxDetails.getAdultCount() > 1 ? 1 : paxDetails.getAdultCount()) + DELIMITER + (
        paxDetails.getChildCount() > 1 ? 1 : paxDetails.getChildCount()) + DELIMITER + (
        paxDetails.getInfantCount() > 1 ? 1 : paxDetails.getInfantCount());
  }

  public boolean putSearchResultsToCache(FlightSearchRequest searchRequest,
      FlightSearchResponse response)
      throws JsonProcessingException {
    String key = prepareKey(searchRequest);
    ObjectMapper objectMapper = new ObjectMapper();
    redisService.populateCache(key, objectMapper.writeValueAsString(response));
    return true;
  }

}
