/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms;

import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.FiltersFactory;
import static org.junit.Assert.*;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.haar.impl.HaarWaveletTransform;

/**
 *
 * @author Kirilchuk V.E.
 */
public class DiscreteWaveletTransformTest {

    public DiscreteWaveletTransformTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of decomposeLow method, of class DiscreteWaveletTransform.
     */
    @Test
    public void testDecomposeLow() {
        System.out.println("decomposeLow");
        double[] data = {0,1,2,3,4,5,6,7};

        DiscreteWaveletTransform instance = new HaarWaveletTransform();

        double[] expResult = {1,5,9,13};
        double[] result = instance.decomposeLow(data);

        assertArrayEquals(expResult, result, 0);

        data = new double[]{1,2,3,4};

        expResult = new double[]{3,7};
        result = instance.decomposeLow(data);

        assertArrayEquals(expResult, result, 0);
    }

    /**
     * Test of decomposeHigh method, of class DiscreteWaveletTransform.
     */
    @Test
    public void testDecomposeHigh() {
        System.out.println("decomposeHigh");
        double[] data = {0,1,2,3,4,5,6,7};

        DiscreteWaveletTransform instance = new HaarWaveletTransform();

        double[] expResult = {-1,-1,-1,-1};
        double[] result = instance.decomposeHigh(data);

        assertArrayEquals(expResult, result, 0);

        data = new double[]{0,3,9,27};

        expResult = new double[]{-3,-18};
        result = instance.decomposeHigh(data);

        assertArrayEquals(expResult, result, 0);
    }

    /**
     * Test of reconstruction method, of class DiscreteWaveletTransform.
     */
    @Test
    public void testReconstruction() {//TODO failing...
        System.out.println("reconstruction");
        double[] lowData = {2.0, 12.0, 48.0, 192.0};
        double[] highData = {-2.0, -4.0, -16.0, -64.0};

        DiscreteWaveletTransform instance = new HaarWaveletTransform();

        double[] expResult = {0,2,4,8,16,32,64,128};
        double[] result = instance.reconstruct(lowData, highData);

        System.out.println(Arrays.toString(result));

        assertArrayEquals(expResult, result, 0);
    }
}