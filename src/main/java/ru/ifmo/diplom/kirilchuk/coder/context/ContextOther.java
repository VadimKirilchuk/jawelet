package ru.ifmo.diplom.kirilchuk.coder.context;

import java.util.Arrays;

public class ContextOther {
    private int[] lowContext;
    private boolean active;
    private int[] context;
    
    /**
     * Helper method to get context number depending on context values 
     * @param context context values
     * @return number of context
     */
    public static int contextNumber(ContextOther context) {
        //TODO: some function
        //currently always returns 3
        return 3;
    }
    
    /**
     * Creates context for specified point.
     * The difference from LL context: we use not only neighboor points but also points from LL!
     * We take nine points from LL and 3 points from nearest points. 
     * 
     * @param dataLL LL data
     * @param data matrix data
     * @param zones zones
     * @param row row position in data and zones
     * @param col col position in data and zones
     * @param value actual to create context for
     * @return context object
     */
    public static ContextOther create(int[][] dataLL, int[][] data, int[][] zones, int row, int col) {
        ContextOther result = new ContextOther();
        result.lowContext = new int[9];
        result.active = (zones[row][col] == 1);
        result.context = new int[3];
        int size = dataLL.length;
        
        if (row == 0 || col == 0) {
            throw new IllegalArgumentException("No context for row=0 and col=0");
        }
        
        result.lowContext[0] = dataLL[row - 1][col - 1];
        result.lowContext[1] = dataLL[row - 1][col];
        if (col != size - 1) {
            result.lowContext[2] = dataLL[row - 1][col + 1];
            result.lowContext[5] = dataLL[row][col + 1];
        } else {
            result.lowContext[2] = dataLL[row - 1][col];
            result.lowContext[5] = dataLL[row][col];
        }
        result.lowContext[3] = dataLL[row][col - 1];
        result.lowContext[4] = dataLL[row][col];
        
        if (row != size - 1) {
            result.lowContext[6] = dataLL[row + 1][col - 1];
            result.lowContext[7] = dataLL[row + 1][col];
            if (col != size - 1) {
                result.lowContext[8] = dataLL[row + 1][col + 1];
            } else {
                result.lowContext[8] = dataLL[row + 1][col];
            }
        } else {
            result.lowContext[6] = dataLL[row][col - 1];
            result.lowContext[7] = dataLL[row][col];
            if (col != size - 1) {
                result.lowContext[8] = dataLL[row][col + 1];
            } else {
                result.lowContext[8] = dataLL[row][col];
            }
        }
        
        result.context[0] = data[row - 1][col - 1];
        result.context[1] = data[row - 1][col];
        result.context[2] = data[row][col - 1];
        
        return result;
    }

    public int[] getLowContext() {
        return lowContext;
    }

    public boolean isActive() {
        return active;
    }

    public int[] getContext() {
        return context;
    }

    @Override
    public String toString() {
        return "[lowContext=" + Arrays.toString(lowContext) + ", active=" + active + ", context="
                + Arrays.toString(context) + "]";
    }
    
    public static void main(String[] args) {
        int[][] dataLL = {
                {1,2,3,4},
                {5,6,7,8},
                {1,2,3,4},
                {5,6,7,8},
        };
        
        int[][] data = {
                {10,11,12,13},
                {14,15,16,17},
                {10,11,12,13},
                {14,15,16,17}
        };
        
        int[][] zones = {
                {1,1,1,1},
                {1,1,1,1},
                {1,1,1,1},
                {1,1,1,1}
        };
        
        System.out.println(create(dataLL, data, zones, 3, 3));
    }
}
