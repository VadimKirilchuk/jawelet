package ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.haar.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.AbstractFiltersFactory;

/**
 * Haar(normalized) filters factory.
 * 
 * @author Kirilchuk V.E.
 */
public class HaarFiltersFactory extends AbstractFiltersFactory {

    private static final Filter LOW_DECOMPOSITION_FILTER  = new HaarLowDecompFilter();
    private static final Filter HI_DECOMPOSITION_FILTER   = new HaarHighDecompFilter();
    private static final Filter LOW_RECONSTRUCTION_FILTER = new HaarLowReconstrFilter();
    private static final Filter HI_RECONSTRUCTION_FILTER  = new HaarHighReconstrFilter();

    public HaarFiltersFactory() {
        super(LOW_DECOMPOSITION_FILTER, HI_DECOMPOSITION_FILTER,
              LOW_RECONSTRUCTION_FILTER, HI_RECONSTRUCTION_FILTER);
    }
}
