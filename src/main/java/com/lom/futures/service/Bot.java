package com.lom.futures.service;

import com.lom.futures.enums.Interval;
import com.lom.futures.enums.Pair;
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
    public void start() {

        var result = marketService.klines(Symbol.BTCUSDT, Interval._1d, 2);
        System.out.println(result);
        var result2 = marketService.markPriceKlines(Symbol.BTCUSDT, Interval._1d, 2);
        System.out.println(result2);
        var result3 = marketService.indexPriceKlines(Pair.BTCUSDT, Interval._1d, 2);
        System.out.println(result3);
    }
}
