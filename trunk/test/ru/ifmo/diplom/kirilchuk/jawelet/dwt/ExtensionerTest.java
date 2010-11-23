package ru.ifmo.diplom.kirilchuk.jawelet.dwt;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.Filter;
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
    public void testExtendEnd() {
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

    /**
     * Test of extend method, of class Extensioner.
     */
    @Test
    public void testExtendBeginning() {
        double[] data = {1, 2, 3, 4};
        Filter filter = new Filter() {

            @Override
            public double[] getCoeff() {
                double[] filter = {2, 8};
                return filter;
            }
        };

        Extensioner extensioner = new Extensioner();
        extensioner.setMode(Extensioner.Mode.BEGINNING);
        double[] result = extensioner.extend(data, filter);

        assertEquals(data.length + filter.getCoeff().length, result.length);

        double[] expected = {3, 4, 1, 2, 3, 4};
        assertArrayEquals(expected, result, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataCantBeNull() {
        double[] data = null;
        Filter filter = new Filter() {

            @Override
            public double[] getCoeff() {
                double[] filter = {2, 8, 4, -1};
                return filter;
            }
        };
        new Extensioner().extend(data, filter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFilterCantBeNull() {
        double[] data = {11, 1, 1, 1};
        Filter filter = null;
        new Extensioner().extend(data, filter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArgsCantBeNull() {
        double[] data = null;
        Filter filter = null;
        new Extensioner().extend(data, filter);
    }
}
