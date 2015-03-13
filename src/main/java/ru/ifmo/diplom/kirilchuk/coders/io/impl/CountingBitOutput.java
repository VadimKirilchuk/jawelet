package ru.ifmo.diplom.kirilchuk.coders.io.impl;

import java.io.IOException;

import ru.ifmo.diplom.kirilchuk.coders.io.BitOutput;

/**
 * Dummy class which just counting number of written bits. 
 */
public class CountingBitOutput implements BitOutput {

    private int bits = 0;

    public void close() throws IOException {}

    public void flush() throws IOException {}

    public void writeBit(boolean bit) throws IOException {
        ++bits;
    }

    public void writeTrueBit() throws IOException {
        ++bits;
    }

    public void writeFalseBit() throws IOException {
        ++bits;
    }

    public int getBits() {
        return bits;
    }

}
