package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.ifmo.diplom.kirilchuk.jawelet.util.Assert;
import ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils;

/**
 * Class that corresponds for performing of 2D discrete wavelet transform.
 * 
 * @author Kirilchuk V.E.
 */
public class DWTransform2D {
	private static final Logger LOG = LoggerFactory.getLogger(DWTransform2D.class);
	
	private final DWTransform1D transform;

	/**
	 * Constructs 2D transform object with specified underlying 
	 * discrete wavelet transform.
	 * 
	 * @param transform 1D discrete wavelet transform.
	 */
	public DWTransform2D(DWTransform1D transform) {
		this.transform = transform;
	}

	
	public void decomposeInplace(double[][] data, int level) {
		int width = data[0].length;
		int heigth = data.length;
		while (level > 0) {
			decomposeInplace(data, width, heigth);
			width /= 2;
			heigth /= 2;
			level--;
		}
	}
	
	/**
	 * Perform inplace 1 level 2D decomposition.
	 * 
	 * @param data array to transform.
	 */
	public void decomposeInplace(double[][] data, int width, int height) {
//		Assert.checkNotNull(data, "Data can`t be null");
//		int height = data.length;
//		Assert.argCondition(MathUtils.is2power(height), "Data height must be power of two");
//		int width = data[0].length;
//		Assert.argCondition(MathUtils.is2power(width), "Data width must be power of two");
		
		/* Transforming each row */
		double[] buffer;
		for (int row = 0; row < height; ++row) {
			buffer = new double[width];
			System.arraycopy(data[row], 0, buffer, 0, width);
			transform.decomposeInplace(buffer, 1);
			System.arraycopy(buffer, 0, data[row], 0, width);
		}
		
		/* Here we have [L, H] 2D array, where L is approximation part and H is details part */
		/* Now transforming each column */
		for (int column = 0; column < width; ++column) {
			
			/* Creating temporary array from column values */
			buffer = new double[height];
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
	
	public void reconstructInplace(double[][] data, int fromLevel) {
		//divider = 2^(fromLevel - 1) because for 1 level it is 2^0
		int divider = MathUtils.getTwoInPower(fromLevel - 1);
		int width = data[0].length / divider;
		int heigth = data.length / divider;
		for(int level = 0; level < fromLevel; ++level) {
			LOG.debug("Reconstruction step: {}", level);
			reconstructInplace(data, width, heigth);
			width *= 2;
			heigth *= 2;
		}
	}
	
	/**
	 * Perform inplace 2D reconstruction from 1 level 2D decomposition.
	 * 
	 * @param data array to transform.
	 */
	public void reconstructInplace(double[][] data, int width, int height) {
//		Assert.checkNotNull(data, "Data can`t be null");
//		int height = data.length;
//		Assert.argCondition(MathUtils.is2power(height), "Data height must be power of two");
//		int width = data[0].length;
//		Assert.argCondition(MathUtils.is2power(width), "Data width must be power of two");

//		int halfSize = width / 2;
		LOG.debug("Reconstruct inplace (width: {}; height: {})", width, height);
		/* Reconstructing columns */
		for (int column = 0; column < width; ++column) {

			/* Creating temporary array from column values */
			double[] buffer = new double[height];
			for (int row = 0; row < height; ++row) {
				buffer[row] = data[row][column];
			}
			/* Performing reconstruction */
			transform.reconstructInplace(buffer, height / 2);

			/* Changing data */
			for (int row = 0; row < height; ++row) {
				data[row][column] = buffer[row];
			}
		}

//		halfSize = height / 2;
		/* Reconstructing rows */
		for (int row = 0; row < height; ++row) {
			transform.reconstructInplace(data[row], width / 2);
		}
	}
}