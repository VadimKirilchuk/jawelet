package ru.ifmo.diplom.kirilchuk.coder;

import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import ru.ifmo.diplom.kirilchuk.coder.context.ContextLL;
import ru.ifmo.diplom.kirilchuk.coder.context.ContextOther;
import ru.ifmo.diplom.kirilchuk.coder.predict.LLPredictioner;
import ru.ifmo.diplom.kirilchuk.coder.zones.ZonesRescaler;
import ru.ifmo.diplom.kirilchuk.coders.Encoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.AdaptiveUnigramModel;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithEncoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.fusion.FusionCodeOutputStream;
import ru.ifmo.diplom.kirilchuk.coders.impl.fusion.FusionEncoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.fixed.FixedEncoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;
import ru.ifmo.diplom.kirilchuk.coders.io.impl.BitOutputImpl;
import ru.ifmo.diplom.kirilchuk.util.AnalyzeUtils;
import ru.ifmo.diplom.kirilchuk.util.ArrayUtils;
import ru.ifmo.diplom.kirilchuk.util.Assert;
import ru.ifmo.diplom.kirilchuk.util.ImageUtils;
import ru.ifmo.diplom.kirilchuk.util.MathUtils;

/**
 * Really important class. Here comes the cumbersome logic of coding from original image.
 * Sorry for mess with contexts - it was written under hard time pressure.  
 */
public class JaweletCoderFacade {

    private File targetDir;
    private File debugDir;
    private JaweletCoderConfig config;
    private TransformHandler holder;
    
    public JaweletCoderFacade(File targetDir, JaweletCoderConfig config) {
        this.targetDir = targetDir;
        this.debugDir = new File(targetDir, "debug");  
        this.config = config;
    }
    
    public void encodeImage(BufferedImage image) throws IOException {        
        double[][] originalData = ImageUtils.getGrayscaleImageData(image);
        Assert.checkNotNull(originalData, "Data can`t be null");
        int height = originalData.length;
        Assert.argCondition(MathUtils.is2power(height), "Data height must be power of two");
        int width = originalData[0].length;
        Assert.argCondition(MathUtils.is2power(width), "Data width must be power of two");
        Assert.argCondition(width == height, "Image must be square");

        FileUtils.deleteDirectory(targetDir);
        FileUtils.deleteDirectory(debugDir);
        targetDir.mkdirs();
        debugDir.mkdirs();
        
        // first performing transformations
        holder = TransformHandler.createHolder(originalData);
        int targetLevel = config.getTransformLevel();
        while (targetLevel > 0) {
            targetLevel--;
            holder = holder.transformNextLevel();
        }

        // Main part - encoding transformation results with zones encoding
        encodeAll();        
    }

