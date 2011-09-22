package ru.ifmo.diplom.kirilchuk.coding.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated was added only for experimental purposes.
 * @author Kirilchuk V.E.
 */
public class Inputer {

	private int buffer;
	private int bits_to_go;
//	private int garbage_bits;

	List<Character> in = new ArrayList<Character>();

	public Inputer(List<Character> input) {
		this.in = input;
	}

	public void initInput() {
		buffer = 0;
		bits_to_go = 0;
//		garbage_bits = 0;
	}

	public int input_bit() {
		int t;
		if (bits_to_go == 0) {
			buffer = in.remove(0);
//			if (buffer == -1) {
//				garbage_bits += 1;
//				if (garbage_bits > 16 - 2) {
//					System.err.println("Bad input\n");
//					System.exit(-1);
//				}
//			}
			bits_to_go = 8;
		}
		t = buffer & 1; /* Return the next bit from */
		buffer >>= 1; /* the bottom of the byte. */
		bits_to_go -= 1;
		return t;
	}
}
