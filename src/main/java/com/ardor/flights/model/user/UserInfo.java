package com.ardor.flights.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

  private long id;
  private String userCode;
  private String apiTypeCode;
  private String apiName;
  private String apiKey;
  private String isActive;
  private String created;
  private String modified;

}
