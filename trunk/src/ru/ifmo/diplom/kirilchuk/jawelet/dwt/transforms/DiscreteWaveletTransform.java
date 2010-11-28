package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.DecompositionResult;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.FiltersFactory;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.strategies.impl.StrategiesFactory;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Assert;
import ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils;

/**
 * Abstract class that corresponds for providing basic implementation
 * of decomposition and reconstruction of discrete wavelet transform.
 *
 * @author Kirilchuk V.E.
 */
public abstract class DiscreteWaveletTransform {
    //TODO must be outside class and give strategy in parameter.
    private static final StrategiesFactory strategiesFactory = new StrategiesFactory();//TODO must not be here
    private final FiltersFactory filtersFactory;
    private final TransformStrategy strategy;

    /**
     * Constructs discrete wavelet transform with specified filters factory.
     *
     * @param filtersFactory factory of filters.
     */
    public DiscreteWaveletTransform(FiltersFactory filtersFactory) {
        Assert.argNotNull(filtersFactory);

        this.filtersFactory = filtersFactory;
        this.strategy = strategiesFactory.getDefaultStrategy();
    }

    /**
     * Performs dwt decomposition to maximal level.
     *
     * @param data input vector to decompose.
     * @param level level to which decompose.
     * @return result of decomposition.
     */
    public DecompositionResult decompose(double[] data) {
        Assert.argNotNull(data);
        Assert.valueIs2Power(data.length, "Input data");
        Assert.argCondition(data.length >= 2, "Data length must be >= 2.");

        int level = MathUtils.getExact2Power(data.length);
        
        return decompose(data, level);
    }

    /**
     * Performs dwt decomposition to specified level. If level is
     * more than maximal level availiable for the data, then
     * the maximal level of decomposition will be returned.
     *
     * @param data input vector to decompose.
     * @param level level to which decompose.
     * @return result of decomposition.
     */
    public DecompositionResult decompose(double[] data, int level) {
        Assert.argNotNull(data);
        Assert.valueIs2Power(data.length, "Input data");
        Assert.argCondition(data.length >= 2, "Data length must be >= 2.");
        Assert.argCondition(level > 1, "Level argument must be >= 1.");

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
        }

        return result;
    }

    public double[] decomposeLow(double[] data) {
        return strategy.decomposeLow(data, filtersFactory.getLowDecompositionFilter());
    }

    public double[] decomposeHigh(double[] data) {
        return strategy.decomposeHigh(data, filtersFactory.getHighDecompositionFilter());
    }

    public double[] reconstruct(double[] approximation, double[] details) {
        return strategy.reconstruct(approximation, details,
                                    filtersFactory.getLowReconstructionFilter(),
                                    filtersFactory.getHighReconstructionFilter());
    }

    public FiltersFactory getFiltersFactory() {
        return filtersFactory;
    }
}
