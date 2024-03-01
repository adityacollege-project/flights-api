package com.ardor.flights.user.service;

import com.ardor.flights.common.ErrorCodeConstants;
import com.ardor.flights.exception.UserNotFoundExcpetion;
import com.ardor.flights.model.user.UserInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  @Value("${flights.user.endpoint}")
  private String getUserEndpoint;

  @Autowired
  private final RestTemplate userRestTemplate;
  private final ObjectMapper objectMapper = new ObjectMapper();

  public UserInfo getUserInfo(HttpServletRequest httpRequest)
  {
    String apiKey = httpRequest.getHeader("apikey");
    if(!apiKey.isEmpty()) {
      return userInfoByAPIKey(apiKey);
    }else {
      log.info(":::::=> Empty API key provided");
      throw new UserNotFoundExcpetion(ErrorCodeConstants.UIE_0001);
    }
  }
  public UserInfo userInfoByAPIKey(String apiKey)
  {
    ResponseEntity<String> responseEntity = callArdorAPI(apiKey);
    List<UserInfo> userInfo;
    try {
      String responseBody = responseEntity.getBody();
      if(StringUtils.isNotEmpty(responseBody)) {
        UserInfo [] users = objectMapper.readValue(responseBody, UserInfo[].class);
        userInfo = Arrays.asList(users);
        log.info("userInfoCaptured");
      }
      else
        throw new UserNotFoundExcpetion(ErrorCodeConstants.UIE_0002);
    } catch (JsonProcessingException e) {
      log.error("Json Processing Exception");
      throw new UserNotFoundExcpetion(ErrorCodeConstants.UIE_0003);
    }
    return userInfo.get(0);
  }

  private ResponseEntity<String> callArdorAPI(String apiKey)
  {
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      String finalURL = getFinalUrl(apiKey);
      return userRestTemplate.getForEntity(finalURL,String.class);
    } catch (Exception e) {
      throw new UserNotFoundExcpetion(ErrorCodeConstants.UIE_0004);
    }
  }

  private String getFinalUrl(String apikey){
    return getUserEndpoint + "/"+ apikey;
  }
}
