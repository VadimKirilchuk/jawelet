package ru.ifmo.diplom.kirilchuk.coders.io;

import java.io.IOException;

/**
 * Input that works in terms of bits.
 * Standard java library does not allow you to do such things, so
 * implementation of this interface must provide program way 
 * to do this.
 * 
 * @author Kirilchuk V.E.
 *
 */
public interface BitInput {

	/**
	 * Returns number of bits available for reading. Will always be
	 * <code>0</code> or <code>1</code>.
	 * 
	 * @return Number of bits available for reading.
	 * @throws IOException
	 *             If there is an exception checking available bytes in the
	 *             underlying input stream.
	 */
	@Deprecated
	long available() throws IOException;

	/**
	 * Closes the underlying input stream.
	 * 
	 * @throws IOException
	 *             If there is an exception closing the underlying input stream.
	 */
	void close() throws IOException;

	/**
	 * Returns <code>true</code> if all of the available bits have been read.
	 * 
	 * @return <code>true</code> if all of the available bits have been read.
	 */
	boolean endOfStream();

	/**
	 * Reads the next bit from the input stream. Returns garbage if reading
	 * while available() is false.
	 * 
	 * @return The boolean value of the next bit, <code>true</code>=1,
	 *         <code>false</code>=0.
	 * @throws IOException
	 *             If there is an exception reading a byte from the underlying
	 *             stream.
	 */
	boolean readBit() throws IOException;

}