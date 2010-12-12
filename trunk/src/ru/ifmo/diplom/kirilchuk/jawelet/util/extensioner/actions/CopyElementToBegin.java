package ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions;

import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Action;

/**
 *
 * @author Kirilchuk V.E.
 */
public class CopyElementToBegin implements Action {
    private final int index;

    public CopyElementToBegin(int index) {
        this.index = index;
    }

    @Override
    public int getExtensionLength(int dataLength) {
        return 1;
    }

    @Override
    public int execute(double[] data, int currentLength) {
        System.arraycopy(data, 0, data, 1, currentLength);
        data[0] = data[1 + index];
        return currentLength + 1;
    }

}
