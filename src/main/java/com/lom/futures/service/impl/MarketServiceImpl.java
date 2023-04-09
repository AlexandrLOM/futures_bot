package com.lom.futures.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.IndexPriceKline;
import com.lom.futures.dto.Kline;
import com.lom.futures.dto.MarkPriceKline;
import com.lom.futures.enums.Interval;
import com.lom.futures.enums.Pair;
import com.lom.futures.enums.Params;
import com.lom.futures.enums.Symbol;
import com.lom.futures.service.FuturesClientService;
import com.lom.futures.service.MarketService;
import com.lom.futures.service.helpe.MarketServiceHelper;
import com.lom.futures.util.ClientParametersUtil;
import com.lom.futures.util.JsonObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class MarketServiceImpl extends MarketServiceHelper implements MarketService {

    FuturesClientService client;
    JsonObjectMapper jsonObjectMapper;

    private LinkedList<ArrayList<String>> getKlines(Symbol symbol, Interval interval, Integer limit,
                                                    Function<LinkedHashMap<String, Object>, String> klinesFunction) {
        var params = ClientParametersUtil.createEmptyParameters();
        params.put(Params.symbol.name(), symbol.name());
        params.put(Params.interval.name(), interval.getValue());
        Optional.of(limit).ifPresent(l -> params.put(Params.limit.name(), l));
        var result = klinesFunction.apply(params);
        LinkedList<ArrayList<String>> list = new LinkedList<>();
        try {
            return jsonObjectMapper.convertList(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Kline> klines(Symbol symbol, Interval interval, Integer limit) {
        var list = getKlines(symbol, interval, limit, client.market()::klines);
        return list.stream().map(this::getKline).toList();
    }

    public List<MarkPriceKline> markPriceKlines(Symbol symbol, Interval interval, Integer limit) {
        var list = getKlines(symbol, interval, limit, client.market()::markPriceKlines);
        return list.stream().map(this::getMarkPriceKline).toList();
    }

    public List<IndexPriceKline> indexPriceKlines(Pair pair, Interval interval, Integer limit) {
        var params = ClientParametersUtil.createEmptyParameters();
        params.put(Params.pair.name(), pair.name());
        params.put(Params.interval.name(), interval.getValue());
        Optional.of(limit).ifPresent(l -> params.put(Params.limit.name(), l));
        var result = client.market().indexPriceKlines(params);
        LinkedList<ArrayList<String>> list = new LinkedList<>();
        try {
            list = jsonObjectMapper.convertList(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return list.stream().map(this::getIndexPriceKline).toList();
    }

}
