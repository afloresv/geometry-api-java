package com.esri.core.geometry;

import static org.junit.Assert.*;
import junit.framework.TestCase;

import org.junit.Test;

public class TestExtra extends TestCase {

	@Test
	public void testPoint2D() {
		Point2D a = new Point2D(1,2),
				b = new Point2D(3,4),
				c = new Point2D(5,6),
				d = new Point2D(3,0),
				e = new Point2D(1,1),
				z = new Point2D(0,0);
		
		// SCALE
		a.scale(1,b);
		//assertTrue(a.x > b.x+0.9 && a.y > b.x+0.9);
		a.scaleAdd(1,b,c);
		//assertTrue(a.x > b.x+c.x-0.1 && a.y > b.y+c.y-0.1);

		// Normalize
		a.normalize();
		a.normalize(z);
		z.normalize();
		//assertTrue(z.x == 1 && z.y == 0);

		// NaN
		a._setNan();
		assertTrue(a._isNan());
		assertTrue(a.isNaN());

		// Others
		a.setCoords(1,2);
		a.rotateDirect(0.5,0.5);
		//assertTrue(a.x == -0.5 && a.y == -0.5);
		a.rightPerpendicular(b);
		//assertTrue(a.x == -4 && a.y == 3);
		
		a.getQuarter();
		Point2D.compareVectors(a,a);
		a.toString();
		a.offset(b,c);
		a.offset(b,b);
		a._norm(-1);
		a._norm(0);
		a._norm(1);
		a._norm(2);
		a._norm(3);
		a.inCircleRobust(b,c,d,e);
		a.calculateCircleCenterFromThreePoints(a,b,c);
		a.calculateCircleCenterFromThreePoints(z,z,z);
		a.hashCode();
	}

	@Test
	public void testPoint3D() {
		Point3D p = new Point3D(new Point3D(0.1,0.2,0.3));
		Point3D q = Point3D.construct(0.3,0.2,0.1);

		q.setZero();
		q.normalize();
		//assertTrue(q.x == -0.5 && q.y == -0.5 && q.z == 1);
		q.setCoords(p);
		q.normalize();
		double v = p.dotProduct(q);
		v = p.sqrLength();
		q.sub(q);
		p.sub(p,q);
		p.scale(1,p);
		//assertTrue(p.x == 1 && q.y == 1 && p.z == 1);
		p.mul(2);
		
		q._setNan();
		assertTrue(q._isNan());
	}
	
	@Test
	public void testTransformation2D() {

		// POSSIBLE BUG AT LINE 147

		Transformation2D
			t1 = new Transformation2D(2),
			t2 = new Transformation2D(2);
		t1.setZero();
		t1.setScale(2);

		assertTrue(t1.equals(t1));
		assertTrue(!t1.equals(new Point2D(1,1)));
		assertTrue(t1.equals(t2));

		t1.hashCode();
		Transformation2D.multiply(t1,t2,new Transformation2D());
		t1.copy();
		t1.getCoefficients(new double[6]);

		assertTrue(!t1.isIdentity());
		assertTrue(!t1.isIdentity(0.1));
		t1.isReflective();
		t1.isUniform(0.1);

		assertTrue(!t1.isShift());
		assertTrue(!t1.isShift(0.1));
		assertTrue(!t1.isOrthonormal(0.1));
		assertTrue(!t1.isDegenerate(0.1));
		assertTrue(t1.isScaleAndShift(0.1));

		t1.setFlipX(0.1,0.2);
		t1.setFlipY(0.1,0.2);
		t1.setRotate(3.14);
		t1.setRotate(0.3,0.14);
		t1.shift(1,2);
		t1.scale(1,2);
		t1.flipX(0.1,0.1);
		t1.flipY(0.1,0.1);
		t1.shear(0.1,0.1);
		t1.rotate(3.14);
		t1.rotate(0.1,0.1);
		t1.rotate(0.1,0.1,new Point2D(1,1));
		t1.inverse(t2);
		t1.inverse();
		t1.extractScaleTransform(t1,t2);
		t1.transform(new Envelope2D(1,2,3,4));
		t1.initializeFromRectIsotropic(new Envelope2D(1,2,3,4), new Envelope2D(3,4,5,6));
		t1.transformSize(new Point2D(1,1));
		
	}

