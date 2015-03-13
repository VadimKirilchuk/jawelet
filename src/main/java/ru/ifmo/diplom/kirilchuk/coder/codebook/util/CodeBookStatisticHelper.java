package ru.ifmo.diplom.kirilchuk.coder.codebook.util;

/**
 * Helper class to count mean square error. 
 */
public class CodeBookStatisticHelper {

    public static double countAndPrintMeanSquareError(double[][] errors, int size) {
        double errorSumm = 0;
        for (int row = 0; row < errors.length; ++row) {
            for (int col = 0; col < errors[0].length; ++col) {
                double squareError = errors[row][col];
                errorSumm += squareError;
            }
        }
        double result = (errorSumm / size);

        return result;
    }
}
