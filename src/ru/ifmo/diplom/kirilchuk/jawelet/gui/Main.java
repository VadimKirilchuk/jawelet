package ru.ifmo.diplom.kirilchuk.jawelet.gui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
                    File file = chooseImageFile(gui);
                    if (file == null) {
                    	SwingUtils.showError(gui, "You didn`t select any file.", true);
                    	gui.dispose();
                    	return;
                    }
                    
                    BufferedImage image = ImageUtils.loadImage(file);
                    if (image == null) {
                        SwingUtils.showError(gui, "Image load failed.", true);
                        gui.dispose();
                        return;
					}
                    
					/* autoconvert to grayscale */
					image = ImageUtils.tryCreateGrayscaleCopy(image);
					gui.setImage(image);

                } catch (IOException ex) {
                	SwingUtils.showError(gui, ex.getMessage(), true);
                }
            }

			private File chooseImageFile(JaweletMainFrame gui) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.showOpenDialog(gui);
				
				File file = fileChooser.getSelectedFile();
				return file;
			}
        });
    }


}
