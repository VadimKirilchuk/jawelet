package ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions;

import ru.ifmo.diplom.kirilchuk.jawelet.util.Assert;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.Action;

/**
 *
 * @author Kirilchuk V.E.
 */
public class MirrorExtension implements Action {
    private final int size;
    private final int extension;

    public MirrorExtension(int size) {
        Assert.argCondition(size >= 1, "Mirroring size must be >= 1");
        this.size = size;
        this.extension = size * 2;
    }

    @Override
    public int getExtensionLength(int dataLength) {
        return extension;
    }

    @Override
    public int execute(double[] data, int currentLength) {
        Assert.argCondition(currentLength - size > 0, "Not enough elements in data for mirroring.");
        System.arraycopy(data, 0, data, size, currentLength);
        for (int i = 0; i < size; ++i) {
            data[size - 1 - i] = data[size + 1 + i];
            data[currentLength + size + i] = data[currentLength + size - 2 - i];
        }
        return currentLength + extension;
    }
}
