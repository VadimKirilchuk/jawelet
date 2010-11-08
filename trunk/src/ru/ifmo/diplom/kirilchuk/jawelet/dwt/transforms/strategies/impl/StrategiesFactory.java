package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.strategies.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.TransformStrategy;

/**
 *
 * @author Kirilchuk V.E.
 */
public class StrategiesFactory {
    private static final TransformStrategy DEFAULT_STRATEGY = new PeriodicalExtensionTransformStrategy();

    public TransformStrategy getDefaultStrategy() {
        return DEFAULT_STRATEGY;
    }
}
