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
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;

import ru.ifmo.diplom.kirilchuk.util.ImageUtils;
import ru.ifmo.diplom.kirilchuk.util.SwingUtils;

/**
 * Main frame
 * 
 * @author Kirilchuk V.E.
 */
public class JaweletMainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private final int width  = 800;
	private final int heigth = 600;
	
	private final ImageWaveletTransformer transformer = new ImageWaveletTransformer();

	private final JLabel   imageLabel        = new JLabel();
	private final JSpinner spinner           = new JSpinner();
	private final JButton  decomposeButton   = new JButton("Decompose");
	private final JButton  reconstructButton = new JButton("Reconstruct");

	private BufferedImage image;

	/**
	 * Creates frame.
	 */
	public JaweletMainFrame() {
		super("Jawelet");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(width, heigth);

		initMenu();
		initComponentsAndLayout();

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initComponentsAndLayout() {
		setLayout(new BorderLayout());
		add(new JScrollPane(imageLabel), BorderLayout.CENTER);

		Box rightBox = Box.createVerticalBox();
		rightBox.add(Box.createVerticalGlue());

		TransformButtonListener transformButtonListener = new TransformButtonListener();
		decomposeButton.addActionListener(transformButtonListener);
		reconstructButton.addActionListener(transformButtonListener);

		rightBox.add(spinner);
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

		/* menu 'Options' */
		JMenu optionsMenu = new JMenu("Options");
		final JCheckBoxMenuItem autoGray = new JCheckBoxMenuItem("Convert to gray", true);
		optionsMenu.add(autoGray);

		/* menu 'File' */
		JMenu fileMenu = new JMenu("File");

		JMenuItem loadItem = new JMenuItem("Load image");
		loadItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JaweletMainFrame gui = JaweletMainFrame.this;
				try {
					File file = SwingUtils.chooseImageFile(gui);
					if (file == null) {						
						return;
					}

					BufferedImage image = ImageUtils.loadImage(file);
					if (image == null) {
						SwingUtils.showError(gui, "Image load failed.");
						return;
					}

					/* autoconvert to grayscale */
					if (autoGray.isSelected()) {
						image = ImageUtils.tryCreateGrayscaleCopy(image);
					}
					gui.setImage(image);
				} catch (IOException ex) {
					SwingUtils.showError(gui, ex.getMessage());
				} catch (Exception e2) {
					SwingUtils.showError(gui, e2.getMessage());
				}
			}
		});
		fileMenu.add(loadItem);
		
		JMenuItem saveItem = new JMenuItem("Save image as bmp");
		saveItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JaweletMainFrame gui = JaweletMainFrame.this;
				try {
					File file = SwingUtils.saveImageFile(gui);
					if (file == null) {						
						return;
					}
					ImageUtils.saveAsBitmap(image, file);
				} catch (IOException ex) {
					SwingUtils.showError(gui, ex.getMessage());
				} catch (Exception e2) {
					SwingUtils.showError(gui, e2.getMessage());
				}
			}
		});
		fileMenu.add(saveItem);
		fileMenu.add(new JSeparator());

		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JaweletMainFrame.this.dispose();
			}
		});
		fileMenu.add(exitItem);

		/* Add menus to menuBar and set menuBar to JFrame */
		menuBar.add(fileMenu);
		menuBar.add(optionsMenu);
		setJMenuBar(menuBar);
	}

	private class TransformButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {			
			double[][] result;
			int level;
			try {
				if (e.getSource() == decomposeButton) {
					level = (Integer) spinner.getValue();
					result = transformer.decomposeTransform(image, level)
							.getData();
					result = ImageUtils.trimToUnsignedByteRange(result);
				} else {
					result = transformer.reconstructTransform().getData();
				}
				ImageUtils.setNewGrayscaleImageData(image, result);
				imageLabel.repaint();
			} catch (IllegalArgumentException ex) {
				SwingUtils.showError(JaweletMainFrame.this, ex.getMessage());
			}
		}
	}
}
