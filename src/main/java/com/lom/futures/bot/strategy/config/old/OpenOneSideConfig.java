package com.lom.futures.bot.strategy.config.old;

import com.lom.futures.enums.Symbol;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OpenOneSideConfig {

    Symbol symbol;

    Double positionStopLossLong;
    Double positionTakeProfitLong;

    Double positionStopLossShort;
    Double positionTakeProfitShort;

    Double quantity;
    Double quantityMax;

    public OpenOneSideConfig(Symbol symbol,
                             Double positionStopLossLong,
                             Double positionTakeProfitLong,
                             Double positionStopLossShort,
                             Double positionTakeProfitShort,
                             Double quantity,
                             Double quantityMax) {
        this.symbol = symbol;
        this.positionStopLossLong = positionStopLossLong;
        this.positionTakeProfitLong = positionTakeProfitLong;
        this.positionStopLossShort = positionStopLossShort;
        this.positionTakeProfitShort = positionTakeProfitShort;
        this.quantity = quantity;
        this.quantityMax = quantityMax;
    }
}
