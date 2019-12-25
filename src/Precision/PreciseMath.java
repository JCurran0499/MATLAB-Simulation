/* 
 * John Curran
 * 
 * The PreciseMath class allows the user to perform more precise arithmetic
 * with decimals, because the current double and float data types are not always
 * precise. Creating a PreciseMath object allows the user to specify the 
 * level of precision (in other words, how many digits occur after the decimal)
 * and use the object to perform arithmetic with double values. There are of
 * course limits to this, as precision levels must be small and larger numbers
 * cannot be used, but it serves as a useful tool in lower level arithmetic
 * when precision is necessary
 */

package Precision;

public class PreciseMath {
	private long degree;
	
	//instantiating a PreciseMath object will allow the user to define
	//the level of precision that they desire. More precision is, by definition,
	//more precise, however it is more restrictive on how large the numbers you
	//use are. It is recommended the user uses a precision somewhere around
	//level 3 or 4. Large or nonpositive precision values will cause
	//undefined results
	public PreciseMath(int precision) {
		degree = (int)Math.pow(10, precision);
	}
	
	
	//adding two double values
	public double add(double x, double y) {
		long xInt = (long) (x * degree);
		long yInt = (long) (y * degree);

		return (double)(xInt + yInt) / degree;
	}
	
	//subtracting two double values
	public double subtract(double x, double y) {
		long xInt = (long) (x * degree);
		long yInt = (long) (y * degree);
		
		return (double)(xInt - yInt) / degree;
	}
	
	//multiplying two double values
	public double multiply(double x, double y) {
		long xInt = (long) (x * degree);
		long yInt = (long) (y * degree);
		
		return (double)(xInt * yInt) / (degree * degree);
	}
	
	//dividing two double values
	public double divide(double x, double y) {
		long xInt = (long) (x * degree);
		long yInt = (long) (y * degree);
		
		return (double) xInt / yInt;
	}
}