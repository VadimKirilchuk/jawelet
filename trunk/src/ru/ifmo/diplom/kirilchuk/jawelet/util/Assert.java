package ru.ifmo.diplom.kirilchuk.jawelet.util;

/**
 *
 * @author Kirilchuk V.E.
 */
public class Assert {

    public static void valueIs2Power(int value) throws IllegalArgumentException {
        valueIs2Power(value, "Data length");
    }

    public static void valueIs2Power(int value, String valueName) throws IllegalArgumentException {
        if ((value & (value - 1)) != 0) {
            throw new IllegalArgumentException(valueName + " must be a power of two.");
        }
    }

    public static void argNotNull(Object... objects) throws IllegalArgumentException {
        for (Object object : objects) {
            if (object == null) {
                throw new IllegalArgumentException("Arguments can`t be null.");
            }
        }
    }

    public static void checkNotNull(Object object, String detailMessage) throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(detailMessage);
        }
    }

    public static void argCondition(boolean condition, String errorMessage) {
        if(!condition) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
