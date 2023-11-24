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
public class Analise {

    final KlinRepository klinRepository;

//    @Scheduled(timeUnit = TimeUnit.DAYS, fixedRate = 1)
    public void doAnalise() {
        var wallet = 100D;
        Double bet = null;
        var betUsdt = 0D;
        var betUsdtMax = 3;
        Double differences = null;
        Double percent = 0.3;
        Double takeProfit = null;
        Double stopLose = null;

//        var start = 1655672400000L;
        var start = 1688158800000L;
        var step = 29940000;
        var end = start + step;
        var lkines = klinRepository.findAllByOpenTimeBetween(start, end)
                .stream()
                .sorted(Comparator.comparing(Klin::getOpenTime))
                .collect(Collectors.toList());

//        wallet = wallet--;
//        bet = lkines.get(0).getOpen();
//        differences = Math.round((bet / 100) * percent, 3);
//        takeProfit = Math.round(bet + differences, 3);
//        stopLose = Math.round(bet - differences, 3);

        Long time = 0L;
        while (!lkines.isEmpty()) {
//            log.info("---");
            for (var klin : lkines) {
                if (time > klin.getOpenTime()) {
                    log.warn(" ERROR !!! " + time + " > " + klin.getOpenTime());
                }
                time = klin.getOpenTime();

//                log.info(Instant.ofEpochMilli(klin.getOpenTime()).toString());
                if (Objects.isNull(bet)) {
                    betUsdt = 1;
                    wallet = wallet - betUsdt - Math.round((betUsdt /100), 3);
                    bet = klin.getOpen();
                    differences = Math.round((bet / 100) * percent, 3);
                    takeProfit = Math.round(bet + differences, 3);
                    stopLose = Math.round(bet - differences, 3);
                    log.info(String.valueOf(wallet));
                    log.info(Instant.ofEpochMilli(klin.getOpenTime()).toString());
                } else {
                    if (stopLose >= klin.getLow()) {
                        if (betUsdt > betUsdtMax) {
                            bet = null;
                        } else {
                            wallet = wallet - betUsdt;
                            takeProfit = Math.round(takeProfit - (differences / 2D), 3);
                            stopLose = Math.round(stopLose - (differences / 2D), 3);
                            betUsdt = betUsdt + betUsdt;
                        }

                        continue;
                    }
                }
                if (takeProfit <= klin.getHigh()) {
                    wallet = wallet + betUsdt * 2;
                    bet = null;
                    continue;
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
    }


}
