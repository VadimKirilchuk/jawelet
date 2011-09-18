package ru.ifmo.diplom.kirilchuk.coders.impl.fusion;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.Encoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithEncoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;

/**
 * Facade encoder that combines arithmetic and monotone code
 * to produce encoder that can encode all values in integer range.
 * 
 * @author Kirilchuk V.E.
 */
public class FusionEncoder implements Encoder {

	private ArithEncoder arithmeticEncoder;
	private Encoder monotoneEncoder;
	
	/**
	 * Arithmetic encoder will be used for encoding values from
	 * 0 to 255, while monotone encoder will encode other values
	 * to cover range from Integer.MIN_VALUE to Integer.MAX_VALUE  
	 * 
	 * @param arithmeticEncoder arithmetic encoder
	 * @param monotoneEncoder monotome encoder
	 */
	public FusionEncoder(ArithEncoder arithmeticEncoder, Encoder monotoneEncoder) {
		this.arithmeticEncoder = arithmeticEncoder;
		this.monotoneEncoder = monotoneEncoder;
	}
	
	/**
	 * Encodes next value. If value is inside [0, 255] range,
	 * then arithmetic encoding is used, monotone encoding - otherwise.
	 */
	public void encode(int value, BitOutput out) throws IOException {
		if (value >=0 && value <= 255) {//TODO: need to fix arithmetic coder interface
//			arithmeticEncoder.encode(value, out);
		} else {
			monotoneEncoder.encode(value, out);
		}
	}
}
