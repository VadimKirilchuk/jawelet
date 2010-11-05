package ru.ifmo.diplom.kirilchuk.jawelet.dwt;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Kirilchuk V.E.
 */
public class ExtensionerTest {

    public ExtensionerTest() {
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
     * Test of extend method, of class Extensioner.
     */
    @Test
    public void testExtend() {
        double[] data = {1, 2, 3, 4};
        Filter filter = new Filter() {

            @Override
            public double[] getCoeff() {
                double[] filter = {2, 8, 4, -1};
                return filter;
            }
        };

        double[] result = new Extensioner().extend(data, filter);

        assertEquals(data.length + filter.getCoeff().length, result.length);

        double[] expected = {1, 2, 3, 4, 1, 2, 3, 4};
        assertArrayEquals(expected, result, 0);
    }
}
