package ru.ifmo.diplom.kirilchuk.coders.impl.monotone.elias;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ru.ifmo.diplom.kirilchuk.coders.Decoder;
import ru.ifmo.diplom.kirilchuk.coders.Encoder;
import ru.ifmo.diplom.kirilchuk.coders.StringBitOutput;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.MonotoneCodeInputStream;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.MonotoneCodeOutputStream;
import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;
import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;
import ru.ifmo.diplom.kirilchuk.coders.io.impl.BitInputImpl;
import ru.ifmo.diplom.kirilchuk.coders.io.impl.BitOutputImpl;


public class MonotoneCodeOutputInputTest {
	
	private Encoder encoder;
	private Decoder decoder;
	
	@Before 
	public void before() {
		encoder = new Elias3PartEncoder();
		decoder = new Elias3PartDecoder();
	}
	
	@Test
	public void simpleTest() throws Exception {
		encodeDecodeWithAssert(21);
		encodeDecodeWithAssert(63);
		encodeDecodeWithAssert(-21);
		encodeDecodeWithAssert(-63);
	}
	
	@Test
	public void integrationTest() throws Exception {
		for(int i = 2; i < Integer.MAX_VALUE - 100000 /* to not overfill */; i+=100000) {
			encodeDecodeWithAssert(i);
		}
		
		encodeDecodeWithAssert(Integer.MAX_VALUE);
		
		for(int i = -2; i > Integer.MIN_VALUE + 100000 /* to not overfill */; i-=100000) {
			encodeDecodeWithAssert(i);
		}
	}
	
	private void encodeDecodeWithAssert(int value) throws Exception {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();		
		BitOutput bitOutput = new BitOutputImpl(bos);
		MonotoneCodeOutputStream output = new MonotoneCodeOutputStream(encoder, bitOutput);
		
		output.write(value);
		output.close();
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		BitInput bitIn = new BitInputImpl(bis);
		MonotoneCodeInputStream input = new MonotoneCodeInputStream(decoder, bitIn);
		
		Assert.assertEquals(value, input.read());
		input.close();
	}
}
