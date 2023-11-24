package com.lom.futures.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.dto.FuturesBalance;
import com.lom.futures.service.AccountService;
import com.lom.futures.service.impl.MarketServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class MainFutureBot {

    final AccountService accountService;
    final MarketServiceImpl marketService;

    List<FuturesBalance> futuresBalance;

//    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 60)
    public void checkBalance() {
        try {
            List<FuturesBalance> lastBalance;
            if (Objects.isNull(futuresBalance) || futuresBalance.isEmpty()) {
                futuresBalance = accountService.futuresBalance();
                lastBalance = futuresBalance;
            } else {
                lastBalance = accountService.futuresBalance();
            }
            log.info(lastBalance.toString());
            lastBalance.stream()
                    .filter(balance -> Objects.equals("USDT", balance.getAsset()))
                    .findFirst().ifPresent(balance -> {
                        futuresBalance.stream()
                                .filter(b -> Objects.equals("USDT", b.getAsset()))
                                .findFirst()
                                .ifPresent(usdtBalance -> {
                                    log.info("Balance USDT: " + (balance.getBalance() - usdtBalance.getBalance()));
                                });
                    });

            lastBalance.stream()
                    .filter(balance -> Objects.equals("BNB", balance.getAsset()))
                    .findFirst().ifPresent(balance -> {
                        futuresBalance.stream()
                                .filter(b -> Objects.equals("BNB", b.getAsset()))
                                .findFirst()
                                .ifPresent(bnbBalance -> {
                                    log.info("Balance BNB: " + (balance.getBalance() - bnbBalance.getBalance()));
                                });
                    });
        } catch (JsonProcessingException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

}
