package com.lom.futures.storage;

import com.lom.futures.bot.strategy.dto.BetResult;
import com.lom.futures.enums.PositionSide;
import com.lom.futures.enums.Symbol;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MapBet<E> {

    @Getter
    private Map<Map<Symbol, PositionSide>, ArrayDequeFixSize<E>> bets;


    public MapBet(Set<Symbol> symbols, Integer sizeMax, Integer devaultValue) {
        bets = new HashMap<>();
        for (Symbol symbol : symbols) {
            bets.put(Map.of(symbol, PositionSide.LONG), new ArrayDequeFixSize(sizeMax, devaultValue));
            bets.put(Map.of(symbol, PositionSide.SHORT), new ArrayDequeFixSize(sizeMax, devaultValue));
        }
    }

    public MapBet(Set<Symbol> symbols, Integer sizeMax, BetResult devaultValue) {
        bets = new HashMap<>();
        for (Symbol symbol : symbols) {
            bets.put(Map.of(symbol, PositionSide.BOTH), new ArrayDequeFixSize(sizeMax, devaultValue));
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

    public void addBet(Symbol symbol, PositionSide positionSide, E bit) {
        bets.get(Map.of(symbol, positionSide)).addFirst(bit);
    }


    public ArrayDequeFixSize getBet(Symbol symbol, PositionSide positionSide) {
        return bets.get(Map.of(symbol, positionSide));
    }

    public E getLastBet(Symbol symbol, PositionSide positionSide) {
        return bets.get(Map.of(symbol, positionSide)).getFirstValue();
    }

    public E getLastTheEndBet(Symbol symbol, PositionSide positionSide) {
        return bets.get(Map.of(symbol, positionSide)).getLastValue();
    }
}
