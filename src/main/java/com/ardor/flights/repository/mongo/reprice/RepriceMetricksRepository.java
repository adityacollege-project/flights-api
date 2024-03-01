/**
 * 
 */
package com.ardor.flights.repository.mongo.reprice;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.ardor.flights.entity.reprice.RepriceMetricksEntity;

/**
 * 
 */
public interface RepriceMetricksRepository extends MongoRepository<RepriceMetricksEntity, String> {

}