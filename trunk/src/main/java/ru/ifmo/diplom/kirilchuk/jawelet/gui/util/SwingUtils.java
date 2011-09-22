package ru.ifmo.diplom.kirilchuk.jawelet.gui.util;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author Kirilchuk V.E.
 */
public final class SwingUtils {
	private SwingUtils() {
	}

	public static void showError(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static File chooseImageFile(Component parent) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "Supported image files(JPEG, PNG, BMP)";
			}

			@Override
			public boolean accept(File f) {
				String name = f.getName();
				boolean accepted = f.isDirectory()  
						|| name.endsWith(".jpg") 
						|| name.endsWith(".jpeg") 
						|| name.endsWith(".png")
						|| name.endsWith(".bmp");
				return accepted;
			}
		});

		fileChooser.showOpenDialog(parent);
		return fileChooser.getSelectedFile();
	}
}
