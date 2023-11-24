package com.lom.futures.bot;

import com.lom.futures.db.entity.Klin;
import com.lom.futures.db.repository.KlinRepository;
import com.lom.futures.util.Math;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class Analise04 {

    final KlinRepository klinRepository;

//    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 1)
    public void doAnalise() {
        var walletLong = 0D;
        var walletShort = 0D;
        Double betLong = null;
        Double betShort = null;
        var betUsdtLong = 0D;
        var usdtLongProfit = 0D;
        var betUsdtShort = 0D;
        var usdtShortProfit = 0D;
        var betUsdtMax = 0.16;
        Double differencesLong = null;
        Double differencesShort = null;
        Double percent = 0.2;
        Double takeProfitLong = null;
        Double stopLoseLong = null;
        Double takeProfitShort = null;
        Double stopLoseShort = null;

//        var start = 1655672400000L;
        var start = 1688158800000L;
        var step = 29940000;
        var end = start + step;
        var lkines = klinRepository.findAllByOpenTimeBetween(start, end)
                .stream()
                .sorted(Comparator.comparing(Klin::getOpenTime))
                .collect(Collectors.toList());

        Long time = 0L;
        while (!lkines.isEmpty()) {

            for (var klin : lkines) {
                if (time > klin.getOpenTime()) {
                    log.warn(" ERROR !!! " + time + " > " + klin.getOpenTime());
                }
                time = klin.getOpenTime();

                // LONG
                if (Objects.isNull(betLong)) {
                    betUsdtLong = 0.015;
                    usdtLongProfit = 0.015;
                    walletLong = walletLong - betUsdtLong - Math.round(((betUsdtLong / 3) * 2), 3);
                    betLong = klin.getOpen();
                    differencesLong = Math.round((betLong / 100) * percent, 3);
                    takeProfitLong = Math.round(betLong + differencesLong, 3);
                    stopLoseLong = Math.round(betLong - differencesLong, 3);
                    log.info("LONG - " + walletLong + "   " + Instant.ofEpochMilli(klin.getOpenTime()).toString());
                } else {
                    if (stopLoseLong >= klin.getLow()) {
                        if (usdtLongProfit > betUsdtMax) {
                            betLong = null;
                            log.warn("LONG stop lose");
                        } else {
                            walletLong = walletLong - betUsdtLong - Math.round(((betUsdtLong / 3)* 2), 3);
                            usdtLongProfit = usdtLongProfit + betUsdtLong;
                            takeProfitLong = Math.round(takeProfitLong - (differencesLong / 2D), 3);
                            stopLoseLong = Math.round(stopLoseLong - (differencesLong), 3);
                            log.warn("LONG bet: " + usdtLongProfit);
                        }
                    } else {
                        if (takeProfitLong <= klin.getHigh()) {
                            walletLong = walletLong + usdtLongProfit * 2;
                            betLong = null;
                            log.warn("LONG tale profit");
                        }
                    }
                }

                // SHORT
                if (Objects.isNull(betShort)) {
                    betUsdtShort = 0.015;
                    usdtShortProfit = 0.015;
                    walletShort = walletShort - betUsdtShort - Math.round(((betUsdtShort / 3)* 2), 3);
                    betShort = klin.getOpen();
                    differencesShort = Math.round((betShort / 100) * percent, 3);
                    takeProfitShort = Math.round(betShort - differencesShort, 3);
                    stopLoseShort = Math.round(betShort + differencesShort, 3);
                    log.info("SHORT - " + walletShort + "   " + Instant.ofEpochMilli(klin.getOpenTime()).toString());
                } else {
                    if (stopLoseShort <= klin.getHigh()) {
                        if (usdtLongProfit > betUsdtMax) {
                            betShort = null;
                            log.warn("SHORT stop lose");
                        } else {
                            walletShort = walletShort - betUsdtShort - Math.round(((betUsdtLong / 3) * 2), 3);
                            usdtShortProfit = usdtShortProfit + betUsdtShort;
                            takeProfitShort = Math.round(takeProfitShort + (differencesShort / 2D), 3);
                            stopLoseShort = Math.round(stopLoseShort + (differencesShort), 3);
                            log.warn("SHORT bet: " + usdtShortProfit);
                        }
                    } else {
                        if (takeProfitShort >= klin.getLow()) {
                            walletShort = walletShort + usdtShortProfit * 2;
                            betShort = null;
                            log.warn("SHORT tale profit");
                        }
                    }
                }

            }


            start = end;
            end = start + step;

            lkines = klinRepository.findAllByOpenTimeBetween(
                            start, end)
                    .stream()
                    .sorted(Comparator.comparing(Klin::getOpenTime))
                    .collect(Collectors.toList());

        }
        log.info("FINISH");
        log.info("LONG - " + walletLong);
        log.info("SHORT - " + walletShort);
    }


}
