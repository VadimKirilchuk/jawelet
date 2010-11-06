package ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters;

/**
 * Interface that represents AbstractFiltersFactory.(AbstractFactory pattern).
 * Such factory must produce filters for concrete wavelet transform.
 *
 * @author Kirilchuk V.E.
 */
public interface FiltersFactory {

    /**
     * Returns concrete low decomposition filter as {@link Filter}
     *
     * @return concrete low decomposition filter as {@link Filter}
     */
    Filter getLowDecompositionFilter();

    /**
     * Returns concrete high decomposition filter as {@link Filter}
     *
     * @return concrete high decomposition filter as {@link Filter}
     */
    Filter getHighDecompositionFilter();

    /**
     * Returns concrete low reconstruction filter as {@link Filter}
     *
     * @return concrete low reconstruction filter as {@link Filter}
     */
    Filter getLowReconstructionFilter();

    /**
     * Returns concrete high reconstruction filter as {@link Filter}
     *
     * @return concrete high reconstruction filter as {@link Filter}
     */
    Filter getHighReconstructionFilter();
}
