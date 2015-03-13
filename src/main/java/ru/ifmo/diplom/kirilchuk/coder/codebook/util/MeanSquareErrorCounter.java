package ru.ifmo.diplom.kirilchuk.coder.codebook.util;

/**
 * Helper class to count mean square error. 
 *
 * @param <T> type of values
 */
public class MeanSquareErrorCounter {

    private double summ;
    private int count = 0;

    public MeanSquareErrorCounter add(double error) {
        summ += error;
        count++;
        return this;
    }

    public double getMeanSquareError() {
        return summ / count;
    }
}

