package ru.ifmo.diplom.kirilchuk.coding;

/**
 * @deprecated use arithmetic coders stuff from other package
 * @author Kirilchuk V.E.
 */
public class AdaptiveModel {
	/* THE SET OF SYMBOLS THAT MAY BE ENCODED. */

	/** Number of character symbols */
	private static final int NUM_OF_CHARS = 256;

	/** Total number of symbols */
	private static final int NUM_OF_SYMBOLS = NUM_OF_CHARS + 1;

	/** Maximum allowed frequency count */
	private static final int MAX_FREQUENCY = 16383;

	/* TRANSLATION TABLES BETWEEN CHARACTERS AND SYMBOL INDEXES. */
	
	/** To index from character */
	private int[] characterToIndex = new int[NUM_OF_CHARS];
	/** To character from index */
	private int[] indexToCharacter = new int[NUM_OF_SYMBOLS + 1];

	/* FREQUENCY TABLES. */

	/* Symbol frequencies */
	private int[] frequency = new int[NUM_OF_SYMBOLS + 1];
	/* Cumulative symbol frequencies */
	private int[] cumulativeFrequency = new int[NUM_OF_SYMBOLS + 1];

	public void initModel() {
		/*
		 * Set up tables that translate between symbol indexes and characters
		 */
		for (int i = 0; i < NUM_OF_CHARS; ++i) {
			characterToIndex[i] = i + 1;
			indexToCharacter[i + 1] = i;
		}
		/*
		 * Set up initial frequency count to be one for all symbols
		 */
		for (int i = 0; i < NUM_OF_SYMBOLS; ++i) {
			frequency[i] = 1;
			cumulativeFrequency[i] = NUM_OF_SYMBOLS - i;
		}

		/* freq[0] must not be the same as freq[1] */
		frequency[0] = 0;
	}

	public void updateModel(int symbol) {
		/*
		 * See if frequency counts are at their maximum. If so, halve all the
		 * counts (keeping them non-zero)
		 */
		if (cumulativeFrequency[0] == MAX_FREQUENCY) {
			int cum = 0;
			for (int i = NUM_OF_SYMBOLS; i >= 0; i--) {
				frequency[i] = (frequency[i] + 1) / 2;
				cumulativeFrequency[i] = cum;
				cum += frequency[i];
			}
		}

		/* New index for symbol */
		int i;

		/* Find symbol's new index. */
		for (i = symbol; frequency[i] == frequency[i - 1]; i--) {
			;
		}

		if (i < symbol) {
			int ch_i, ch_symbol;
			ch_i = indexToCharacter[i]; /* Update the translation */
			ch_symbol = indexToCharacter[symbol]; /* tables if the symbol has */
			indexToCharacter[i] = ch_symbol; /* moved. */
			indexToCharacter[symbol] = ch_i;
			characterToIndex[ch_i] = symbol;
			characterToIndex[ch_symbol] = i;
		}

		frequency[i] += 1; /* Increment the frequency */
		while (i > 0) { /* count for the symbol and */
			i -= 1; /* update the cumulative */
			cumulativeFrequency[i] += 1; /* frequencies. */
		}
	}
	
	public int getIndex(int character) {
		return characterToIndex[character];
	}
	
	public int getChar(int index) {
		return indexToCharacter[index];
	}

	public int[] getCumulativeFreq() {
		return cumulativeFrequency;
	}
}
