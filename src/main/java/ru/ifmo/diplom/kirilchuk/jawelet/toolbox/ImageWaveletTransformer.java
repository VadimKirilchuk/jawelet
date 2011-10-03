package ru.ifmo.diplom.kirilchuk.jawelet.toolbox;

import java.awt.image.BufferedImage;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.DWTransform2D;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.legall.impl.LeGallWaveletTransform;
import ru.ifmo.diplom.kirilchuk.jawelet.gui.util.ImageUtils;
import ru.ifmo.diplom.kirilchuk.jawelet.util.Assert;

public class ImageWaveletTransformer {
	private final DWTransform2D transform = new DWTransform2D(new LeGallWaveletTransform());
	
	private int originalWidth;
	private int originalHeight;
	
	private double[][] transformationData;
	   
	public TransformationResult decomposeTransform(BufferedImage image, int level) {
		originalWidth  = image.getWidth();
		originalHeight = image.getHeight();
		
		transformationData = ImageUtils.getGrayscaleImageData(image);
		transform.decomposeInplace(transformationData, level);
		
		return new TransformationResult(transformationData, 0, level);
	}
	
	public TransformationResult reconstructTransform(int fromLevel) {
		Assert.checkNotNull(transformationData, "Decompose image first!");

		transform.reconstructInplace(transformationData, fromLevel);
		return new TransformationResult(transformationData, 0, 0);
	}
	
	public final static class TransformationResult {
		private final double[][] data;
		private final long time;
		private final int level;
		
		public TransformationResult(double[][] data, int level, long time) {
			this.data = data;
			this.level = level;
			this.time = time;
		}

		public double[][] getData() {
			return data;
		}

		public long getTime() {
			return time;
		}

		public int getLevel() {
			return level;
		}			
	}
}
