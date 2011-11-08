package ru.ifmo.diplom.kirilchuk.jawelet.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class AnalyzeUtilsTest {

	@Test
	public void testPSNRInfinity() {
		double[][] image1 = new double[5][5];
		Double result = AnalyzeUtils.calculatePSNR(image1, image1, 255);
		assertTrue(result.isInfinite());
	}

	@Test
	public void testEntropyZero() {
		int[][] data = { { 3, 3, 3 }, { 3, 3, 3 }, { 3, 3, 3 } };
		double result = AnalyzeUtils.calculateEntropy(data, 0, 0, 3, 3);
		assertEquals(0, result, 0);
	}

	@Test
	public void testEntropyOne() {//throwing coin 
		int[][] data = { { 0, 1, 0, 1 }, { 1, 0, 1, 0 }};
		double result = AnalyzeUtils.calculateEntropy(data, 0, 0, 2, 4);
		assertEquals(1, result, 0);
	}
	
	@Test
	public void testEntropyZeroFortySeven() {//throwing coin 
		int[][] data = { { 1, 9, 9, 9, 9, 9, 9, 9, 9, 9 }};
		double result = AnalyzeUtils.calculateEntropy(data, 0, 0, 1, 10);
		assertEquals(0.47, result, 0.005);
	}
}
