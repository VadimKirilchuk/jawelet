package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.legall.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.DecompositionResult;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.legall.impl.LeGallFiltersFactory;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.DiscreteWaveletTransform;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Assert;
import ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Sampler;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Windower;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Action;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Extensioner;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.AddLastToEnd;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.CopyElementToBegin;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.MirrorExtension;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.ZeroPaddingTo2Power;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.ZeroPaddingToEven;

/**
 * Class that represents DiscreteWaveletTransform on haar filter bank basis.
 * 
 * @author Kirilchuk V.E.
 */
public class LeGallWaveletTransform extends DiscreteWaveletTransform {
    private final Sampler     sampler     = new Sampler();
    private final Windower    windower    = new Windower();

    public LeGallWaveletTransform() {
        super(new LeGallFiltersFactory());
    }

    @Override
    public DecompositionResult decompose(double[] data) {//same as in DWT instead of length restriction.
        Assert.argNotNull(data);//TODO after finding how to do this with lenght = 2.. remove this method.
        Assert.argCondition(data.length >= 4, "Data length must be >= 4.");

        //finding closest 2nd power value
        int value = MathUtils.getClosest2PowerValue(data.length);
        int level = MathUtils.getExact2Power(value);

        //extending if need
        if(data.length - value < 0) {
            data = new Extensioner(data)
                    .schedule(new ZeroPaddingTo2Power(level))
                    .execute();
        }

        return decompose(data, level);
    }

    @Override
    public DecompositionResult decompose(double[] data, int level) {//same as in DWT instead of length restriction.
        Assert.argNotNull(data);//TODO after finding how to do this with lenght = 2.. remove this method.
        Assert.argCondition(level >= 1, "Level argument must be >= 1.");
        Assert.argCondition(data.length >= 4, "Data length must be >= 4.");

        DecompositionResult result = new DecompositionResult();
        double[] approximation;
        double[] details;
        for (int i = 1; i <= level; ++i) {
            approximation = decomposeLow(data);
            details = decomposeHigh(data);
            result.setApproximation(approximation);
            result.addDetails(details);
            result.setLevel(i);
            if(approximation.length == 1) {
                break; //we can`t decompose more...
            }
            data = approximation; //approximation is data for next decomposition
        }

        return result;
    }

    @Override
    public double[] reconstruct(DecompositionResult decomposition) {
        return reconstruct(decomposition, 0);
    }

    @Override
    public double[] reconstruct(DecompositionResult decomposition, int level) {
        if (level >= decomposition.getLevel()) {
            throw new IllegalArgumentException("Level must be less than decomposition level.");
        }

        double[] reconstructed = decomposition.getApproximation();
        for (int i = decomposition.getLevel(); i > level; --i) {
            reconstructed = reconstruct(reconstructed, decomposition.getDetailsList().get(i - 1));
        }

        return reconstructed;
    }

    //TODO this implementation is to understand how to do it in common case...
    //asymmetric filters delays cause very much trouble.
    private double[] decomposeLow(final double[] data) {//TODO assumes that filter length is 5!!!
        double[] result;

        Action zeroPaddingToEven = new ZeroPaddingToEven();

        Filter lowDecompositionFilter = filtersFactory.getLowDecompositionFilter();

        int extra = zeroPaddingToEven.getExtensionLength(data.length);
        double[] temp = new Extensioner(data)
                .schedule(zeroPaddingToEven)
                .schedule(new MirrorExtension(1))
                .execute();

        //TODO ugly but need one more element...
        result = new double[temp.length + 1];
        System.arraycopy(temp, 0, result, 0, temp.length);
        result[result.length - 1] = data[data.length - 3];

        result = MathUtils.convolve(result, lowDecompositionFilter.getCoeff());
        result = sampler.downsample(result);

        return windower.window(result, 2, (data.length + extra) / 2 + 2);
    }

    //TODO this implementation is to understand how to do it in common case...
    //asymmetric filters delays cause very much trouble.
    private double[] decomposeHigh(final double[] data) {//TODO assumes that filter length is 3!!!
        double[] result;

        Action zeroPaddingToEven = new ZeroPaddingToEven();

        Filter highDecompositionFilter = filtersFactory.getHighDecompositionFilter();

        int extra = zeroPaddingToEven.getExtensionLength(data.length);
        double[] temp = new Extensioner(data)
                .schedule(zeroPaddingToEven)
                .schedule(new MirrorExtension(1))
                .execute();

        //TODO ugly but need one more element...
        result = new double[temp.length + 1];
        System.arraycopy(temp, 0, result, 0, temp.length);
        result[result.length - 1] = data[data.length - 3];

        result = MathUtils.convolve(result, highDecompositionFilter.getCoeff());
        result = sampler.downsample(result);

        return windower.window(result, 1, (data.length + extra) / 2 + 1);
    }

    private double[] reconstruct(double[] approximation, double[] details) {
        if (approximation.length != details.length) {
            throw new IllegalArgumentException("Data vectors must have equal size.");
        }

        Filter lowReconstructionFilter = filtersFactory.getLowReconstructionFilter();
        Filter highReconstructionFilter = filtersFactory.getHighReconstructionFilter();

        double[] extendedApproximation;
        extendedApproximation = new Extensioner(approximation)
                .schedule(new CopyElementToBegin(0))
                .schedule(new AddLastToEnd())
                .execute();

        extendedApproximation = sampler.upsample(extendedApproximation);
        extendedApproximation = MathUtils.convolve(extendedApproximation, lowReconstructionFilter.getCoeff());
        extendedApproximation = windower.window(extendedApproximation, 2, approximation.length * 2 + 2);


        double[] extendedDetails;
        extendedDetails = new Extensioner(details)
                .schedule(new CopyElementToBegin(1))
                .schedule(new AddLastToEnd())
                .execute();

        extendedDetails = sampler.upsample(extendedDetails);
        extendedDetails = MathUtils.convolve(extendedDetails, highReconstructionFilter.getCoeff());
        extendedDetails = windower.window(extendedDetails, 4, details.length * 2 + 4);

        double[] result = new double[extendedApproximation.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = extendedApproximation[i] + extendedDetails[i];
        }

        return result;
    }
}
