package ru.ifmo.diplom.kirilchuk.coding.io;

import java.io.IOException;

/**
 * Output that works in terms of bits.
 * Standard java library does not allow you to do such things, so
 * implementation of this interface must provide program way 
 * to do this.
 * 
 * <p> Methods like flush and close included in this interface, assuming,
 * that implementation will be based on some buffers and will write 
 * data to the underlaying streams.
 * 
 * @author Kirilchuk V.E.
 */
public interface BitOutput {

	/**
	 * Closes underlying output stream after filling to a byte boundary with
	 * <code>0</code> bits.
	 * 
	 * @throws IOException
	 *             If there is an I/O exception writing the next byte or closing
	 *             the underlying output stream.
	 */
	void close() throws IOException;

	/**
	 * Flushes the underlying output stream.
	 * 
	 * @throws IOException
	 *             If there is an exception flushing the underlying output
	 *             stream.
	 */
	void flush() throws IOException;

	/**
	 * Writes the single specified bit to the underlying output stream,
	 * <code>1</code> for <code>true</code> and <code>0</code> for
	 * <code>false</code>.
	 * 
	 * @param bit
	 *            Value to write.
	 * @throws IOException
	 *             If there is an exception in the underlying output stream.
	 */
	void writeBit(boolean bit) throws IOException;

	/**
	 * Writes a single <code>true</code> (<code>1</code>) bit.
	 * 
	 * @throws IOException
	 *             If there is an exception in the underlying output stream.
	 */
	void writeTrueBit() throws IOException;

	/**
	 * Writes a single <code>false</code> (<code>0</code>) bit.
	 * 
	 * @throws IOException
	 *             If there is an exception in the underlying output stream.
	 */
	void writeFalseBit() throws IOException;
}