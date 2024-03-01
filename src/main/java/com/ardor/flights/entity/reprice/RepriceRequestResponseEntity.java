/**
 *
 */
package com.ardor.flights.entity.reprice;

import com.ardor.flights.model.reprice.FlightRepriceResponse;
import com.ardor.flights.model.reprice.RepriceRequest;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 *
 */

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "flight-reprice-request")
public class RepriceRequestResponseEntity {

  @Id
  private String id;
  private String searchKey;
  private String jId;
  private String supplier;
  private RepriceRequest rawRequest;
  private FlightRepriceResponse rawResponse;
  private String supplierRequest;
  private String supplierResponse;
  private boolean validPrice;
  @CreatedDate
  private Date created;

}
