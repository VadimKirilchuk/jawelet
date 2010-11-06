package ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.haar.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;

/**
 * High frequency decomposition filter for discrete wavelet transform.(haar)
 *
 * @author Kirilchuk V.E.
 */
public class HaarHighDecompFilter implements Filter {

    private static final double[] COEFFICIENTS = {-1, 1};

    @Override
    public double[] getCoeff() {
        return COEFFICIENTS;
    }
}
