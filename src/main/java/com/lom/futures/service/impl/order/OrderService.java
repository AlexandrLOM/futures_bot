package com.lom.futures.service.impl.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.NewOrder;
import com.lom.futures.dto.Order;
import com.lom.futures.enums.*;
import com.lom.futures.service.FuturesClientService;
import com.lom.futures.service.impl.BasicAccountService;
import com.lom.futures.util.ClientParametersUtil;
import com.lom.futures.util.JsonObjectMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedList;
import java.util.Optional;

@Service
public class OrderService extends BasicAccountService {

    public OrderService(FuturesClientService client, JsonObjectMapper jsonObjectMapper) {
        super(client, jsonObjectMapper);
    }

    public NewOrder newOrder(Symbol symbol,
                             Side side,
                             PositionSide positionSide,
                             OrderType type,
                             Double quantity,
                             Double price,
                             Boolean closePosition,
                             Double stopPrice,
                             TimeInForce timeInForce) throws JsonProcessingException {
        var params = ClientParametersUtil.createEmptyParameters();

        params.put(Params.symbol.name(), symbol.name());
        params.put(Params.side.name(), side.name());
        Optional.ofNullable(positionSide).ifPresent(ps -> params.put(Params.positionSide.name(), ps));
        params.put(Params.type.name(), type.name());
        Optional.ofNullable(quantity).ifPresent(q -> params.put(Params.quantity.name(), q.toString()));
        Optional.ofNullable(price).ifPresent(p -> params.put(Params.price.name(), p.toString()));
        Optional.ofNullable(closePosition).ifPresent(cp -> params.put(Params.closePosition.name(), cp));
        Optional.ofNullable(stopPrice).ifPresent(sp -> params.put(Params.stopPrice.name(), sp.toString()));
        Optional.ofNullable(timeInForce).ifPresent(tif -> params.put(Params.timeInForce.name(), tif));
        params.put(Params.newOrderRespType.name(), NewOrderRespType.RESULT.name());

        var result = client.account().newOrder(params);
        return jsonObjectMapper.convertNewOrder(result);
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

    public LinkedList<Order> getAllOpenOrders(Symbol symbol, Long timestamp) throws JsonProcessingException {
        var params = ClientParametersUtil.createEmptyParameters();
        params.put(Params.symbol.name(), symbol.name());
        params.put(Params.timestamp.name(), timestamp);

        var result = client.account().currentAllOpenOrders(params);
        return jsonObjectMapper.convertOrders(result);

    }

    public String cancelOrder(Symbol symbol,
                            Long orderId,
                            String origClientOrderId) {
        var params = ClientParametersUtil.createEmptyParameters();
        params.put(Params.symbol.name(), symbol.name());
        Optional.ofNullable(orderId).ifPresent(oi -> params.put(Params.orderId.name(), oi));
        Optional.ofNullable(origClientOrderId).ifPresent(ocoi -> params.put(Params.origClientOrderId.name(), ocoi));
        params.put(Params.timestamp.name(), Instant.now().toEpochMilli());

        var result = client.account().cancelOrder(params);
        return result;
    }


}
