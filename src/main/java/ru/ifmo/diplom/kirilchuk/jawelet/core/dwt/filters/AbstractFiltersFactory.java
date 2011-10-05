package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters;


/**
 * Abstract class that represents AbstractFiltersFactory.(AbstractFactory pattern).
 * Such factory must produce filters for concrete wavelet transform.
 *
 * <p>
 * In general:
 * <p>
 *  1) An orthogonal wavelet is entirely defined by the scaling filter
 * a low-pass finite impulse response (FIR) filter of <b>length 2N and sum 1</b>.
 * For analysis with orthogonal wavelets the high pass filter 
 * is calculated as the quadrature mirror filter of the low pass, 
 * and reconstruction filters are the time reverse of the decomposition filters.
 * <p>
 *  2)In biorthogonal wavelets, separate decomposition and reconstruction filters are defined.
 * <p> So we need all filters to be fairly given in constructor.
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
