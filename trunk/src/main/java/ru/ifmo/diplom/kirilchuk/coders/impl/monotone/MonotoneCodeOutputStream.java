package ru.ifmo.diplom.kirilchuk.coders.impl.monotone;

import java.io.IOException;
import java.io.OutputStream;

import ru.ifmo.diplom.kirilchuk.coders.Encoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;


public class MonotoneCodeOutputStream extends OutputStream {
	
	private Encoder encoder;
	private BitOutput out;
	
	public MonotoneCodeOutputStream(Encoder encoder, BitOutput out) {
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
