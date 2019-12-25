/*
 * John Curran
 * 
 * These are tests for the vector class. I test the functionality of every 
 * method in the class
 */

package Tests;

import org.junit.*;
import static org.junit.Assert.*;
import Vectors.Vector;
import Vectors.Matrix;

public class VectorTests {
	//tests creating a vector with various constructors
	@Test public void test1() {
		Vector v1 = new Vector(3);
		assert(v1.length() == 3);
		
		Vector v2 = new Vector(new double[] {1, 2, 3, 4});
		assert(v2.length() == 4);
		
		Vector v3 = new Vector(v2);
		assert(v3.length() == 4);
	}
	
	//tests printing a vector
	@Test public void test2() {
		Vector v = new Vector(new double[] {1, 2, 3, 4});
		v.print();
	}
	
	//tests printing an empty vector
	@Test public void test3() {
		Vector v = new Vector(3);
		v.print();
	}
	
	//tests printing vertically
	@Test public void test4() {
		Vector v = new Vector(new double[] {1.11f, 2.5f, 3});
		v.printVertical();
	}
	
	//tests getting the value of a vector
	@Test public void test5() {
		Vector v = new Vector(new double[] {5, 13, 27, 2, 17, 0, 10});
		assert(v.length() == 7);
		assert(v.getValue(2) == 27);
		assert(v.getValue(5) == 0);
		assert(v.getValue(0) == 5);
		assert(v.getValue(6) == 10);
	}
	
	//tests simple setting of values
	@Test public void test6() {
		Vector v = new Vector(4);
		v.setValue(0, 5.5);
		v.setValue(1, 10);
		v.setValue(2, 10.1);
		v.setValue(3, 4);
		
		assert(v.getValue(0) == 5.5);
		assert(v.getValue(1) == 10);
		assert(v.getValue(2) == 10.1);
		assert(v.getValue(3) == 4);
	}
	
	//tests setting values of an already set vector
	@Test public void test7() {
		Vector v = new Vector(new double[] {5, 13, 27, 2, 17, 0, 10});
		v.setValue(0, 5.5);
		v.setValue(1, 10);
		v.setValue(2, 10.1);
		v.setValue(3, 4);
		
		assert(v.getValue(0) == 5.5);
		assert(v.getValue(1) == 10);
		assert(v.getValue(2) == 10.1);
		assert(v.getValue(3) == 4);
	}
	
	//tests that setting values on a copied vector doesn't change the original
	@Test public void test8() {
		Vector v1 = new Vector(new double[] {5, 13, 27, 2, 17, 0, 10});
		Vector v2 = new Vector(v1);
		
		v2.setValue(0, 0);
		v2.setValue(1, 10);
		v2.setValue(2, 100);
		
		assert(v1.getValue(0) == 5);
		assert(v1.getValue(1) == 13);
		assert(v1.getValue(2) == 27);
	}
	
	//tests that altering a vector doesn't alter the array it came from
	@Test public void test9() {
		double[] values = new double[] {1, 2, 3, 4};
		Vector v = new Vector(values);
		v.setValue(0, 20);
		
		assert(v.getValue(0) == 20);
		assert(values[0] == 1);
	}
	
	//tests adding two vectors together
	@Test public void test10() {
		Vector v1 = new Vector(new double[] {5, 10, 15});
		Vector v2 = new Vector(new double[] {10, 20, 30});
		
		Vector v3 = v1.add(v2);
		
		assert(v1.length() == 3);
		assert(v1.getValue(0) == 5);
		assert(v1.getValue(1) == 10);
		assert(v1.getValue(2) == 15);
		
		assert(v2.length() == 3);
		assert(v2.getValue(0) == 10);
		assert(v2.getValue(1) == 20);
		assert(v2.getValue(2) == 30);
		
		assert(v3.length() == 3);
		assert(v3.getValue(0) == 15);
		assert(v3.getValue(1) == 30);
		assert(v3.getValue(2) == 45);		
	}
	
	//tests subtracting two vectors
	@Test public void test11() {
		Vector v1 = new Vector(new double[] {5, 10, 15});
		Vector v2 = new Vector(new double[] {10, 20, 30});
		
		Vector v3 = v1.subtract(v2);
		
		assert(v1.length() == 3);
		assert(v1.getValue(0) == 5);
		assert(v1.getValue(1) == 10);
		assert(v1.getValue(2) == 15);
		
		assert(v2.length() == 3);
		assert(v2.getValue(0) == 10);
		assert(v2.getValue(1) == 20);
		assert(v2.getValue(2) == 30);
		
		assert(v3.length() == 3);
		assert(v3.getValue(0) == -5);
		assert(v3.getValue(1) == -10);
		assert(v3.getValue(2) == -15);		
	}
	
