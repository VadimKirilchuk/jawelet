package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.legall.impl;

import static ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils.SQRT_32;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.Filter;

/**
 * High decomposition filter from Le Gall filter bank.
 *
 * @author Kirilchuk V.E.
 */
public class LeGallHighDecompFilter extends Filter {

    private static final double[] COEFFICIENTS = {
        -2.0 / SQRT_32,
         4.0 / SQRT_32,
        -2.0 / SQRT_32};

    public LeGallHighDecompFilter() {
    	super(COEFFICIENTS);
    }
}
