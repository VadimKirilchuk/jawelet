package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters;

import ru.ifmo.diplom.kirilchuk.jawelet.util.FilterUtils;

/**
 * Default implementation of Finite Impulse Response filter(FIR).
 *
 * Read more at <a href = http://en.wikipedia.org/wiki/Finite_impulse_response>
 * FIR on wikipedia. </a>
 *
 * @author Kirilchuk V.E.
 */
public class Filter {

    private final double[] coefficients;
    
    public Filter(double[] coefficients) {
		this.coefficients = coefficients;
	}
	
	/**
     * Returns coefficients of filter.
     *
     * @return coefficients of filter.
     */
    public double[] getCoeff() {
    	return coefficients;
    }

    /**
     * Returns length of filter. 
     * Same as {@code filter.getCoeff().length;}
     * 
     * @return length of filter.
     */
    public int getLength() {
    	return coefficients.length;
    }
    
    /**
     * Checks is filter symmetric. 
     * 
     * @return true if filter is symmetric, false otherwise.
     */
    public boolean isSymmetric() {
    	return FilterUtils.isSymmetric(coefficients);
    }
}
