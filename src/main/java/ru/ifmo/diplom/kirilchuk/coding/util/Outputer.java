package ru.ifmo.diplom.kirilchuk.coding.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @deprecated was added only for experimental purposes.
 * @author Kirilchuk V.E.
 */
public class Outputer {

	private int buffer;
	private int bits_to_go;
	
	private List<Character> output = new ArrayList<Character>(); 
	
	public Outputer() {}
	
	public void initOutput() {
		buffer = 0;
		bits_to_go = 8;
	}
	
	public long bit_plus_follow(int bit,long bits_to_follow) {
		/* Output the bit. */
		output_bit(bit);	
		
		while (bits_to_follow > 0) {
			/* 
			 * Output bits_to_follow opposite bits. Set
			 * bits_to_follow to zero.    
			 */			
			output_bit(bit == 0 ? 1 : 0);
			bits_to_follow -= 1;			
		}
		
		return bits_to_follow;
    }
	
	/* OUTPUT A BIT. */
	private void output_bit(int bit)	{
		buffer >>= 1; 
		if (bit != 0) {
			/* Put bit in top of buffer.*/
			buffer |= 0x80;	
		}
	    bits_to_go -= 1;
	    if (bits_to_go == 0) {			
	    	/* 
	    	 * Output buffer if it is now full.   
	    	 */
	    	output.add((char)buffer);
	        bits_to_go = 8;
	    }
	}

	
	public List<Character> stopOutputingBits() {
		output.add((char)(buffer>>bits_to_go));
		
		return output;
	}
}
