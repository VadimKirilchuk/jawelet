package ru.ifmo.diplom.kirilchuk.trash.statistic;

import java.awt.image.BufferedImage;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.DWTransform2D;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.legall.impl.LeGallLiftingWaveletTransform;
import ru.ifmo.diplom.kirilchuk.util.ArrayUtils;
import ru.ifmo.diplom.kirilchuk.util.ImageUtils;

public class TransformResultHolder {
    private final DWTransform2D transform = new DWTransform2D(new LeGallLiftingWaveletTransform());
    
    private int level;
    private int size;
    private double[][] LL;
    private double[][] HL;
    private double[][] LH;
    private double[][] HH;

    private TransformResultHolder nextLevel;

    /**
     * Creates holder from image, performing 1 level of decomposition.
     * 
     * @param image
     *            image to get data from
     * @return holder instance with 1 transformation level
     */
    public static TransformResultHolder createHolder(BufferedImage image) {
        double[][] transformationData = ImageUtils.getGrayscaleImageData(image);
        return createHolder(transformationData);
    }

    public static TransformResultHolder createHolder(double[][] transformationData) {
        TransformResultHolder result = new TransformResultHolder();

        int dataSize = transformationData.length;
        // result.transform.decomposeInplace(transformationData, 1);

        result.LL = ArrayUtils.submatrix(transformationData, 0, dataSize, 0, dataSize);
        result.HL = new double[0][0];
        result.LH = new double[0][0];
        result.HH = new double[0][0];
        // result.HL = ArrayUtils.submatrix(transformationData, 0, blockSize, blockSize, dataSize);
        // result.LH = ArrayUtils.submatrix(transformationData, blockSize, dataSize, 0, blockSize);
        // result.HH = ArrayUtils.submatrix(transformationData, blockSize, dataSize, blockSize, dataSize);

        result.level = 0;
        result.size = dataSize;

        return result;
    }

    public TransformResultHolder transformNextLevel() {
        TransformResultHolder holder = new TransformResultHolder();

        int currentBlockSize = LL.length;
        int newBlockSize = currentBlockSize / 2;

        // one more level
        double[][] currentLL = ArrayUtils.clone2DArray(LL);
        transform.decomposeInplace(currentLL, 1);

        // now splitting result on blocks
        holder.LL = ArrayUtils.submatrix(currentLL, 0, newBlockSize, 0, newBlockSize);
        holder.HL = ArrayUtils.submatrix(currentLL, 0, newBlockSize, newBlockSize, currentBlockSize);
        holder.LH = ArrayUtils.submatrix(currentLL, newBlockSize, currentBlockSize, 0, newBlockSize);
        holder.HH = ArrayUtils.submatrix(currentLL, newBlockSize, currentBlockSize, newBlockSize, currentBlockSize);

        // increasing level, setting size
        holder.level = level + 1;
        holder.size  = currentBlockSize;
        
        // chaining new level to previous one
        nextLevel = holder;

        return holder;
    }

    public int getLevel() {
        return level;
    }
    
    public int getSize() {
        return size;
    }
    
    public int getBlockSize() {
        return size / 2;
    }

    public double[][] getLL() {
        return LL;
    }

    public double[][] getHL() {
        return HL;
    }

    public double[][] getLH() {
        return LH;
    }

    public double[][] getHH() {
        return HH;
    }

    public TransformResultHolder getNextLevel() {
        return nextLevel;
    }
}
