package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.legall.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.legall.impl.LeGallFiltersFactory;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.DWTransform1D;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Assert;

/**
 * Class that represents DiscreteWaveletTransform on Le Gall filter bank basis
 * using so-called "lifting" scheme. Also it performs integer-to-integer wavelet transform.
 * It means that for image data(integer data) decomposition will
 * produce integer coefficients.
 * 
 * @author Kirilchuk V.E.
 */
public class LeGallLiftingWaveletTransform extends DWTransform1D {
	public LeGallLiftingWaveletTransform() {
		super(new LeGallFiltersFactory());
	}

	@Override
	public void decomposeInplace(double[] data, int toLevel) {
		Assert.checkNotNull(data, "Data vector can`t be null.");
		Assert.argCondition(toLevel >= 1, "Level must be >= 1");
		Assert.valueIs2Power(data.length, "Data must have at least 2^level elements.");
		int n = data.length;
		for (int step = 0; step < toLevel; ++step) {
			decomposeInplace1Lvl(data, n);
			n /= 2;
		}
	}

	@Override
	public void reconstructInplace(double[] data, int n) {
		Assert.checkNotNull(data, "Data vector can`t be null.");

        int endIndex = n * 2;
        
		double a;
		int pos;

		// Unpack
		double[] temp = new double[endIndex];
		for (pos = 0; pos < endIndex / 2; pos++) {
			temp[pos * 2] = data[pos];
			temp[pos * 2 + 1] = data[pos + endIndex / 2];
		}
		for (pos = 0; pos < endIndex; pos++) {
			data[pos] = temp[pos];
		}
		
		// Undo update 1
		a = -0.25;
		for (pos = 2; pos < endIndex; pos += 2) {
			data[pos] -= ((int)(data[pos - 1] + data[pos + 1] + 2) >> 2);
		}
		data[0] += 4 * a * data[1];

		// Undo predict 1
		a = 0.5;
		for (pos = 1; pos < endIndex - 2; pos += 2) {
			data[pos] += ((int)(data[pos - 1] + data[pos + 1])>>1);
		}
		data[endIndex - 1] += 2 * a * data[endIndex - 2];

	}

	/**
	 * +1 level of decomposition.
	 * 
	 * <pre>
	 * For example: 
	 * We have vector 1,1,1,1,2,2,2,2 where 1 are approximation and 2 are details.
	 * To decompose one more time we need call
	 * decomposeInplace1Lvl([1,1,1,1,2,2,2,2], 4);
	 * 4 - index where details start and approximations ended.
	 * </pre>
	 * 
	 * @param data
	 *            vector with approximation and details.
	 * @param endIndex
	 *            index where details start and approximations ended.
	 */
	private void decomposeInplace1Lvl(double[] data, int endIndex) {
		double coeff;
		int pos;

		// Predict 1
		coeff = -0.5;
		for (pos = 1; pos < endIndex - 2; pos += 2) {
			data[pos] -= ((int) (data[pos - 1] + data[pos + 1]) >> 1);
		}
		data[endIndex - 1] += 2 * coeff * data[endIndex - 2];

		// Update 1
		coeff = 0.25;
		for (pos = 2; pos < endIndex; pos += 2) {
			data[pos] += ((int) (data[pos - 1] + data[pos + 1] + 2) >> 2);
		}
		data[0] += 4 * coeff * data[1];

		// Pack
		double[] temp = new double[endIndex];
		for (pos = 0; pos < endIndex; pos++) {
			if (pos % 2 == 0) {
				temp[pos / 2] = data[pos];
			} else {
				temp[endIndex / 2 + pos / 2] = data[pos];
			}
		}

		for (pos = 0; pos < endIndex; pos++) {
			data[pos] = temp[pos];
		}
	}
}
