package ru.ifmo.diplom.kirilchuk.coders;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithDecoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.levenshtein.LevenshteinDecoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;

/**
 * Common interface for decoders.
 * 
 * <p>
 * <pre>Known implementations:
 *   {@link LevenshteinDecoder}
 *   {@link ArithDecoder}
 * </pre>
 * @see Encoder
 * @author Kirilchuk V.E.
 */
public interface Decoder {
	/**
	 * Decodes next integer from input. 
	 * 
	 * @param in input
	 * @return decoded integer
	 * @throws IOException if io errors occur during read from input 
	 */
	int decodeNext(BitInput in) throws IOException;
}
