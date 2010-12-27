package ru.ifmo.diplom.kirilchuk.jawelet.gui.util;

import java.awt.Component;
import javax.swing.JOptionPane;

/**
 *
 * @author Kirilchuk V.E.
 */
public final class SwingUtils {
    private SwingUtils() {}

    public  static void showError(Component parent, String message, boolean termination) {
        String msg = termination ? message : message.concat("\nProgram will terminate.");
        JOptionPane.showMessageDialog(parent, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
