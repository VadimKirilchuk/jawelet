package ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic;

import java.io.IOException;
import java.io.OutputStream;

import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;
import ru.ifmo.diplom.kirilchuk.coders.util.Converter;


/**
 * <P>
 * A filter output stream which uses a statistical model and arithmetic coding
 * for compression of bytes read from an underlying arithmetic encoder. This
 * encoder may be constructed from an output stream or bit output. Given a model
 * and a stream, this class operates in the same way as
 * <code>java.util.zip.GZIPOutputStream</code>.
 * 
 * @author <a href="http://www.colloquial.com/carp/">Bob Carpenter</a>
 * @version 1.1
 * @see ArithCodeInputStream
 * @see ArithCodeModel
 * @since 1.0
 */
public class ArithCodeOutputStream extends OutputStream {

	private BitOutput out;

	/**
	 * The arithmetic encoder used to write coded bytes.
	 */
	private final ArithEncoder encoder;

	public ArithCodeOutputStream(ArithEncoder encoder, BitOutput output) {
		this.encoder = encoder;
		this.out = output;
	}

	/**
	 * Writes the eight low-order bits of argument to the output stream as a
	 * byte.
	 * 
	 * @param i
	 *            Bits to write.
	 * @throws IOException
	 *             If there is an exception in writing to the underlying
	 *             encoder.
	 */
	public void write(int i) throws IOException {
		encoder.encode(i, out);
	}

	/**
	 * Close this output stream.
	 * 
	 * @throws IOException
	 *             If there is an exception in the underlying encoder.
	 */
	public void close() throws IOException {
		write(ArithCodeModel.EOF); // must code EOF to allow decoding to halt
		encoder.close(out); // to allow encoder to output buffered bits
		out.close();
	}

	/**
	 * Flushes underlying stream.
	 * 
	 * @throws IOException
	 *             If there is an exception flushing the underlying stream.
	 */
	public void flush() throws IOException {
		out.flush();
	}

	/**
	 * Writes array of bytes to the output stream.
	 * 
	 * @param bs
	 *            Array of bytes to write.
	 * @throws IOException
	 *             If there is an exception in writing to the underlying
	 *             encoder.
	 */
	public void write(byte[] bs) throws IOException {
		write(bs, 0, bs.length);
	}

	/**
	 * Writes section of array of bytes to the output stream.
	 * 
	 * @param bs
	 *            Array of bytes to write.
	 * @param off
	 *            Index from which to start writing.
	 * @param len
	 *            Number of bytes to write.
	 * @throws IOException
	 *             If there is an exception in writing to the underlying
	 *             encoder.
	 */
	public void write(byte[] bs, int off, int len) throws IOException {
		while (off < len) {
			write(Converter.byteToInteger(bs[off++]));
		}
	}
}
