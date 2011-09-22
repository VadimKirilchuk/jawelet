package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.legall.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.AbstractFiltersFactory;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.Filter;

/**
 * LeGall(CDF 5/3) filters factory.
 * 
 * @author Kirilchuk V.E.
 */
public class LeGallFiltersFactory extends AbstractFiltersFactory {

    private static final Filter LOW_DECOMPOSITION_FILTER  = new LeGallLowDecompFilter();
    private static final Filter HI_DECOMPOSITION_FILTER   = new LeGallHighDecompFilter();
    private static final Filter LOW_RECONSTRUCTION_FILTER = new LeGallLowReconstrFilter();
    private static final Filter HI_RECONSTRUCTION_FILTER  = new LeGallHighReconstrFilter();

    public LeGallFiltersFactory() {
        super(LOW_DECOMPOSITION_FILTER,  HI_DECOMPOSITION_FILTER,
              LOW_RECONSTRUCTION_FILTER, HI_RECONSTRUCTION_FILTER); 
    }
}
