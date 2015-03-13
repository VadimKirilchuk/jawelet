package ru.ifmo.diplom.kirilchuk.trash.statistic.table;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.ifmo.diplom.kirilchuk.coder.predict.LLPredictioner;
import ru.ifmo.diplom.kirilchuk.coder.zones.ZonesRescaler;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.DWTransform2D;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.legall.impl.LeGallLiftingWaveletTransform;
import ru.ifmo.diplom.kirilchuk.trash.statistic.TransformResultHolder;
import ru.ifmo.diplom.kirilchuk.trash.statistic.table.util.AprioriHelper3;
import ru.ifmo.diplom.kirilchuk.util.AnalyzeUtils;
import ru.ifmo.diplom.kirilchuk.util.ArrayUtils;
import ru.ifmo.diplom.kirilchuk.util.ImageUtils;
import ru.ifmo.diplom.kirilchuk.util.MathUtils;

/**
 * This is cumbersome class to gerate table of comparison of different coding methods.
 * @Deprecated
 */
public class TableGenerator {
	private final DWTransform2D transform = new DWTransform2D(new LeGallLiftingWaveletTransform());
    private Logger log = LoggerFactory.getLogger(getClass());
//    private AprioriHelper aprioriHelper = new AprioriHelper();
//    private AprioriHelper2 aprioriHelper2 = new AprioriHelper2();
    private AprioriHelper3 aprioriHelper3 = new AprioriHelper3();
//    private SimpleHelper simpleHelper = new SimpleHelper();

    public static void main(String[] args) throws Exception {
        String imageDir = "D:/Temp/MAGISTR/TABLE/IMAGES";
        String outputDir = "D:/Temp/MAGISTR/TABLE/RESULTS";

        double[][] filterSet = new double[][] { { 1, 0, 0, 0 } };
        
        File castle = new File(imageDir, "Castle.jpg");
        double[][] imageData = getGrayscaleData(castle);
        
        TransformResultHolder holder = TransformResultHolder.createHolder(imageData);
        //holder = holder.transformNextLevel();//.transformNextLevel();
        
        double entropy;
        
        int[][] LL = ArrayUtils.convert(holder.getLL());
        entropy = AnalyzeUtils.calculateEntropy(LL, 0, 0, LL.length, LL.length);
        System.out.println("LL before: " + entropy);
        LLPredictioner predictionerLL = new LLPredictioner(LL, filterSet);
        predictionerLL.setDispersionThreshold(4);
        int[][] differenceLL = predictionerLL.buildDifference();
        entropy = AnalyzeUtils.calculateEntropy(differenceLL, 0, 0, differenceLL.length, differenceLL.length);
        System.out.println("LL after: " + entropy);
        
        ZonesRescaler rescaler = new ZonesRescaler();
//        int[][] resc = rescaler.rescaleZones(predictionerLL.getZones(), 1);
//        CommonUtils.prettyPrint(resc,"D:/Temp/MAGISTR/TABLE/RESULTS/resc1");
        /*
        int[][] LH = ArrayUtils.convert(holder.getLH());
        entropy = AnalyzeUtils.calculateEntropy(LH, 0, 0, LH.length, LH.length);
        System.out.println("LH before: " + entropy);
        CommonUtils.prettyPrint(LH, "D:/Temp/MAGISTR/TABLE/RESULTS/LH");
        OtherPredictioner predictionerLH = new OtherPredictioner(LH, filterSet, LL, predictionerLL.getZones());
        int[][] differenceLH = predictionerLH.buildDifference();
        entropy = AnalyzeUtils.calculateEntropy(differenceLH, 0, 0, LH.length, LH.length);
        
        int[][] HL = ArrayUtils.convert(holder.getHL());
        entropy = AnalyzeUtils.calculateEntropy(HL, 0, 0, HL.length, HL.length);
        System.out.println("HL before: " + entropy);
        OtherPredictioner predictionerHL = new OtherPredictioner(HL, filterSet, LL, predictionerLL.getZones());
        int[][] differenceHL = predictionerHL.buildDifference();
        entropy = AnalyzeUtils.calculateEntropy(differenceHL, 0, 0, HL.length, HL.length);
        
        int[][] HH = ArrayUtils.convert(holder.getHH());
        entropy = AnalyzeUtils.calculateEntropy(HH, 0, 0, HH.length, HH.length);
        System.out.println("HH before: " + entropy);
        OtherPredictioner predictionerHH = new OtherPredictioner(HH, filterSet, LL, predictionerLL.getZones());
        int[][] differenceHH = predictionerHH.buildDifference();
        entropy = AnalyzeUtils.calculateEntropy(differenceHH, 0, 0, HH.length, HH.length);
        */
//        double[][] normalized = ImageUtils.grayscaleNormalize3(difference);
//        
//        BufferedImage normalizedImage = ImageUtils.createNewGrayscaleImage(normalized);
//        ImageUtils.setNewGrayscaleImageData(normalizedImage, normalized);
//        ImageUtils.saveAsBitmap(normalizedImage, "D:/Temp/MAGISTR/TABLE/RESULTS/normalized.bmp");
//        CommonUtils.prettyPrint(normalized, "D:/Temp/MAGISTR/TABLE/RESULTS/normalized");
//        CommonUtils.prettyPrint(predictionerLH.edges, "D:/Temp/MAGISTR/TABLE/RESULTS/edges");
        
//        Restorator restorator = new Restorator(difference, predictioner.zones, predictioner.passiveFilter, predictioner.activeFilter);
//        int[][] restored = restorator.buildOriginal();
//        
//        for (int row = 0; row < original.length; row++) {
//			for (int col = 0; col < original.length; col++) {
//				if (original[row][col] != restored[row][col]) {
//					System.out.println(row + " " + col);
//				}
//			}
//		}
//        difference = predictioner.zones;
//        CommonUtils.prettyPrint(restored, "D:/Temp/MAGISTR/TABLE/RESULTS/res2.txt");
//        new TableGenerator().analyze(imageDir, outputDir);
    }

