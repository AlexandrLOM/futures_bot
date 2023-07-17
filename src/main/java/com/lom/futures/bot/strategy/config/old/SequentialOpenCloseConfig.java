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
public class SequentialOpenCloseConfig {

    Symbol symbol;

    Double positionStopLossPercent;
    Double positionTakeProfitPercent;
    Double zoneOfInsensitivityPercent;

    Double quantity;
    Double quantityMax;

    public SequentialOpenCloseConfig(Symbol symbol,
                                     Double positionStopLossPercent,
                                     Double positionTakeProfitPercent,
                                     Double zoneOfInsensitivityPercent,
                                     Double quantity,
                                     Double quantityMax) {
        this.symbol = symbol;
        this.positionStopLossPercent = positionStopLossPercent;
        this.positionTakeProfitPercent = positionTakeProfitPercent;
        this.zoneOfInsensitivityPercent = zoneOfInsensitivityPercent;
        this.quantity = quantity;
        this.quantityMax = quantityMax;
    }
}
