package com.lom.futures.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lom.futures.dto.FuturesBalance;
import com.lom.futures.dto.NewOrder;
import com.lom.futures.dto.Order;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Component
public class JsonObjectMapper {

    ObjectMapper objectMapper;

    public LinkedList<ArrayList<String>> convertList(String jsonString) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, new TypeReference<LinkedList<ArrayList<String>>>() {
        });

    }

    public List<FuturesBalance> convertFuturesBalance(String jsonString) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, new TypeReference<List<FuturesBalance>>() {
        });
    }

    public NewOrder convertNewOrder(String jsonString) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, NewOrder.class);
    }

    public Order convertOrder(String jsonString) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, Order.class);
    }

    public LinkedList<Order> convertOrders(String jsonString) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, new TypeReference<LinkedList<Order>>() {
        });
    }

}
