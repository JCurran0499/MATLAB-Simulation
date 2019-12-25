/*
 * John Curran
 * 
 * This program is a basic simulation of the MATLAB software,
 * focusing mostly on linear algebra concepts. This is done
 * using a continuously operating scanner that reads one line
 * at a time. Errors are reported, but the program continues
 * to operate as normal
 * 
 * This program allows the user to create matrices, vectors, and
 * scalars, and manipulate data using those structures. Different
 * commands allow the user to compute matrix arithmetic, derive the 
 * inverse and transpose of a matrix, and many other calculations
 * 
 * This program's functionality is more limited and commands are not
 * identical to the real MATLAB software, so it will be provided with 
 * documentation, which will explain the possible calculations 
 * that can be done
 */

package MATLAB;

import java.util.Scanner;
import java.util.Set;
import Vectors.Vector;
import Vectors.Matrix;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import Precision.PreciseMath;

public class MATLAB {
	private static Map<String, Matrix> matrices; //all matrix variables
	private static Map<String, Vector> vectors; //all vector variables
	private static Map<String, Double> scalars; //all scalar (double) variables
	private static PreciseMath p = new PreciseMath(4); //for scalar arithmetic
	
	public static void main(String args[]) {
		matrices = new TreeMap<String, Matrix>();
		vectors = new TreeMap<String, Vector>();
		scalars = new TreeMap<String, Double>();
		
		boolean exit = true; //determines whether the program continues
		
		//lineScanner scans an entire command, while commandScanner
		//scans the various parts of a single command
		Scanner commandScanner, lineScanner = new Scanner(System.in);
		String commandIndex; //used to iterate through a command
		
		//these are generic variables that will be used whenever needed,
		//to avoid unnecessary repetitive declarations
		Matrix m;
		Vector v;
		Double s;
		
		//the program continuously loops until the user uses the exit command
		while (exit) {
			System.out.print(">> "); //every command line starts with this
			String entry = lineScanner.nextLine();
			
			//establish commandScanner to scan the given command
			commandScanner = new Scanner(entry);
			if (commandScanner.hasNext()) {
				commandIndex = commandScanner.next();
				
				//this switch statement executes the first command.
				//commandIndex has been set as the first word,
				//and the switch statement executes differently
				//depending on it
				switchlabel:
				switch(commandIndex) {
					//declaring a vector
					case "Vector":
						if (!commandScanner.hasNext()) {
							error("Must Have Variable");
							break;
						}
					
						//a vector can be named almost anything, but it
						//cannot share a name with any existing
						//matrix or scalar
						commandIndex = commandScanner.next();
						if (existsAs(commandIndex, true, false, true))
							break;
						
						if (!validName(commandIndex)) {
							error("Invalid Name");
							break;
						}
					
						//vector declaration must be followed with "="
						if (!commandScanner.hasNext() || !commandScanner.next().equals("=")
								|| !commandScanner.hasNext()) {
							error("Must Have Values");
							break;
						}
						
						//establishes a double list that will take in
						//all of the values that follow the "="
						DoubleList list = new DoubleList();
						while (commandScanner.hasNext()) {
							
							//s will be set to the next integer, but
							//if there is anything besides an integer,
							//an error will occur
							s = getInt(commandScanner);
							if (s == null) {
								error("Invalid Command");
								break switchlabel;
							}
							
							list.add(s);
						}
					
						//establishes a new vector using the double list,
						//puts it into the vector map using the given name,
						//and then prints it
						v = new Vector(list.toArray());
						vectors.put(commandIndex, v);
						System.out.println(commandIndex + " =");
						v.printVertical();						
						break;
					
					//declaring a matrix
					case "Matrix":
						if (!commandScanner.hasNext()) {
							error("Must Have Variable");
							break;
						}
					
						//a matrix variable can be named almost anything, but it
						//cannot share a name with any existing vector
						//or scalar
						commandIndex = commandScanner.next();
						if (existsAs(commandIndex, false, true, true))
							break;
						
						if (!validName(commandIndex)) {
							error("Invalid Name");
							break;
						}
					
						if (!commandScanner.hasNext() || !commandScanner.next().equals("=")
								|| !commandScanner.hasNext()) {
							error("Must Have Values");
							break;
						}
					
						//establishes this list of double lists, with each
						//double list acting as the row of the vector
						ArrayList<DoubleList> mvalues = new ArrayList<DoubleList>();
						DoubleList row = new DoubleList();
						while (commandScanner.hasNext()) {
							if (commandScanner.hasNextDouble())
								row.add(commandScanner.nextDouble());
							else {
								String val = commandScanner.next();
								
								if (scalars.containsKey(val))
									row.add(scalars.get(val));
								else if (val.equals(";")) {
									mvalues.add(row);
									row = new DoubleList();
								}
								else {
									error("Invalid Command");
									break switchlabel;
								}
							}
						}
						mvalues.add(row);
					
						//this convert mValues into an array
						double[][] mArr = new double[mvalues.size()][mvalues.get(0).size()];
						try {
							for (int i = 0; i < mvalues.size(); i++) 
								mArr[i] = mvalues.get(i).toArray();
						
							matrices.put(commandIndex, new Matrix(mArr));
							//this will result in an exception if the dimensions
							//are not the same
						} catch (Exception e) {
							error("Invalid Dimensions");
							break;
						}
					
						//print out the new vector
						System.out.println(commandIndex + " =");
						matrices.get(commandIndex).print();
						break;
					
					//declaring a scalar (double)
					case "Scalar":
						if (!commandScanner.hasNext()) {
							error("Must Have Variable");
							break;
						}
					
						//a scalar can be named almost anything, but it cannot
						//share a name with any existing vector or matrix
						commandIndex = commandScanner.next();
						if (existsAs(commandIndex, true, true, false))
							break;
						
						if (!validName(commandIndex)) {
							error("Invalid Name");
							break;
						}
					
						if (!commandScanner.hasNext() || !commandScanner.next().equals("=")
								|| !commandScanner.hasNextDouble()) {
							error("Must Have Value");
							break;
						}
					
						//establishes the scalar,puts it in the scalar map,
						//and prints it
						s = commandScanner.nextDouble();
						if (commandScanner.hasNext()) {
							error("Invalid Command");
							break;
						}
					
						scalars.put(commandIndex, s);
						System.out.print(commandIndex + " = ");
						print(s);					
						break;
					
					//calculating the length of a vector
					case "length":
						if (!commandScanner.hasNext()) {
							error("No Vector");
							break;
						}
					
						commandIndex = commandScanner.next();
						if (!vectors.containsKey(commandIndex)) {
							error("Vector Does Not Exist");
							break;
						}
					
						//gets the vector's length
						s = (double)vectors.get(commandIndex).length();
						
						//this helper method determines whether this command
						//is followed by ">", and if it is, then it copies
						//the length into the declared scalar
						if (!copyS(s, commandScanner))
							break switchlabel;
					
						System.out.println(s.intValue() + "\n");
						break;
					
					//calculate the number of rows of a matrix
					case "rows":
						if (!commandScanner.hasNext()) {
							error("No Matrix");
							break;
						}
					
						commandIndex = commandScanner.next();
						if (!matrices.containsKey(commandIndex)) {
							error("Matrix Does Not Exist");
							break;
						}
						
						//this command is executed similarly to "length"
						s = (double)matrices.get(commandIndex).rows();
					
						if (!copyS(s, commandScanner))
							break switchlabel;
					
						System.out.println(s.intValue() + "\n");
						break;
					
					//calculate the number of columns in a matrix
					case "columns":
						if (!commandScanner.hasNext()) {
							error("No Matrix");
							break;
						}
					
						commandIndex = commandScanner.next();
						if (!matrices.containsKey(commandIndex)) {
							error("Matrix Does Not Exist");
							break;
						}
					
						//this command is executed identically to "rows"
						s = (double)matrices.get(commandIndex).columns();
						
						if (!copyS(s, commandScanner))
							break switchlabel;
					
						System.out.println(s.intValue() + "\n");
						break;
					
					//calculates the size of a matrix
					case "size":
						if (!commandScanner.hasNext()) {
							error("No Matrix");
							break;
						}
					
						commandIndex = commandScanner.next();
						if (!matrices.containsKey(commandIndex)) {
							error("Matrix Does Not Exist");
							break;
						}
					
						//this command is executed identically to "columns"
						s = (double)matrices.get(commandIndex).size();
					
						if (!copyS(s, commandScanner))
							break switchlabel;
					
						System.out.println(s.intValue() + "\n");
						break;
					
					//derives the tranpose a matrix
					case "transpose":
						if (!commandScanner.hasNext()) {
							error("No Matrix");
							break;
						}
					
						commandIndex = commandScanner.next();
						if (!matrices.containsKey(commandIndex)) {
							error("Matrix Does Not Exist");
							break;
						}
					
						//calculates the transpose
						m = matrices.get(commandIndex).transpose();
					
						//copies the transpose to another matrix if necessary
						if (!copyM(m, commandScanner))
							break switchlabel;
					
						m.print();
						break;
					
					//derives the row reduced echelon form of a matrix
					case "rref":
						if (!commandScanner.hasNext()) {
							error("No Matrix");
							break;
						}
					
						commandIndex = commandScanner.next();
						if (!matrices.containsKey(commandIndex)) {
							error("Matrix Does Not Exist");
							break;
						}
					
						//calculates the rref
						m = matrices.get(commandIndex).rref();
					
						//copies the rref matrix if necessary
						if (!copyM(m, commandScanner))
							break switchlabel;
					
						m.print();
						break;
					
					//derives the inverse of a matrix
					case "inverse":	
						if (!commandScanner.hasNext()) {
							error("No Matrix");
							break;
						}
						
						commandIndex = commandScanner.next();
						if (!matrices.containsKey(commandIndex)) {
							error("Matrix Does Not Exist");
							break;
						}
					
						//calculates the inverse of the matrix, returning
						//an error if the matrix is not invertible
						m = matrices.get(commandIndex).invert();
						if (m == null) {
							error("Not Invertible");
							break;
						}
					
						if (!copyM(m, commandScanner))
							break switchlabel;
					
						m.print();
						break;
						
					//derives the determinant of a matrix
					case "det":
						if (!commandScanner.hasNext()) {
							error("No Matrix");
							break;
						}
						
						commandIndex = commandScanner.next();
						if (!matrices.containsKey(commandIndex)) {
							error("Matrix Does Not Exist");
							break;
						}
					
						//calculates the determinant of the matrix
						s = matrices.get(commandIndex).determinant();
					
						if (!copyS(s, commandScanner))
							break switchlabel;
					
						print(s);
						break;
						
					//calculates the dot product (inner product) of two vectors
					case "dot":
						if (!commandScanner.hasNext()) {
							error("No Vector");
							break;
						}
						
						//sets v1 and v2 as the vectors to be calculated,
						//returning an error if the vectors don't exist
						commandIndex = commandScanner.next();
						if (!vectors.containsKey(commandIndex)) {
							error("Vector Doesn't Exist");
							break;
						}
						Vector v1 = vectors.get(commandIndex);
						
						if (!commandScanner.hasNext()) {
							error("No Vector");
							break;
						}
						
						commandIndex = commandScanner.next();
						if (!vectors.containsKey(commandIndex)) {
							error("Vector Doesn't Exist");
							break;
						}
						Vector v2 = vectors.get(commandIndex);
						
						//calculates the dot product of the vectors,
						//returning an error if an exception was thrown
						//by the method
						try {
							s = v1.dot(v2);
						
						
							if (!copyS(s, commandScanner))
								break switchlabel;
							
							print(s);
							
						} catch (Exception e) {
							error("Invalid Computation");
							break;
						}
						
						break;
						
					//list the variables in the program
					case "list":
						Set<String> set;
						
						//if the list command is not followed by anything,
						//it will list all variables of all types. If it
						//has a word after it, it will list the variables
						//of the type declared in that word
						if (commandScanner.hasNext()) {
							commandIndex = commandScanner.next();							
							
							//create a set of all matrices
							if (commandIndex.equals("matrices")) {
								set = matrices.keySet();
								System.out.print("Matrices: ");
								
							//create a set of all vectors
							} else if (commandIndex.equals("vectors")) {
								set = vectors.keySet();
								System.out.print("Vectors: ");
								
							//create a set of all scalars
							} else if (commandIndex.equals("scalars")) {
								set = scalars.keySet();
								System.out.print("Scalars: ");
							} else {
								error("Invalid Command");
								break;
							}
							
							//print out every member of the list
							for (String var : set)
								System.out.print(var + "  ");
							System.out.println();
						}
						
						//print everything
						else {
							//this goes through each map, one at a time,
							//creating a set of the members and then
							//printing them
							set = matrices.keySet();
							System.out.print("Matrices: ");
							for (String var : set)
								System.out.print(var + "  ");
							System.out.println();
							
							set = vectors.keySet();
							System.out.print("Vectors: ");
							for (String var : set)
								System.out.print(var + "  ");
							System.out.println();
							
							set = scalars.keySet();
							System.out.print("Scalars: ");
							for (String var : set)
								System.out.print(var + "  ");
							System.out.println();
						}
						
						System.out.println();						
						break;
						
					//set a value in a matrix or vector, or set an entire
					//column in a matrix
					case "set":
						if (!commandScanner.hasNext()) {
							error("Must Have Variable");
							break;
						}
						
						//take in the name of the variable
						String name = commandScanner.next();
						if (!commandScanner.hasNext()) {
							error("Invalid Command");
							break;
						}
						
						//altering a matrix
						if (matrices.containsKey(name)) {
							m = matrices.get(name);
							commandIndex = commandScanner.next();
							switch(commandIndex) {
								//setting an entire column
								case ":":
									//set s to the column number 
									s = getInt(commandScanner);
									if (s == null || s % 1 != 0) {
										error("Invalid Command");
										break switchlabel;
									}
									
									if (!commandScanner.hasNext()) {
										error("Invalid Command");
										break switchlabel;
									}
									
									//the next variable must be a vector variable.
									//Set it to the given column
									commandIndex = commandScanner.next();
									if (vectors.containsKey(commandIndex))
										try {
											m.setColumn(s.intValue() - 1, new Vector(vectors.get(commandIndex)));
										} catch (Exception e) {
											error("Invalid Dimensions");
											break switchlabel;
										}
									else {
										error("Invalid Command");
										break switchlabel;
									}
									
									break;
									
								//set an value in the matrix
								case "::":
									//set s1 as the row, s2 as the vector, s as the new value
									Double s1 = getInt(commandScanner), s2 = getInt(commandScanner);
									s = getInt(commandScanner);
									if (s1 == null || s2 == null || s == null ||
											s1 % 1 != 0 || s2 % 1 != 0) {
										error("Invalid Command");
										break switchlabel;
									}
									
									//set the new value. If there is an error, the 
									//function will throw an exception, so return
									//an error
									try {
										m.setValue(s1.intValue() - 1, s2.intValue() - 1, s);
									} catch (Exception e) {
										error("Invalid Dimensions");
										break switchlabel;
									}
									
									System.out.println(name + " = ");
									m.print();
									break;
									
								default:
									error("Unknown Command");
									break switchlabel;
							}
							
							System.out.println(name + " = ");
							m.print();
							
							//altering a vector
						} else if (vectors.containsKey(name)) {
							v = vectors.get(name);
							commandIndex = commandScanner.next();
							
							//the only possible next word is ":". If something
							//else is written, return an error
							if (!commandIndex.equals(":")) {
								error("Unknown Command");
								break;
							}
							
							//set s1 as the value position, and s2 as the new value
							Double s1 = getInt(commandScanner), s2 = getInt(commandScanner);
							if (s1 == null || s2 == null || s1 % 1 != 0) {
								error("Invalid Command");
								break;
							}
							
							//set the new value in the vector. If an exception
							//is thrown, return an error
							try {
								v.setValue(s1.intValue() - 1, s2);
							} catch (Exception e) {
								error("Invalid Dimensions");
								break;
							}
							System.out.println(name + " = ");
							v.printVertical();
						} else {
							error("Unknown Command");
							break;
						}
						
						if (commandScanner.hasNext()) {
							error("Invalid Command");
							break;
						}
						
						break;
						
					case "identity":
						s = getInt(commandScanner);
						if (s == null || s % 1 != 0) {
							error("Invalid Command");
							break;
						}
						
						m = Matrix.Identity(s.intValue());
						
						if (!copyM(m, commandScanner))
							break;
						
						break;
						
					//terminate the program
					case "exit":
						exit = false;
						break;
						
					//if none of the standard commands were used, then
					//the only other option was a command that starts
					//with an existing variable. The default case 
					//encompasses all possible commands involving
					//vectors, matrices, and scalars
					default:
						//if the variable is a vector
						if (vectors.containsKey(commandIndex)) {
							//declare "vector" as the variable being operated on
							Vector vector = vectors.get(commandIndex);
							
							//if it is just the variable name and nothing else,
							//simply print the vector. Otherwise, follow
							//the rest of the command
							if (!commandScanner.hasNext())
								vector.printVertical();
							
							else {
								commandIndex = commandScanner.next();
								if (!commandScanner.hasNext()) {
									error("Unknown Command");
									break;
								}
								
								switch(commandIndex) {
									//adding two vectors
									case "+":
										commandIndex = commandScanner.next();
										if (!vectors.containsKey(commandIndex)) {
											error("Must Add A Vector");
											break switchlabel;
										}
										
										//set v to the sum of the two vectors, and
										//return an error if it is not possible
										v = vector.add(vectors.get(commandIndex));
										if (v == null) {
											error("Invalid Computation");
											break switchlabel;
										}
										
										//copy to another vector if necessary
										if (!copyV(v, commandScanner))
											break switchlabel;
										
										v.printVertical();										
										break;
										
									//subtracting two vectors
									case "-":
										commandIndex = commandScanner.next();
										if (!vectors.containsKey(commandIndex)) {
											error("Must Subtract A Vector");
											break switchlabel;
										}
										
										//this is done indentically to "+"
										v = vector.subtract(vectors.get(commandIndex));
										if (v == null) {
											error("Invalid Computation");
											break switchlabel;
										}
										
										if (!copyV(v, commandScanner))
											break switchlabel;
										
										v.printVertical();										
										break;
										
									//multiply a vector by a scalar
									case "*":
										//set s to the next scalar, and return
										//an error if there isn't one
										s = getInt(commandScanner);
										if (s == null) {
											error("Invalid Command");
											break switchlabel;
										}
										
										v = vector.multiply(s);
										
										if (!copyV(v, commandScanner))
											break switchlabel;
										
										v.printVertical();										
										break;
										
									//derive the value at a certain point in
									//the vector
									case ":":
										//this command must be followed by 
										//an integer, which refers to the
										//point in the vector
										s = getInt(commandScanner);
										
										//checks if there wasn't a scalar or
										//if the scalar isn't an integer
										if (s == null || s % 1 != 0) {
											error("Invalid Command");
											break switchlabel;
										}
										
										//gets the value, and copies it to another
										//scalar if necessary
										s = vector.getValue(s.intValue() - 1);
										
										if (!copyS(s, commandScanner))
											break switchlabel;
										
										print(s);
										break;
										
									//copies a vector to another vector
									case ">":
										if (!commandScanner.hasNext()) {
											error("Invalid Command");
											break switchlabel;
										}
										
										//sets commandIndex to the new vector
										//to be copied to
										commandIndex = commandScanner.next();
										if (commandScanner.hasNext()) {
											error("Invalid Command");
											break switchlabel;
										}
										
										
										//new vector name cannot exist as a matrix
										//or a scalar
										if (existsAs(commandIndex, true, false, true))
											break switchlabel;
										
										vectors.put(commandIndex, new Vector(vector));
										System.out.println(commandIndex + " = ");
										vector.printVertical();
										break;
										
									//if the next word is not any of the valid commands,
									//it must be an error
									default:
										error("Unknown Command");
										break switchlabel;
								}
							}
						}
						
						//if the variable is a matrix
						else if (matrices.containsKey(commandIndex)) {
							//declare "matrix" as the matrix being operated on
							Matrix matrix = matrices.get(commandIndex);
							
							//if it is just the variable name, print it
							if (!commandScanner.hasNext())
								matrix.print();
							
							else {
								//sets commandIndex to the next command (which
								//must be followed by another word)
								commandIndex = commandScanner.next();
								if (!commandScanner.hasNext()) {
									error("Invalid Command");
									break;
								}
								
								switch(commandIndex) {
									//adding two matrices
									case "+":
										commandIndex = commandScanner.next();
										if (!matrices.containsKey(commandIndex)) {
											error("Must Add A Matrix");
											break switchlabel;
										}
										
										//this works similar to adding two vectors
										m = matrix.add(matrices.get(commandIndex));
										if (m == null) {
											error("Invalid Computation");
											break switchlabel;
										}
										
										if (!copyM(m, commandScanner))
											break switchlabel;
										
										m.print();										
										break;
										
									//subtracting two vectors
									case "-":
										commandIndex = commandScanner.next();
										if (!matrices.containsKey(commandIndex)) {
											error("Must Add A Matrix");
											break switchlabel;
										}
										
										//this work identically to adding matrices
										m = matrix.subtract(matrices.get(commandIndex));
										if (m == null) {
											error("Invalid Computation");
											break switchlabel;
										}
										
										if (!copyM(m, commandScanner))
											break switchlabel;
										
										m.print();
										break;
										
									//multiply a matrix by another matrix, a vector,
									//or a scalar
									case "*":
										//if the next word is a double, simply multiply,
										//print it, and the break
										if (commandScanner.hasNextDouble()) {
											double mult = commandScanner.nextDouble();
											m = matrix.multiply(mult);
											
											if (!copyM(m, commandScanner))
												break switchlabel;
											
											m.print();
											break;
										}
										
										//otherwise, it must be a variable, so
										//set commandIndex to the variable name
										commandIndex = commandScanner.next();
										
										//if it's a vector
										if (vectors.containsKey(commandIndex)) {
											
											//multiply, and then copy if necessary
											v = matrix.multiply(vectors.get(commandIndex));
											if (v == null) {
												error("Invalid Computation");
												break switchlabel;
											}
											
											if (!copyV(v, commandScanner))
												break switchlabel;
											
											v.printVertical();
											
										//if it's a matrix
										} else if (matrices.containsKey(commandIndex)) {
											m = matrix.multiply(matrices.get(commandIndex));
											if (m == null) {
												error("Invalid Computation");
												break switchlabel;
											}
											
											if (!copyM(m, commandScanner))
												break switchlabel;
											
											m.print();
											
										//if it's a scalar
										} else if (scalars.containsKey(commandIndex)) {
											m = matrix.multiply(scalars.get(commandIndex));
											
											if (!copyM(m, commandScanner))
												break switchlabel;
											
											m.print();
										} else {
											error("Invalid Command");
											break switchlabel;
										}
										
										break;
										
									//bring a matrix to a given power
									case "^":
										//set s to the exponent, and then
										//make sure it exists and is an
										//integer
										s = getInt(commandScanner);
										if (s == null || s % 1 != 0) {
											error("Invalid Command");
											break switchlabel;
										}
										
										//bring m to the given power, check for
										//erros, and then copy if necessary
										m = matrix.toPower(s.intValue());
										if (m == null) {
											error("Invalid Computation");
											break switchlabel;
										}
										
										if (!copyM(m, commandScanner))
											break switchlabel;
										
										m.print();
										break;
										
									//deriving a given column in the matrix
									//in vector form
									case ":":
										//set s to the number, and then 
										//check for validity
										s = getInt(commandScanner);
										if (s == null || s % 1 != 0) {
											error("Invalid Command");
											break switchlabel;
										}
										
										//set v as the given column, check
										//for errors, and then copy if
										//necessary
										v = matrix.getColumn(s.intValue() - 1);
										if (v == null) {
											error("Invalid Computation");
											break switchlabel;
										}
										
										if (!copyV(v, commandScanner))
											break switchlabel;
										
										v.printVertical();
										break;
										
									//derives a value at a given row and column
									//in the matrix
									case "::":
										//read two scalars, and check for validity
										//in both
										Double s1 = getInt(commandScanner), s2 = getInt(commandScanner);
										if (s1 == null || s2 == null ||
												s1 % 1 != 0 || s2 % 1 != 0) {
											error("Invalid Command");
											break switchlabel;
										}
										
										//set s as the given value, and copy to another
										//scalar if necessary
										s = matrix.getValue(s1.intValue() - 1, s2.intValue() - 1);
										
										if (!copyS(s, commandScanner)) 
											break switchlabel;
										
										print(s);
										break;
										
									//copy to another matrix
									case ">":
										if (!commandScanner.hasNext()) {
											error("Invalid Command");
											break switchlabel;
										}
										
										//set commandIndex to the variable
										//to be copied to
										commandIndex = commandScanner.next();
										if (commandScanner.hasNext()) {
											error("Unknown Command");
											break switchlabel;
										}
										
										//cannot exist as a vector or scalar
										if (existsAs(commandIndex, false, true, true))
											break switchlabel;
										
										//copy into map
										matrices.put(commandIndex, new Matrix(matrix));
										System.out.println(commandIndex + " = ");
										matrix.print();
										break;
								}
							}
							
						}
						
						//if the variable is a scalar
						else if (scalars.containsKey(commandIndex)) {
							//set "scalar" as the scalar being operated on
							Double scalar = scalars.get(commandIndex);
							
							//if it's just the variable name, print the scalar
							if (!commandScanner.hasNext()) 
								print(scalar);
							
							else {
								commandIndex = commandScanner.next();
								//with the exception of "!", all commands need
								//another word after the command
								if (!commandIndex.equals("!") && !commandScanner.hasNext()) {
									error("Unknown Command");
									break;
								}
								
								switch(commandIndex) {
									//add two scalars
									case "+":
										//set s to the next scalar
										s = getInt(commandScanner);										
										if (s == null) {
											error("Invalid Command");
											break switchlabel;
										}
										
										//add them together, and copy if necessary
										s = p.add(s, scalar);
										
										if (!copyS(s, commandScanner))
											break switchlabel;
										
										print(s);										
										break;
										
									//subtract two scalars
									case "-":
										s = getInt(commandScanner);
										if (s == null) {
											error("Invalid Command");
											break switchlabel;
										}
										
										//this is done identically to "+"
										s = p.subtract(scalar, s);
										
										if (!copyS(s, commandScanner))
											break switchlabel;
										
										print(s);
										break;
										
									case "*":
										s = getInt(commandScanner);
										if (s == null) {
											error("Invalid Command");
											break switchlabel;
										}
										
										s = p.multiply(s, scalar);
										
										if (!copyS(s, commandScanner))
											break switchlabel;
										
										print(s);										
										break;
										
									//divide two scalars
									case "/":
										//set s to the divisor, and check for 
										//validity
										s = getInt(commandScanner);
										if (s == null) {
											error("Invalid Command");
											break switchlabel;
										}
										if (s == 0) {
											error("Divide By Zero");
											break switchlabel;
										}
										
										s = p.divide(scalar, s);
										
										if (!copyS(s, commandScanner))
											break switchlabel;
										
										print(s);										
										break;
										
									//set the scalar to a given power
									case "^":
										s = getInt(commandScanner);
										if (s == null) {
											error("Invalid Command");
											break switchlabel;
										}
										
										//brings s to the given exponent, and
										//then copies if necessary
										s = (double)Math.pow(scalar, s);
										
										if (!copyS(s, commandScanner))
											break switchlabel;
										
										print(s);										
										break;
										
									//gets the remainder
									case "%":
										s = getInt(commandScanner);
										if (s == null) {
											error("Invalid Command");
											break switchlabel;
										}
										
										s = scalar % s;
										
										if (!copyS(s, commandScanner))
											break switchlabel;
										
										print(s);										
										break;
										
									//gets the factorial
									case "!":
										//must be an integer
										if (scalar % 1 != 0) {
											error("Must Use Integer");
											break switchlabel;
										}
										
										//set s to the factorial, and then copy
										//if necessary
										s = (double)factorial(scalar.intValue());
										
										if (!copyS(s, commandScanner))
											break switchlabel;
										
										print(s);										
										break;
										
									//copies a scalar another
									case ">":
										if (!commandScanner.hasNext()) {
											error("Invalid Command");
											break switchlabel;
										}
										
										commandIndex = commandScanner.next();
										if (commandScanner.hasNext()) {
											error("Invalid Command");
											break switchlabel;
										}
										
										//cannot exist as a matrix or vector
										if (existsAs(commandIndex, true, true, false))
											break switchlabel;
										
										scalars.put(commandIndex, scalar);
										System.out.print(commandIndex + " = ");
										print(scalar);
										break;
										
									//if it isn't one of these commands,
									//it must be an error
									default:
										error("Unknown Command");
										break switchlabel;
								}
							}
						}
						
						//if the variable isn't any of those three, then it
						//doesn't exist, and it must be an error
						else {
							error("Unknown Command");
							break;
						}
						
						break;
				}
			}
			
			//for each line, close the commandScanner before it is reopened for the next line
			commandScanner.close();
		}
		
		//at the end of the program, close the lineScanner
		lineScanner.close();
	}
	
	
	
