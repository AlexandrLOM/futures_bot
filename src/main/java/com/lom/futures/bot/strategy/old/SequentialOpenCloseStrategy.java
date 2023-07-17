package com.lom.futures.bot.strategy.old;

import com.lom.futures.bot.strategy.config.old.SequentialOpenCloseConfig;
import com.lom.futures.dto.Position;
import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Symbol;
import com.lom.futures.enums.bot.Action;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;

@Slf4j
@Getter
@ToString
public class SequentialOpenCloseStrategy {

    public SequentialOpenCloseConfig config;

    public Double stopLoss;
    private Double takeProfit;
    private Double zoneOfInsensitivity;

    public Position positionLong;
    public Position positionShort;

    private Double takeProfitLong = MAX_VALUE;
    private Double stopLossLong = MIN_VALUE;

    private Double takeProfitShort = MIN_VALUE;
    private Double stopLossShort = MAX_VALUE;

    private Double latestPrice;

    public SequentialOpenCloseStrategy(SequentialOpenCloseConfig config) {
        this.config = config;
    }

    public void init(List<Position> positions) {
        this.positionLong = getPosition(positions, config.getSymbol(), PositionSide.LONG);
        this.positionShort = getPosition(positions, config.getSymbol(), PositionSide.SHORT);

        latestPrice = positionLong.getMarkPrice();

        stopLoss = latestPrice / 100 * config.getPositionStopLossPercent();
        takeProfit = latestPrice / 100 * config.getPositionTakeProfitPercent();
        zoneOfInsensitivity = latestPrice / 100 * config.getZoneOfInsensitivityPercent();


        log.info(config.getSymbol().name() + ". init: " + this);
    }

    public void setParams(List<Position> positions) {

        this.positionLong = getPosition(positions, config.getSymbol(), PositionSide.LONG);
        this.positionShort = getPosition(positions, config.getSymbol(), PositionSide.SHORT);

        latestPrice = positionLong.getMarkPrice();

        log.info(config.getSymbol().name() + ". " + this);
    }

    private Position getPosition(List<Position> positions, Symbol symbol, PositionSide positionSide) {
        return positions.stream()
                .filter(position -> Objects.equals(symbol, position.getSymbol()))
                .filter(position -> Objects.equals(positionSide, position.getPositionSide()))
                .findFirst()
                .orElse(null);
    }

    public Action getAction() {
        if (positionLong.getEntryPrice() == 0.0 && positionShort.getEntryPrice() == 0.0) {
            return Action.OPEN_LONG;
        }

        if (positionLong.getEntryPrice() != 0.0 && positionShort.getEntryPrice() == 0.0) {

            if (takeProfitLong == MAX_VALUE) {
                takeProfitLong = positionLong.getEntryPrice() + takeProfit;
            }
            if (stopLossLong == MIN_VALUE) {
                stopLossLong = positionLong.getEntryPrice() - stopLoss;
            }
            log.info(config.getSymbol().name() + ". LONG. OPEN. latestPrice > takeProfitLong : " + latestPrice + " > " + takeProfitLong);
            if (latestPrice >= takeProfitLong) {
                stopLossLong = latestPrice + zoneOfInsensitivity;
            }
            log.info(config.getSymbol().name() + ". LONG. OPEN -> CLOSE. latestPrice < stopLossLong : " + latestPrice + " < " + stopLossLong);
            if (latestPrice < stopLossLong) {
                takeProfitLong = MAX_VALUE;
                stopLossLong = MIN_VALUE;
                if (stopLossLong == positionLong.getEntryPrice() - stopLoss) {
                    return Action.OPEN_SHORT_CLOSE_LONG_SL;
                } else {
                    return Action.OPEN_SHORT_CLOSE_LONG;
                }
            }
        }

        if (positionShort.getEntryPrice() != 0.0 && positionLong.getEntryPrice() == 0.0) {

            if (takeProfitShort == MIN_VALUE) {
                takeProfitShort = positionShort.getEntryPrice() - takeProfit;
            }
            if (stopLossShort == MAX_VALUE) {
                stopLossShort = positionShort.getEntryPrice() + stopLoss;
            }
            log.info(config.getSymbol().name() + ". SHORT. OPEN. latestPrice > takeProfitShort : " + latestPrice + " < " + takeProfitShort);
            if (latestPrice <= takeProfitShort) {
                stopLossShort = latestPrice - zoneOfInsensitivity;
            }
            log.info(config.getSymbol().name() + ". SHORT. OPEN -> CLOSE. latestPrice < stopLossShort : " + latestPrice + " > " + stopLossShort);
            if (latestPrice > stopLossLong) {
                takeProfitShort = MAX_VALUE;
                stopLossShort = MIN_VALUE;
                if (stopLossLong == positionShort.getEntryPrice() + stopLoss) {
                    return Action.OPEN_LONG_CLOSE_SHORT_SL;
                } else {
                    return Action.OPEN_LONG_CLOSE_SHORT;
                }
            }
        }
        return Action.WAIT;
    }
}

