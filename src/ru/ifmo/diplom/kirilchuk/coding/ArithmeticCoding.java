package ru.ifmo.diplom.kirilchuk.coding;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ArithmeticCoding {

	public void encode(int[] sequence) {
		int n = sequence.length;

		List<Integer> alphabet = new ArrayList<Integer>();
		List<Integer> counts = new ArrayList<Integer>();
		for (int i = 0; i < n; ++i) {
			Integer letter = sequence[i];
			int index = alphabet.indexOf(letter);
			if (index == -1) {
				alphabet.add(letter);
				counts.add(1);
			} else {
				Integer value = counts.get(index);
				counts.set(index, value + 1);
			}
		}	

		int alphabetSize = alphabet.size();
		double[] probabilities = new double[alphabetSize];
		double[] qumulative = new double[alphabetSize];

		probabilities[0] = alphabet.get(0) / (double) n;
		for (int i = 1; i < alphabetSize; ++i) {
			probabilities[i] = alphabet.get(i) / (double) n;
			qumulative[i] = qumulative[i - 1] + probabilities[i - 1];
		}
		
		///////////
		alphabet = new ArrayList<Integer>();
		alphabet.add(1);
		alphabet.add(2);
		alphabet.add(3);

		probabilities = new double[] { 0.1, 0.6, 0.3 };
		qumulative = new double[] {0.0, 0.1, 0.7};
		///////////
		sequence = new int[] { 2, 3, 2, 1, 2 };

		BigDecimal f = new BigDecimal(0);
		BigDecimal g = new BigDecimal(1);
		for (int i = 0; i < n; ++i) {
			int index = alphabet.indexOf(sequence[i]);
			double p = probabilities[index];
			double q = qumulative[index];
			
			BigDecimal pG = g.multiply(new BigDecimal(p));					
			BigDecimal qG = g.multiply(new BigDecimal(q));
		
			f = f.add(qG);
			System.out.println("f: " + f.doubleValue());
			
			g = pG;
			System.out.println("g: " + g.doubleValue());
		}
		
		long codeLength = (long)(-(Math.log(g.doubleValue()) / Math.log(2.0)) + 1) + 1;
		System.out.println("length: " + codeLength);
	}
	
	public void decode(List<Integer> alphabet,
			double[] probabilities, int decodeLength, double code) {
		List<Integer> result = new ArrayList<Integer>();
		
		int alphabetSize = alphabet.size();
		double[] qumulative = new double[alphabetSize + 1];
		for (int i = 1; i < alphabetSize; ++i) {
			qumulative[i] = qumulative[i - 1] + probabilities[i - 1];
		}
		
		qumulative[alphabetSize] = 1;
		
		BigDecimal s = new BigDecimal(0);
		BigDecimal g = new BigDecimal(1);
		
		for (int i = 0; i < decodeLength; ++i) {
			int j = 0;								
			while(s.add(g.multiply(new BigDecimal(qumulative[j+1]))).doubleValue() < code) {
				j++;
			}
			
			s = s.add(g.multiply(new BigDecimal(qumulative[j])));	
			g = g.multiply(new BigDecimal(probabilities[j]));			
			
			result.add(alphabet.get(j));
		}			
	}
	
	public static void main(String... args) {
		new ArithmeticCoding().encode(new int[] { 2, 3, 2, 1, 2 });
		
		List<Integer> alphabet = new ArrayList<Integer>();
		alphabet.add(1);
		alphabet.add(2);
		alphabet.add(3);
		
		new ArithmeticCoding().decode(alphabet, new double[]{0.1, 0.6, 0.3}, 5, 0.541);
	}
}
