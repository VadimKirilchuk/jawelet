package ru.ifmo.diplom.kirilchuk.coding;

import java.io.IOException;
import java.io.InputStreamReader;


public class ArithmeticCoding {

	public static void main(String... args) throws IOException {		

		ArithmeticCoder coder = new ArithmeticCoder();
		coder.startEncoding();
		
		InputStreamReader reader = new InputStreamReader(System.in);
		while(true) {
			int ch = reader.read();
			if(ch == 10) { //enter as EOF
				break;
			}
			coder.encodeCharacter(ch);
		}
		coder.encodeEOF();
		coder.doneEncoding();
		coder.stopOutputingBits();
	}
}
