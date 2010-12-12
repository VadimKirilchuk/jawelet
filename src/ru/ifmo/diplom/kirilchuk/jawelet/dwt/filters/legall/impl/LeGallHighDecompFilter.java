package ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.legall.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
import static ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils.*;

/**
 * High frequency decomposition filter for discrete wavelet transform.(haar)
 *
 * @author Kirilchuk V.E.
 */
public class LeGallHighDecompFilter implements Filter {

    private static final double[] COEFFICIENTS = {
        -2.0 / SQRT_32,
        4.0 / SQRT_32,
        -2.0 / SQRT_32};

    @Override
    public double[] getCoeff() {
        return COEFFICIENTS;
    }

    @Override
    public int getLength() {
        return COEFFICIENTS.length;
    }
}
