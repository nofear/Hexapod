package org.pdeboer.genetic.app.gait2;

import org.pdeboer.genetic.algorithm.*;
import org.pdeboer.genetic.app.gait.*;
import processing.core.*;

/**
 * Genetic gait
 */
public class App extends PApplet {

	private Chromosome c;
	private Gait gait;
	private DrawGait drawGait;

	public static void main(String[] args) {
		// "--present",
		PApplet.main(new String[] { App.class.getName() });
	}

	@Override
	public void settings() {
		size(1024, 800);
	}

	@Override
	public void setup() {
		background(0);

		c = new Chromosome(Gait.count * Ant.size + Gait.count * 4);
		gait = new Gait(c);
		drawGait = new DrawGait(this);

		double r = 60;

		int r0 = -40;
		int r1 = 20;
		int r2 = 0;
		int r3 = -20;

		int l[] = { 0, 3, 2, 1 };

		double s = r / 4;
		double q = -s / 3;

		int i0 = 0;
		for (int ll : l) {
			for (int i = 0; i < 4; ++i) {
				gait.setStep(i0 + i, new int[] { r0, r1, r2, r3, ll == 0 ? 0 : 1, ll == 1 ? 0 : 1, ll == 2 ? 0 : 1,
												 ll == 3 ? 0 : 1 });

				r0 += ll == 0 ? s : q;
				r1 += ll == 1 ? s : q;
				r2 += ll == 2 ? s : q;
				r3 += ll == 3 ? s : q;
			}
			i0 += 4;
		}

		gait.init();
		gait.updateDistance();
	}

	@Override
	public void draw() {
		g.pushMatrix();

		drawGait.draw(gait);

		g.popMatrix();
	}
}