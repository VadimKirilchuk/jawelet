package ru.ifmo.diplom.kirilchuk.coders.impl.fusion;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import ru.ifmo.diplom.kirilchuk.coders.Decoder;
import ru.ifmo.diplom.kirilchuk.coders.Encoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.AdaptiveUnigramModel;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithDecoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.arithmetic.ArithEncoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.levenshtein.LevenshteinDecoder;
import ru.ifmo.diplom.kirilchuk.coders.impl.monotone.levenshtein.LevenshteinEncoder;

public class FusionCodeOutputInputTest {
	private FusionEncoder encoder;
	private FusionDecoder decoder;
	
	private Random rnd = new Random();
	
	@Before
	public void before() {
		ArithEncoder arithEncoder = new ArithEncoder(new AdaptiveUnigramModel());
		Encoder monotoneEncoder = new LevenshteinEncoder();
		encoder = new FusionEncoder(arithEncoder, monotoneEncoder);

		ArithDecoder arithDecoder = new ArithDecoder(new AdaptiveUnigramModel());
		Decoder monotoneDecoder = new LevenshteinDecoder();
		decoder = new FusionDecoder(arithDecoder, monotoneDecoder);
	}
	
	@Test
	public void integrationTestReadWriteSequenceOnlyArithCode() throws Exception {
		int[] values = new int[255];
		for (int i = 0; i < values.length; ++i) {
			values[i] = i;
		}
		encodeDecodeWithAssert(values);
	}

//	@Ignore//TODO: FAIL NOT WORKS!
	@Test
	public void testReadWriteArithMonoArith() throws Exception {
		int[] values = {
				128, 255
		};
		encodeDecodeWithAssert(values);
	}

	private void encodeDecodeWithAssert(int... values) throws Exception {
//		ByteArrayOutputStream bos = new ByteArrayOutputStream();
//		BitOutput bitOutput = new BitOutputImpl(bos);
//		FusionCodeOutputStream output = new FusionCodeOutputStream(encoder, bitOutput);
//
//		for (int value : values) {
//			output.write(value);
//		}
//		output.close();
//
//		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
////		System.out.println(Arrays.toString(bos.toByteArray()));
//		BitInput bitInput = new BitInputImpl(bis);
//		FusionCodeInputStream input = new FusionCodeInputStream(decoder, bitInput);
//
//		for (int value : values) {
//			assertEquals(value, input.read());
//		}
//		input.close();
	}
}
