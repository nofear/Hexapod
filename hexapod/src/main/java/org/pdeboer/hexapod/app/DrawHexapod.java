package org.pdeboer.hexapod.app;

import org.pdeboer.hexapod.*;
import org.pdeboer.util.*;
import processing.core.*;

import java.awt.*;

import static org.pdeboer.hexapod.Hexapod.*;
import static org.pdeboer.hexapod.Leg.*;
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
		g.pushMatrix();

		translate(g, hexapod.center());

		rotate(g);

		float eyeY = (float) offsetY;
		float eyeX = (float) Hexapod.length / 2;
		float eyeZ = (float) Hexapod.height / 2;
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
		g.fill(240, 128);

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
		for (int i = 0; i < LEG_COUNT; ++i) {
			Leg leg = hexapod.getLeg(i);

			drawLegCircle(g, leg);
			drawJoints(g, leg);
			drawLeg(g, leg);
		}
	}

	private void drawJoints(
			final PApplet g,
			final Leg leg) {

		g.fill((leg.touchGround() ? Color.green : Color.orange).getRGB());

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
			final Leg leg) {
		g.noStroke();
		g.fill((leg.touchGround() ? Color.green : Color.orange).getRGB());

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

	private void drawLegCircle(
			final PApplet g,
			final Leg leg) {
		// float shade = 150 + (float) leg.moveEnd().z() / 2;
		Vector3d s = leg.moveStart();
		Vector3d e = leg.moveEnd();

		g.noFill();
		g.stroke(Color.lightGray.getRGB());

		if (leg.isMoving()) {
			g.line((float) s.x(), (float) s.y(), (float) s.z(),
				   (float) e.x(), (float) e.y(), (float) e.z());
		}

		double radius = leg.moveEnd().sub(leg.moveStart()).length();
		if (radius > 0) {
			g.pushMatrix();
			translate(g, leg.isMoving() ? s : leg.p4);

			//rotate(g);
			g.circle((float) 0, (float) 0, (float) radius * 2);
			g.popMatrix();

			g.pushMatrix();
			translate(g, e);
			// rotate(g);
			g.fill(Color.darkGray.getRGB());
			g.sphere(4);
			g.popMatrix();
		}
	}

	void drawLegFrame(final PApplet g) {
		g.pushMatrix();

		Vector3d o = new Vector3d(0, 0, 2);

		g.fill(200, 200, 50);

		g.beginShape();
		for (int i = 0; i < LEG_COUNT; ++i) {
			Leg leg = hexapod.getLeg(i);
			if (leg.touchGround()) {
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
