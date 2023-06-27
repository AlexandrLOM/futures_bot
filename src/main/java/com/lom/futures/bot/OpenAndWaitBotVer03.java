package com.lom.futures.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.bot.strategy.OpenAndWaitStrategy;
import com.lom.futures.bot.strategy.config.OpenAndWaitConfig;
import com.lom.futures.db.service.HistoryBetService;
import com.lom.futures.dto.Position;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class OpenAndWaitBotVer03 extends OpenAndWaitStrategy {

    public final static UUID TAG = UUID.randomUUID();

    final AccountService accountService;
    final MarketService marketService;
    final HistoryBetService historyBetService;

    Map<Symbol, OpenAndWaitConfig> config;

    MapBet<Integer> mapBetHistory;
    MapBet<Integer> mapBetHistoryLast2;

    @Autowired
    public OpenAndWaitBotVer03(Map<Symbol,
            OpenAndWaitConfig> config,
                               AccountService accountService,
                               MarketService marketService,
                               HistoryBetService historyBetService) {
        this.config = config;
        this.accountService = accountService;
        this.marketService = marketService;
        this.historyBetService = historyBetService;

        mapBetHistory = new MapBet(config.keySet(), 100, 0);
        mapBetHistoryLast2 = new MapBet(config.keySet(), 2, 0);
    }

//    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 5)
    public void doMoney() {

        for (Symbol symbol : config.keySet()) {
            processing(symbol);
        }
    }

    private void processing(Symbol symbol) {
        var positions = getPosition(symbol);
        var positionLong = getPosition(positions, symbol, PositionSide.LONG);
        var positionShort = getPosition(positions, symbol, PositionSide.SHORT);

        try {
            if (positionLong.getEntryPrice() == 0.0) {
                orderProcessing(symbol, PositionSide.LONG);

                openPositionLong(symbol);

                positions = getPosition(symbol);
                positionLong = getPosition(positions, symbol, PositionSide.LONG);

            }
            if (positionLong.getEntryPrice() != 0.0) {
                addOrdersForPositionsLong(symbol, positionLong);
            }
        } catch (JsonProcessingException ex) {
            log.error(symbol.name());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

        try {
            if (positionShort.getEntryPrice() == 0.0) {
                orderProcessing(symbol, PositionSide.SHORT);

                openPositionShort(symbol);

                positions = getPosition(symbol);
                positionShort = getPosition(positions, symbol, PositionSide.SHORT);
            }
            if (positionShort.getEntryPrice() != 0.0) {
                addOrdersForPositionsShort(symbol, positionShort);
            }
        } catch (JsonProcessingException ex) {
            log.error(symbol.name());
            log.error(Arrays.toString(ex.getStackTrace()));
        }
    }

    private void orderProcessing(Symbol symbol, PositionSide positionSide) throws JsonProcessingException {
        var orders = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
        getOrders(orders, symbol, positionSide).forEach(order -> {
            accountService.cancelOrder(symbol, order.getOrderId());
            if (Objects.equals(OrderType.TAKE_PROFIT_MARKET, order.getOrigType())) {
                mapBetHistory.addBet(symbol, positionSide, -1);
                historyBetService.save(symbol, positionSide, -1);
            }
            if (Objects.equals(OrderType.STOP_MARKET, order.getOrigType())) {
                mapBetHistory.addBet(symbol, positionSide, 1);
                historyBetService.save(symbol, positionSide, 1);
            }
            log.info(symbol.name() + " " + " " + positionSide.name() + " - " + mapBetHistory.getBet(symbol, positionSide));
        });
    }

    private void openPositionLong(Symbol symbol) throws JsonProcessingException {
        log.info(symbol.name() + ". LONG action OPEN!");
        accountService.newOrderMarketLongOpen(symbol, getQuantity(symbol, PositionSide.LONG));
    }

    private void openPositionShort(Symbol symbol) throws JsonProcessingException {
        log.info(symbol.name() + ". SHORT action OPEN!");
        accountService.newOrderMarketShortOpen(symbol, getQuantity(symbol, PositionSide.SHORT));
    }

    private void addOrdersForPositionsLong(Symbol symbol, Position positionLong) throws JsonProcessingException {
        var ordersLong = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
        if (!isPresentOrder(ordersLong, symbol, OrderType.STOP_MARKET, PositionSide.LONG)) {
            accountService.newOrderStopMarketLong(symbol, Math.round(positionLong.getEntryPrice() - config.get(symbol).getPositionStopLossLong(), 3));
        }
        if (!isPresentOrder(ordersLong, symbol, OrderType.TAKE_PROFIT_MARKET, PositionSide.LONG)) {
            accountService.newOrderTakeProfitMarketLong(symbol, Math.round(positionLong.getEntryPrice() + config.get(symbol).getPositionTakeProfitLong(), 3));
        }
    }

    private void addOrdersForPositionsShort(Symbol symbol, Position positionShort) throws JsonProcessingException {
        var ordersShort = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());

        if (!isPresentOrder(ordersShort, symbol, OrderType.STOP_MARKET, PositionSide.SHORT)) {
            accountService.newOrderStopMarketShort(symbol, Math.round(positionShort.getEntryPrice() + config.get(symbol).getPositionStopLossShort(), 3));
        }
        if (!isPresentOrder(ordersShort, symbol, OrderType.TAKE_PROFIT_MARKET, PositionSide.SHORT)) {
            accountService.newOrderTakeProfitMarketShort(symbol, Math.round(positionShort.getEntryPrice() - config.get(symbol).getPositionTakeProfitShort(), 3));
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

    private Double getQuantity(Symbol symbol, PositionSide positionSide) {
        var lastBet = mapBetHistory.getLastBet(symbol, positionSide);
        var lastSecondBet = mapBetHistory.getLastTheEndBet(symbol, positionSide);

        if (lastBet == -1 && lastSecondBet == 1) {
            return Math.round(config.get(symbol).getQuantity() * 10, 3);
        }
        return config.get(symbol).getQuantity();
    }

}
