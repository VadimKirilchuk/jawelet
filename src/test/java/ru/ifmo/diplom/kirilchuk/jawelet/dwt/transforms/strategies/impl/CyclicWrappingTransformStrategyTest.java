package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.strategies.impl;

import static org.junit.Assert.assertArrayEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.Filter;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.haar.impl.HaarFiltersFactory;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.strategies.impl.CyclicWrappingTransformStrategy;

/**
 *
 * @author Kirilchuk V.E.
 */
public class CyclicWrappingTransformStrategyTest {

    public CyclicWrappingTransformStrategyTest() {
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
    private CyclicWrappingTransformStrategy instance = new CyclicWrappingTransformStrategy();

    /**
     * Test of decomposeLow method, of class CyclicWrappingTransformStrategy.
     */
    @Test
    public void testDecomposeLowWithFilterLength2() {
        double[] data = {11.1, 22.2, 33.3, 44.4, 55.5, 66.6};
        Filter lowDecompositionFilter = new Filter(new double[]{1, 1});
        double[] expResult = {55.5, 99.9, 77.7};
        decomposeLowWithAssert(data, lowDecompositionFilter, expResult);

        lowDecompositionFilter = new Filter(new double[]{15.54, 18.2});
        expResult = new double[]{921.521999999999, 1670.549999999999, 1384.613999999999};
        decomposeLowWithAssert(data, lowDecompositionFilter, expResult);
    }

    private double[] decomposeLowWithAssert(double[] data, Filter filter, double[] expect) {
        double[] result = instance.decomposeLow(data, filter);
        assertArrayEquals(expect, result, DOBLE_COMPARISON_DELTA);
        return result;
    }

    /**
     * Test of decomposeHigh method, of class CyclicWrappingTransformStrategy.
     */
    @Test
    public void testDecomposeHigh() {
        double[] data = {46.2000, 565.8000, 12.1200, 163.5670, 123.1230, 90.8700, 18.3400, 1};
        Filter highDecompositionFilter = new Filter(new double[]{1.12, 1.21, 2.123, 12.2, 14.1, 7.8, 4.7});
        double[] expResult = {8009.72259, 7188.673029, 4216.37072,1546.0581};

        decomposeHighWithAssert(data, highDecompositionFilter, expResult);
    }

    private double[] decomposeHighWithAssert(double[] data, Filter filter, double[] expect) {
        double[] result = instance.decomposeHigh(data, filter);
        assertArrayEquals(expect, result, DOBLE_COMPARISON_DELTA);
        return result;
    }

    /**
     * Test of reconstruct method, of class CyclicWrappingTransformStrategy.
     */
    @Test
    public void testReconstructEvenVectorWithFilterLength2() {
        double[] approximation = {5, 9, 7};
        double[] details = {-1, -1, 5};

        Filter lowReconstructionFilter = new Filter(new double[]{1, 1});
        Filter highReconstructionFilter = new Filter(new double[]{1, -1});

        double[] expResult = {2, 4, 6, 8, 10, 12};
        double[] result = instance.reconstruct(approximation, details, lowReconstructionFilter, highReconstructionFilter);
        assertArrayEquals(expResult, result, 0);

        approximation = new double[]{6, 9, 12, 11};
        details       = new double[]{2, 3, 4, 9};
        expResult = new double[]{2, 8, 4, 12, 6, 16, 8, 20};

        result = instance.reconstruct(approximation, details, lowReconstructionFilter, highReconstructionFilter);
        assertArrayEquals(expResult, result, 0);

        approximation = new double[]{55.5, 99.9, 144.3, 99.9};
        details       = new double[]{-11.1, -11.1, -11.1, 77.7};
        expResult = new double[]{22.2, 44.4, 66.6, 88.8, 111, 133.2, 155.4, 177.6 };

        result = instance.reconstruct(approximation, details, lowReconstructionFilter, highReconstructionFilter);
        assertArrayEquals(expResult, result, DOBLE_COMPARISON_DELTA);
    }

    @Test
    public void testDecomposeReconstructForHaarFilters() {
        HaarFiltersFactory factory = new HaarFiltersFactory();
        double[] data = {20,10,15,12,14,18,40,32,14,14};
        Filter lowDec  = factory.getLowDecompositionFilter();
        Filter highDec = factory.getHighDecompositionFilter();

        double[] lowExpect = {17.6776695296636, 18.38477631085, 41.01219330881975,
                              32.52691193458118, 24.04163056034261};
        double[] low = decomposeLowWithAssert(data, lowDec, lowExpect);

        double[] highExpect = {-3.535533905932737, -1.41421356237309, -15.55634918610404,
                               12.72792206135785, -4.242640687119268};
        double[] high = decomposeLowWithAssert(data, highDec, highExpect);

        Filter lowRec = factory.getLowReconstructionFilter();
        Filter highRec = factory.getHighReconstructionFilter();
        double[] recon = instance.reconstruct(low, high, lowRec, highRec);
        assertArrayEquals(data, recon, DOBLE_COMPARISON_DELTA);

        data = new double[]{23.25, 12.23, 56.124, 76.34, 642.12, 4.1, 87.9};
        lowExpect = new double[]{48.333576921225, 508.027938011286, 65.053823869162, 16.440232662587};
        highExpect = new double[]{-31.037745053402, -400.066874659724, -59.255548263432, -16.440232662587};

        low = decomposeLowWithAssert(data, lowDec, lowExpect);
        high = decomposeLowWithAssert(data, highDec, highExpect);
    }
}
