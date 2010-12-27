package ru.ifmo.diplom.kirilchuk.jawelet.gui;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 *
 * @author Kirilchuk V.E.
 */
public class JaweletMainFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    private final int width  = 640;
    private final int heigth = 480;

    private final JLabel imageLabel = new JLabel();

    public JaweletMainFrame() {
        super("Jawelet");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout());

        setSize(width, heigth);

        add(new JScrollPane(imageLabel));

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setImage(Image image) {
        imageLabel.setIcon(new ImageIcon(image));
    }
}
