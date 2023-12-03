package org.pdeboer.genetic.app.line;

import org.pdeboer.genetic.algorithm.*;
import org.pdeboer.genetic.model.*;

import java.util.*;

public class LineFactory implements IPhenotypeFactory<Line> {

	private Random rnd;

	private static int maxY = 50;
	
	private int count = 50;

	public LineFactory() {
		this.rnd = new Random();
	}

	public Line create() {
		Chromosome c = new Chromosome(count);
		init(c);
		return create(c);
	}

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
