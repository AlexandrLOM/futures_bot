package com.lom.futures.bot.old;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.bot.strategy.old.BreakdownStrategy;
import com.lom.futures.dto.Kline;
import com.lom.futures.dto.context.ContextBot;
import com.lom.futures.enums.Symbol;
import com.lom.futures.service.AccountService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class BtcUsdtBotVer05 extends BreakdownStrategy {

    final AccountService accountService;

    final static Double QUANTITY = 0.001;

    @Override
    public void init(List<Kline> klineList) {
        super.init(klineList);
    }

//    @Override
    public void doMoney(ContextBot context) {

        setParams(context.getPositions(), context.getOrderBook());

        var actionForLong = getActionForLong();
        try {
            switch (actionForLong) {
                case OPEN_LONG -> {
                    log.info("BTCUSDT. LONG action OPEN!");
                    accountService.newOrderMarketLongOpen(Symbol.BTCUSDT, QUANTITY);
                }
                case CLOSE_LONG -> {
                    log.info("BTCUSDT. LONG action CLOSE!");
                    accountService.newOrderMarketLongClose(Symbol.BTCUSDT, QUANTITY);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var actionForShort = getActionForShort();
        try {
            switch (actionForShort) {
                case OPEN_SHORT -> {
                    log.info("BTCUSDT. SHORT action OPEN!");
                    accountService.newOrderMarketShortOpen(Symbol.BTCUSDT, QUANTITY);
                }
                case CLOSE_SHORT -> {
                    log.info("BTCUSDT. SHORT action CLOSE!");
                    accountService.newOrderMarketShortClose(Symbol.BTCUSDT, QUANTITY);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
