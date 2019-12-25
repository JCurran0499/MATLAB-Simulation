/*
 * John Curran
 * 
 * This class simulates a matrix of numbers.
 * A matrix is simulated with an array of vectors,
 * and the methods below define the behavior of
 * the matrix. In this case, each column in a 
 * matrix is a vector in the array (not each row),
 * so the vectors are read top to bottom, not left
 * to right. All methods that return objects will 
 * return null on an error, so it is the responsibility
 * of the user to check for that if they think there
 * might be an error
 */

package Vectors;

import Precision.PreciseMath;

public class Matrix {
	private Vector[] matrix;
	private static PreciseMath p = new PreciseMath(4);
	
	//Just like a vector, a matrix can be initialized in one of
	//four ways. First, it can be defined using dimensions, which
	//will initialize it to a 0 matrix with those dimensions. Second,
	//can be defined with an array of vectors, in which it will
	//take in the vectors in the array. Third, it can be defined as a 
	//two dimensional array, with the array designed as array[row][column]
	//Finally, it be defined as a deep copy of another matrix
	
	public Matrix(int rows, int cols) {
		if (rows <= 0 || cols <= 0)
			throw new ArrayIndexOutOfBoundsException("Invalid Dimensions");
		
		matrix = new Vector[cols];
		for (int i = 0; i < cols; i++)
			matrix[i] = new Vector(rows);
	}
	
	public Matrix(Vector[] m) {
		if (m == null)
			throw new ArrayIndexOutOfBoundsException("Invalid");
		
		int length = m[0].length(); //must be constant length
		matrix = new Vector[m.length];
		for (int i = 0; i < m.length; i++) {
			if (m[i].length() != length)
				throw new ArrayIndexOutOfBoundsException("Invalid Dimensions");
			
			matrix[i] = new Vector(m[i]);
		}
	}
	
	public Matrix(double[][] m) {
		if (m == null)
			throw new ArrayIndexOutOfBoundsException("Invalid");
		
		for (double[] v : m)
			if (v == null)
				throw new ArrayIndexOutOfBoundsException("Invalid");
			else if (v.length != m[0].length)
				throw new ArrayIndexOutOfBoundsException("Invalid Dimensions");
		
		matrix = new Vector[m.length];
		for (int i = 0; i < m.length; i++) {
			matrix[i] = new Vector(m[i]);
		}
		
		matrix = transpose().matrix;
	}
	
	public Matrix(Matrix m) {
		if (m == null)
			throw new ArrayIndexOutOfBoundsException("Invalid Matrix");
		
		matrix = new Vector[m.columns()];
		for (int i = 0; i < m.columns(); i++)
			matrix[i] = new Vector(m.getColumn(i));
	}
	
	//this static method will return the identity matrix of the 
	//given dimension
	public static Matrix Identity(int dimensions) {
		if (dimensions <= 0)
			return null;
		
		Vector[] m = new Vector[dimensions];
		for (int i = 0; i < dimensions; i++) {
			m[i] = new Vector(dimensions);
			m[i].setValue(i, 1);
		}
		
		return new Matrix(m);
	}
	
	//this will print the entire matrix out. It will be encased in [],
	//and the vectors it is made up of will be the columns. Values in the
	//matrix will be separated by spaces
	public void print() {
		int[] widths = new int[columns()];
		for (int i = 0; i < columns(); i++)
			widths[i] = width(matrix[i]);
		
		for (int i = 0; i < rows(); i++) {
			System.out.print("[");
			for (int j = 0; j < columns(); j++) {
				
				if (matrix[j].getValue(i) % 1 == 0) {
					for (int k = Integer.toString((int)getValue(i, j)).length(); k < widths[j]; k++)
							System.out.print(" ");
					System.out.print((int)matrix[j].getValue(i));					
				}
				else {
					for (int k = Double.toString(getValue(i, j)).length(); k < widths[j]; k++)
						System.out.print(" ");
					System.out.print(matrix[j].getValue(i));
				}
				
				if (j + 1 < columns())
					System.out.print("  ");
			}
			
			System.out.println("]");
		}
		
		System.out.println();
	}
	
	//this method returns the number of rows in the matrix
	public int rows() {
		return matrix[0].length();
	}
	
	//this method returns the number of columns in the matrix
	public int columns() {
		return matrix.length;
	}
	
	//returns how many total values exist in the matrix
	public int size() {
		return rows() * columns();
	}
	
	//returns whether it is a square matrix
	public boolean isSquare() {
		return (rows() == columns());
	}
	
	//return whether the matrix is symmetrical
	public boolean symmetrical() {
		return equals(transpose());
	}
	
