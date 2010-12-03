package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.DecompositionResult;
import static org.junit.Assert.*;
import ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms.haar.impl.HaarWaveletTransform;

/**
 *
 * @author Kirilchuk V.E.
 */
public class DiscreteWaveletTransformTest {

    public DiscreteWaveletTransformTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    private static double DOBLE_COMPARISON_DELTA = 1.0e-12;

    @Test
    public void testDecomposeTo1Level() {
        double[] data = {0, 1, 2, 3, 4, 5, 6, 7};

        DiscreteWaveletTransform instance = new HaarWaveletTransform();
        double[] approximationExpect = {2.121320343559, 4.949747468305, 7.778174593052, 4.949747468305};
        double[] detailExpect = {-0.707106781186, -0.707106781186, -0.707106781186, 4.949747468306};

        DecompositionResult result = instance.decompose(data, 1);
        assertArrayEquals(approximationExpect, result.getApproximation(), DOBLE_COMPARISON_DELTA);
        assertArrayEquals(detailExpect, result.getDetailsList().get(0), DOBLE_COMPARISON_DELTA);
    }

    @Test
    public void testReconstructionFrom1LevelDecomposition() {
        double[] data = {0, 1, 2, 3, 4, 5, 6, 7};

        DiscreteWaveletTransform instance = new HaarWaveletTransform();
        double[] approximationExpect = {2.121320343559, 4.949747468305, 7.778174593052, 4.949747468305};
        double[] detailExpect = {-0.707106781186, -0.707106781186, -0.707106781186, 4.949747468306};

        DecompositionResult result = instance.decompose(data, 1);
        assertArrayEquals(approximationExpect, result.getApproximation(), DOBLE_COMPARISON_DELTA);
        assertArrayEquals(detailExpect, result.getDetailsList().get(0), DOBLE_COMPARISON_DELTA);

        double[] reconstructed = instance.reconstruct(result, 0);
        assertArrayEquals(data, reconstructed, DOBLE_COMPARISON_DELTA);
    }
}