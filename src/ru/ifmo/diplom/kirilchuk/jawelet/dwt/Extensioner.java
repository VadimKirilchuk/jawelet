package ru.ifmo.diplom.kirilchuk.jawelet.dwt;

/**
 *
 * @author Kirilchuk V.E.
 */
public class Extensioner {

    public double[] extend(double[] data, Filter filter) {
        int dataLength   = data.length;
        int filterLength = filter.getCoeff().length;

        double[] result = new double[dataLength + filterLength];
        System.arraycopy(data, 0, result, 0, dataLength);
        System.arraycopy(data, 0, result, dataLength, filterLength);

        return result;
    }
}
