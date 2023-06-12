package com.lom.futures.storage;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayDeque;
import java.util.Optional;

@ToString()
public class ArrayDequeFixSize {

    private int sizeMax;

    @Getter
    private ArrayDeque<Integer> array;

    public ArrayDequeFixSize(Integer sizeMax) {
        this.sizeMax = sizeMax;
        this.array = new ArrayDeque<>();
    }

    public void addFirst(Integer value) {
        array.addFirst(value);

        if (array.size() > sizeMax) {
            array.removeLast();
        }
    }

    public Integer getFirstValue(){
        return array.isEmpty() ? 0 : array.getFirst();
    }

}
