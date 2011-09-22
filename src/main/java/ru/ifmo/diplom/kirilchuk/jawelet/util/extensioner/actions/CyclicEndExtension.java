package ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions;

import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Action;

/**
 *
 * @author Kirilchuk V.E.
 */
public class CyclicEndExtension implements Action {

    private final int length;

    public CyclicEndExtension(int length) {
        this.length = length;
    }

    @Override
    public int getExtensionLength(int dataLength) {
        return length;
    }

    @Override
    public int execute(double[] data, int currentLength) {
        if (length <= currentLength) {
            System.arraycopy(data, 0, data, currentLength, length);
        } else {
            int wholeNum = length / currentLength;
            int pointer = currentLength;
            for (int i = 0; i < wholeNum; ++i) {//copy whole parts
                System.arraycopy(data, 0, data, pointer, currentLength);
                pointer += currentLength;
            }
            System.arraycopy(data, 0, data, pointer, length - (wholeNum * currentLength));
        }

        return currentLength + length;
    }
}
