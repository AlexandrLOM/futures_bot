package com.lom.futures.service.impl.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.NewOrder;
import com.lom.futures.enums.*;
import com.lom.futures.service.FuturesClientService;
import com.lom.futures.util.JsonObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderTakeProfitMarket extends OrderService {

    public OrderTakeProfitMarket(FuturesClientService client, JsonObjectMapper jsonObjectMapper) {
        super(client, jsonObjectMapper);
    }

    public NewOrder newOrder(Symbol symbol,
                             Side side,
                             PositionSide positionSide,
                             Double profitPrice) throws JsonProcessingException {
        return newOrder(symbol, side, positionSide, OrderType.TAKE_PROFIT_MARKET, null, null, true, profitPrice, TimeInForce.GTC);
    }

    public NewOrder newOrderTakeProfitMarketLong(Symbol symbol, Double profitPrice) throws JsonProcessingException {
        return newOrder(symbol, Side.SELL, PositionSide.LONG, profitPrice);
    }

    public NewOrder newOrderTakeProfitMarketShort(Symbol symbol, Double profitPrice) throws JsonProcessingException {
        return newOrder(symbol, Side.BUY, PositionSide.SHORT, profitPrice);
    }
}
