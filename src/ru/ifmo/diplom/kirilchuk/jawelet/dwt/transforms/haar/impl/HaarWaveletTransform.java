package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.haar.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.haar.impl.HaarFiltersFactory;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.DiscreteWaveletTransform;

/**
 * Class that represents DiscreteWaveletTransform on haar filter bank basis.
 * 
 * @author Kirilchuk V.E.
 */
public class HaarWaveletTransform extends DiscreteWaveletTransform {

    public HaarWaveletTransform() {
        super(new HaarFiltersFactory());
    }
}
