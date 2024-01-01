package org.pdeboer.genetic.app.gait;

import org.pdeboer.genetic.app.base.*;
import org.pdeboer.util.*;
import processing.core.*;

import java.awt.*;

public class DrawGait extends DrawBase {

	public DrawGait(PApplet g) {
		super(g);

		this.width = 1000;
		this.height = 250;
	}

	public void draw(Gait p) {
		g.fill(255);
		g.text(Double.toString(p.getFitness()), 80, height - 2);

		g.noFill();
		g.stroke(255);
		g.rect(0, 0, width, height);

		float offset = Ant.height / 2 + Ant.length + 20;

		g.translate(Ant.width / 2 + 20, offset);
		g.pushMatrix();
		for (int i = 0; i < Gait.count; ++i) {
			g.fill(255);
			text(p.getDistance(i), -25, -offset + 15);

			Ant a0 = p.getAnt(i);
			Ant a1 = p.getAnt((i + 1) % Gait.count);

			drawGroundPlane(a0);
			draw(a0, a1);

			g.translate(Ant.width + 20, 0);
		}
		g.popMatrix();

		g.translate(0, Ant.height + Ant.length * 2 + 20);

		g.pushMatrix();
		for (int i = 0; i < Gait.count * 2; ++i) {
			int index = i % Gait.count;
			Ant a0 = p.getAnt(index);
			draw(a0, a0);

			g.translate((float) p.getDistance(index), 0);
		}
		g.popMatrix();
	}

	private void draw(Ant a0, Ant a1) {
		g.stroke(255);
		g.fill(Color.yellow.getRGB());
		g.rect(-Ant.width / 2, -Ant.height / 2, Ant.width, Ant.height);

		for (int i = 0; i < Ant.count; ++i) {
			g.stroke(a0.ground[i] ? Color.green.getRGB() : Color.red.getRGB());
			line(a0.p0[i], a0.p1[i]);

			g.stroke(a0.ground[i] ? Color.blue.getRGB() : Color.orange.getRGB());
			line(a1.p0[i], a1.p1[i]);

			g.fill(Color.yellow.getRGB());
			rect(a0.p0[i], 3);

			g.fill(a0.ground[i] ? Color.green.getRGB() : Color.red.getRGB());
			g.noStroke();
			rect(a0.p1[i], 3);

			g.fill(a0.poly.inside(new Vector2d(0, 0)) ? Color.blue.getRGB()
													  : Color.red.getRGB());
			g.noStroke();
			rect(new Vector2d(0, 0), 3);
		}
	}

	private void drawGroundPlane(Ant a) {
		g.noStroke();
		g.fill(Color.orange.getRGB());
		g.beginShape();
		for (Vector2d p : a.poly.getPoints()) {
			g.vertex((float) p.x(), (float) p.y());
		}
		g.endShape();
	}

}
