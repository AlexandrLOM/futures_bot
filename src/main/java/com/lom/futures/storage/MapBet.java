package com.lom.futures.storage;

import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Symbol;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MapBet {

    @Getter
    private Map<Map<Symbol, PositionSide>, ArrayDequeFixSize> bets;


    public MapBet(Set<Symbol> symbols, Integer sizeMax) {
        bets = new HashMap<>();
        for (Symbol symbol : symbols) {
            bets.put(Map.of(symbol, PositionSide.LONG), new ArrayDequeFixSize(sizeMax));
            bets.put(Map.of(symbol, PositionSide.SHORT), new ArrayDequeFixSize(sizeMax));
        }
    }

    public AlgorithmBet getAlgorithm(Symbol symbol, PositionSide positionSide) {
        if (getRateQuantity(symbol, positionSide, 1) >= 3) {
            return AlgorithmBet.BET_HIGH;
        }
        return AlgorithmBet.DEFAULT;
    }

    private Integer getRateQuantity(Symbol symbol, PositionSide positionSide, Integer bit) {
        return bets.get(Map.of(symbol, positionSide)).getArray().stream()
                .filter(bet -> Objects.equals(bet, bit))
                .toList()
                .size();
    }

    public void addBet(Symbol symbol, PositionSide positionSide, Integer bit) {
        bets.get(Map.of(symbol, positionSide)).addFirst(bit);
    }


    public ArrayDequeFixSize getBet(Symbol symbol, PositionSide positionSide) {
        return bets.get(Map.of(symbol, positionSide));
    }

    public Integer getLastBet(Symbol symbol, PositionSide positionSide) {
        return bets.get(Map.of(symbol, positionSide)).getFirstValue();
    }
}
