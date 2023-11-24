package com.lom.futures.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.db.entity.Klin;
import com.lom.futures.db.repository.KlinRepository;
import com.lom.futures.dto.FuturesBalance;
import com.lom.futures.dto.Kline;
import com.lom.futures.enums.Interval;
import com.lom.futures.enums.Symbol;
import com.lom.futures.service.AccountService;
import com.lom.futures.service.impl.MarketServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class Statistics {

    final AccountService accountService;
    final MarketServiceImpl marketService;
    final KlinRepository klinRepository;

//    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 1)
    public void getStatistics() {

        var timestamp = Instant.ofEpochMilli(1655672400000L); // 2022-06-20 00:00:00
        List<Kline> klines = new LinkedList<>();
        var instantNew = Instant.now().toEpochMilli();
        var startTime = timestamp.toEpochMilli();
        while (instantNew > startTime) {
            var openTimeStr = timestamp.toString();
            timestamp = timestamp.plus(1, ChronoUnit.HOURS);
            var endTime = timestamp.toEpochMilli();
            klines = getKlineList(Symbol.ETHUSDT, Interval._1m, 100, startTime, endTime);
            for (var klinr : klines) {
                var entity = new Klin();
                entity.setInterval(Interval._1m.getValue());
                entity.setSymbol(Symbol.ETHUSDT.name());
                entity.setOpenTime(klinr.getOpenTime());
                entity.setOpenTimeStr(openTimeStr);
                entity.setOpen(klinr.getOpen());
                entity.setHigh(klinr.getHigh());
                entity.setLow(klinr.getLow());
                entity.setClose(klinr.getClose());
                entity.setVolume(klinr.getVolume());
                entity.setCloseTime(klinr.getCloseTime());
                entity.setQuoteAssetVolume(klinr.getQuoteAssetVolume());
                entity.setNumberOfTrades(klinr.getNumberOfTrades());
                entity.setTakerBuyBaseAssetVolume(klinr.getTakerBuyBaseAssetVolume());
                entity.setTakerBuyQuoteAssetVolume(klinr.getTakerBuyQuoteAssetVolume());
                klinRepository.save(entity);
            }
            log.info(timestamp.toString());
            startTime = timestamp.toEpochMilli();
        }

    }

    public List<Kline> getKlineList(Symbol symbol, Interval interval, Integer limit, Long startTime, Long endTime) {
        List<Kline> klines = new LinkedList<>();
        try {
            klines = marketService.klines(symbol, interval, limit, startTime, endTime);
        } catch (JsonProcessingException e) {
            System.out.println(e);
        }
        return klines;
    }


}
