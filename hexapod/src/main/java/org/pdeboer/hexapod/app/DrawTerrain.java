package org.pdeboer.hexapod.app;

import org.pdeboer.*;
import processing.core.*;

import static processing.core.PConstants.*;

class DrawTerrain {

	private final Terrain terrain;

	DrawTerrain(final Terrain terrain) {
		this.terrain = terrain;
	}

	void draw(final PApplet g) {

		int width2 = 1000 / 2;
		int meshSize = 20;

		g.pushMatrix();

		for (int x1 = -width2; x1 < width2; x1 += meshSize) {
			for (int y1 = -width2; y1 < width2; y1 += meshSize) {

				int x2 = x1 + meshSize;
				int y2 = y1 + meshSize;

				g.fill(100, 150 + (float) terrain.height(x1, y1), 50);

				g.beginShape();
				g.vertex(x1, y1, (float) terrain.height(x1, y1));
				g.vertex(x2, y1, (float) terrain.height(x2, y1));
				g.vertex(x2, y2, (float) terrain.height(x2, y2));
				g.vertex(x1, y2, (float) terrain.height(x1, y2));
				g.endShape(CLOSE);
			}
		}

		g.popMatrix();
	}

}
