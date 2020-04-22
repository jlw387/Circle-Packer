package com.pcg;

public class Circle {
	public double x;
	public double y;
	public int index;
	public int depth;
	
	private int[] neighbors = {-1,-1,-1,-1,-1,-1};
	
	public Circle() {
		x = 0;
		y = 0;
		index = 0;
		depth = 0;
	}
	
	public Circle(double cx, double cy, int dep) {
		x = cx;
		y = cy;
		index = 0;
		depth = dep;
	}
	
	public Circle(double cx, double cy, int ind, int dep) {
		x = cx;
		y = cy;
		index = ind;
		depth = dep;
	}
	
	public boolean[] hasNeighbors() {
		boolean[] ret = {
			(neighbors[0] != -1),
			(neighbors[1] != -1),
			(neighbors[2] != -1),
			(neighbors[3] != -1),
			(neighbors[4] != -1),
			(neighbors[5] != -1)
		};
		return ret;
	}
	
	public int getNeighbor(int indexC) {
		return neighbors[indexC];
	}
	
	public int[] getNeighbors() {
		return neighbors;
	}
	
	public void setNeighbor(int indexC, int id) {
		neighbors[indexC] = id;
	}
	
	public void setNeighbors(int[] neighbs) throws Exception {
		if(neighbs.length == 6)
			neighbors = neighbs;
		else
			throw new Exception("Array Length Exception: Array Length Must Be 6");
	}
	
	public double eccentricity() {
		return Math.sqrt(x*x + y*y);
	}
}
