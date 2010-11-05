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
public class DecimatorTest {

    public DecimatorTest() {}

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
    public void testDecimate() {
        double[] data = {1,0,1,0,1,0,1,0};
        
        Decimator decimator = new Decimator();
        
        double[] expResult = {1,1,1,1};
        double[] result = decimator.decimate(data);

        assertEquals(data.length/2, result.length);

        assertArrayEquals(expResult, result, 0);
    }
}