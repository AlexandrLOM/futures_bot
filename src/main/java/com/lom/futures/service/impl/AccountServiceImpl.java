package com.lom.futures.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.FuturesBalance;
import com.lom.futures.dto.NewOrder;
import com.lom.futures.dto.Order;
import com.lom.futures.dto.Position;
import com.lom.futures.enums.*;
import com.lom.futures.service.AccountService;
import com.lom.futures.service.FuturesClientService;
import com.lom.futures.service.helpe.AccountServiceHelper;
import com.lom.futures.service.impl.order.*;
import com.lom.futures.util.ClientParametersUtil;
import com.lom.futures.util.JsonObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class AccountServiceImpl extends AccountServiceHelper implements AccountService {

    FuturesClientService client;
    JsonObjectMapper jsonObjectMapper;


    OrderService orderService;
    OrderMarket orderMarket;
    OrderStopMarket orderStopMarket;
    OrderStopLimit orderStopLimit;
    OrderTakeProfitMarket orderTakeProfitMarket;
    QueryOrder queryOrder;
    PositionService positionService;

    @Override
    public List<FuturesBalance> futuresBalance() throws JsonProcessingException {
        var params = ClientParametersUtil.createEmptyParameters();
        var result = client.account().futuresAccountBalance(params);
        List<FuturesBalance> futuresBalances = jsonObjectMapper.convertFuturesBalance(result);

        return futuresBalances.stream()
                .filter(Objects::nonNull)
                .filter(futuresBalance -> futuresBalance.getUpdateTime() != 0L)
                .toList();
    }

    // newOrder

    public NewOrder newOrderMarketLong(Symbol symbol, Side side, Double quantity) throws JsonProcessingException {
        return orderMarket.newOrderMarketLong(symbol, side, quantity);
    }

    @Override
    public NewOrder newOrderMarketLongOpen(Symbol symbol, Double quantity) throws JsonProcessingException {
        return orderMarket.newOrderMarketLongOpen(symbol, quantity);
    }

    @Override
    public NewOrder newOrderMarketLongClose(Symbol symbol, Double quantity) throws JsonProcessingException {
        return orderMarket.newOrderMarketLongClose(symbol, quantity);
    }

    public NewOrder newOrderMarketSort(Symbol symbol, Side side, Double quantity) throws JsonProcessingException {
        return orderMarket.newOrderMarketSort(symbol, side, quantity);
    }

    @Override
    public NewOrder newOrderMarketShortOpen(Symbol symbol, Double quantity) throws JsonProcessingException {
        return orderMarket.newOrderMarketShortOpen(symbol, quantity);
    }

    @Override
    public NewOrder newOrderMarketShortClose(Symbol symbol, Double quantity) throws JsonProcessingException {
        return orderMarket.newOrderMarketShortClose(symbol, quantity);
    }

    // newOrderStopMarket

    @Override
    public NewOrder newOrderStopMarketLong(Symbol symbol, Double stopPrice) throws JsonProcessingException {
        return orderStopMarket.newOrderStopMarketLong(symbol, stopPrice);
    }

    @Override
    public NewOrder newOrderStopMarketLong(Symbol symbol, Double stopPrice, Double quantity) throws JsonProcessingException {
        return orderStopMarket.newOrderStopMarketLong(symbol, stopPrice, quantity);
    }

    @Override
    public NewOrder newOrderStopMarketBuyLong(Symbol symbol, Double stopPrice, Double quantity) throws JsonProcessingException {
        return orderStopMarket.newOrderStopMarketBuyLong(symbol, stopPrice, quantity);
    }

    @Override
    public NewOrder newOrderStopMarketShort(Symbol symbol, Double stopPrice, Double quantity) throws JsonProcessingException {
        return orderStopMarket.newOrderStopMarketShort(symbol, stopPrice, quantity);
    }

    @Override
    public NewOrder newOrderStopMarketBuyShort(Symbol symbol, Double stopPrice, Double quantity) throws JsonProcessingException {
        return orderStopMarket.newOrderStopMarketBuyShort(symbol, stopPrice, quantity);
    }

    @Override
    public NewOrder newOrderStopMarketShort(Symbol symbol, Double stopPrice) throws JsonProcessingException {
        return orderStopMarket.newOrderStopMarketShort(symbol, stopPrice);
    }

    // newOrderStopLimit

    @Override
    public NewOrder newOrderStopLimitLong(Symbol symbol, Double stopPrice, Double price, Double quantity) throws JsonProcessingException {
        return orderStopLimit.newOrderStopLimitLong(symbol, stopPrice, price, quantity);
    }

    @Override
    public NewOrder newOrderStopLimitShort(Symbol symbol, Double stopPrice, Double price, Double quantity) throws JsonProcessingException {
        return orderStopLimit.newOrderStopLimitShort(symbol, stopPrice, price, quantity);
    }

    // newOrderTakeProfitMarket

    @Override
    public NewOrder newOrderTakeProfitMarketLong(Symbol symbol, Double profitPrice) throws JsonProcessingException {
        return orderTakeProfitMarket.newOrderTakeProfitMarketLong(symbol, profitPrice);
    }

    @Override
    public NewOrder newOrderTakeProfitMarketShort(Symbol symbol, Double profitPrice) throws JsonProcessingException {
        return orderTakeProfitMarket.newOrderTakeProfitMarketShort(symbol, profitPrice);
    }

    @Override
    public NewOrder newOrderTakeProfitMarketLong(Symbol symbol, Double profitPrice, Double quantity) throws JsonProcessingException {
        return orderTakeProfitMarket.newOrderTakeProfitMarketLong(symbol, profitPrice, quantity);
    }

    @Override
    public NewOrder newOrderTakeProfitMarketShort(Symbol symbol, Double profitPrice, Double quantity) throws JsonProcessingException {
        return orderTakeProfitMarket.newOrderTakeProfitMarketShort(symbol, profitPrice, quantity);
    }


    @Override
    public NewOrder newOrderTakeProfitMarketBuyLong(Symbol symbol, Double profitPrice, Double quantity) throws JsonProcessingException {
        return orderTakeProfitMarket.newOrderTakeProfitMarketBuyLong(symbol, profitPrice, quantity);
    }

    @Override
    public NewOrder newOrderTakeProfitMarketBuyShort(Symbol symbol, Double profitPrice, Double quantity) throws JsonProcessingException {
        return orderTakeProfitMarket.newOrderTakeProfitMarketBuyShort(symbol, profitPrice, quantity);
    }


    // queryOrder

    @Override
    public Order queryOrder(Symbol symbol, Long orderId) throws JsonProcessingException {
        return queryOrder.queryOrder(symbol, orderId);
    }

    @Override
    public Order queryOrder(Symbol symbol, String origClientOrderId) throws JsonProcessingException {
        return queryOrder.queryOrder(symbol, origClientOrderId);
    }

    @Override
    public LinkedList<Order> getAllOpenOrders(Symbol symbol, Long timestamp) throws JsonProcessingException {
        return orderService.getAllOpenOrders(symbol, timestamp);
    }

    @Override
    public List<Position> positionInformation(Symbol symbol, Long timestamp)  throws JsonProcessingException {
        return positionService.positionInformation(symbol, timestamp);
    }

    @Override
    public String cancelOrder(Symbol symbol, Long orderId) {
        return orderService.cancelOrder(symbol, orderId, null);
    }

}
