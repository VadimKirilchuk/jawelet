package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.strategies.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Extensioner;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Sampler;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Windower;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.DWTransformStrategy;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Action;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.CyclicBeginExtension;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.CyclicEndExtension;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.ZeroPaddingToEven;

/**
 *
 * @author Kirilchuk V.E.
 */
public class CyclicWrappingTransformStrategy implements DWTransformStrategy {
    private final Sampler     sampler     = new Sampler();
    private final Windower    windower    = new Windower();

    @Override
    public double[] decomposeLow(double[] data, Filter lowDecompositionFilter) {
        double[] result;
        
        Action zeroPaddingToEven = new ZeroPaddingToEven();

        int extra = zeroPaddingToEven.getExtensionLength(data.length);
        result = new Extensioner(data)
                .schedule(zeroPaddingToEven)
                .schedule(new CyclicEndExtension(lowDecompositionFilter.getLength()))
                .execute();
        result = convolve(result, lowDecompositionFilter);
        result = sampler.downsample(result);

        int filterLength = lowDecompositionFilter.getCoeff().length;

        return windower.window(result, filterLength - 1, (data.length + extra) / 2 - 1 + filterLength);
    }

    @Override
    public double[] decomposeHigh(double[] data, Filter highDecompositionFilter) {
        double[] result;

        Action zeroPaddingToEven = new ZeroPaddingToEven();
        
        int extra = zeroPaddingToEven.getExtensionLength(data.length);
        result = new Extensioner(data)
                .schedule(zeroPaddingToEven)
                .schedule(new CyclicEndExtension(highDecompositionFilter.getLength()))
                .execute();

        result = convolve(result, highDecompositionFilter);
        result = sampler.downsample(result);

        int filterLength = highDecompositionFilter.getCoeff().length;

        return windower.window(result, filterLength - 1, (data.length + extra) / 2 - 1 + filterLength);
    }

    @Override
    public double[] reconstruct(double[] approximation, double[] details,
            Filter lowReconstructionFilter, Filter highReconstructionFilter) {
        if (approximation.length != details.length) {
            throw new IllegalArgumentException("Data vectors must have equal size.");
        }
        
        double[] extendedApproximation;
        extendedApproximation = new Extensioner(approximation)
                .schedule(new CyclicBeginExtension(lowReconstructionFilter.getLength()))
                .execute();

        extendedApproximation = sampler.upsample(extendedApproximation);
        extendedApproximation = convolve(extendedApproximation, lowReconstructionFilter);

        double[] extendedDetails;
        extendedDetails = new Extensioner(details)
                .schedule(new CyclicBeginExtension(highReconstructionFilter.getLength()))
                .execute();

        extendedDetails = sampler.upsample(extendedDetails);
        extendedDetails = convolve(extendedDetails, highReconstructionFilter);

        double[] result = new double[extendedApproximation.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = extendedApproximation[i] + extendedDetails[i];
        }

        int filterLength = lowReconstructionFilter.getCoeff().length;

        result = windower.window(result, filterLength + 1, details.length * 2 + filterLength + 1);

        return result;
    }

    private double[] convolve(double[] data, Filter filter) {//TODO rename short-named variables
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
