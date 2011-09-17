package ru.ifmo.diplom.kirilchuk.coders.impl.monotone;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.Encoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;


public class MonotoneEncoder implements Encoder {

	private static final int	BITS_IN_INTEGER	= 32;

	/**
	 * Encode values from 1 to Integer.MAX_VALUE using monotone code.
	 * Additionally supports encoding of negative values from -1 to Integer.MIN_VALUE + 1.
	 * Integer.MIN_VALUE and 0 are not supported. 
	 * 
	 * <p> Firstly output sign of value, then encodes absolute value as
	 * [unary coded binary representation length + binary representation of value].
	 * 
	 * <p> For example: for value 5 it will be
	 * <p> 1 for sign
	 * <p> 110 for unary code of binary length (3) 
	 * <p> 101 for binary representation of 5
	 * <p> 1110101 as result
	 * @param value integer to encode
	 * @param out destination for encoding results
	 * @throws IOException if any io errors occur.
	 */
	public void encode(int value, BitOutput out) throws IOException {
		if (value == 0 || value == Integer.MIN_VALUE) {
			throw new IllegalArgumentException("This coder does not support value: " + value);
		}
		
		// outputing sign of number
		if (value > 0) {
			out.writeTrueBit();
		} else {
			out.writeFalseBit();
			//inverting negative value
			//Note: for Integer.MIN_VALUE it have no effect
			value = -value;
		}
		
		/*
		 * Output length of following binary representation. Example: if we have
		 * number 00000000 0000000 0000000 01010101 then it`s short binary
		 * representation will be just 1010101 it`s length is 7 so we must
		 * output seven as unary code and then output value binary representation.
		 */
		int leadingZero = Integer.numberOfLeadingZeros(value);
		int shortBinaryLength = BITS_IN_INTEGER - leadingZero;
		outputUnary(shortBinaryLength, out);

		//output binary representation of value
		for (int i = shortBinaryLength - 1; i >= 0; --i) {
			out.writeBit(getBit(value, i));
		}
	}
	
	/**
	 * Output binary code length using unary code.
	 * Unary code: for any number n it outputs n-1 true bits and then
	 * one false bit.
	 * 
	 * For example: for number 21 it will output 11110 cause
	 * binary representation of 21 (10101) has length 5.
	 * 
	 * @param binaryCodeLength number to encode by unary code.
	 * @throws IOException if any io errors occur.
	 */
	private void outputUnary(int binaryCodeLength, BitOutput out) throws IOException {
		for (int i = 0; i < binaryCodeLength - 1; ++i) {
			out.writeTrueBit();
		}	
		out.writeFalseBit();
	}

	private static boolean getBit(int value, int pos) {				
		int shifted = value >>> pos;
		return (shifted & 1) == 1;
	}
}
