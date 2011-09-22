package ru.ifmo.diplom.kirilchuk.coders.io.impl;

import java.io.OutputStream;
import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;

/**
 * Writes to an underlying output stream a bit at a time. A bit is can be input
 * as a boolean, with <code>true=1</code> and <code>false=0</code>, or as a
 * number, in which case any non-zero input will be converted to <code>1</code>.
 * If the number of bits written before closing the output does not land on a
 * byte boundary, the remaining fractional byte is filled with <code>0</code>
 * bits.
 * 
 * @author <a href="http://www.colloquial.com/carp/">Bob Carpenter</a>
 * @author Kirilchuk V.E.
 * @see BitInputImpl
 */
public class BitOutputImpl implements BitOutput {

	/**
	 * Buffering for output. Bytes are represented as integers, primarily for
	 * efficiency of bit fiddling and for compatibility with underlying output
	 * stream.
	 */
	private int nextByte;

	/**
	 * The indexof the next bit to write into the next byte.
	 */
	private int nextBitIndex;

	/**
	 * Underlying output stream.
	 */
	private final OutputStream out;

	/**
	 * Construct a bit output from the specified output stream.
	 * 
	 * @param out
	 *            Underlying output stream.
	 */
	public BitOutputImpl(OutputStream out) {
		this.out = out;
		reset();
	}

	public void close() throws IOException {
		if (nextBitIndex < 7) {// there's something in the buffer
			out.write(nextByte << nextBitIndex); // shift to fill last byte
//			System.out.println("Writed: " + (byte)(nextByte << nextBitIndex));
		}
		out.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.colloquial.arithcode.io.BitOutput#flush()
	 */
	public void flush() throws IOException {
		out.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.colloquial.arithcode.io.BitOutput#writeBit(boolean)
	 */
	public void writeBit(boolean bit) throws IOException {
		if (bit) {
			writeTrueBit();
		} else {
			writeFalseBit();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.colloquial.arithcode.io.BitOutput#writeBitTrue()
	 */
	public void writeTrueBit() throws IOException {
		if (nextBitIndex == 0) {
			out.write(nextByte + 1);
//			System.out.println("Writed: " + (byte)((nextByte + 1) & 0x00FF));
			reset();
		} else {
			nextByte = (nextByte + 1) << 1;
			--nextBitIndex;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.colloquial.arithcode.io.BitOutput#writeBitFalse()
	 */
	public void writeFalseBit() throws IOException {
		if (nextBitIndex == 0) {
			out.write(nextByte);
//			System.out.println("Writed: " + (byte)((nextByte) & 0x00FF));
			reset();
		} else {
			nextByte <<= 1;
			--nextBitIndex;
		}
	}

	/**
	 * Resets the bit buffer.
	 */
	private void reset() {
		nextByte = 0;
		nextBitIndex = 7;
	}
}
