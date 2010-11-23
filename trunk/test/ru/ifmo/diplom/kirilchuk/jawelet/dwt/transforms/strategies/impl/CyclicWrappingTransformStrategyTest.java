/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.strategies.impl;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;

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
    private static double DOBLE_COMPARISON_DELTA = 1.0e-8;
    private CyclicWrappingTransformStrategy instance = new CyclicWrappingTransformStrategy();

    /**
     * Test of decomposeLow method, of class CyclicWrappingTransformStrategy.
     */
    @Test
    public void testDecomposeLow() {
        System.out.println("Test decomposeLow");
        double[] data = {11.1, 22.2, 33.3, 44.4, 55.5, 66.6};
        Filter lowDecompositionFilter = new Filter() {

            @Override
            public double[] getCoeff() {
                return new double[]{1, 1};
            }
        };
        double[] expResult = {55.5, 99.9, 77.7};
        decomposeLowWithAssert(data, lowDecompositionFilter, expResult);

        lowDecompositionFilter = new Filter() {

            @Override
            public double[] getCoeff() {
                return new double[]{15.54, 18.2};
            }
        };
        expResult = new double[]{921.52199999, 1670.54999999, 1384.61399999};
        decomposeLowWithAssert(data, lowDecompositionFilter, expResult);
    }

    private void decomposeLowWithAssert(double[] data, Filter filter, double[] expect) {
        double[] result = instance.decomposeLow(data, filter);
        assertArrayEquals(expect, result, DOBLE_COMPARISON_DELTA);
    }

    /**
     * Test of decomposeHigh method, of class CyclicWrappingTransformStrategy.
     */
    @Test
    public void testDecomposeHigh() {
        System.out.println("Test decomposeHigh");
        double[] data = {46.2000, 565.8000, 12.1200, 163.5670, 123.1230, 90.8700, 18.3400};

        Filter highDecompositionFilter = new Filter() {

            @Override
            public double[] getCoeff() {
                return new double[]{1.12, 1.21, 2.123, 12.2, 14.1, 7.8, 4.7};
            }
        };
        double[] expResult = {9170.207971, 6777.29970999, 3234.1393};
        decomposeHighWithAssert(data, highDecompositionFilter, expResult);
    }

    private void decomposeHighWithAssert(double[] data, Filter filter, double[] expect) {
        double[] result = instance.decomposeHigh(data, filter);
        assertArrayEquals(expect, result, DOBLE_COMPARISON_DELTA);
    }

    /**
     * Test of reconstruct method, of class CyclicWrappingTransformStrategy.
     */
    @Test
    public void testReconstruct() {
        System.out.println("reconstruct");
        double[] approximation = {5, 9, 7};
        double[] details = {-1, -1, 5};
        Filter lowReconstructionFilter = new Filter() {

            @Override
            public double[] getCoeff() {
                return new double[]{1, 1};
            }
        };
        Filter highReconstructionFilter = new Filter() {

            @Override
            public double[] getCoeff() {
                return new double[]{1, -1};
            }
        };

        CyclicWrappingTransformStrategy instance = new CyclicWrappingTransformStrategy();

        double[] expResult = {2, 4, 6, 8, 10, 12};
        double[] result = instance.reconstruct(approximation, details, lowReconstructionFilter, highReconstructionFilter);
        assertArrayEquals(expResult, result, 0);

        approximation = new double[]{6, 9, 12, 11};
        details       = new double[]{2, 3, 4, 9};

        expResult = new double[]{2, 8, 4, 12, 6, 16, 8, 20};
        result = instance.reconstruct(approximation, details, lowReconstructionFilter, highReconstructionFilter);
        assertArrayEquals(expResult, result, 0);
    }
}
