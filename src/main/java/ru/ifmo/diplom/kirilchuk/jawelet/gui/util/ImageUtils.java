package ru.ifmo.diplom.kirilchuk.jawelet.gui.util;

import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
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
     * Saves image as bitmap to specified location.
     *
     * @param image image to save.
     * @param filePath path to destination.
     * @throws IOException if an error occurs during save.
     */
    public static void saveAsBitmap(BufferedImage image, String filePath) throws IOException {
        File file = new File(filePath);
        ImageIO.write(image, "bmp", file);
    }

    /**
     * Checks whenever image is grayscale.
     * 
     * @param image to check
     * @return true if image is grayscale, false otherwise
     */
    public static boolean isGrayScale(BufferedImage image) {
    	Assert.checkNotNull(image, "Image can`t be null.");
        int type = image.getType();
        return (type == BufferedImage.TYPE_BYTE_GRAY || type == BufferedImage.TYPE_USHORT_GRAY);
    }

    /**
     * Extracts data from grayscale image and returns it as 2d array of doubles.
     * 
     * @param image image to get data from
     * @return image data as double array
     */
    public static double[][] getGrayscaleImageData(BufferedImage image) {
        Assert.checkNotNull(image, "Image can`t be null.");
        Assert.argCondition(isGrayScale(image), "Image must be grayscale.");

        int width = image.getWidth();
        int heigth = image.getHeight();

        double[] lineData = new double[width * heigth];
        image.getRaster().getPixels(0, 0, width, heigth, lineData);

        //converting to 2D array
        double[][] result = new double[heigth][width];
        int shift = 0;
        for (int row = 0; row < heigth; ++row) {
            System.arraycopy(lineData, shift, result[row], 0, width);
            shift += width;
        }

        return result;
    }

    /**
     * Changes data of specified image to given one.
     * 
     * @param image image to change data in
     * @param imageData data to set in image
     */
	public static void setNewGrayscaleImageData(BufferedImage image, double[][] imageData) {
        Assert.checkNotNull(image, "Image can`t be null.");
        Assert.argCondition(isGrayScale(image), "Image must be grayscale.");
        
        int dataHeigth = imageData.length;
        int dataWidth = imageData[0].length;
        double[] data = new double[dataHeigth * dataWidth];
        int shift = 0;
        for(int row = 0; row < dataHeigth; ++row) {
        	System.arraycopy(imageData[row], 0, data, shift, dataWidth);
        	shift += dataWidth;
        }
        
        image.getRaster().setPixels(0, 0, dataWidth, dataHeigth, data);
	}
	
	/**
	 * Helper method to change colorspace of image.
	 * 
	 * @param source image to change
	 * @param colorSpace target colorspace
	 * @return image with changed colorspace
	 */
	public static BufferedImage changeColorSpace(BufferedImage source, ColorSpace colorSpace) {
		BufferedImageOp operation = new ColorConvertOp(colorSpace, null);
		return operation.filter(source, null);
	}
	
	/**
	 * Tryes to create grayscale copy of given image. If image is already grayscale 
	 * then returns itself.
	 * 
	 * @param source image to create copy from
	 * @return grayscale copy of given image or itself if it is already grayscale
	 */
	public static BufferedImage tryCreateGrayscaleCopy(BufferedImage source) {
		Assert.checkNotNull(source, "Source image can`t be null.");
		
		if(isGrayScale(source)) {
			return source;
		}
		
		BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		Graphics graphics = result.getGraphics();
		graphics.drawImage(source, 0, 0, null);
		graphics.dispose();
		
		return result;
	}
	
	/**
	 * Normalizes image data and return normalized copy of the data. 
	 * If data element in data is bigger than 255 then in copy it will be set to 255
	 * and if it is less than 0 it will be set to 0.
	 * 
	 * <p> It must be done for proper set of such data into image raster.
	 * 
	 * @param imageData data to normalize
	 * @return normalized data
	 */
	public static double[][] grayscaleNormalize(double[][] imageData) {
		int heigth = imageData.length;
		int width  = imageData[0].length;
		double[][] result = new double[heigth][width];
		for (int row = 0; row < heigth; ++row) {
			for (int col = 0; col < width; ++col) {
				double val = imageData[row][col];
				if (val > 255) {
					result[row][col] = 255;
				} else if (val < 0) {
					result[row][col] = 0;
				} else {
					result[row][col] = imageData[row][col];
				}
			}
		}
		return result;
	}
}
