package com.lom.futures.bot.strategy.config;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@ToString
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class MainBotConfig {

    OrderBlockConfig orderBlockConfig;

    @Autowired
    public MainBotConfig(OrderBlockConfig orderBlockConfig) {
        this.orderBlockConfig = orderBlockConfig;
    }
}
