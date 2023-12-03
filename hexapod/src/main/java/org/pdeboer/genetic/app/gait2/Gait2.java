package org.pdeboer.genetic.app.gait2;

import org.pdeboer.genetic.algorithm.*;
import org.pdeboer.genetic.model.*;

/**
 * 
 * 12 steps, 6 legs, states: move leg, don't move leg, leg down, leg up (down
 * moves backward, up moves forward)
 * 
 * 
 * 
 * 
 * @author Patrick
 * 
 */
public class Gait2 implements IPhenotype {

	private Chromosome chromosome;
	private double fitness;

	public Gait2(final Chromosome c) {
		chromosome = c;
		fitness = Double.MAX_VALUE;
	}

	public IChromosome getChromosome() {
		return chromosome;
	}

	public double getFitness() {
		return fitness;
	}

	public void update() {
		// TODO Auto-generated method stub

	}

}
