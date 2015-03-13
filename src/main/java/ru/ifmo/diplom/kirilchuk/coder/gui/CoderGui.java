package ru.ifmo.diplom.kirilchuk.coder.gui;

import javax.swing.SwingUtilities;

/**
 * Entry-Point for coder GUI
 * 
 * @author Kirilchuk V.E.
 */
public class CoderGui {

	/**
	 * main
	 * @param args not supported
	 */
	public static void main(String[] args) {
	    SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new CoderMainFrame();
			}
		});
	}
}
