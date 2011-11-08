package ru.ifmo.diplom.kirilchuk.coders.impl.monotone.elias;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.Encoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;

/**
 * elias(1)=0
 * elias(21) = (1110)(00)(0101) = 1110000101
 * 
 * elias(i)=(unar( |bin'(|bin'(i)|)|+2 ) , bin'(|bin'(i)|), bin'(i) )
 * 
 * @author Kirilchuk V.E.
 */
public class Elias3PartEncoder implements Encoder {

	private static final int	BITS_IN_INTEGER	= 32;

	//TODO: not works for +-1
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
		
		// counting length of short binary representation of value
		// Why short? Because it is without first 1
		int leadingZero = Integer.numberOfLeadingZeros(value);
		int shortBinaryLength = BITS_IN_INTEGER - leadingZero - 1;
		
		// now we need length of second part:
		// of binary representation of short binary length of value
		int leadingZeroOfBinaryLength = Integer.numberOfLeadingZeros(shortBinaryLength);
		int secondPartLength = BITS_IN_INTEGER - leadingZeroOfBinaryLength - 1;
		
		// now we need last part:
		// unary code of (second part length + 2)
		// and we can start output of (third part) (second part) (short binary of value)
		outputUnary(secondPartLength + 2, out);
		
		// output short binary representation of (length of first part)
		for (int i = secondPartLength - 1; i >= 0; --i) {
			out.writeBit(getBit(shortBinaryLength, i));
		}
		
		// output short binary representation of value
		for (int i = shortBinaryLength - 1; i >= 0; --i) {
			out.writeBit(getBit(value, i));
		}
	}
	
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
