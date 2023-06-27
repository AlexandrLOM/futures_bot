package com.lom.futures.db.service.impl;

import com.lom.futures.bot.OpenAndWaitBotVer02;
import com.lom.futures.db.entity.HistoryBet;
import com.lom.futures.db.repository.HistoryBetRepository;
import com.lom.futures.db.service.HistoryBetService;
import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Symbol;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class HistoryBetServiceImpl implements HistoryBetService {

    HistoryBetRepository historyBetRepository;

    @Override
    public HistoryBet save(Symbol symbol, PositionSide positionSide, Integer bet) {
        var historyBet = new HistoryBet();
        historyBet.setSymbol(symbol.name());
        historyBet.setPositionSide(positionSide.name());
        historyBet.setStatus(bet);
        historyBet.setTimestamp(Instant.now());
        historyBet.setTag(OpenAndWaitBotVer02.TAG);

        return historyBetRepository.save(historyBet);
    }
}
