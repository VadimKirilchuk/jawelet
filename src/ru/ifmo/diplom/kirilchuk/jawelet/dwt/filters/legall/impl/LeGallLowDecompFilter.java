package ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.legall.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
import static ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils.*;

/**
 *
 * @author Kirilchuk V.E.
 */
public class LeGallLowDecompFilter implements Filter {

    private static final double[] COEFFICIENTS = {
        -1.0 / SQRT_32,
        2.0 / SQRT_32,
        6.0 / SQRT_32,
        2.0 / SQRT_32,
        -1.0 / SQRT_32
    };

    @Override
    public double[] getCoeff() {
        return COEFFICIENTS;
    }

    @Override
    public int getLength() {
        return COEFFICIENTS.length;
    }
}
