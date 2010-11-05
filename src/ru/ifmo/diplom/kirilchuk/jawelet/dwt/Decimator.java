package ru.ifmo.diplom.kirilchuk.jawelet.dwt;

/**
 *
 * @author Kirilchuk V.E.
 */
public class Decimator {
    public double[] decimate(double[] data) {
        double[] result = new double[data.length/2];

        for (int i = 0; i < result.length; ++i) {
            result[i] = data[i*2];
        }

        return result;
    }
}
