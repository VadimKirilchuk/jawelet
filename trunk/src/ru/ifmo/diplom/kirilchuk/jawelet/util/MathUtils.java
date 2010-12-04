package ru.ifmo.diplom.kirilchuk.jawelet.util;

/**
 *
 * @author Kirilchuk V.E.
 */
public final class MathUtils {

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

    public static int getClosest2PowerLength(int value) {
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
}
