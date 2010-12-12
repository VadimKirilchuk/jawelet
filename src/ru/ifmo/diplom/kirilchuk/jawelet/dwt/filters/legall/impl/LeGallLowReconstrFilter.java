package ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.legall.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
import static ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils.*;

/**
 *
 * @author Kirilchuk V.E.
 */
public class LeGallLowReconstrFilter implements Filter {

    private static final double[] COEFFICIENTS = {
        2.0 / SQRT_32,
        4.0 / SQRT_32,
        2.0 / SQRT_32};

    @Override
    public double[] getCoeff() {
        return COEFFICIENTS;
    }

    @Override
    public int getLength() {
        return COEFFICIENTS.length;
    }
}
