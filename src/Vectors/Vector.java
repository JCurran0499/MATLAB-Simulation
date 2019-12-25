/*
 * John Curran
 * 
 * This class simulates a vector of numbers.
 * The vector is simply an array of numbers,
 * with certain behaviors defined by the methods
 * below, that allow it to reflect the behavior
 * of a standard vector in linear algebra. All
 * methods that return objects with return null
 * on any error, so it is the responsibility of
 * the user to check for that
 */

package Vectors;

import Precision.PreciseMath;

public class Vector {
	private double[] vector;
	private static PreciseMath p = new PreciseMath(4);
	
	//a vector can be initialized in one of three ways. First, it can be
	//defined with a length, in which it will be initially filled with 
	//zeros. Second, it can be defined using a double array, in which
	//it will take the values of the array in order. Finally, it can
	//be defined using another vector, in which it will become a
	//deep copy of that vector
	
	public Vector(int length) {
		if (length <= 0)
			throw new ArrayIndexOutOfBoundsException("Invalid Length");
		
		vector = new double[length];
	}
	
	public Vector(double[] v) {
		vector = new double[v.length];
		for (int i = 0; i < v.length; i++)
				vector[i] = v[i];
	}
	
	public Vector(Vector v) {
		vector = new double[v.length()];
		for (int i = 0; i < v.length(); i++)
			vector[i] = v.getValue(i);
	}
	
	
	//returns a vector of a given length containing only 0s
	public static Vector ZeroVector(int length) {
		if (length <= 0)
			return null;
		
		Vector v = new Vector(length);
		for (int i = 0; i < length; i++)
			v.setValue(i, 0);
		
		return v;
	}	
	
	//prints the vector our horizontally, encased in [], with commas
	//separating each value
	public void print() {
		System.out.print("[");
		for (int i = 0; i < length() - 1; i++) {
			if (vector[i] % 1 == 0)
				System.out.print((int)vector[i] + ", ");
			else
				System.out.print(vector[i] + ", ");
		}
		
		if (vector[length() - 1] % 1 == 0)
			System.out.println((int)vector[length() - 1] + "]");
		else
			System.out.println(vector[length() - 1] + "]");
		
		System.out.println();
	}
	
	//prints the vector vertically, with [] encasing each value
	public void printVertical() {
		int width = width();
		
		for (int i = 0; i < length(); i++) {
			if (vector[i] % 1 == 0) {	
				System.out.print("[");
				for (int j = Integer.toString((int)vector[i]).length(); j < width; j++)
					System.out.print(" ");
				System.out.println((int)vector[i] + "]");
			}
			else {
				System.out.print("[");
				for (int j = Double.toString(vector[i]).length(); j < width; j++)
					System.out.print(" ");
				System.out.println(vector[i] + "]");
			}
		}
		
		System.out.println();
	}
	
	//returns how many values are in the array
	public int length() {
		return vector.length;
	}
	
	//returns a value at a given index in the vector (0 is the top)
	public double getValue(int index) {
		if (index >= length() || index < 0)
			throw new ArrayIndexOutOfBoundsException("Invalid Index");
		
		return vector[index];
	}
	
	//sets an index to a given value
	public void setValue(int index, double value) {
		if (index >= length() || index < 0)
			throw new ArrayIndexOutOfBoundsException("Invalid Index");
		
		vector[index] = value;
	}
	
	//adds two vectors together 
	public Vector add(Vector v) {
		if (v == null || v.length() != length())
			return null;
		
		double[] newVector = new double[length()];
		for (int i = 0; i < length(); i++)
			newVector[i] = p.add(vector[i], v.vector[i]);
		
		return new Vector(newVector);
	}
	
	//subtracts the argument vector from the calling vector (calling - argument)
	public Vector subtract(Vector v) {
		if (v == null || v.length() != length())
			return null;
		
		double[] newVector = new double[length()];
		for (int i = 0; i < length(); i++) 
			newVector[i] = p.subtract(vector[i], v.vector[i]);
		
		return new Vector(newVector);
	}
	
	//multiplies (scales) a vector with a given scalar
	public Vector multiply(double c) {
		double[] newVector = new double[length()];
		for (int i = 0; i < length(); i++) 
			newVector[i] = p.multiply(vector[i], c);
		
		return new Vector(newVector);
	}
	
	//returns the inner product (dot product) of the calling vector and 
	//the parameter vector
	public double dot(Vector v) {
		if (v.length() != length())
			throw new ArrayIndexOutOfBoundsException("Invalid Lengths");
		
		double dot = 0;
		for (int i = 0; i < length(); i++)
			dot += p.multiply(vector[i], v.vector[i]);
		
		return dot;
	}
	
	//returns whether the vectors are orthogonal
	public boolean orthogonal(Vector v) {
		if (v.length() != length())
			throw new ArrayIndexOutOfBoundsException("Invalid Lengths");
		
		return dot(v) == 0;
	}
	
	//returns whether two vectors have identical values in identical indexes
	public boolean equals(Vector v) {
		if (v == null || v.length() != length())
			return false;
		
		for (int i = 0; i < length(); i++)
			if (v.getValue(i) != getValue(i))
				return false;
		
		return true;
	}
	
	//returns the calling vector as an independent array of its values in order
	public double[] asArray() {
		double[] array = new double[length()];
		for (int i = 0; i < length(); i++)
			array[i] = vector[i];
		
		return array;
	}
	
	//returns the vector in the form of a matrix with only one column. All
	//behavior is the same, but now functions that require matrix arguments
	//can use this vector
	public Matrix asMatrix() {
		Vector[] matrix = new Vector[] {this};
		return new Matrix(matrix);
	}
	
	
	//this helper method returns the size of the longest value in the vector,
	//and will be used for vector printing purposes
	private int width() {
		int width = 0;
		String s;
		
		for (int i = 0; i < length(); i++) {
			if (getValue(i) % 1 == 0)
				s = Integer.toString((int)getValue(i));
			else
				s = Double.toString(getValue(i));
			
			if (s.length() > width)
				width = s.length();
		}
		
		return width;
	}
}
