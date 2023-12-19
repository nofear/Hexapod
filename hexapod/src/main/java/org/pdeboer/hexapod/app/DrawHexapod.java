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

		drawCog(g, hexapod.getCenter());

		drawBody(g);
		drawLegs(g);
	}

	private static void drawCog(
			final PApplet g,
			final Vector3d c) {
		g.pushMatrix();
		g.fill(Color.gray.getRGB());
		translate(g, c);
		g.sphere(4);
		g.popMatrix();
	}

	private static void translate(
			final PApplet g,
			final Vector3d v) {
		g.translate((float) v.x, (float) v.y, (float) v.z);
	}

	private void drawEye(
			final PApplet g,
			final double offsetY) {
		Vector3d c = hexapod.getCenter();

		float eyeY = (float) (c.y + offsetY);
		float eyeX = (float) c.x + Hexapod.length / 2;
		float eyeZ = (float) c.z + Hexapod.height / 2;

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
		for (int i = 0; i < 6; ++i) {
			p[i] = new Vector3d();
			p[i].add(Hexapod.offset[i]);
			p[i].z = Hexapod.height / 2;
		}

		// bottom plate
		for (int i = 0; i < 6; ++i) {
			p[i + 6] = new Vector3d();
			p[i + 6].add(Hexapod.offset[i]);
			p[i + 6].z = -Hexapod.height / 2;
		}

		Matrix r = hexapod.rotationMatrix();
		for (int i = 0; i < p.length; ++i) {
			p[i] = r.multiply(p[i]);
			p[i].add(hexapod.getCenter());
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

		Vector3d c = hexapod.getCenter();
		Vector3d t = hexapod.calculateLegConfig().getGroundPlane().project(c);

		translate(g, t);
		g.fill(Color.yellow.getRGB());
		g.sphere(4);

		g.popMatrix();
	}

	void drawPlane(final PApplet g) {
		Plane3d plane = hexapod.calculateLegConfig().getGroundPlane();
		double[] r = Matrix.getRotation(plane.n);

		g.pushMatrix();
		g.rotateZ((float) r[0]);
		g.rotateY((float) r[1]);
		g.rotateX((float) r[2]);
		g.translate(0, 0, (float) plane.D + 1);
		g.fill(150, 150, 100);
		g.rect(-300, -300, 600, 600);
		g.popMatrix();
	}

	private static void vertex(
			final PApplet g,
			final Vector3d p) {
		g.vertex((float) p.x, (float) p.y, (float) p.z);
	}

	private static void vertex(
			final PApplet g,
			final Vector3d v,
			final Vector3d o) {
		var tmp = new Vector3d(v);
		tmp.add(o);
		g.vertex((float) tmp.x, (float) tmp.y, (float) tmp.z);
	}

}
