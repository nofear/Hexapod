package org.pdeboer.genetic.app.base;

import org.pdeboer.genetic.model.*;
import processing.core.*;

import java.util.*;

public abstract class DrawPhenotype<T extends IPhenotype> extends DrawBase {

	protected IAlgorithm<T> algorithm;
	protected int sequence;

	protected DrawPhenotype(PApplet g, IAlgorithm<T> algorithm) {
		super(g);
		this.algorithm = algorithm;
		this.sequence = 0;
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
		int count = (int) (g.width / w2);

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
		int w = g.width;
		int h = 50;
		g.fill(0);
		g.stroke(255);
		g.rect(0, 0, w, h);

		List<Double> l = p.getFitnessHistory();

		double y1 = h;
		for (int i = 0; i < l.size(); ++i) {
			double f0 = l.get(l.size() - 1);
			double fi = l.get(i);
			g.line(i, (float) y1, i, (float) (y1 - (fi / f0) * h));
		}
	}

	protected abstract void draw(T phenotype);

}
