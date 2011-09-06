package com.fearke.genetic.model;

public interface IMutate {

	void setProbabitliy(double probability);

	double getProbability();

	void mutate(IChromosome o);

}