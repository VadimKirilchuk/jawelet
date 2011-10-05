package ru.ifmo.diplom.kirilchuk.jawelet.gui;

import javax.swing.SwingUtilities;

/**
 * Entry-Point for wavelet GUI
 * 
 * @author Kirilchuk V.E.
 */
public class Main {

	/**
	 * main
	 * @param args not supported
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new JaweletMainFrame();
			}
		});
	}
}
