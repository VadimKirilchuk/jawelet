package ru.ifmo.diplom.kirilchuk.jawelet.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import ru.ifmo.diplom.kirilchuk.jawelet.gui.util.ImageUtils;
import ru.ifmo.diplom.kirilchuk.jawelet.gui.util.SwingUtils;

/**
 *
 * @author Kirilchuk V.E.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JaweletMainFrame gui = new JaweletMainFrame();
                try {
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.showOpenDialog(gui);
                    BufferedImage image = ImageUtils.loadImage(fileChooser.getSelectedFile());
                    if (image == null) {
                        SwingUtils.showError(gui, "Image load failed.", true);
                        gui.dispose();
                    } else if (!ImageUtils.isGrayScale(image)) {
                        SwingUtils.showError(gui, "Image must be grayscale.", true);
                        gui.dispose();
                    } else {
                        print2DArray(ImageUtils.getGrayscaleImageData(image));
                        gui.setImage(image);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private static void print2DArray(int[][] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(Arrays.toString(array[i]));
        }
    }
}
