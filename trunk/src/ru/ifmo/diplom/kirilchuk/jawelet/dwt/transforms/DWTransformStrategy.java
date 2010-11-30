package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;

/**
 *
 * @author Kirilchuk V.E.
 */
public interface DWTransformStrategy {

    double[] decomposeLow(double[] data, Filter lowDecompositionFilter);

    double[] decomposeHigh(double[] data,Filter highDecompositionFilter);

    double[] reconstruct(double[] approximation, double[] details,
                         Filter lowReconstructionFilter,
                         Filter highReconstructionFilter);
}
