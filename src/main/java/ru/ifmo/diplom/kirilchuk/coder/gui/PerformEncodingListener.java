package ru.ifmo.diplom.kirilchuk.coder.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;

import ru.ifmo.diplom.kirilchuk.coder.JaweletCoderConfig;
import ru.ifmo.diplom.kirilchuk.coder.JaweletCoderFacade;
import ru.ifmo.diplom.kirilchuk.util.ImageUtils;
import ru.ifmo.diplom.kirilchuk.util.SwingUtils;

public class PerformEncodingListener implements ActionListener {

    static class PerformFatalException extends Exception {
        private static final long serialVersionUID = 953827723756267103L;

        public PerformFatalException(String message) {
            super(message);
        }
    }
    
    private CoderMainFrame mainFrame;
    private List<String> messages = new ArrayList<String>();
    
    public PerformEncodingListener(CoderMainFrame coderMainFrame) {
        this.mainFrame = coderMainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
          File sourceDirectory = new File(mainFrame.getSourcePath());
          if (!sourceDirectory.exists() || !sourceDirectory.isDirectory()) {
              throw new PerformFatalException("Source path must be existing directory containing grayscale images");
          }
          File targetDirectory = new File(mainFrame.getTargetPath());
          
          File[] files = sourceDirectory.listFiles();
          messages.clear();          
          processFiles(files, targetDirectory);
          showResult();
        } catch (PerformFatalException ex) {
            SwingUtils.showError(mainFrame, ex.getMessage());
        } 
    }

    private void showResult() {
        messages.add("Transformation done");
        StringBuilder builder = new StringBuilder();
        for (String message: messages) {
            builder.append(message);
            builder.append('\n');
        }
        SwingUtils.showInfo(mainFrame, builder.toString());
    }

    private void processFiles(File[] files, File targetDirectory) {
        for (File file : files) {
            if (file.isDirectory()) {
                messages.add("Ignoring [" + file.getAbsolutePath() + "] is directory, not a file");
                continue;
            }
            try {
               BufferedImage image = ImageUtils.loadImage(file);
               if (image == null) {
                   messages.add("Ignoring [" + file.getAbsolutePath() + "] failed to load (maybe format is unsupported).");
                   continue;
               }
               if (!ImageUtils.isGrayScale(image)) {
                   messages.add("Ignoring [" + file.getAbsolutePath() + "] not grayscale!");
                   continue;
               }
               // all is OK
               processImage(file, image, targetDirectory);
            } catch (IOException ex) {
                ex.printStackTrace();
                messages.add("Ignoring [" + file.getAbsolutePath() + "] reason: " + ex.getMessage());
            }
        }       
    }

    private void processImage(File sourceFile, BufferedImage image, File targetDirectory) {
        JaweletCoderConfig config = new JaweletCoderConfig()
                            .setArithmeticCodeThreshold(mainFrame.getArithCodeThreshold())
                            .setDispThresholdLL(mainFrame.getDispersionThreshold())
                            .setTransformLevel(mainFrame.getTransformLevels());
        
        File targetDir = new File(targetDirectory, trimExtension(sourceFile.getName()));
        JaweletCoderFacade facade = new JaweletCoderFacade(targetDir, config);
                
        try {
            facade.encodeImage(image);
            
            File[] statistics = targetDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if (pathname.isDirectory()) {
                        return false;
                    }
                    return true;
                }
            });

            Map<String, Long> stats = new LinkedHashMap<String, Long>();
            long summ = 0;
            for (File f : statistics) {
                summ += f.length();
            }

            stats.put("General result is", summ);
            
            //You can remove this if you need only general result
            for (File f : statistics) {
                stats.put(f.getName(), f.length());
            }
            
            messages.add("Statistic for: " + sourceFile.getName() + "\r\n");
            messages.add(processStats(stats));
        } catch (Throwable e) {
            e.printStackTrace();
            messages.add("Encoding failed for [" + sourceFile.getAbsolutePath() + "] reason:" + e.getMessage());
        }
    }

    private String trimExtension(String name) {
        return FilenameUtils.removeExtension(name);
    }
    
    private String processStats(Map<String, Long> stats) {
        StringBuilder builder = new StringBuilder(); 
        for(Entry<String, Long> entry: stats.entrySet()) {
            builder.append("    ");
            builder.append(entry.getKey());
            builder.append(" bytes: ");
            builder.append(entry.getValue());
            builder.append("\r\n");
        }
        return builder.toString();
    }

}
