package ru.ifmo.diplom.kirilchuk.coders.impl.fusion;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;

/**
 * This output stream transparently encodes integers by using
 * {@link FusionEncoder}. Note that fusion encoder encodes to two separate 
 * streams: one for arithmetic code and other for monotone code.
 * This is the simplest solution to fuse arithmetic code with another code.
 * (In other way you must hardly hack your arithmetic encoder) 
 * Aslo this helps a lot with gathering separate statistics on this codes.
 * 
 * @see FusionDecoder
 * @see FusionCodeInputStream
 * @author Kirilchuk V.E.
 */
public class FusionCodeOutputStream {

	private FusionEncoder encoder;
	private BitOutput arithOut;
	private BitOutput monoOut;

	public FusionCodeOutputStream(FusionEncoder encoder, BitOutput arithOut, BitOutput monoOut) {
		this.encoder = encoder;
		this.arithOut = arithOut;
		this.monoOut = monoOut;
	}

	public void close() throws IOException {
		//arithmetic encoder must output some additional information 
		//about end of coding
		encoder.close(arithOut);
		
		//then close underlaying streams
		arithOut.close();
		monoOut.close();
	}

	/**
	 * Writes next integer to one of underlying outs. 
	 * 
	 * @param value integer to write
	 * @throws IOException
	 */
	public void write(int value) throws IOException {
		encoder.encode(value, arithOut, monoOut);
	}
}
