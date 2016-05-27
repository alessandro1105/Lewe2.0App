package com.lewetechnologies.app.utils;

/**
 * Created by alessandro on 27/05/16.
 */
public class DoubleValue<T, T2> {

    //---VARIABILI---
    private T value1; //valore 1 del tipo T
    private T2 value2; //valore 2 del tipo T2

    //costruttore
    public DoubleValue(T v1, T2 v2) {

        //salvo i valori
        value1 = v1;
        value2 = v2;

    }

    //get function
    public T getValue1() {
        return value1;
    }

    public T2 getValue2() {
        return value2;
    }

}
