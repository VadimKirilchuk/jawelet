package ru.ifmo.diplom.kirilchuk.coder.predict;

import ru.ifmo.diplom.kirilchuk.coder.codebook.CodeBookGenerator;
import ru.ifmo.diplom.kirilchuk.coder.codebook.GeneratorResult;
import ru.ifmo.diplom.kirilchuk.coder.zones.ZoneByDispersionFinder;
import ru.ifmo.diplom.kirilchuk.util.Assert;

public class LLPredictioner implements Predictor {

	private double[][] initFilterSet;
	private int[][] data;
	private int[][] zones;
	private int dispersionThreshold = 4;
	
	private double[] passiveFilter;
	private double[] activeFilter;

	public LLPredictioner(int[][] data, double[][] filterSet) {
		Assert.argCondition(filterSet.length == 1, "Only size 1 is supported now");
		this.data = data;
		this.initFilterSet = filterSet;
	}

	/* (non-Javadoc)
	 * @see jawelet.statistic.coder.Predictor#buildDifference()
	 */
	@Override
	public int[][] buildDifference() {
		int size = data.length;

		ZoneByDispersionFinder finder = new ZoneByDispersionFinder(dispersionThreshold);
		zones = finder.buildZones(data);

		CodeBookGenerator passiveGenerator = new CodeBookGenerator();
		CodeBookGenerator activeGenerator = new CodeBookGenerator();
		for (double[] filter : initFilterSet) {
			passiveGenerator.addFilter(filter);
			activeGenerator.addFilter(filter);
		}

		// adding data to generators for central zone
		for (int row = 1; row < size; ++row) {
			for (int col = 1; col < size - 1; ++col) {
				int[] element = new int[]{
						data[row - 1][col - 1],
						data[row - 1][col],
						data[row - 1][col + 1],
						data[row][col - 1],
						data[row][col],
						// position
						row, col};
				if (zones[row][col] == 0) {
					passiveGenerator.addData(element);
				} else {
					activeGenerator.addData(element);
				}
			}
		}

		// don`t forget last column
		int lastCol = size - 1;
		for (int row = 1; row < size; ++row) {
			int[] element = new int[]{
					data[row - 1][lastCol - 1],
					data[row - 1][lastCol],
					data[row - 1][lastCol], // double
																	// previous
					data[row][lastCol - 1],
					data[row][lastCol],
					// position
					row, lastCol};
			if (zones[row][lastCol] == 0) {
				passiveGenerator.addData(element);
			} else {
				activeGenerator.addData(element);
			}
		}

		GeneratorResult passiveGeneratorResult = passiveGenerator.perform(size - 1);
		GeneratorResult activeGeneratorResult = activeGenerator.perform(size - 1);
		
		passiveFilter = passiveGeneratorResult.filterPack[0].filter;
		activeFilter = activeGeneratorResult.filterPack[0].filter;

		int[][] result = new int[size][size];
		result[0][0] = data[0][0];
		// subtracting left from right for first row
		for (int col = 1; col < size; ++col) {
			int diff = data[0][col] - data[0][col - 1];
			result[0][col] = diff;
		}
		// subtracting top from bottom for first column
		for (int row = 1; row < size; ++row) {
			int diff = data[row][0] - data[row - 1][0];
			result[row][0] = diff;
		}

		// now filling difference for other elements
		for (int row = 1; row < size; ++row) {
			for (int col = 1; col < size; ++col) {
				int shifterRow = row - 1;
				int shiftedCol = col - 1;
				if (zones[row][col] == 0) {
					result[row][col] = passiveGeneratorResult.optimal[shifterRow][shiftedCol].difference;
				} else {
					result[row][col] = activeGeneratorResult.optimal[shifterRow][shiftedCol].difference;
				}
			}
		}

		return result;
	}

    public int getDispersionThreshold() {
        return dispersionThreshold;
    }

    public void setDispersionThreshold(int dispersionThreshold) {
        this.dispersionThreshold = dispersionThreshold;
    }

    public int[][] getZones() {
        return zones;
    }

    public double[] getPassiveFilter() {
        return passiveFilter;
    }

    public double[] getActiveFilter() {
        return activeFilter;
    }
}
