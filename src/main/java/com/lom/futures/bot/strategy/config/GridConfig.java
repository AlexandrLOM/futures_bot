package com.lom.futures.bot.strategy.config;

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
public class GridConfig {

    Symbol symbol;

    Double stopLossLong;
    Double takeProfitLong;

    Double stopLossShort;
    Double takeProfitShort;

    Double quantity;
    Double quantityMax;

    public GridConfig(Symbol symbol,
                      Double stopLossLong,
                      Double takeProfitLong,
                      Double stopLossShort,
                      Double takeProfitShort,
                      Double quantity,
                      Double quantityMax) {
        this.symbol = symbol;
        this.stopLossLong = stopLossLong;
        this.takeProfitLong = takeProfitLong;
        this.stopLossShort = stopLossShort;
        this.takeProfitShort = takeProfitShort;
        this.quantity = quantity;
        this.quantityMax = quantityMax;
    }
}
