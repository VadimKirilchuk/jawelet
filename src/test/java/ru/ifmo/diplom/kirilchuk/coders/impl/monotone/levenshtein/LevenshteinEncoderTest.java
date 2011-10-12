package ru.ifmo.diplom.kirilchuk.coders.impl.monotone.levenshtein;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import ru.ifmo.diplom.kirilchuk.coders.StringBitOutput;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.levenshtein.LevenshteinEncoder;

public class LevenshteinEncoderTest {

	private LevenshteinEncoder encoder;
	
	@Before
	public void before() {
		encoder = new LevenshteinEncoder();
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEncodeZeroShouldFail() throws Exception {
		encoder.encode(0, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEncodeMinIntShouldFail() throws Exception {
		encoder.encode(Integer.MIN_VALUE, null);
	}
	
	@Test
	public void testEncodePositive() throws Exception {
		encodeWithAssert(1, "101");
		encodeWithAssert(21, "11111010101");
	}
	
	@Test
	public void testEncodeNegative() throws Exception {
		encodeWithAssert(-1, "001");
		encodeWithAssert(-21, "01111010101");
	}
	
	@Test//TODO
	public void testEncodeMaxIntMustBeOk() throws Exception {
//		encodeWithAssert(1, "101");
//		encodeWithAssert(21, "11111010101");
	}
	
	private void encodeWithAssert(int value, String expected) throws Exception {
		StringBitOutput out = new StringBitOutput();
		encoder.encode(value, out);
		assertEquals(expected, out.getOutputedString());
	}
}
