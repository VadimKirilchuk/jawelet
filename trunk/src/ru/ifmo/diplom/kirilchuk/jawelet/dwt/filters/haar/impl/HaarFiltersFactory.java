package ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.haar.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.FiltersFactory;

/**
 * Concrete factory for Haar discrete wavelet transform.
 * 
 * @author Kirilchuk V.E.
 */
public class HaarFiltersFactory implements FiltersFactory {

    private static final Filter LOW_DECOMPOSITION_FILTER  = new HaarLowDecompFilter();
    private static final Filter HI_DECOMPOSITION_FILTER   = new HaarHighDecompFilter();
    private static final Filter LOW_RECONSTRUCTION_FILTER = new HaarLowReconstrFilter();
    private static final Filter HI_RECONSTRUCTION_FILTER  = new HaarHighReconstrFilter();

    @Override
    public Filter getLowDecompositionFilter() {
        return LOW_DECOMPOSITION_FILTER;
    }

    @Override
    public Filter getHighDecompositionFilter() {
        return HI_DECOMPOSITION_FILTER;
    }

    @Override
    public Filter getLowReconstructionFilter() {
        return LOW_RECONSTRUCTION_FILTER;
    }

    @Override
    public Filter getHighReconstructionFilter() {
        return HI_RECONSTRUCTION_FILTER;
    }
}
