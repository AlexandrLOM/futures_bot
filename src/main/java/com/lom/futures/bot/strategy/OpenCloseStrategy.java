package com.lom.futures.bot.strategy;

import com.lom.futures.bot.strategy.config.OpenCloseConfig;
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
public class OpenCloseStrategy {

    public OpenCloseConfig config;

    private Double zoneOfInsensitivity;
    public Double stopLoss;
    private Double takeProfit;

    private Double zoneOfInsensitivityOpenPosition;
    private Double zoneOfInsensitivityTakeProfit;

    public Position positionLong;
    public Position positionShot;

    private Double highLevel = MIN_VALUE;
    private Double lowLevel = MAX_VALUE;

    private Double openPositionLong = MAX_VALUE;
    private Double openPositionShort = MIN_VALUE;

    private Double takeProfitLong = MAX_VALUE;
    private Double takeProfitShort = MIN_VALUE;

    private Double latestPrice;

    public OpenCloseStrategy(OpenCloseConfig config) {
        this.config = config;
    }

    public void init(List<Position> positions) {
        this.positionLong = getPosition(positions, config.getSymbol(), PositionSide.LONG);
        this.positionShot = getPosition(positions, config.getSymbol(), PositionSide.SHORT);

        latestPrice = positionLong.getMarkPrice();

        zoneOfInsensitivity = latestPrice / 100 * config.getZoneOfInsensitivityPercent();
        stopLoss = latestPrice / 100 * config.getPositionStopLossPercent();
        takeProfit = latestPrice / 100 * config.getPositionTakeProfitPercent();
        zoneOfInsensitivityOpenPosition = latestPrice / 100 * config.getZoneOfInsensitivityOpenPositionPercent();
        zoneOfInsensitivityTakeProfit = latestPrice / 100 * config.getZoneOfInsensitivityTakeProfitPercent();

        log.info(config.getSymbol().name() + ". init: " + this);
    }

    public void setParams(List<Position> positions) {

        this.positionLong = getPosition(positions, config.getSymbol(), PositionSide.LONG);
        this.positionShot = getPosition(positions, config.getSymbol(), PositionSide.SHORT);

        latestPrice = positionLong.getMarkPrice();

        if (lowLevel > latestPrice) {
            lowLevel = latestPrice;
        }
        if (highLevel < latestPrice) {
            highLevel = latestPrice;
        }
        log.info(config.getSymbol().name() + ". " + this);
    }

    private Position getPosition(List<Position> positions, Symbol symbol, PositionSide positionSide) {
        return positions.stream()
                .filter(position -> Objects.equals(symbol, position.getSymbol()))
                .filter(position -> Objects.equals(positionSide, position.getPositionSide()))
                .findFirst()
                .orElse(null);
    }

    public Action getActionForLong() {
        if (positionLong.getEntryPrice() == 0L) {
            var triggerOpenPosition = highLevel - zoneOfInsensitivity;
            log.info(config.getSymbol().name() + ". LONG. OPEN. latestPrice < triggerOpenPosition : " + latestPrice + " < " + triggerOpenPosition);
            if (latestPrice < triggerOpenPosition) {
                openPositionLong = latestPrice + zoneOfInsensitivityOpenPosition;
            }
            if (openPositionLong != MAX_VALUE) {
                log.info(config.getSymbol().name() + ". LONG. OPEN. latestPrice > openPositionLong : " + latestPrice + " > " + openPositionLong);
                if (latestPrice > openPositionLong) {
                    openPositionLong = MAX_VALUE;
                    takeProfitLong = MAX_VALUE;
                    highLevel = MIN_VALUE;
                    return Action.OPEN_LONG;
                }
            }
        }
        if (positionLong.getEntryPrice() != 0L) {
            if (latestPrice > positionLong.getEntryPrice()) {
                var triggerTakeProfit = positionLong.getEntryPrice() + takeProfit;
                log.info(config.getSymbol().name() + ". LONG. CLOSE. latestPrice > triggerTakeProfit : " + latestPrice + " > " + triggerTakeProfit);
                if (latestPrice > triggerTakeProfit) {
                    takeProfitLong = latestPrice + zoneOfInsensitivityTakeProfit;
                }
                if (takeProfitLong != MAX_VALUE) {
                    log.info(config.getSymbol().name() + ". LONG. CLOSE. latestPrice < takeProfitLong : " + latestPrice + " < " + takeProfitLong);
                    if (latestPrice < takeProfitLong) {
                        return Action.CLOSE_LONG_TP;
                    }
                }
            }
            if (latestPrice < positionLong.getEntryPrice()) {
                Double triggerStopLoss;
                if (takeProfitLong != MAX_VALUE) {
                    triggerStopLoss = takeProfitLong;
                } else {
                    triggerStopLoss = positionLong.getEntryPrice() - stopLoss;
                }
                log.info(config.getSymbol().name() + ". LONG. CLOSE. latestPrice < triggerStopLoss : " + latestPrice + " < " + triggerStopLoss);
                if (latestPrice < triggerStopLoss) {
                    return Action.CLOSE_LONG_SL;
                }
            }
        }
        return Action.WAIT;
    }

    public Action getActionForShort() {
        if (positionShot.getEntryPrice() == 0L) {
            var triggerOpenPosition = lowLevel + zoneOfInsensitivity;
            log.info(config.getSymbol().name() + ". SHORT. OPEN. latestPrice > triggerOpenPosition : " + latestPrice + " > " + triggerOpenPosition);
            if (latestPrice > triggerOpenPosition) {
                openPositionShort = latestPrice - zoneOfInsensitivityOpenPosition;
            }
            if (openPositionShort != MIN_VALUE) {
                log.info(config.getSymbol().name() + ". SHORT. OPEN. latestPrice > openPositionShort : " + latestPrice + " < " + openPositionShort);
                if (latestPrice < openPositionShort) {
                    openPositionShort = MIN_VALUE;
                    takeProfitShort = MIN_VALUE;
                    lowLevel = MAX_VALUE;
                    return Action.OPEN_SHORT;
                }
            }
        }
        if (positionShot.getEntryPrice() != 0L) {
            if (latestPrice < positionShot.getEntryPrice()) {
                var triggerTakeProfit = positionShot.getEntryPrice() - takeProfit;
                log.info(config.getSymbol().name() + ". SHORT. CLOSE. latestPrice < triggerTakeProfit : " + latestPrice + " < " + triggerTakeProfit);
                if (latestPrice < triggerTakeProfit) {
                    takeProfitShort = latestPrice + zoneOfInsensitivityTakeProfit;
                }
                if (takeProfitShort != MIN_VALUE) {
                    log.info(config.getSymbol().name() + ". SHORT. CLOSE. latestPrice > takeProfitShort : " + latestPrice + " > " + takeProfitShort);
                    if (latestPrice > takeProfitShort) {
                        return Action.CLOSE_SHORT_TP;
                    }
                }
            }
            if (latestPrice > positionShot.getEntryPrice()) {
                Double triggerStopLoss;
                if (takeProfitShort != MIN_VALUE) {
                    triggerStopLoss = takeProfitShort;
                } else {
                    triggerStopLoss = positionShot.getEntryPrice() + stopLoss;
                }
                log.info(config.getSymbol().name() + ". SHORT. CLOSE. latestPrice > triggerStopLoss : " + latestPrice + " > " + triggerStopLoss);
                if (latestPrice > triggerStopLoss) {
                    return Action.CLOSE_SHORT_SL;
                }
            }
        }
        return Action.WAIT;
    }

}

