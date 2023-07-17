package com.lom.futures.bot.strategy.old;

import com.lom.futures.bot.strategy.dto.Level;
import com.lom.futures.dto.Kline;
import com.lom.futures.dto.OrderBook;
import com.lom.futures.dto.Position;
import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Symbol;
import com.lom.futures.enums.bot.Action;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@ToString
public class BreakdownStrategy {

    private final Symbol symbol = Symbol.BTCUSDT;

    private Double deviationPercent = 0.001;
    private LinkedList<Level> highLevelList = new LinkedList<>();
    private LinkedList<Level> lowLevelList = new LinkedList<>();

    private List<List<Double>> bids = new ArrayList();
    private List<List<Double>> asks = new ArrayList();

    private Double positionStopLossPercent = 0.1;
    private Double positionTakeProfitPercent = 0.22;

    private Double deviationTakeProfitPercent = 0.02;

    public Double stopLoss;
    private Double takeProfit;

    private Double deviationTakeProfit;

    public Position positionLong;
    public Position positionShot;

    private Double takeProfitLong = Double.MAX_VALUE;
    private Double takeProfitShort = Double.MIN_VALUE;

    private Double latestPrice;

    public void init(List<Kline> klineList) {
        LinkedList<Kline> klineListSorted = klineList.stream()
                .sorted(Comparator.comparingLong(Kline::getCloseTime).reversed())
                .collect(Collectors.toCollection(LinkedList::new));

        LinkedList<Level> highLevelList = new LinkedList<>();
        LinkedList<Level> lowLevelList = new LinkedList<>();
        var highLevel = Double.MIN_VALUE;
        var lowLevel = Double.MAX_VALUE;

        for (Kline kline : klineListSorted) {
            if (highLevel <= kline.getHigh()) {
                highLevel = kline.getHigh();
                highLevelList.add(newLevel(kline.getHigh()));
            }
            if (lowLevel >= kline.getLow()) {
                lowLevel = kline.getLow();
                lowLevelList.add(newLevel(kline.getLow()));
            }
        }
        this.highLevelList = highLevelList.stream()
                .sorted(Comparator.comparingDouble(Level::getValue).reversed())
                .collect(Collectors.toCollection(LinkedList::new));
        this.lowLevelList = lowLevelList.stream()
                .sorted(Comparator.comparingDouble(Level::getValue).reversed())
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Level newLevel(Double value) {
        var newLevel = new Level();
        newLevel.setCount(1);
        newLevel.setValue(value);
        return newLevel;
    }

    public void setParams(List<Position> positions, OrderBook orderBook) {

        this.positionLong = positions.stream()
                .filter(position -> Objects.equals(symbol, position.getSymbol()))
                .filter(position -> Objects.equals(PositionSide.LONG, position.getPositionSide()))
                .findFirst()
                .orElse(null);

        this.positionShot = positions.stream()
                .filter(position -> Objects.equals(symbol, position.getSymbol()))
                .filter(position -> Objects.equals(PositionSide.SHORT, position.getPositionSide()))
                .findFirst()
                .orElse(null);

        latestPrice = positionLong.getMarkPrice();

        bids = orderBook.getBids().stream()
                .filter(order -> order.get(0) > (latestPrice - 5.0))
                .filter(order -> order.get(1) >= 5.00).toList();
        log.info("bids" + bids.toString());
        log.info("bids - " + orderBook.getBids().stream()
                .filter(order -> order.get(0) > (latestPrice - 5.0))
                .mapToDouble(value -> value.get(1)).sum());
        log.info("latestPrice " + latestPrice);
        asks = orderBook.getAsks().stream()
                .filter(order -> order.get(0) < latestPrice + 5.0)
                .filter(order -> order.get(1) >= 5.00).toList();
        log.info("asks - " + orderBook.getAsks().stream()
                .filter(order -> order.get(0) < (latestPrice + 5.0))
                .mapToDouble(value -> value.get(1)).sum());
        log.info("asks" + asks.toString());

//        log.info(this.toString());
    }

    public Action getActionForLong() {

        return Action.WAIT;
    }

    public Action getActionForShort() {

        return Action.WAIT;
    }

}