    private void encodeAll() throws IOException {
        // performing prediction of low freq part
        int[][] data = ArrayUtils.convert(holder.getLL());
        LLPredictioner predictioner = new LLPredictioner(data, new double[][] { { 1, 0, 0, 0 } });
        predictioner.setDispersionThreshold(config.getDispThresholdLL());
        int[][] predictedLL = predictioner.buildDifference();
        int[][] zones = predictioner.getZones();

        File file = newFile(debugDir, "original");
        CommonUtils.prettyPrint(data, file.getAbsolutePath());
        System.out.println(AnalyzeUtils.calculateEntropy(data, 0, 0, data.length, data.length));
        // we need to encode filters
        debugFilters(predictioner.getActiveFilter(), predictioner.getPassiveFilter());
        encodeFilters(predictioner.getActiveFilter(), predictioner.getPassiveFilter());

        // encode predicted LL
        String level = "lvl" + holder.getLevel() + "-LL";       
        encodeLLTransformPart(predictedLL, zones, level);
        System.out.println(AnalyzeUtils.calculateEntropy(predictedLL, 0, 0, data.length, data.length));
        encodeZones(zones);

        // debug
        debugZones(zones, holder.getLevel());
        file = newFile(debugDir, level);
        CommonUtils.prettyPrint(predictedLL, file.getAbsolutePath());

        ZonesRescaler rescaler = new ZonesRescaler();
        // and then other parts
        while (holder.getLevel() > 0) { // we dont need to encode holders 0 lvl
                                        // as it is original image data
            int[][] contextLL = ArrayUtils.convert(holder.getLL());
            // encode LH
            data = ArrayUtils.convert(holder.getLH());
            level = "lvl" + holder.getLevel() + "-LH";
            encodeTransformPart(data, zones, level, contextLL);
            file = newFile(debugDir, level);
            CommonUtils.prettyPrint(data, file.getAbsolutePath());

            // encode HL
            data = ArrayUtils.convert(holder.getHL());
            level = "lvl" + holder.getLevel() + "-HL";
            encodeTransformPart(data, zones, level, contextLL);
            file = newFile(debugDir, level);
            CommonUtils.prettyPrint(data, file.getAbsolutePath());

            // encode HH
            data = ArrayUtils.convert(holder.getHH());
            level = "lvl" + holder.getLevel() + "-HH";
            encodeTransformPart(data, zones, level, contextLL);
            file = newFile(debugDir, level);
            CommonUtils.prettyPrint(data, file.getAbsolutePath());

            holder = holder.getPreviousLevel();
            zones = rescaler.rescaleZones(zones);
        }
    }
    
    
    /*
     * SORRY FOR SUCH CODE!!! =) 
     */
    
    
    private void encodeLLTransformPart(int[][] data, int[][] zones, String level) throws IOException {     
        String activePart = "-active";
        String passivePart = "-passive";
        
        int threshold = config.getArithmeticCodeThreshold();
        int range = ArrayUtils.findRange(data, threshold);
//        FusionCodeOutputStream
        //coders for contexts
        //0
        FusionCodeOutputStream activeCoder0  = createFusionCoder(level + activePart  + "0", threshold, range);
        FusionCodeOutputStream passiveCoder0 = createFusionCoder(level + passivePart + "0", threshold, range);
        //1
        FusionCodeOutputStream activeCoder1  = createFusionCoder(level + activePart  + "1", threshold, range);
        FusionCodeOutputStream passiveCoder1 = createFusionCoder(level + passivePart + "1", threshold, range);
        //2
        FusionCodeOutputStream activeCoder2  = createFusionCoder(level + activePart  + "2", threshold, range);
        FusionCodeOutputStream passiveCoder2 = createFusionCoder(level + passivePart + "2", threshold, range);
        //3
        FusionCodeOutputStream activeCoder3  = createFusionCoder(level + activePart  + "3", threshold, range);
        FusionCodeOutputStream passiveCoder3 = createFusionCoder(level + passivePart + "3", threshold, range);
        
        //first row and first column are exceptions
        for (int col = 0; col < data.length; ++col) {
            int zone = zones[0][col];
            int value = data[0][col];
            if (zone == 0) {
                passiveCoder3.write(value);
            } else {
                activeCoder3.write(value); 
            }
        }
        
        for (int row = 0; row < data.length; ++row) {
            int zone = zones[row][0];
            int value = data[row][0];
            if (zone == 0) {
                passiveCoder3.write(value);
            } else {
                activeCoder3.write(value); 
            }
        }
                
        List<Integer> pas0data = new ArrayList<Integer>();
        List<Integer> pas1data = new ArrayList<Integer>();
        List<Integer> pas2data = new ArrayList<Integer>();
        List<Integer> pas3data = new ArrayList<Integer>();
        List<Integer> act0data = new ArrayList<Integer>();
        List<Integer> act1data = new ArrayList<Integer>();
        List<Integer> act2data = new ArrayList<Integer>();
        List<Integer> act3data = new ArrayList<Integer>();
        List<ContextLL> conA = new ArrayList<ContextLL>();
        List<ContextLL> conP = new ArrayList<ContextLL>();
        for (int row = 1; row < data.length; row++) {
            for (int col = 1; col < data.length; col++) {
                int zone = zones[row][col];
                int value = data[row][col];
                ContextLL сon = ContextLL.create(data, zones, row, col, value);
                int context = ContextLL.contextNumber(сon);                
                if (zone == 0) {                    
                    if (context == 0) {
                        passiveCoder0.write(value);
                        pas0data.add(value);
                    } else if (context == 1) {
                        passiveCoder1.write(value);
                        pas1data.add(value);
                    } else if (context == 2) {
                        passiveCoder2.write(value);
                        pas2data.add(value);
                    } else {
                        passiveCoder3.write(value);
                        pas3data.add(value);
                    }
                    conP.add(сon);
                } else {
                    if (context == 0) {
                        activeCoder0.write(value);
                        act0data.add(value);
                    } else if (context == 1) {
                        activeCoder1.write(value);
                        act1data.add(value);
                    } else if (context == 2) {
                        activeCoder2.write(value);
                        act2data.add(value);
                    } else {
                        activeCoder3.write(value);
                        act3data.add(value);
                    }
                    conA.add(сon);
                }
            }
        }
        activeCoder0.close();
        passiveCoder0.close();
        activeCoder1.close();
        passiveCoder1.close();
        activeCoder2.close();
        passiveCoder2.close();
        activeCoder3.close();
        passiveCoder3.close();
        
        File p0 = newFile(debugDir, level + "pas0.txt");        
        File p1 = newFile(debugDir, level + "pas1.txt");
        File p2 = newFile(debugDir, level + "pas2.txt");
        File p3 = newFile(debugDir, level + "pas3.txt");
        File a0 = newFile(debugDir, level + "act0.txt");        
        File a1 = newFile(debugDir, level + "act1.txt");
        File a2 = newFile(debugDir, level + "act2.txt");
        File a3 = newFile(debugDir, level + "act3.txt");
        File aContext = newFile(debugDir, level + "contextA.txt");
        File pContext = newFile(debugDir, level + "contextP.txt");
        CommonUtils.prettyPrint(pas0data, p0.getAbsolutePath());
        CommonUtils.prettyPrint(pas1data, p1.getAbsolutePath());
        CommonUtils.prettyPrint(pas2data, p2.getAbsolutePath());
        CommonUtils.prettyPrint(pas3data, p3.getAbsolutePath());
        CommonUtils.prettyPrint(act0data, a0.getAbsolutePath());
        CommonUtils.prettyPrint(act1data, a1.getAbsolutePath());
        CommonUtils.prettyPrint(act2data, a2.getAbsolutePath());
        CommonUtils.prettyPrint(act3data, a3.getAbsolutePath());
        CommonUtils.debug(conA, aContext.getAbsolutePath());
        CommonUtils.debug(conP, pContext.getAbsolutePath());
    }

