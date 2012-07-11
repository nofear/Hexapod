package com.fearke.app.gait2;

import com.fearke.app.gait.Ant;
import com.fearke.app.gait.DrawGait;
import com.fearke.app.gait.Gait;
import com.fearke.genetic.algorithm.Chromosome;

import processing.core.PApplet;

/**
 * Genetic gait
 */
public class App extends PApplet {

	/** */
	private static final long serialVersionUID = 1L;

	private Chromosome c;
	private Gait gait;
	private DrawGait drawGait;

	public static void main(String[] args) {
		// "--present",
		PApplet.main(new String[] { App.class.getName() });
	}

	@Override
	public void setup() {
		size(1024, 800);
		background(0);

		c = new Chromosome(Gait.count * Ant.size + Gait.count * 4);
		gait = new Gait(c);
		drawGait = new DrawGait(this);

		double r = 0.6;

		double r0 = -0.4;
		double r1 = 0.2;
		double r2 = 0;
		double r3 = -0.2;

		int l[] = { 0, 3, 2, 1 };

		double s = r / 4;
		double q = -s / 3;

		int i0 = 0;
		for (int ll : l) {
			for (int i = 0; i < 4; ++i) {
				gait.setStep(i0 + i, new double[] { r0, r1, r2, r3,
						ll == 0 ? 0 : 1, ll == 1 ? 0 : 1, ll == 2 ? 0 : 1,
						ll == 3 ? 0 : 1 });

				r0 += ll == 0 ? s : q;
				r1 += ll == 1 ? s : q;
				r2 += ll == 2 ? s : q;
				r3 += ll == 3 ? s : q;
			}
			i0 += 4;
		}

		gait.update();
		gait.updateDistance();
	}

	@Override
	public void draw() {
		g.pushMatrix();

		drawGait.draw(gait);

		g.popMatrix();
	}
}