package ru.ifmo.diplom.kirilchuk.coder.codebook;

import java.util.Arrays;

public class FilterProbabilityPair {

    public double[] filter;
    public double probability;
    public int realIndex;

    public FilterProbabilityPair(double[] filter, double probability, int realIndex) {
        this.filter = filter;
        this.probability = probability;
        this.realIndex = realIndex;
    }

    @Override
    public String toString() {
        return "[filter=" + Arrays.toString(filter) + ", probability=" + probability + "]\n";
    }

}
