package ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic;

import static ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithmeticCoderConstants.CODE_VALUE_BITS;
import static ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithmeticCoderConstants.FIRST_QUARTER;
import static ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithmeticCoderConstants.HALF;
import static ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithmeticCoderConstants.THIRD_QUARTER;
import static ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithmeticCoderConstants.TOP_VALUE;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.ifmo.diplom.kirilchuk.coders.Decoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;
import ru.ifmo.diplom.kirilchuk.coders.io.impl.BitInputImpl;

/**
 * <P>
 * Performs arithmetic decoding, converting bit input into cumulative
 * probability interval output. Returns probabilities as integer counts
 * <code>low</code>, <code>high</code> and <code>total</code>, with the range
 * being <code>[low/total,high/total)</code>.
 * 
 * <P>
 * For more details, see <a href="../../../tutorial.html">The Arithemtic Coding
 * Tutorial</a>.
 * 
 * @author <a href="http://www.colloquial.com/carp/">Bob Carpenter</a>
 * @author Kirilchuk V.E.
 * @see ArithEncoder
 * @see BitInputImpl
 */
public final class ArithDecoder implements Decoder {
	private static final Logger LOG = LoggerFactory.getLogger(ArithDecoder.class);
	
	/**
	 * The statistical model on which the decoder is based.
	 */
	private final ArithCodeModel _model;

	/**
	 * The buffered next byte. If it's equal to -1, the end of stream has been
	 * reached, otherwise next byte is the low order bits.
	 */
	private int _nextByte;

	/**
	 * Interval used for coding ranges.
	 */
	private final int[] _interval = new int[3];

	/**
	 * Current bits for decoding.
	 */
	private long _value;

	/**
	 * The low bound on the current interval for coding. Initialized to zero.
	 */
	private long _low;

	/**
	 * The high bound on the current interval for coding. Initialized to top
	 * value possible.
	 */
	private long _high = TOP_VALUE;

	/**
	 * Value will be <code>true</code> if the end of stream has been reached.
	 */
	private boolean _endOfStream = false;

	/**
	 * Number of bits that have been buffered.
	 */
	private int _bufferedBits;

	private boolean initialized = false;

	public ArithDecoder(ArithCodeModel model) {
		this._model = model;
	}

	/**
	 * Returns <code>true</code> if the end of stream has been reached and there
	 * are no more symbols to decode.
	 * 
	 * @return <code>true</code> if the end of stream has been reached.
	 */
	public boolean endOfStream() {
		return _endOfStream;
	}

	/**
	 * Returns a count for the current symbol that will be between the low and
	 * high counts for the symbol in the model given the total count. Once
	 * symbol is retrieved, the model is used to compute the actual low, high
	 * and total counts and {@link #removeSymbolFromStream} is called.
	 * 
	 * @param totalCount
	 *            The current total count for the model.
	 * @return A count that is in the range above or equal to the low count and
	 *         less than the high count of the next symbol decoded.
	 */
	public int getCurrentSymbolCount(int totalCount) {
		return (int) (((_value - _low + 1) * totalCount - 1) / (_high - _low + 1));
	}

	/**
	 * Removes a symbol from the input stream that was coded with counts
	 * <code>{ low, high, total }</code>. Called after
	 * {@link #getCurrentSymbolCount}.
	 * 
	 * @param counts
	 *            Array of low, high and total count used to code the symbol.
	 * @throws IOException
	 *             If there is an exception in buffering input from the
	 *             underlying input stream.
	 * @see #removeSymbolFromStream(long,long,long)
	 */
	public void removeSymbolFromStream(int[] counts, BitInput in) throws IOException {
		removeSymbolFromStream(counts[0], counts[1], counts[2], in);
	}

	/**
	 * Removes a symbol from the input stream. Called after
	 * {@link #getCurrentSymbolCount}.
	 * 
	 * @param lowCount
	 *            Cumulative count for symbols indexed below symbol to be
	 *            removed.
	 * @param highCount
	 *            <code>lowCount</code> plus count for this symbol.
	 * @param totalCount
	 *            Total count for all symbols seen.
	 * @throws IOException
	 *             If there is an exception in buffering input from the
	 *             underlying input stream.
	 */
	public void removeSymbolFromStream(long lowCount, long highCount, long totalCount, BitInput in) throws IOException {
		long range = _high - _low + 1;
		_high = _low + (range * highCount) / totalCount - 1;
		_low = _low + (range * lowCount) / totalCount;
		while (true) {
			if (_high < HALF) {
				// no effect
			} else if (_low >= HALF) {
				_value -= HALF;
				_low -= HALF;
				_high -= HALF;
			} else if (_low >= FIRST_QUARTER && _high <= THIRD_QUARTER) {
				_value -= FIRST_QUARTER;
				_low -= FIRST_QUARTER;
				_high -= FIRST_QUARTER;
			} else {
				return;
			}
			_low <<= 1;
			_high = (_high << 1) + 1;
			bufferBit(in);
		}
	}

	/**
	 * Reads a bit from the underlying bit input stream and buffers it.
	 * 
	 * @throws IOException
	 *             If there is an <code>IOException</code> buffering from the
	 *             underlying bit stream.
	 */
	private void bufferBit(BitInput _in) throws IOException {
		if (_in.endOfStream()) {
			if (_bufferedBits == 0) {
				_endOfStream = true;
				return;
			}
			_value <<= 1;
			--_bufferedBits;
		} else {
			_value = (_value << 1);
			if (_in.readBit()) {
				++_value;
			}
		}
	}

	/**
	 * Buffers the next byte into <code>_nextByte</code>.
	 */
	private void decodeNextByte(BitInput in) throws IOException {
		LOG.debug("Decoding next byte from input. Current cached byte: {}", _nextByte);
		if (_nextByte == ArithCodeModel.EOF) {
			//if previous byte was EOF then return 
			return;
		}
		
		if (endOfStream()) {
			//if we reach end of stream return and set next byte to EOF
			_nextByte = ArithCodeModel.EOF;
			return;
		}
		
		while (true) {
			_nextByte = _model.pointToSymbol(getCurrentSymbolCount(_model.totalCount()));
			_model.interval(_nextByte, _interval);
			removeSymbolFromStream(_interval, in);
			//if _nextByte is ESCAPE then continue, otherwise return
			if (_nextByte != ArithCodeModel.ESCAPE) {				
				return;
			}
		}
	}

	@Override
	public int decodeNext(BitInput in) throws IOException {
		if (!initialized) {
			for (int i = 1; i <= CODE_VALUE_BITS; ++i) {
				bufferBit(in);
				++_bufferedBits;
			}
			decodeNextByte(in);
			initialized = true;
		}
//		System.out.println("VALUE: " + _value);
		int result = _nextByte;
		decodeNextByte(in);

		return result;
	}

}
