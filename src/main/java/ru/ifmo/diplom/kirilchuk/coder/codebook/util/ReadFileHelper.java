package ru.ifmo.diplom.kirilchuk.coder.codebook.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coder.codebook.CodeBookGenerator;

public final class ReadFileHelper {

    private ReadFileHelper() {}

    public static void readData(File fileWithData, CodeBookGenerator generator) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileWithData));
        String line = "";
        while ((line = reader.readLine()) != null) {
            String[] nums = line.split(",");
            int[] data = {
                    Integer.valueOf(nums[0]),
                    Integer.valueOf(nums[1]),
                    Integer.valueOf(nums[2]),
                    Integer.valueOf(nums[3]),
                    Integer.valueOf(nums[4]),
                    // position
                    Integer.valueOf(nums[5]),
                    Integer.valueOf(nums[6])
            };
            generator.addData(data);
        }
        reader.close();
    }
    
    public static void readFilters(File fileWithFilters, CodeBookGenerator generator) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileWithFilters));
        String line = "";
        while ((line = reader.readLine()) != null) {
            String[] nums = line.split(",");
            double[] filter = {
                    Double.valueOf(nums[0]),
                    Double.valueOf(nums[1]),
                    Double.valueOf(nums[2]),
                    Double.valueOf(nums[3]),
            };
            generator.addFilter(filter);
        }
        reader.close();
    }
}
