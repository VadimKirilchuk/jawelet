package ru.ifmo.diplom.kirilchuk.coders.io.impl;

import java.io.InputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;

/**
 * Reads input from an underlying input stream a bit at a time. Bits are
 * returned as booleans, with <code>true=1</code> and <code>false=0</code>.
 * 
 * @see ru.ifmo.diplom.kirilchuk.coders.io.impl.BitOutputImpl
 * 
 * @author <a href="http://www.colloquial.com/carp/">Bob Carpenter</a>
 * @version 1.1
 * @see BitOutputImpl
 * @since 1.0
 */
public final class BitInputImpl implements BitInput {
	private static final Logger LOG = LoggerFactory.getLogger(BitInputImpl.class);
	
	
	/**
	 * Underlying input stream.
	 */
	private final InputStream _in;

	/**
	 * Buffered byte from which bits are read.
	 */
	private int _nextByte; // implied = 0;

	/**
	 * Position of next bit in the buffered byte.
	 */
	private int _nextBitIndex;

	/**
	 * Set to true when all bits have been read.
	 */
	private boolean _endOfStream = false;

	/**
	 * Constructs a bit input from an underlying input stream.
	 * 
	 * @param in
	 *            Input stream backing this bit input.
	 * @throws IOException
	 *             If there is an exception reading from the specified input
	 *             stream.
	 */
	public BitInputImpl(InputStream in) throws IOException {
		_in = in;
		readAhead();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.colloquial.arithcode.io.BitInput#available()
	 */
	public long available() throws IOException {
		return endOfStream() ? 0 : 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.colloquial.arithcode.io.BitInput#close()
	 */
	public void close() throws IOException {
		_in.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.colloquial.arithcode.io.BitInput#endOfStream()
	 */
	public boolean endOfStream() {
		return _endOfStream;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.colloquial.arithcode.io.BitInput#readBit()
	 */
	public boolean readBit() throws IOException {
		if (_nextBitIndex > 0) {
			boolean result = ((_nextByte & (1 << _nextBitIndex--)) != 0);
			
			LOG.debug("Read bit: {}", result ? "1" : "0");
			return result;
		}

		// taking last bit from buffered byte
		boolean result = ((_nextByte & 1) != 0);

		// buffering new byte
		readAhead();
		
		LOG.debug("Read bit: {}", result ? "1" : "0");
		return result;
	}

	/**
	 * Reads the next byte from the input stream into <code>_nextByte</code>.
	 * 
	 * @throws IOException
	 *             If there is an IOException reading from the stream.
	 */
	private void readAhead() throws IOException {
		LOG.debug("Buffering next byte");
		if (_endOfStream) {
			return;
		}

		_nextByte = _in.read();
		if (_nextByte == -1) {
			_endOfStream = true;
			return;
		}

		_nextBitIndex = 7;
	}

}
