package com.lom.futures.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.FuturesBalance;
import com.lom.futures.service.impl.MarketServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class Bot {

    final AccountService accountService;
    final MarketServiceImpl marketService;

    List<FuturesBalance> futuresBalance;

    //    @PostConstruct
    public void start() throws JsonProcessingException {

//        var result = marketService.klines(Symbol.BNBUSDT, Interval._1d, 2);
//        System.out.println(result);
//        var result2 = accountService.newOrderMarketLongOpen(Symbol.BNBUSDT, 1D);
//        System.out.println(result2);
//        var result4 = accountService.queryOrder(Symbol.BNBUSDT, result2.getOrderId());
//        System.out.println(result4);
//        var result3 = accountService.newOrderMarketLongOpen(Symbol.BTCUSDT, 0.05D);
//        System.out.println(result3);
//        var result5 = accountService.queryOrder(Symbol.BNBUSDT, result3.getClientOrderId());
//        System.out.println(result5);

//        var result6 = accountService.newOrderMarketLongClos(Symbol.BNBUSDT, 1D);
//        System.out.println(result6);
//
//        var result7 = accountService.newOrderMarketSortClos(Symbol.BNBUSDT, 1D);
//        System.out.println(result7);

//        var result8 = accountService.getAllOpenOrders(Symbol.ETHUSDT);
//        System.out.println(result8);
//        try {
//            List<FuturesBalance> lastBalance;
//            if (Objects.isNull(futuresBalance) || futuresBalance.isEmpty()) {
//                futuresBalance = accountService.futuresBalance();
//                lastBalance = futuresBalance;
//                log.info(futuresBalance.toString());
//            } else {
//                lastBalance = accountService.futuresBalance();
//            }
//            log.info(lastBalance.toString());
//            lastBalance.stream()
//                    .filter(balance -> Objects.equals("USDT", balance.getAsset()))
//                    .findFirst().ifPresent(balance -> {
//                        futuresBalance.stream()
//                                .filter(b -> Objects.equals("USDT", b.getAsset()))
//                                .findFirst()
//                                .ifPresent(usdtBalance -> {
//                                    log.info("Balance USDT: " + (usdtBalance.getBalance() - balance.getBalance()));
//                                });
//                    });
//
//            lastBalance.stream()
//                    .filter(balance -> Objects.equals(Assert.BTC.name(), balance.getAsset()))
//                    .findFirst().ifPresent(balance -> {
//                        futuresBalance.stream()
//                                .filter(b -> Objects.equals(Assert.BTC.name(), b.getAsset()))
//                                .findFirst()
//                                .ifPresent(btsBalance -> {
//                                    log.info("Balance BTC: " + (btsBalance.getBalance() - balance.getBalance()));
//                                });
//                    });
//
//            lastBalance.stream()
//                    .filter(balance -> Objects.equals(Assert.BNB, balance.getAsset()))
//                    .findFirst().ifPresent(balance -> {
//                        futuresBalance.stream()
//                                .filter(b -> Objects.equals(Assert.BNB, b.getAsset()))
//                                .findFirst()
//                                .ifPresent(bnbBalance -> {
//                                    log.info("Balance BNB: " + (bnbBalance.getBalance() - balance.getBalance()));
//                                });
//                    });
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
    }
}
