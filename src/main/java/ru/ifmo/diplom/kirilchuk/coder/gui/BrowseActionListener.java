package ru.ifmo.diplom.kirilchuk.coder.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JTextField;

import ru.ifmo.diplom.kirilchuk.util.SwingUtils;

public class BrowseActionListener implements ActionListener {

    private JTextField textField;
    
    public BrowseActionListener(JTextField targetField) {
        this.textField = targetField; 
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String path = textField.getText();
        if (path == null || path.isEmpty()) {
           path = "."; 
        }
        JFileChooser chooser = new JFileChooser(new File(path));
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.showOpenDialog(null);
        File directory = chooser.getSelectedFile();  
        if (directory == null) {
            return;
        }
        try {
            textField.setText(directory.getCanonicalPath());
        } catch (IOException e1) {
            SwingUtils.showError(null, e1.getMessage());
        }
    }

}
