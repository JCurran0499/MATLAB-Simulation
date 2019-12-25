/*
 * John Curran
 * 
 * This is a basic, stripped down linked list of double values
 * used in the MATLAB simulation as any easy way to convert a
 * linked list to an array. This list uses inner close nodes,
 * and has only a few limited methods that are protected, 
 * as this is designed only to be used in the MATLAB simulation
 */

package MATLAB;

public class DoubleList {
	private class Node {
		private double value;
		private Node next;
		
		private Node(double v) {
			value = v;
			next = null;
		}
	}
	
	private Node head;
	private short size;
	
	protected DoubleList() {
		head = null;
		size = 0;
	}
	
	protected void add(double v)   {
		if (head == null)
			head = new Node(v);
		else {
			Node temp = head;
		
			while (temp.next != null)
				temp = temp.next;
		
			temp.next = new Node(v);
		}
		
		size++;
	}
	
	protected double getValue(int index)   {
		if (index > size - 1 || index < 0)
			throw new ArrayIndexOutOfBoundsException("Out of Bounds");
		
		Node temp = head;
		for(int i = 0; i < index; i++)
			temp = temp.next;
		
		return temp.value;
	}
	
	protected int size() {
		return size;
	}
	
	protected void clear() {
		head = null;
		size = 0;
	}
	
	protected double[] toArray() {
		Node temp = head;
		int index = 0;
		double[] list = new double[size];
		while (temp != null) {
			list[index] = temp.value;
			temp = temp.next;
			index++;
		}
		
		return list;
	}
}
