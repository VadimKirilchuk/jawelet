package ru.ifmo.diplom.kirilchuk.coder.predict;

/**
 * Restores original matrix from difference between original and prediction
 */
public class Restorator {

	private int[][] difference;
	private int[][] zones;
	private double[] passiveFilter;
	private double[] activeFilter;

	public Restorator(int[][] difference, int[][] zones,
			double[] passiveFilter, double[] activeFilter) {
		this.difference = difference;
		this.zones = zones;
		this.passiveFilter = passiveFilter;
		this.activeFilter = activeFilter;
	}

	/**
	 * Restores original from prediction
	 */
	public int[][] buildOriginal() {
		int size = difference.length;

		int[][] result = new int[size][size];

		result[0][0] = difference[0][0];
		// adding left to right for first row
		for (int col = 1; col < size; ++col) {
			int data = difference[0][col] + result[0][col - 1];
			result[0][col] = data;
		}

		// adding top to bottom for first column
		for (int row = 1; row < size; ++row) {
			int data = difference[row][0] + result[row - 1][0];
			result[row][0] = data;
		}

		// now filling difference for other elements
		int lastCol = size - 1;
		for (int row = 1; row < size; ++row) {
			for (int col = 1; col < size - 1; ++col) {
				if (zones[row][col] == 0) {
					result[row][col] = calculateOriginal(result, row, col, passiveFilter);
				} else {
					result[row][col] = calculateOriginal(result, row, col, activeFilter);
				}
			}
			if (zones[row][lastCol] == 0) {
				result[row][lastCol] = calculateLC(result, row, lastCol, passiveFilter);
			} else {
				result[row][lastCol] = calculateLC(result, row, lastCol, activeFilter);
			}
		}
		
		return result;
	}

	private int calculateOriginal(int[][] result, int row, int col,
			double[] filter) {
		double predDouble = result[row - 1][col - 1] * filter[0]
				+ result[row - 1][col] * filter[1] + result[row - 1][col + 1]
				* filter[2] + result[row][col - 1] * filter[3];
		int prediction = (int) Math.round(predDouble);

		return prediction + difference[row][col];
	}
	
	private int calculateLC(int[][] result, int row, int col,
			double[] filter) {
		double predDouble = result[row - 1][col - 1] * filter[0]
						  + result[row - 1][col] * filter[1]
						  + result[row - 1][col] * filter[2]
						  + result[row][col - 1] * filter[3];
		int prediction = (int) Math.round(predDouble);

		return prediction + difference[row][col];
	}
}