    private void encodeTransformPart(int[][] data, int[][] zones, String level, int[][] contextLL) throws IOException {
        String activePart = "-active";
        String passivePart = "-passive";
        
        int range = ArrayUtils.findRange(data, config.getArithmeticCodeThreshold());
        //coders for contexts
        //0
        FusionCodeOutputStream activeCoder0  = createFusionCoder(level + activePart  + "0", config.getArithmeticCodeThreshold(), range);
        FusionCodeOutputStream passiveCoder0 = createFusionCoder(level + passivePart + "0", config.getArithmeticCodeThreshold(), range);
        //1
        FusionCodeOutputStream activeCoder1  = createFusionCoder(level + activePart  + "1", config.getArithmeticCodeThreshold(), range);
        FusionCodeOutputStream passiveCoder1 = createFusionCoder(level + passivePart + "1", config.getArithmeticCodeThreshold(), range);
        //2
        FusionCodeOutputStream activeCoder2  = createFusionCoder(level + activePart  + "2", config.getArithmeticCodeThreshold(), range);
        FusionCodeOutputStream passiveCoder2 = createFusionCoder(level + passivePart + "2", config.getArithmeticCodeThreshold(), range);
        //3
        FusionCodeOutputStream activeCoder3  = createFusionCoder(level + activePart  + "3", config.getArithmeticCodeThreshold(), range);
        FusionCodeOutputStream passiveCoder3 = createFusionCoder(level + passivePart + "3", config.getArithmeticCodeThreshold(), range);
        
        //first row and first column are exceptions
        for (int col = 0; col < data.length; ++col) {
            int zone = zones[0][col];
            int value = data[0][col];
            if (zone == 0) {
                passiveCoder3.write(value);
            } else {
                activeCoder3.write(value); 
            }
        }
        
        for (int row = 0; row < data.length; ++row) {
            int zone = zones[row][0];
            int value = data[row][0];
            if (zone == 0) {
                passiveCoder3.write(value);
            } else {
                activeCoder3.write(value); 
            }
        }
                
        List<Integer> pas0data = new ArrayList<Integer>();
        List<Integer> pas1data = new ArrayList<Integer>();
        List<Integer> pas2data = new ArrayList<Integer>();
        List<Integer> pas3data = new ArrayList<Integer>();
        List<Integer> act0data = new ArrayList<Integer>();
        List<Integer> act1data = new ArrayList<Integer>();
        List<Integer> act2data = new ArrayList<Integer>();
        List<Integer> act3data = new ArrayList<Integer>();
        for (int row = 1; row < data.length; row++) {
            for (int col = 1; col < data.length; col++) {
                int zone = zones[row][col];
                int value = data[row][col];
                int context = ContextOther.contextNumber(ContextOther.create(contextLL, data, zones, row, col));                
                if (zone == 0) {                    
                    if (context == 0) {
                        passiveCoder0.write(value);
                        pas0data.add(value);
                    } else if (context == 1) {
                        passiveCoder1.write(value);
                        pas1data.add(value);
                    } else if (context == 2) {
                        passiveCoder2.write(value);
                        pas2data.add(value);
                    } else {
                        passiveCoder3.write(value);
                        pas3data.add(value);
                    }
                } else {
                    if (context == 0) {
                        activeCoder0.write(value);
                        act0data.add(value);
                    } else if (context == 1) {
                        activeCoder1.write(value);
                        act1data.add(value);
                    } else if (context == 2) {
                        activeCoder2.write(value);
                        act2data.add(value);
                    } else {
                        activeCoder3.write(value);
                        act3data.add(value);
                    }
                }
            }
        }
        activeCoder0.close();
        passiveCoder0.close();
        activeCoder1.close();
        passiveCoder1.close();
        activeCoder2.close();
        passiveCoder2.close();
        activeCoder3.close();
        passiveCoder3.close();
        
        File p0 = newFile(debugDir, level + "pas0.txt");        
        File p1 = newFile(debugDir, level + "pas1.txt");
        File p2 = newFile(debugDir, level + "pas2.txt");
        File p3 = newFile(debugDir, level + "pas3.txt");
        File a0 = newFile(debugDir, level + "act0.txt");        
        File a1 = newFile(debugDir, level + "act1.txt");
        File a2 = newFile(debugDir, level + "act2.txt");
        File a3 = newFile(debugDir, level + "act3.txt");
        CommonUtils.prettyPrint(pas0data, p0.getAbsolutePath());
        CommonUtils.prettyPrint(pas1data, p1.getAbsolutePath());
        CommonUtils.prettyPrint(pas2data, p2.getAbsolutePath());
        CommonUtils.prettyPrint(pas3data, p3.getAbsolutePath());
        CommonUtils.prettyPrint(act0data, a0.getAbsolutePath());
        CommonUtils.prettyPrint(act1data, a1.getAbsolutePath());
        CommonUtils.prettyPrint(act2data, a2.getAbsolutePath());
        CommonUtils.prettyPrint(act3data, a3.getAbsolutePath());
    }
    
