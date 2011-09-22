package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.strategies.impl;

import java.util.HashMap;
import java.util.Map;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.DWTransformStrategy;

/**
 * @deprecated only one strategy that not works with 5/3 filter.
 * @author Kirilchuk V.E.
 */
public class StrategiesFactory {

    private static final DWTransformStrategy CYCLIC_WRAPPING_STRATEGY = new CyclicWrappingTransformStrategy();

    private static final Map<String, DWTransformStrategy> strategies = new HashMap<String, DWTransformStrategy>();
    static {
        strategies.put("default", CYCLIC_WRAPPING_STRATEGY);
        strategies.put("cyclicWrapping", CYCLIC_WRAPPING_STRATEGY);
    }

    private StrategiesFactory() {}

    public static DWTransformStrategy getByName(String name) {
        return strategies.get(name);
    }
}
