package com.lom.futures.service.helpe;

import com.lom.futures.dto.IndexPriceKline;
import com.lom.futures.dto.Kline;
import com.lom.futures.dto.MarkPriceKline;

import java.util.ArrayList;

import static com.lom.futures.util.ConvertorUtil.*;

public abstract class MarketServiceHelper {

    public Kline getKline(ArrayList<String> klineRav) {
        var kline = Kline.builder();
        kline.openTime(getLong(klineRav.get(0)));
        kline.open(getDouble(klineRav.get(1)));
        kline.high(getDouble(klineRav.get(2)));
        kline.low(getDouble(klineRav.get(3)));
        kline.close(getDouble(klineRav.get(4)));
        kline.volume(getDouble(klineRav.get(5)));
        kline.closeTime(getLong(klineRav.get(6)));
        kline.quoteAssetVolume(getDouble(klineRav.get(7)));
        kline.numberOfTrades(getInteger(klineRav.get(8)));
        kline.takerBuyBaseAssetVolume(getDouble(klineRav.get(9)));
        kline.takerBuyQuoteAssetVolume(getDouble(klineRav.get(10)));

        return kline.build();
    }

    public MarkPriceKline getMarkPriceKline(ArrayList<String> klineRav) {
        var kline = MarkPriceKline.builder();
        kline.openTime(getLong(klineRav.get(0)));
        kline.open(getDouble(klineRav.get(1)));
        kline.high(getDouble(klineRav.get(2)));
        kline.low(getDouble(klineRav.get(3)));
        kline.close(getDouble(klineRav.get(4)));
        kline.closeTime(getLong(klineRav.get(6)));

        return kline.build();
    }

    public IndexPriceKline getIndexPriceKline(ArrayList<String> klineRav) {
        var kline = IndexPriceKline.builder();
        kline.openTime(getLong(klineRav.get(0)));
        kline.open(getDouble(klineRav.get(1)));
        kline.high(getDouble(klineRav.get(2)));
        kline.low(getDouble(klineRav.get(3)));
        kline.close(getDouble(klineRav.get(4)));
        kline.closeTime(getLong(klineRav.get(6)));

        return kline.build();
    }
}
