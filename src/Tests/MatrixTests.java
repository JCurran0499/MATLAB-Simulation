/*
 * John Curran
 * 
 * These are tests for the vector class. I test the functionality of every 
 * method in the class
 */

package Tests;

import org.junit.*;
import Vectors.Vector;
import Vectors.Matrix;

public class MatrixTests {
	//tests creating a matrix with various constructors
	@Test public void test1() {
		Matrix m1 = new Matrix(2, 3);
		assert(m1.rows() == 2);
		assert(m1.columns() == 3);
		assert(m1.size() == 6);
		
		Matrix m2 = new Matrix(new Vector[] {new Vector(2), new Vector(2)});
		assert(m2.rows() == 2);
		assert(m2.columns() == 2);
		assert(m2.size() == 4);
		
		double[][] m = new double[3][2];
		m[0][0] = 1; m[0][1] = 5;
		m[1][0] = 4; m[1][1] = 6;
		m[2][0] = 0; m[2][1] = 9;
		Matrix m3 = new Matrix(m);
		m3.print();
		
		Matrix m4 = new Matrix(m2);
		assert(m4.rows() == 2);
		assert(m4.columns() == 2);
		assert(m4.size() == 4);
		assert(m4.equals(m2));
	}
	
	//tests printing a matrix and creating an identity matrix
	@Test public void test2() {
		Matrix identity4 = Matrix.Identity(4);
		identity4.print();
		System.out.println();
		
		Vector col1 = new Vector(new double[] {1, 2, 3, 4});
		Vector col2 = new Vector(new double[] {2, 3, 4, 5});
		Vector col3 = new Vector(new double[] {3, 4, 5, 6});
		Matrix m = new Matrix(new Vector[] {col1, col2, col3});
		m.print();
		System.out.println();
		
		assert(m.rows() == 4);
		assert(m.columns() == 3);
	}
	
	//tests whether a matrix is square or not
	@Test public void test3() {
		Vector col1 = new Vector(new double[] {1, 2, 3});
		Vector col2 = new Vector(new double[] {2, 3, 4});
		Vector col3 = new Vector(new double[] {3, 4, 5});
		
		Matrix m1 = new Matrix(new Vector[] {col1, col2, col3});		
		assert(m1.isSquare());
		
		Matrix m2 = new Matrix(new Vector[] {col1, col2});
		assert(!m2.isSquare());
	}
	
	//tests getting values at specific points in the matrix
	@Test public void test4() {
		Vector col1 = new Vector(new double[] {1, 2, 3, 4});
		Vector col2 = new Vector(new double[] {1, 2, 3, 4});
		Vector col3 = new Vector(new double[] {1, 2, 3, 4});
		
		Matrix m1 = new Matrix(new Vector[] {col1, col2, col3});
		assert(m1.getValue(1, 1) == 2);
		assert(m1.getValue(0, 0) == 1);
		assert(m1.getValue(3, 2) == 4);
		assert(m1.getValue(0, 2) == 1);
		assert(m1.getValue(1, 2) == 2);
	}
	
	//tests altering a copy matrix does not alter the original matrix, and
	//that altering a matrix's columns doesn't alter the vectors they came
	//from
	@Test public void test5() {
		Vector col1 = new Vector(new double[] {1, 2, 3, 4});
		Vector col2 = new Vector(new double[] {1, 2, 3, 4});
		Vector col3 = new Vector(new double[] {1, 2, 3, 4});
		
		Matrix m1 = new Matrix(new Vector[] {col1, col2, col3});
		Matrix m2 = new Matrix(m1);
		
		m2.setValue(0, 0, 12);
		assert(m1.getValue(0, 0) == 1);
		assert(m2.getValue(0, 0) == 12);
		
		m1.setValue(0, 0, 15);
		assert(m1.getValue(0, 0) == 15);
		assert(col1.getValue(0) == 1);
	}
	
	//tests getting a column, and that altering it doesn't alter the column
	//in the matrix
	@Test public void test6() {
		Matrix m = new Matrix(3, 2);
		m.setValue(0, 0, 5);
		m.setValue(1, 0, 6);
		m.setValue(2, 0, 7);
		
		Vector v = m.getColumn(0);
		v.setValue(0, 10);
		
		assert(v.getValue(0) == 10);
		assert(m.getValue(0, 0) == 5);		
	}
	
