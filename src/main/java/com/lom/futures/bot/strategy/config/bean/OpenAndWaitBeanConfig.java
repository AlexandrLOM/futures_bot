package com.lom.futures.bot.strategy.config.bean;

import com.lom.futures.bot.strategy.config.OpenAndWaitConfig;
import com.lom.futures.enums.Symbol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class OpenAndWaitBeanConfig {

    @Bean
    public Map<Symbol, OpenAndWaitConfig> config() {
        return Map.of(Symbol.ETHUSDT,
                new OpenAndWaitConfig(
                        Symbol.ETHUSDT,
                        10.0,
                        10.0,
                        10.0,
                        10.0,
                        0.02,
                        0.16),
                Symbol.BNBUSDT,
                new OpenAndWaitConfig(
                        Symbol.BNBUSDT,
                        2.0,
                        2.0,
                        2.0,
                        2.0,
                        0.05,
                        0.8),
                Symbol.SOLUSDT,
                new OpenAndWaitConfig(
                        Symbol.SOLUSDT,
                        0.2,
                        0.2,
                        0.2,
                        0.2,
                        1.0,
                        16.0),
                Symbol.ATOMUSDT,
                new OpenAndWaitConfig(
                        Symbol.ATOMUSDT,
                        0.07,
                        0.07,
                        0.07,
                        0.07,
                        1.0,
                        16.0)

        );
    }

}
