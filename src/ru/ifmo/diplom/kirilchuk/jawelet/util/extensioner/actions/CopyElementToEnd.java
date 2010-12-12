package ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions;

import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Action;

/**
 *
 * @author Kirilchuk V.E.
 */
public class CopyElementToEnd implements Action {
    private final int index;

    public CopyElementToEnd(int index) {
        this.index = index;
    }

    @Override
    public int getExtensionLength(int dataLength) {
        return 1;
    }

    @Override
    public int execute(double[] data, int currentLength) {
        data[currentLength] = data[index];
        return currentLength + 1;
    }

}