	//tests setting a column in the matrix
	@Test public void test7() {
		Vector col = new Vector(new double[] {0, 0, 0, 0});
		Vector col1 = new Vector(new double[] {1, 2, 3, 4});
		Vector col2 = new Vector(new double[] {1, 2, 3, 4});
		Vector col3 = new Vector(new double[] {1, 2, 3, 4});
		Matrix m = new Matrix(new Vector[] {col1, col2, col3});
		
		m.setColumn(0, col);
		m.setColumn(2, col);
		m.print();
		System.out.println();
		
		col.setValue(0, 10);
		assert(m.getValue(0, 0) == 0);
	}
	
	//tests getting a row as a vector
	@Test public void test8() {
		Vector col1 = new Vector(new double[] {1, 2, 3, 4});
		Vector col2 = new Vector(new double[] {5, 2, 3, 4});
		Vector col3 = new Vector(new double[] {7, 2, 3, 4});
		Matrix m = new Matrix(new Vector[] {col1, col2, col3});
		
		Vector row = m.rowAsVector(0);
		assert(row.length() == 3);
		assert(row.getValue(0) == 1);
		assert(row.getValue(1) == 5);
		assert(row.getValue(2) == 7);
		
		m.setValue(0, 0, 10);
		assert(row.getValue(0) == 1);
	}
	
	//tests adding two matrices together
	@Test public void test9() {
		Vector col11 = new Vector(new double[] {1, 2, 3, 4});
		Vector col12 = new Vector(new double[] {1, 2, 3, 4});
		Vector col13 = new Vector(new double[] {1, 2, 3, 4});
		Matrix m1 = new Matrix(new Vector[] {col11, col12, col13});
		
		Vector col21 = new Vector(new double[] {5, 3, 0, 0});
		Vector col22 = new Vector(new double[] {0, 0, 3, 7});
		Vector col23 = new Vector(new double[] {1, 6, 6, 0});
		Matrix m2 = new Matrix(new Vector[] {col21, col22, col23});
		
		Matrix m3 = m1.add(m2);
		m3.print();
		System.out.println();
	}
	
	//tests subtracting two matrices
	@Test public void test10() {
		Vector col11 = new Vector(new double[] {1, 2, 3, 4});
		Vector col12 = new Vector(new double[] {1, 2, 3, 4});
		Vector col13 = new Vector(new double[] {1, 2, 3, 4});
		Matrix m1 = new Matrix(new Vector[] {col11, col12, col13});
			
		Vector col21 = new Vector(new double[] {5, 3, 0, 0});
		Vector col22 = new Vector(new double[] {0, 0, 3, 7});
		Vector col23 = new Vector(new double[] {1, 6, 6, 0});
		Matrix m2 = new Matrix(new Vector[] {col21, col22, col23});
			
		Matrix m3 = m1.subtract(m2);
		m3.print();
		System.out.println();
	}
	
	//tests multiplying a matrix by a constant
	@Test public void test11() {
		Vector col1 = new Vector(new double[] {1, 2, 3, 4});
		Vector col2 = new Vector(new double[] {5, 2, 3, 4});
		Matrix m = new Matrix(new Vector[] {col1, col2});
		
		m = m.multiply(5);
		assert(m.getColumn(0).equals(new Vector(new double[] {5, 10, 15, 20})));
		assert(m.getColumn(1).equals(new Vector(new double[] {25, 10, 15, 20})));
	}
	
	//tests multiplying a matrix and a vector
	@Test public void test12() {
		Vector col1 = new Vector(new double[] {1, 2, 3, 4});
		Vector col2 = new Vector(new double[] {5, 2, 3, 4});
		
		Matrix m = new Matrix(new Vector[] {col1, col2});		
		Vector v = new Vector(new double[] {5, 4});		
		Vector result = new Vector(new double[] {25, 18, 27, 36});
		
		assert(m.multiply(v).equals(result));
		
		v = new Vector(new double[] {0, 0});
		result = new Vector(new double[] {0, 0, 0, 0});
		assert(m.multiply(v).equals(result));
	}
	
