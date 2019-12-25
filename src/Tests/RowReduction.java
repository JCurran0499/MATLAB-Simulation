package Tests;

import org.junit.*;
import Vectors.Vector;
import Vectors.Matrix;

public class RowReduction {
	
	@Test public void test1() {
		Matrix m = new Matrix(new Vector[] {new Vector(new double[] {0, 0, 1}),
		   		 						    new Vector(new double[] {1, 1, 5}),
		   		 						    new Vector(new double[] {5, 4, 0}),
		   		 						    new Vector(new double[] {0, 6, 0}),
		   		 						    new Vector(new double[] {8, 0, 0}),
		   		 						    new Vector(new double[] {0, 0, 0})});
		
		m.print();
		//m.rref().print();
	}
	
	@Test public void test2() {
		Matrix A = new Matrix(new Vector[] {new Vector(new double[] {3, -3, 6}),
				    						new Vector(new double[] {5, -2, 1}),
				    						new Vector(new double[] {-4, 4, -8})});
		
		Vector b = new Vector(new double[] {7, -1, -4});

		Vector x = A.AxEqualsB(b);
		assert(A.multiply(x).equals(b));
	}
	
	@Test public void test3() {
		Matrix A = new Matrix(new Vector[] {new Vector(new double[] {3, -3, 6}),
											new Vector(new double[] {5, -2, 1}),
											new Vector(new double[] {-4, 4, -8})});
		
		Double x = 5.000;
		System.out.println(x.intValue());
	}

}