    private FusionCodeOutputStream createFusionCoder(String name, int arithThreshold, int range) throws IOException {
        File arithFile = newFile(targetDir, name + "-arith");
        File monoFile = newFile(targetDir, name + "-mono");
        BitOutput arithOut = new BitOutputImpl(new FileOutputStream(arithFile));
        BitOutput monoOut = new BitOutputImpl(new FileOutputStream(monoFile));
        ArithEncoder arithCoder = new ArithEncoder(new AdaptiveUnigramModel());
        Encoder monoCoder = new FixedEncoder(range);
        FusionEncoder encoder = new FusionEncoder(arithCoder, monoCoder);
        encoder.setArithmeticCodeThreshold(arithThreshold);
        FusionCodeOutputStream out = new FusionCodeOutputStream(encoder, arithOut, monoOut);

        return out;
    }

    private void debugFilters(double[] activeFilter, double[] passiveFilter) throws IOException {
        File filters = newFile(debugDir, "filters");
        FileWriter output = new FileWriter(filters);
        output.write("Active filer: " + Arrays.toString(activeFilter));
        output.write("\r\nPassive filer: " + Arrays.toString(passiveFilter));

        output.close();
    }

    private void encodeFilters(double[] activeFilter, double[] passiveFilter) throws IOException {
        File filters = newFile(targetDir, "filters");
        FileOutputStream outputStream = new FileOutputStream(filters);
        DataOutputStream dataOut = new DataOutputStream(outputStream);
        for (int i = 0; i < activeFilter.length; i++) {
            dataOut.writeDouble(activeFilter[i]);
        }
        for (int i = 0; i < passiveFilter.length; i++) {
            dataOut.writeDouble(passiveFilter[i]);
        }
        dataOut.close();
    }

    private void debugZones(int[][] zones, int level) throws IOException {
        File zonesFile = newFile(debugDir, "zones" + level);
        CommonUtils.prettyPrint(zones, zonesFile.getAbsolutePath());
    }

    private long encodeZones(int[][] zones) throws IOException {
        File zonesFile = newFile(targetDir, "zones");
        // encoding zones. Note: we are using gzip encoding for better
        // compression.
        BitOutputImpl out = new BitOutputImpl(CommonUtils.createGZIP(new FileOutputStream(zonesFile)));
        int size = zones.length;
        for (int row = 0; row < size; row += 4) {
            for (int col = 0; col < size; col += 4) {
                int value = zones[row][col];
                if (value == 1) {
                    out.writeTrueBit();
                } else if (value == 0) {
                    out.writeFalseBit();
                } else {
                    throw new RuntimeException("Unexpected zone value.");
                }
            }
        }
        out.close();
        return zonesFile.length();
    }

    private File newFile(File tempDir, String fileName) throws IOException {
        File filters = new File(tempDir, fileName);
        filters.createNewFile();
        return filters;
    }
}