	//gets a value in the matrix from a given column and row (both start
	//at 0)
	public double getValue(int row, int column) {
		if (row >= rows() || column >= columns() ||
				row < 0 || column < 0)
			throw new ArrayIndexOutOfBoundsException("Invalid Index");
		
		return matrix[column].getValue(row);
	}
	
	//returns an entire column in the form of a vector
	public Vector getColumn(int column) {
		if (column >= columns() || column < 0)
			return null;
		
		return new Vector(matrix[column]);
	}
	
	//sets a value at a given row and column position in the matrix
	public void setValue(int row, int column, double value) {
		if (row >= rows() || column >= columns() ||
				row < 0 || column < 0)
			throw new ArrayIndexOutOfBoundsException("Invalid Index");
		
		matrix[column].setValue(row, value);
	}
	
	//sets a column in the matrix to a given vector
	public void setColumn(int column, Vector v) {
		if (v == null || column >= columns() || column < 0)
			throw new ArrayIndexOutOfBoundsException("Invalid Index");
		
		matrix[column] = new Vector(v);
	}
	
	//returns a row in the matrix as its own independent vector
	public Vector rowAsVector(int row) {
		if (row >= rows() || row < 0)
			return null;
		
		double[] newVector = new double[columns()];
		for (int i = 0; i < columns(); i++)
			newVector[i] = matrix[i].getValue(row);
		
		return new Vector(newVector);
	}
	
	//adds a matrix to the calling matrix, similar to how a vector performs it
	public Matrix add(Matrix m) {
		if (m == null || m.rows() != rows() || m.columns() != columns())
			return null;
		
		Vector[] newMatrix = new Vector[columns()];
		for (int i = 0; i < columns(); i++)
			newMatrix[i] = matrix[i].add(m.matrix[i]);
		
		return new Matrix(newMatrix);
	}
	
	public Matrix subtract(Matrix m) {
		if (m == null || m.rows() != rows() || m.columns() != columns())
			return null;
		
		Vector[] newMatrix = new Vector[columns()];
		for (int i = 0; i < columns(); i++)
			newMatrix[i] = matrix[i].subtract(m.matrix[i]);
		
		return new Matrix(newMatrix);
	}
	
	//multiplies the matrix by a given scalar
	public Matrix multiply(double c) {
		Vector[] newMatrix = new Vector[columns()];
		for (int i = 0; i < columns(); i++)
			newMatrix[i] = getColumn(i).multiply(c);
		
		return new Matrix(newMatrix);
	}
	
	//multiples a vector to the matrix (Matrix * vector), returning
	//the resulting vector
	public Vector multiply(Vector v) {
		if (columns() != v.length())
			return null;
		
		double[] vector = new double[rows()];
		for (int i = 0; i < rows(); i++) 
			for (int j = 0; j < columns(); j++)
				vector[i] += p.multiply(v.getValue(j), getValue(i, j));		
		
		return new Vector(vector);
	}
	
	//multiples a matrix to the calling matrix (Calling * Argument), returning
	//the resulting matrix
	public Matrix multiply(Matrix m) {
		if (columns() != m.rows())
			return null;
		
		Vector[] newMatrix = new Vector[m.columns()];
		for (int i = 0; i < m.columns(); i++) 
			newMatrix[i] = multiply(m.matrix[i]);
		
		return new Matrix(newMatrix);
	}
	
	//takes the calling matrix to the given power (Matrix^power)
	public Matrix toPower(int power) {
		if (!isSquare() || power < 0)
			return null;
		
		Matrix newMatrix = Identity(rows());
		for (int i = 0; i < power; i++)
			newMatrix = multiply(newMatrix);
		
		return newMatrix;
	}
	
	//returns whether the calling matrix shares identical values in identical
	//positions across the entire array
	public boolean equals(Matrix m) {
		if (m == null || m.rows() != rows() || m.columns() != columns())
			return false;
		
		for (int i = 0; i < columns(); i++)
			if (!matrix[i].equals(m.matrix[i]))
				return false;
		
		return true;
	}
	
	//returns the transpose of the matrix
	public Matrix transpose() {
		Vector[] newMatrix = new Vector[rows()];
		for (int i = 0; i < rows(); i++)
			newMatrix[i] = rowAsVector(i);
		
		return new Matrix(newMatrix);
	}
	
	//returns the matrix as a two dimensional array of values. Getting a value
	//in the array needs to be done using array[row][column]
	public double[][] asArray() {
		double[][] array = new double[rows()][columns()];
		for (int i = 0; i < rows(); i++)
			array[i] = rowAsVector(i).asArray();
		
		return array;
	}
	
