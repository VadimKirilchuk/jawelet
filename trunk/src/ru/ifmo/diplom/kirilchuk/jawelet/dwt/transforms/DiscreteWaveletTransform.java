package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.FiltersFactory;

/**
 * Abstract class that corresponds for providing basic implementation
 * of decomposition and reconstruction of discrete wavelet transform.
 *
 * @author Kirilchuk V.E.
 */
public abstract class DiscreteWaveletTransform {

    private final FiltersFactory filtersFactory;

    /**
     * Constructs discrete wavelet transform with specified filters factory.
     *
     * @param filtersFactory factory of filters.
     */
    public DiscreteWaveletTransform(FiltersFactory filtersFactory) {
        this.filtersFactory = filtersFactory;
    }

    /**
     * Default implementation of low frequency decomposition.
     *
     * @param data input to decompose.
     * @return decimated low frequency output from input.
     */
    public double[] decomposeLow(double[] data) {
        double[] low = decompose(data, filtersFactory.getLowDecompositionFilter());

        return low;
    }

    /**
     * Default implementation of high frequency decomposition.
     *
     * @param data input to decompose.
     * @return decimated high frequency output.
     */
    public double[] decomposeHigh(double[] data) {
        double[] high = decompose(data, filtersFactory.getHighDecompositionFilter());

        return high;
    }

    private double[] decompose(double[] data, Filter filter) throws IllegalArgumentException {
        int dataLength = data.length;
        checkIs2Power(dataLength);//throws IllegalArgumentException.

        int halfLength = dataLength / 2;
        double[] result = new double[halfLength];
            
        double[] filterCoeff = filter.getCoeff();
        for (int i = 0; i < halfLength; ++i) {
            for (int j = 0; j < filterCoeff.length; ++j) {
                int index = i * 2 + j;
                if (index >= dataLength) {
                    //cyclic extension emulation
                    index = index % dataLength;
                }
                result[i] += data[index] * filterCoeff[j]; //y(i) = summ( h(j) * x((i-j) mod N) )
            }
        }

        return result;
    }

    private void checkIs2Power(int dataLength) throws IllegalArgumentException {
        if ((dataLength & (dataLength - 1)) != 0) {
            throw new IllegalArgumentException("Data length must be a power of two.");
        }
    }
}
