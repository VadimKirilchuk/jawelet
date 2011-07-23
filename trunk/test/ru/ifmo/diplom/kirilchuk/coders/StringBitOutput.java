package ru.ifmo.diplom.kirilchuk.coders;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coding.io.BitOutput;


/**
 * Helper class for tests.
 * 
 * @author Kirilchuk V.E.
 */
public class StringBitOutput implements BitOutput {

	private StringBuilder data;
	
	public StringBitOutput() {
		data = new StringBuilder();
	}
	
	@Override
	public void close() throws IOException {
		//do nothing
	}

	@Override
	public void flush() throws IOException {
		//do nothing
	}

	@Override
	public void writeBit(boolean bit) throws IOException {
		data.append(bit ? '1' : '0');
	}

	@Override
	public void writeFalseBit() throws IOException {
		writeBit(false);
	}

	@Override
	public void writeTrueBit() throws IOException {
		writeBit(true);
	}

	public String getOutputedString() {
		return data.toString();
	}
}
