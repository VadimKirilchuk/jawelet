package ru.ifmo.diplom.kirilchuk.trash.statistic.table.util;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.ifmo.diplom.kirilchuk.coder.codebook.CodeBookGenerator;
import ru.ifmo.diplom.kirilchuk.coder.codebook.FilterProbabilityPair;
import ru.ifmo.diplom.kirilchuk.coder.codebook.GeneratorResult;
import ru.ifmo.diplom.kirilchuk.coder.zones.ZoneByDispersionFinder;
import ru.ifmo.diplom.kirilchuk.coders.impl.fusion.FusionCodeOutputStream;
import ru.ifmo.diplom.kirilchuk.coders.io.impl.CountingBitOutput;
import ru.ifmo.diplom.kirilchuk.trash.statistic.TransformResultHolder;
import ru.ifmo.diplom.kirilchuk.util.ArrayUtils;
import ru.ifmo.diplom.kirilchuk.util.ImageUtils;

public class AprioriHelper3 {

    private Logger log = LoggerFactory.getLogger(getClass());

    private List<double[][]> filters = new ArrayList<double[][]>();
    {
        filters.add(new double[][] { { 1, 0, 0, 0 } });

        filters.add(new double[][] {
                { 1, 0, 0, 0 },
                { 0, 1, 0, 0 },
                { 0, 0, 1, 0 },
                { 0, 0, 0, 1 }
        });

        // filters.add(new double[][] {
        // { 1, 0, 0, 0 },
        // { 0, 1, 0, 0 },
        // { 0, 0, 1, 0 },
        // { 0, 0, 0, 1 },
        // { 1, 1, 0, 0 },
        // { 1, 0, 1, 0 },
        // { 1, 0, 0, 1 },
        // { 0, 1, 1, 0 },
        // { 0, 1, 0, 1 },
        // { 0, 0, 1, 1 }
        // });
    }

    public void perform(TransformResultHolder holder, File results) throws Exception {
        results.delete();

        BufferedWriter writer = new BufferedWriter(new FileWriter(results));
        writer.write("LEVELS\tLL\tfSize\tLH\tHL\tHH\tALLBytes");
        writer.write("\r\n");

        for (double[][] filterSet : filters) {
            TransformResultHolder currentHolder = holder;
            int previousLevelBytes = 0; // without LL part

            do {
                writer.write(currentHolder.getLevel() + "\t");
                int[][] LL = ArrayUtils.convert(currentHolder.getLL());
                int[][] LH = ArrayUtils.convert(currentHolder.getLH());
                int[][] HL = ArrayUtils.convert(currentHolder.getHL());
                int[][] HH = ArrayUtils.convert(currentHolder.getHH());

                int bytesLL = countEncodedBytesForOther(LL, filterSet);// countEncodedBytesForLL(LL, filterSet);
                int bytesLH = countEncodedBytesForOther(LH, filterSet);
                int bytesHL = countEncodedBytesForOther(HL, filterSet);
                int bytesHH = countEncodedBytesForOther(HH, filterSet);
                writer.write(bytesLL + "\t");
                writer.write(filterSet.length + "\t");

                writer.write(bytesLH + "\t");
                writer.write(bytesHL + "\t");
                writer.write(bytesHH + "\t");

                // log.info("Previous level except LL size: {} bytes", previousLevelBytes);
                // log.info("LL size: {} bytes", bytesLL);
                // log.info("LH size: {} bytes", bytesLH);
                // log.info("HL size: {} bytes", bytesHL);
                // log.info("HH size: {} bytes", bytesHH);
                int allSize = bytesLL + bytesLH + bytesHL + bytesHH + previousLevelBytes;
                // log.info("ALL SIZE: {} bytes", allSize);

                writer.write(allSize + "\t");
                writer.write("\r\n");
                previousLevelBytes += bytesLH + bytesHL + bytesHH;
                currentHolder = currentHolder.getNextLevel();
            } while (currentHolder.getNextLevel() != null);
            writer.write("\r\n");
        }
        writer.close();
    }

