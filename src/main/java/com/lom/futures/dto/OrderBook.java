package com.lom.futures.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderBook {

    Long lastUpdateId;
    @JsonProperty("E")
    Long e; // Message output time
    @JsonProperty("T")
    Long t; // Transaction time
    List<List<Double>> bids;
    List<List<Double>> asks;

}
