package com.lom.futures.service.impl.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.Order;
import com.lom.futures.enums.Symbol;
import com.lom.futures.service.FuturesClientService;
import com.lom.futures.util.JsonObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class QueryOrder extends OrderService {

    public QueryOrder(FuturesClientService client, JsonObjectMapper jsonObjectMapper) {
        super(client, jsonObjectMapper);
    }

    public Order queryOrder(Symbol symbol, Long orderId) throws JsonProcessingException {
        return queryOrder(symbol, orderId, null);
    }

    public Order queryOrder(Symbol symbol, String origClientOrderId) throws JsonProcessingException {
        return queryOrder(symbol, null, origClientOrderId);
    }
}
