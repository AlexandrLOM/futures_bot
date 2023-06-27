package com.lom.futures.storage;

import lombok.Getter;
import lombok.ToString;

import java.util.ArrayDeque;

@ToString()
public class ArrayDequeFixSize<E> {

    private int sizeMax;
    private E defaultValue;

    @Getter
    private ArrayDeque<E> array;

    public ArrayDequeFixSize(Integer sizeMax, E defaultValue) {
        this.sizeMax = sizeMax;
        this.defaultValue = defaultValue;
        this.array = new ArrayDeque<>();
    }

    public void addFirst(E value) {
        array.addFirst(value);

        if (array.size() > sizeMax) {
            array.removeLast();
        }
    }

    public E getFirstValue(){
        return array.isEmpty() ? defaultValue : array.getFirst();
    }

    public E getLastValue(){
        return array.isEmpty() ? defaultValue : array.getLast();
    }

}
