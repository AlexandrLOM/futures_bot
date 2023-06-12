package com.lom.futures.service.impl.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.NewOrder;
import com.lom.futures.enums.OrderType;
import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Side;
import com.lom.futures.enums.Symbol;
import com.lom.futures.service.FuturesClientService;
import com.lom.futures.util.JsonObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderMarket extends OrderService {

    public OrderMarket(FuturesClientService client, JsonObjectMapper jsonObjectMapper) {
        super(client, jsonObjectMapper);
    }

    public NewOrder newOrder(Symbol symbol,
                             Side side,
                             PositionSide positionSide,
                             OrderType type,
                             Double quantity) throws JsonProcessingException {
        return newOrder(symbol, side, positionSide, type, quantity, null, null, null, null);
    }

    public NewOrder newOrderMarketLong(Symbol symbol, Side side, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, side, PositionSide.LONG, OrderType.MARKET, quantity);
    }

    public NewOrder newOrderMarketLongOpen(Symbol symbol, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.BUY, PositionSide.LONG, OrderType.MARKET, quantity);
    }

    public NewOrder newOrderMarketLongClose(Symbol symbol, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.SELL, PositionSide.LONG, OrderType.MARKET, quantity);
    }

    public NewOrder newOrderMarketSort(Symbol symbol, Side side, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, side, PositionSide.SHORT, OrderType.MARKET, quantity);
    }


    public NewOrder newOrderMarketShortOpen(Symbol symbol, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.SELL, PositionSide.SHORT, OrderType.MARKET, quantity);
    }


    public NewOrder newOrderMarketShortClose(Symbol symbol, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.BUY, PositionSide.SHORT, OrderType.MARKET, quantity);
    }

}
