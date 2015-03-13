package ru.ifmo.diplom.kirilchuk.coder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

import ru.ifmo.diplom.kirilchuk.coder.context.ContextLL;
import ru.ifmo.diplom.kirilchuk.coders.Encoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.AdaptiveUnigramModel;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithEncoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.fusion.FusionCodeOutputStream;
import ru.ifmo.diplom.kirilchuk.coders.impl.fusion.FusionEncoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.elias.Elias3PartEncoder;
import ru.ifmo.diplom.kirilchuk.coders.io.impl.CountingBitOutput;

public class CommonUtils {

    public static int countArithmeticEncodedBytes(int[][] data) throws IOException {
        CountingBitOutput out1 = new CountingBitOutput();
        FusionCodeOutputStream out = createFusionCoder(out1);

        for (int i = 0; i < data.length; ++i) {
            int[] row = data[i];
            for (int k = 0; k < data[0].length; ++k) {
                out.write(row[k]);
            }
        }
        out.close();

        return (out1.getBits() / 8);
    }

    public static FusionCodeOutputStream createFusionCoder(CountingBitOutput outBits) {
        ArithEncoder arithCoder = new ArithEncoder(new AdaptiveUnigramModel());
        Encoder monoCoder = new Elias3PartEncoder();
        FusionEncoder encoder = new FusionEncoder(arithCoder, monoCoder);
        encoder.setArithmeticCodeThreshold(127);
        FusionCodeOutputStream out = new FusionCodeOutputStream(encoder, outBits, outBits);

        return out;
    }

    public static GZIPOutputStream createGZIP(OutputStream stream) throws IOException {
        return new GZIPOutputStream(stream) {
            {
                def.setLevel(Deflater.BEST_COMPRESSION);
            }
        };
    }

    public static int countBytesFromBOS(ByteArrayOutputStream stream) {
        byte[] bytes = stream.toByteArray();

        return bytes.length;
    }

    public static String formatDouble(double num) {
        DecimalFormat df = new DecimalFormat("#.###");
        return df.format(num);
    }

    public static void prettyPrint(int[][] data) {
        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < data.length; ++k) {
            int[] row = data[k];
            for (int t = 0; t < data.length; ++t) {
                sb.append(prettyInt(3, row[t]));
                sb.append(" ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    public static void prettyPrint(int[][] data, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
        	file.createNewFile();
        }
        PrintWriter writer = new PrintWriter(file);

        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < data.length; ++k) {
            int[] row = data[k];
            for (int t = 0; t < data.length; ++t) {
                sb.append(prettyInt(3, row[t]));
                sb.append(" ");
            }
            sb.append("\n");
        }
        writer.append(sb);
        writer.close();
    }

    public static void prettyPrint(List<Integer> data, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter writer = new PrintWriter(file);

        StringBuilder sb = new StringBuilder();
        for (Integer k: data) {
            sb.append(prettyInt(3, k));
            sb.append(" ");
        }
        writer.append(sb);
        writer.close();
    }

    public static void prettyPrint(double[][] data, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
        	file.createNewFile();
        }
        PrintWriter writer = new PrintWriter(file);

        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < data.length; ++k) {
            double[] row = data[k];
            for (int t = 0; t < data.length; ++t) {
                sb.append(row[t]);
                sb.append('\t');
            }
            sb.append("\n");
        }
        writer.append(sb);
        writer.close();
    }

    private static String prettyInt(int needLen, int value) {
        String res = String.valueOf(value);
        StringBuilder sb = new StringBuilder(needLen);
        int len = res.length();

        if (len < needLen) {
            for (int i = 0; i < (needLen - len); ++i) {
                sb.append(' ');
            }
        }
        sb.append(res);

        return sb.toString();
    }

    public static void debug(List<ContextLL> contexts, String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        PrintWriter writer = new PrintWriter(file);
        Collections.sort(contexts, new Comparator<ContextLL>() {
            @Override
            public int compare(ContextLL o1, ContextLL o2) {
                int sum1 = o1.getContext()[0] + o1.getContext()[1] + o1.getContext()[2];
                int sum2 = o2.getContext()[0] + o2.getContext()[1] + o2.getContext()[2];
                return sum2 - sum1;
            }
        });
        for (ContextLL contextLL : contexts) {            
            writer.print(contextLL.getContext()[2] - contextLL.getContext()[0]);
            writer.print(' ');
            writer.println(contextLL.getValue());
//            writer.print(' ');
//            writer.println(ContextLL.contextNumber(contextLL));
        }
        
        writer.close();
    }   
}