	//private helper methods
	
	//prints an error message
	private static void error(String erMessage) {
		System.out.println("Error: " + erMessage + "\n");
	}
	
	//calculates the factorial of an integer
	private static int factorial(int f) {
		int factorial = 1;
	    for (int i = f; i > 1; i--)
	    	factorial *= i;
	    
	    return factorial;
	}
	
	//if necessary, copies a vector in the vectors map. If there is an
	//error, the function returns false
	private static boolean copyV(Vector v, Scanner s) {
		//this method only executes if there are words
		//after the original command
		if (s.hasNext()) {
			
			//if the next word isn't ">", then this is an error
			if (!s.next().equals(">") || !s.hasNext()) {
				error("Invalid Command");
				return false;
			}
			
			String commandIndex = s.next();
			if (s.hasNext()) {
				error("Unknown Command");
				return false;
			}
			
			//cannot duplicate something that already exists as a matrix
			//or scalar
			if (existsAs(commandIndex, true, false, true)) {
				error("Invalid Command");
				return false;
			}
			if (!validName(commandIndex)) {
				error("Invalid Name");
				return false;
			}
			
			//copies the vector into the map. If it doesn't exist, then
			//it will be created. If it already exists, then it will be
			//overwritten
			vectors.put(commandIndex, new Vector(v));
			System.out.println(commandIndex + " = ");
		}
		
		return true;
	}
	
