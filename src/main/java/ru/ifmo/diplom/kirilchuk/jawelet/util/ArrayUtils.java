package ru.ifmo.diplom.kirilchuk.jawelet.util;

import java.util.Arrays;

public class ArrayUtils {
	private ArrayUtils() {}
	
	public static void print(int[][] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.println(Arrays.toString(array[i]));
		}		
	}
	
	public static void print(int[] array) {
		System.out.println(Arrays.toString(array));
	}
	
	public static void print(double[][] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.println(Arrays.toString(array[i]));
		}		
	}
	
	public static void print(double[] array) {
		System.out.println(Arrays.toString(array));
	}
	
	public static double[][] convert(int[][] array) {
		double[][] result = new double[array.length][];
		for(int row = 0; row < array.length; ++row) {
			result[row] = new double[array[row].length];
			for (int column = 0; column < array[row].length; ++ column) {
				result[row][column] = array[row][column];
			}
		}
		return result;
	}
	
	public static int[][] convert(double[][] array) {
		int[][] result = new int[array.length][];
		for(int row = 0; row < array.length; ++row) {
			result[row] = new int[array[row].length];
			for (int column = 0; column < array[row].length; ++ column) {
				result[row][column] = (int)array[row][column];
			}
		}
		return result;
	}
}
