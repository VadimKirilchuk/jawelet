package ru.ifmo.diplom.kirilchuk.coders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.ifmo.diplom.kirilchuk.coding.io.BitInput;


/**
 * Helper class for tests.
 * 
 * @author Kirilchuk V.E.
 */
public class StringBitInput implements BitInput {

	private List<Boolean> bytes = new ArrayList<Boolean>();
	
	public StringBitInput(String input) {
		//parsing input
		int length = input.length();
		char[] chars = new char[length];
		input.getChars(0, length, chars, 0);
		for(char ch : chars) {
			if(ch == '1') {
				bytes.add(Boolean.TRUE);
			} else if (ch == '0') {
				bytes.add(Boolean.FALSE);
			} else {
				throw new IllegalArgumentException("Not supported character: [" + ch + "] Can`t parse.");
			}
		}
	}
	
	@Override
	public long available() throws IOException {
		return bytes.size();
	}

	@Override
	public void close() throws IOException {
		//do nothing
	}

	@Override
	public boolean endOfStream() {
		return bytes.size() == 0;
	}

	@Override
	public boolean readBit() throws IOException {
		if(endOfStream()) {
			throw new IOException("Can`t read from ended stream.");
		}
		return bytes.remove(0);
	}

}
