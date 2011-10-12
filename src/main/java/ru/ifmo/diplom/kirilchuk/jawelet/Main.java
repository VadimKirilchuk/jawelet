package ru.ifmo.diplom.kirilchuk.jawelet;

import java.util.Arrays;

import ru.ifmo.diplom.kirilchuk.jawelet.core.dwt.transforms.legall.impl.LeGallWaveletTransform;

/**
 * 
 * @author Kirilchuk V.E.
 */
public class Main {
	public static void main(String[] args) {
		double[] x = new double[32];

		int i;
		// Makes a fancy cubic signal
		for (i = 0; i < 32; i++) {
			x[i] = i; // 5+i+0.4*i*i-0.02*i*i*i;
		}
		
		// Prints original sigal x
		System.out.println("Original signal:\n");
		for (i = 0; i < 32; i++) {
			System.out.printf("x[%d]=%f\n", i, x[i]);
		}
		System.out.println("\n");

		fwt53(x, 32);
		
		// Prints the wavelet coefficients
		System.out.println("Wavelets coefficients:\n");
		for (i = 0; i < 32; i++) {
			System.out.printf("wc[%d]=%f\n", i, x[i]);
		}
		System.out.println("\n");


		
		// Do the inverse 5/3 transform
		iwt53(x, 32);

		// Prints the reconstructed signal
		System.out.println("Reconstructed signal:\n");
		for (i = 0; i < 32; i++) {
			System.out.printf("xx[%d]=%f\n", i, x[i]);
		}
	}

	/**
	 * fwt53 - Forward biorthogonal 5/3 wavelet transform (lifting
	 * implementation)
	 * 
	 * x is an input signal, which will be replaced by its output transform. n
	 * is the length of the signal, and must be a power of 2.
	 * 
	 * The first half part of the output signal contains the approximation
	 * coefficients. The second half part contains the detail coefficients (aka.
	 * the wavelets coefficients).
	 * 
	 * See also iwt53.
	 */
	static void fwt53(double[] data, int n) {
		double coeff;
		int pos;

		// Predict 1
		coeff = -0.5;		
		for (pos = 1; pos < n - 2; pos += 2) {
			data[pos] -= ((int)(data[pos - 1] + data[pos + 1])>>1);
		}
		data[n - 1] += 2 * coeff * data[n - 2];

		// Update 1
		coeff = 0.25;
		for (pos = 2; pos < n; pos += 2) {
			data[pos] += ((int)(data[pos - 1] + data[pos + 1] + 2)>>2);
		}
		data[0] += 2 * coeff * data[1];

		// Scale
//		coeff = Math.sqrt(2.0);
//		for (pos = 0; pos < n; pos++) {
//			if (pos % 2 != 0) {
//				data[pos] *= coeff;
//			} else {
//				data[pos] /= coeff;
//			}
//		}

		// Pack
		double[] temp = new double[n];
		for (pos = 0; pos < n; pos++) {
			if (pos % 2 == 0) {
				temp[pos / 2] = data[pos];
			} else {
				temp[n / 2 + pos / 2] = data[pos];
			}
		}

		for (pos = 0; pos < n; pos++) {
			data[pos] = temp[pos];
		}
	}

	/**
	 * iwt53 - Inverse biorthogonal 5/3 wavelet transform
	 * 
	 * This is the inverse of fwt53 so that iwt53(fwt53(x,n),n)=x for every
	 * signal x of length n.
	 * 
	 * See also fwt53.
	 */
	static void iwt53(double[] data, int n) {
		double a;
		int pos;

		// Unpack
		double[] temp = new double[n];
		for (pos = 0; pos < n / 2; pos++) {
			temp[pos * 2] = data[pos];
			temp[pos * 2 + 1] = data[pos + n / 2];
		}
		for (pos = 0; pos < n; pos++)
			data[pos] = temp[pos];

		// Undo scale
//		a = 1 / Math.sqrt(2.0);
//		for (pos = 0; pos < n; pos++) {
//			if (pos % 2 != 0) {
//				data[pos] *= a;
//			} else {
//				data[pos] /= a;
//			}
//		}

		// Undo update 1
		a = -0.25;
		for (pos = 2; pos < n; pos += 2) {
			data[pos] -= ((int)(data[pos - 1] + data[pos + 1] + 2) >> 2);
		}
		data[0] += 2 * a * data[1];

		// Undo predict 1
		a = 0.5;
		for (pos = 1; pos < n - 2; pos += 2) {
			data[pos] += ((int)(data[pos - 1] + data[pos + 1])>>1);
		}
		data[n - 1] += 2 * a * data[n - 2];
	}
}
