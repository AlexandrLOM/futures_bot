package com.lom.futures.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.enums.Interval;
import com.lom.futures.enums.Symbol;
import com.lom.futures.service.impl.MarketServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class Bot {

    AccountService accountService;
    MarketServiceImpl marketService;

    @PostConstruct
    public void start() throws JsonProcessingException {

        var result = marketService.klines(Symbol.BNBUSDT, Interval._1d, 2);
        System.out.println(result);
//        var result2 = accountService.newOrderMarketLongOpen(Symbol.BNBUSDT, 1D);
//        System.out.println(result2);
//        var result4 = accountService.queryOrder(Symbol.BNBUSDT, result2.getOrderId());
//        System.out.println(result4);
        var result3 = accountService.newOrderMarketLongOpen(Symbol.BTCUSDT, 0.05D);
        System.out.println(result3);
//        var result5 = accountService.queryOrder(Symbol.BNBUSDT, result3.getClientOrderId());
//        System.out.println(result5);

//        var result6 = accountService.newOrderMarketLongClos(Symbol.BNBUSDT, 1D);
//        System.out.println(result6);
//
//        var result7 = accountService.newOrderMarketSortClos(Symbol.BNBUSDT, 1D);
//        System.out.println(result7);

        var result8 = accountService.getAllOpenOrders(Symbol.ETHUSDT);
        System.out.println(result8);
    }
}
