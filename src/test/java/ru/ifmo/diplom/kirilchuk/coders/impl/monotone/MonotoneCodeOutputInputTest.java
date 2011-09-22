package ru.ifmo.diplom.kirilchuk.coders.impl.monotone;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.MonotoneCodeInputStream;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.MonotoneCodeOutputStream;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.MonotoneDecoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.MonotoneEncoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitInput;
import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;
import ru.ifmo.diplom.kirilchuk.coders.io.impl.BitInputImpl;
import ru.ifmo.diplom.kirilchuk.coders.io.impl.BitOutputImpl;


public class MonotoneCodeOutputInputTest {
	
	private MonotoneEncoder encoder;
	private MonotoneDecoder decoder;
	
	@Before 
	public void before() {
		encoder = new MonotoneEncoder();
		decoder = new MonotoneDecoder();
	}
	
	@Test
	public void integrationTest() throws Exception {
		for(int i = 1; i < Integer.MAX_VALUE - 100000 /* to not overfill */; i+=100000) {
			encodeDecodeWithAssert(i);
		}
		
		encodeDecodeWithAssert(Integer.MAX_VALUE);
		
		for(int i = -1; i > Integer.MIN_VALUE + 100000 /* to not overfill */; i-=100000) {
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
