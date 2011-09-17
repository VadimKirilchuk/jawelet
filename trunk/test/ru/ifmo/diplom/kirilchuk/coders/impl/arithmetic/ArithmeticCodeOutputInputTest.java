package ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.AdaptiveUnigramModel;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithCodeInputStream;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithCodeModel;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithCodeOutputStream;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithDecoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithEncoder;
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
		for(int i = 0; i < 256; ++i) {
			encodeDecodeWithAssert(i);
		}
	}
	
	private void encodeDecodeWithAssert(int value) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();		
		BitOutput bitOutput = new BitOutputImpl(bos);
		ArithEncoder encoder = new ArithEncoder(bitOutput);
		ArithCodeOutputStream output = new ArithCodeOutputStream(encoder, encodeModel);
		
		output.write(value);
		output.close();
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		BitInput bitInput = new BitInputImpl(bis);
		ArithDecoder decoder = new ArithDecoder(bitInput);
		ArithCodeInputStream input = new ArithCodeInputStream(decoder, decodeModel);
		
		assertEquals(value, input.read());	
		input.close();
	}
}
