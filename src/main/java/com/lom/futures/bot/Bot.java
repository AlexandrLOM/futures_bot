package com.lom.futures.bot;

import com.lom.futures.dto.Kline;
import com.lom.futures.dto.context.ContextBot;

import java.util.List;

public interface Bot {

    void doMoney(ContextBot context);
    default void init(ContextBot context) {
    };
}
