package ru.ifmo.diplom.kirilchuk.jawelet.util;

/**
 *
 * @author Kirilchuk V.E.
 */
public final class MathUtils {

    public static final double SQRT_2  = Math.sqrt(2);
    public static final double SQRT_32 = Math.sqrt(32);

    private MathUtils() {}
    
    /**
     * Returns n for 2^n value.
     * 
     * @param value the power of two integer.
     * @return n for 2^n value.
     */
    public static int getExact2Power(int powerOfTwoValue) {
        Assert.argCondition(powerOfTwoValue > 0, "Argument must be positive.");
        Assert.valueIs2Power(powerOfTwoValue, "Value");

        int n = -1;
        while(powerOfTwoValue > 0) {
            powerOfTwoValue >>>= 1;
            n++;
        }

        return n;
    }

    public static int getClosest2PowerValue(int value) {
        Assert.argCondition(value > 1, "Argument must be positive.");

        if (is2power(value)) {
            return value;
        } else {
            value |= value >> 1;
            value |= value >> 2;
            value |= value >> 4;
            value |= value >> 8;
            value |= value >> 16;
            return value + 1;
        }
    }

    public static boolean is2power(int a) {
        return ((a & (a - 1)) == 0);
    }

    /**
     * Convoles vectors first and second. Algebraically, convolution is the same operation
     * as multiplying polinomials whose coefficients are the elemnts of first and second.
     * <pre>
     * For vectors with length n convolution is:
     * result[0] = first[0] * second[0]
     * result[1] = first[0] * second[1] + first[1] * second[0]
     * ...
     * result[i] = summ by j { first[j] * second[i-j]}
     *              where j in [max(0, i + 1 - secondLength)..(i, firstLength - 1)]
     * ...
     * result[n-1] = first[n-1] * second[n-1]
     * </pre>
     *
     * @param first coefficients of first polinom
     * @param second coefficients of second polinom
     * @return convolution of first and second vectors.
     */
    public static double[] convolve(double[] first, double[] second) {
        int firstLength = first.length;
        int secondLength = second.length;

        double[] result = new double[firstLength + secondLength - 1];

        for (int resultIndex = 0; resultIndex < result.length; ++resultIndex) {
            for (int summIndex = Math.max(0, resultIndex + 1 - secondLength);
                     summIndex <= Math.min(resultIndex, firstLength - 1); ++summIndex) {
                result[resultIndex] += (first[summIndex] * second[resultIndex - summIndex]);
            }
        }

        return result;
    }
}
