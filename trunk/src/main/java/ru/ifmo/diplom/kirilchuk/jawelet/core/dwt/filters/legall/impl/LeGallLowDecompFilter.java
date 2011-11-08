package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.legall.impl;

import static ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils.SQRT_32;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.Filter;

/**
 * Low decomposition filter from Le Gall filter bank.
 *
 * @author Kirilchuk V.E.
 */
public class LeGallLowDecompFilter extends Filter {

	private static final double DECOMPOSITION_DIVIDER = 2;
	
    private static final double[] COEFFICIENTS = {
        -1.0 / DECOMPOSITION_DIVIDER,
         2.0 / DECOMPOSITION_DIVIDER,
         6.0 / DECOMPOSITION_DIVIDER,
         2.0 / DECOMPOSITION_DIVIDER,
        -1.0 / DECOMPOSITION_DIVIDER
    };
    
    public LeGallLowDecompFilter() {
    	super(COEFFICIENTS);
    }
}
