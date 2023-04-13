package com.lom.futures.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.bot.strategy.ThreeKlinesStrategy;
import com.lom.futures.dto.context.ContextBot;
import com.lom.futures.enums.Symbol;
import com.lom.futures.service.AccountService;
import com.lom.futures.service.impl.MarketServiceImpl;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class EthUsdtBot extends ThreeKlinesStrategy implements Bot {

    final AccountService accountService;
    final MarketServiceImpl marketService;

    final static Double QUANTITY = 0.1;

    @Override
    public void doMoney(ContextBot context) {

        var isNewKlineParam = setNewKlineParam(context.getKlinesForEthUsdt3m3limit());

        if (isNewKlineParam) {
            try {
                actionLong();
            } catch (JsonProcessingException e) {
                log.error("ETHUSDT. Long action ERROR!");
                log.error(Arrays.toString(e.getStackTrace()));
            }
            try {
                actionShort();
            } catch (JsonProcessingException e) {
                log.error("ETHUSDT. Short action ERROR!");
                log.error(Arrays.toString(e.getStackTrace()));
            }


        }
    }

    private void actionLong() throws JsonProcessingException {
        switch (whatShouldDoForLong()) {
            case OPEN -> {
                log.info("ETHUSDT. Long action OPEN!");
                accountService.newOrderMarketLongOpen(Symbol.ETHUSDT, QUANTITY);
            }
            case CLOSE -> {
                log.info("ETHUSDT. Long action CLOSE!");
                accountService.newOrderMarketLongClose(Symbol.ETHUSDT, QUANTITY);
            }
        }
    }

    private void actionShort() throws JsonProcessingException {
        switch (whatShouldDoForShort()) {
            case OPEN -> {
                log.info("ETHUSDT. Short action OPEN!");
                accountService.newOrderMarketShortOpen(Symbol.ETHUSDT, QUANTITY);
            }
            case CLOSE -> {
                log.info("ETHUSDT. Short action CLOSE!");
                accountService.newOrderMarketShortClose(Symbol.ETHUSDT, QUANTITY);
            }
        }
    }
}
