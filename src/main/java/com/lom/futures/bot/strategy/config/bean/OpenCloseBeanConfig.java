package com.lom.futures.bot.strategy.config.bean;

import com.lom.futures.bot.strategy.config.OpenCloseConfig;
import com.lom.futures.enums.Symbol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenCloseBeanConfig {

    @Bean
    public OpenCloseConfig configBTCUSDT() {
        return new OpenCloseConfig(
                Symbol.BTCUSDT,
                0.2,
                0.3,
                0.7,
                0.15,
                0.001,
                0.01);
    }

    @Bean
    public OpenCloseConfig configETHUSDT() {
        return new OpenCloseConfig(
                Symbol.ETHUSDT,
                0.2,
                0.2,
                0.2,
                0.1,
                0.03,
                0.5);
    }

}
