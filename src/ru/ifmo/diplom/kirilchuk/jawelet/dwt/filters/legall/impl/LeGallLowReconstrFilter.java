package ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.legall.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
import static ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils.*;

/**
 * Low reconstruction filter from Le Gall filter bank.
 * 
 * @author Kirilchuk V.E.
 */
public class LeGallLowReconstrFilter extends Filter {

    private static final double[] COEFFICIENTS = {
        2.0 / SQRT_32,
        4.0 / SQRT_32,
        2.0 / SQRT_32};

    public LeGallLowReconstrFilter() {
    	super(COEFFICIENTS);
    }
}
