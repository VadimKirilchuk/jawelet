package ru.ifmo.diplom.kirilchuk.jawelet.util;

import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.*;

public class FilterUtilsTest {

	private static Random rnd = new Random();
	
	@Test
	public void testSymmetricTrue() {
		double[] coeff = {1.0, 2.0, 2.0, 1.0};
		assertTrue(FilterUtils.isSymmetric(coeff));
		
		coeff = new double[]{1.0, 2.0, 3.0, 2.0, 1.0};
		assertTrue(FilterUtils.isSymmetric(coeff));
		
		int iter = 1000;
		while (iter>0) {
			coeff = createSymmetric();
			assertTrue(FilterUtils.isSymmetric(coeff));
			iter--;
		}
	}
	
	@Test
	public void testSymmetricFalse() {
		double[] coeff = {1.0, 2.0, 3.0, 4.0};
		assertFalse(FilterUtils.isSymmetric(coeff));
		
		coeff = new double[]{1.0, 3.0, 3.0, 2.0, 1.0};
		assertFalse(FilterUtils.isSymmetric(coeff));	
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArg1() {
		FilterUtils.isSymmetric(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIllegalArg2() {
		FilterUtils.isSymmetric(new double[]{});
	}
	
	private double[] createSymmetric() {
		double[] coeff;
		int length = rnd.nextInt(1000) + 100;
		coeff = new double[length];
		for(int i = 0; i < length / 2; ++i) {
			double value = rnd.nextDouble(); 
			coeff[i] = value;
			coeff[length - 1 - i] = value;
		}
		return coeff;
	}
}
