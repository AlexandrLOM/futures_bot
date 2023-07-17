package com.lom.futures.bot.strategy.config.bean.old;

import com.lom.futures.bot.strategy.config.old.SequentialOpenCloseConfig;
import com.lom.futures.enums.Symbol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SequentialOpenCloseBeanConfig {

    @Bean
    public SequentialOpenCloseConfig sequentialConfigETHUSDT() {
        return new SequentialOpenCloseConfig(
                Symbol.ETHUSDT,
                0.2,
                0.1,
                0.0,
                0.03,
                0.5);
    }

}
