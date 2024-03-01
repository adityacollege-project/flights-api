package com.ardor.flights.mapper;

import com.ardor.flights.entity.search.SearchRequestEntity;
import com.ardor.flights.model.search.FlightSearchRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

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

@Mapper(componentModel = "spring")
public interface FlightSearchRequestMapper {

  @Mappings({
      @Mapping(source = "paxDetails", target = "paxDetails"),
      @Mapping(source = "originDestinations", target = "originDestinations"),
      @Mapping(target = "created", expression = "java(new java.util.Date())")
  })
  SearchRequestEntity mapSearchModelToEntity(FlightSearchRequest request);

}
