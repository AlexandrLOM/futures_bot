package com.lom.futures.service;

import com.lom.futures.dto.FuturesBalance;
import com.lom.futures.dto.NewOrder;
import com.lom.futures.dto.Order;
import com.lom.futures.enums.Symbol;

import java.util.LinkedList;
import java.util.List;

public interface AccountService {


    List<FuturesBalance> futuresBalance();

    NewOrder newOrderMarketLongOpen(Symbol symbol, Double quantity);

    NewOrder newOrderMarketLongClos(Symbol symbol, Double quantity);

    NewOrder newOrderMarketSortOpen(Symbol symbol, Double quantity);

    NewOrder newOrderMarketSortClos(Symbol symbol, Double quantity);

    NewOrder queryOrder(Symbol symbol, Long orderId);

    NewOrder queryOrder(Symbol symbol, String origClientOrderId);

    LinkedList<Order> getAllOpenOrders(Symbol symbol);

}
