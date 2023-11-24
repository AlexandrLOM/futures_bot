package com.lom.futures.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.IndexPriceKline;
import com.lom.futures.dto.Kline;
import com.lom.futures.dto.MarkPriceKline;
import com.lom.futures.dto.OrderBook;
import com.lom.futures.enums.Interval;
import com.lom.futures.enums.Pair;
import com.lom.futures.enums.Symbol;

import java.util.List;

public interface MarketService {

    List<Kline> klines(Symbol symbol, Interval interval, Integer limit) throws JsonProcessingException;

    List<MarkPriceKline> markPriceKlines(Symbol symbol, Interval interval, Integer limit) throws JsonProcessingException;

    List<IndexPriceKline> indexPriceKlines(Pair pair, Interval interval, Integer limit) throws JsonProcessingException;

    OrderBook depth(Symbol symbol, Integer limit) throws JsonProcessingException;

    List<Kline> klines(Symbol symbol, Interval interval, Integer limit,
                       Long startTime, Long endTime) throws JsonProcessingException;
}
