package ru.ifmo.diplom.kirilchuk.coding;

import java.util.List;

import ru.ifmo.diplom.kirilchuk.coding.util.Outputer;

/**
 * @deprecated use arithmetic coders stuff from other package
 * @author Kirilchuk V.E.
 */
public class ArithmeticCoder {
	private static class CoderConstants {
		/* THE SET OF SYMBOLS THAT MAY BE ENCODED. */

		/** Number of character symbols */
		public static final int NUM_OF_CHARS = 256;
		/** Index of EOF symbol */
		public static final int EOF_INDEX = NUM_OF_CHARS + 1;

		/* SIZE OF ARITHMETIC CODE VALUES. */

		/** Number of bits in a code value */
		public static final int CODE_VALUE_BITS = 16;
		// typedef long code_value; /* Type of an arithmetic code value */

		/** Largest code value */
		public static final long TOP_VALUE = (((long) 1 << CODE_VALUE_BITS) - 1);

		/* HALF AND QUARTER POINTS IN THE CODE VALUE RANGE. */

		/** Point after first quarter */
		public static final long FIRST_QUATER = (TOP_VALUE / 4 + 1);
		/** Point after first half */
		public static final long HALF = (2 * FIRST_QUATER);
		/** Point after first third quater */
		public static final long THIRD_QUATER = (3 * FIRST_QUATER);
	}
	
	
	private AdaptiveModel model;
	
	private long low;
	private long high;

	private long bits_to_follow;
	private Outputer bitHandler;	

	public void startEncoding() {
		low = 0;
		high = CoderConstants.TOP_VALUE;
		bits_to_follow = 0;
		bitHandler = new Outputer();
		bitHandler.initOutput();
		model = new AdaptiveModel();
		model.initModel();
	}

	private void encodeSymbol(int symbol, int[] cum_freq) {
		long range = (long) (high - low) + 1; /* Size of the current code region */

		/*
		 * Narrow the code region to that allotted to this symbol
		 */
		high = low + (range * cum_freq[symbol - 1]) / cum_freq[0] - 1;
		low = low + (range * cum_freq[symbol]) / cum_freq[0];

		/* Loop to output bits. */
		while (true) {
			if (high < CoderConstants.HALF) {
				/* Output 0 if in low half. */
				bits_to_follow = bitHandler.bit_plus_follow(0, bits_to_follow);
			} else if (low >= CoderConstants.HALF) {
				/* Output 1 if in high half. */
				bits_to_follow = bitHandler.bit_plus_follow(1, bits_to_follow);

				/* Subtract offset to top. */
				low -= CoderConstants.HALF;
				high -= CoderConstants.HALF;
			} else if (low >= CoderConstants.FIRST_QUATER && high < CoderConstants.THIRD_QUATER) {
				/* Output an opposite bit */
				/* later if in middle half. */
				bits_to_follow += 1;

				/* Subtract offset to middle */
				low -= CoderConstants.FIRST_QUATER;
				high -= CoderConstants.FIRST_QUATER;
			} else {
				/* Otherwise exit loop. */
				break;
			}
			
			/* Scale up code range. */
			low = 2 * low;
			high = 2 * high + 1;
		}
	}

	public void doneEncoding() {
		/*
		 * Output two bits that select the quarter that the current code range
		 * contains.
		 */
		bits_to_follow += 1;
		if (low < CoderConstants.FIRST_QUATER) {
			bits_to_follow = bitHandler.bit_plus_follow(0, bits_to_follow);
		} else {
			bits_to_follow = bitHandler.bit_plus_follow(1, bits_to_follow);
		}
	}
	
	public List<Character> stopOutputingBits() {
		return bitHandler.stopOutputingBits();
	}

	public void encodeCharacter(int ch) {
		int symbol = model.getIndex(ch);
		encodeSymbol(symbol, model.getCumulativeFreq());
		model.updateModel(symbol);			
	}

	public void encodeEOF() {		
		encodeSymbol(CoderConstants.EOF_INDEX, model.getCumulativeFreq());
	}
}
