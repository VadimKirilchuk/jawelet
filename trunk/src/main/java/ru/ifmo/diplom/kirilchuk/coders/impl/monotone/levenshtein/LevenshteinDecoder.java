package ru.ifmo.diplom.kirilchuk.coders.impl.monotone.levenshtein;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.Decoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;


public class LevenshteinDecoder implements Decoder {

	public int decodeNext(BitInput in) throws IOException {
		boolean isPositive = in.readBit();
		
		int binaryLength = countBinaryLength(in);
		
		int result = 0;
		for (int i = 0; i < binaryLength; ++i) {
			result <<= 1;
			if(in.readBit()) {
				result |= 1;
			}
		}
		
		return (isPositive ? result : -result);
	}

	private int countBinaryLength(BitInput in) throws IOException {
		int length = 1;//even if no true bits we still must have one bit in binary representation.
		while (in.readBit()) {
			++length;
		}
		
		return length;
	}	
}
