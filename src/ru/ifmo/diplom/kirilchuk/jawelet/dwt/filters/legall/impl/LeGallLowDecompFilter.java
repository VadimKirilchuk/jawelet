package ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.legall.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
import static ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils.*;

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
