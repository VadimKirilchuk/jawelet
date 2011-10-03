package ru.ifmo.diplom.kirilchuk.coders.impl.fusion;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.Decoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithDecoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;

/**
 * Fusion decoder. Decodes next symbol from one of specified inputs.
 * 
 * @see FusionEncoder
 * @author Kirilchuk V.E.
 */
public class FusionDecoder {
	
	private ArithDecoder arithmeticDecoder;
	private Decoder monotoneDecoder;

	/**
	 * Constructs decoder with specified arithmetic and monotone decoders. 
	 * 
	 * @param arithmeticDecoder arithmetic decoder
	 * @param monotoneDecoder monotone decoder
	 */
	public FusionDecoder(ArithDecoder arithmeticDecoder, Decoder monotoneDecoder) {
		this.arithmeticDecoder = arithmeticDecoder;
		this.monotoneDecoder = monotoneDecoder;
	}

	/**
	 * Decodes next integer from specified bit input. Uses monotone decoder
	 * if parameter useMonotone is true, uses arithmetic decoder otherwise.
	 * 
	 * @param in input to get bits from
	 * @param useMonotone flag to use monotone decoder
	 * @return next integer from input
	 * @throws IOException if any io errors occur
	 */
	public int decodeNext(BitInput in, boolean useMonotone) throws IOException {
		if (!useMonotone) {
			return arithmeticDecoder.decodeNext(in);
		} else {
			return monotoneDecoder.decodeNext(in);
		}
	}
}