	//this is almost identical to copyV, except it copies a matrix
	private static boolean copyM(Matrix m, Scanner s) {
		if (s.hasNext()) {
			if (!s.next().equals(">") || !s.hasNext()) {
				error("Invalid Command");
				return false;
			}
			
			String commandIndex = s.next();
			if (s.hasNext()) {
				error("Unknown Command");
				return false;
			}
			
			
			if (existsAs(commandIndex, false, true, true)) {
				error("Invalid Command");
				return false;
			}
			if (!validName(commandIndex)) {
				error("Invalid Name");
				return false;
			}
			
			matrices.put(commandIndex, new Matrix(m));
			System.out.println(commandIndex + " = ");
		}
		
		return true;
	}
	
	//this is almost identical to copyV and copyM, except it copies a scalar
	private static boolean copyS(double d, Scanner s) {
		if (s.hasNext()) {
			if (!s.next().equals(">") || !s.hasNext()) {
				error("Invalid Command");
				return false;
			}
			
			String commandIndex = s.next();
			if (s.hasNext()) {
				error("Unknown Command");
				return false;
			}
			
			
			if (existsAs(commandIndex, true, true, false)) {
				error("Invalid Command");
				return false;
			}
			if (!validName(commandIndex)) {
				error("Invalid Name");
				return false;
			}
			
			scalars.put(commandIndex, d);
			System.out.print(commandIndex + " = ");
		}
		
		return true;
	}
	
