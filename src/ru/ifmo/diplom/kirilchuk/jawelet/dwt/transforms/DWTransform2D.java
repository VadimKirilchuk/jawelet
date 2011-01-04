package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms;

import ru.ifmo.diplom.kirilchuk.jawelet.util.Assert;
import ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils;

public class DWTransform2D {
	private final DiscreteWaveletTransform transform;

	public DWTransform2D(DiscreteWaveletTransform transform) {
		this.transform = transform;
	}

	/**
	 * Perform 1 level 2D decomposition
	 * 
	 * @param data
	 */
	public void decomposeInplace(double[][] data) {
		/* Preconditions: 
		 * All data arrays not null.
		 * Width and height are both power of two.
		 * All rows have the same length. 
		 */
		Assert.checkNotNull(data, "Data can`t be null");
		int height = data.length;
		Assert.argCondition(height > 0, "Array can`t be zero-length");
		Assert.argCondition(MathUtils.is2power(height), "Data height must be power of two");
		Assert.checkNotNull(data[0], "Data rows can`t be null");
		int width = data[0].length;
		Assert.argCondition(width > 0, "Array can`t be zero-length");
		Assert.argCondition(MathUtils.is2power(width), "Data width must be power of two");
		for (int i = 1; i < height; ++i) {
			Assert.checkNotNull(data[i], "Data rows can`t be null");
			Assert.argCondition(data[i].length == width, "Data rows must have same length");
		}
		
		/* Transforming each row */
		for (int i = 0; i < height; ++i) {
			transform.decomposeInplace(data[i], 1);
		}
		
		/* Here we have [L, H] 2D array, where L is approximation part and H is details part */
		/* Now transforming each column */
		for (int column = 0; column < width; ++ column) {
			
			/* Creating temporary array from column values */
			double[] buffer = new double[height];
			for (int row = 0; row < height; ++row) {
				buffer[row] = data[row][column];
			}
			/* Performing transform */
			transform.decomposeInplace(buffer, 1);
			
			/* Changing data */
			for (int row = 0; row < height; ++row) {
				data[row][column] = buffer[row];
			}
		}
		/* And now we have 
		 * [ LL , LH ] where LH is approximation of details
		 * [ HL , HH ] where HL is details of approximation
		 */
	}
}