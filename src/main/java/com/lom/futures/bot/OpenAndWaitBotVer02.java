package com.lom.futures.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.bot.strategy.OpenAndWaitStrategy;
import com.lom.futures.bot.strategy.config.OpenAndWaitConfig;
import com.lom.futures.dto.Kline;
import com.lom.futures.dto.Position;
import com.lom.futures.enums.Interval;
import com.lom.futures.enums.OrderType;
import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Symbol;
import com.lom.futures.service.AccountService;
import com.lom.futures.service.MarketService;
import com.lom.futures.storage.MapBet;
import com.lom.futures.util.Math;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class OpenAndWaitBotVer02 extends OpenAndWaitStrategy {

    final AccountService accountService;
    final MarketService marketService;

    Map<Symbol, OpenAndWaitConfig> config;

    MapBet mapBetHistory;

    @Autowired
    public OpenAndWaitBotVer02(Map<Symbol, OpenAndWaitConfig> config, AccountService accountService, MarketService marketService) {
        this.config = config;
        this.accountService = accountService;
        this.marketService = marketService;

        mapBetHistory = new MapBet(config.keySet(), 100);
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 5)
    public void doMoney() throws JsonProcessingException {

        for (Symbol symbol : config.keySet()) {
            var positions = getPosition(symbol);
            var positionLong = getPosition(positions, symbol, PositionSide.LONG);
            var positionShort = getPosition(positions, symbol, PositionSide.SHORT);


            if (positionLong.getEntryPrice() == 0.0) {
                orderProcessing(symbol, PositionSide.LONG);

//                var ordersLong = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
//                getOrders(ordersLong, symbol, PositionSide.LONG).forEach(order -> {
//                    accountService.cancelOrder(symbol, order.getOrderId());
//                    if (Objects.equals(OrderType.TAKE_PROFIT_MARKET, order.getOrigType())) {
//                        mapBetHistory.addBet(symbol, PositionSide.LONG, -1);
//                    }
//                    if (Objects.equals(OrderType.STOP_MARKET, order.getOrigType())) {
//                        mapBetHistory.addBet(symbol, PositionSide.LONG, 1);
//
//                    }
//                    log.info(symbol.name() + " " + " " + PositionSide.LONG.name() + " - " + mapBetHistory.getBet(symbol, PositionSide.LONG));
//                });

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
                        mapBetHistory.addBet(symbol, PositionSide.SHORT, -1);
                    }
                    if (Objects.equals(OrderType.STOP_MARKET, order.getOrigType())) {
                        mapBetHistory.addBet(symbol, PositionSide.SHORT, 1);
                    }
                    log.info(symbol.name() + " " + " " + PositionSide.SHORT.name() + " - " + mapBetHistory.getBet(symbol, PositionSide.SHORT));
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

    private void orderProcessing(Symbol symbol, PositionSide positionSide) throws JsonProcessingException {
        var ordersLong = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
        getOrders(ordersLong, symbol, PositionSide.LONG).forEach(order -> {
            accountService.cancelOrder(symbol, order.getOrderId());
            if (Objects.equals(OrderType.TAKE_PROFIT_MARKET, order.getOrigType())) {
                mapBetHistory.addBet(symbol, PositionSide.LONG, -1);
            }
            if (Objects.equals(OrderType.STOP_MARKET, order.getOrigType())) {
                mapBetHistory.addBet(symbol, PositionSide.LONG, 1);

            }
            log.info(symbol.name() + " " + " " + positionSide.name() + " - " + mapBetHistory.getBet(symbol, positionSide));
        });
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
        var lastBet = mapBetHistory.getLastBet(symbol, positionSide);

        if (lastBet == 1) {
            return Math.round(config.get(symbol).getQuantity() * 10, 3);
        }
        return config.get(symbol).getQuantity();
    }

}
