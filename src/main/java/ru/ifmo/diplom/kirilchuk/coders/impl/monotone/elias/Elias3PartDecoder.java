package ru.ifmo.diplom.kirilchuk.coders.impl.monotone.elias;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.Decoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;


public class Elias3PartDecoder implements Decoder {

	public int decodeNext(BitInput in) throws IOException {
		boolean isPositive = in.readBit();
		
		int secondPartLength = countBinaryLength(in) - 2;
		
		// Note: leading bit always 1 
		int shortBinaryLength = 1;
		for (int i = 0; i < secondPartLength; ++i) {
			shortBinaryLength <<= 1;
			if(in.readBit()) {
				shortBinaryLength |= 1;
			}
		}
		
		// Note: leading bit always 1
		int result = 1;
		for (int i = 0; i < shortBinaryLength; ++i) {
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
