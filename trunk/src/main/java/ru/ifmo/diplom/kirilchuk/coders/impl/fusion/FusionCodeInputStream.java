package ru.ifmo.diplom.kirilchuk.coders.impl.fusion;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;

/**
 * This input stream transparently reads integers which was encoded by
 * {@link FusionEncoder}. Note that fusion encoder encodes to two separate 
 * streams: one for arithmetic code and other for monotone code.
 * This is the simplest solution to fuse arithmetic code with another code.
 * (In other way you must hardly hack your arithmetic encoder) 
 * Aslo this helps a lot with gathering separate statistics on this codes.
 * 
 * @see FusionDecoder
 * @see FusionCodeOutputStream
 * @author Kirilchuk V.E.
 */
public class FusionCodeInputStream {

	private FusionDecoder decoder;
	private BitInput arithIn;
	private BitInput monoIn;

	private int arithmeticCodeShift; //0 by default
	/**
	 * Constructs input facade with specified fusion decoder and 
	 * bit inputs for arithmetic code and monotone code.
	 * 
	 * @param decoder decoder for fusion code
	 * @param arithIn bit input for arithmetic code
	 * @param monoIn bit input for monotone code
	 */
	public FusionCodeInputStream(FusionDecoder decoder, BitInput arithIn, BitInput monoIn) {
		this.decoder = decoder;
		this.arithIn = arithIn;
		this.monoIn = monoIn;
	}

	/**
	 * Returns net integer from one of underlaying inputs.
	 * 
	 * @return readed integer
	 * @throws IOException if io errors occur during reading
	 */
	public int read() throws IOException {//TODO move logic for decision about monotone and shift to decoder
		int result = decoder.decodeNext(arithIn, false);
		if (result == FusionCoderConstants.ESCAPE_TO_SWITCH_TO_MONOTONE_CODE) {
			result = decoder.decodeNext(monoIn, true);
		} else {
			result = result - arithmeticCodeShift;
		}

		return result;
	}
	
	/**
	 * Sets shift for arithmetic code. For example,
	 * if we encoded value "-100" as (-100+127): "27" by
	 * arithmetic encoder we must read 27 from stream
	 * and shift it back to -100. It is done because
	 * current arithmetic coder encodes symbols from 0 to 255 only. 
	 * 
	 * @param shift shift for arithmetic code
	 */
	public void setArithmeticCodeShift(int shift) {
		this.arithmeticCodeShift = shift;
	}
}
