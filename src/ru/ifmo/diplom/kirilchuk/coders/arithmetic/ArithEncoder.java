package ru.ifmo.diplom.kirilchuk.coders.arithmetic;

import java.io.IOException;
import java.io.OutputStream;

import ru.ifmo.diplom.kirilchuk.coding.io.BitOutput;
import ru.ifmo.diplom.kirilchuk.coding.io.BitOutputImpl;


import static ru.ifmo.diplom.kirilchuk.coders.arithmetic.ArithmeticCoderConstants.*;
/**
 * <P>
 * Performs arithmetic encoding, converting cumulative probability interval
 * input into bit output. Cumulative probability intervals are given as integer
 * counts <code>low</code>, <code>high</code> and <code>total</code>, with the
 * range being <code>[low/total,high/total)</code>.
 * 
 * <P>
 * For more details, see <a href="../../../tutorial.html">The Arithemtic Coding
 * Tutorial</a>.
 * 
 * @author <a href="http://www.colloquial.com/carp/">Bob Carpenter</a>
 * @version 1.1
 * @see ArithDecoder
 * @see BitOutputImpl
 * @since 1.0
 */
public final class ArithEncoder {
	
	/**
	 * Bit output stream for writing encoding bits.
	 */
	private final BitOutput	_out;

	/**
	 * Number of bits beyond first bit that were normalized.
	 */
	private int				_bitsToFollow;
	
	/**
	 * The low bound on the current interval for coding. Initialized to zero.
	 */
	private long			_low;

	/**
	 * The high bound on the current interval for coding. Initialized to top
	 * value possible.
	 */
	private long			_high			= TOP_VALUE;
	
	/**
	 * Construct an arithmetic coder from a bit output.
	 * 
	 * @param out
	 *            Underlying bit output.
	 * @since 1.1
	 */
	public ArithEncoder(BitOutput out) {
		_out = out;
	}

	/**
	 * Construct an arithmetic coder from an output stream.
	 * 
	 * @param out
	 *            Underlying output stream.
	 */
	public ArithEncoder(OutputStream out) {
		this(new BitOutputImpl(out));
	}

	/**
	 * Close the arithmetic encoder, writing all bits that are buffered and
	 * closing the underlying output streams.
	 * 
	 * @throws IOException
	 *             If there is an exception writing to or closing the underlying
	 *             output stream.
	 */
	public void close() throws IOException {
		++_bitsToFollow; // need a final bit (not sure why)
		if (_low < FIRST_QUARTER) {
			bitPlusFollowFalse();
		} else {
			bitPlusFollowTrue();
		}
		_out.close();
	}

	/**
	 * Flushes bit output.
	 * 
	 * @throws IOException
	 *             If there is an exception flushing the underlying output
	 *             stream.
	 */
	public void flush() throws IOException {
		_out.flush();
	}

	/**
	 * Encodes an interval expressed as a low count, high count and total count
	 * in an array <code>{low,high,total}</code>.
	 * 
	 * @param counts
	 *            Low, high and total counts of symbols.
	 * @see #encode(int,int,int)
	 * @throws IOException
	 *             If there is an exception writing to the underlying stream.
	 */
	public void encode(int[] counts) throws IOException {
		encode(counts[0], counts[1], counts[2]);
	}

	/**
	 * Encodes an interval expressed as a low count, high count and total count.
	 * The high count is taken to be exclusive, and the resulting range is
	 * <code>highCount - lowCount + 1</code>.
	 * 
	 * @param lowCount
	 *            Cumulative count of symbols below current one.
	 * @param highCount
	 *            Cumulative count of symbols below current one plus currnet
	 *            one.
	 * @param totalCount
	 *            Cumulative count of all symbols.
	 * @throws IOException
	 *             If there is an exception writing to the underlying stream.
	 * @see #encode(int[])
	 */
	public void encode(int lowCount, int highCount, int totalCount) throws IOException {
		long range = _high - _low + 1;
		_high = _low + (range * highCount) / totalCount - 1;
		_low = _low + (range * lowCount) / totalCount;
		while (true) {
			if (_high < HALF) {
				bitPlusFollowFalse();
			} else if (_low >= HALF) {
				bitPlusFollowTrue();
				_low -= HALF;
				_high -= HALF;
			} else if (_low >= FIRST_QUARTER && _high < THIRD_QUARTER) {
				++_bitsToFollow;
				_low -= FIRST_QUARTER;
				_high -= FIRST_QUARTER;
			} else {
				return;
			}
			_low <<= 1;
			_high = (_high << 1) + 1;
		}
	}	

	/**
	 * Write a <code>true</code> bit, and then a number of <code>false</code>
	 * bits equal to the number of bits to follow.
	 * 
	 * @throws IOException
	 *             If there is an exception writing a bit.
	 * @since 1.1
	 */
	private void bitPlusFollowTrue() throws IOException {
		for (_out.writeTrueBit(); _bitsToFollow > 0; --_bitsToFollow) {
			_out.writeFalseBit();
		}
	}

	/**
	 * Write a <code>false</code> bit, and then a number of <code>true</code>
	 * bits equal to the number of bits to follow.
	 * 
	 * @throws IOException
	 *             If there is an exception writing a bit.
	 * @since 1.1
	 */
	private void bitPlusFollowFalse() throws IOException {
		for (_out.writeFalseBit(); _bitsToFollow > 0; --_bitsToFollow) {
			_out.writeTrueBit();
		}
	}
}
