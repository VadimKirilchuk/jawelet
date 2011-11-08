package ru.ifmo.diplom.kirilchuk.jawelet.gui.util;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

/**
 * Class with utilities for swing related stuff.
 * 
 * @author Kirilchuk V.E.
 */
public final class SwingUtils {
	
	private SwingUtils() {}

	/**
	 * Shows error in error dialog.
	 * 
	 * @param parent determines the Frame in which the dialog is displayed; if null, or if the 
	 * parentComponent has no Frame, a default Frame is used
	 * @param message error message
	 */
	public static void showError(Component parent, String message) {
		JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Dialog for choosing file.
	 * 
	 * @param parent the parent component of the dialog, can be null
	 * @return selected file or null if no file is selected
	 */
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
						|| name.endsWith(".jp2")
						|| name.endsWith(".png")
						|| name.endsWith(".bmp");
				return accepted;
			}
		});

		fileChooser.showOpenDialog(parent);
		return fileChooser.getSelectedFile();
	}
}
