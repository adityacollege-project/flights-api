package com.ardor.flights.model.reprice;


import com.ardor.flights.common.ErrorCodeConstants;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

import com.ardor.flights.annotation.PaxValidation;
import com.ardor.flights.model.search.FlightSearchRequest;

import jakarta.validation.GroupSequence;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@GroupSequence({RepriceRequest.class, NotNull.class, NotEmpty.class, PaxValidation.class})
public class RepriceRequest {

  @NotNull(message = ErrorCodeConstants.RPE_0001, groups = NotNull.class)
  @NotEmpty(message = ErrorCodeConstants.RPE_0001, groups = NotEmpty.class)
  private List<String> jIds;
  
  private Map<String, String> brandedFareIds;
  private boolean flightInfoRequired;
  
  @Valid
  @NotNull(message = ErrorCodeConstants.RPE_0002, groups = NotNull.class)
  @PaxValidation(message = ErrorCodeConstants.RPE_0003, groups = PaxValidation.class)
  private FlightSearchRequest.PaxDetails paxDetails;
  
}
