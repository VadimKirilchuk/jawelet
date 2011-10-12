package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.legall.impl;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.DWTransform1D;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.legall.impl.LeGallLiftingWaveletTransform;
import ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils;

/**
 *
 * @author Kirilchuk V.E.
 */
public class LeGallLiftingWaveletTransformTest {

    private DWTransform1D transform;

    @Before
    public void setUp() {
        transform = new LeGallLiftingWaveletTransform();
    }

    @Test
    public void testDecomposeReconstruct1Lvl() {
        Random rnd = new Random();
        int length = 1024;

        int count = 10000;
        while(count-->0) {
        double[] data = new double[length];
        for (int i = 0; i < data.length; i++) {
            data[i] = rnd.nextInt(Integer.MAX_VALUE) - (Integer.MAX_VALUE - 10 );
        }

        double[] copy = data.clone();

        //working with copy
        transform.decomposeInplace(copy, 1);
        transform.reconstructInplace(copy, length/2);
        
        assertArrayEquals(data, copy, 0);
        }
    }
}
