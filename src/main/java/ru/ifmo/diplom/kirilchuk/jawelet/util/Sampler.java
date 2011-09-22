package ru.ifmo.diplom.kirilchuk.jawelet.util;

/**
 * Instances of this class correspond for down-sampling  and up-sampling
 * data sequences.
 *
 * @author Kirilchuk V.E.
 */
public class Sampler {

    /**
     * Downsamples data sequence by removing every
     * even element. This method have no side effects, so
     * input array will be unchanged.
     *
     * @param data input sequence.
     * @return downsampled data.
     */
    public double[] downsample(double[] data) {
        Assert.argNotNull(data);
        
        double[] result = new double[(data.length + 1) / 2];

        for (int i = 0; i < result.length; ++i) {
            result[i] = data[i * 2];
        }

        return result;
    }

    /**
     * Upsamples data sequence by adding zeroeth elements
     * on even positions. This method have no side effects, so
     * input array will be unchanged.
     *
     * @param data input sequence.
     * @return upsampled data.
     */
    public double[] upsample(double[] data) {
        Assert.argNotNull(data);

        double[] result = new double[data.length * 2];

        for (int i = 0; i < data.length; ++i) {
            result[i * 2] = data[i];
        }

        return result;
    }
}
