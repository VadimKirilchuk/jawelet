package ru.ifmo.diplom.kirilchuk.coders.impl.monotone.fixed;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.Encoder;
import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;
import ru.ifmo.diplom.kirilchuk.util.AnalyzeUtils;

/**
 * I need this only to check if fixed-length coding will give me more than monotone encoding.
 */
public class FixedEncoder implements Encoder {

    private int bitsPerNumber;
    
    /**
     * Constructor from range.
     * For example for values from -64 to 64 range is 128
     * @param range from zero
     */
    public FixedEncoder(int range) {        
        this.bitsPerNumber = (int) Math.floor(AnalyzeUtils.log2(range));//for value
    }
    
    @Override
    public void encode(int value, BitOutput out) throws IOException {
        //TODO: this is just stub so value is not used
        for(int i = 0; i < bitsPerNumber; ++i) {
            out.writeTrueBit();
        }
    }
}
