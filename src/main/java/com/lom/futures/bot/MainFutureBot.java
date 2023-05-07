package com.lom.futures.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.FuturesBalance;
import com.lom.futures.dto.Kline;
import com.lom.futures.dto.OrderBook;
import com.lom.futures.dto.Position;
import com.lom.futures.dto.context.ContextBot;
import com.lom.futures.enums.Interval;
import com.lom.futures.enums.Symbol;
import com.lom.futures.service.AccountService;
import com.lom.futures.service.impl.MarketServiceImpl;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class MainFutureBot {

    final AccountService accountService;
    final MarketServiceImpl marketService;
    final List<Bot> list;

    List<FuturesBalance> futuresBalance;

    @PostConstruct
    public void init() {
        var context = getInitContext();
        list.forEach(bot -> bot.init(context));
    }

    @Scheduled(timeUnit = TimeUnit.SECONDS, fixedRate = 5)
    public void startDoMoney() {
        var context = getContext();
        list.forEach(bot -> bot.doMoney(context));
    }

    public ContextBot getInitContext() {
        var context = ContextBot.builder();
        context.positions(getPositions());
        return context.build();
    }

    public ContextBot getContext() {
        var context = ContextBot.builder();
        context.positions(getPositions());
//        context.orderBook(getOrderBook());
        return context.build();
    }

    private OrderBook getOrderBook() {
        try {
            return marketService.depth(Symbol.BTCUSDT, 1000);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        return new OrderBook();
    }

    public List<Kline> getKlinesForBtcUsdt5m1000limit() {
        List<Kline> klines = new LinkedList<>();
        try {
            klines = marketService.klines(Symbol.BTCUSDT, Interval._5m, 1000);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        return klines;
    }

    public List<Position> getPositions() {
        List<Position> positions = new LinkedList<>();
        try {
            positions.addAll(accountService.positionInformation(Symbol.BTCUSDT,
                    Instant.now().toEpochMilli()));
            positions.addAll(accountService.positionInformation(Symbol.ETHUSDT,
                    Instant.now().toEpochMilli()));
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        return positions;
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 1)
    public void checkBalance() {
        try {
            List<FuturesBalance> lastBalance;
            if (Objects.isNull(futuresBalance) || futuresBalance.isEmpty()) {
                futuresBalance = accountService.futuresBalance();
                lastBalance = futuresBalance;
            } else {
                lastBalance = accountService.futuresBalance();
            }
            log.info(lastBalance.toString());
            lastBalance.stream()
                    .filter(balance -> Objects.equals("USDT", balance.getAsset()))
                    .findFirst().ifPresent(balance -> {
                        futuresBalance.stream()
                                .filter(b -> Objects.equals("USDT", b.getAsset()))
                                .findFirst()
                                .ifPresent(usdtBalance -> {
                                    log.info("Balance USDT: " + (balance.getBalance() - usdtBalance.getBalance()));
                                });
                    });

            lastBalance.stream()
                    .filter(balance -> Objects.equals("BNB", balance.getAsset()))
                    .findFirst().ifPresent(balance -> {
                        futuresBalance.stream()
                                .filter(b -> Objects.equals("BNB", b.getAsset()))
                                .findFirst()
                                .ifPresent(bnbBalance -> {
                                    log.info("Balance BNB: " + (balance.getBalance() - bnbBalance.getBalance()));
                                });
                    });
        } catch (JsonProcessingException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

}
