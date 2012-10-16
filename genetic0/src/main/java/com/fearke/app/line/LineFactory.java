package com.fearke.app.line;

import java.util.Random;

import com.fearke.genetic.algorithm.Chromosome;
import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.IPhenotypeFactory;

public class LineFactory implements IPhenotypeFactory<Line> {

	private Random rnd;

	private static int maxY = 50;
	
	private int count = 50;

	public LineFactory() {
		this.rnd = new Random();
	}

	@Override
	public Line create() {
		Chromosome c = new Chromosome(count);
		init(c);
		return create(c);
	}

	@Override
	public Line create(final IChromosome c) {
		Line o = new Line(c);
		o.update();
		return o;
	}

	private void init(final Chromosome c) {
		for (int i = 0; i < c.getCount(); ++i) {
			c.setGene(i, rnd.nextInt(maxY) - maxY / 2);
		}
	}

};
