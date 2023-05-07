package com.lom.futures.bot.strategy.config;

import com.lom.futures.enums.Symbol;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OpenCloseConfig {

    Symbol symbol;

    Double zoneOfInsensitivityPercent;
    Double positionStopLossPercent;
    Double positionTakeProfitPercent;

    Double zoneOfInsensitivityTakeProfitPercent;

    Double quantity;
    Double quantityMax;

    public OpenCloseConfig(Symbol symbol,
                           Double zoneOfInsensitivityPercent,
                           Double positionStopLossPercent,
                           Double positionTakeProfitPercent,
                           Double zoneOfInsensitivityTakeProfitPercent,
                           Double quantity,
                           Double quantityMax) {
        this.symbol = symbol;
        this.zoneOfInsensitivityPercent = zoneOfInsensitivityPercent;
        this.positionStopLossPercent = positionStopLossPercent;
        this.positionTakeProfitPercent = positionTakeProfitPercent;
        this.zoneOfInsensitivityTakeProfitPercent = zoneOfInsensitivityTakeProfitPercent;
        this.quantity = quantity;
        this.quantityMax = quantityMax;
    }
}