	//prints a double, printing it in integer form if it does not
	//have a decimal section
	private static void print(Double s) {
		if (s % 1 == 0)
			System.out.println(s.intValue());
		else
			System.out.println(s);
	}
	
	//returns the next scalar, whether it is an immediate value or
	//a scalar variable. Returns null if there isn't one
	private static Double getInt(Scanner s) {
		if (s.hasNextDouble())
			return s.nextDouble();
		
		else {
			String next = s.next();
			if (scalars.containsKey(next))
				return scalars.get(next);
			else
				return null;
		}
	}
	
	//checks if a string is a valid name for a variable. A variable cannot
	//be named something that acts as a command, and it cannot be just a number
	private static boolean validName(String name) {
		//name cannot be a normal command
		if (name.equals("Matrix") || name.equals("Vector") || name.equals("Scalar") ||
				name.equals("length") || name.equals("rows") || name.equals("column") ||
				name.equals("size") || name.equals("transpose") || name.equals("rref") ||
				name.equals("inverse") || name.equals("det") || name.equals("dot") ||
				name.equals("exit") || name.equals(";") || name.equals(">") || 
				name.equals(":") || name.equals("::") || name.equals("!") || name.equals("+") ||
				name.equals("-") || name.equals("*") || name.equals("/") || name.equals("%") || 
				name.equals("^") || name.equals("#"))
			return false;
		
		//name cannot be just a number (it can include digits, though)
		try {
			Double.parseDouble(name);
		} catch (Exception e) {
			return true;
		}
		
		return false;
	}
	
	//this helper method returns whether the variable name exists as a matrix,
	//vector, or scalar. The three boolean parameters allow customization. Setting
	//any of them to false results in the method not checking if it 
	//the variable exists in that form
	private static boolean existsAs(String name, boolean matrix, boolean vector, boolean scalar) {
		if (matrix) 
			if (matrices.containsKey(name)) {
				error("Exists As Matrix");
				return true;
			}		
		
		if (vector) 
			if (vectors.containsKey(name)) {
				error("Exists As Vector");
				return true;
			}		
		
		if (scalar) 
			if (scalars.containsKey(name)) {
				error("Exists As Scalar");
				return true;
			}		
		
		return false;
	}
}