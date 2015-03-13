package ru.ifmo.diplom.kirilchuk.coder.codebook;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.linear.Array2DRowRealMatrix;
import org.apache.commons.math.linear.LUDecompositionImpl;
import org.apache.commons.math.linear.MatrixUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.apache.commons.math.linear.SingularMatrixException;

public class CodeBookGenerator {
    private int iterationsLimit = 2;

    private List<int[]> data = new ArrayList<int[]>(1024);
    private List<double[]> filters = new ArrayList<double[]>(16);
    private List<List<int[]>> subsets;

    public void addFilter(double[] filter) {
        filters.add(filter);
    }

    public void addData(int[] dataVector) {
        data.add(dataVector);
    }

    public int getDataSize() {
        return data.size();
    }

    /**
     * @return trained filters
     */
    public GeneratorResult perform(int matrixSize) {
        // System.out.println("Train data size: " + data.size());
        // System.out.println("Filters size: " + filters.size());

        List<double[]> optimizedFilters = performRec(filters, 1);

        return GeneratorResult.createGeneratorResult(matrixSize, data, optimizedFilters, subsets);
    }


    public List<List<int[]>> getSubsets() {
        return subsets;
    }

    private List<double[]> performRec(List<double[]> filters, int iteration) {
        if (iteration == iterationsLimit) {
            return filters;
        }
        // System.out.println();
        // System.out.println("Iteration: " + iteration);

        int filtersSize = filters.size();
        int dataSize = data.size();

        double[][] errorData = calculateErrors(filters, filtersSize, dataSize);
        subsets = generateSubsets(filtersSize, dataSize, errorData);

        // EntropyCounter<Integer> entropyCounter = new EntropyCounter<Integer>();
        // MeanSquareErrorCounter errorCounter = new MeanSquareErrorCounter();

        for (int filterIndex = 0; filterIndex < subsets.size(); ++filterIndex) {
            List<int[]> filterSubset = subsets.get(filterIndex);
            double[] filter = filters.get(filterIndex);

            int size = filterSubset.size();
            double[] y = new double[size];
            double[][] x = new double[size][];
            for (int dataIndex = 0; dataIndex < size; ++dataIndex) {
                int[] dataInts = filterSubset.get(dataIndex);
                x[dataIndex] = new double[] { dataInts[0], dataInts[1], dataInts[2], dataInts[3] };
                y[dataIndex] = dataInts[4];
                // double symbol = dataInts[4]
                // - (filter[0] * dataInts[0] + filter[1] * dataInts[1] + filter[2] * dataInts[2] + filter[3] *
                // dataInts[3]);
                // errorCounter.add(symbol * symbol);
                // entropyCounter.addSymbol((int) Math.round(symbol));
            }

            double[] optimized = null;
            if (size == 0) {
                // System.out.println("No data for filter " + Arrays.toString(filter));
            } else {
                try {
                    optimized = optimizeFilter(x, y);
                    // System.out.println("Changing filter [" + Arrays.toString(filter) + "] to ["
                    // + Arrays.toString(optimized)
                    // + "]");
                    // System.out.println("Subset size: " + size);
                    filters.set(filterIndex, optimized);
                } catch (SingularMatrixException e) {
                    // System.out.println("Filter " + Arrays.toString(filter) + " can`t be optimized. Singular data.");
                }
            }
        }
        // System.out.println("MSE: " + errorCounter.getMeanSquareError());
        // System.out.println("Entropy: " + entropyCounter.getEntropy());
        // repeat with new filters
        return performRec(filters, iteration + 1);
    }

    private double[] optimizeFilter(double[][] x, double[] y) throws SingularMatrixException {
        RealMatrix X = MatrixUtils.createRealMatrix(x);
        RealMatrix Y = new Array2DRowRealMatrix(y);

        RealMatrix transposedX = X.transpose();
        RealMatrix G = transposedX.multiply(X);
        RealMatrix invertedG = new LUDecompositionImpl(G).getSolver().getInverse();
        // result coefficients
        return invertedG.multiply(transposedX).multiply(Y).getColumn(0);
    }

    private List<List<int[]>> generateSubsets(int filtersSize, int dataSize, double[][] errorData) {
        List<List<int[]>> subsets = new ArrayList<List<int[]>>(filtersSize);
        for (int i = 0; i < filtersSize; ++i) {
            // initializing
            subsets.add(new ArrayList<int[]>());
        }

        for (int dataIndex = 0; dataIndex < dataSize; ++dataIndex) {
            double[] results = new double[filtersSize];
            // converting column to row to find the best filter for data
            for (int filterIndex = 0; filterIndex < filtersSize; ++filterIndex) {
                results[filterIndex] = errorData[filterIndex][dataIndex];
            }

            // choosing closest filter index
            int index = findMinIndex(results);
            int[] dataToPut = data.get(dataIndex);
            subsets.get(index).add(dataToPut);
        }

        return subsets;
    }

    private double[][] calculateErrors(List<double[]> filters, int filtersSize, int dataSize) {
        // calculating errors for filters
        double[][] errorData = new double[filtersSize][dataSize];
        for (int filterIndex = 0; filterIndex < filtersSize; ++filterIndex) {
            double[] filterError = errorData[filterIndex];
            double[] filter = filters.get(filterIndex);
            for (int dataIndex = 0; dataIndex < dataSize; ++dataIndex) {
                int[] element = data.get(dataIndex);
                double prediction = element[0] * filter[0] + element[1] * filter[1] + element[2] * filter[2] + element[3] * filter[3];
                double err = element[4] - prediction;
                filterError[dataIndex] = err * err;
            }
        }

        return errorData;
    }

    private static int findMinIndex(double[] array) {
        double minimum = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] < minimum) {
                minimum = array[i];
                index = i;
            }
        }
        return index;
    }
}