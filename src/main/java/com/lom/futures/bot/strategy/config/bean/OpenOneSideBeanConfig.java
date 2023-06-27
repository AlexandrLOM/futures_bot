package com.lom.futures.bot.strategy.config.bean;

import com.lom.futures.bot.strategy.config.OpenOneSideConfig;
import com.lom.futures.enums.Symbol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenOneSideBeanConfig {

//    @Bean
    public Map<Symbol, OpenOneSideConfig> openOneSideConfig() {
        return Map.of(Symbol.ETHUSDT,
                new OpenOneSideConfig(
                        Symbol.ETHUSDT,
                        4.0,
                        4.0,
                        4.0,
                        4.0,
                        0.02,
                        0.19),
                Symbol.BNBUSDT,
                new OpenOneSideConfig(
                        Symbol.BNBUSDT,
                        1.5,
                        1.5,
                        1.5,
                        1.5,
                        0.07,
                        0.64),
                Symbol.SOLUSDT,
                new OpenOneSideConfig(
                        Symbol.SOLUSDT,
                        0.12,
                        0.12,
                        0.12,
                        0.12,
                        1.0,
                        10.0),
                Symbol.ATOMUSDT,
                new OpenOneSideConfig(
                        Symbol.ATOMUSDT,
                        0.04,
                        0.04,
                        0.04,
                        0.04,
                        1.4,
                        13.0),
                Symbol.XRPUSDT,
                new OpenOneSideConfig(
                        Symbol.XRPUSDT,
                        0.005,
                        0.005,
                        0.005,
                        0.005,
                        30.0,
                        300.0),
                Symbol.BTCUSDT,
                new OpenOneSideConfig(
                        Symbol.XRPUSDT,
                        150.0,
                        150.0,
                        150.0,
                        150.0,
                        0.001,
                        0.01),
                Symbol.APTUSDT,
                new OpenOneSideConfig(
                        Symbol.APTUSDT,
                        0.1,
                        0.1,
                        0.1,
                        0.1,
                        1.0,
                        10.0),
                Symbol.LTCUSDT,
                new OpenOneSideConfig(
                        Symbol.LTCUSDT,
                        0.75,
                        0.75,
                        0.75,
                        0.75,
                        0.2,
                        2.0),
                Symbol.MASKUSDT,
                new OpenOneSideConfig(
                        Symbol.MASKUSDT,
                        0.04,
                        0.04,
                        0.04,
                        0.04,
                        4.0,
                        40.0)
        );
    }

//    @Bean
    public Map<Symbol, OpenOneSideConfig> openOneSideConfigV2() {
        return Map.of(Symbol.ETHUSDT,
                new OpenOneSideConfig(
                        Symbol.ETHUSDT,
                        4.0,
                        8.0,
                        4.0,
                        8.0,
                        0.02,
                        1.2),
                Symbol.BNBUSDT,
                new OpenOneSideConfig(
                        Symbol.BNBUSDT,
                        0.75,
                        1.5,
                        0.75,
                        1.5,
                        0.07,
                        1.92),
                Symbol.SOLUSDT,
                new OpenOneSideConfig(
                        Symbol.SOLUSDT,
                        0.06,
                        0.12,
                        0.06,
                        0.12,
                        1.0,
                        30.0),
                Symbol.ATOMUSDT,
                new OpenOneSideConfig(
                        Symbol.ATOMUSDT,
                        0.02,
                        0.04,
                        0.02,
                        0.04,
                        1.4,
                        39.0),
                Symbol.XRPUSDT,
                new OpenOneSideConfig(
                        Symbol.XRPUSDT,
                        0.003,
                        0.006,
                        0.003,
                        0.006,
                        30.0,
                        900.0),
                Symbol.BTCUSDT,
                new OpenOneSideConfig(
                        Symbol.XRPUSDT,
                        75.0,
                        150.0,
                        75.0,
                        150.0,
                        0.001,
                        0.03),
                Symbol.APTUSDT,
                new OpenOneSideConfig(
                        Symbol.APTUSDT,
                        0.05,
                        0.1,
                        0.05,
                        0.1,
                        1.0,
                        30.0),
                Symbol.LTCUSDT,
                new OpenOneSideConfig(
                        Symbol.LTCUSDT,
                        0.31,
                        0.75,
                        0.31,
                        0.75,
                        0.2,
                        6.0),
                Symbol.MASKUSDT,
                new OpenOneSideConfig(
                        Symbol.MASKUSDT,
                        0.02,
                        0.04,
                        0.02,
                        0.04,
                        4.0,
                        120.0)
        );
    }

    @Bean
    public Map<Symbol, OpenOneSideConfig> openOneSideConfigV3() {
        return Map.of(Symbol.ETHUSDT,
                new OpenOneSideConfig(
                        Symbol.ETHUSDT,
                        9.0,
                        9.0,
                        9.0,
                        9.0,
                        0.02,
                        0.2),
                Symbol.BNBUSDT,
                new OpenOneSideConfig(
                        Symbol.BNBUSDT,
                        1.2,
                        1.2,
                        1.2,
                        1.2,
                        0.07,
                        0.32),
                Symbol.SOLUSDT,
                new OpenOneSideConfig(
                        Symbol.SOLUSDT,
                        0.1,
                        0.1,
                        0.1,
                        0.1,
                        1.0,
                        5.0),
                Symbol.ATOMUSDT,
                new OpenOneSideConfig(
                        Symbol.ATOMUSDT,
                        0.1,
                        0.1,
                        0.1,
                        0.1,
                        1.4,
                        6.5),
                Symbol.XRPUSDT,
                new OpenOneSideConfig(
                        Symbol.XRPUSDT,
                        0.005,
                        0.005,
                        0.005,
                        0.005,
                        30.0,
                        150.0),
                Symbol.BTCUSDT,
                new OpenOneSideConfig(
                        Symbol.XRPUSDT,
                        170.0,
                        170.0,
                        170.0,
                        170.0,
                        0.001,
                        0.005),
                Symbol.APTUSDT,
                new OpenOneSideConfig(
                        Symbol.APTUSDT,
                        0.1,
                        0.1,
                        0.1,
                        0.1,
                        1.0,
                        5.0),
                Symbol.LTCUSDT,
                new OpenOneSideConfig(
                        Symbol.LTCUSDT,
                        0.75,
                        0.75,
                        0.75,
                        0.75,
                        0.2,
                        1.0),
                Symbol.MASKUSDT,
                new OpenOneSideConfig(
                        Symbol.MASKUSDT,
                        0.05,
                        0.05,
                        0.05,
                        0.05,
                        4.0,
                        20.0)
        );
    }

}
