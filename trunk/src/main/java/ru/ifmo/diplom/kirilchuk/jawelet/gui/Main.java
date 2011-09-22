package ru.ifmo.diplom.kirilchuk.jawelet.gui;

import javax.swing.SwingUtilities;

/**
 * 
 * @author Kirilchuk V.E.
 */
public class Main {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new JaweletMainFrame();
			}
		});
	}
}
