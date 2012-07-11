package com.fearke.app.gait;

import processing.core.PApplet;

import com.fearke.app.base.DrawPhenotype;
import com.fearke.genetic.model.IAlgorithm;

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
