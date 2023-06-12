package com.lom.futures.bot.strategy.config.bean;

import com.lom.futures.bot.strategy.config.OrderBlockConfig;
import com.lom.futures.enums.Symbol;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OrderBlockBeanConfig {

    @Bean
    public OrderBlockConfig orderBlockConfig() {
        return new OrderBlockConfig(
                List.of(
                        Symbol.BNBUSDT,
                        Symbol.ETHUSDT,
//                        Symbol.DOGEUSDT
                        Symbol.BTCUSDT

//                        Symbol.XRPUSDT,
//                        Symbol.AGIXUSDT,
//                        Symbol.LTCUSDT,
//                        Symbol.MASKUSDT,
//                        Symbol.CFXUSDT,
//                        Symbol.INJUSDT,
//                        Symbol.RNDRUSDT,
//                        Symbol.ARBUSDT
                )
        );
    }

}
