package ru.ifmo.diplom.kirilchuk.coder.zones;

import ru.ifmo.diplom.kirilchuk.util.AnalyzeUtils;
import ru.ifmo.diplom.kirilchuk.util.ArrayUtils;

/**
 * Finds active and passive zones by dispersion.
 * If block dispersion is more then some threshold then it is cactive, otherwise - passive.
 */
public class ZoneByDispersionFinder {
	
	private int threshold;
	
	public ZoneByDispersionFinder(int dispersionThreshold) {
		this.threshold = dispersionThreshold;
	}
	
	public int[][] buildZones(int[][] data) {
		int size = data.length;
		
		// building active-passive matrix from not-predicted data
		int[][] zones = new int[size][size];
		for (int row = 0; row < size; row += 4) {
			for (int col = 0; col < size; col += 4) {
				double dispersion = AnalyzeUtils.calculateDispersion(data, row, col, row + 4, col + 4);
				if (dispersion > threshold) {// if dispersion higher then threshold then zone is active
					ArrayUtils.fillMatrix(zones, row, row + 4, col, col + 4, 1);
				}
			}
		}
		return zones;
	}
}
