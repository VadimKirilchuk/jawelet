/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class WindowerTest {

    public WindowerTest() {}

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
     * Test of window method, of class Windower.
     */
    @Test
    public void testWindow() {
        double[] data = {1,2,3,4,5,6,7,8,9,10};
        
        int start = 3;
        int end = 5;
        Windower windower = new Windower();

        double[] expResult = {4, 5};
        double[] result = windower.window(data, start, end);
        assertArrayEquals(expResult, result, 0);
    }
}