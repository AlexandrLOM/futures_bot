package com.lom.futures.config;

import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeansConfig {

    @Bean
    public UMFuturesClientImpl FuturesClientServiceImpl(PrivateConfig privateConfig) {
        return new UMFuturesClientImpl(privateConfig.getApiKey(), privateConfig.getSecretKey());
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
