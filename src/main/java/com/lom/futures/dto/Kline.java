package com.lom.futures.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Kline {

    Long openTime;
    Double open;
    Double high;
    Double low;
    Double close;
    Double volume;
    Long closeTime;
    Double quoteAssetVolume;
    Integer numberOfTrades;
    Double takerBuyBaseAssetVolume;
    Double takerBuyQuoteAssetVolume;
}
