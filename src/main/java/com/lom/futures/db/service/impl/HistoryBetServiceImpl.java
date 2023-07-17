package com.lom.futures.db.service.impl;

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
import java.util.UUID;

@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Service
public class HistoryBetServiceImpl implements HistoryBetService {

    public final static UUID TAG = UUID.randomUUID();

    HistoryBetRepository historyBetRepository;

    @Override
    public HistoryBet save(Symbol symbol, PositionSide positionSide, Integer result) {
        var historyBet = new HistoryBet();
        historyBet.setSymbol(symbol.name());
        historyBet.setPositionSide(positionSide.name());
        historyBet.setStatus(result);
        historyBet.setTimestamp(Instant.now());
        historyBet.setTag(TAG);

        return historyBetRepository.save(historyBet);
    }
}