	//tests scaling a vector
	@Test public void test12() {
		Vector v1 = new Vector(new double[] {5, 10, 15});
		
		Vector v3 = v1.multiply(5);
		
		assert(v1.length() == 3);
		assert(v1.getValue(0) == 5);
		assert(v1.getValue(1) == 10);
		assert(v1.getValue(2) == 15);
		
		assert(v3.length() == 3);
		assert(v3.getValue(0) == 25);
		assert(v3.getValue(1) == 50);
		assert(v3.getValue(2) == 75);	
	}
	
	//tests comparing two equal vectors
	@Test public void test13() {
		Vector v1 = new Vector(new double[] {5, 10, 15});
		Vector v2 = new Vector(new double[] {5, 10, 15});
		
		assert(v1.equals(v2) && v2.equals(v1));
		assert(v1.equals(v1));
	}
	
	//tests comparing two unequal vectors
	@Test public void test14() {
		Vector v1 = new Vector(new double[] {5, 10, 15});
		Vector v2 = new Vector(new double[] {6, 10, 15});
		
		assert(!v1.equals(v2) && !v2.equals(v1));
	}
	
	//tests turning a vector into an array
	@Test public void test15() {
		Vector v1 = new Vector(new double[] {5, 10, 15});
		double[] vectorArray = v1.asArray();
		
		assert(vectorArray[0] == 5);
		assert(vectorArray[1] == 10);
		assert(vectorArray[2] == 15);
	}
	
	//tests altering the new array doesn't change the original vector,
	//and vice-versa
	@Test public void test16() {
		Vector v1 = new Vector(new double[] {5, 10, 15});
		double[] vectorArray = v1.asArray();
		vectorArray[0] = 0;
		vectorArray[1] = 0.5;
		vectorArray[2] = 1.2;
		
		assert(v1.getValue(0) == 5);
		assert(v1.getValue(1) == 10);
		assert(v1.getValue(2) == 15);
		
		v1.setValue(0, 0);
		v1.setValue(1, 10);
		v1.setValue(2, 20);
		
		assert(vectorArray[0] == 0);
		assert(vectorArray[1] == 0.5);
		assert(vectorArray[2] == 1.2);
	}
	
	//tests turning the vector into a matrix
	@Test public void test17() {
		Vector v = new Vector(4);
		v.setValue(0, 0);
		v.setValue(1, 1);
		v.setValue(2, 2);
		v.setValue(3, 4);
		
		Matrix m = v.asMatrix();
		assert(m.getColumn(0).equals(v));
		assert(m.columns() == 1 && m.rows() == v.length());
		assert(m.getValue(0, 0) == 0);
		assert(m.getValue(1, 0) == 1);
		assert(m.getValue(2, 0) == 2);
		assert(m.getValue(3, 0) == 4);
		
		v.setValue(3, 10);
		m.setValue(0, 0, 5);
		assert(m.getValue(3, 0) == 4);
		assert(v.getValue(0) == 0);
	}
	
	@SuppressWarnings("unused")
	//tests invalid initialization lengths
	@Test public void test18() {
		try {
			Vector v = new Vector(0);
			fail();
		} catch (Exception e) {}
		
		try {
			Vector v = new Vector(-5);
			fail();
		} catch (Exception e) {}
	}
	
	//tests getting values at invalid positions
	@Test public void test19() {
		double[] values = new double[] {1, 2, 3, 4};
		Vector v = new Vector(values);
		
		try {
			v.getValue(-1);
			fail();
		} catch (Exception e) {}
		
		try {
			v.getValue(4);
			fail();
		} catch (Exception e) {}
	}
	
	//tests setting values at invalid positions
	@Test public void test20() {
		double[] values = new double[] {1, 2, 3, 4};
		Vector v = new Vector(values);
		
		try {
			v.setValue(-1, 0);
			fail();
		} catch (Exception e) {}
		
		try {
			v.setValue(4, 0);
			fail();
		} catch (Exception e) {}
	}
	
	//tests adding together null vectors and mismatched vectors
	@Test public void test21() {
		Vector v1 = new Vector(new double[] {1, 2, 3, 0});
		Vector v2 = new Vector(3);
		Vector v3 = v2;
	
		v1 = v1.add(v2);
		assert(v1 == null);
		
		v1 = v3.add(null);
		assert(v1 == null);
	}
	
	//tests subtracting together null vectors and mismatched vectors
	@Test public void test22() {
		Vector v1 = new Vector(new double[] {1, 2, 3, 0});
		Vector v2 = new Vector(3);
		Vector v3 = v1;
			
		v1 = v1.subtract(v2);
		assert(v1 == null);
		
		v1 = v3.subtract(null);
		assert(v1 == null);
	}
}
