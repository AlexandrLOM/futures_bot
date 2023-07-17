package com.lom.futures.bot.old;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.bot.strategy.old.OpenAndWaitStrategy;
import com.lom.futures.bot.strategy.config.old.OpenAndWaitConfig;
import com.lom.futures.dto.Kline;
import com.lom.futures.dto.Position;
import com.lom.futures.enums.Interval;
import com.lom.futures.enums.OrderType;
import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Symbol;
import com.lom.futures.service.AccountService;
import com.lom.futures.service.MarketService;
import com.lom.futures.storage.AlgorithmBet;
import com.lom.futures.storage.MapBet;
import com.lom.futures.util.Math;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class OpenAndWaitBotVer01 extends OpenAndWaitStrategy {

    final AccountService accountService;
    final MarketService marketService;

    Map<Symbol, OpenAndWaitConfig> config;
    Map<Symbol, Double> quantitysLong = new HashMap<>();
    Map<Symbol, Double> quantitysShort = new HashMap<>();

    MapBet mapBet;

    @Autowired
    public OpenAndWaitBotVer01(Map<Symbol, OpenAndWaitConfig> config, AccountService accountService, MarketService marketService) {
        this.config = config;
        this.accountService = accountService;
        this.marketService = marketService;
        for (Symbol symbol : config.keySet()) {
            var positions = getPosition(symbol);
            var positionLong = getPosition(positions, symbol, PositionSide.LONG);
            var positionShort = getPosition(positions, symbol, PositionSide.SHORT);
            quantitysLong.put(symbol, positionLong.getPositionAmt() == 0.0 ? config.get(symbol).getQuantity() : java.lang.Math.abs(positionLong.getPositionAmt()));
            quantitysShort.put(symbol, positionShort.getPositionAmt() == 0.0 ? config.get(symbol).getQuantity() : java.lang.Math.abs(positionShort.getPositionAmt()));
        }
        log.info("quantitysLong: " + quantitysLong.toString());
        log.info("quantitysShort: " + quantitysShort.toString());

        mapBet = new MapBet(config.keySet(), 4, 0);
    }

//    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 5)
    public void doMoney() throws JsonProcessingException {

        for (Symbol symbol : config.keySet()) {
            var positions = getPosition(symbol);
            var positionLong = getPosition(positions, symbol, PositionSide.LONG);
            var positionShort = getPosition(positions, symbol, PositionSide.SHORT);

            if (positionLong.getEntryPrice() == 0.0) {
                var ordersLong = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
                getOrders(ordersLong, symbol, PositionSide.LONG).forEach(order -> {
                    accountService.cancelOrder(symbol, order.getOrderId());
                    if (Objects.equals(OrderType.TAKE_PROFIT_MARKET, order.getOrigType())) {
                        var quantity = quantitysLong.get(symbol);
                        quantitysLong.put(symbol, Math.round(quantity + (config.get(symbol).getQuantity() * 3.0), 3));
                        mapBet.addBet(symbol, PositionSide.LONG, -1);
                    }
                    if (Objects.equals(OrderType.STOP_MARKET, order.getOrigType())) {
                        quantitysLong.put(symbol, config.get(symbol).getQuantity());
                        mapBet.addBet(symbol, PositionSide.LONG, 1);
                    }
                    log.info(symbol.name() + " " + " " + PositionSide.LONG.name() + " - " + mapBet.getBet(symbol, PositionSide.LONG));
                });

                if (openLong(symbol)) {
                    log.info(symbol.name() + ". LONG action OPEN!");
                    accountService.newOrderMarketLongOpen(symbol, getQuantity(symbol, PositionSide.LONG));

                    positions = getPosition(symbol);
                    positionLong = getPosition(positions, symbol, PositionSide.LONG);

                }
            }
            if (positionLong.getEntryPrice() != 0.0) {
                var ordersLong = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
                if (!isPresentOrder(ordersLong, symbol, OrderType.STOP_MARKET, PositionSide.LONG)) {
                    accountService.newOrderStopMarketLong(symbol, Math.round(positionLong.getEntryPrice() - config.get(symbol).getPositionStopLossLong(), 3));
                }
                if (!isPresentOrder(ordersLong, symbol, OrderType.TAKE_PROFIT_MARKET, PositionSide.LONG)) {
                    accountService.newOrderTakeProfitMarketLong(symbol, Math.round(positionLong.getEntryPrice() + config.get(symbol).getPositionTakeProfitLong(), 3));
                }
            }


            if (positionShort.getEntryPrice() == 0.0) {
                var ordersShort = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
                getOrders(ordersShort, symbol, PositionSide.SHORT).forEach(order -> {
                    accountService.cancelOrder(symbol, order.getOrderId());
                    if (Objects.equals(OrderType.TAKE_PROFIT_MARKET, order.getOrigType())) {
                        var quantity = quantitysShort.get(symbol);
                        quantitysShort.put(symbol, Math.round(quantity + (config.get(symbol).getQuantity() * 3.0), 3));
                        mapBet.addBet(symbol, PositionSide.SHORT, -1);
                    }
                    if (Objects.equals(OrderType.STOP_MARKET, order.getOrigType())) {
                        quantitysShort.put(symbol, config.get(symbol).getQuantity());
                        mapBet.addBet(symbol, PositionSide.SHORT, 1);
                    }
                    log.info(symbol.name() + " " + " " + PositionSide.SHORT.name() + " - " + mapBet.getBet(symbol, PositionSide.SHORT));
                });

                if (openShort(symbol)) {
                    log.info(symbol.name() + ". SHORT action OPEN!");
                    accountService.newOrderMarketShortOpen(symbol, getQuantity(symbol, PositionSide.SHORT));

                    positions = getPosition(symbol);
                    positionShort = getPosition(positions, symbol, PositionSide.SHORT);

                }
            }
            if (positionShort.getEntryPrice() != 0.0) {
                var ordersShort = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());

                if (!isPresentOrder(ordersShort, symbol, OrderType.STOP_MARKET, PositionSide.SHORT)) {
                    accountService.newOrderStopMarketShort(symbol, Math.round(positionShort.getEntryPrice() + config.get(symbol).getPositionStopLossShort(), 3));
                }
                if (!isPresentOrder(ordersShort, symbol, OrderType.TAKE_PROFIT_MARKET, PositionSide.SHORT)) {
                    accountService.newOrderTakeProfitMarketShort(symbol, Math.round(positionShort.getEntryPrice() - config.get(symbol).getPositionTakeProfitShort(), 3));
                }
            }
        }
    }

    private List<Position> getPosition(Symbol symbol) {
        List<Position> positions = new LinkedList<>();
        try {
            return accountService.positionInformation(symbol, Instant.now().toEpochMilli());
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        return positions;
    }

    private Boolean openLong(Symbol symbol) {
        var kline = getKlines15m2limit(symbol).get(1);
        var kline2 = getKlines15m2limit(symbol).get(0);
//        log.info(symbol.name() + " openLong? High < Close " + kline2.getHigh() + " < " + kline.getClose());
//        log.info(symbol.name() + " openLong? Open < Close " + kline2.getOpen() + " < " + kline2.getClose());
//        log.info(symbol.name() + " openLong? Open < Close " + kline.getOpen() + " < " + kline.getClose());
        return (kline2.getHigh() < kline.getClose())
                || (kline2.getOpen() < kline2.getClose() && kline.getOpen() < kline.getClose());
    }

    private Boolean openShort(Symbol symbol) {
        var kline = getKlines15m2limit(symbol).get(1);
        var kline2 = getKlines15m2limit(symbol).get(0);
//        log.info(symbol.name() + " openShort? Low > Close " + kline2.getLow() + " > " + kline.getClose());
//        log.info(symbol.name() + " openShort? Open > Close " + kline2.getOpen() + " > " + kline2.getClose());
//        log.info(symbol.name() + " openShort? Open > Close " + kline.getOpen() + " > " + kline.getClose());
        return (kline2.getLow() > kline.getClose())
                || (kline2.getOpen() > kline2.getClose() && kline.getOpen() > kline.getClose());
    }

    private List<Kline> getKlines15m2limit(Symbol symbol) {
        List<Kline> klines = new LinkedList<>();
        try {
            klines = marketService.klines(symbol, Interval._15m, 2);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        return klines;
    }

    private Double getQuantity(Symbol symbol, PositionSide positionSide) {
        Double quantity = 0.0;
        switch (positionSide) {
            case LONG -> quantity = quantitysLong.get(symbol);
            case SHORT -> quantity = quantitysShort.get(symbol);
        }
        if (config.get(symbol).getQuantityMax() > quantity) {
            return quantity;
        }
        if(Objects.equals(AlgorithmBet.BET_HIGH, mapBet.getAlgorithm(symbol, positionSide))){
            return Math.round(config.get(symbol).getQuantity() * 8, 3);
        }
        return config.get(symbol).getQuantity();
    }

}
