package ru.ifmo.diplom.kirilchuk.jawelet.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.DWTransform2D;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.legall.impl.LeGallWaveletTransform;
import ru.ifmo.diplom.kirilchuk.jawelet.gui.util.ImageUtils;
import ru.ifmo.diplom.kirilchuk.jawelet.gui.util.SwingUtils;
import ru.ifmo.diplom.kirilchuk.jawelet.util.ArrayUtils;

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
                    
                    File file = fileChooser.getSelectedFile();
                    if (file == null) {
                    	SwingUtils.showError(gui, "You didn`t select any file.", true);
                    	gui.dispose();
                    }
                    
                    BufferedImage image = ImageUtils.loadImage(file);
                    if (image == null) {
                        SwingUtils.showError(gui, "Image load failed.", true);
                        gui.dispose();
                    } else if (!ImageUtils.isGrayScale(image)) {
                        SwingUtils.showError(gui, "Image must be grayscale.", true);
                        gui.dispose();
                    } else {
                    	int[][] imageData = ImageUtils.getGrayscaleImageData(image);
                        gui.setImage(image);
                        
                        SwingUtils.showError(gui, "After you click OK would be magic.", false);
                        double[][] data = ArrayUtils.convert(imageData);
                        DWTransform2D transform = new DWTransform2D(new LeGallWaveletTransform());
                        transform.decomposeInplace(data);
                        imageData = ArrayUtils.convert(data);
                        ImageUtils.setNewGrayscaleImageData(image, imageData);
                        gui.setImage(image);
                    }
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }


}
