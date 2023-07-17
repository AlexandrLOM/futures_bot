package com.lom.futures.bot.old;

import com.lom.futures.bot.Bot;
import com.lom.futures.bot.strategy.old.OrderBlockStrategy;
import com.lom.futures.bot.strategy.config.OrderBlockConfig;
import com.lom.futures.dto.context.ContextBot;
import com.lom.futures.enums.Symbol;
import com.lom.futures.service.AccountService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Service
public class OrderBlockBotVer01 extends OrderBlockStrategy
        implements Bot {

    final AccountService accountService;

    Double quantity;

    @Autowired
    public OrderBlockBotVer01(OrderBlockConfig config, AccountService accountService) {
        super(config);
        this.accountService = accountService;
    }


    public void init(ContextBot context) {
    }

    @Override
    public void doMoney(ContextBot context) {
        var symbols = context.getOrderBooks().keySet();
        for (Symbol symbol : symbols) {
            analysisOrder(symbol, context.getOrderBooks().get(symbol));
        }
    }
}
