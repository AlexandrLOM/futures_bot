package com.lom.futures.bot.strategy;

import com.lom.futures.bot.strategy.config.OrderBlockConfig;
import com.lom.futures.dto.OrderBook;
import com.lom.futures.enums.Symbol;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.lom.futures.util.Math.round;

@Slf4j
@Getter
@ToString
public class OrderBlockStrategy {

    public OrderBlockConfig config;
    Double present = 0.2;

    public OrderBlockStrategy(OrderBlockConfig config) {
        this.config = config;
    }

    public void analysisOrder(Symbol symbol, OrderBook orders) {

        analysisOrderMax(symbol, orders);
    }

    private void analysisOrderMax(Symbol symbol, OrderBook orders) {

        var bigOrdersAsks = orders.getAsks().stream()
                .filter(order -> (orders.getAsks().get(0).get(0) + (orders.getAsks().get(0).get(0) * present / 100)) > orders.getAsks().get(0).get(0))
                .sorted((order1, order2) -> Double.compare(order2.get(1), order1.get(1)))
                .limit(15)
                .sorted((order1, order2) -> Double.compare(order1.get(0), order2.get(0)))
                .limit(15)
                .toList();

        var bigOrdersAsksMax = orders.getAsks().stream()
                .filter(order -> (orders.getAsks().get(0).get(0) + (orders.getAsks().get(0).get(0) * present / 100)) > orders.getAsks().get(0).get(0))
                .sorted((order1, order2) -> Double.compare(order2.get(1), order1.get(1)))
                .limit(3)
                .sorted((order1, order2) -> Double.compare(order1.get(0), order2.get(0)))
                .limit(3)
                .toList();


        var bigOrdersBids = orders.getBids().stream()
                .filter(order -> (orders.getBids().get(0).get(0) - (orders.getBids().get(0).get(0) * present / 100)) < orders.getBids().get(0).get(0))
                .sorted((order1, order2) -> Double.compare(order2.get(1), order1.get(1)))
                .limit(15)
                .sorted((order1, order2) -> Double.compare(order2.get(0), order1.get(0)))
                .limit(15)
                .toList();

        var bigOrdersBidsMax = orders.getBids().stream()
                .filter(order -> (orders.getBids().get(0).get(0) - (orders.getBids().get(0).get(0) * present / 100)) < orders.getBids().get(0).get(0))
                .sorted((order1, order2) -> Double.compare(order2.get(1), order1.get(1)))
                .limit(3)
                .sorted((order1, order2) -> Double.compare(order2.get(0), order1.get(0)))
                .limit(3)
                .toList();


        List<List<Double>> ordersMax = new ArrayList<>();
        ordersMax.addAll(bigOrdersBidsMax);
        ordersMax.addAll(bigOrdersAsksMax);
        var orderMax = ordersMax.stream()
                .sorted((order1, order2) -> Double.compare(order2.get(1), order1.get(1)))
                .limit(1)
                .findFirst()
                .map(order -> order.get(1))
                .get();


        bigOrdersAsks = bigOrdersAsks.stream()
                .peek(order -> order.add(round((100.0 * order.get(1) / orderMax), 2)))
                .peek(order -> order.remove(1))
                .toList();

        bigOrdersBids = bigOrdersBids.stream()
                .peek(order -> order.add(round((100.0 * order.get(1) / orderMax), 2)))
                .peek(order -> order.remove(1))
                .toList();


        log.info("----" + symbol.name() + "------------------------------------------------");

        log.info(symbol.name() + ". bigOrdersAsksMax: " + bigOrdersAsksMax);
        log.info(symbol.name() + ". bigOrdersAsks: " + bigOrdersAsks);
        log.info(symbol.name() + ". bigOrdersBids: " + bigOrdersBids);
        log.info(symbol.name() + ". bigOrdersBidsMax: " + bigOrdersBidsMax);


    }

    private void analysisOrderInPresent(Symbol symbol, OrderBook orders) {
        var priseBid = orders.getBids().get(0).get(0);
        var priseAsk = orders.getAsks().get(0).get(0);
        var delta = orders.getBids().get(0).get(0) * present / 100;
        List<List<Double>> bids = new ArrayList<>();
        List<List<Double>> asks = new ArrayList<>();

        do {
            var priseBidDelta = priseBid - delta;
            var priseBidOld = priseBid;
            bids.add(List.of((round(priseBidDelta, 3)),
                    orders.getBids().stream()
                            .filter(order -> order.get(0) < priseBidOld)
                            .filter(order -> order.get(0) > (priseBidDelta))
                            .mapToDouble(order -> order.get(1))
                            .sum()
            ));
            priseBid = priseBid - delta;

            var priseAskDelta = priseAsk + delta;
            var priseAskOld = priseAsk;
            asks.add(List.of((round(priseAskDelta, 3)),
                    orders.getAsks().stream()
                            .filter(order -> order.get(0) > priseAskOld)
                            .filter(order -> order.get(0) < (priseAskDelta))
                            .mapToDouble(order -> order.get(1))
                            .sum()
            ));
            priseAsk = priseAsk + delta;

        } while (asks.get(asks.size() - 1).get(1) != 0.0
                || bids.get(bids.size() - 1).get(1) != 0.0);

        asks = asks.stream().limit(30).toList();
        bids = bids.stream().limit(30).toList();
        List<List<Double>> ordersMax = new ArrayList<>();
        ordersMax.addAll(bids);
        ordersMax.addAll(asks);
        var orderMax = ordersMax.stream()
                .mapToDouble(order -> order.get(1))
                .max()
                .getAsDouble();

        List<List<Double>> asksPresent = new ArrayList<>();
        List<List<Double>> bidsPresent = new ArrayList<>();

        asks.forEach(order -> asksPresent.add(List.of(
                order.get(0),
                round((100.0 * order.get(1) / orderMax), 3)))
        );

        bids.forEach(order -> bidsPresent.add(List.of(
                order.get(0),
                round((100.0 * order.get(1) / orderMax), 3)))
        );


        log.info("----" + symbol.name() + "------------------------------------------------");

        log.info(symbol.name() + ". asks: " + asksPresent);
        log.info(symbol.name() + ". bids: " + bidsPresent);

    }
}

