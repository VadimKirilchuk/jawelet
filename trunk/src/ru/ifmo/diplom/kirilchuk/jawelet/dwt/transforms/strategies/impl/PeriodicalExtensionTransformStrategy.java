package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.strategies.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.TransformStrategy;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Assert;

/**
 * @deprecated wrong pereodic extension. Need to be rewrited.
 * @author Kirilchuk V.E.
 */
public class PeriodicalExtensionTransformStrategy implements TransformStrategy {

    /**
     * Default implementation of low frequency decomposition.
     *
     * @param data input to decompose.
     * @return decimated low frequency output from input.
     */
    @Override
    public double[] decomposeLow(double[] data, Filter lowDecompositionFilter) {
        Assert.argNotNull(data);
        Assert.argNotNull(lowDecompositionFilter);
        Assert.argCondition(data.length % 2 == 0, "Data length must be even.");

        double[] low = decomposition(data, lowDecompositionFilter);

        return low;
    }

    /**
     * Default implementation of high frequency decomposition.
     *
     * @param data input to decompose.
     * @return decimated high frequency output.
     */
    @Override
    public double[] decomposeHigh(double[] data, Filter highDecompositionFilter) {
        Assert.argNotNull(data);
        Assert.argNotNull(highDecompositionFilter);
        Assert.argCondition(data.length % 2 == 0, "Data length must be even.");

        double[] high = decomposition(data, highDecompositionFilter);

        return high;
    }

    @Override
    public double[] reconstruct(double[] approximation, double[] detail,
                                Filter lowReconstructionFilter,
                                Filter highReconstructionFilter) {
        Assert.argNotNull(approximation, detail);
        Assert.argNotNull(lowReconstructionFilter, highReconstructionFilter);

        if (approximation.length != detail.length) {
            throw new IllegalArgumentException("Data lenghts must be the same.");
        }

        double[] result = reconstruction(approximation, detail,
                                         lowReconstructionFilter,
                                         highReconstructionFilter);

        return result;
    }

    private double[] decomposition(double[] data, Filter filter) throws IllegalArgumentException {
        int dataLength = data.length;

        int halfLength = dataLength / 2;
        double[] result = new double[halfLength];
            
        double[] filterCoeff = filter.getCoeff();
        for (int i = 0; i < halfLength; ++i) {
            for (int j = 0; j < filterCoeff.length; ++j) {
                int shift = 2 * i + j;
                int m = shift % dataLength;
                result[i] += data[m] * filterCoeff[filterCoeff.length -1 - j];
                System.out.println(result[i]);
            }
        }

        return result;
    }

    private double[] reconstruction(double[] lowData, double[] highData,
                                    Filter lowFilter, Filter highFilter) {
        int halfLength = lowData.length;
        int resultLength = halfLength * 2;

        double[] lowCoeff  = lowFilter.getCoeff();
        double[] highCoeff = highFilter.getCoeff();
        int filtersLength = lowCoeff.length;

        double[] result = new double[resultLength];

        int index = 0;
        for (int i = 0; i < halfLength; ++i) {

            for (int j = 0; j < filtersLength; ++j) {
                index = i * 2 + j;
                while (index >= resultLength) {
                    index -= resultLength;
                }
                result[index] += (lowData[i] * lowCoeff[j] + highData[i] * highCoeff[j]);
            }
        }

        return result;
    }
}
