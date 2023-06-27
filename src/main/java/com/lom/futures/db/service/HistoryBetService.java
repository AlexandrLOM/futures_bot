package com.lom.futures.db.service;

import com.lom.futures.db.entity.HistoryBet;
import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Symbol;

public interface HistoryBetService {

    HistoryBet save(Symbol symbol, PositionSide positionSide, Integer bet);
}
