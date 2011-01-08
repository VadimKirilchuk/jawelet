package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters;


/**
 * Abstract class that represents AbstractFiltersFactory.(AbstractFactory pattern).
 * Such factory must produce filters for concrete wavelet transform.
 *
 * @author Kirilchuk V.E.
 */
public abstract class AbstractFiltersFactory {

    private final Filter lowDecompositionFilter;
    private final Filter highDecompositionFilter;
    private final Filter lowReconstructionFilter;
    private final Filter highReconstructionFilter;

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
