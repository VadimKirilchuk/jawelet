package ru.ifmo.diplom.kirilchuk.jawelet.util;

import java.util.HashMap;
import java.util.Map;

public final class AnalyzeUtils {

	private AnalyzeUtils() {
	}

	/**
	 * Calculates peak signal-to-noise ratio The PSNR is most commonly used as a
	 * measure of quality of reconstruction of lossy compression codecs (e.g.,
	 * for image compression). The signal in this case is the original data, and
	 * the noise is the error introduced by compression.
	 * 
	 * <p>
	 * PSNR = (max^2 * width * heigth) / summ(|x-y|^2)
	 * 
	 * <p>
	 * expected to be Infinity if original and actual are the same
	 * 
	 * @param max
	 * @param original
	 * @param actual
	 * @return
	 */
	public static double calculatePSNR(double[][] original, double[][] actual,
			int max) {
		int heigth = original.length;
		Assert.argNotNull(original, actual);
		Assert.argCondition(heigth == actual.length, "Sizes must be same");
		Assert.argCondition(heigth > 0, "original data must be non empty");

		int width = original[0].length;
		Assert.argCondition(width == actual[0].length, "Sizes must be same");

		long numerator = max * max * width * heigth;

		double denominator = 0;
		double absDifference = 0;
		for (int row = 0; row < heigth; ++row) {
			for (int col = 0; col < width; ++col) {
				absDifference = Math.abs(original[row][col] - actual[row][col]);
				denominator += (absDifference * absDifference);
			}
		}

		double result = 10 * Math.log10(numerator / denominator);

		return result;
	}

	public static double calculateEntropy(int[][] data, int startx, int starty,
			int endx, int endy) {
		// counting frequency
		int elementsCount = (endx - startx) * (endy - starty);
		Map<Integer, Integer> frequencyMap = new HashMap<Integer, Integer>();
		for (int row = startx; row < endx; ++row) {
			for (int col = starty; col < endy; ++col) {
				increaseCount(frequencyMap, data[row][col]);
			}
		}

		// probability is symbolCount/elementsCount
		// so counting entropy as summ(-p(x)*log(p(x))) where log base is two
		double result = 0;
		for (Integer symbolCount : frequencyMap.values()) {
			double p = (double) symbolCount / elementsCount;
			result -=  p * log2(p);
		}

		return result;
	}

	private static void increaseCount(Map<Integer, Integer> data, int element) {
		Integer value = data.get(element);
		if (value == null) {
			value = new Integer(0);
			data.put(element, value);
		}
		data.put(element, ++value);
	}

	public static double log2(double num) {
		return (Math.log(num) / Math.log(2));
	}
}