    private int countEncodedBytesForOther(int[][] data, double[][] filterSet) throws IOException {
        if (data.length == 0) {
            return 0;
        }

        int size = data.length;

        ZoneByDispersionFinder finder = new ZoneByDispersionFinder(5);
        int[][] zones = finder.buildZones(data);
        // CommonUtils.prettyPrint(zones, "/media/KASPERSKY R/Новая папка/ActivePassive128/zones.txt");

        CodeBookGenerator passiveGenerator = new CodeBookGenerator();
        CodeBookGenerator activeGenerator = new CodeBookGenerator();
        for (double[] filter : filterSet) {
            passiveGenerator.addFilter(filter);
            activeGenerator.addFilter(filter);
        }

        // adding data to generators for central zone
        for (int row = 1; row < size; ++row) {
            for (int col = 1; col < size - 1; ++col) {
                int[] element = new int[] {
                        data[row - 1][col - 1],
                        data[row - 1][col],
                        data[row - 1][col + 1],
                        data[row][col - 1],
                        data[row][col],
                        // position
                        row,
                        col
                };
                if (zones[row][col] == 0) {
                    passiveGenerator.addData(element);
                } else {
                    activeGenerator.addData(element);
                }
            }
        }

        // don`t forget last column
        int lastCol = size - 1;
        for (int row = 1; row < size; ++row) {
            int[] element = new int[] {
                    data[row - 1][lastCol - 1],
                    data[row - 1][lastCol],
                    data[row - 1][lastCol], // double previous
                    data[row][lastCol - 1],
                    data[row][lastCol],
                    // position
                    row,
                    lastCol
            };
            if (zones[row][lastCol] == 0) {
                passiveGenerator.addData(element);
            } else {
                activeGenerator.addData(element);
            }
        }

        GeneratorResult passiveGeneratorResult = passiveGenerator.perform(size - 1);
        GeneratorResult activeGeneratorResult = activeGenerator.perform(size - 1);

        // ///////////////////////////////////////////////////
        CountingBitOutput outBits = new CountingBitOutput();
        FusionCodeOutputStream passiveOut = CommonUtils.createFusionCoder(outBits);

        CountingBitOutput outBits2 = new CountingBitOutput();
        FusionCodeOutputStream activeOut = CommonUtils.createFusionCoder(outBits2);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = CommonUtils.createGZIP(bos);
        // //////////////////////////////////////////////////
        // We need to output first column and first row as is
        int[][] tmp = new int[data.length][data.length];

        activeOut.write(data[0][0]);
        tmp[0][0] = data[0][0];
        for (int col = 1; col < size; ++col) {
            int diff = data[0][col] - data[0][col - 1];
            tmp[0][col] = diff;
            activeOut.write(diff);
        }

        for (int row = 1; row < size; ++row) {
            int diff = data[row][0] - data[row - 1][0];
            tmp[row][0] = diff;
            activeOut.write(diff);
        }

        FilterProbabilityPair[] passiveFilterPack = passiveGeneratorResult.filterPack;
        FilterProbabilityPair[] activeFilterPack = activeGeneratorResult.filterPack;

        for (int row = 1; row < size; ++row) {
            for (int col = 1; col < size - 1; ++col) {
                int[] element = new int[] {
                        data[row - 1][col - 1],
                        data[row - 1][col],
                        data[row - 1][col + 1],
                        data[row][col - 1],
                        data[row][col] // actual value
                };
                final int diff;
                if (zones[row][col] == 0) {
                    diff = doWithElement(passiveOut, gzip, passiveFilterPack, element, passiveGeneratorResult, row, col);
                } else {
                    diff = doWithElement(activeOut, gzip, activeFilterPack, element, activeGeneratorResult, row, col);
                }
                tmp[row][col] = diff;
            }
        }

        // don`t forget last column
        for (int row = 1; row < size; ++row) {
            int[] element = new int[] {
                    data[row - 1][lastCol - 1],
                    data[row - 1][lastCol],
                    data[row - 1][lastCol], // double previous
                    data[row][lastCol - 1],
                    data[row][lastCol]
            };
            final int diff;
            if (zones[row][lastCol] == 0) {
                diff = doWithElement(passiveOut, gzip, passiveFilterPack, element, passiveGeneratorResult, row, lastCol);
            } else {
                diff = doWithElement(activeOut, gzip, activeFilterPack, element, activeGeneratorResult, row, lastCol);
            }
            tmp[row][lastCol] = diff;
        }
        CommonUtils.prettyPrint(tmp, "/media/KASPERSKY R/Новая папка/ActivePassive512/pred.txt");
        double[][] tmp2 = ArrayUtils.convert(tmp);
        // for (int row = 0; row < tmp2.length; ++row) {
        // for (int col = 0; col < tmp2.length; ++col) {
        // tmp2[row][col] = tmp2[row][col] + 127;
        // }
        // }
        BufferedImage image = ImageUtils.createNewGrayscaleImage(tmp2);
        ImageUtils.setNewGrayscaleImageData(image, tmp2);
        ImageUtils.saveAsBitmap(image, "/media/KASPERSKY R/Новая папка/ActivePassive512/image.bmp");

        passiveOut.close();
        activeOut.close();
        gzip.close();
        // //////////////////////////////////////////////////
        int passiveBytes = (outBits.getBits() / 8);
        int activeBytes = (outBits2.getBits() / 8);
        int result = passiveBytes + activeBytes + CommonUtils.countBytesFromBOS(bos);
        // System.out.println("Passive: " + passiveBytes + " active: " + activeBytes);

        return result;
    }

    private int doWithElement(FusionCodeOutputStream out, GZIPOutputStream gzip, FilterProbabilityPair[] filterPack,
            int[] element, GeneratorResult generatorResult, int row, int col) throws IOException {

        row = row - 1;
        col = col - 1;
        int filterIndex = generatorResult.optimal[row][col].filter.realIndex;;

        FilterProbabilityPair pair = filterPack[filterIndex];
        double[] filter = pair.filter;
        Integer error = GeneratorResult.countError(filter, element);

        out.write(error);
        if (filterPack.length > 1) {
            gzip.write(filterIndex);
        }

        return error;
    }
}