	//returns the matrix in the form of a vector. All
	//behavior is the same, but now functions that require vector
	//arguments can use this matrix. In order for this to work, the
	//calling matrix must only have one column
	public Vector asVector() {
		if (columns() != 1)
			return null;
		
		return new Vector(getColumn(0));
	}
	
	
	//adds another column to the matrix, making it an augmented matrix
	public Matrix augment(Vector v) {
		if (v.length() != rows())
			return null;
		
		Vector[] newMatrix = new Vector[columns() + 1];
		for (int i = 0; i < columns(); i++)
			newMatrix[i] = matrix[i];
		newMatrix[columns()] = v;
		
		return new Matrix(newMatrix);
	}
	
	//adds multiple columns to the matrix, making it an augmented matrix
	public Matrix augment(Matrix m) {
		if (m.rows() != rows())
			return null;
		
		Vector[] newMatrix = new Vector[columns() + m.columns()];
		for (int i = 0; i < columns(); i++)
			newMatrix[i] = matrix[i];

		for (int i = 0; i < m.columns(); i++)
			newMatrix[i + columns()] = m.matrix[i];
			
		return new Matrix(newMatrix);
	}
	
	//this method reduces the matrix into row reduced echelon form
	public Matrix rref() {
		Matrix m = transpose(); //it is simpler to work with columns rather than rows
		double scale;
		int indexRow = 0, indexCol = 0;
		
		int rows = m.rows(), columns = m.columns();
		while (indexCol < columns && indexRow < rows) {
			if (!zeroFromPoint(m.rowAsVector(indexRow), indexCol, 1)) {
				if (m.getValue(indexRow, indexCol) == 0) {
					int j  = indexCol + 1;
					Vector v;
				
					while (m.getValue(indexRow, j) == 0)
						j++;
					v = m.getColumn(j); //row swap
					m.setColumn(j, m.getColumn(indexCol));
					m.setColumn(indexCol, v);
				}
				
				for (int i = 0; i < columns; i++)  //row reduce
					if (i != indexCol && m.getValue(indexRow, i) != 0) {
						scale = -(p.divide(m.getValue(indexRow, i), m.getValue(indexRow, indexCol)));
						m.setColumn(i, m.getColumn(indexCol).multiply(scale).add(m.getColumn(i)));
					}
				
				if (m.getValue(indexRow, indexCol) != 1) { //scale the row
					scale = p.divide(1, m.getValue(indexRow, indexCol));
					m.setColumn(indexCol, m.getColumn(indexCol).multiply(scale));
				}
				
				indexCol++;
			}
			
			indexRow++;
		}		
		
		return m.transpose();
	}
	
	//returns the determinant of the matrix
	public double determinant() {
		if (!isSquare())
			return 0f;
		
		double determinant = 1;
		
		Matrix m = transpose(); //it is simpler to work with columns rather than rows
		double scale; 
		int indexRow = 0, indexCol = 0;
		
		int rows = m.rows(), columns = m.columns();
		while (indexCol < columns && indexRow < rows) {
			if (!zeroFromPoint(m.rowAsVector(indexRow), indexCol, 1)) {
				if (m.getValue(indexRow, indexCol) == 0) {
					int j  = indexCol + 1;
					Vector v;
				
					while (m.getValue(indexRow, j) == 0)
						j++;
					v = m.getColumn(j); //row swap
					m.setColumn(j, m.getColumn(0));
					m.setColumn(0, v);
					determinant = -determinant;
				}
				
				for (int i = 0; i < columns; i++)  //row reduce
					if (i != indexCol && m.getValue(indexRow, i) != 0) {
						scale = -(p.divide(m.getValue(indexRow, i), m.getValue(indexRow, indexCol)));
						m.setColumn(i, m.getColumn(indexCol).multiply(scale).add(m.getColumn(i)));
					}
				
				if (m.getValue(indexRow, indexCol) != 1) { //scale the row
					scale = p.divide(1, m.getValue(indexRow, indexCol));
					m.setColumn(indexCol, m.getColumn(indexCol).multiply(scale));
					
					determinant = determinant / scale;
				}
				
				indexCol++;
			}
			
			indexRow++;
		}
		
		if (!m.transpose().equals(Identity(rows)))
			return 0f;
		
		return determinant;
	}
	
	//returns whether the matrix is invertible
	public boolean invertible() {
		return determinant() != 0;
	}
	
	//returns the inverse of the given matrix
	public Matrix invert() {
		if (!invertible())
			return null;
		
		int columns = columns();
		Matrix m = augment(Identity(columns)).rref();
		
		Vector[] newMatrix = new Vector[columns];
		for (int i = columns; i < m.columns(); i++) {
			newMatrix[i - columns] = m.getColumn(i);
		}		
		
		return new Matrix(newMatrix);
	}
	
