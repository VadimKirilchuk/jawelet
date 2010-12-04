package ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions;

import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Action;

/**
 *
 * @author Kirilchuk V.E.
 */
public class CyclicBeginExtension implements Action {

    private final int length;

    public CyclicBeginExtension(int length) {
        this.length = length;
    }

    @Override
    public int getExtensionLength(int dataLength) {
        return length;
    }

    @Override
    public int execute(double[] data, int currentLength) {
        //shifting array to the right
        System.arraycopy(data, 0, data, length, currentLength);

        //filleng beginning
        if (length <= currentLength) {
            System.arraycopy(data, currentLength, data, 0, length);
        } else {
            int wholeNum = length / currentLength;
            //copy not whole part first
            int partLen = length - wholeNum * currentLength;
            System.arraycopy(data, length + currentLength - partLen, data, 0, partLen);
            //copy whole parts
            int pointer = partLen;
            for (int i = 0; i < wholeNum; ++i) {
                System.arraycopy(data, length, data, pointer, currentLength);
                pointer += currentLength;
            }
        }

        return currentLength + length;
    }
}
