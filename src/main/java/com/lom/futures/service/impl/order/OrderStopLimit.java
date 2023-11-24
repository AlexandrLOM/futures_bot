package com.lom.futures.service.impl.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.NewOrder;
import com.lom.futures.enums.*;
import com.lom.futures.service.FuturesClientService;
import com.lom.futures.util.JsonObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderStopLimit extends OrderService {

    public OrderStopLimit(FuturesClientService client, JsonObjectMapper jsonObjectMapper) {
        super(client, jsonObjectMapper);
    }

    public NewOrder newOrder(Symbol symbol,
                             Side side,
                             PositionSide positionSide,
                             Double stopPrice,
                             Double price,
                             Double quantity) throws JsonProcessingException {
        return newOrder(symbol, side, positionSide, OrderType.STOP, quantity, price, false, stopPrice, TimeInForce.GTC);
    }

    public NewOrder newOrderStopLimitLong(Symbol symbol, Double stopPrice, Double price, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.SELL, PositionSide.LONG, stopPrice, price, quantity);
    }

    public NewOrder newOrderStopLimitShort(Symbol symbol, Double stopPrice, Double price, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.BUY, PositionSide.SHORT, stopPrice, price, quantity);
    }

}
