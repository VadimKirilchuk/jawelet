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
}
