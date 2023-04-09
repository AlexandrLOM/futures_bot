package com.lom.futures.service.impl;


import com.binance.connector.futures.client.impl.UMFuturesClientImpl;
import com.binance.connector.futures.client.impl.um_futures.UMAccount;
import com.binance.connector.futures.client.impl.um_futures.UMMarket;
import com.lom.futures.service.FuturesClientService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class FuturesClientServiceImpl implements FuturesClientService {

    UMFuturesClientImpl client;

    @Override
    public UMAccount account() {
        return client.account();
    }

    @Override
    public UMMarket market() {
        return client.market();
    }

}
