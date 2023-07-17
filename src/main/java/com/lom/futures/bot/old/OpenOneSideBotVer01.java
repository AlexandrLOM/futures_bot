package com.lom.futures.bot.old;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.bot.strategy.old.OpenOneSideStrategy;
import com.lom.futures.bot.strategy.config.old.OpenOneSideConfig;
import com.lom.futures.bot.strategy.dto.BetResult;
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
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class OpenOneSideBotVer01 extends OpenOneSideStrategy {

    public final static UUID TAG = UUID.randomUUID();
    public final static Random RANDOM = new Random();

    final AccountService accountService;
    final MarketService marketService;
    final HistoryBetService historyBetService;

    Map<Symbol, OpenOneSideConfig> config;

    MapBet<BetResult> mapBetHistory;
    MapBet<BetResult> mapBetHistoryLast2;

    Map<Symbol, Double> quantities = new HashMap<>();

    @Autowired
    public OpenOneSideBotVer01(Map<Symbol,
            OpenOneSideConfig> openOneSideConfig,
                               AccountService accountService,
                               MarketService marketService,
                               HistoryBetService historyBetService) {
        this.config = openOneSideConfig;
        this.accountService = accountService;
        this.marketService = marketService;
        this.historyBetService = historyBetService;

        for (Symbol symbol : config.keySet()) {
            var positions = getPosition(symbol);
            var positionLong = getPosition(positions, symbol, PositionSide.LONG);
            var positionShort = getPosition(positions, symbol, PositionSide.SHORT);
            quantities.put(symbol, config.get(symbol).getQuantity());
            if (positionLong.getPositionAmt() != 0.0 && positionShort.getPositionAmt() == 0.0) {
                quantities.put(symbol, java.lang.Math.abs(positionLong.getPositionAmt()));
            }
            if ((positionLong.getPositionAmt() == 0.0 && positionShort.getPositionAmt() != 0.0)) {
                quantities.put(symbol, java.lang.Math.abs(positionShort.getPositionAmt()));
            }
        }
        var defaultValue = BetResult.create(PositionSide.BOTH, 0);
        mapBetHistory = new MapBet<>(config.keySet(), 100, defaultValue);
        mapBetHistoryLast2 = new MapBet<>(config.keySet(), 2, defaultValue);
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
            if (positionLong.getPositionAmt() == 0.0 && positionShort.getPositionAmt() == 0.0) {
                orderProcessing(symbol);

                if (mapBetHistoryLast2.getLastBet(symbol, PositionSide.BOTH).getBet() == -1
                        && mapBetHistoryLast2.getLastTheEndBet(symbol, PositionSide.BOTH).getBet() == -1) {
                    if (Objects.equals(mapBetHistoryLast2.getLastBet(symbol, PositionSide.BOTH).getPositionSide(),
                            mapBetHistoryLast2.getLastTheEndBet(symbol, PositionSide.BOTH).getPositionSide())) {
                        switch (mapBetHistoryLast2.getLastBet(symbol, PositionSide.BOTH).getPositionSide()) {
                            case LONG -> openPositionShort(symbol);
                            case SHORT -> openPositionLong(symbol);
                        }
                    } else {
                        switch (mapBetHistoryLast2.getLastBet(symbol, PositionSide.BOTH).getPositionSide()) {
                            case LONG -> openPositionLong(symbol);
                            case SHORT -> openPositionShort(symbol);
                        }
                    }
                } else {
                    if (RANDOM.nextBoolean()) {
                        openPositionLong(symbol);
                    } else {
                        openPositionShort(symbol);
                    }
                }

//                if (mapBetHistoryLast2.getLastBet(symbol, PositionSide.BOTH).getBet() == -1
//                        && mapBetHistoryLast2.getLastTheEndBet(symbol, PositionSide.BOTH).getBet() == -1) {
//                    if (RANDOM.nextBoolean()) {
//                        openPositionLong(symbol);
//                    } else {
//                        openPositionShort(symbol);
//                    }
//                } else {
//                    if (PositionSide.LONG != mapBetHistoryLast2.getLastBet(symbol, PositionSide.BOTH).getPositionSide()) {
//                        openPositionLong(symbol);
//                    } else {
//                        openPositionShort(symbol);
//                    }
//                }
                positions = getPosition(symbol);
                positionLong = getPosition(positions, symbol, PositionSide.LONG);
                positionShort = getPosition(positions, symbol, PositionSide.SHORT);

            }
            if (positionLong.getEntryPrice() != 0.0) {
                addOrdersForPositionsLong(symbol, positionLong);
            }
            if (positionShort.getEntryPrice() != 0.0) {
                addOrdersForPositionsShort(symbol, positionShort);
            }

        } catch (JsonProcessingException ex) {
            log.error(symbol.name());
            log.error(Arrays.toString(ex.getStackTrace()));
        }

    }

    private void orderProcessing(Symbol symbol) throws JsonProcessingException {
        var orders = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
        getOrders(orders, symbol).forEach(order -> {
            accountService.cancelOrder(symbol, order.getOrderId());
            if (Objects.equals(OrderType.TAKE_PROFIT_MARKET, order.getOrigType())) {
                mapBetHistory.addBet(symbol, PositionSide.BOTH, BetResult.create(order.getPositionSide(), -1));
                mapBetHistoryLast2.addBet(symbol, PositionSide.BOTH, BetResult.create(order.getPositionSide(), -1));
                historyBetService.save(symbol, order.getPositionSide(), -1);
            }
            if (Objects.equals(OrderType.STOP_MARKET, order.getOrigType())) {
                mapBetHistory.addBet(symbol, PositionSide.BOTH, BetResult.create(order.getPositionSide(), 1));
                mapBetHistoryLast2.addBet(symbol, PositionSide.BOTH, BetResult.create(order.getPositionSide(), 1));
                historyBetService.save(symbol, order.getPositionSide(), 1);
            }
//            log.info(symbol.name() + ". " + " - " + mapBetHistory.getBet(symbol, PositionSide.BOTH));
        });
    }

    private void openPositionLong(Symbol symbol) throws JsonProcessingException {
        log.info(symbol.name() + ". LONG action OPEN!");
        accountService.newOrderMarketLongOpen(symbol, getQuantity(symbol));
    }

    private void openPositionShort(Symbol symbol) throws JsonProcessingException {
        log.info(symbol.name() + ". SHORT action OPEN!");
        accountService.newOrderMarketShortOpen(symbol, getQuantity(symbol));
    }

    private void addOrdersForPositionsLong(Symbol symbol, Position positionLong) throws JsonProcessingException {
        var ordersLong = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());
        if (!isPresentOrder(ordersLong, symbol, OrderType.STOP_MARKET, PositionSide.LONG)) {
            accountService.newOrderStopMarketLong(symbol, Math.round(positionLong.getEntryPrice() - config.get(symbol).getPositionStopLossLong(), 3, symbol));
        }
        if (!isPresentOrder(ordersLong, symbol, OrderType.TAKE_PROFIT_MARKET, PositionSide.LONG)) {
            accountService.newOrderTakeProfitMarketLong(symbol, Math.round(positionLong.getEntryPrice() + config.get(symbol).getPositionTakeProfitLong(), 3, symbol));
        }
    }

    private void addOrdersForPositionsShort(Symbol symbol, Position positionShort) throws JsonProcessingException {
        var ordersShort = accountService.getAllOpenOrders(symbol, Instant.now().toEpochMilli());

        if (!isPresentOrder(ordersShort, symbol, OrderType.STOP_MARKET, PositionSide.SHORT)) {
            accountService.newOrderStopMarketShort(symbol, Math.round(positionShort.getEntryPrice() + config.get(symbol).getPositionStopLossShort(), 3, symbol));
        }
        if (!isPresentOrder(ordersShort, symbol, OrderType.TAKE_PROFIT_MARKET, PositionSide.SHORT)) {
            accountService.newOrderTakeProfitMarketShort(symbol, Math.round(positionShort.getEntryPrice() - config.get(symbol).getPositionTakeProfitShort(), 3, symbol));
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

    private Double getQuantity(Symbol symbol) {
        var lastBet = mapBetHistory.getLastBet(symbol, PositionSide.BOTH).getBet();

        if (lastBet == -1) {
            var quantityCheck = Math.round((quantities.get(symbol) / 3 / 3), 3);
            if (config.get(symbol).getQuantity() >= quantityCheck) {
                quantities.put(symbol,
                        Math.round((quantities.get(symbol) * 3), 3));
            } else {
                quantities.put(symbol,
                        Math.round((quantities.get(symbol) * 2), 3));
            }
            log.info(symbol.name() + " Quantity: " + quantities.get(symbol));
            if (quantities.get(symbol) > config.get(symbol).getQuantityMax()) {
                quantities.put(symbol, config.get(symbol).getQuantity());
            }
            return quantities.get(symbol);
        }
        quantities.put(symbol, config.get(symbol).getQuantity());
        log.info(symbol.name() + " Quantity: " + quantities.get(symbol));
        return config.get(symbol).getQuantity();
    }

}
