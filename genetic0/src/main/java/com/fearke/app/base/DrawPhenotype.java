package com.fearke.app.base;

import java.text.DecimalFormat;
import java.util.List;

import processing.core.PApplet;

import com.fearke.genetic.model.IAlgorithm;
import com.fearke.genetic.model.IPhenotype;
import com.fearke.genetic.model.IPopulation;
import com.fearke.util.Vector2d;

public abstract class DrawPhenotype<T extends IPhenotype> {

	private DecimalFormat decimalFormat;

	protected float height = 200;
	protected float width = 200;

	protected PApplet g;
	protected IAlgorithm<T> algorithm;
	protected int sequence;

	protected DrawPhenotype(PApplet g, IAlgorithm<T> algorithm) {
		this.g = g;
		this.algorithm = algorithm;
		this.sequence = 0;
		this.decimalFormat = new DecimalFormat("##.###");
	}

	public void draw() {
		for (IPopulation<T> p : algorithm.getPopulation()) {
			synchronized (p) {
				g.pushMatrix();
				draw(p);
				g.popMatrix();
				g.translate(0, height);
				drawFitness(p);
				g.translate(0, 50 + 2);
			}
		}
		sequence++;
	}

	private void draw(IPopulation<T> p) {
		float w2 = width + 2;
		int count = (int) (g.getWidth() / w2);

		g.fill(0);
		g.rect(0, 0, count * w2, height);

		g.fill(255);
		g.textAlign(PApplet.LEFT);
		g.text(p.getGeneration(), 2, height - 2);

		for (int i = 0; i < count; ++i) {
			g.pushMatrix();
			draw(p.get(i));
			g.popMatrix();
			g.translate(w2, 0);
		}

	}

	private void drawFitness(IPopulation<T> p) {
		int w = g.getWidth();
		int h = 50;
		g.fill(0);
		g.stroke(255);
		g.rect(0, 0, w, h);

		List<Double> l = p.getFitnessHistory();
		float scale = (float) w / l.size();
		double y1 = h;
		for (int i = 0; i < l.size(); ++i) {
			double f0 = l.get(0);
			double fi = l.get(i);
			g.line(i * scale, (float) y1, i * scale, (float) (y1 - (fi / f0) * h));
		}
	}

	protected abstract void draw(T phenotype);

	protected void vertex(Vector2d p) {
		g.vertex((float) p.x, (float) p.y);
	}

	protected void rect(Vector2d v) {
		rect(v, 5);
	}

	protected void rect(Vector2d v, float size) {
		g.rect((float) v.x - size / 2, (float) v.y - size / 2, size, size);
	}

	protected void line(Vector2d v0, Vector2d v1) {
		g.line((float) v0.x, (float) v0.y, (float) v1.x, (float) v1.y);
	}

	protected void text(double v, double x, double y) {
		g.text(decimalFormat.format(v), (float) x, (float) y);
	}
}
