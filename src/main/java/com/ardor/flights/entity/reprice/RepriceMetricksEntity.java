/**
 *
 */
package com.ardor.flights.entity.reprice;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
@Document(collection = "flight-reprice-metricks")
public class RepriceMetricksEntity {

  @Id
  private String id;
  private String jId;
  private String phase;
  private String supplier;
  private String airline;
  private String cabinClass;
  private String bookingCode;
  private String fareBasisCode;
  private String paxCount;
  private double baseFare;
  private double taxes;
  private String changedCabinClass;
  private String changedBookingCode;
  private String changedFareBasisCode;
  private double changedBaseFare;
  private double changedTaxes;
  private String remarks;
  private String changedPaxCount;

  private List<String> jIds;
  private Map<String, String> brandIds;
  private boolean brandedfares;

  private String searchKey;
  private String metricks;
  private long responseTime;

  @CreatedDate
  private Date created;
}
