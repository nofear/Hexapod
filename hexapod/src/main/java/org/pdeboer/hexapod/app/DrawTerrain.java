package org.pdeboer.hexapod.app;

import org.pdeboer.*;
import processing.core.*;

import static processing.core.PConstants.*;

public class DrawTerrain {

	private final Terrain terrain;

	public DrawTerrain(final Terrain terrain) {
		this.terrain = terrain;
	}

	void draw(final PApplet g) {
		int meshSize = Terrain.MESH_SIZE;
		int resX = terrain.resX;
		int resY = terrain.resY;

		double[][] val = terrain.data();

		g.pushMatrix();
		g.translate(-resX / 2 * meshSize, -resY / 2 * meshSize);

		//g.background(0);

		for (int x = 0; x < resX - 1; x++) {
			for (int y = 0; y < resY - 1; y++) {
				g.beginShape();
				g.fill(0, 150 + (float) val[x][y], 0);

				g.vertex(x * meshSize, y * meshSize, (float) val[x][y]);
				g.vertex((x + 1) * meshSize, y * meshSize, (float) val[x + 1][y]);
				g.vertex((x + 1) * meshSize, (y + 1) * meshSize, (float) val[x + 1][y + 1]);
				g.vertex(x * meshSize, (y + 1) * meshSize, (float) val[x][y + 1]);

				g.endShape(CLOSE);
			}
		}

		g.popMatrix();
	}

}
