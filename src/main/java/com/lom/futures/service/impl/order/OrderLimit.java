package com.lom.futures.service.impl.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.NewOrder;
import com.lom.futures.enums.*;
import com.lom.futures.service.FuturesClientService;
import com.lom.futures.util.JsonObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderLimit extends OrderService {

    public OrderLimit(FuturesClientService client, JsonObjectMapper jsonObjectMapper) {
        super(client, jsonObjectMapper);
    }

    public NewOrder newOrder(Symbol symbol,
                             Side side,
                             PositionSide positionSide,
                             Double price,
                             Double quantity) throws JsonProcessingException {
        return newOrder(symbol, side, positionSide, OrderType.LIMIT, quantity, price, null, null, TimeInForce.GTC);
    }


    public NewOrder newOrderLimitLong(Symbol symbol, Side side, Double price, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, side, PositionSide.LONG, price, quantity);
    }

    public NewOrder newOrderLimitLongOpen(Symbol symbol, Double price, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.BUY, PositionSide.LONG, price, quantity);
    }

    public NewOrder newOrderLimitLongClose(Symbol symbol, Double price, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.SELL, PositionSide.LONG, price, quantity);
    }

    public NewOrder newOrderLimitSort(Symbol symbol, Side side, Double price, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, side, PositionSide.SHORT, price, quantity);
    }


    public NewOrder newOrderLimitShortOpen(Symbol symbol, Double price, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.SELL, PositionSide.SHORT, price, quantity);
    }


    public NewOrder newOrderLimitShortClose(Symbol symbol, Double price, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.BUY, PositionSide.SHORT, price, quantity);
    }

}
