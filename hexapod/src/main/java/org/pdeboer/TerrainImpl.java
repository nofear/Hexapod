package org.pdeboer;

import static org.pdeboer.util.PerlinNoise.*;

public class TerrainImpl implements Terrain {

	private final double noiseScale;

	public TerrainImpl(final double noiseScale) {
		this.noiseScale = noiseScale;
	}

	@Override
	public double height(
			final double x,
			final double y) {
		double xoff = (x + 1000) * noiseScale;
		double yoff = (y + 1000) * noiseScale;

		return noise(xoff, yoff) * 50;
	}

}
