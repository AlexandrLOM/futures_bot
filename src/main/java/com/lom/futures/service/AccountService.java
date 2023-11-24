package com.lom.futures.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.FuturesBalance;
import com.lom.futures.dto.NewOrder;
import com.lom.futures.dto.Order;
import com.lom.futures.dto.Position;
import com.lom.futures.enums.Symbol;

import java.util.LinkedList;
import java.util.List;

public interface AccountService {


    List<FuturesBalance> futuresBalance() throws JsonProcessingException;

    NewOrder newOrderMarketLongOpen(Symbol symbol, Double quantity) throws JsonProcessingException;

    NewOrder newOrderStopMarketLong(Symbol symbol, Double stopPrice) throws JsonProcessingException;

    NewOrder newOrderStopMarketLong(Symbol symbol, Double stopPrice, Double quantity) throws JsonProcessingException;

    NewOrder newOrderStopMarketBuyLong(Symbol symbol, Double stopPrice, Double quantity) throws JsonProcessingException;

    NewOrder newOrderTakeProfitMarketLong(Symbol symbol, Double stopPrice) throws JsonProcessingException;

    NewOrder newOrderTakeProfitMarketLong(Symbol symbol, Double stopPrice, Double quantity) throws JsonProcessingException;

    NewOrder newOrderTakeProfitMarketBuyLong(Symbol symbol, Double stopPrice, Double quantity) throws JsonProcessingException;

    NewOrder newOrderMarketLongClose(Symbol symbol, Double quantity) throws JsonProcessingException;

    NewOrder newOrderMarketShortOpen(Symbol symbol, Double quantity) throws JsonProcessingException;

    NewOrder newOrderMarketShortClose(Symbol symbol, Double quantity) throws JsonProcessingException;

    NewOrder newOrderStopMarketShort(Symbol symbol, Double stopPrice) throws JsonProcessingException;

    NewOrder newOrderStopMarketShort(Symbol symbol, Double stopPrice, Double quantity) throws JsonProcessingException;

    NewOrder newOrderStopMarketBuyShort(Symbol symbol, Double stopPrice, Double quantity) throws JsonProcessingException;

    NewOrder newOrderTakeProfitMarketShort(Symbol symbol, Double stopPrice) throws JsonProcessingException;

    NewOrder newOrderTakeProfitMarketShort(Symbol symbol, Double stopPrice, Double quantity) throws JsonProcessingException;

    NewOrder newOrderTakeProfitMarketBuyShort(Symbol symbol, Double stopPrice, Double quantity) throws JsonProcessingException;

    NewOrder queryOrder(Symbol symbol, Long orderId) throws JsonProcessingException;

    NewOrder queryOrder(Symbol symbol, String origClientOrderId) throws JsonProcessingException;

    LinkedList<Order> getAllOpenOrders(Symbol symbol, Long timestamp) throws JsonProcessingException;

    List<Position> positionInformation(Symbol symbol, Long timestamp) throws JsonProcessingException;

    String cancelOrder(Symbol symbol, Long orderId);

    public NewOrder newOrderStopLimitLong(Symbol symbol, Double stopPrice, Double price, Double quantity) throws JsonProcessingException;

    public NewOrder newOrderStopLimitShort(Symbol symbol, Double stopPrice, Double price, Double quantity) throws JsonProcessingException;
}