package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.strategies.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.Extensioner;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.Sampler;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.Windower;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.TransformStrategy;

/**
 *
 * @author Kirilchuk V.E.
 */
public class CyclicWrappingTransformStrategy implements TransformStrategy {
    private final Extensioner endExtensioner   = new Extensioner();
    private final Extensioner beginExtensioner = new Extensioner();
    {
        beginExtensioner.setMode(Extensioner.Mode.BEGINNING);
    }

    private final Sampler     sampler     = new Sampler();
    private final Windower    windower    = new Windower();

    @Override
    public double[] decomposeLow(double[] data, Filter lowDecompositionFilter) {
        double[] result;
        result = endExtensioner.extend(data, lowDecompositionFilter);
        result = convolve(result, lowDecompositionFilter);
        result = sampler.downsample(result);

        int filterLength = lowDecompositionFilter.getCoeff().length;

        return windower.window(result, filterLength - 1, data.length/2 -1 + filterLength);
    }

    @Override
    public double[] decomposeHigh(double[] data, Filter highDecompositionFilter) {
        double[] result;
        result = endExtensioner.extend(data, highDecompositionFilter);
        result = convolve(result, highDecompositionFilter);
        result = sampler.downsample(result);

        int filterLength = highDecompositionFilter.getCoeff().length;

        return windower.window(result, filterLength - 1, data.length/2 -1 + filterLength);
    }

    @Override
    public double[] reconstruct(double[] approximation, double[] details,
            Filter lowReconstructionFilter, Filter highReconstructionFilter) {
        if(approximation.length != details.length) {
            throw new IllegalArgumentException("Data vectors must have equal size.");
        }
        
        double[] extendedApproximation;
        extendedApproximation = beginExtensioner.extend(approximation, lowReconstructionFilter);
        extendedApproximation = sampler.upsample(extendedApproximation);
        extendedApproximation = convolve(extendedApproximation, lowReconstructionFilter);

        double[] extendedDetails;
        extendedDetails = beginExtensioner.extend(details, highReconstructionFilter);
        extendedDetails = sampler.upsample(extendedDetails);
        extendedDetails = convolve(extendedDetails, highReconstructionFilter);

        double[] result = new double[extendedApproximation.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = extendedApproximation[i] + extendedDetails[i];
        }

        int filterLength = lowReconstructionFilter.getCoeff().length;

        result = windower.window(result, filterLength + 1, details.length * 2 + filterLength + 1);
//        System.out.println(Arrays.toString(result));
        return result;
    }

    private double[] convolve(double[] data, Filter filter) {
        int m = data.length;
        double[] filterCoeff = filter.getCoeff();
        int n = filterCoeff.length;

        double[] result = new double[m + n - 1];

        for (int i = 0; i < result.length; ++i) {
            for (int j = Math.max(0, i + 1 - n); j <= Math.min(i, m - 1); ++j) {
                result[i] += (data[j] * filterCoeff[i - j]);
            }
        }

        return result;
    }
}
