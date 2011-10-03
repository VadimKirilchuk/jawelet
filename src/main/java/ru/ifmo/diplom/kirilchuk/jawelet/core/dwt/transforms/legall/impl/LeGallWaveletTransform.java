package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.legall.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.Filter;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.legall.impl.LeGallFiltersFactory;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.DWTransform1D;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Assert;
import ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Sampler;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Windower;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Extensioner;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.AddLastToEnd;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.CopyElementToBegin;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.MirrorExtension;

/**
 * Class that represents DiscreteWaveletTransform on Le Gall filter bank basis.
 * 
 * @author Kirilchuk V.E.
 */
public class LeGallWaveletTransform extends DWTransform1D {
    private final Sampler     sampler     = new Sampler();
    private final Windower    windower    = new Windower();

    public LeGallWaveletTransform() {
        super(new LeGallFiltersFactory());
    }

    @Override
    public void decomposeInplace(double[] data, int toLevel) {
        Assert.checkNotNull(data, "Data vector can`t be null.");
        Assert.argCondition(toLevel>=1, "Level must be >= 1");
        Assert.valueIs2Power(data.length, "Data must have at least 2^level elements."); //TODO can`t decompose to max level cause need at least 3 elements for extension
        int n = data.length;
        for (int step = 0; step < toLevel; ++step) {
            decomposeInplace1Lvl(data, n);
            n /= 2;
        }
    }

    @Override
    public void reconstructInplace(double[] data, int approxLength) {
        Assert.checkNotNull(data, "Data vector can`t be null.");

        Filter lowReconstructionFilter = filtersFactory.getLowReconstructionFilter();
        Filter highReconstructionFilter = filtersFactory.getHighReconstructionFilter();
        
//        while(approxLength <= data.length/2) {
            double[] approximation = new double[approxLength];
            System.arraycopy(data, 0, approximation, 0, approxLength);

            double[] extendedApproximation = new Extensioner(approximation)
                    .schedule(new CopyElementToBegin(0))
                    .schedule(new AddLastToEnd())
                    .execute();

            extendedApproximation = sampler.upsample(extendedApproximation);
            extendedApproximation = MathUtils.convolve(extendedApproximation, lowReconstructionFilter.getCoeff());
            extendedApproximation = windower.window(extendedApproximation, 2, approximation.length * 2 + 2);

            double[] details = new double[approxLength]; //must have same length with approximation
            System.arraycopy(data, approxLength, details, 0, approxLength);
            double[] extendedDetails = new Extensioner(details)
                    .schedule(new CopyElementToBegin(1))
                    .schedule(new AddLastToEnd())
                    .execute();

            extendedDetails = sampler.upsample(extendedDetails);
            extendedDetails = MathUtils.convolve(extendedDetails, highReconstructionFilter.getCoeff());
            extendedDetails = windower.window(extendedDetails, 4, details.length * 2 + 4);

            for (int index = 0; index < approxLength * 2; index++) {
              data[index] = extendedApproximation[index] + extendedDetails[index];
            }
//            approxLength *= 2; // upsampled Low(level) + upsampled High(level) = Low(level-1)
//        }
    }

