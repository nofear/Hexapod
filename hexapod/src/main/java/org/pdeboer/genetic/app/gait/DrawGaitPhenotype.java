package org.pdeboer.genetic.app.gait;

import org.pdeboer.genetic.app.base.*;
import org.pdeboer.genetic.model.*;
import processing.core.*;

public class DrawGaitPhenotype extends DrawPhenotype<Gait> {

	private DrawGait drawGait;

	public DrawGaitPhenotype(PApplet g, IAlgorithm<Gait> algorithm) {
		super(g, algorithm);

		drawGait = new DrawGait(g);

		this.width = 1000;
		this.height = 250;
	}

	@Override
	protected void draw(Gait p) {
		drawGait.draw(p);
	}
}
