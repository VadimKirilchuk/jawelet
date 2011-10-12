package ru.ifmo.diplom.kirilchuk.coders.impl.monotone;

import java.io.IOException;
import java.io.InputStream;

import ru.ifmo.diplom.kirilchuk.coders.Decoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;

public class MonotoneCodeInputStream extends InputStream {

	private Decoder decoder;
	private BitInput in;
	
	
	public MonotoneCodeInputStream(Decoder decoder, BitInput in) {
		this.decoder = decoder;
		this.in = in;
	}
	
	@Override
	public int read() throws IOException {
		return decoder.decodeNext(in);
	}
}
