package ru.ifmo.diplom.kirilchuk.jawelet.util;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.Filter;

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
			// Adding element to delay buffer
			delayLine[count] = data[dataIndex];

			// moving to correct position of buffer
			delayIndex = count;

			// applying filter
			for (int filterIndex = 0; filterIndex < filter.getLength(); ++filterIndex) {
                result[dataIndex] += filter.getCoeff()[filterIndex] * delayLine[delayIndex--];
				if (delayIndex < 0) {// going to next cycle
					delayIndex = filter.getLength() - 1;
				}
			}
			if (++count >= filter.getLength()) {// going to next cycle
				count = 0;
			}
		}
		return result;
    }

	/**
	 * Checks is filter with such coefficients symmetric or not.
	 * It can be easily checked: filter is symmetric if and only if
	 * coeff(i) == coeff(N - 1 - i) for each i, where N is number of coefficients.
	 * 
	 * <p> For example: [1,2,2,1] is symmetric filter, [2,3,4,3,2] is symmetric too.
	 * 
	 * @param coeff coefficients of filter
	 * @return true if filter with such coefficients is symmetric, else otherwise
	 */
	public static boolean isSymmetric(double[] coeff) {
		Assert.argNotNull(coeff);
		int length = coeff.length;
		Assert.argCondition(length > 0, "Must have at least 1 coefficient.");
		for (int index = 0; index < length / 2; ++index) {
			if (coeff[index] != coeff[length - 1 - index]) {
				return false;
			}
		}
		return true;
	}
}