	//solves for x in the equation Ax=b, with A being the calling matrix
	//and b being the argument vector. Since many equations like this have
	//infinite answers, this gives the answer in which all free variables
	//equal 1
	public Vector AxEqualsB(Vector b) {
		if (b.length() != rows())
			return null;
		
		double value;
		double[] v = new double[columns()];
		Matrix a = augment(b).rref();
		
		int rows = a.rows();
		int cols = a.columns();
		
		for (int i = 0; i < rows; i++) {
			if (a.getValue(i, cols - 1) != 0 && zeroFromPoint(a.rowAsVector(i), cols - 1, -1))
				return null;
		}
		
		int indexRow = 0, indexCol = 0;
		while (indexRow < rows && indexCol < cols - 1) {
			if (a.getValue(indexRow, indexCol) == 1) {
				
				for (int i = indexCol + 1; i < cols - 1; i++) {
					value = a.getValue(indexRow, i);
					if (value != 0)
						v[indexCol] -= value;						
				}				
				
				v[indexRow] += a.getValue(indexRow, cols - 1);
				indexRow++;
			}
			else {
				v[indexCol] = 1;
			}
			
			indexCol++;
		}
		
		return new Vector(v);
	}
	
	//returns the least squares solution, the closest estimation to Ax = b 
	//should Ax = b not have a solution
	public Vector LeastSquares(Vector b) {		
		return transpose().multiply(this).AxEqualsB(transpose().multiply(b));
	}
	
	//returns the rank of the matrix
	public int rank() {
		Matrix r = rref();
		int rows = rows(), cols = columns(), indexRow = 0, indexCol = 0, rank = 0;
		
		while (indexRow < rows && indexCol < cols) {
			if (r.getValue(indexRow, indexCol) == 1) {
				rank++;
				indexRow++;
			}
			
			indexCol++;
		}
		
		return rank;
	}
	
	//returns an array of vectors that serves as the column space of 
	//the calling matrix
	public Vector[] columnSpace() {
		Vector[] colspace = new Vector[rank()];
		Matrix r = rref();
		int rows = rows(), cols = columns(), indexRow = 0, indexCol = 0, i = 0;
		
		while (indexRow < rows && indexCol < cols) {
			if (r.getValue(indexRow, indexCol) == 1) {
				colspace[i] = new Vector(getColumn(indexCol));
				i++;
				indexRow++;
			}
			
			indexCol++;
		}
		
		return colspace;
	}
	
	//returns an array of vectors that serves as the null space of the
	//calling matrix
	public Vector[] nullSpace() {
		Vector[] nullspace;
		int cols = columns(), indexRow = 0, index = 0;
		int nullrank = cols - rank();
		
		if (nullrank == 0) { //no null space
			nullspace = new Vector[] {Vector.ZeroVector(cols)};
		}
		
		else {
			nullspace = new Vector[nullrank];
			for (int i = 0; i < nullrank; i++)
				nullspace[i] = new Vector(cols);
			
			Matrix r = rref();
			
			int[] freeColumns = new int[nullrank];
			for (int i = 0; i < cols; i++)
				if (pivot(r.getColumn(i))) {
					freeColumns[index] = i;
					index++;
				}
			index = 0;
			
			for (int i = 0; i < cols; i++) {
				Vector thisColumn = r.getColumn(i);
				if (pivot(thisColumn)) {
					nullspace[index].setValue(i, 1f);
					index++;
				} else {
					indexRow = valueIndex(thisColumn, 1f);
					for (int j = index; j < nullrank; j++) {
						nullspace[j].setValue(i, -(r.getValue(indexRow, freeColumns[j])));
					}
				}
			}
		}
		
		return nullspace;
	}
	
	
	//helper method for rref()
	private boolean zeroFromPoint(Vector v, int point, int direction) {
		if (direction > 0) {
			for (int i = point; i < v.length(); i++)
				if (v.getValue(i) != 0)
					return false;
		}
		else if (direction < 0) {
			for (int i = point - 1; i > 0; i--)
				if (v.getValue(i - 1) != 0)
					return false;
		}
				
		
		return true;
	}
	
	
	private int width(Vector v) {
		int width = 0;
		String s;
		
		for (int i = 0; i < v.length(); i++) {
			if (v.getValue(i) % 1 == 0)
				s = Integer.toString((int)v.getValue(i));
			else
				s = Double.toString(v.getValue(i));
			
			if (s.length() > width)
				width = s.length();
		}
		
		return width;
	}
	
	//returns whether a vector would be a pivot column a row reduced
	//echelon form matrix
	private boolean pivot(Vector v) {
		return (v.getValue(0) != 1 && v.getValue(0) != 0) ||
				v.equals(Vector.ZeroVector(v.length()));
	}
	
	private int valueIndex(Vector v, double c) {
		for (int i = 0; i < v.length(); i++) {
			if (v.getValue(i) == c)
				return i;
		}
		
		return -1;
	}
}
