package org.pdeboer;

import static org.pdeboer.util.PerlinNoise.*;

public class Terrain {

	private final double noiseScale;

	public Terrain(final double noiseScale) {
		this.noiseScale = noiseScale;
	}

	// map 0..49 -> -(resX*MESH_SIZE)/2 -> +(resX*MESH_SIZE)/2
	public double height(
			final float x,
			final float y) {
		double xoff = (x + 1000) * noiseScale;
		double yoff = (y + 1000) * noiseScale;

		return noise(xoff, yoff) * 50;
	}

}
