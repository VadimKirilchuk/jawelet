package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.strategies.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.Filter;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.DWTransformStrategy;
import ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Sampler;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Windower;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Action;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Extensioner;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.CyclicBeginExtension;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.CyclicEndExtension;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.ZeroPaddingToEven;

/**
 * @deprecated not works with 5/3 and other odd length filters.
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
        result = MathUtils.convolve(result, lowDecompositionFilter.getCoeff());
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

        result = MathUtils.convolve(result, highDecompositionFilter.getCoeff());
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
        extendedApproximation = MathUtils.convolve(extendedApproximation, lowReconstructionFilter.getCoeff());

        double[] extendedDetails;
        extendedDetails = new Extensioner(details)
                .schedule(new CyclicBeginExtension(highReconstructionFilter.getLength()))
                .execute();

        extendedDetails = sampler.upsample(extendedDetails);
        extendedDetails = MathUtils.convolve(extendedDetails, highReconstructionFilter.getCoeff());

        double[] result = new double[extendedApproximation.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = extendedApproximation[i] + extendedDetails[i];
        }

        int filterLength = lowReconstructionFilter.getCoeff().length;

        result = windower.window(result, filterLength + 1, details.length * 2 + filterLength + 1);

        return result;
    }

    @Override
    public void decomposeInplace(double[] input, Filter lowDecompositionFilter, Filter highDecompositionFilter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reconstructInplace(double[] input, Filter lowReconstructionFilter, Filter highReconstructionFilter) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
