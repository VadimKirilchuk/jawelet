package ru.ifmo.diplom.kirilchuk.coding;

import java.util.List;

import ru.ifmo.diplom.kirilchuk.coding.util.Inputer;
import ru.ifmo.diplom.kirilchuk.coding.util.Outputer;

/**
 * @deprecated use arithmetic coders stuff from other package
 * @author Kirilchuk V.E.
 */
public class ArithmeticDecoder {
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

	private long Top_value = (((long) 1 << 16) - 1);

	private AdaptiveModel model;

	private long value;
	private long low;
	private long high;

	private long bits_to_follow;

	private Inputer bitHandler;

	public void startDecoding(List<Character> in) {
		bitHandler = new Inputer(in);

		model = new AdaptiveModel();
		model.initModel();

		int i;
		value = 0; /* Input bits to fill the */
		for (i = 1; i <= 16; i++) { /* code value. */
			value = 2 * value + bitHandler.input_bit();
		}

		/* Full code range. */
		low = 0;
		high = Top_value;
	}

	/**
	 * @return symbol
	 */
	public int decodeSymbol() {
		long range; /* Size of current code region */
		int cum; /* Cumulative frequency calculated */
		int symbol; /* Symbol decoded */
		range = (long) (high - low) + 1;

		/*
		 * Find cum freq for value.
		 */
		cum = (int) ((((long) (value - low) + 1) * model.getCumulativeFreq()[0] - 1) / range);

		for (symbol = 1; model.getCumulativeFreq()[symbol] > cum; symbol++) {
			; /* Then find symbol. */
		}
		/*
		 * Narrow the code region to that allotted to this symbol.
		 */
		high = low + (range * model.getCumulativeFreq()[symbol - 1]) / model.getCumulativeFreq()[0] - 1;
		low = low + (range * model.getCumulativeFreq()[symbol]) / model.getCumulativeFreq()[0];

		for (;;) { /* Loop to get rid of bits. */
			if (high < CoderConstants.HALF) {
				/* nothing *//* Expand low half. */
			} else if (low >= CoderConstants.HALF) { /* Expand high half. */
				value -= CoderConstants.HALF;
				/* Subtract offset to top. */
				low -= CoderConstants.HALF;
				high -= CoderConstants.HALF;
			} else if (low >= CoderConstants.FIRST_QUATER && high < CoderConstants.THIRD_QUATER) {
				/*
				 * Expand middle half.
				 */

				value -= CoderConstants.FIRST_QUATER;
				/* Subtract offset to middle */
				low -= CoderConstants.FIRST_QUATER;
				high -= CoderConstants.FIRST_QUATER;
			} else
				break; /* Otherwise exit loop. */
			low = 2 * low;
			high = 2 * high + 1; /* Scale up code range. */
			/*
			 * Move in next input bit.
			 */
			value = 2 * value + bitHandler.input_bit();
		}
		return symbol;
	}
	
	public int getChar(int index) {
		return model.getChar(index);
	}

	public void updateModel(int decodedSymbol) {
		model.updateModel(decodedSymbol);
	}
}
