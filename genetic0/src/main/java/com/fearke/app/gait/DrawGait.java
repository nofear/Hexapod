package com.fearke.app.gait;

import java.awt.Color;

import processing.core.PApplet;

import com.fearke.app.base.DrawPhenotype;
import com.fearke.genetic.model.IAlgorithm;
import com.fearke.util.Vector2d;

public class DrawGait extends DrawPhenotype<Gait> {

	public DrawGait(PApplet g, IAlgorithm<Gait> algorithm) {
		super(g, algorithm);

		this.width = 1000;
		this.height = 250;
	}

	protected void draw(Gait p) {
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
			drawGroundPlane(a0);
			draw(a0);

			g.translate(Ant.width + 20, 0);
		}
		g.popMatrix();

		g.translate(0, Ant.height + Ant.length * 2 + 20);

		g.pushMatrix();
		for (int i = 0; i < Gait.count * 2; ++i) {
			int index = i % Gait.count;
			Ant a0 = p.getAnt(index);
			draw(a0);

			g.translate((float) p.getDistance(index), 0);
		}
		g.popMatrix();
	}

	protected void draw(Ant a) {
		g.stroke(255);
		g.fill(Color.yellow.getRGB());
		g.rect(-Ant.width / 2, -Ant.height / 2, Ant.width, Ant.height);

		for (int i = 0; i < Ant.count; ++i) {
			g.stroke(255);
			line(a.p0[i], a.p1[i]);

			g.fill(Color.yellow.getRGB());
			rect(a.p0[i], 3);

			g.fill(a.ground[i] ? Color.green.getRGB() : Color.red.getRGB());
			g.noStroke();
			rect(a.p1[i], 3);

			// g.fill(Color.blue.getRGB());
			// g.noStroke();
			// rect(a.poly.centroid(), 3);
		}
	}

	private void drawGroundPlane(Ant a) {
		g.noStroke();
		g.fill(Color.orange.getRGB());
		g.beginShape();
		for (Vector2d p : a.poly.getPoints()) {
			g.vertex((float) p.x, (float) p.y);
		}
		g.endShape();
	}
}
