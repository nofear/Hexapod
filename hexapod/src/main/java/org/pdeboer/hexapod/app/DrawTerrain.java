package org.pdeboer.hexapod.app;

import org.pdeboer.*;
import processing.core.*;

import static processing.core.PConstants.*;

class DrawTerrain {

	private final Terrain terrain;

	DrawTerrain(final Terrain terrain) {
		this.terrain = terrain;
	}

	void draw(
			final PApplet g,
			final double xoff,
			final double yoff) {

		int width = 2000;
		int meshSize = 50;

		g.pushMatrix();

		for (int x0 = 0; x0 < width; x0 += meshSize) {
			for (int y0 = 0; y0 < width; y0 += meshSize) {

				float x1 = (float) (x0 - width / 2 + xoff);
				float y1 = (float) (y0 - width / 2 + yoff);
				float x2 = x1 + meshSize;
				float y2 = y1 + meshSize;

				float shade = 150 + (float) terrain.height(x1, y1) / 2;
				g.fill(shade, shade, shade);

				g.beginShape();
				g.vertex(x1, y1, (float) terrain.height(x1, y1));
				g.vertex(x2, y1, (float) terrain.height(x2, y1));
				g.vertex(x2, y2, (float) terrain.height(x2, y2));
				g.endShape(CLOSE);

				g.beginShape();
				g.vertex(x1, y1, (float) terrain.height(x1, y1));
				g.vertex(x1, y2, (float) terrain.height(x1, y2));
				g.vertex(x2, y2, (float) terrain.height(x2, y2));
				g.endShape(CLOSE);
			}
		}

		g.popMatrix();
	}

}
