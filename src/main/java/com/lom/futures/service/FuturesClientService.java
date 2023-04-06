package com.lom.futures.service;

import com.binance.connector.futures.client.impl.um_futures.UMAccount;
import com.binance.connector.futures.client.impl.um_futures.UMMarket;

public interface FuturesClientService {


    UMAccount account();

    UMMarket market();
}
