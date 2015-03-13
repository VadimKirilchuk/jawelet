package ru.ifmo.diplom.kirilchuk.coder.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

/**
 * Main frame
 * 
 * @author Kirilchuk V.E.
 */
public class CoderMainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	private final int width  = 400;
	private final int heigth = 290;
	
	private JTextField sourceField;
	private JTextField targetField;
	
	private JSpinner dispersionSpinner = new JSpinner(new SpinnerNumberModel(4, 0, 1000, 1));
	private JSpinner arithCodeSpinner = new JSpinner(new SpinnerNumberModel(127, 1, 127, 1));
	private JSpinner transformLevelsSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
	
	/**
	 * Creates frame.
	 */
	public CoderMainFrame() {
		super("Jawelet");
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		setSize(width, heigth);
		setMinimumSize(getSize());

		initComponentsAndLayout();

		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void initComponentsAndLayout() {
	    setLayout(new BorderLayout());
        
	    JPanel form = new JPanel(new GridBagLayout());
        add(form, BorderLayout.NORTH);	    	    
	    
	    GridBagConstraints c = new GridBagConstraints();
	    c.anchor = GridBagConstraints.WEST;
	    c.insets = new Insets(0, 3, 3, 3);
	    
	    c.gridx = 0;
	    c.gridy = 0;
	    c.gridwidth = GridBagConstraints.REMAINDER;
	    c.weightx = 0;
	    form.add(new JLabel("Source Directory:"), c);
        
	    c.weightx = 1;
	    c.gridwidth = 1;
	    c.gridy = 1;
	    c.fill = GridBagConstraints.HORIZONTAL;
	    sourceField = new JTextField(20); 
	    form.add(sourceField, c);
	    c.fill = GridBagConstraints.NONE;
	    c.weightx = 0;
	    c.gridx = 1;
	    JButton browseSource = new JButton("Browse");
	    browseSource.addActionListener(new BrowseActionListener(sourceField));
	    form.add(browseSource, c);
	    
	    c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.weightx = 0;
        form.add(new JLabel("Target Directory:"), c);
        
        c.weightx = 1;
        c.gridwidth = 1;
        c.gridy = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        targetField = new JTextField(20); 
        form.add(targetField, c);
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0;
        c.gridx = 1;
        JButton browseTarget = new JButton("Browse");
        browseTarget.addActionListener(new BrowseActionListener(targetField));
        form.add(browseTarget, c);
        
        c.gridx = 0;
        c.gridy = 4;
        c.weightx = 1;
        c.gridwidth = 2;
        form.add(createConfigPanel(), c);
        
        c.gridx = 0;
        c.gridy = 5;
        c.weightx = 0;
        c.gridwidth = 0;
        c.insets = new Insets(10, 3, 5, 5);
        
        
        JButton encodeButton = new JButton("Encode");
        encodeButton.addActionListener(new PerformEncodingListener(this));
        form.add(encodeButton, c);
	}

    private Component createConfigPanel() {
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        JPanel configPanel = new JPanel(new GridBagLayout());
        configPanel.setBorder(border);
        
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5, 5, 5, 5);
        
        c.gridx = 0;
        c.gridy = 0;
        configPanel.add(new JLabel("DispersionThreshold:"), c);
        c.gridx = 1;
        c.ipadx = 5;
        c.ipady = 5;
        configPanel.add(dispersionSpinner, c);
        
        c.gridx = 0;
        c.gridy = 1;
        configPanel.add(new JLabel("ArithCodeThreshold:"), c);
        c.gridx = 1;
        c.ipadx = 5;
        c.ipady = 5;
        configPanel.add(arithCodeSpinner, c);
        
        c.gridx = 0;
        c.gridy = 2;
        configPanel.add(new JLabel("TransformLevels:"), c);
        c.gridx = 1;
        c.ipadx = 5;
        c.ipady = 5;
        configPanel.add(transformLevelsSpinner, c);
        
        return configPanel;
    }
    
    public String getSourcePath() {
        return sourceField.getText();
    }
    
    public String getTargetPath() {
        return targetField.getText();
    }
    
    public int getDispersionThreshold() {
        return (Integer) dispersionSpinner.getValue();
    }
    
    public int getArithCodeThreshold() {
        return (Integer) arithCodeSpinner.getValue();
    }
    
    public int getTransformLevels() {
        return (Integer) transformLevelsSpinner.getValue();
    }
}
