package ru.ifmo.diplom.kirilchuk.util;

import java.util.Arrays;

public final class ArrayUtils {
    private ArrayUtils() {}

    /**
     * Returns submatrix of specified array.
     * 
     * @param original
     *            array of doubles to get part of
     * @param startx
     *            start x
     * @param endx
     *            end x
     * @param starty
     *            start y
     * @param endy
     *            end y
     * @return submatrix of original array
     */
    public static double[][] submatrix(double[][] original, int startx, int endx,
                                                            int starty, int endy) {
        int sizeRow = endx - startx;
        int sizeCol = endy - starty;

        double[][] result = new double[sizeRow][sizeCol];
        for (int row = startx; row < endx; ++row) {
            for (int col = starty; col < endy; ++col) {
                result[row - startx][col - starty] = original[row][col];
            }
        }

        return result;
    }

    /**
     * Fills specified submatrix of matrix with provided value.
     * 
     * @param matrixToFill
     * @param startx
     * @param endx
     * @param starty
     * @param endy
     * @param value
     */
    public static void fillMatrix(int[][] matrixToFill, int startx, int endx, int starty, int endy, int value) {
        for (int row = startx; row < endx; ++row) {
            for (int col = starty; col < endy; ++col) {
                matrixToFill[row][col] = value;
            }
        }
    }

    /**
     * Prints 2D int array
     * 
     * @param array
     *            to print
     */
    public static void print(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(Arrays.toString(array[i]));
        }
    }

    /**
     * Prints 1D int array to system out
     * 
     * @param array
     *            to print
     */
    public static void print(int[] array) {
        System.out.println(Arrays.toString(array));
    }

    /**
     * Prints 2D double array to system out
     * 
     * @param array
     *            to print
     */
    public static void print(double[][] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(Arrays.toString(array[i]));
        }
    }

    /**
     * Prints 1D double array to system out
     * 
     * @param array
     *            to print
     */
    public static void print(double[] array) {
        System.out.println(Arrays.toString(array));
    }

    /**
     * Converts array of ints to array of doubles
     * 
     * @param array
     *            to convert to double array
     * @return array of doubles
     */
    public static double[][] convert(int[][] array) {
        double[][] result = new double[array.length][];
        for (int row = 0; row < array.length; ++row) {
            result[row] = new double[array[row].length];
            for (int column = 0; column < array[row].length; ++column) {
                result[row][column] = array[row][column];
            }
        }
        return result;
    }

    /**
     * Be careful! This method just taking integer part from doubles. It doesn`t
     * do any rounding or other things.
     * 
     * @param array
     *            array to convert
     * @return array of integers converted from double array
     */
    public static int[][] convert(double[][] array) {
        int[][] result = new int[array.length][];
        for (int row = 0; row < array.length; ++row) {
            result[row] = new int[array[row].length];
            for (int column = 0; column < array[row].length; ++column) {
                result[row][column] = (int) array[row][column];
            }
        }
        return result;
    }

    public static int[] convert(double[] array) {
        int[] result = new int[array.length];
        for(int i = 0; i < array.length; ++i) {
            result[i] = (int) array[i];
        }
        
        return result;
    }
    
    public static double[] convert(int[] array) {
        double[] result = new double[array.length];
        for(int i = 0; i < array.length; ++i) {
            result[i] = (double) array[i];
        }
        
        return result;
    }

    /**
     * Clones 2D array of doubles
     * 
     * @param original
     *            array
     * @return cloned array
     */
    public static double[][] clone2DArray(double[][] original) {
        double[][] clone = new double[original.length][];
        for (int i = 0; i < original.length; i++) {
            clone[i] = original[i].clone();
        }

        return clone;
    }

    /**
     * Clones 2D array of ints
     * 
     * @param original
     *            array
     * @return cloned array
     */
    public static int[][] clone2DArray(int[][] original) {
        int[][] clone = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            clone[i] = original[i].clone();
        }

        return clone;
    }
    
    public static int findRange(int[][] data, int reserved) {
        int maximal = Integer.MIN_VALUE;
        int minimal = Integer.MAX_VALUE;
        for (int row = 0; row < data.length; row++) {
            for (int col = 0; col < data[0].length; col++) {
                int value = data[row][col];
                if (value > maximal) {
                    maximal = value;
                }
                if (value < minimal) {
                    minimal = value;
                }
            }
        }
        //watch at min and max
        if (minimal > 0) {
            minimal = 0;
        } 
        
        if (maximal < 0) {
            maximal = 0;
        }
        
        if (maximal >= reserved) {
            maximal -= reserved;
        }
        if (minimal <= -reserved) {
            minimal += reserved;
        }
        
        return (maximal - minimal);
    }
    
    public static void main(String[] args) {
        int[][] data = {
                {-13, 14} 
        };
        
        System.out.println(findRange(data, 10));
    }
}
