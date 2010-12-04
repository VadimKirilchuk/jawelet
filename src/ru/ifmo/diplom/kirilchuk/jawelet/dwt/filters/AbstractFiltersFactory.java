package ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters;

import ru.ifmo.diplom.kirilchuk.jawelet.util.Assert;

/**
 * Interface that represents AbstractFiltersFactory.(AbstractFactory pattern).
 * Such factory must produce filters for concrete wavelet transform.
 *
 * @author Kirilchuk V.E.
 */
public abstract class AbstractFiltersFactory {

    private final Filter lowDecompositionFilter;
    private final Filter highDecompositionFilter;
    private final Filter lowReconstructionFilter;
    private final Filter highReconstructionFilter;

    public static Filter createFilter(final double[] filter) {
        Assert.argNotNull(filter);
        Assert.argCondition(filter.length > 0, "Filter must have at least one coeff.");
        return new Filter() {

            @Override
            public double[] getCoeff() {
                return filter;
            }

            @Override
            public int getLength() {
                return filter.length;
            }
        };
    }

    public AbstractFiltersFactory(Filter lowDecompositionFilter, Filter highDecompositionFilter,
                                  Filter lowReconstructionFilter, Filter highReconstructionFilter) {
        this.lowDecompositionFilter   = lowDecompositionFilter;
        this.highDecompositionFilter  = highDecompositionFilter;
        this.lowReconstructionFilter  = lowReconstructionFilter;
        this.highReconstructionFilter = highReconstructionFilter;
    }

    /**
     * Returns concrete low decomposition filter as {@link Filter}
     *
     * @return concrete low decomposition filter as {@link Filter}
     */
    public Filter getLowDecompositionFilter() {
        return lowDecompositionFilter;
    }

    /**
     * Returns concrete high decomposition filter as {@link Filter}
     *
     * @return concrete high decomposition filter as {@link Filter}
     */
    public Filter getHighDecompositionFilter() {
        return highDecompositionFilter;
    }

    /**
     * Returns concrete low reconstruction filter as {@link Filter}
     *
     * @return concrete low reconstruction filter as {@link Filter}
     */
    public Filter getLowReconstructionFilter() {
        return lowReconstructionFilter;
    }

    /**
     * Returns concrete high reconstruction filter as {@link Filter}
     *
     * @return concrete high reconstruction filter as {@link Filter}
     */
    public Filter getHighReconstructionFilter() {
        return highReconstructionFilter;
    }
}
