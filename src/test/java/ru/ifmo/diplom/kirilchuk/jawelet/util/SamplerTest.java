package ru.ifmo.diplom.kirilchuk.jawelet.util;

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
public class SamplerTest {

    public SamplerTest() {
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
     * Test of decimate method, of class Decimator.
     */
    @Test
    public void testDownsample() {
        double[] data = {1, 0, 1, 0, 1, 0, 1, 0};

        Sampler sampler = new Sampler();

        double[] expResult = {1, 1, 1, 1};
        double[] result = sampler.downsample(data);

        assertEquals(data.length / 2, result.length);

        assertArrayEquals(expResult, result, 0);
    }

    /**
     * Test of upsample method, of class Sampler.
     */
    @Test
    public void testUpsample() {
        double[] data = {1, 1, 1, 1};

        Sampler sampler = new Sampler();

        double[] expResult = {1, 0, 1, 0, 1, 0, 1, 0};
        double[] result = sampler.upsample(data);

        assertEquals(data.length * 2, result.length);

        assertArrayEquals(expResult, result, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataToUpsampleCantBeNull() {
        double[] data = null;

        Sampler sampler = new Sampler();
        sampler.upsample(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDataToDownsampleCantBeNull() {
        double[] data = null;

        Sampler sampler = new Sampler();
        sampler.upsample(data);
    }
}
