package ru.ifmo.diplom.kirilchuk.coders;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithEncoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.fusion.FusionEncoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.MonotoneEncoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;

/**
 * Common interface for encoders.
 * 
 * <p>
 * <pre>Known implementations:
 *   {@link MonotoneEncoder}
 *   {@link ArithEncoder}
 * </pre>
 * @see Decoder
 * @author Kirilchuk V.E.
 */
public interface Encoder {

	/**
	 * Encodes value and outputs result to output 
	 * 
	 * @param value integer to encode
	 * @param out output
	 * @throws IOException if io errors occur during sending result to output
	 */
	void encode(int value, BitOutput out) throws IOException;
}
