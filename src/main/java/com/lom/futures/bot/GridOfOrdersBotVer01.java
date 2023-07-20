package com.lom.futures.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.bot.strategy.GridStrategy;
import com.lom.futures.bot.strategy.config.GridConfig;
import com.lom.futures.db.service.HistoryBetService;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.lom.futures.util.Math.findNumberFromPercentAndRound;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class GridOfOrdersBotVer01 extends GridStrategy {

    final AccountService accountService;
    final MarketService marketService;
    final HistoryBetService historyBetService;

    Map<Symbol, GridConfig> config;

    @Autowired
    public GridOfOrdersBotVer01(Map<Symbol,
            GridConfig> gridConfig,
                                AccountService accountService,
                                MarketService marketService,
                                HistoryBetService historyBetService) {
        this.config = gridConfig;
        this.accountService = accountService;
        this.marketService = marketService;
        this.historyBetService = historyBetService;
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 5)
    public void doMoney() {

        for (Symbol symbol : config.keySet()) {
            processing(symbol);
        }
    }

    private void processing(Symbol symbol) {
        var positions = getPosition(symbol);
        var positionLong = getPosition(positions, symbol, PositionSide.LONG);
        var positionShort = getPosition(positions, symbol, PositionSide.SHORT);
        var lastKline = getGetLastKline(symbol);
        var takeProfitLong = findNumberFromPercentAndRound(
                lastKline.getClose(), config.get(symbol).getTakeProfitLong(), 3, symbol);
        var takeProfitShort = findNumberFromPercentAndRound(
                lastKline.getClose(), config.get(symbol).getTakeProfitShort(), 3, symbol);

        try {
            if (lastKline.getOpen() < lastKline.getClose()) {
                if (positionLong.getPositionAmt() == 0.0) {
                    orderProcessing(symbol, PositionSide.LONG);
                    openPositionLong(symbol);
                    historyBetService.save(symbol, PositionSide.LONG, 1);
                } else {
                    var valueProfit = calculateTakeProfit(positionLong.getPositionAmt(),
                            config.get(symbol).getQuantity(),takeProfitLong);
                    log.info("LONG: " + symbol.name() + " " + positionLong.getMarkPrice()
                            + " < " + (positionLong.getEntryPrice() - valueProfit)
                            + " / " + valueProfit + " (" + takeProfitLong
                            + ") / " + config.get(symbol).getTakeProfitLong());
                    if (positionLong.getMarkPrice() < positionLong.getEntryPrice() - valueProfit) {
                        openPositionLong(symbol, positionLong);
                        orderProcessing(symbol, PositionSide.LONG);
                        historyBetService.save(symbol, PositionSide.LONG, -1);
                    }
                }
            }

            if (lastKline.getOpen() > lastKline.getClose()) {
                if (positionShort.getPositionAmt() == 0.0) {
                    orderProcessing(symbol, PositionSide.SHORT);
                    openPositionShort(symbol);
                    historyBetService.save(symbol, PositionSide.SHORT, 1);
                } else {
                    var valueProfit = calculateTakeProfit(java.lang.Math.abs(positionShort.getPositionAmt()),
                            config.get(symbol).getQuantity(), takeProfitShort);
                    log.info("SHORT: " + symbol.name() + " " + positionShort.getMarkPrice()
                            + " > " + (positionShort.getEntryPrice() + valueProfit)
                            + " / " + valueProfit + " (" + takeProfitShort
                            + ") / " + config.get(symbol).getTakeProfitShort());
                    if (positionShort.getMarkPrice() > positionShort.getEntryPrice() + valueProfit) {
                        openPositionShort(symbol, positionShort);
                        orderProcessing(symbol, PositionSide.SHORT);
                        historyBetService.save(symbol, PositionSide.SHORT, -1);
                    }
                }
            }

            positions = getPosition(symbol);
            positionLong = getPosition(positions, symbol, PositionSide.LONG);
            positionShort = getPosition(positions, symbol, PositionSide.SHORT);
            if (positionLong.getEntryPrice() != 0.0) {
                addOrdersForPositionsLong(symbol, positionLong, takeProfitLong);
            }
            if (positionShort.getEntryPrice() != 0.0) {
                addOrdersForPositionsShort(symbol, positionShort, takeProfitShort);
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
        });
    }

    private void openPositionLong(Symbol symbol) throws JsonProcessingException {
        log.info(symbol.name() + ". LONG action OPEN!");
        accountService.newOrderMarketLongOpen(symbol, config.get(symbol).getQuantity());
    }

    private void openPositionLong(Symbol symbol, Position position) throws JsonProcessingException {
        log.info(symbol.name() + ". LONG action OPEN!");
        accountService.newOrderMarketLongOpen(symbol, calculateQuantity(position, symbol));
    }

    private void openPositionShort(Symbol symbol) throws JsonProcessingException {
        log.info(symbol.name() + ". SHORT action OPEN!");
        accountService.newOrderMarketShortOpen(symbol, config.get(symbol).getQuantity());
    }

    private void openPositionShort(Symbol symbol, Position position) throws JsonProcessingException {
        log.info(symbol.name() + ". SHORT action OPEN!");
        accountService.newOrderMarketShortOpen(symbol, calculateQuantity(position, symbol));
    }

    public Double calculateQuantity(Position position, Symbol symbol) {
        var quantity = java.lang.Math.abs(Math.round(position.getPositionAmt() * 2, 2, symbol));
        if (actualQuantityAboveMax(symbol, position)){
            quantity = position.getPositionAmt();
        }
        return config.get(symbol).getQuantity() > quantity ? config.get(symbol).getQuantity() : quantity;
    }


    private void addOrdersForPositionsLong(Symbol symbol, Position position, Double takeProfit) throws JsonProcessingException {
        var ordersLong = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
        if (!isPresentOrder(ordersLong, symbol, OrderType.TAKE_PROFIT_MARKET, PositionSide.LONG)) {
            accountService.newOrderTakeProfitMarketLong(symbol,
                    actualQuantityAboveMax(symbol, position)
                            ? Math.round(position.getEntryPrice()
                            + (takeProfit / 2), 3, symbol)
                            : Math.round(position.getEntryPrice()
                            + takeProfit, 3, symbol));
        }
    }

    private void addOrdersForPositionsShort(Symbol symbol, Position position, Double takeProfit) throws JsonProcessingException {
        var ordersShort = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
        if (!isPresentOrder(ordersShort, symbol, OrderType.TAKE_PROFIT_MARKET, PositionSide.SHORT)) {
            accountService.newOrderTakeProfitMarketShort(symbol,
                    actualQuantityAboveMax(symbol, position)
                            ? Math.round(position.getEntryPrice()
                            - (takeProfit / 2), 3, symbol)
                            : Math.round(position.getEntryPrice()
                            - takeProfit, 3, symbol));
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

    public Double calculateTakeProfit(Double actualQuantity, Double firstQuantity, Double shift) {
        var quantity = actualQuantity <= firstQuantity ? actualQuantity : Math.round((actualQuantity / 2), 3);
        return Math.round(Math.round(quantity / firstQuantity, 0) * shift, 3);
    }

    public boolean actualQuantityAboveMax(Symbol symbol, Position position) {
        return config.get(symbol).getQuantityMax() < position.getPositionAmt();
    }

    public List<Kline> getKlineList(Symbol symbol, Interval interval, Integer limit) {
        List<Kline> klines = new LinkedList<>();
        try {
            klines = marketService.klines(symbol, interval, limit);
        } catch (JsonProcessingException e) {
            System.out.println(e);
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
