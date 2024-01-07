package org.pdeboer.hexapod.app;

import org.pdeboer.hexapod.*;
import org.pdeboer.util.*;
import processing.core.*;

import java.awt.*;

import static org.pdeboer.hexapod.Hexapod.*;
import static processing.core.PConstants.*;

class DrawHexapod {

	private static final int jointSize = 6;

	private final Hexapod hexapod;

	DrawHexapod(final Hexapod hexapod) {
		this.hexapod = hexapod;
	}

	public void draw(final PApplet g) {
		drawEye(g, -10);
		drawEye(g, 10);

		drawCog(g, hexapod.center());

		drawBody(g);
		drawLegs(g);
	}

	private static void drawCog(
			final PApplet g,
			final Vector3d c) {
		g.pushMatrix();
		g.fill(Color.orange.getRGB());
		translate(g, c);
		g.sphere(5);
		g.popMatrix();
	}

	static void translate(
			final PApplet g,
			final Vector3d v) {
		g.translate((float) v.x(), (float) v.y(), (float) v.z());
	}

	private void drawEye(
			final PApplet g,
			final double offsetY) {
		Vector3d c = hexapod.center();

		float eyeY = (float) (c.y() + offsetY);
		float eyeX = (float) c.x() + Hexapod.length / 2;
		float eyeZ = (float) c.z() + Hexapod.height / 2;

		g.pushMatrix();

		rotate(g);

		g.translate(eyeX, eyeY, eyeZ);

		g.fill(Color.blue.getRGB());
		g.sphere(6);
		g.popMatrix();
	}

	private void rotate(final PApplet g) {
		double[] r = hexapod.rotation();
		g.rotateX((float) -r[ROLL]);
		g.rotateY((float) -r[PITCH]);
		g.rotateZ((float) -r[YAW]);
	}

	private void drawBody(final PApplet g) {
		g.stroke(0);
		g.strokeWeight(2);
		g.pushMatrix();
		g.fill(240);
		g.noFill();

		Vector3d[] p = new Vector3d[12];

		// top plate
		int height2 = height / 2;
		for (int i = 0; i < 6; ++i) {
			p[i] = offset[i].add(new Vector3d(0, 0, height));
		}

		// bottom plate
		for (int i = 0; i < 6; ++i) {
			p[i + 6] = offset[i].add(new Vector3d(0, 0, 0));
		}

		Rotation3D r = hexapod.rotationMatrix();
		for (int i = 0; i < p.length; ++i) {
			p[i] = r.apply(p[i]).add(hexapod.center());
		}

		g.beginShape();
		for (int i = 0; i < 6; ++i) {
			vertex(g, p[i]);
		}
		g.endShape();

		g.beginShape();
		for (int i = 6; i < 12; ++i) {
			vertex(g, p[i]);
		}
		g.endShape();

		g.beginShape(QUADS);
		for (int i = 0; i < 5; ++i) {
			int i0 = i;
			int i1 = i0 + 1;
			int i2 = (i0 + 7) % 12;
			int i3 = (i0 + 6) % 12;
			vertex(g, p[i0]);
			vertex(g, p[i1]);
			vertex(g, p[i2]);
			vertex(g, p[i3]);
		}

		int i0 = 0;
		int i1 = 5;
		int i2 = 11;
		int i3 = 6;
		vertex(g, p[i0]);
		vertex(g, p[i1]);
		vertex(g, p[i2]);
		vertex(g, p[i3]);
		g.endShape();

		g.popMatrix();
	}

	private void drawLegs(final PApplet g) {
		LegConfig lc = hexapod.calculateLegConfig();
		for (int i = 0; i < LEG_COUNT; ++i) {
			g.fill((lc.touchGround(i) ? Color.green : Color.orange).getRGB());

			drawJoints(g, i);
			drawLeg(g, i);
		}
	}

	private void drawJoints(
			final PApplet g,
			final int index) {
		Leg leg = hexapod.getLeg(index);

		g.noStroke();
		g.pushMatrix();
		translate(g, leg.p1);
		g.sphere(jointSize);
		g.popMatrix();
		g.pushMatrix();
		translate(g, leg.p2);
		g.sphere(jointSize);
		g.popMatrix();
		g.pushMatrix();
		translate(g, leg.p3);
		g.sphere(jointSize);
		g.popMatrix();
		g.pushMatrix();
		translate(g, leg.p4);
		g.sphere(jointSize);
		g.popMatrix();
	}

	private void drawLeg(
			final PApplet g,
			final int index) {
		Leg leg = hexapod.getLeg(index);
		double[] r = hexapod.rotation();

		g.pushMatrix();
		translate(g, leg.p1);
		rotate(g);
		g.rotateZ((float) (PI / 2 - leg.getRa()));

		float lengthCoxa = (float) leg.lengthCoxa();
		float lengthFemur = (float) leg.lengthFemur();
		float lengthTibia = (float) leg.lengthTibia();

		g.translate(lengthCoxa / 2, 0, 0);
		g.box(lengthCoxa, 4, 6);
		g.translate(lengthCoxa / 2, 0, 0);

		g.rotateY(-(PI / 2));
		g.rotateY((float) leg.getRb());

		g.translate(lengthFemur / 2, 0, 0);
		g.box(lengthFemur, 4, 6);
		g.translate(lengthFemur / 2, 0, 0);

		g.rotateY((float) (leg.getRc()));

		g.translate(lengthTibia / 2, 0, 0);
		g.box(lengthTibia, 4, 6);

		g.popMatrix();
	}

	void drawLegFrame(final PApplet g) {
		g.pushMatrix();

		Vector3d o = new Vector3d(0, 0, 2);

		g.fill(200, 200, 50);

		g.beginShape();
		LegConfig lc = hexapod.calculateLegConfig();
		for (int i = 0; i < LEG_COUNT; ++i) {
			Leg leg = hexapod.getLeg(i);
			if (lc.touchGround(i)) {
				vertex(g, leg.p4, o);
			}
		}
		g.endShape();

		g.popMatrix();
	}

	private static void vertex(
			final PApplet g,
			final Vector3d p) {
		g.vertex((float) p.x(), (float) p.y(), (float) p.z());
	}

	private static void vertex(
			final PApplet g,
			final Vector3d v,
			final Vector3d o) {
		var tmp = v.add(o);
		g.vertex((float) tmp.x(), (float) tmp.y(), (float) tmp.z());
	}

}
