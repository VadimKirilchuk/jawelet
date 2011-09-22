package ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.Filter;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.CyclicBeginExtension;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.CyclicEndExtension;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.ZeroPaddingToEven;

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
        Filter filter = new Filter(new double[]{2, 8, 4, -1});

        double[] result = new Extensioner(data)
                .schedule(new CyclicEndExtension(filter.getLength()))
                .execute();
        double[] expected = {1, 2, 3, 4, 1, 2, 3, 4};
        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result, 0);

        data = new double[]{1, 2, 3};
        result = new Extensioner(data)
                .schedule(new ZeroPaddingToEven())
                .schedule(new CyclicEndExtension(filter.getLength()))
                .execute();
        expected = new double[]{1, 2, 3, 0, 1, 2, 3, 0};
        assertEquals(expected.length, result.length);
        assertArrayEquals(expected, result, 0);
    }

    /**
     * Test of extend method, of class Extensioner.
     */
    @Test
    public void testExtendBeginning() {
        double[] data = {1, 2, 3, 4};
        Filter filter = new Filter(new double[]{2, 8});

        double[] result = new Extensioner(data)
                .schedule(new CyclicBeginExtension(filter.getLength()))
                .execute();

        assertEquals(data.length + filter.getCoeff().length, result.length);

        double[] expected = {3, 4, 1, 2, 3, 4};

        assertArrayEquals(expected, result, 0);
    }

    @Test
    public void testEndExtensionWhenFilterLongButDataSmall() {
        double[] data = {1, 2};
        Filter filter = new Filter(new double[]{1, 1, 1, 1, 1, 1, 1});

        double[] result = new Extensioner(data)
                .schedule(new CyclicEndExtension(filter.getLength()))
                .execute();

        double[] expected = {1, 2, 1, 2, 1, 2, 1, 2, 1};
        assertArrayEquals(expected, result, 0);
    }

    @Test
    public void testBeginExtensionWhenFilterLongButDataSmall() {
        double[] data = {1, 2};
        Filter filter = new Filter(new double[]{1, 1, 1, 1, 1, 1, 1});

        double[] result = new Extensioner(data)
                .schedule(new CyclicBeginExtension(filter.getLength()))
                .execute();

        double[] expected = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        assertArrayEquals(expected, result, 0);
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testDataCantBeNull() {
        double[] data = null;
        Filter filter = new Filter(new double[]{2, 8, 4, -1});
//        new Extensioner().extend(data, filter);
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testFilterCantBeNull() {
        double[] data = {11, 1, 1, 1};
        Filter filter = null;
//        new Extensioner().extend(data, filter);
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testArgsCantBeNull() {
        double[] data = null;
        Filter filter = null;
//        new Extensioner().extend(data, filter);
    }
}
