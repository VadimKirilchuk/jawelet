package ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner;

import java.util.ArrayList;
import java.util.List;

/**
 * Instance of this class corresponding for extending 
 * data sequence on filter length.
 *
 * @author Kirilchuk V.E.
 */
public class Extensioner {

    private final List<Action> actions = new ArrayList<Action>();
    private final double[] dataToExtend;

    public Extensioner(double[] dataToExtend) {
        this.dataToExtend = dataToExtend;
    }

    public Extensioner schedule(Action action) {
        actions.add(action);

        return this;
    }

    public double[] execute() {
        int newLength = dataToExtend.length;
        for (Action action : actions) {
            newLength += action.getExtensionLength(newLength);
        }

        double[] result = new double[newLength];
        System.arraycopy(dataToExtend, 0, result, 0, dataToExtend.length);

        newLength = dataToExtend.length;
        for (Action action : actions) {
            newLength = action.execute(result, newLength);
        }

        return result;
    }
}
