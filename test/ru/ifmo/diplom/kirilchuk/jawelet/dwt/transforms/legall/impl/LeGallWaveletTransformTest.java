package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.legall.impl;

import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.DiscreteWaveletTransform;
import ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils;

/**
 *
 * @author Kirilchuk V.E.
 */
public class LeGallWaveletTransformTest {

    private DiscreteWaveletTransform transform;

    @Before
    public void setUp() {
        transform = new LeGallWaveletTransform();
    }

    @Test
    public void testDecomposeReconstruct() {
        Random rnd = new Random();
        int length = rnd.nextInt(1000) + 1000; //data with 1000..1999 elements
        int extendedLength = MathUtils.getClosest2PowerValue(length);
        int power = MathUtils.getExact2Power(extendedLength);

        double[] data = new double[extendedLength];
        for (int i = 0; i < data.length; i++) {
            data[i] = rnd.nextInt(Integer.MAX_VALUE);
        }

        double[] copy = data.clone();

        //working with copy
        transform.decomposeInplace(copy, power - 1);
        transform.reconstructInplace(copy, 2);

        assertArrayEquals(data, copy, 1);
    }
}
