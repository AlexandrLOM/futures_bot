package com.lom.futures.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public final class PrivateConfig {

    @Value("${app.url.base.um}")
    private String umBaseUrl;
    @Value("${app.url.base.cm}")
    private String cmBaseUrl;

    @Value("${app.key.api}")
    private String apiKey;
    @Value("${app.key.secret}")
    private String secretKey;


}