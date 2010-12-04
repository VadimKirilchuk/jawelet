package ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions;

import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Action;

/**
 *
 * @author Kirilchuk V.E.
 */
public class ZeroPaddingToEven implements Action {

    @Override
    public int getExtensionLength(int dataLength) {
        if((dataLength & 1) == 1 ) {
            return 1;
        }

        return 0;
    }

    @Override
    public int execute(double[] data, int currentLength) {
        if((currentLength & 1) == 1 ) {
            return currentLength + 1;
        }

        return currentLength;
    }

}
