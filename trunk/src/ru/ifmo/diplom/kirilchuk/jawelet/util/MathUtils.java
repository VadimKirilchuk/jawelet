package ru.ifmo.diplom.kirilchuk.jawelet.util;

/**
 *
 * @author Kirilchuk V.E.
 */
public final class MathUtils {

    private MathUtils(){}
    
    /**
     * Returns n for 2^n value.
     * 
     * @param value the power of two integer.
     * @return n for 2^n value.
     */
    public static int getExact2Power(int powerOfTwoValue) {
        Assert.valueIs2Power(powerOfTwoValue, "Value");

        int n = -1;
        while(powerOfTwoValue > 0) {
            powerOfTwoValue >>>= 1;
            n++;
        }

        return n;
    }
}
