package ru.ifmo.diplom.kirilchuk.trash.statistic.table.util;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.ifmo.diplom.kirilchuk.coder.codebook.CodeBookGenerator;
import ru.ifmo.diplom.kirilchuk.coder.codebook.FilterProbabilityPair;
import ru.ifmo.diplom.kirilchuk.coder.codebook.GeneratorResult;
import ru.ifmo.diplom.kirilchuk.coders.impl.fusion.FusionCodeOutputStream;
import ru.ifmo.diplom.kirilchuk.coders.io.impl.CountingBitOutput;
import ru.ifmo.diplom.kirilchuk.trash.statistic.TransformResultHolder;
import ru.ifmo.diplom.kirilchuk.util.AnalyzeUtils;
import ru.ifmo.diplom.kirilchuk.util.ArrayUtils;

public class AprioriHelper {

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
        writer.write("LEVELS\tLL\tcoeffA\tfSize\tLH\tHL\tHH\tALLBytes");
        writer.write("\r\n");

        for (double[][] filterSet : filters) {
            for (double a = 0; a <= 1; a += 0.2) {
                TransformResultHolder currentHolder = holder;
                int previousLevelBytes = 0; // without LL part

                do {
                    writer.write(currentHolder.getLevel() + "\t");
                    int[][] LL = ArrayUtils.convert(currentHolder.getLL());
                    int[][] LH = ArrayUtils.convert(currentHolder.getLH());
                    int[][] HL = ArrayUtils.convert(currentHolder.getHL());
                    int[][] HH = ArrayUtils.convert(currentHolder.getHH());

                    int bytesLL = countEncodedBytesForLL(LL, filterSet, a);
                    int bytesLH = CommonUtils.countArithmeticEncodedBytes(LH);
                    int bytesHL = CommonUtils.countArithmeticEncodedBytes(HL);
                    int bytesHH = CommonUtils.countArithmeticEncodedBytes(HH);
                    writer.write(bytesLL + "\t");
                    writer.write(CommonUtils.formatDouble(a) + "\t");
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
        }
        writer.close();
    }

    private int countEncodedBytesForLL(int[][] data, double[][] filterSet, double a) throws IOException {
        CodeBookGenerator generator = new CodeBookGenerator();
        for (double[] filter : filterSet) {
            generator.addFilter(filter);
        }

        for (int row = 1; row < data.length; ++row) {
            for (int col = 1; col < data[0].length - 1; ++col) {
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
                generator.addData(element);
            }
        }
        // don`t forget last column
        int lastCol = data[0].length - 1;
        for (int row = 1; row < data.length; ++row) {
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
            generator.addData(element);
        }
        GeneratorResult generatorResult = generator.perform(data.length - 1);

        // ///////////////////////////////////////////////////
        CountingBitOutput outBits = new CountingBitOutput();
        FusionCodeOutputStream out = CommonUtils.createFusionCoder(outBits);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        GZIPOutputStream gzip = CommonUtils.createGZIP(bos);
        // //////////////////////////////////////////////////
        // We need to output first column and first row as is
        int[][] tmp = new int[data.length][data.length];

        out.write(data[0][0]);
        tmp[0][0] = data[0][0];
        for (int col = 1; col < data[0].length; ++col) {
            int diff = data[0][col] - data[0][col - 1];
            tmp[0][col] = diff;
            out.write(diff);
        }

        for (int row = 1; row < data.length; ++row) {
            int diff = data[row][0] - data[row - 1][0];
            tmp[row][0] = diff;
            out.write(diff);
        }

        FilterProbabilityPair[] filterPack = generatorResult.filterPack;
        Map<Integer, Double> errorProb = generatorResult.errorProbability;
        int filtersCount = filterPack.length;
        double[] bits = new double[filtersCount];// estimate in bits how much took pair of filter and error
        int[] errors = new int[filtersCount];// difference to send
        for (int row = 1; row < data.length; ++row) {
            for (int col = 1; col < data[0].length - 1; ++col) {
                int[] element = new int[] {
                        data[row - 1][col - 1],
                        data[row - 1][col],
                        data[row - 1][col + 1],
                        data[row][col - 1],
                        data[row][col] // actual value
                };
                int diff = doWithElement(a, out, gzip, filterPack, errorProb, filtersCount, bits, errors, element);
                tmp[row][col] = diff;
            }
        }

        // don`t forget last column
        for (int row = 1; row < data.length; ++row) {
            int[] element = new int[] {
                    data[row - 1][lastCol - 1],
                    data[row - 1][lastCol],
                    data[row - 1][lastCol], // double previous
                    data[row][lastCol - 1],
                    data[row][lastCol]
            };
            int diff = doWithElement(a, out, gzip, filterPack, errorProb, filtersCount, bits, errors, element);
            tmp[row][lastCol] = diff;
        }

        out.close();
        gzip.close();
        // //////////////////////////////////////////////////
        int result = (outBits.getBits() / 8) + CommonUtils.countBytesFromBOS(bos);
        double entr = AnalyzeUtils.calculateEntropy(data, 0, 0, data.length, data.length);
        System.out.println(entr);
        CommonUtils.prettyPrint(data);
        entr = AnalyzeUtils.calculateEntropy(tmp, 0, 0, data.length, data.length);
        System.out.println(entr);
        CommonUtils.prettyPrint(tmp);
        return result;
    }

    private int doWithElement(double a, FusionCodeOutputStream out, GZIPOutputStream gzip, FilterProbabilityPair[] filterPack,
            Map<Integer, Double> errorProb, int filtersCount, double[] bits, int[] errors, int[] element) throws IOException {
        // calculating errors and bits
        for (int filterIndex = 0; filterIndex < filtersCount; ++filterIndex) {
            FilterProbabilityPair pair = filterPack[filterIndex];
            double[] filter = pair.filter;
            Integer error = GeneratorResult.countError(filter, element);
            double filterBits = pair.probability;
            Double errBits = errorProb.get(error);

            if (errBits == null) {
                errBits = 100500.0;
            }
            bits[filterIndex] = filterBits + a * errBits;
            errors[filterIndex] = error;
        }
        // getting index of minimal
        int index = findMinIndex(bits);

        out.write(errors[index]);
        if (filtersCount > 1) {
            gzip.write(index);
        }
        GeneratorResult.liftFilter(filterPack, index);

        return errors[index];
    }

    private static int findMinIndex(double[] array) {
        double minimum = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < array.length; i++) {
            if (array[i] < minimum) {
                minimum = array[i];
                index = i;
            }
        }
        return index;
    }
}
