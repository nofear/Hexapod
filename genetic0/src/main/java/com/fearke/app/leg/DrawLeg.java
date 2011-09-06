package com.fearke.app.leg;

import java.awt.Color;

import processing.core.PApplet;

import com.fearke.app.base.DrawPhenotype;
import com.fearke.genetic.model.IAlgorithm;
import com.fearke.util.Polygon2d;
import com.fearke.util.Vector2d;

public class DrawLeg extends DrawPhenotype<Leg> {

	public DrawLeg(PApplet g, IAlgorithm<Leg> algorithm) {
		super(g, algorithm);

		this.width = 200;
		this.height = 200;
	}

	protected void draw(Leg p) {
		g.fill(255);
		g.text(Double.toString(p.getFitness()), 2, height - 15);

		g.noFill();
		g.stroke(255);
		g.rect(0, 0, width, height);

		float col = 128;
		for (Vector2d[] v : p.getPath()) {
			g.stroke(col);
			line(v[0], v[1]);
			line(v[1], v[2]);

			g.stroke(255);
			rect(v[2]);
		}

		g.fill(Color.blue.getRGB());
		rect(p.p0);

		g.fill(Color.yellow.getRGB());
		rect(p.p1);

		drawBump(p);
		test(p);
	}

	public void drawBump(Leg p) {
		g.fill(200, 200, 50);
		g.beginShape();
		for (Vector2d v : p.block) {
			vertex(v);
		}

		g.endShape();
	}

	public void test(Leg p) {
		g.stroke(Color.red.getRGB());
		line(p.p0, p.p1);

		Polygon2d poly = new Polygon2d(p.block);
		Vector2d r = poly.project(p.p0);

		g.noStroke();
		g.fill(Color.green.getRGB());

		rect(r);
	}

}
