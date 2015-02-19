package com.fearke.app.gait;

import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.IPhenotype;
import com.fearke.util.Polygon2d;
import com.fearke.util.Vector2d;

public class Gait implements IPhenotype {

	public static int count = 16;

	private IChromosome chromosome;
	private double fitness;

	private Ant[] ants;
	private double[] distance;

	public Gait(final IChromosome c) {
		this.chromosome = c;
		this.fitness = Double.MAX_VALUE;
	}

	public IChromosome getChromosome() {
		return chromosome;
	}

	public void update() {
		fitness = getFitnessMotion();
	}

	public double getFitness() {
		return fitness;
	}

	/**
	 * - move 1 leg (otherwise unbalanced)<br>
	 * - up legs moves foward<br>
	 * - down legs move backward<br>
	 * - move 3 legs backward<br>
	 * - move legs consecutive times<br>
	 * - move each of 4 legs, move each leg equal steps ?<br>
	 * - 3 legs must be balanced (COG)<br>
	 * 
	 * 
	 * @return
	 */
	private double getFitnessMotion() {
		double fitness = 0;

		updateDistance();

		for (int i = 0; i < count; ++i) {
			Ant a0 = ants[i];
			Ant a1 = ants[(i + 1) % count];
			double d = distance[i];
			if (d > 0) {
				d += 1;
				d *= 10;
				fitness += 5000 - Math.abs(d * d);
			} else {
				d -= 1;
				d *= 100;
				fitness += 5000 + Math.abs(d * d);
			}

			if (Integer.bitCount(a0.getGroundSet() & a1.getGroundSet()) <= 2) {
				fitness += 20000;
			}

			Vector2d p0 = new Vector2d(0, 0);
			int g = a0.getGroundSet();
			int gbc = Integer.bitCount(g);
			if (gbc >= 3) {
				if (!a0.poly.inside(p0)) {
					fitness += 50000 * a0.poly.project(p0).distanceSquared(p0);
				}
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

	public void updateDistance() {
		for (int i = 0; i < count; ++i) {
			Ant a0 = ants[i];
			Ant a1 = ants[(i + 1) % count];

			int g = a1.getGroundSet();

			Polygon2d gp0 = a0.getGroundPlane(g);
			Polygon2d gp1 = a1.getGroundPlane(g);

			distance[i] = gp0.centroid().x - gp1.centroid().x;
		}
	}

	public void init() {
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

	public double getDistance() {
		double dist = 0;
		for (int i = 0; i < Ant.size; ++i) {
			dist += getDistance(i);
		}
		return dist;
	}

	private Ant createAnt(int idx) {
		double[] c = getStep(idx);
		Ant a = new Ant();
		a.set(c);
		a.update();
		return a;
	}

	public double[] getStep(int idx) {
		double[] c = new double[Ant.size];
		for (int i = 0; i < Ant.size; ++i) {
			c[i] = Math.PI / Ant.stepSize * chromosome.getGene(i + idx * Ant.size);
		}
		return c;
	}

	public void setStep(int idx, int[] c) {
		for (int i = 0; i < Ant.size; ++i) {
			chromosome.setGene(i + idx * Ant.size, c[i]);
		}
	}
}
