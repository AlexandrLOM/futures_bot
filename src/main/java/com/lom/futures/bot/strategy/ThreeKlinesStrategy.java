package com.lom.futures.bot.strategy;

import com.lom.futures.dto.Kline;
import com.lom.futures.enums.bot.Action;
import com.lom.futures.enums.bot.KlineType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Slf4j
@Getter
public class ThreeKlinesStrategy {


    private KlineType firstKline;

    private KlineType secondKline;
    private KlineType thirdKline;

    private Double thirdKlineGreenOpen = null;
    private Double thirdKlineRedOpen = null;

    private Double secondKlineClose;

    private Long openTimeLastKline;
    private Action lastActionLong = Action.CLOSE;
    private Action lastActionShor = Action.CLOSE;

    public Boolean setNewKlineParam(List<Kline> klinesFor3m3limit) {
        var klines = klinesFor3m3limit.stream()
                .sorted(Comparator.comparing(Kline::getOpenTime).reversed())
                .toList();

        if (!Objects.equals(klines.get(0).getOpenTime(), openTimeLastKline)) {
            secondKline = getKlineType(klines.get(1));
            secondKlineClose = klines.get(1).getClose();

            thirdKline = getKlineType(klines.get(2));

            if (Objects.equals(KlineType.GREEN, thirdKline)) {
                thirdKlineRedOpen = klines.get(2).getOpen();
                thirdKlineGreenOpen = klines.get(2).getClose();
            }
            if (Objects.equals(KlineType.RED, thirdKline)) {
                thirdKlineRedOpen = klines.get(2).getClose();
                thirdKlineGreenOpen = klines.get(2).getOpen();
            }

            secondKlineClose = klines.get(1).getClose();

            openTimeLastKline = klines.get(0).getOpenTime();
            log.info("Set new 'klines' -> " + thirdKline + " " + secondKline);
            return true;
        } else {
            return false;
        }
    }

    private KlineType getKlineType(Kline kline) {
        if (kline.getOpen() < kline.getClose()) return KlineType.GREEN;
        if (kline.getOpen() > kline.getClose()) return KlineType.RED;
        return KlineType.GREEN_RED;
    }

    public Action whatShouldDoForLong() {
        if (Objects.equals(KlineType.GREEN, thirdKline)
                && Objects.equals(KlineType.GREEN, secondKline)
                && !Objects.equals(lastActionLong, Action.OPEN)) {
            lastActionLong = Action.OPEN;
            return lastActionLong;
        }
        if (!Objects.equals(lastActionLong, Action.CLOSE)
                && thirdKlineGreenOpen < secondKlineClose) {
            lastActionLong = Action.CLOSE;
            return lastActionLong;
        }
        return Action.WAIT;
    }

    public Action whatShouldDoForShort() {
        if (Objects.equals(KlineType.RED, thirdKline)
                && Objects.equals(KlineType.RED, secondKline)
                && !Objects.equals(lastActionShor, Action.OPEN)) {
            lastActionShor = Action.OPEN;
            return lastActionShor;
        }
        if (!Objects.equals(lastActionShor, Action.CLOSE)
                && thirdKlineRedOpen > secondKlineClose) {
            lastActionShor = Action.CLOSE;
            return lastActionShor;
        }
        return Action.WAIT;
    }

}
