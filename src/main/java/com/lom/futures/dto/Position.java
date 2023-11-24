package com.lom.futures.dto;

import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Symbol;
import com.lom.futures.enums.MarginType;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Position {

    Double entryPrice;
    MarginType marginType;
    String isAutoAddMargin;
    Double isolatedMargin;
    Double leverage;
    Double liquidationPrice;
    Double markPrice;
    Double maxNotionalValue;
    Double positionAmt;
    Double notional;
    Double isolatedWallet;
    Symbol symbol;
    Double unRealizedProfit;
    PositionSide positionSide;
    Long updateTime;
    Double breakEvenPrice;
    Boolean isolated;
    String adlQuantile;

}
