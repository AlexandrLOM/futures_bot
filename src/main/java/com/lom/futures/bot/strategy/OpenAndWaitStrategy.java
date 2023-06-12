package com.lom.futures.bot.strategy;

import com.lom.futures.dto.Order;
import com.lom.futures.dto.Position;
import com.lom.futures.enums.OrderType;
import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Symbol;
import com.lom.futures.enums.bot.Action;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Getter
@ToString
public class OpenAndWaitStrategy {

    public void init(List<Position> positions) {
    }

    public void setParams(List<Position> positions) {
    }

    public Position getPosition(List<Position> positions, Symbol symbol, PositionSide positionSide) {
        return positions.stream()
                .filter(position -> Objects.equals(symbol, position.getSymbol()))
                .filter(position -> Objects.equals(positionSide, position.getPositionSide()))
                .findFirst()
                .orElse(null);
    }

    public boolean isPresentOrder(List<Order> orders, Symbol symbol, OrderType orderType, PositionSide positionSide) {
        return orders.stream()
                .anyMatch(order -> Objects.equals(symbol, order.getSymbol())
                        && Objects.equals(orderType, order.getOrigType())
                        && Objects.equals(positionSide, order.getPositionSide()));
    }

    public Stream<Order> getOrders(List<Order> orders, Symbol symbol, PositionSide positionSide) {
        return orders.stream()
                .filter(order -> Objects.equals(symbol, order.getSymbol())
                        && Objects.equals(positionSide, order.getPositionSide()));
    }

    public Action getActionForLong() {

        return Action.WAIT;
    }

    public Action getActionForShort() {

        return Action.WAIT;
    }

}

