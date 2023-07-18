package com.lom.futures.bot.strategy.config.bean;

import com.lom.futures.bot.strategy.config.GridConfig;
import com.lom.futures.bot.strategy.config.old.OpenOneSideConfig;
import com.lom.futures.enums.Symbol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class GridBeanConfig {

    @Bean
    public Map<Symbol, GridConfig> gridConfig() {
        return Map.of(Symbol.ETHUSDT,
                new GridConfig(
                        Symbol.ETHUSDT,
                        0.7,
                        0.7,
                        0.7,
                        0.7,
                        0.015,
                        0.3),
                Symbol.BTCUSDT,
                new GridConfig(
                        Symbol.BTCUSDT,
                        0.6,
                        0.6,
                        0.6,
                        0.6,
                        0.001,
                        0.017),
                Symbol.BNBUSDT,
                new GridConfig(
                        Symbol.BNBUSDT,
                        0.8,
                        0.8,
                        0.8,
                        0.8,
                        0.12,
                        1.95),
                Symbol.DOTUSDT,
                new GridConfig(
                        Symbol.DOTUSDT,
                        1.2,
                        1.2,
                        1.2,
                        1.2,
                        5.7,
                        93.0),
                Symbol.NEARUSDT,
                new GridConfig(
                        Symbol.NEARUSDT,
                        2.5,
                        2.5,
                        2.5,
                        2.5,
                        21.0,
                        342.0),
                Symbol.DEFIUSDT,
                new GridConfig(
                        Symbol.DEFIUSDT,
                        2.0,
                        2.0,
                        2.0,
                        2.0,
                        0.06,
                        0.99),
                Symbol.ADAUSDT,
                new GridConfig(
                        Symbol.ADAUSDT,
                        1.0,
                        1.0,
                        1.0,
                        1.0,
                        96.0,
                        780.0)
        );
    }

//    @Bean
//    public Map<Symbol, GridConfig> gridConfig() {
//        return Map.of(Symbol.ETHUSDT,
//                new GridConfig(
//                        Symbol.ETHUSDT,
//                        10.0,
//                        10.0,
//                        10.0,
//                        10.0,
//                        0.005,
//                        0.1),
//                Symbol.BNBUSDT,
//                new GridConfig(
//                        Symbol.BNBUSDT,
//                        1.5,
//                        1.5,
//                        1.5,
//                        1.5,
//                        0.04,
//                        0.65),
//                Symbol.DOTUSDT,
//                new GridConfig(
//                        Symbol.DOTUSDT,
//                        0.06,
//                        0.06,
//                        0.06,
//                        0.06,
//                        1.9,
//                        31.0),
//                Symbol.NEARUSDT,
//                new GridConfig(
//                        Symbol.NEARUSDT,
//                        0.02,
//                        0.02,
//                        0.02,
//                        0.02,
//                        7.0,
//                        114.0),
//                Symbol.DEFIUSDT,
//                new GridConfig(
//                        Symbol.DEFIUSDT,
//                        6.0,
//                        6.0,
//                        6.0,
//                        6.0,
//                        0.02,
//                        0.33)
//        );
//    }

}
