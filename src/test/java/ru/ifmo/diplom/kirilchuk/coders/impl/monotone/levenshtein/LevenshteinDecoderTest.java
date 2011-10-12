package ru.ifmo.diplom.kirilchuk.coders.impl.monotone.levenshtein;

import org.junit.Before;
import org.junit.Test;

import ru.ifmo.diplom.kirilchuk.coders.StringBitInput;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.levenshtein.LevenshteinDecoder;

import static org.junit.Assert.*;

public class LevenshteinDecoderTest {

	private LevenshteinDecoder decoder;
	
	@Before
	public void before() {
		decoder = new LevenshteinDecoder();
	}
	
	@Test
	public void testDecodingPositive() throws Exception {
		decodeWithAssert("101", 1);
		decodeWithAssert("11111010101", 21);
	}
	
	@Test
	public void testDecodingNegative() throws Exception {
		decodeWithAssert("001", -1);
		decodeWithAssert("01111010101", -21);
	}
	
	private void decodeWithAssert(String value, int expect) throws Exception {
		StringBitInput in = new StringBitInput(value);
		int result = decoder.decodeNext(in);
		assertEquals(expect, result);
	}
}
