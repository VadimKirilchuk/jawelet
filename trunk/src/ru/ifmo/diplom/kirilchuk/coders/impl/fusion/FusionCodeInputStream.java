package ru.ifmo.diplom.kirilchuk.coders.impl.fusion;

import java.io.IOException;
import java.io.InputStream;

import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;

/**
 * This input stream transparently reads integers which was
 * encoded by {@link FusionEncoder} 
 * 
 * @see FusionDecoder
 * @see FusionCodeOutputStream
 * @author Kirilchuk V.E.
 */
public class FusionCodeInputStream extends InputStream {

	private FusionDecoder decoder;
	private BitInput in;
	
	
	public FusionCodeInputStream(FusionDecoder decoder, BitInput in) {
		this.decoder = decoder;
		this.in = in;
	}
	
	@Override
	public int read() throws IOException {
		return decoder.decodeNext(in);
	}
}
