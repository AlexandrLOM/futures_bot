package com.lom.futures.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.bot.strategy.GridStrategy;
import com.lom.futures.bot.strategy.config.GridConfig;
import com.lom.futures.dto.Kline;
import com.lom.futures.dto.Order;
import com.lom.futures.dto.Position;
import com.lom.futures.enums.Interval;
import com.lom.futures.enums.OrderType;
import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Symbol;
import com.lom.futures.service.AccountService;
import com.lom.futures.service.MarketService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.lom.futures.util.Math.findNumberFromPercentAndRound;
import static com.lom.futures.util.Math.round;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class GridOfOrdersBotVer02 extends GridStrategy {

    final AccountService accountService;
    final MarketService marketService;

    Map<Symbol, GridConfig> config;

    @Autowired
    public GridOfOrdersBotVer02(Map<Symbol, GridConfig> gridConfig,
                                AccountService accountService,
                                MarketService marketService) {
        this.config = gridConfig;
        this.accountService = accountService;
        this.marketService = marketService;
    }

//    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 3)
    public void doMoney() {

        for (Symbol symbol : config.keySet()) {
            processing(symbol);
        }
    }

    private void processing(Symbol symbol) {
        try {
            var positions = getPosition(symbol);
            var positionLong = getPosition(positions, symbol, PositionSide.LONG);
            var positionShort = getPosition(positions, symbol, PositionSide.SHORT);

            try {
                if (Objects.isNull(positionLong.getPositionAmt()) || positionLong.getPositionAmt() == 0.0) {
                    orderProcessingClean(symbol, PositionSide.LONG);
                    openPositionLong(symbol, Optional.ofNullable(positionLong.getPositionAmt()).orElse(0.0));
                    positions = getPosition(symbol);
                    positionLong = getPosition(positions, symbol, PositionSide.LONG);

                    orderProcessingLong(symbol, positionLong);
                }
            } catch (JsonProcessingException ex) {
                log.error(symbol.name());
                log.error(ex.getMessage());
                log.error(Arrays.toString(ex.getStackTrace()));
            }

            try {
                if (Objects.isNull(positionShort.getPositionAmt()) || positionShort.getPositionAmt() == 0.0) {
                    orderProcessingClean(symbol, PositionSide.SHORT);
                    openPositionShort(symbol, Optional.ofNullable(positionShort.getPositionAmt()).orElse(0.0));
                    positions = getPosition(symbol);
                    positionShort = getPosition(positions, symbol, PositionSide.SHORT);

                    orderProcessingShort(symbol, positionShort);
                }
            } catch (JsonProcessingException ex) {
                log.error(symbol.name());
                log.error(ex.getMessage());
                log.error(Arrays.toString(ex.getStackTrace()));
            }


            if (orderProcessingCheck(symbol, PositionSide.LONG)) {
                orderProcessingClean(symbol, PositionSide.LONG);
                orderProcessingLong(symbol, positionLong);
            }

            if (orderProcessingCheck(symbol, PositionSide.SHORT)) {
                orderProcessingClean(symbol, PositionSide.SHORT);
                orderProcessingShort(symbol, positionShort);
            }

        } catch (Exception ex) {
            log.error(symbol.name());
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

    }

    private void orderProcessingLong(Symbol symbol, Position positionLong) {
        try {
            var takeProfitLong = findNumberFromPercentAndRound(
                    positionLong.getEntryPrice(), config.get(symbol).getTakeProfitLong(), 3, symbol);

            accountService.newOrderTakeProfitMarketLong(symbol,
                    round((positionLong.getEntryPrice() + takeProfitLong), 3, symbol));

            var steps = 1;
            var quantityMax = config.get(symbol).getQuantityMax();
            var quantity = config.get(symbol).getQuantity() + 0;
            while (quantityMax > quantity) {
                quantity = quantity + config.get(symbol).getQuantity();
                accountService.newOrderTakeProfitMarketBuyLong(symbol,
                        round((positionLong.getEntryPrice() - takeProfitLong * steps), 3, symbol),
                        config.get(symbol).getQuantity());

                    accountService.newOrderStopLimitLong(symbol,
                            round((positionLong.getEntryPrice() - (takeProfitLong * steps)), 3, symbol),
                            round((positionLong.getEntryPrice() + takeProfitLong - ((takeProfitLong / 3) * steps)), 3, symbol),
                            config.get(symbol).getQuantity());

                steps = steps + 1;
            }

            accountService.newOrderStopMarketLong(symbol,
                    round((positionLong.getEntryPrice() - takeProfitLong * steps), 3, symbol));
        } catch (JsonProcessingException ex) {
            log.error(symbol.name());
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }
    }

    private void orderProcessingShort(Symbol symbol, Position positionShort) {
        try {
            var takeProfitShort = findNumberFromPercentAndRound(
                    positionShort.getEntryPrice(), config.get(symbol).getTakeProfitShort(), 3, symbol);

            accountService.newOrderTakeProfitMarketShort(symbol,
                    round((positionShort.getEntryPrice() - takeProfitShort), 3, symbol));

            var steps = 1;
            var quantityMax = config.get(symbol).getQuantityMax();
            var quantity = config.get(symbol).getQuantity() + 0;
            while (quantityMax > quantity) {
                quantity = quantity + config.get(symbol).getQuantity();
                accountService.newOrderTakeProfitMarketBuyShort(symbol,
                        round((positionShort.getEntryPrice() + takeProfitShort * steps), 3, symbol),
                        config.get(symbol).getQuantity());

                accountService.newOrderStopLimitShort(symbol,
                        round((positionShort.getEntryPrice() + (takeProfitShort * steps)), 3, symbol),
                        round((positionShort.getEntryPrice() - takeProfitShort + ((takeProfitShort / 3) * steps)), 3, symbol),
                        config.get(symbol).getQuantity());

                steps = steps + 1;
            }

            accountService.newOrderStopMarketShort(symbol,
                    round((positionShort.getEntryPrice() + takeProfitShort * steps), 3, symbol));
        } catch (JsonProcessingException ex) {
            log.error(symbol.name());
            log.error(ex.getMessage());
            log.error(Arrays.toString(ex.getStackTrace()));
        }
    }

    private void orderProcessingClean(Symbol symbol, PositionSide positionSide) throws JsonProcessingException {
        var orders = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
        getOrders(orders, symbol, positionSide).forEach(order -> {
            accountService.cancelOrder(symbol, order.getOrderId());
        });
    }

    private boolean orderProcessingCheck(Symbol symbol, PositionSide positionSide) throws JsonProcessingException {
        var orders = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
        return 2 != getOrders(orders, symbol, positionSide)
                .filter(Order::getClosePosition)
                .count();

    }

    private void openPositionLong(Symbol symbol) throws JsonProcessingException {
        log.info(symbol.name() + ". LONG action OPEN!");
        accountService.newOrderMarketLongOpen(symbol, config.get(symbol).getQuantity());
    }

    private void openPositionLong(Symbol symbol, Double quantity) throws JsonProcessingException {
        log.info(symbol.name() + ". LONG action OPEN!");
        var newAmount = round(java.lang.Math.abs(quantity / 3), 3, symbol);
        accountService.newOrderMarketLongOpen(symbol,
                newAmount > config.get(symbol).getQuantity() ? newAmount : config.get(symbol).getQuantity());
    }

    private void openPositionLong(Symbol symbol, Position position) throws JsonProcessingException {
        log.info(symbol.name() + ". LONG action OPEN!");
        accountService.newOrderMarketLongOpen(symbol, calculateQuantity(position, symbol));
    }

    private void openPositionShort(Symbol symbol) throws JsonProcessingException {
        log.info(symbol.name() + ". SHORT action OPEN!");
        accountService.newOrderMarketShortOpen(symbol, config.get(symbol).getQuantity());
    }

    private void openPositionShort(Symbol symbol, Double quantity) throws JsonProcessingException {
        log.info(symbol.name() + ". SHORT action OPEN!");
        var newAmount = round(java.lang.Math.abs(quantity / 3), 3, symbol);
        accountService.newOrderMarketShortOpen(symbol,
                newAmount > config.get(symbol).getQuantity() ? newAmount : config.get(symbol).getQuantity());
    }

    private void openPositionShort(Symbol symbol, Position position) throws JsonProcessingException {
        log.info(symbol.name() + ". SHORT action OPEN!");
        accountService.newOrderMarketShortOpen(symbol, calculateQuantity(position, symbol));
    }

    public Double calculateQuantity(Position position, Symbol symbol) {
        var quantity = java.lang.Math.abs(round(position.getPositionAmt() * 2, 2, symbol));
        if (actualQuantityAboveMax(symbol, position)) {
            quantity = position.getPositionAmt();
        }
        return config.get(symbol).getQuantity() > quantity ? config.get(symbol).getQuantity() : quantity;
    }


    private void addOrdersForPositionsLong(Symbol symbol, Position position, Double takeProfit) throws JsonProcessingException {
        var ordersLong = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
        if (!isPresentOrder(ordersLong, symbol, OrderType.TAKE_PROFIT_MARKET, PositionSide.LONG)) {
            accountService.newOrderTakeProfitMarketLong(symbol,
                    actualQuantityAboveMax(symbol, position)
                            ? round(position.getEntryPrice()
                            + (takeProfit / 2), 3, symbol)
                            : round(position.getEntryPrice()
                            + takeProfit, 3, symbol));
        }
    }

    private void addOrdersForPositionsShort(Symbol symbol, Position position, Double takeProfit) throws JsonProcessingException {
        var ordersShort = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
        if (!isPresentOrder(ordersShort, symbol, OrderType.TAKE_PROFIT_MARKET, PositionSide.SHORT)) {
            accountService.newOrderTakeProfitMarketShort(symbol,
                    actualQuantityAboveMax(symbol, position)
                            ? round(position.getEntryPrice()
                            - (takeProfit / 2), 3, symbol)
                            : round(position.getEntryPrice()
                            - takeProfit, 3, symbol));
        }
    }

    private List<Position> getPosition(Symbol symbol) {
        List<Position> positions = new LinkedList<>();
        try {
            return accountService.positionInformation(symbol, Instant.now().toEpochMilli());
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            log.warn(Arrays.toString(e.getStackTrace()));
        }
        return positions;
    }

    public Double calculateTakeProfit(Double actualQuantity, Double firstQuantity, Double shift) {
        var quantity = actualQuantity <= firstQuantity ? actualQuantity : round((actualQuantity / 2), 3);
        return round(round(quantity / firstQuantity, 0) * shift, 3);
    }

    public boolean actualQuantityAboveMax(Symbol symbol, Position position) {
        return config.get(symbol).getQuantityMax() < position.getPositionAmt();
    }

    public List<Kline> getKlineList(Symbol symbol, Interval interval, Integer limit) {
        List<Kline> klines = new LinkedList<>();
        try {
            klines = marketService.klines(symbol, interval, limit);
        } catch (JsonProcessingException e) {
            log.warn(e.getMessage());
            log.warn(Arrays.toString(e.getStackTrace()));
        }
        return klines;
    }

    public Kline getGetLastKline2(Symbol symbol) {
        return Optional.ofNullable(getKlineList(symbol, Interval._1h, 1).stream()
                        .findFirst()
                        .orElse(Kline.builder().build()))
                .get();
    }

    public Kline getGetLastKline(Symbol symbol) {
        var klines = getKlineList(symbol, Interval._30m, 2);
        return Kline.builder()
                .open(klines.get(0).getOpen())
                .close(klines.get(1).getClose())
                .build();
    }


}
