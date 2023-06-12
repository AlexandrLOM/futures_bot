package com.lom.futures.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.bot.strategy.SequentialOpenCloseStrategy;
import com.lom.futures.bot.strategy.config.SequentialOpenCloseConfig;
import com.lom.futures.dto.context.ContextBot;
import com.lom.futures.service.AccountService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.lom.futures.util.Math.round;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class EthUsdtBotVer02 extends SequentialOpenCloseStrategy {
//        implements Bot {

    final AccountService accountService;

    Double quantity;

    @Autowired
    public EthUsdtBotVer02(SequentialOpenCloseConfig configETHUSDT, AccountService accountService) {
        super(configETHUSDT);
        this.accountService = accountService;
    }


    public void init(ContextBot context) {
        init(context.getPositions());
        var quantityLong = Objects.equals(positionLong.getEntryPrice(), 0D) ? config.getQuantity() : positionLong.getPositionAmt();
        var quantityShort = Objects.equals(positionShort.getEntryPrice(), 0D) ? config.getQuantity() : Math.abs(positionShort.getPositionAmt());
        quantity = quantityLong > quantityShort ? quantityLong : quantityShort;

        log.info(config.getSymbol().name() + ". init: " + this);
    }

//    @Override
    public void doMoney(ContextBot context) {

        setParams(context.getPositions());


        var action = getAction();
        try {
            switch (action) {
                case OPEN_LONG -> {
                    log.info(config.getSymbol().name() + ". LONG action OPEN!");
                    accountService.newOrderMarketLongOpen(config.getSymbol(), quantity);
                }
                case OPEN_SHORT_CLOSE_LONG -> {
                    log.info(config.getSymbol().name() + ". LONG action OPEN_SHORT_CLOSE_LONG!");
                    accountService.newOrderMarketLongClose(config.getSymbol(), quantity);
                    quantity = config.getQuantity();
                    accountService.newOrderMarketShortOpen(config.getSymbol(), quantity);
                }
                case OPEN_LONG_CLOSE_SHORT -> {
                    log.info(config.getSymbol().name() + ". LONG action OPEN_LONG_CLOSE_SHORT!");
                    accountService.newOrderMarketShortClose(config.getSymbol(), quantity);
                    quantity = config.getQuantity();
                    accountService.newOrderMarketLongOpen(config.getSymbol(), quantity);
                }
                case OPEN_SHORT_CLOSE_LONG_SL -> {
                    log.info(config.getSymbol().name() + ". LONG action OPEN_SHORT_CLOSE_LONG_SL!");
                    accountService.newOrderMarketLongClose(config.getSymbol(), quantity);
                    quantity = round((quantity * 2), 3);
                    if (quantity > config.getQuantityMax()) {
                        quantity = config.getQuantity();
                    }
                    accountService.newOrderMarketShortOpen(config.getSymbol(), quantity);
                }
                case OPEN_LONG_CLOSE_SHORT_SL -> {
                    log.info(config.getSymbol().name() + ". LONG action OPEN_LONG_CLOSE_SHORT_SL!");
                    accountService.newOrderMarketShortClose(config.getSymbol(), quantity);
                    quantity = round((quantity * 2), 3);
                    if (quantity > config.getQuantityMax()) {
                        quantity = config.getQuantity();
                    }
                    accountService.newOrderMarketLongOpen(config.getSymbol(), quantity);
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
