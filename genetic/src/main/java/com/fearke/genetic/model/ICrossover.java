package com.fearke.genetic.model;

public interface ICrossover {

	void setProbabitliy(double probability);

	double getProbability();
	
	IChromosome[] crossover(IChromosome io1, IChromosome io2);

}