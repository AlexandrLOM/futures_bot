package com.lom.futures.service.impl.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.NewOrder;
import com.lom.futures.enums.*;
import com.lom.futures.service.FuturesClientService;
import com.lom.futures.util.JsonObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderStopMarket extends OrderService {

    public OrderStopMarket(FuturesClientService client, JsonObjectMapper jsonObjectMapper) {
        super(client, jsonObjectMapper);
    }

    public NewOrder newOrder(Symbol symbol,
                             Side side,
                             PositionSide positionSide,
                             Double stopPrice) throws JsonProcessingException {
        return newOrder(symbol, side, positionSide, OrderType.STOP_MARKET, null, null, true, stopPrice, TimeInForce.GTC);
    }

    public NewOrder newOrderStopMarketLong(Symbol symbol, Double stopPrice) throws JsonProcessingException {
        return newOrder(symbol, Side.SELL, PositionSide.LONG, stopPrice);
    }

    public NewOrder newOrderStopMarketShort(Symbol symbol, Double stopPrice) throws JsonProcessingException {
        return newOrder(symbol, Side.BUY, PositionSide.SHORT, stopPrice);
    }
}
