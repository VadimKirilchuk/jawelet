package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.legall.impl;

import static ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils.SQRT_32;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.Filter;

/**
 * Low decomposition filter from Le Gall filter bank.
 *
 * @author Kirilchuk V.E.
 */
public class LeGallLowDecompFilter extends Filter {

    private static final double[] COEFFICIENTS = {
        -1.0 / SQRT_32,
         2.0 / SQRT_32,
         6.0 / SQRT_32,
         2.0 / SQRT_32,
        -1.0 / SQRT_32
    };
    
    public LeGallLowDecompFilter() {
    	super(COEFFICIENTS);
    }
}
