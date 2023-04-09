package com.lom.futures.service;

import com.lom.futures.dto.IndexPriceKline;
import com.lom.futures.dto.Kline;
import com.lom.futures.dto.MarkPriceKline;
import com.lom.futures.enums.Interval;
import com.lom.futures.enums.Pair;
import com.lom.futures.enums.Symbol;

import java.util.List;

public interface MarketService {

    List<Kline> klines(Symbol symbol, Interval interval, Integer limit);

    List<MarkPriceKline> markPriceKlines(Symbol symbol, Interval interval, Integer limit);

    List<IndexPriceKline> indexPriceKlines(Pair pair, Interval interval, Integer limit);
}
