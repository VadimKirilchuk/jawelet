package ru.ifmo.diplom.kirilchuk.jawelet.util;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;

/**
 *
 * @author Kirilchuk V.E.
 */
public final class FilterUtils {

    private FilterUtils() {}

    public static double[] filter(double[] data, Filter filter) {
        Assert.argNotNull(filter, data);

        double[] delayLine = new double[filter.getLength()];
        double[] result = new double[data.length];
        int count = 0;
        int delayIndex = 0;

        for (int dataIndex = 0; dataIndex < data.length; ++dataIndex) {
            //Adding element to delay buffer
            delayLine[count] = data[dataIndex];

            //moving to correct position of buffer
            delayIndex = count;

            //applying filter
            for (int filterIndex = 0; filterIndex < filter.getLength(); ++filterIndex) {
                result[dataIndex] += filter.getCoeff()[filterIndex] * delayLine[delayIndex--];
                if (delayIndex < 0) {//going to next cycle
                    delayIndex = filter.getLength() - 1;
                }
            }
            if (++count >= filter.getLength()) {//going to next cycle
                count = 0;
            }
        }
        return result;
    }
}
