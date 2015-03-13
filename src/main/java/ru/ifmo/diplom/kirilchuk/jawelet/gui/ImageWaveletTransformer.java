package ru.ifmo.diplom.kirilchuk.jawelet.gui;

import java.awt.image.BufferedImage;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.DWTransform2D;
import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.legall.impl.LeGallLiftingWaveletTransform;
import ru.ifmo.diplom.kirilchuk.util.Assert;
import ru.ifmo.diplom.kirilchuk.util.ImageUtils;

public class ImageWaveletTransformer {
	private final DWTransform2D transform = new DWTransform2D(new LeGallLiftingWaveletTransform());
	
	private TransformationResult transformationResult;
	   
	public TransformationResult decomposeTransform(BufferedImage image, int level) {
		double[][] transformationData = ImageUtils.getGrayscaleImageData(image);
		transform.decomposeInplace(transformationData, level);
		transformationResult = new TransformationResult(transformationData, level); 
		
		return transformationResult;
	}
	
	public TransformationResult reconstructTransform() {
		Assert.checkNotNull(transformationResult, "Decompose image first!");

		double[][] transformationData = transformationResult.getData();
		int level = transformationResult.getLevel();
		transformationResult = null; // to avoid reconsruction abuse
		
		
		transform.reconstructInplace(transformationData, level);
		return (new TransformationResult(transformationData, 0));
	}
	
	public final static class TransformationResult {
		private final double[][] data;
		private final int level;
		
		public TransformationResult(double[][] data, int level) {
			this.data = data;
			this.level = level;
		}

		public double[][] getData() {
			return data;
		}

		public int getLevel() {
			return level;
		}			
	}
}
