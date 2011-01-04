package ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.haar.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;

/**
 * Low reconstruction filter from Haar filter bank. Orthonormalized.
 * 
 * @author Kirilchuk V.E.
 */
public class HaarLowReconstrFilter extends Filter {

	private static final double[] COEFFICIENTS = { 1 / Math.sqrt(2), 1 / Math.sqrt(2) };

	public HaarLowReconstrFilter() {
		super(COEFFICIENTS);
	}
}
