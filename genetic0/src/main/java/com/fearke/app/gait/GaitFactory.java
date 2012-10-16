package com.fearke.app.gait;

import java.util.Random;

import com.fearke.genetic.algorithm.Chromosome;
import com.fearke.genetic.model.IChromosome;
import com.fearke.genetic.model.IPhenotypeFactory;

public class GaitFactory implements IPhenotypeFactory<Gait> {

	private Random rnd;

	private int count = Gait.count * Ant.size;

	public GaitFactory() {
		this.rnd = new Random(0);
	}

	@Override
	public Gait create() {
		Chromosome c = new Chromosome(count);
		Gait gait = create(c);
		// initGait(gait);
		initGait0(gait);
		gait.update();
		return gait;
	}

	private void initGait0(Gait gait) {
		for (int i = 0; i < Gait.count; ++i) {
			gait.setStep(i, new double[] { 0, 0, 0, 0, 1, 1, 1, 1 });
		}
	}

	private void initGait(Gait gait) {
		double r = 0.6;

		double r0 = rnd.nextDouble() - 0.5;
		double r1 = rnd.nextDouble() - 0.5;
		double r2 = rnd.nextDouble() - 0.5;
		double r3 = rnd.nextDouble() - 0.5;

		int l[] = { 0, 1, 2, 3 };

		double s = r / 4;
		double q = -s / 3;

		int i0 = 0;
		for (int ll : l) {
			for (int i = 0; i < 4; ++i) {
				gait.setStep(i0 + i, new double[] { r0, r1, r2, r3, ll == 0 ? 0 : 1, ll == 1 ? 0 : 1, ll == 2 ? 0 : 1,
						ll == 3 ? 0 : 1 });

				r0 += ll == 0 ? s : q;
				r1 += ll == 1 ? s : q;
				r2 += ll == 2 ? s : q;
				r3 += ll == 3 ? s : q;
			}
			i0 += 4;
		}
	}

	@Override
	public Gait create(IChromosome c) {
		Gait gait = new Gait(c);
		gait.update();
		return gait;
	}

}
