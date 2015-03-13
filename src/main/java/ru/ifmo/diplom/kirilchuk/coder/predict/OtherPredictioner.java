package ru.ifmo.diplom.kirilchuk.coder.predict;

import ru.ifmo.diplom.kirilchuk.coder.codebook.CodeBookGenerator;
import ru.ifmo.diplom.kirilchuk.coder.codebook.GeneratorResult;
import ru.ifmo.diplom.kirilchuk.coder.codebook.util.EntropyCounter;
import ru.ifmo.diplom.kirilchuk.util.ArrayUtils;
import ru.ifmo.diplom.kirilchuk.util.Assert;

public class OtherPredictioner implements Predictor {
    
    private double[][] initFilterSet;
    private int[][] LL;
    private int[][] data;
    private int[][] zones;

    public double[] filter;
    public int[][] edges;   
    
    public OtherPredictioner(int[][] data, double[][] filterSet, int[][] LL, int[][] zones) {
        Assert.argCondition(filterSet.length == 1, "Only size 1 is supported now");
        this.data = data;
        this.LL = LL;
        this.zones = zones;
        this.initFilterSet = filterSet;
    }

    /**
     * Builds prediction and then subtracts this prediction from real data
     */
    public int[][] buildDifference() {
        int size = data.length;

//        edges = new int[size][size];
//        buildEdges();
        
        EntropyCounter<Integer> edgeEntropyCounter = new EntropyCounter<Integer>();
        EntropyCounter<Integer> otherEntropyCounter = new EntropyCounter<Integer>();
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Integer value = data[row][col];
                if (zones[row][col] == 1) {
                    edgeEntropyCounter.addSymbol(value);
                } else {
                    otherEntropyCounter.addSymbol(value);
                }
            }
        }
        System.out.println("Active entropy: " + edgeEntropyCounter.getEntropy());
        System.out.println("Passive entropy: " + otherEntropyCounter.getEntropy());

        CodeBookGenerator generator = new CodeBookGenerator();
        for (double[] filter : initFilterSet) {
            generator.addFilter(filter);
        }

        // adding data to generators for central zone
        for (int row = 1; row < size - 1; ++row) {
            for (int col = 1; col < size - 1; ++col) {
                int[] element = new int[] { LL[row - 1][col - 1], LL[row - 1][col + 1], LL[row + 1][col - 1],
                        LL[row + 1][col + 1], data[row][col],
                        // position
                        row, col };
                generator.addData(element);
            }
        }

        GeneratorResult generatorResult = generator.perform(size - 1);
        filter = generatorResult.filterPack[0].filter;

        int[][] result = new int[size][size];
        result[0][0] = data[0][0];
        int lastRow = size - 1;
        int lastCol = size - 1;
        // subtracting left from right for first row
        for (int col = 1; col < size; ++col) {
            int diff = data[0][col] - data[0][col - 1];
            result[0][col] = diff;
        }
        // subtracting left from right for last row
        for (int col = 1; col < size; ++col) {
            int diff = data[lastRow][col] - data[lastRow][col - 1];
            result[lastRow][col] = diff;
        }
        // subtracting top from bottom for first column
        for (int row = 1; row < size; ++row) {
            int diff = data[row][0] - data[row - 1][0];
            result[row][0] = diff;
        }
        // subtracting top from bottom for last column
        for (int row = 1; row < size; ++row) {
            int diff = data[row][lastCol] - data[row - 1][lastCol];
            result[row][lastCol] = diff;
        }

        // now filling difference for other elements
        for (int row = 1; row < size - 1; ++row) {
            for (int col = 1; col < size - 1; ++col) {
                int shifterRow = row - 1;
                int shiftedCol = col - 1;
                result[row][col] = generatorResult.optimal[shifterRow][shiftedCol].difference;
            }
        }

        return result;
    }

    private void buildEdges() {
        int size = edges.length;

        for (int row = 0; row < size; row += 4) {
            for (int col = 0; col < size; col += 4) {
                int currentBlockZone = zones[row][col];
                // watching up
                if (row > 4) {
                    fillEdge(row, col, Direction.UP, currentBlockZone);
                }
                if (row < size - 1 - 4) {
                    fillEdge(row, col, Direction.DOWN, currentBlockZone);
                }
                if (col > 4) {
                    fillEdge(row, col, Direction.LEFT, currentBlockZone);
                }
                if (col < size - 1 - 4) {
                    fillEdge(row, col, Direction.RIGHT, currentBlockZone);
                }

            }
        }
    }

    private static enum Direction {
        UP, LEFT, RIGHT, DOWN
    }

    private void fillEdge(int row, int col, Direction direction, int currentZone) {
        int neighboorZone;
        switch (direction) {
            case UP: {
                neighboorZone = zones[row - 1][col];
                if (neighboorZone != currentZone) {
                    ArrayUtils.fillMatrix(edges, row - 1, row + 1, col, col + 4, 1);
                }
                break;
            }
            case LEFT: {
                neighboorZone = zones[row][col - 1];
                if (neighboorZone != currentZone) {
                    ArrayUtils.fillMatrix(edges, row, row + 4, col - 1, col + 1, 1);
                }
                break;
            }
            case RIGHT: {
                neighboorZone = zones[row][col + 4];
                if (neighboorZone != currentZone) {
                    ArrayUtils.fillMatrix(edges, row, row + 4, col + 3, col + 5, 1);
                }
                break;
            }
            case DOWN: {
                neighboorZone = zones[row + 4][col];
                if (neighboorZone != currentZone) {
                    ArrayUtils.fillMatrix(edges, row + 3, row + 5, col, col + 4, 1);
                }
                break;
            }
            default: {
                throw new RuntimeException("Developer is Petya");
            }
        }
    }   
    
    public static void main(String[] args) {
        int[][] zones = {
                {0,0,0,0,1,1,1,1,0,0,0,0},
                {0,0,0,0,1,1,1,1,0,0,0,0},
                {0,0,0,0,1,1,1,1,0,0,0,0},
                {0,0,0,0,1,1,1,1,0,0,0,0},
                {1,1,1,1,0,0,0,0,1,1,1,1},
                {1,1,1,1,0,0,0,0,1,1,1,1},
                {1,1,1,1,0,0,0,0,1,1,1,1},
                {1,1,1,1,0,0,0,0,1,1,1,1},
                {0,0,0,0,1,1,1,1,0,0,0,0},
                {0,0,0,0,1,1,1,1,0,0,0,0},
                {0,0,0,0,1,1,1,1,0,0,0,0},
                {0,0,0,0,1,1,1,1,0,0,0,0},
        };
        OtherPredictioner pred = new OtherPredictioner(null, new double[][]{{1,2,3,4}}, null, zones);
        pred.edges = new int[12][12];
        pred.buildEdges();
        ArrayUtils.print(pred.edges);
    }
}
