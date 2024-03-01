/**
 * 
 */
package com.ardor.flights.repository.mongo.reprice;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.ardor.flights.entity.reprice.RepriceRequestResponseEntity;

/**
 * 
 */
@Repository
public interface RepriceRequestResponseRepository extends MongoRepository<RepriceRequestResponseEntity, String> {

  @Query("{ 'rawRequest.jIds' : ?0 }")
  List<RepriceRequestResponseEntity> findByRawRequestJIdsOrderBy_idDesc(List<String> jIds, Sort sort);

  default RepriceRequestResponseEntity findLatestByRawRequestJIds(List<String> jIds) {
    List<RepriceRequestResponseEntity> resultList = findByRawRequestJIdsOrderBy_idDesc(jIds, Sort.by(Sort.Order.desc("_id")));
    return resultList.isEmpty() ? null : resultList.get(0);
  }

}
