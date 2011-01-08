package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.Filter;

/**
 * Class that represents discrete wavelet transform strategy.
 * Main difference between strategies is method of reducing boundary effect.
 *
 * @author Kirilchuk V.E.
 */
public interface DWTransformStrategy {

	/**
	 * Decomposing 1 level transform inplace, changing given input.
	 * 
	 * @param input data to transform.
	 * @param lowDecompositionFilter low decomposition filter.
	 * @param highDecompositionFilter high decomposition filter.
	 */
    void decomposeInplace(double[] input, Filter lowDecompositionFilter, Filter highDecompositionFilter);

    void reconstructInplace(double[] input, Filter lowReconstructionFilter, Filter highReconstructionFilter);

    double[] decomposeLow(double[] data, Filter lowDecompositionFilter);

    double[] decomposeHigh(double[] data,Filter highDecompositionFilter);

    double[] reconstruct(double[] approximation, double[] details,
                         Filter lowReconstructionFilter,
                         Filter highReconstructionFilter);
}
