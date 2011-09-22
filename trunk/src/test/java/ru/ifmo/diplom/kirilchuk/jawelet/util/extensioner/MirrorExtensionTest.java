package ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner;

import org.junit.Before;
import org.junit.Test;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.MirrorExtension;
import ru.ifmo.diplom.kirilchuk.jawelet.util.extensioner.actions.ZeroPaddingToEven;
import static org.junit.Assert.*;

/**
 *
 * @author Kirilchuk V.E.
 */
public class MirrorExtensionTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testMirrorExtension() {
        double[] data = {1, 2, 3, 4, 5};

        double[] expect = {2, 1, 2, 3, 4, 5, 4};
        double[] result = new Extensioner(data).schedule(new MirrorExtension(1)).execute();
        assertArrayEquals(expect, result, 0);

        expect = new double[]{5, 4, 3, 2, 1, 2, 3, 4, 5, 4, 3, 2, 1};
        result = new Extensioner(data).schedule(new MirrorExtension(4)).execute();
        assertArrayEquals(expect, result, 0);

        expect = new double[]{4, 3, 2, 1, 2, 3, 4, 5, 0, 5, 4, 3};
        result = new Extensioner(data)
                .schedule(new ZeroPaddingToEven())
                .schedule(new MirrorExtension(3)).execute();
        assertArrayEquals(expect, result, 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void shouldFailWhenDataTooSmallForMirroring() {
        double[] data = {1, 2, 3};
        new Extensioner(data).schedule(new MirrorExtension(3)).execute();
    }
}
