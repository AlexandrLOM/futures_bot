package com.lom.futures.service.impl;

import com.lom.futures.config.PrivateConfig;
import com.lom.futures.dto.ServerTime;
import com.lom.futures.service.BinanceService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class BinanceServiceImpl implements BinanceService {

    RestTemplate restTemplate = new RestTemplate();
    PrivateConfig privateConfig;

    @Override
    public ServerTime getCurrentServerTime() {
        return restTemplate.getForObject(
                privateConfig.getUmBaseUrl().concat("/fapi/v1/time"),
                ServerTime.class);
    }

}
