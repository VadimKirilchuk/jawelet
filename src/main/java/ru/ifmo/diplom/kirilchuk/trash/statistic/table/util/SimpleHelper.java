package ru.ifmo.diplom.kirilchuk.trash.statistic.table.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.ifmo.diplom.kirilchuk.coders.impl.fusion.FusionCodeOutputStream;
import ru.ifmo.diplom.kirilchuk.coders.io.impl.CountingBitOutput;
import ru.ifmo.diplom.kirilchuk.trash.statistic.TransformResultHolder;
import ru.ifmo.diplom.kirilchuk.util.ArrayUtils;

public class SimpleHelper {

    private Logger log = LoggerFactory.getLogger(getClass());

    public void perform(TransformResultHolder holder, File results) throws IOException {
        results.delete();

        BufferedWriter writer = new BufferedWriter(new FileWriter(results));
        writer.write("LEVELS\ttype\tLL\tLH\tHL\tHH\tALLBytes");
        writer.write("\r\n");

        for (int type = 0; type < 4; ++type) {
            TransformResultHolder currentHolder = holder;
            int previousLevelBytes = 0; // without LL part
            do {
                writer.write(currentHolder.getLevel() + "\t");

                switch (type) {
                    case 0: {
                        writer.write("diag" + "\t");
                        break;
                    }
                    case 1: {
                        writer.write("vert" + "\t");
                        break;
                    }
                    case 2: {
                        writer.write("horz" + "\t");
                        break;
                    }
                    case 3: {
                        writer.write("c+b-a" + "\t");
                        break;
                    }
                }

                int[][] LL = ArrayUtils.convert(currentHolder.getLL());
                int[][] LH = ArrayUtils.convert(currentHolder.getLH());
                int[][] HL = ArrayUtils.convert(currentHolder.getHL());
                int[][] HH = ArrayUtils.convert(currentHolder.getHH());

                int bytesLL = process(LL, type);
                int bytesLH = CommonUtils.countArithmeticEncodedBytes(LH);
                int bytesHL = CommonUtils.countArithmeticEncodedBytes(HL);
                int bytesHH = CommonUtils.countArithmeticEncodedBytes(HH);
                writer.write(bytesLL + "\t");
                writer.write(bytesLH + "\t");
                writer.write(bytesHL + "\t");
                writer.write(bytesHH + "\t");

                log.info("Previous level except LL size: {} bytes", previousLevelBytes);
                log.info("LL size: {} bytes", bytesLL);
                log.info("LH size: {} bytes", bytesLH);
                log.info("HL size: {} bytes", bytesHL);
                log.info("HH size: {} bytes", bytesHH);
                int allSize = bytesLL + bytesLH + bytesHL + bytesHH + previousLevelBytes;
                log.info("ALL SIZE: {} bytes", allSize);

                writer.write(allSize + "\t");
                writer.write("\r\n");
                previousLevelBytes += bytesLH + bytesHL + bytesHH;
                currentHolder = currentHolder.getNextLevel();
            } while (currentHolder.getNextLevel() != null);
            writer.write("\r\n");
        }
        writer.close();
    }

    private int process(int[][] data, int type) throws IOException {
        CountingBitOutput outBits = new CountingBitOutput();
        FusionCodeOutputStream out = CommonUtils.createFusionCoder(outBits);

        int size = data[0].length;
        // We still need to output first column and first row
        out.write(data[0][0]);
        for (int col = 1; col < data[0].length; ++col) {
            out.write(data[0][col] - data[0][col - 1]);
        }

        for (int row = 1; row < data.length; ++row) {
            out.write(data[row][0] - data[row - 1][0]);
        }

        // processing other
        switch (type) {
            case 0: {
                for (int row = 1; row < size; ++row) { //diag
                    for (int col = 1; col < size; ++col) {
                        int elem = data[row][col] - data[row - 1][col - 1];
                        out.write(elem);
                    }
                }
                break;
            }
            case 1: {
                for (int row = 1; row < size; ++row) {
                    for (int col = 1; col < size; ++col) {// vert
                        int elem = data[row][col] - data[row][col - 1];
                        out.write(elem);
                    }
                }
                break;
            }
            case 2: {
                for (int row = 1; row < size; ++row) {
                    for (int col = 1; col < size; ++col) {// hor
                        int elem = data[row][col] - data[row - 1][col];
                        out.write(elem);
                    }
                }
                break;
            }
            case 3: {
                for (int row = 1; row < size; ++row) { //a+b-c
                    for (int col = 1; col < size; ++col) {
                        int elem = data[row][col] - (data[row - 1][col] + data[row][col - 1] - data[row - 1][col - 1]);
                        out.write(elem);
                    }
                }
                break;
            }
        }
        out.close();

        return (outBits.getBits() / 8);
    }
}
