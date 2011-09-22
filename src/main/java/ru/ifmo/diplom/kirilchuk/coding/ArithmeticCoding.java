package ru.ifmo.diplom.kirilchuk.coding;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

/**
 * @deprecated use arithmetic coders stuff from other package
 * @author Kirilchuk V.E.
 */
public class ArithmeticCoding {
    
	public static void main(String... args) throws IOException {
		String s = "JAWELET";
    	StringReader reader = new StringReader(s);
    	char[] characters = new char[s.length()];
    	for(int i = 0; i < characters.length; ++i) {
    		characters[i] = (char)reader.read();
    	}  
    	
    	List<Character> result = new ArithmeticCoding().encode(characters);
    	System.out.println(result);
    	
    	new ArithmeticCoding().decode(result);
    }
	
	
	public List<Character> encode(char[] data) {		
		ArithmeticCoder coder = new ArithmeticCoder();
		coder.startEncoding();
				
		for(int i = 0; i < data.length; ++i) {			
			coder.encodeCharacter(data[i]);
		}
		coder.encodeEOF();
		coder.doneEncoding();
		List<Character> result = coder.stopOutputingBits();
		
		return result;
	}
	
	public void decode(List<Character> data) {
		ArithmeticDecoder decoder = new ArithmeticDecoder();
		decoder.startDecoding(data);			
		
		while(true) {			
			int decodedSymbol = decoder.decodeSymbol();
			if(decodedSymbol == 10) {
				break;
			}
			
			int ch = decoder.getChar(decodedSymbol);
			System.out.print((char)ch);
			decoder.updateModel(decodedSymbol);
		}
	}
}