    /**
     * +1 level of decomposition.
     * <pre> For example: 
     * We have vector 1,1,1,1,2,2,2,2 where 1 are approximation and 2 are details.
     * To decompose one more time we need call
     * decomposeInplace1Lvl([1,1,1,1,2,2,2,2], 4);
     * 4 - index where details start and approximations ended.
     * </pre>
     * 
     * @param data vector with approximation and details.
     * @param endIndex index where details start and approximations ended.
     */
    private void decomposeInplace1Lvl(double[] data, int endIndex) {
        Filter lowDecompositionFilter  = filtersFactory.getLowDecompositionFilter();
        Filter highDecompositionFilter = filtersFactory.getHighDecompositionFilter();

        double[] approximation = new double[endIndex]; //working only with approximation coefficients
        System.arraycopy(data, 0, approximation, 0, endIndex);

        double[] temp = new Extensioner(approximation)
                .schedule(new MirrorExtension(1))
                .execute();

        //TODO ugly but need one more element...
        double[] extended = new double[temp.length + 1];
        System.arraycopy(temp, 0, extended, 0, temp.length);
        extended[extended.length - 1] = approximation[approximation.length - 3];

        /* Here in "extended" we have extended data to reduce boundary effect */

        temp = MathUtils.convolve(extended, lowDecompositionFilter.getCoeff());
        temp = sampler.downsample(temp);
        temp = windower.window(temp, 2, endIndex / 2 + 2);

        double[] temp2 = MathUtils.convolve(extended, highDecompositionFilter.getCoeff());
        temp2 = sampler.downsample(temp2);
        temp2 = windower.window(temp2, 1, endIndex / 2 + 1);

        System.arraycopy(temp, 0, data, 0, temp.length);
        System.arraycopy(temp2, 0, data, temp.length, temp2.length);
    }

//        @Override
//    public DecompositionResult decompose(double[] data) {//same as in DWT instead of length restriction.
//        Assert.argNotNull(data);//TODO after finding how to do this with lenght = 2.. remove this method.
//        Assert.argCondition(data.length >= 4, "Data length must be >= 4.");
//
//        //finding closest 2nd power value
//        int value = MathUtils.getClosest2PowerValue(data.length);
//        int level = MathUtils.getExact2Power(value);
//
//        //extending if need
//        if(data.length - value < 0) {
//            data = new Extensioner(data)
//                    .schedule(new ZeroPaddingTo2Power(level))
//                    .execute();
//        }
//
//        return decompose(data, level);
//    }
//
//    @Override
//    public DecompositionResult decompose(double[] data, int level) {//same as in DWT instead of length restriction.
//        Assert.argNotNull(data);//TODO after finding how to do this with lenght = 2.. remove this method.
//        Assert.argCondition(level >= 1, "Level argument must be >= 1.");
//        Assert.argCondition(data.length >= 4, "Data length must be >= 4.");
//
//        DecompositionResult result = new DecompositionResult();
//        double[] approximation;
//        double[] details;
//        for (int i = 1; i <= level; ++i) {
//            approximation = decomposeLow(data);
//            details = decomposeHigh(data);
//            result.setApproximation(approximation);
//            result.addDetails(details);
//            result.setLevel(i);
//            if(approximation.length == 1) {
//                break; //we can`t decompose more...
//            }
//            data = approximation; //approximation is data for next decomposition
//        }
//
//        return result;
//    }
//
//    @Override
//    public double[] reconstruct(DecompositionResult decomposition) {
//        return reconstruct(decomposition, 0);
//    }
//
//    @Override
//    public double[] reconstruct(DecompositionResult decomposition, int level) {
//        if (level >= decomposition.getLevel()) {
//            throw new IllegalArgumentException("Level must be less than decomposition level.");
//        }
//
//        double[] reconstructed = decomposition.getApproximation();
//        for (int i = decomposition.getLevel(); i > level; --i) {
//            reconstructed = reconstruct(reconstructed, decomposition.getDetailsList().get(i - 1));
//        }
//
//        return reconstructed;
//    }
//
//    //TODO this implementation is to understand how to do it in common case...
//    //asymmetric filters delays cause very much trouble.
//    private double[] decomposeLow(final double[] data) {//TODO assumes that filter length is 5!!!
//        double[] result;
//
//        Action zeroPaddingToEven = new ZeroPaddingToEven();
//
//        Filter lowDecompositionFilter = filtersFactory.getLowDecompositionFilter();
//
//        int extra = zeroPaddingToEven.getExtensionLength(data.length);
//        double[] temp = new Extensioner(data)
//                .schedule(zeroPaddingToEven)
//                .schedule(new MirrorExtension(1))
//                .execute();
//
//        //TODO ugly but need one more element...
//        result = new double[temp.length + 1];
//        System.arraycopy(temp, 0, result, 0, temp.length);
//        result[result.length - 1] = data[data.length - 3];
//
//        result = MathUtils.convolve(result, lowDecompositionFilter.getCoeff());
//        result = sampler.downsample(result);
//
//        return windower.window(result, 2, (data.length + extra) / 2 + 2);
//    }
//
//    //TODO this implementation is to understand how to do it in common case...
//    //asymmetric filters delays cause very much trouble.
//    private double[] decomposeHigh(final double[] data) {//TODO assumes that filter length is 3!!!
//        double[] result;
//
//        Action zeroPaddingToEven = new ZeroPaddingToEven();
//
//        Filter highDecompositionFilter = filtersFactory.getHighDecompositionFilter();
//
//        int extra = zeroPaddingToEven.getExtensionLength(data.length);
//        double[] temp = new Extensioner(data)
//                .schedule(zeroPaddingToEven)
//                .schedule(new MirrorExtension(1))
//                .execute();
//
//        //TODO ugly but need one more element...
//        result = new double[temp.length + 1];
//        System.arraycopy(temp, 0, result, 0, temp.length);
//        result[result.length - 1] = data[data.length - 3];
//
//        result = MathUtils.convolve(result, highDecompositionFilter.getCoeff());
//        result = sampler.downsample(result);
//
//        return windower.window(result, 1, (data.length + extra) / 2 + 1);
//    }
//
//    private double[] reconstruct(double[] approximation, double[] details) {
//        if (approximation.length != details.length) {
//            throw new IllegalArgumentException("Data vectors must have equal size.");
//        }
//
//        Filter lowReconstructionFilter = filtersFactory.getLowReconstructionFilter();
//        Filter highReconstructionFilter = filtersFactory.getHighReconstructionFilter();
//
//        double[] extendedApproximation;
//        extendedApproximation = new Extensioner(approximation)
//                .schedule(new CopyElementToBegin(0))
//                .schedule(new AddLastToEnd())
//                .execute();
//
//        extendedApproximation = sampler.upsample(extendedApproximation);
//        extendedApproximation = MathUtils.convolve(extendedApproximation, lowReconstructionFilter.getCoeff());
//        extendedApproximation = windower.window(extendedApproximation, 2, approximation.length * 2 + 2);
//
//
//        double[] extendedDetails;
//        extendedDetails = new Extensioner(details)
//                .schedule(new CopyElementToBegin(1))
//                .schedule(new AddLastToEnd())
//                .execute();
//
//        extendedDetails = sampler.upsample(extendedDetails);
//        extendedDetails = MathUtils.convolve(extendedDetails, highReconstructionFilter.getCoeff());
//        extendedDetails = windower.window(extendedDetails, 4, details.length * 2 + 4);
//
//        double[] result = new double[extendedApproximation.length];
//        for (int i = 0; i < result.length; i++) {
//            result[i] = extendedApproximation[i] + extendedDetails[i];
//        }
//
//        return result;
//    }
}
