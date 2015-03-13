package ru.ifmo.diplom.kirilchuk.coder.context;

import java.util.Arrays;

public class ContextLL {
    private int value;
    private boolean active;
    private int[] context;
    
    /**
     * Helper method to get context number depending on context values 
     * @param context context values
     * @return number of context
     */
    public static int contextNumber(ContextLL context) {
        int nearSumm = context.context[0] + context.context[1] + context.context[2];
        if (context.active) { 
            if (Math.abs(nearSumm) < 10) {
                return 0;
            } else if (Math.abs(nearSumm) < 20) {
                return 1;
            } else if (Math.abs(nearSumm) < 40) {
                return 2;
            } else {
                return 3;
            }
        } else { //passive
            if (Math.abs(nearSumm) < 40) {
                return 0;
            } else if (Math.abs(nearSumm) < 100) {
                return 1;
            } else if (Math.abs(nearSumm) < 200) {
                return 2;
            } else {
                return 3;
            }
        }
    }
    
    /**
     * Creates context for specified point 
     * 
     * @param data matrix data
     * @param zones zones
     * @param row row position in data and zones
     * @param col col position in data and zones
     * @param value actual to create context for
     * @return context object
     */
    public static ContextLL create(int[][] data, int[][] zones, int row, int col, int value) {
        ContextLL result = new ContextLL();
        result.value = value;
        result.active = (zones[row][col] == 1);
        result.context = new int[3];
        
        if (row == 0 || col == 0) {
            throw new IllegalArgumentException("No context for row=0 and col=0");
        }
        
        result.context[0] = data[row - 1][col - 1];
        result.context[1] = data[row - 1][col];
        result.context[2] = data[row][col - 1];
        
        return result;
    }
    
    public int getValue() {
        return value;
    }

    public boolean isActive() {
        return active;
    }

    public int[] getContext() {
        return context;
    }

    
    @Override
    public String toString() {
        return "ContextLL [active=" + active + ", context=" + Arrays.toString(context) + "]";
    }

    public static void main(String[] args) {
        int[][] data = {
                {1,2,3,4},
                {5,6,7,8},
                {1,2,3,4},
                {5,6,7,8},
        };
        
        int[][] zones = {
                {1,1,1,1},
                {1,1,1,1},
                {1,1,1,1},
                {1,1,1,1}
        };
        
        System.out.println(create(data, zones, 1, 1, 1));
    }
}
