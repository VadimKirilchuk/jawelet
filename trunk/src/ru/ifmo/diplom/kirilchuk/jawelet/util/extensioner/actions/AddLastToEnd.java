package ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions;

import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Action;

/**
 *
 * @author Kirilchuk V.E.
 */
public class AddLastToEnd implements Action {

    @Override
    public int getExtensionLength(int dataLength) {
        return 1;
    }

    @Override
    public int execute(double[] data, int currentLength) {
        data[currentLength] = data[currentLength - 1];
        return currentLength + 1;
    }
}
