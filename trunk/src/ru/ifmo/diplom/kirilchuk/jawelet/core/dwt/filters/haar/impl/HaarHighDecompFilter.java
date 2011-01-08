package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.haar.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.Filter;

/**
 * High decomposition filter from Haar filter bank. Orthonormalized.
 * 
 * @author Kirilchuk V.E.
 */
public class HaarHighDecompFilter extends Filter {

	private static final double[] COEFFICIENTS = { -1 / Math.sqrt(2), 1 / Math.sqrt(2) };// as

	public HaarHighDecompFilter() {
		super(COEFFICIENTS);
	}
}
