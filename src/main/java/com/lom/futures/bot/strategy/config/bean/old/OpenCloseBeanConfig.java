package com.lom.futures.bot.strategy.config.bean.old;

import com.lom.futures.bot.strategy.config.old.OpenCloseConfig;
import com.lom.futures.enums.Symbol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenCloseBeanConfig {

    @Bean
    public OpenCloseConfig configBTCUSDT() {
        return new OpenCloseConfig(
                Symbol.BTCUSDT,
                0.4,
                0.15,
                0.1,
                0.1,
                0.001,
                0.01,
                0.1);
    }

    @Bean
    public OpenCloseConfig configETHUSDT() {
        return new OpenCloseConfig(
                Symbol.ETHUSDT,
                0.2,
                0.3,
                0.0,
                0.2,
                0.03,
                0.5,
                0.1);
    }

}
