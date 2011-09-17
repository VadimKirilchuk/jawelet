package ru.ifmo.diplom.kirilchuk.coders.impl.fusion;

import java.io.IOException;
import java.io.OutputStream;

import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;

/**
 * This output stream transparently encodes integers
 * by using {@link FusionEncoder} 
 * 
 * @see FusionDecoder
 * @see FusionCodeInputStream
 * @author Kirilchuk V.E.
 */
public class FusionCodeOutputStream extends OutputStream {
	
	private FusionEncoder encoder;
	private BitOutput out;
	
	public FusionCodeOutputStream(FusionEncoder encoder, BitOutput out) {
		this.encoder = encoder;
		this.out = out;
	}
	
	@Override
	public void flush() throws IOException {
		super.flush();
		out.flush();
	}
	
	@Override
	public void close() throws IOException {
		super.close();
		out.close();
	}

	public void write(int i) throws IOException {
		encoder.encode(i, out);
	}	
}
