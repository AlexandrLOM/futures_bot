package com.lom.futures.bot.strategy.config;

import com.lom.futures.enums.Symbol;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderBlockConfig {

    List<Symbol> symbols;

    public OrderBlockConfig( List<Symbol> symbols) {
        this.symbols = symbols;
    }
}
