package ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;

import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;
import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;
import ru.ifmo.diplom.kirilchuk.coders.io.impl.BitInputImpl;
import ru.ifmo.diplom.kirilchuk.coders.io.impl.BitOutputImpl;

public class ArithmeticCodeOutputInputTest {
	
	private ArithCodeModel encodeModel;
	private ArithCodeModel decodeModel;
	
	@Before
	public void before() {
		encodeModel = new AdaptiveUnigramModel();
		decodeModel = new AdaptiveUnigramModel();
	}
	
	@Test
	public void integrationTest() throws Exception {
		for(int i = 0; i <= 255; ++i) {
			encodeDecodeWithAssert(i);
		}
	}
	
	@Test
	public void integrationTestReadWriteSequence() throws Exception {
		int[] values = new int[256];
		for(int i = 0; i < values.length; ++i) {
			values[i] = i;
		}
		encodeDecodeWithAssert(values);
	}
	
	@Test
	public void integrationTestReadWriteSequence2() throws Exception {
		int[] values = {128, 255};
		encodeDecodeWithAssert(values);
	}
	
	private void encodeDecodeWithAssert(int... values) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();		
		BitOutput bitOutput = new BitOutputImpl(bos);
		ArithEncoder encoder = new ArithEncoder(encodeModel);
		ArithCodeOutputStream output = new ArithCodeOutputStream(encoder, bitOutput);
		
		for(int value: values) {
			output.write(value);
		}
		output.close();
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		BitInput bitInput = new BitInputImpl(bis);
		ArithDecoder decoder = new ArithDecoder(decodeModel);
		ArithCodeInputStream input = new ArithCodeInputStream(decoder, bitInput);
		
		for(int value: values) {
			assertEquals(value, input.read());
		}
		input.close();
	}
}
