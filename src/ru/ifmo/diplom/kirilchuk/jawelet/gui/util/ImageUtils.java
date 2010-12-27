package ru.ifmo.diplom.kirilchuk.jawelet.gui.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Assert;

/**
 * Utility class for working with images.
 *
 * @author Kirilchuk V.E.
 */
public final class ImageUtils {

    private ImageUtils() {}

    /**
     * Loads image from file.
     *
     * @param file file with image.
     * @throws IOException if an error occurs during reading.
     * @return a BufferedImage containing the decoded contents of the input, or null.
     */
    public static BufferedImage loadImage(File file) throws IOException {
        Assert.checkNotNull(file, "File can`t be null.");
        return ImageIO.read(file);
    }

    /**
     * Saves image to specified location.
     *
     * @param image image to save.
     * @param filePath path to destination.
     * @throws IOException if an error occurs during save.
     */
    public static void saveAsBitmap(BufferedImage image, String filePath) throws IOException {
        File file = new File(filePath);
        ImageIO.write(image, "bmp", file);
    }

    public static boolean isGrayScale(BufferedImage image) {
        int type = image.getType();
        return (type == BufferedImage.TYPE_BYTE_GRAY || type == BufferedImage.TYPE_USHORT_GRAY);
    }

    public static int[][] getGrayscaleImageData(BufferedImage image) {
        Assert.checkNotNull(image, "Image can`t be null.");
        Assert.argCondition(isGrayScale(image), "Image must be grayscale.");

        int width = image.getWidth();
        int heigth = image.getHeight();

        int[] lineData = new int[width * heigth];
        image.getRaster().getPixels(0, 0, width, heigth, lineData);

        //converting to 2D array
        int[][] result = new int[width][heigth];
        int shift = 0;
        for (int row = 0; row < heigth; ++row) {
            System.arraycopy(lineData, shift, result[row], 0, width);
            shift += width;
        }

        return result;
    }
}
