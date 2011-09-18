package ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic;

import java.io.IOException;
import java.io.InputStream;

import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;
import ru.ifmo.diplom.kirilchuk.coders.util.Converter;

/**
 * <P>
 * An input stream which uses a statistical model and arithmetic coding for
 * decompression of encoded bytes read from an underlying input stream. Given a
 * statistical model of a byte sequence, it operates in the same way as
 * <code>java.util.zip.GZIPInputStream</code>.
 * 
 * @author <a href="http://www.colloquial.com/carp/">Bob Carpenter</a>
 * @version 1.1
 * @see ArithCodeOutputStream
 * @see ArithCodeModel
 * @since 1.0
 */
public final class ArithCodeInputStream extends InputStream {

	/**
	 * The arithmetic decoder used to read bytes.
	 */
	private final ArithDecoder _decoder;

	private BitInput input;
	
	public ArithCodeInputStream(ArithDecoder decoder, BitInput input) {
		this._decoder = decoder;
		this.input = input;
	}

	/**
	 * Closes this input stream.
	 * 
	 * @throws IOException
	 *             If there is an exception closing the underlying input stream.
	 */
	public void close() throws IOException {
//		_decoder.close();
		input.close();
	}

	/**
	 * Read an array of bytes into the specified byte array, returning number of
	 * bytes read.
	 * 
	 * @param bs
	 *            Byte array into which to read the bytes.
	 * @return Number of bytes read.
	 * @throws IOException
	 *             If there is an I/O exception reading from the underlying
	 *             stream.
	 */
	public int read(byte[] bs) throws IOException {
		return read(bs, 0, bs.length);
	}

	/**
	 * Read the specified number of bytes into the array, beginning from the
	 * position specified by the offset. Return the total number of bytes read.
	 * Will be less than array length if the end of stream was encountered.
	 * 
	 * @param bs
	 *            Byte array into which to read the bytes.
	 * @param off
	 *            Offset into byte array from which to begin writing output.
	 * @param len
	 *            Maximum number of bytes to read.
	 * @return Number of bytes read.
	 * @throws IOException
	 *             If there is an I/O exception reading from the underlying
	 *             stream.
	 */
	public int read(byte[] bs, int off, int len) throws IOException {
		for (int i = off; i < len; ++i) {
			int nextByte = read();
			if (nextByte == -1) {
				return (i - off); // eof, return length read
			}
			bs[i] = Converter.integerToByte(nextByte);
		}
		return len > 0 ? len : 0;
	}

	/**
	 * Reads the next byte from the input stream. Returns -1 if end-of-stream is
	 * encountered; otherwise result is given in the low order 8 bits of the
	 * return value.
	 * 
	 * @return The next byte from the input stream or -1 if end of stream is
	 * encountered.
	 * 
	 * @throws IOException If there is an I/O exception reading from the
	 * underlying stream.
	 */
	public int read() throws IOException {
		return _decoder.decodeNext(input);
	}

	/**
	 * Not supported. Throws an <code>IOException</code> if called.
	 * 
	 * @throws IOException
	 *             whenever called.
	 */
	public void reset() throws IOException {
		throw new IOException("Reset not supported");
	}
}
