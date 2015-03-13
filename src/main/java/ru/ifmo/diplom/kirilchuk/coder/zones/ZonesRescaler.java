package ru.ifmo.diplom.kirilchuk.coder.zones;

import ru.ifmo.diplom.kirilchuk.util.ArrayUtils;

/**
 * Rescale zone: produce [2n*2n] zones matrix from [n*n] zones matrix.
 * The purpose is for counting zones on last LL level of transform and then
 * every inverse transform rescale zones up, so we dont need to encode zones
 * on every transform level - we do it only once - on the last level.
 */
public class ZonesRescaler {

    public int[][] rescaleZones(int[][] original) {
        int originalSize = original.length;
        int newSize = originalSize * 2;
        int[][] result = new int[newSize][newSize];

        for (int row = 0; row < originalSize; row += 4) {
            for (int col = 0; col < originalSize; col += 4) {
                int val = original[row][col];
                ArrayUtils.fillMatrix(result, row * 2, row * 2 + 8, col * 2, col * 2 + 8, val);
            }
        }

        return result;
    }
}
