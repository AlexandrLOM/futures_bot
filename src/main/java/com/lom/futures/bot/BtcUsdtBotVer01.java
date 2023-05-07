package com.lom.futures.bot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lom.futures.bot.strategy.OpenCloseStrategy;
import com.lom.futures.bot.strategy.config.OpenCloseConfig;
import com.lom.futures.dto.context.ContextBot;
import com.lom.futures.service.AccountService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Objects;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class BtcUsdtBotVer01 extends OpenCloseStrategy implements Bot {

    final static DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.###");

    final AccountService accountService;

    Double quantityLong;
    Double quantityShort;

    @Autowired
    public BtcUsdtBotVer01(OpenCloseConfig configBTCUSDT, AccountService accountService) {
        super(configBTCUSDT);
        this.accountService = accountService;
    }


    public void init(ContextBot context) {
        init(context.getPositions());
        quantityLong = Objects.equals(positionLong.getEntryPrice(), 0D) ? config.getQuantity() : positionLong.getPositionAmt();
        quantityShort = Objects.equals(positionShot.getEntryPrice(), 0D) ? config.getQuantity() : Math.abs(positionShot.getPositionAmt());
        log.info(config.getSymbol().name() + ". init: " + this);
    }

    @Override
    public void doMoney(ContextBot context) {

        setParams(context.getPositions());


        var actionForLong = getActionForLong();
        try {
            switch (actionForLong) {
                case OPEN_LONG -> {
                    log.info(config.getSymbol().name() + ". LONG action OPEN!");
                    accountService.newOrderMarketLongOpen(config.getSymbol(), quantityLong);
                }
                case CLOSE_LONG_TP -> {
                    log.info(config.getSymbol().name() + ". LONG action CLOSE TP!");
                    accountService.newOrderMarketLongClose(config.getSymbol(), quantityLong);
                    quantityLong = config.getQuantity();
                }
                case CLOSE_LONG_SL -> {
                    log.info(config.getSymbol().name() + ". LONG action CLOSE SL!");
                    accountService.newOrderMarketLongClose(config.getSymbol(), quantityLong);
                    quantityLong = Double.parseDouble(DECIMAL_FORMAT.format(quantityLong * 3));
                    log.info(config.getSymbol().name() + ". LONG action CLOSE SL! quantityLong = " + quantityLong);
                    if (quantityLong > config.getQuantityMax()) {
                        quantityLong = config.getQuantity();
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        var actionForShort = getActionForShort();
        try {
            switch (actionForShort) {
                case OPEN_SHORT -> {
                    log.info(config.getSymbol().name() + ". SHORT action OPEN!");
                    accountService.newOrderMarketShortOpen(config.getSymbol(), quantityShort);
                }
                case CLOSE_SHORT_TP -> {
                    log.info(config.getSymbol().name() + ". SHORT action CLOSE TP!");
                    accountService.newOrderMarketShortClose(config.getSymbol(), quantityShort);
                    quantityShort = config.getQuantity();
                }
                case CLOSE_SHORT_SL -> {
                    log.info(config.getSymbol().name() + ". SHORT action CLOSE SL!");
                    accountService.newOrderMarketShortClose(config.getSymbol(), quantityShort);
                    quantityShort = Double.parseDouble(DECIMAL_FORMAT.format(quantityShort * 3));
                    log.info(config.getSymbol().name() + ". SHORT action CLOSE SL! quantityShort = " + quantityShort);
                    if (quantityShort > config.getQuantityMax()) {
                        quantityShort = config.getQuantity();
                    }
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


    }
}
