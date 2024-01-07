package org.pdeboer;

import static org.pdeboer.util.PerlinNoise.*;

public class Terrain {

	public static int MESH_SIZE = 20;

	public int resX = 50;
	public int resY = 50;
	private final double[][] data;

	public Terrain(final double noiseScale) {

		this.data = new double[resX][resY];

		double xoff = 0.0;
		for (int x = 0; x < resX; x++) {
			xoff += noiseScale;
			double yoff = 0.0;
			for (int y = 0; y < resY; y++) {

				yoff += noiseScale;

				data[x][y] = noise(xoff, yoff) * 50;
			}
		}
	}

	public double[][] data() {
		return data;
	}


}
