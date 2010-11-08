package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.strategies.impl;

import java.util.Arrays;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;

/**
 *
 * @author Kirilchuk V.E.
 */
public class PeriodicalExtensionTransformStrategyTest {

    public PeriodicalExtensionTransformStrategyTest() {
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
    public static double DOBLE_COMPARISON_DELTA = 1.0e-13;
    private final PeriodicalExtensionTransformStrategy instance = new PeriodicalExtensionTransformStrategy();

    /**
     * Test of decomposeLow method, of class PeriodicalExtensionTransformStrategy.
     */
    @Test
    public void testDecomposeLow() {//TODO failing
        double[] data = {1, 2, 3, 4};
        Filter filter = new Filter() {

            @Override
            public double[] getCoeff() {
                return new double[]{2, 3};
            }
        };
        double[] expect = {7, 17};
        decomposeLowWithAssert(data, filter, expect);

        data = new double[]{122.2, 13.45, 18.1, 30.4, 55.6, 80.2, 77.7, 88.8};
        filter = new Filter() {

            @Override
            public double[] getCoeff() {
                return new double[]{2, 3};
            }
        };
        expect = new double[]{393.5000, 115.1000, 327.2000, 410.7000};
        decomposeLowWithAssert(data, filter, expect);

        data = new double[]{15.14, 18.2, 16.2, 29.4, 56.4, 20.8, 150.9, 302.2, 1000.1, 1.1};
        filter = new Filter() {

            @Override
            public double[] getCoeff() {
                return new double[]{4.235, 23.3, 245.2,532.2, 76.3,34.2};
            }
        };
        expect = new double[]{0.4731, 0.1914, 0.4271, 1.8123, 5.6117};
        decomposeLowWithAssert(data, filter, expect);

//        data = new double[]{1,2,3,4};
//        filter = new Filter() {
//
//            @Override
//            public double[] getCoeff() {
//                return new double[]{1,2,3,4,5,6};
//            }
//        };
//        expect = new double[]{58, 44}; //don`t know why matlab has 58, 44 but my realization 44,58
//        decomposeLowWithAssert(data, filter, expect);
    }

    private void decomposeLowWithAssert(double[] data, Filter filter, double[] expect) {
        System.out.println("decomposeLow");
        double[] result = instance.decomposeLow(data, filter);
        System.out.println(Arrays.toString(result));
        assertArrayEquals(expect, result, DOBLE_COMPARISON_DELTA);
    }

    /**
     * Test of decomposeHigh method, of class PeriodicalExtensionTransformStrategy.
     */
    @Test
    public void testDecomposeHigh() {//TODO failing
        double[] data = {1, 2, 3, 4};
        Filter filter = new Filter() {

            @Override
            public double[] getCoeff() {
                return new double[]{4, 5};
            }
        };
        double[] expect = {13, 31};

        decomposeHighWithAssert(data, filter, expect);

        data = new double[]{122.2, 13.45, 18.1, 30.4, 55.6, 80.2, 77.7, 88.8};
        filter = new Filter() {

            @Override
            public double[] getCoeff() {
                return new double[]{4, 5};
            }
        };
        expect = new double[]{664.8000, 212.1000, 598.8000, 743.7000};
        decomposeHighWithAssert(data, filter, expect);

        data = new double[]{15.14, 18.2, 16.2, 29.4, 56.4, 20.8, 150.9, 302.2, 1000.1, 1.1};
        filter = new Filter() {

            @Override
            public double[] getCoeff() {
                return new double[]{43.2, 12.3, 45.3, 53.2,11.1,98.9};
            }
        };
        expect = new double[]{1.0202, 0.0549, 0.2078, 0.3988, 0.7251};
        decomposeHighWithAssert(data, filter, expect);

//        data = new double[]{1,2,3,4};
//        filter = new Filter() {
//
//            @Override
//            public double[] getCoeff() {
//                return new double[]{1,2,3,4,5,6};
//            }
//        };
//        expect = new double[]{58, 44}; //don`t know why matlab has 58, 44 but my realization 44,58
//        decomposeHighWithAssert(data, filter, expect);
    }

    private void decomposeHighWithAssert(double[] data, Filter filter, double[] expect) {
        System.out.println("decomposeHigh");
        double[] result = instance.decomposeHigh(data, filter);
        System.out.println(Arrays.toString(result));
        assertArrayEquals(expect, result, DOBLE_COMPARISON_DELTA);
    }

    /**
     * Test of reconstruct method, of class PeriodicalExtensionTransformStrategy.
     */
    @Ignore
    @Test
    public void testReconstruct() {
        System.out.println("reconstruct");
        double[] approximation = null;
        double[] detail = null;
        Filter lowReconstructionFilter = null;
        Filter highReconstructionFilter = null;
        PeriodicalExtensionTransformStrategy instance = new PeriodicalExtensionTransformStrategy();
        double[] expResult = null;
        double[] result = instance.reconstruct(approximation, detail, lowReconstructionFilter, highReconstructionFilter);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