	@Test
	public void testEnvelope3D () {
		Envelope3D e1, e2;
		e1 = new Envelope3D();
		e1 = Envelope3D.construct(1,2,3,4,5,6);
		e2 = new Envelope3D(e1);
		e2.setInfinite();
		
		e2.setEmpty();
		assertTrue(e2.isEmpty());
		e2.setEmptyZ();
		assertTrue(e2.isEmptyZ());
		
		e2.normalize();
		e2.merge(new Point3D(1,2,3));
		e2.merge(e1);
		e2.merge(1,2,3,4,5,6);
		
		e2.hasEmptyDimension();
		e2.setCoords(1,2,3);
		e2.setCoords(new Point3D(0,0,0),1,2,3);
		e2.getWidth();
		e2.getHeight();
		e2.getDepth();
		e2.move(new Point3D(1,2,3));
		e2.normalize();
		e2.copyTo(new Envelope2D());
		e2.inflate(1,2,3);
		
		assertTrue(e1.isIntersecting(e2));
		
		e1.intersect(e2);
		e2.contains(e1);
		e2.mergeNE(-10,-10,-10);
		e2.mergeNE(10,10,10);
		e2.inflate(-20,-20,-20);
		e2.setCoords(new Point3D(0,0,0),1,2,3);
		
		assertTrue(e1.equals(e1));
		assertTrue(!e1.equals(e2));
		assertTrue(!e1.equals(new Point3D(1,2,3)));

		e1.setEmpty();
		e2.setEmpty();
		assertTrue(e1.equals(e2));
		
		e1.construct(new Envelope1D(1,2),new Envelope1D(1,2),new Envelope1D(1,2));
		Point3D[] a = new Point3D[8];
		e1.queryCorners(a);
		e1.setFromPoints(a);
	}

	@Test
	public void testEnvelope2D () {

		Envelope2D e1, e2;
		e1 = new Envelope2D(1,2,3,4);
		e2 = new Envelope2D(e1);

		e2.setEmpty();
		assertTrue(e2.isEmpty());
		e2.setInfinite();
		assertTrue(!e2.isEmpty());

		e2 = new Envelope2D(e1);
		e2 = Envelope2D.construct(e1);
		e2 = e2.getInflated(1,2);
		e2.setCoords(new Envelope1D(1,2),new Envelope1D(1,2));
		e2.setCoords(new Envelope1D(1,2),new Envelope1D(1,2));
		e2.merge(new Point3D(1,2,3));
		e2.scale(1);
		e2.setEmpty();
		e2.getArea();
		e2.reaspect(1,1);
		e2.offset(1,1);
		e2.hashCode();
	}

	@Test
	public void testEnvelope1D () {
		Envelope1D e1, e2;
		e1 = new Envelope1D(1,3);
		e2 = new Envelope1D(2,4);

		e2.setInfinite();
		assertTrue(!e2.isEmpty());
		e2.setEmpty();
		assertTrue(e2.isEmpty());

		e1.merge(e2);
		e2.merge(e1);
		e2.setCoords(2,4);
		e1.merge(e2);
		e1.intersect(e2);
		e2.inflate(1);
		
		assertTrue(e1.equals(e1));
		assertTrue(!e1.equals(new Envelope2D(1,1,1,1)));

		e1.setEmpty();
		e2.setEmpty();
		assertTrue(e1.equals(e2));
		e1.setCoords(1,3);
		e2.setCoords(2,4);
		assertTrue(!e1.equals(e2));
		e1.hashCode();
	}

	@Test
	public void testECoordinate () {
		ECoordinate c1, c2;
		c1 = new ECoordinate(1);
		c1.setError(0.1);
		c1.resetError();
		c2 = new ECoordinate(2);
		c2.setError(0.1);
		c2.scaleError(2);
		c2.eps();
		c2.add(1);
		c1.add(c1,c2);
		c1.add(0.1,0.2);
		c1.add(c2,0.2);
		c1.add(0.2,c2);
		c1.sub(c1,c2);
		c1.sub(0.1,0.2);
		c1.sub(c2,0.2);
		c1.sub(0.2,c2);
		c1.mul(c1,c2);
		c1.mul(0.1,0.2);
		c1.mul(c2,0.2);
		c1.mul(0.2,c2);
		c1.div(c1,c2);
		c1.div(0.1,0.2);
		c1.div(c2,0.2);
		c1.div(0.2,c2);
		c1.sqrt();
		c1.sqr();
	}
}