	//tests multiplying two matrices
	@Test public void test13() {
		Vector col1 = new Vector(new double[] {1, 2, 3, 4});
		Vector col2 = new Vector(new double[] {5, 2, 3, 4});
		
		Vector col3 = new Vector(new double[] {1, 2});
		Vector col4 = new Vector(new double[] {7, 3});
		Vector col5 = new Vector(new double[] {0, 0});
		Vector col6 = new Vector(new double[] {2, 2});
		Vector col7 = new Vector(new double[] {0, 2});
		
		Matrix m1 = new Matrix(new Vector[] {col1, col2});
		Matrix m2 = new Matrix(new Vector[] {col3, col4, col5, col6, col7});
		Matrix result = new Matrix(new Vector[] {new Vector(new double[] {11, 6, 9, 12}),
										   		 new Vector(new double[] {22, 20, 30, 40}),
										   		 new Vector(new double[] {0, 0, 0, 0}),
										   		 new Vector(new double[] {12, 8, 12, 16}),
										   		 new Vector(new double[] {10, 4, 6, 8}) });
		
		assert(m1.multiply(m2).equals(result));
	}
	
	//tests that multiplying by the identity matrix does not change the matrix
	@Test public void test14() {
		Vector col1 = new Vector(new double[] {1, 2, 3, 4});
		Vector col2 = new Vector(new double[] {5, 2, 3, 4});
		Vector col3 = new Vector(new double[] {1, 2, 3, 4});
		Vector col4 = new Vector(new double[] {5, 2, 3, 4});
		Matrix m = new Matrix(new Vector[] {col1, col2, col3, col4});
		
		assert(m.multiply(Matrix.Identity(4)).equals(m));
	}
	
	//tests bringing a matrix to a power
	@Test public void test15() {
		Vector col1 = new Vector(new double[] {1, 2, 3, 4});
		Vector col2 = new Vector(new double[] {5, 2, 3, 4});
		Vector col3 = new Vector(new double[] {1, 2, 3, 4});
		Vector col4 = new Vector(new double[] {5, 2, 3, 4});
		Matrix m = new Matrix(new Vector[] {col1, col2, col3, col4});
		
		Vector col5 = new Vector(new double[] {364, 248, 372, 496});
		Vector col6 = new Vector(new double[] {500, 328, 492, 656});
		Vector col7 = new Vector(new double[] {364, 248, 372, 496});
		Vector col8 = new Vector(new double[] {500, 328, 492, 656});
		Matrix result = new Matrix(new Vector[] {col5, col6, col7, col8});
		
		assert(m.toPower(3).equals(result));
	}
	
	//tests that the 0 power is the identity matrix
	@Test public void test16() {
		Vector col1 = new Vector(new double[] {1, 2, 3, 4});
		Vector col2 = new Vector(new double[] {5, 2, 3, 4});
		Vector col3 = new Vector(new double[] {1, 2, 3, 4});
		Vector col4 = new Vector(new double[] {5, 2, 3, 4});
		Matrix m = new Matrix(new Vector[] {col1, col2, col3, col4});
		
		assert(m.toPower(0).equals(Matrix.Identity(4)));
	}
	
	//tests getting the transpose of a matrix
	@Test public void test17() {
		Vector col1 = new Vector(new double[] {1, 2, 3, 4});
		Vector col2 = new Vector(new double[] {5, 2, 3, 4});
		Matrix m = new Matrix(new Vector[] {col1, col2});
		
		Vector col3 = new Vector(new double[] {1, 5});
		Vector col4 = new Vector(new double[] {2, 2});
		Vector col5 = new Vector(new double[] {3, 3});
		Vector col6 = new Vector(new double[] {4, 4});
		Matrix tp = new Matrix(new Vector[] {col3, col4, col5, col6});
		
		m.transpose();
		assert(m.transpose().equals(tp));
		assert(tp.transpose().equals(m));
	}
	
	//tests the transpose of a 1x1 matrix and the identity matrix
	@Test public void test18() {
		Matrix m = new Matrix(new Vector[] {new Vector(new double[] {1})});
		assert(m.transpose().equals(m));
		assert(Matrix.Identity(3).transpose().equals(Matrix.Identity(3)));
	}
}
