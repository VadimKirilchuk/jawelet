package ru.ifmo.diplom.kirilchuk.coders.impl.fusion;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithEncoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.MonotoneEncoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;
import ru.ifmo.diplom.kirilchuk.coders.Encoder;

/**
 * Facade encoder that combines arithmetic and monotone code
 * to produce encoder that can encode all values in integer range.
 * 
 * @author Kirilchuk V.E.
 */
public class FusionEncoder implements Encoder {

	private ArithEncoder arithmeticEncoder;
	private MonotoneEncoder monotoneEncoder;
	
	public FusionEncoder(ArithEncoder arithmeticEncoder, MonotoneEncoder monotoneEncoder) {
		this.arithmeticEncoder = arithmeticEncoder;
		this.monotoneEncoder = monotoneEncoder;
	}
	
	public void encode(int value, BitOutput out) throws IOException {
		
	}
}
