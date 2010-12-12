package ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions;

import ru.ifmo.diplom.kirilchuk.jawelet.util.Assert;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Action;

/**
 *
 * @author Kirilchuk V.E.
 */
public class ZeroPaddingTo2Power implements Action {

    private final int power;
    private final int value;

    public ZeroPaddingTo2Power(int power) {
        Assert.argCondition(power >=1, "Power must be >= 1.");
        this.power = power;
        this.value = (1 << power);//2nd power value
    }

    @Override
    public int getExtensionLength(int dataLength) {
        if(dataLength - value > 0) {
            throw new IllegalArgumentException("Data length is more than specified 2nd power.");
        }
        return (value - dataLength);
    }

    @Override
    public int execute(double[] data, int currentLength) {
        return value;
    }
}
