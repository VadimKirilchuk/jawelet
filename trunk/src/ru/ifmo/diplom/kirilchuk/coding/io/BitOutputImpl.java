package ru.ifmo.diplom.kirilchuk.coding.io;

import java.io.OutputStream;
import java.io.IOException;


/**
 * Writes to an underlying output stream a bit at a time. A bit is can be input
 * as a boolean, with <code>true=1</code> and <code>false=0</code>, or as a
 * number, in which case any non-zero input will be converted to <code>1</code>.
 * If the number of bits written before closing the output does not land on a
 * byte boundary, the remaining fractional byte is filled with <code>0</code>
 * bits.
 * 
 * @author <a href="http://www.colloquial.com/carp/">Bob Carpenter</a>
 * @version 1.1
 * @see BitInputImpl
 * @since 1.0
 */
public class BitOutputImpl implements BitOutput {
	
	/**
	 * Buffering for output. Bytes are represented as integers, primarily for
	 * efficiency of bit fiddling and for compatibility with underlying output
	 * stream.
	 */
	private int					_nextByte;

	/**
	 * The indexof the next bit to write into the next byte.
	 */
	private int					_nextBitIndex;

	/**
	 * Underlying output stream.
	 */
	private final OutputStream	_out;

	/**
	 * Construct a bit output from the specified output stream.
	 * 
	 * @param out
	 *            Underlying output stream.
	 */
	public BitOutputImpl(OutputStream out) {
		_out = out;
		reset();
	}

	/* (non-Javadoc)
	 * @see com.colloquial.arithcode.io.BitOutput#close()
	 */
	public void close() throws IOException {
		if (_nextBitIndex < 7) {// there's something in the buffer
			_out.write(_nextByte << _nextBitIndex); // shift to fill last byte
		}
		_out.close();
	}

	/* (non-Javadoc)
	 * @see com.colloquial.arithcode.io.BitOutput#flush()
	 */
	public void flush() throws IOException {
		_out.flush();
	}

	/* (non-Javadoc)
	 * @see com.colloquial.arithcode.io.BitOutput#writeBit(boolean)
	 */
	public void writeBit(boolean bit) throws IOException {
		if (bit) {
			writeTrueBit();
		} else {
			writeFalseBit();
		}
	}

	/* (non-Javadoc)
	 * @see com.colloquial.arithcode.io.BitOutput#writeBitTrue()
	 */
	public void writeTrueBit() throws IOException {
		if (_nextBitIndex == 0) {
			_out.write(_nextByte + 1);
			reset();
		} else {
			_nextByte = (_nextByte + 1) << 1;
			--_nextBitIndex;
		}
	}

	/* (non-Javadoc)
	 * @see com.colloquial.arithcode.io.BitOutput#writeBitFalse()
	 */
	public void writeFalseBit() throws IOException {
		if (_nextBitIndex == 0) {
			_out.write(_nextByte);
			reset();
		} else {
			_nextByte <<= 1;
			--_nextBitIndex;
		}
	}
	
	/**
	 * Resets the bit buffer.
	 */
	private void reset() {
		_nextByte = 0;
		_nextBitIndex = 7;
	}
}
