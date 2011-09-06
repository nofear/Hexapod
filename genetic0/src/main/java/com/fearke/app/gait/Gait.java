package com.fearke.app.gait;

import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.IPhenotype;
import com.fearke.util.Polygon2d;
import com.fearke.util.Vector2d;

public class Gait implements IPhenotype {

	public static int count = 16;

	private IChromosome chromosome;

	private Ant[] ants;
	private double[] distance;

	public Gait(final IChromosome c) {
		this.chromosome = c;
	}

	@Override
	public IChromosome getChromosome() {
		return chromosome;
	}

	@Override
	public double getFitness() {
		double fitness = 0;
		fitness += getFitnessMotion();
		fitness += getFitnessMaxRotation();
		return fitness;
	}

	private double getFitnessMotion() {
		double fitness = 0;
		for (int i = 0; i < count; ++i) {
			Ant a0 = ants[i];
			Ant a1 = ants[(i + 1) % count];

			int g = a0.getGroundSet() & a1.getGroundSet();
			int gbc = Integer.bitCount(g);
			if (gbc == 3) {
				Polygon2d gp0 = a0.getGroundPlane(g);
				Polygon2d gp1 = a1.getGroundPlane(g);

				Vector2d[] pts0 = gp0.getPoints();
				Vector2d[] pts1 = gp1.getPoints();

				double d = gp0.centroid().x - gp1.centroid().x;
				distance[i] = d;
				if (d > 0) {
					d += 1;
					d *= 10;
					fitness += 50000 - Math.abs(d * d);
				} else {
					d -= 1;
					d *= 100;
					fitness += 50000 + Math.abs(d * d);
				}

				for (int j = 0; j < pts0.length; ++j) {
					double dt = pts0[j].x - pts1[j].x;
					double diff = Math.abs(dt - distance[i]);
					diff += 1;
					fitness += diff * diff;
				}
			} else {
				int f[] = { 4, 3, 2, 1, 3 };
				fitness += 50000 * f[gbc];
			}
		}
		return fitness;
	}

	private double getFitnessMaxRotation() {
		double max = Math.PI / (2 * 10);

		double fitness = 0;
		for (int i = 0; i < count; ++i) {
			Ant a0 = ants[i];
			Ant a1 = ants[(i + 1) % count];

			for (int l = 0; l < Ant.count; ++l) {
				double diff = Math.abs(a1.rotation[l] - a0.rotation[l]);
				if (diff > max) {
					diff -= max;
					diff += 1;
					diff *= 100;
					fitness += diff * diff;
				}
			}
		}
		return fitness;
	}

	public void update() {
		distance = new double[count];
		ants = new Ant[count];
		for (int i = 0; i < count; ++i) {
			ants[i] = createAnt(i);
		}
	}

	public Ant getAnt(int idx) {
		return ants[idx];
	}

	public double getDistance(int idx) {
		return distance[idx];
	}

	private Ant createAnt(int idx) {
		double[] c = getStep(idx);
		Ant a = new Ant();
		a.set(c);
		a.update();
		return a;
	}

	private double[] getStep(int idx) {
		double[] c = new double[Ant.size];
		for (int i = 0; i < Ant.size; ++i) {
			c[i] = chromosome.getGene(i + idx * Ant.size);
		}
		return c;
	}

}