    private void analyze(String imageDirPath, String outputDirPath) throws Exception {
        File imageDir = new File(imageDirPath);
        File outputDir = new File(outputDirPath);

        outputDir.delete();
        outputDir.mkdirs();

        File[] images = imageDir.listFiles();
        log.info("{} images found for processing", images.length);

        for (File image : images) {
            double[][] imageData = getGrayscaleData(image);
            log.info("Image size: [heigth: {}; weigth: {}] ", imageData.length, imageData[0].length);
            int n = MathUtils.getExact2Power(imageData.length);
            TransformResultHolder holder = TransformResultHolder.createHolder(imageData);
            int maxLevel = n - 4;
            performTransform(holder, maxLevel);
            log.info("Power of two: {}. Performing {} transforms.", n, maxLevel);

            File aprior = new File(outputDir, "aprior");
            File aprior2 = new File(outputDir, "aprior2");
            File aprior3 = new File(outputDir, "aprior3");
            File simple = new File(outputDir, "simple");
            File blocks = new File(outputDir, "block");
            File blocksLS = new File(outputDir, "blockLS");
            aprior.mkdir();
            aprior2.mkdir();
            aprior3.mkdir();
            simple.mkdir();
            blocks.mkdir();
            blocksLS.mkdir();

            // aprioriHelper.perform(holder, new File(aprior, image.getName() + ".txt"));
            // aprioriHelper2.perform(holder, new File(aprior2, image.getName() + ".txt"));
            aprioriHelper3.perform(holder, new File(aprior3, image.getName() + ".txt"));
            // simpleHelper.perform(holder, new File(simple, image.getName() + ".txt"));
            // blocksHelper.perform(holder, new File(blocks, image.getName() + ".txt"));
            // blocksLSHelper.perform(holder, new File(blocksLS, image.getName() + ".txt"));
        }
    }

    private static TransformResultHolder performTransform(TransformResultHolder root, final int levels) {
        TransformResultHolder current = root;
        for (int level = 0; level < levels; ++level) {
            current = current.transformNextLevel();
        }

        return root;
    }

    public static double[][] getGrayscaleData(File imageFile) throws IOException {
        BufferedImage image = ImageUtils.loadImage(imageFile);
        image = ImageUtils.tryCreateGrayscaleCopy(image);
        return ImageUtils.getGrayscaleImageData(image);
    }

}
