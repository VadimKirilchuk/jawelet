package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
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

    private static double DOBLE_COMPARISON_DELTA = 1.0e-12;

    /**
     * Test of decomposeLow method, of class DiscreteWaveletTransform.
     */
    @Test
    public void testDecomposeLow() {
//        System.out.println("decomposeLow");
//        double[] data = {0, 1, 2, 3, 4, 5, 6, 7};
//
//        DiscreteWaveletTransform instance = new HaarWaveletTransform();
//
//        double[] expResult = {2.121320343559, 4.949747468305, 7.778174593052, 4.949747468305};
//        double[] result = instance.decomposeLow(data);
//
//        assertArrayEquals(expResult, result, DOBLE_COMPARISON_DELTA);
//
//        data = new double[]{1, 2, 3, 4};
//
//        expResult = new double[]{3.535533905932, 3.535533905932};
//        result = instance.decomposeLow(data);
//
//        assertArrayEquals(expResult, result, DOBLE_COMPARISON_DELTA);
    }

    /**
     * Test of decomposeHigh method, of class DiscreteWaveletTransform.
     */
    @Test
    public void testDecomposeHigh() {
//        System.out.println("decomposeHigh");
//        double[] data = {0, 1, 2, 3, 4, 5, 6, 7};
//
//        DiscreteWaveletTransform instance = new HaarWaveletTransform();
//
//        double[] expResult = {-0.707106781186, -0.707106781186, -0.707106781186, 4.949747468306};
//        double[] result = instance.decomposeHigh(data);
//
//        assertArrayEquals(expResult, result, DOBLE_COMPARISON_DELTA);
//
//        data = new double[]{0, 3, 9, 7};
//
//        expResult = new double[]{-4.242640687119, 4.949747468306};
//        result = instance.decomposeHigh(data);
//
//        assertArrayEquals(expResult, result, DOBLE_COMPARISON_DELTA);
    }

    /**
     * Test of reconstruction method, of class DiscreteWaveletTransform.
     */
    @Test
    public void testReconstruction() {
//        System.out.println("reconstruction");
//        double[] lowData = {2.0, 12.0, 48.0, 192.0};
//        double[] highData = {-2.0, -4.0, -16.0, -64.0};
//
//        DiscreteWaveletTransform instance = new HaarWaveletTransform();
//
//        double[] expResult = {181.019335983756, 0, 2.828427124746, 5.656854249492,
//        11.313708498984, 22.627416997969, 45.254833995939, 90.509667991878};
//        double[] result = instance.reconstruct(lowData, highData);
//
//        assertArrayEquals(expResult, result, DOBLE_COMPARISON_DELTA);
    }
}
