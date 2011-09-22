package ru.ifmo.diplom.kirilchuk.jawelet.dwt.transforms;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.DecompositionResult;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.DWTransform1D;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.haar.impl.HaarWaveletTransform;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.legall.impl.LeGallWaveletTransform;
import ru.ifmo.diplom.kirilchuk.jawelet.util.MathUtils;

/**
 *
 * @author Kirilchuk V.E.
 */
public class DiscreteWaveletTransformTest {

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

        DWTransform1D instance = new HaarWaveletTransform();
        double[] approximationExpect = {2.121320343559, 4.949747468305, 7.778174593052, 4.949747468305};
        double[] detailExpect = {-0.707106781186, -0.707106781186, -0.707106781186, 4.949747468306};

        DecompositionResult result = instance.decompose(data, 1);
        assertArrayEquals(approximationExpect, result.getApproximation(), DOBLE_COMPARISON_DELTA);
        assertArrayEquals(detailExpect, result.getDetailsList().get(0), DOBLE_COMPARISON_DELTA);
    }

    @Test
    public void testReconstructionFrom1LevelDecomposition() {
        double[] data = {0, 1, 2, 3, 4, 5, 6, 7};

        DWTransform1D instance = new HaarWaveletTransform();
        double[] approximationExpect = {2.121320343559, 4.949747468305, 7.778174593052, 4.949747468305};
        double[] detailExpect = {-0.707106781186, -0.707106781186, -0.707106781186, 4.949747468306};

        DecompositionResult result = instance.decompose(data, 1);
        assertArrayEquals(approximationExpect, result.getApproximation(), DOBLE_COMPARISON_DELTA);
        assertArrayEquals(detailExpect, result.getDetailsList().get(0), DOBLE_COMPARISON_DELTA);

        double[] reconstructed = instance.reconstruct(result);
        assertArrayEquals(data, reconstructed, DOBLE_COMPARISON_DELTA);
    }

    @Test
    public void testReconstructionFrom2LevelDecomposition() {
        double[] data = {0, 1, 2, 3, 4, 5, 6, 7};

        DWTransform1D instance = new HaarWaveletTransform();

        DecompositionResult result = instance.decompose(data, 2);

        double[] reconstructed = instance.reconstruct(result);
        assertArrayEquals(data, reconstructed, DOBLE_COMPARISON_DELTA);
    }

    
    @Test
    public void testReconstructionFrom3LevelDecomposition() {
        double[] data = {0, 1, 2, 3, 4, 5, 6, 7};

        DWTransform1D instance = new HaarWaveletTransform();

        DecompositionResult result = instance.decompose(data, 3);

        double[] reconstructed = instance.reconstruct(result);
        assertArrayEquals(data, reconstructed, DOBLE_COMPARISON_DELTA);
    }

    @Test
    public void testRandomFullDecomposeReconstructionWithHaar() {
        Random rnd = new Random();
        int length = rnd.nextInt(1000) + 1000; //data with 1000..1999 elements
        int extendedLength = MathUtils.getClosest2PowerValue(length);
        int power = MathUtils.getExact2Power(extendedLength);
        
        double[] data = new double[extendedLength];
        for (int i = 0; i < data.length; i++) {
            data[i] = rnd.nextInt(Integer.MAX_VALUE);
        }

        DWTransform1D instance = new HaarWaveletTransform();

        DecompositionResult result = instance.decompose(data);
        assertEquals(power, result.getLevel());
        assertEquals(1, result.getApproximation().length);

        double[] reconstructed = instance.reconstruct(result);

        assertArrayEquals(data, reconstructed, 1);
    }

    @Ignore //need to be rewrited to inplace decomposition
    @Test
    public void testReconstructionFrom1LevelWithLeGall() {
        double[] data = {1,2,3,4};

        DWTransform1D instance = new LeGallWaveletTransform();
        DecompositionResult result = instance.decompose(data, 1);

        double[] reconstructed = instance.reconstruct(result);
        assertArrayEquals(data, reconstructed, DOBLE_COMPARISON_DELTA);
    }

    @Ignore //need to be rewrited to inplace decomposition
    @Test
    public void testRandomFullDecomposeReconstructionWithLeGall() {
        Random rnd = new Random();
        int length = rnd.nextInt(1000) + 1000; //data with 1000..1999 elements
        int extendedLength = MathUtils.getClosest2PowerValue(length);
        int power = MathUtils.getExact2Power(extendedLength);

        double[] data = new double[extendedLength];
        for (int i = 0; i < data.length; i++) {
            data[i] = rnd.nextInt(Integer.MAX_VALUE);
        }

        DWTransform1D instance = new LeGallWaveletTransform();

        DecompositionResult result = instance.decompose(data, power - 1);
        assertEquals(power - 1, result.getLevel());
        assertEquals(2, result.getApproximation().length);

        double[] reconstructed = instance.reconstruct(result);

        assertArrayEquals(data, reconstructed, 1);
    }
}