package org.pdeboer.util;

import java.util.*;

public class PerlinNoise {

	private static final int P = 512;
	private static final int[] permutation = new int[P * 2];

	static {
		Random rand = new Random(0);

		// Generate a random permutation
		for (int i = 0; i < P; i++) {
			permutation[i] = i;
		}

		for (int i = 0; i < P; i++) {
			int j = rand.nextInt(P);
			int temp = permutation[i];
			permutation[i] = permutation[j];
			permutation[j] = temp;
			permutation[i + P] = permutation[i];
		}
	}

	public static double noise(
			final double x_,
			final double y_) {
		double x = x_;
		double y = y_;

		int X = (int) Math.floor(x) & 255;
		int Y = (int) Math.floor(y) & 255;

		x -= Math.floor(x);
		y -= Math.floor(y);

		double u = fade(x);
		double v = fade(y);

		int A = permutation[X] + Y;
		int AA = permutation[A];
		int AB = permutation[A + 1];
		int B = permutation[X + 1] + Y;
		int BA = permutation[B];
		int BB = permutation[B + 1];

		return lerp(v, lerp(u, grad(permutation[AA], x, y), grad(permutation[BA], x - 1, y)),
					lerp(u, grad(permutation[AB], x, y - 1), grad(permutation[BB], x - 1, y - 1)));
	}

	private static double fade(double t) {
		return t * t * t * (t * (t * 6 - 15) + 10);
	}

	private static double lerp(
			double t,
			double a,
			double b) {
		return a + t * (b - a);
	}

	private static double grad(
			int hash,
			double x,
			double y) {
		int h = hash & 15;
		double grad = 1 + (h & 7); // Gradient value 1-8

		if ((h & 8) != 0) {
			grad = -grad; // Randomly invert half of the gradients
		}

		return (grad * x) + (h < 4 ? 0 : h == 12 || h == 14 ? y : 0);
	}

	private PerlinNoise() {}
}
