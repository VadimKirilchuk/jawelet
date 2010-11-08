package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms;

import ru.ifmo.diplom.kirilchuk.jawelet.dwt.filters.FiltersFactory;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.strategies.impl.StrategiesFactory;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Assert;
/*
 * decomposeXXX and reconstruct methods are initially copied from JWave project.
 * http://code.google.com/p/jwave/
 *
 * Here is the license notice from JWave:
 *
 * Copyright 2010 Christian Scheiblich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @author Christian Scheiblich
 * date 23.02.2010 05:42:23
 * contact source@linux23.de
 *
 * I modifyed this methods for my needs.
 * @author Kirilchuk V.E.
 * date 8.11.2010
 */

/**
 * Abstract class that corresponds for providing basic implementation
 * of decomposition and reconstruction of discrete wavelet transform.
 *
 * @author Kirilchuk V.E.
 */
public abstract class DiscreteWaveletTransform {
    //TODO must be outside class and give strategy in parameter.
    private static final StrategiesFactory strategiesFactory = new StrategiesFactory();//TODO must not be here
    private final FiltersFactory filtersFactory;
    private final TransformStrategy strategy;

    /**
     * Constructs discrete wavelet transform with specified filters factory.
     *
     * @param filtersFactory factory of filters.
     */
    public DiscreteWaveletTransform(FiltersFactory filtersFactory) {
        Assert.argNotNull(filtersFactory);

        this.filtersFactory = filtersFactory;
        this.strategy = strategiesFactory.getDefaultStrategy();
    }

    public double[] decomposeLow(double[] data) {
        return strategy.decomposeLow(data, filtersFactory.getLowDecompositionFilter());
    }

    public double[] decomposeHigh(double[] data) {
        return strategy.decomposeHigh(data, filtersFactory.getHighDecompositionFilter());
    }

    public double[] reconstruct(double[] approximation, double[] details) {
        return strategy.reconstruct(approximation, details,
                                    filtersFactory.getLowReconstructionFilter(),
                                    filtersFactory.getHighReconstructionFilter());
    }

    public FiltersFactory getFiltersFactory() {
        return filtersFactory;
    }
}
