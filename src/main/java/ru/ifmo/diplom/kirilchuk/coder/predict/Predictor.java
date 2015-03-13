package ru.ifmo.diplom.kirilchuk.coder.predict;

public interface Predictor {

	/**
	 * Builds prediction and then subtracts this prediction from real data
	 */
	public abstract int[][] buildDifference();

}