package ru.ifmo.diplom.kirilchuk.coder;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.DWTransform2D;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.legall.impl.LeGallLiftingWaveletTransform;
import ru.ifmo.diplom.kirilchuk.util.ArrayUtils;

/**
 * This class responsibility is creating transformation data for coder. 
 */
public class TransformHandler {
    private final DWTransform2D transform = new DWTransform2D(new LeGallLiftingWaveletTransform());
    
    private int level;
    private int size;
    private double[][] LL;
    private double[][] HL;
    private double[][] LH;
    private double[][] HH;

    private TransformHandler previousLevel;

    public static TransformHandler createHolder(double[][] transformationData) {
        TransformHandler result = new TransformHandler();

        int dataSize = transformationData.length;

        result.LL = ArrayUtils.submatrix(transformationData, 0, dataSize, 0, dataSize);
        result.HL = new double[0][0];
        result.LH = new double[0][0];
        result.HH = new double[0][0];

        result.level = 0;
        result.size = dataSize;

        return result;
    }

    public TransformHandler transformNextLevel() {
        TransformHandler nextLevelHolder = new TransformHandler();

        int currentBlockSize = LL.length;
        int newBlockSize = currentBlockSize / 2;

        // one more level
        double[][] currentLL = ArrayUtils.clone2DArray(LL);
        transform.decomposeInplace(currentLL, 1);

        // now splitting result on blocks
        nextLevelHolder.LL = ArrayUtils.submatrix(currentLL, 0, newBlockSize, 0, newBlockSize);
        nextLevelHolder.HL = ArrayUtils.submatrix(currentLL, 0, newBlockSize, newBlockSize, currentBlockSize);
        nextLevelHolder.LH = ArrayUtils.submatrix(currentLL, newBlockSize, currentBlockSize, 0, newBlockSize);
        nextLevelHolder.HH = ArrayUtils.submatrix(currentLL, newBlockSize, currentBlockSize, newBlockSize, currentBlockSize);

        // increasing level, setting size
        nextLevelHolder.level = level + 1;
        nextLevelHolder.size  = currentBlockSize;
        
        // chaining previous level to new level
        nextLevelHolder.previousLevel = this;

        return nextLevelHolder;
    }

    public int getLevel() {
        return level;
    }
    
    public int getSize() {
        return size;
    }
    
    @Deprecated
    public int getBlockSize() {//TODO incorrect for 0 level
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
    
    public TransformHandler getPreviousLevel() {
        return previousLevel;
    }
}
