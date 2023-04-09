package com.lom.futures.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MarkPriceKline {

    Long openTime;
    Double open;
    Double high;
    Double low;
    Double close;
    Long closeTime;

}
