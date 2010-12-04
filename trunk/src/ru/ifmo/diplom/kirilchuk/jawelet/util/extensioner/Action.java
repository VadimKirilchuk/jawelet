package ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner;

/**
 *
 * @author Kirilchuk V.E.
 */
public interface Action {
    int getExtensionLength(int dataLength);

    int execute(double[] data, int currentLength);
}
