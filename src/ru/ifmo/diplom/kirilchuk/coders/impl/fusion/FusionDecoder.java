package ru.ifmo.diplom.kirilchuk.coders.impl.fusion;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ru.ifmo.diplom.kirilchuk.coders.Decoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithDecoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;

public class FusionDecoder implements Decoder {
	
	private ArithDecoder arithmeticDecoder;
	private Decoder monotoneDecoder;

	public FusionDecoder(ArithDecoder arithmeticDecoder, Decoder monotoneDecoder) {
		this.arithmeticDecoder = arithmeticDecoder;
		this.monotoneDecoder = monotoneDecoder;
	}

	public int decodeNext(BitInput in) throws IOException {
		//first decode using arithmetic decoding
		int result = arithmeticDecoder.decodeNext(in);		
//		if (result == FusionCoderConstants.ESCAPE_TO_SWITCH_TO_MONOTONE_CODE) {
			// if we have escape then current value is monotone encoded
//			result = monotoneDecoder.decodeNext(in);
//		} 
//		System.out.println("Decoded: " + result);
		
		return result;
	}
}
