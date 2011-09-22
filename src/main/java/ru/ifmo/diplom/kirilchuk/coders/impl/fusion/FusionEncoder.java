package ru.ifmo.diplom.kirilchuk.coders.impl.fusion;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.ifmo.diplom.kirilchuk.coders.Encoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithCodeModel;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithEncoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;

/**
 * Facade encoder that combines arithmetic and monotone code to produce encoder
 * that can encode all values in integer range.
 * 
 * @author Kirilchuk V.E.
 */
public class FusionEncoder implements Encoder {
	
	private ArithEncoder arithmeticEncoder;
	private Encoder monotoneEncoder;

	/**
	 * Arithmetic encoder will be used for encoding values from 0 to 254, while
	 * monotone encoder will encode other values to cover range from
	 * Integer.MIN_VALUE+1 to Integer.MAX_VALUE
	 * 
	 * @param arithmeticEncoder
	 *            arithmetic encoder
	 * @param monotoneEncoder
	 *            monotome encoder
	 */
	public FusionEncoder(ArithEncoder arithmeticEncoder, Encoder monotoneEncoder) {
		this.arithmeticEncoder = arithmeticEncoder;
		this.monotoneEncoder = monotoneEncoder;
	}

	/**
	 * Encodes next value. If value is inside [0, 254] range, then arithmetic
	 * encoding is used, otherwise 255 will be encoded by arithmetic encoding
	 * and then actual value will be encoded by monotone encoding. For example,
	 * for value 300, firstly - 255 will be encoded by arithmetic encoding and
	 * then 300 by monotone encoding.
	 */
	public void encode(int value, BitOutput out) throws IOException {		
		if (value >= 0 && value <= 254) { // encode value by arithmetic coder
			arithmeticEncoder.encode(value, out);
		} else {
			// escape to switch fusion decoder to monotone decoder
			arithmeticEncoder.encode(FusionCoderConstants.ESCAPE_TO_SWITCH_TO_MONOTONE_CODE, out);
//			System.out.println("Encoded escape: " + FusionCoderConstants.ESCAPE_TO_SWITCH_TO_MONOTONE_CODE);
			monotoneEncoder.encode(value, out);
		}
//		System.out.println("Encoded: " + value);
	}

	/**
	 * Close this output stream.
	 * 
	 * @throws IOException
	 *             If there is an exception in the underlying encoder.
	 */
	public void close(BitOutput out) throws IOException {
		// must code EOF to allow decoding to halt
		arithmeticEncoder.encode(ArithCodeModel.EOF, out);
		// to allow arithmetic encoder to output buffered bits
		arithmeticEncoder.close(out);
	}
}
