package ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.haar.impl;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.filters.haar.impl.HaarFiltersFactory;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.DWTransform1D;

/**
 * Class that represents DiscreteWaveletTransform on haar filter bank basis.
 * 
 * @author Kirilchuk V.E.
 */
public class HaarWaveletTransform extends DWTransform1D {

    public HaarWaveletTransform() {
        super(new HaarFiltersFactory());
    }
}
