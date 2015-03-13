package ru.ifmo.diplom.kirilchuk.coder.codebook.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class to count entropy. 
 *
 * @param <T> type of values
 */
public class EntropyCounter<T> {

    private List<T> symbols = new ArrayList<T>(256);

    public EntropyCounter<T> addSymbol(T symbol) {
        symbols.add(symbol);
        return this;
    }

    public double getEntropy() {
        Map<T, Integer> frequencyMap = new HashMap<T, Integer>();
        for (T symbol : symbols) {
            increaseCount(frequencyMap, symbol);
        }

        int elementsCount = symbols.size();
        System.out.println("Total elements: " + elementsCount);
        
        // probability is symbolCount/elementsCount
        // so counting entropy as summ(-p(x)*log(p(x))) where log base is two
        double result = 0;
        for (Integer symbolCount : frequencyMap.values()) {
            double p = (double) symbolCount / elementsCount;
            result -= p * log2(p);
        }

        return result;
    }

    private void increaseCount(Map<T, Integer> data, T element) {
        Integer value = data.get(element);
        if (value == null) {
            value = new Integer(0);
            data.put(element, value);
        }
        data.put(element, ++value);
    }

    private double log2(double num) {
        return (Math.log(num) / Math.log(2));
    }
}
