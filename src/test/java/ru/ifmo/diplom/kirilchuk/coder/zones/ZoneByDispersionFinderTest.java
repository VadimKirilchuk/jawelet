package ru.ifmo.diplom.kirilchuk.coder.zones;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ZoneByDispersionFinderTest {

	@Test
	public void testBuildZonesAllActive() {
		ZoneByDispersionFinder finder = new ZoneByDispersionFinder(1);
		int[][] data = {
				{1,2,3,4,5,6,7,8},
				{1,2,3,4,5,6,7,8},
				{1,2,3,4,5,6,7,8},
				{1,2,3,4,5,6,7,8},
				{1,2,3,4,5,6,7,8},
				{1,2,3,4,5,6,7,8},
				{1,2,3,4,5,6,7,8},
				{1,2,3,4,5,6,7,8}
		};
		
		int[][] zones = finder.buildZones(data);
		for (int i = 0; i < zones.length; i++) {
			for (int j = 0; j < zones.length; j++) {
				assertEquals(1, zones[i][j]);
			}
		}
	}
	
	@Test
	public void testBuildZonesAllPassive() {
		ZoneByDispersionFinder finder = new ZoneByDispersionFinder(1);
		int[][] data = {
				{5,5,5,5,5,5,5,5},
				{5,5,5,5,5,5,5,5},
				{5,5,5,5,5,5,5,5},
				{5,5,5,5,5,5,5,5},
				{5,5,5,5,5,5,5,5},
				{5,5,5,5,5,5,5,5},
				{5,5,5,5,5,5,5,5},
				{5,5,5,5,5,5,5,5}
		};
		
		int[][] zones = finder.buildZones(data);
		for (int i = 0; i < zones.length; i++) {
			for (int j = 0; j < zones.length; j++) {
				assertEquals(0, zones[i][j]);
			}
		}
	}
}
