package ru.ifmo.diplom.kirilchuk.jawelet.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.DWTransform2D;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.legall.impl.LeGallWaveletTransform;
import ru.ifmo.diplom.kirilchuk.jawelet.gui.util.ImageUtils;
import ru.ifmo.diplom.kirilchuk.jawelet.gui.util.SwingUtils;

/**
 * 
 * @author Kirilchuk V.E.
 */
public class JaweletMainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private final int width  = 800;
	private final int heigth = 600;

	private final JLabel imageLabel = new JLabel();
	private final JButton decomposeButton = new JButton("Decompose");
	private final JButton reconstructButton = new JButton("Reconstruct");

	private BufferedImage image;

	public JaweletMainFrame() {
		super("Jawelet");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(width, heigth);

		initMenu();
		initAndLayout();

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initAndLayout() {
		setLayout(new BorderLayout());
		add(new JScrollPane(imageLabel), BorderLayout.CENTER);

		Box rightBox = Box.createVerticalBox();
		rightBox.add(Box.createVerticalGlue());

		TransformButtonListener transformButtonListener = new TransformButtonListener();
		decomposeButton.addActionListener(transformButtonListener);
		reconstructButton.addActionListener(transformButtonListener);

		rightBox.add(decomposeButton);
		rightBox.add(reconstructButton);

		rightBox.add(Box.createVerticalStrut(20));
		add(rightBox, BorderLayout.EAST);
	}

	public void setImage(BufferedImage image) {
		this.image = image;
		imageLabel.setIcon(new ImageIcon(image));
	}

	private void initMenu() {
		JMenuBar menuBar = new JMenuBar();

		JMenu fileMenu = new JMenu("File");

		JMenuItem loadItem = new JMenuItem("Load image");
		loadItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JaweletMainFrame gui = JaweletMainFrame.this;
				try {
					File file = SwingUtils.chooseImageFile(gui);
					if (file == null) {
						SwingUtils.showError(gui, "You didn`t select any file.");
						return;
					}

					BufferedImage image = ImageUtils.loadImage(file);
					if (image == null) {
						SwingUtils.showError(gui, "Image load failed.");
						return;
					}

					/* autoconvert to grayscale */
					image = ImageUtils.tryCreateGrayscaleCopy(image);
					gui.setImage(image);
				} catch (IOException ex) {
					SwingUtils.showError(gui, ex.getMessage());
				}
			}
		});
		fileMenu.add(loadItem);
		fileMenu.add(new JSeparator());
		
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JaweletMainFrame.this.dispose();
			}
		});
		fileMenu.add(exitItem);

		menuBar.add(fileMenu);
		setJMenuBar(menuBar);
	}
	

	private class TransformButtonListener implements ActionListener {
		private final DWTransform2D transform = new DWTransform2D(new LeGallWaveletTransform());

		@Override
		public void actionPerformed(ActionEvent e) {
			double[][] imageData = ImageUtils.getGrayscaleImageData(image);
			if (e.getSource() == decomposeButton) {
				transform.decomposeInplace(imageData);
				normalizeForOutput(imageData);
			} else {
				transform.reconstructInplace(imageData);
			}
			ImageUtils.setNewGrayscaleImageData(image, imageData);
			imageLabel.repaint();
		}

		private void normalizeForOutput(double[][] imageData) {
			for (int row = 0; row < imageData.length; ++row) {
				for (int col = 0; col < imageData[0].length; ++col) {
					double val = imageData[row][col];
					if (val > 255) {
						imageData[row][col] = 255;
					} else if (val < 0) {
						imageData[row][col] = 0;
					}
				}
			}

		}
	}
}
