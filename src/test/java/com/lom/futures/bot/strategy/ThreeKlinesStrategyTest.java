package com.lom.futures.bot.strategy;

import com.lom.futures.dto.Kline;
import com.lom.futures.enums.bot.Action;
import com.lom.futures.enums.bot.KlineType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ThreeKlinesStrategyTest {

    private ThreeKlinesStrategy strategy;

    @BeforeEach
    void init(){
        strategy = new ThreeKlinesStrategy();
    }

    @Test
    void setNewKlineParam_newParam_green_green() {
        var klines = List.of(
                Kline.builder().openTime(2L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(1L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(3L).open(30.0).close(20.0).build()
        );

        var result = strategy.setNewKlineParam(klines);
        assertTrue(result);
        assertEquals(KlineType.GREEN, strategy.getSecondKline());
        assertEquals(KlineType.GREEN, strategy.getThirdKline());

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        assertEquals(3L, strategy.getOpenTimeLastKline());

    }

    @Test
    void setNewKlineParam_newParam_green_red() {
        var klines = List.of(
                Kline.builder().openTime(2L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(1L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(3L).open(30.0).close(20.0).build()
        );

        var result = strategy.setNewKlineParam(klines);
        assertTrue(result);
        assertEquals(KlineType.GREEN, strategy.getSecondKline());
        assertEquals(KlineType.RED, strategy.getThirdKline());

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        assertEquals(3L, strategy.getOpenTimeLastKline());

    }

    @Test
    void setNewKlineParam_newParam_red_green() {
        var klines = List.of(
                Kline.builder().openTime(2L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(1L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(3L).open(30.0).close(20.0).build()
        );

        var result = strategy.setNewKlineParam(klines);
        assertTrue(result);
        assertEquals(KlineType.RED, strategy.getSecondKline());
        assertEquals(KlineType.GREEN, strategy.getThirdKline());

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        assertEquals(3L, strategy.getOpenTimeLastKline());

    }

    @Test
    void setNewKlineParam_newParam_red_red() {
        var klines = List.of(
                Kline.builder().openTime(2L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(1L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(3L).open(10.0).close(20.0).build()
        );

        var result = strategy.setNewKlineParam(klines);
        assertTrue(result);
        assertEquals(KlineType.RED, strategy.getSecondKline());
        assertEquals(KlineType.RED, strategy.getThirdKline());

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        assertEquals(3L, strategy.getOpenTimeLastKline());

    }

    @Test
    void setNewKlineParam_notChangeParam() {
        var klines = List.of(
                Kline.builder().openTime(2L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(1L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(3L).open(10.0).close(20.0).build()
        );
        var result = strategy.setNewKlineParam(klines);
        assertTrue(result);
        result = strategy.setNewKlineParam(klines);
        assertFalse(result);
        assertEquals(KlineType.RED, strategy.getSecondKline());
        assertEquals(KlineType.RED, strategy.getThirdKline());

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        assertEquals(3L, strategy.getOpenTimeLastKline());

    }

    @Test
    void whatShouldDoForLong() {
        var klines1 = List.of(
                Kline.builder().openTime(2L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(1L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(3L).open(30.0).close(20.0).build()
        );

        var result = strategy.setNewKlineParam(klines1);
        assertTrue(result);

        assertEquals(KlineType.GREEN, strategy.getSecondKline());
        assertEquals(KlineType.GREEN, strategy.getThirdKline());

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        assertEquals(3L, strategy.getOpenTimeLastKline());

        var klines2 = List.of(
                Kline.builder().openTime(3L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(2L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(4L).open(10.0).close(20.0).build()
        );

        result = strategy.setNewKlineParam(klines2);
        assertTrue(result);

        assertEquals(KlineType.GREEN, strategy.getSecondKline());
        assertEquals(KlineType.GREEN, strategy.getThirdKline());

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        assertEquals(4L, strategy.getOpenTimeLastKline());

        var resultActionLong =  strategy.whatShouldDoForLong();
        assertEquals(Action.OPEN, resultActionLong);

        assertEquals(Action.OPEN, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        var klines3 = List.of(
                Kline.builder().openTime(4L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(3L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(5L).open(10.0).close(20.0).build()
        );

        result = strategy.setNewKlineParam(klines3);
        assertTrue(result);

        assertEquals(KlineType.GREEN, strategy.getSecondKline());
        assertEquals(KlineType.GREEN, strategy.getThirdKline());

        assertEquals(Action.OPEN, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        assertEquals(5L, strategy.getOpenTimeLastKline());

        resultActionLong =  strategy.whatShouldDoForLong();
        assertEquals(Action.WAIT, resultActionLong);

        assertEquals(Action.OPEN, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        var klines4 = List.of(
                Kline.builder().openTime(5L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(4L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(6L).open(10.0).close(20.0).build()
        );

        result = strategy.setNewKlineParam(klines4);
        assertTrue(result);

        assertEquals(KlineType.RED, strategy.getSecondKline());
        assertEquals(KlineType.GREEN, strategy.getThirdKline());

        assertEquals(Action.OPEN, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        assertEquals(6L, strategy.getOpenTimeLastKline());

        resultActionLong =  strategy.whatShouldDoForLong();
        assertEquals(Action.CLOSE, resultActionLong);

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        var klines5 = List.of(
                Kline.builder().openTime(6L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(5L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(7L).open(10.0).close(20.0).build()
        );

        result = strategy.setNewKlineParam(klines5);
        assertTrue(result);

        assertEquals(KlineType.GREEN, strategy.getSecondKline());
        assertEquals(KlineType.RED, strategy.getThirdKline());

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        assertEquals(7L, strategy.getOpenTimeLastKline());

        resultActionLong =  strategy.whatShouldDoForLong();
        assertEquals(Action.WAIT, resultActionLong);

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

    }

    @Test
    void whatShouldDoForShort() {
        var klines1 = List.of(
                Kline.builder().openTime(2L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(1L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(3L).open(10.0).close(20.0).build()
        );

        var result = strategy.setNewKlineParam(klines1);
        assertTrue(result);

        assertEquals(KlineType.RED, strategy.getSecondKline());
        assertEquals(KlineType.RED, strategy.getThirdKline());

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        assertEquals(3L, strategy.getOpenTimeLastKline());

        var klines2 = List.of(
                Kline.builder().openTime(3L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(2L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(4L).open(10.0).close(20.0).build()
        );

        result = strategy.setNewKlineParam(klines2);
        assertTrue(result);

        assertEquals(KlineType.RED, strategy.getSecondKline());
        assertEquals(KlineType.RED, strategy.getThirdKline());

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        assertEquals(4L, strategy.getOpenTimeLastKline());

        var resultActionShort =  strategy.whatShouldDoForShort();
        assertEquals(Action.OPEN, resultActionShort);

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.OPEN, strategy.getLastActionShor());

        var klines3 = List.of(
                Kline.builder().openTime(4L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(3L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(5L).open(30.0).close(20.0).build()
        );

        result = strategy.setNewKlineParam(klines3);
        assertTrue(result);

        assertEquals(KlineType.RED, strategy.getSecondKline());
        assertEquals(KlineType.RED, strategy.getThirdKline());

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.OPEN, strategy.getLastActionShor());

        assertEquals(5L, strategy.getOpenTimeLastKline());

        resultActionShort =  strategy.whatShouldDoForShort();
        assertEquals(Action.WAIT, resultActionShort);

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.OPEN, strategy.getLastActionShor());

        var klines4 = List.of(
                Kline.builder().openTime(5L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(4L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(6L).open(10.0).close(20.0).build()
        );

        result = strategy.setNewKlineParam(klines4);
        assertTrue(result);

        assertEquals(KlineType.GREEN, strategy.getSecondKline());
        assertEquals(KlineType.RED, strategy.getThirdKline());

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.OPEN, strategy.getLastActionShor());

        assertEquals(6L, strategy.getOpenTimeLastKline());

        resultActionShort =  strategy.whatShouldDoForShort();
        assertEquals(Action.CLOSE, resultActionShort);

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        var klines5 = List.of(
                Kline.builder().openTime(6L).open(10.0).close(20.0).build(),
                Kline.builder().openTime(5L).open(30.0).close(20.0).build(),
                Kline.builder().openTime(7L).open(10.0).close(20.0).build()
        );

        result = strategy.setNewKlineParam(klines5);
        assertTrue(result);

        assertEquals(KlineType.GREEN, strategy.getSecondKline());
        assertEquals(KlineType.RED, strategy.getThirdKline());

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

        assertEquals(7L, strategy.getOpenTimeLastKline());

        resultActionShort =  strategy.whatShouldDoForShort();
        assertEquals(Action.WAIT, resultActionShort);

        assertEquals(Action.CLOSE, strategy.getLastActionLong());
        assertEquals(Action.CLOSE, strategy.getLastActionShor());

    }
}