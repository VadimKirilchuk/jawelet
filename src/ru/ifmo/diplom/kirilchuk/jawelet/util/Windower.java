package ru.ifmo.diplom.kirilchuk.jawelet.util;

/**
 *
 * @author Kirilchuk V.E.
 */
public class Windower {

    /**
     * Windowing data by getting only elements from start to end.
     * Note that first index in data is 0, so the first element
     * is at the zeroth position!
     *
     * @param data elements to window.
     * @param start index of start element in data array to take.
     * @param end index of end element in data array to take.
     * @return windowed data.
     */
    public double[] window(double[] data, int start, int end) {

        double[] result = new double[end - start];
        System.arraycopy(data, start, result, 0, end - start);

        return result;
    }
}
