package ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.haar.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;

/**
 *
 * @author Kirilchuk V.E.
 */
public class HaarLowDecompFilter implements Filter {

    private static final double[] COEFFICIENTS = {1/Math.sqrt(2), 1/Math.sqrt(2)};

    @Override
    public double[] getCoeff() {
        return COEFFICIENTS;
    }

    public static void main(String[] args) {
        System.out.println(COEFFICIENTS[0]);
    }
}
