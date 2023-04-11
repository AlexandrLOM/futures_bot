package com.lom.futures.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.FuturesBalance;
import com.lom.futures.dto.NewOrder;
import com.lom.futures.dto.Order;
import com.lom.futures.enums.*;
import com.lom.futures.service.AccountService;
import com.lom.futures.service.FuturesClientService;
import com.lom.futures.service.helpe.AccountServiceHelper;
import com.lom.futures.util.ClientParametersUtil;
import com.lom.futures.util.JsonObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class AccountServiceImpl extends AccountServiceHelper implements AccountService {

    FuturesClientService client;
    JsonObjectMapper jsonObjectMapper;

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

    public NewOrder newOrder(Symbol symbol,
                             Side side,
                             PositionSide positionSide,
                             OrderType type,
                             Double quantity,
                             Double price,
                             Double stopPrice) throws JsonProcessingException {
        var params = ClientParametersUtil.createEmptyParameters();

        params.put(Params.symbol.name(), symbol.name());
        params.put(Params.side.name(), side.name());
        Optional.ofNullable(positionSide).ifPresent(ps -> params.put(Params.positionSide.name(), ps));
        params.put(Params.type.name(), type.name());
        Optional.ofNullable(quantity).ifPresent(q -> params.put(Params.quantity.name(), q.toString()));
        Optional.ofNullable(price).ifPresent(p -> params.put(Params.price.name(), p));
        Optional.ofNullable(stopPrice).ifPresent(sp -> params.put(Params.stopPrice.name(), sp));
        params.put(Params.newOrderRespType.name(), NewOrderRespType.RESULT.name());

        var result = client.account().newOrder(params);
        return jsonObjectMapper.convertNewOrder(result);
    }

    public NewOrder newOrderMarketLong(Symbol symbol, Side side, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, side, PositionSide.LONG, OrderType.MARKET, quantity, null, null);
    }

    @Override
    public NewOrder newOrderMarketLongOpen(Symbol symbol, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.BUY, PositionSide.LONG, OrderType.MARKET, quantity, null, null);
    }

    @Override
    public NewOrder newOrderMarketLongClos(Symbol symbol, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.SELL, PositionSide.LONG, OrderType.MARKET, quantity, null, null);
    }

    public NewOrder newOrderMarketSort(Symbol symbol, Side side, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, side, PositionSide.SHORT, OrderType.MARKET, quantity, null, null);
    }

    @Override
    public NewOrder newOrderMarketSortOpen(Symbol symbol, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.SELL, PositionSide.SHORT, OrderType.MARKET, quantity, null, null);
    }

    @Override
    public NewOrder newOrderMarketSortClos(Symbol symbol, Double quantity) throws JsonProcessingException {
        return newOrder(symbol, Side.BUY, PositionSide.SHORT, OrderType.MARKET, quantity, null, null);
    }

    public Order queryOrder(Symbol symbol,
                            Long orderId,
                            String origClientOrderId) throws JsonProcessingException {
        var params = ClientParametersUtil.createEmptyParameters();
        params.put(Params.symbol.name(), symbol.name());
        Optional.ofNullable(orderId).ifPresent(oi -> params.put(Params.orderId.name(), oi));
        Optional.ofNullable(origClientOrderId).ifPresent(ocoi -> params.put(Params.origClientOrderId.name(), ocoi));
        params.put(Params.timestamp.name(), Instant.now().toEpochMilli());

        var result = client.account().queryOrder(params);
        return jsonObjectMapper.convertOrder(result);
    }

    @Override
    public Order queryOrder(Symbol symbol, Long orderId) throws JsonProcessingException {
        return queryOrder(symbol, orderId, null);
    }

    @Override
    public Order queryOrder(Symbol symbol, String origClientOrderId) throws JsonProcessingException {
        return queryOrder(symbol, null, origClientOrderId);
    }

    @Override
    public LinkedList<Order> getAllOpenOrders(Symbol symbol) throws JsonProcessingException {
        var params = ClientParametersUtil.createEmptyParameters();
        params.put(Params.symbol.name(), symbol.name());
        params.put(Params.timestamp.name(), Instant.now().toEpochMilli());

        var result = client.account().allOrders(params);
        return jsonObjectMapper.convertOrders(result);

    }


}
