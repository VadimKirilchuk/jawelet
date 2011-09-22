package ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic;

import static ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithmeticCoderConstants.FIRST_QUARTER;
import static ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithmeticCoderConstants.HALF;
import static ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithmeticCoderConstants.THIRD_QUARTER;
import static ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithmeticCoderConstants.TOP_VALUE;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.Encoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;
import ru.ifmo.diplom.kirilchuk.coders.io.impl.BitOutputImpl;

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
 * @author Kirilchuk V.E.
 * @see ArithDecoder
 * @see BitOutputImpl
 */
public final class ArithEncoder implements Encoder {

	/**
	 * The model on which the output stream is based.
	 */
	private final ArithCodeModel model;

	/**
	 * Interval used for coding ranges.
	 */
	private final int[] interval = new int[3];

	/**
	 * Number of bits beyond first bit that were normalized.
	 */
	private int bitsToFollow;

	/**
	 * The low bound on the current interval for coding. Initialized to zero.
	 */
	private long low;

	/**
	 * The high bound on the current interval for coding. Initialized to top
	 * value possible.
	 */
	private long high = TOP_VALUE;

	public ArithEncoder(ArithCodeModel model) {
		this.model = model;
	}

	/**
	 * Close the arithmetic encoder, writing all bits that are buffered and
	 * closing the underlying output streams.
	 * 
	 * @throws IOException
	 *             If there is an exception writing to or closing the underlying
	 *             output stream.
	 */
	public void close(BitOutput out) throws IOException {
		++bitsToFollow; // need a final bit (not sure why)
		if (low < FIRST_QUARTER) {
			bitPlusFollowFalse(out);
		} else {
			bitPlusFollowTrue(out);
		}
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
	private void encode(int[] counts, BitOutput out) throws IOException {
		encode(counts[0], counts[1], counts[2], out);
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
	private void encode(int lowCount, int highCount, int totalCount, BitOutput out) throws IOException {
		long range = high - low + 1;
		high = low + (range * highCount) / totalCount - 1;
		low = low + (range * lowCount) / totalCount;
		while (true) {
			if (high < HALF) {
				bitPlusFollowFalse(out);
			} else if (low >= HALF) {
				bitPlusFollowTrue(out);
				low -= HALF;
				high -= HALF;
			} else if (low >= FIRST_QUARTER && high < THIRD_QUARTER) {
				++bitsToFollow;
				low -= FIRST_QUARTER;
				high -= FIRST_QUARTER;
			} else {
				return;
			}
			low <<= 1;
			high = (high << 1) + 1;
		}
	}

	/**
	 * Writes encoded symbol after necessary escapes to the underlying encoder.
	 * 
	 * @param symbol
	 *            Symbol to encode.
	 * @throws IOException
	 *             If the underlying encoder throws an IOException.
	 */
	public void encode(int symbol, BitOutput out) throws IOException {
		while (model.escaped(symbol)) {
			// have already done complete walk to compute escape
			model.interval(ArithCodeModel.ESCAPE, interval);
			encode(interval, out);
		}
		// have already done walk to element to compute escape
		model.interval(symbol, interval);
		encode(interval, out);
	}

	/**
	 * Write a <code>true</code> bit, and then a number of <code>false</code>
	 * bits equal to the number of bits to follow.
	 * 
	 * @throws IOException
	 *             If there is an exception writing a bit.
	 */
	private void bitPlusFollowTrue(BitOutput out) throws IOException {
		out.writeTrueBit();
		for (; bitsToFollow > 0; --bitsToFollow) {
			out.writeFalseBit();
		}
	}

	/**
	 * Write a <code>false</code> bit, and then a number of <code>true</code>
	 * bits equal to the number of bits to follow.
	 * 
	 * @throws IOException
	 *             If there is an exception writing a bit.
	 */
	private void bitPlusFollowFalse(BitOutput out) throws IOException {
		out.writeFalseBit();
		for (; bitsToFollow > 0; --bitsToFollow) {
			out.writeTrueBit();
		}
	}
}
