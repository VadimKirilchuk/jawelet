package ru.ifmo.diplom.kirilchuk.coder.codebook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneratorResult {

	public static class Optimal {
		public int originalValue;
		public FilterProbabilityPair filter;
		public int difference;
	}
	
    public FilterProbabilityPair[] filterPack;
    public Optimal[][] optimal;
    public Map<Integer, Double> errorProbability;

    public static GeneratorResult createGeneratorResult(int matrixSize,
            List<int[]> data, List<double[]> filters, List<List<int[]>> subsets) {

        GeneratorResult result = new GeneratorResult();

        int filtersSize = filters.size();
        int dataSize = data.size();

        FilterProbabilityPair[] filterPack = new FilterProbabilityPair[filtersSize];
        Optimal[][] optimal = new Optimal[matrixSize][matrixSize];
        Map<Integer, Integer> errorCount = new HashMap<Integer, Integer>();
        for (int filterIndex = 0; filterIndex < filtersSize; ++filterIndex) {
            double[] filter = filters.get(filterIndex);
            List<int[]> subset = subsets.get(filterIndex);
            
            int size = subset.size();
            FilterProbabilityPair pair = new FilterProbabilityPair(filter, -log2((double) size / dataSize), filterIndex);
            filterPack[filterIndex] = pair;

            for (int[] vector : subset) {
            	// fill matrix of optimal filters
            	int error = countError(filter, vector);
            	increaseCount(errorCount, error);
            	
            	int row = vector[5] - 1;
                int col = vector[6] - 1;
                
                Optimal optimum = new Optimal();
                optimum.originalValue = vector[4];
                optimum.difference = error;
                optimal[row][col] = optimum;
            }
        }

        // setting errorProbability
        Map<Integer, Double> errorProbability = new HashMap<Integer, Double>();
        for (Map.Entry<Integer, Integer> entry : errorCount.entrySet()) {
            Integer error = entry.getKey();
            Integer count = entry.getValue();
            Double probability = -log2((double) count / dataSize);
            errorProbability.put(error, probability);
        }

        result.filterPack = filterPack;
        result.optimal = optimal;
        result.errorProbability = errorProbability;

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

    public static int countError(double[] filter, int[] data) {
        double prediction = data[0] * filter[0] + data[1] * filter[1] + data[2] * filter[2] + data[3] * filter[3];
        int err = data[4] - (int) Math.round(prediction);

        return err;
    }

    public static void liftFilter(FilterProbabilityPair[] filterPack, int index) {
        if (index == 0) {
            return;
        }

        FilterProbabilityPair temp = filterPack[index];
        for (int i = index; i > 0; --i) {
            filterPack[i] = filterPack[i - 1];
        }
        filterPack[0] = temp;
    }

    private GeneratorResult() {}
}
