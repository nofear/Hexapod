package com.fearke.app.leg;

import java.util.ArrayList;
import java.util.List;

import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.IPhenotype;
import com.fearke.util.Polygon2d;
import com.fearke.util.Vector2d;

public class Leg implements IPhenotype {

	public static int stepSize = 90;
	public static double epsilon = 1E-05;
	public static double force = 2000.0;

	public static int l1 = 80;
	public static int l2 = 100;

	public Vector2d p0 = new Vector2d(25, 100);
	public Vector2d p1 = new Vector2d(175, 150);

	public Vector2d block[] = new Vector2d[] { new Vector2d(40, 155), new Vector2d(95, 135), new Vector2d(110, 160) };

	private IChromosome chromosome;
	private double fitness;
	private List<Vector2d[]> path;

	public Leg(IChromosome chromosome) {
		super();
		this.chromosome = chromosome;
		this.fitness = Double.MAX_VALUE;
	}

	public IChromosome getChromosome() {
		return chromosome;
	}

	public void update() {
		this.p0 = App.p0;

		double sumR = getFitnessStartEnd();
		double sumV = getFitnessDistance();
		double sumS = getFitnessPoly();

		fitness = sumV + sumS + sumR;
	}

	public double getFitness() {
		update();
		return fitness;
	}

	private double getFitnessStartEnd() {
		int count = chromosome.getCount() / 2;
		double r1 = p0.distance(path.get(0)[2]);
		double r2 = p1.distance(path.get(count - 1)[2]);
		return (r1 + r2) * 100;
	}

	private double getFitnessPoly() {
		Polygon2d poly = new Polygon2d(block);
		Vector2d c = poly.centroid();

		double sumS = 0;
		int count = chromosome.getCount() / 2;
		for (int i = 0; i < count; ++i) {
			Vector2d p = path.get(i)[2];
			Vector2d q = poly.project(p);
			double dq = Vector2d.sub(p, q).lengthSquared();
			if (dq <= epsilon || poly.inside(p)) {
				double dc = Vector2d.sub(p, c).lengthSquared();
				if (dc <= epsilon) {
					sumS += 2 * (force / epsilon);
				} else {
					sumS += (force / epsilon) + force / dc;
				}
			} else {
				sumS += force / dq;
			}
		}

		return sumS;
	}

	private double getFitnessCenter() {
		Vector2d c = Vector2d.add(p0, p1);
		c.scale(0.5);

		double sumS = 0;
		int count = chromosome.getCount() / 2;
		for (int i = 0; i < count; ++i) {
			Vector2d p = path.get(i)[2];
			sumS += force / p.distanceSquared(c);
		}
		return sumS;
	}

	private double getFitnessDistance() {
		double sumV = 0;
		int count = chromosome.getCount() / 2;
		for (int i = 0; i < count - 1; ++i) {
			double d2 = path.get(i)[2].distanceSquared(path.get(i + 1)[2]);
			sumV += d2 * d2;
		}
		return Math.sqrt(sumV);
	}

	public List<Vector2d[]> getPath() {
		return path;
	}

	public void init() {
		path = new ArrayList<Vector2d[]>();

		int count = chromosome.getCount() / 2;
		for (int i = 0; i < count; ++i) {
			double ra = Math.PI / stepSize * chromosome.getGene(i * 2);
			double rb = Math.PI / stepSize * chromosome.getGene(i * 2 + 1);

			Vector2d v0 = new Vector2d(150, 50);
			Vector2d v1 = Vector2d.add(v0, new Vector2d(Leg.l1, 0).rotate(ra));
			Vector2d v2 = Vector2d.add(v1, new Vector2d(Leg.l2, 0).rotate(ra + rb));
			path.add(new Vector2d[] { v0, v1, v2 });
		}
	}

}
