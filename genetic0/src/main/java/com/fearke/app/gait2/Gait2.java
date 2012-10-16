package com.fearke.app.gait2;

import com.fearke.genetic.algorithm.Chromosome;
import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.IPhenotype;

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

	@Override
	public IChromosome getChromosome() {
		return chromosome;
	}

	@Override
	public double getFitness() {
		return fitness;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub

	}

}
