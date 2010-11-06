package ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters;

/**
 * Interface that represents Finite impulse responce filters(FIR).
 *
 * Read more at <a href = http://en.wikipedia.org/wiki/Finite_impulse_response>
 * FIR on wikipedia. </a>
 *
 * @author Kirilchuk V.E.
 */
public interface Filter {

    /**
     * Returns coefficients of filter.
     *
     * @return coefficients of filter.
     */
    double[] getCoeff();
}
