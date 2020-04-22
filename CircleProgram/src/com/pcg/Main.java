package com.pcg;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

public class Main {

	public static final int FOVEA_MAX_DEPTH = 30;
	public static final double FOVEA_CONE_DISTANCE = 1.0;
	public static final double FOVEA_DISTANCE = FOVEA_CONE_DISTANCE * (FOVEA_MAX_DEPTH * Math.sqrt(2) / 1.99);
	public static final double DISTANCE_PARAM = FOVEA_DISTANCE;
	public static final double PREEXP_PARAM = 0.1;
	
	public static void main(String[] args) {
		ArrayList<Circle> circles = generateCircles1();
		
		JFrame frame = new JFrame();
		frame.setBounds(0, 0, 1080, 1080);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setName("CircleViewer");
		frame.setBackground(Color.WHITE);
		
		CircleDrawPane mypane = new CircleDrawPane(circles);
		frame.add(mypane);
		
		frame.setVisible(true);
	}
	
	public static ArrayList<Circle> generateCircles1(){
		ArrayList<Circle> frontier = new ArrayList<Circle>(); //Layer of circles currently being added
		ArrayList<Circle> firstFrozen = new ArrayList<Circle>(); //Layer of circles that has just been added
		ArrayList<Circle> hardFrozen = new ArrayList<Circle>(); //All circles which have been added in earlier layers
		
		System.out.println("Enter maximum generation depth: ");
		Scanner s = new Scanner(System.in);
		int maxDepth = s.nextInt();
		s.close();
		
		firstFrozen.add(new Circle());
		int currentIndex = 1;
		
		//Run until the specified maximum generation depth
		for(int depth = 1; depth <= maxDepth; depth++) {
			
			//Loop through all circles in the outside frozen layer
			for (Circle c : firstFrozen) {
				double dist = getInterCircleDistance (c.eccentricity());
				
				//Get an array which tells us which neighboring circles are missing
				boolean[] neighbors = c.hasNeighbors(); 
				//Loop through all elements of the array (the important code in the loop only runs when a neighbor is missing)
				for (int j = 0; j < 6; j++) {
					if(!neighbors[j]) {
						//Create the new circle to be added
						Circle nc = new Circle(c.x + dist * Math.cos(Math.toRadians(60 * j)), 
								c.y + dist * Math.sin(Math.toRadians(60 * j)),
								currentIndex, depth);
						//Add this circle to the frontier
						frontier.add(nc);
						
						//Determine which existing circles neighbor the new circle and update each circle's "neighbors" array.
						//Obviously, the frozen layer circle will neighbor this new circle.
						c.setNeighbor(j, currentIndex);
						nc.setNeighbor((j+3) % 6, c.index);
						
						//Additional potential neighbors of the new circle will be at the j+1 and j-1 positions of the frozen layer circle's neighbors
						if(c.hasNeighbors()[(j+5) % 6]) {
							int neighborIndex = c.getNeighbor((j+5) % 6);
							nc.setNeighbor((j+4) % 6, neighborIndex);
							if(neighborIndex < firstFrozen.size() + hardFrozen.size()) {
								Circle neighbor = firstFrozen.get(neighborIndex - hardFrozen.size());
								neighbor.setNeighbor((j+1) % 6, currentIndex);
							}
							else {
								Circle neighbor = frontier.get(neighborIndex - hardFrozen.size() - firstFrozen.size());
								neighbor.setNeighbor((j+1) % 6, currentIndex);
							}
								
						}
						if(c.hasNeighbors()[(j+1) % 6]) {
							int neighborIndex = c.getNeighbor((j+1) % 6);
							nc.setNeighbor((j+2) % 6, neighborIndex);
							
							if(neighborIndex < firstFrozen.size() + hardFrozen.size()) {
								Circle neighbor = firstFrozen.get(neighborIndex - hardFrozen.size());
								neighbor.setNeighbor((j+5) % 6, currentIndex);
							}
							else {
								Circle neighbor = frontier.get(neighborIndex - hardFrozen.size() - firstFrozen.size());
								neighbor.setNeighbor((j+5) % 6, currentIndex);
							}
						}
						
						currentIndex++;
					}
				}
			}
			
			hardFrozen.addAll(firstFrozen);
			firstFrozen = new ArrayList<Circle>();
			firstFrozen.addAll(frontier);
			frontier = new ArrayList<Circle>();
		}
		
		hardFrozen.addAll(firstFrozen);
		
		return hardFrozen;
	}
	
	public static ArrayList<Circle> generateCircles2(){
		ArrayList<Circle> retina = generateFovea();
		System.out.println("Enter maximum generation depth: ");
		Scanner s = new Scanner(System.in);
		int maxDepth = s.nextInt();
		s.close();
		
		double radius = FOVEA_DISTANCE + getInterCircleDistance(FOVEA_DISTANCE);
		
		for(int depth = 1; depth < maxDepth; depth++) {
			double theta = 2*Math.asin(getInterCircleDistance(radius) / 2 / radius);
			int numCircles = (int)(Math.PI / theta) + 1;
			double phi = Math.random() * 2 * Math.PI;
			for(int i = 0; i < numCircles; i++) {
				retina.add(new Circle(radius*Math.cos(phi + i*2*Math.PI/numCircles), radius*Math.sin(phi + i*2*Math.PI/numCircles), depth));
			}
			
			radius += getInterCircleDistance(radius);
		}
		
		return retina;
	}
	
	public static ArrayList<Circle> generateFovea(){
		
		ArrayList<Circle> foveaCircles = new ArrayList<Circle>();
		foveaCircles.add(new Circle());
		
		for(int depth = 1; depth < FOVEA_MAX_DEPTH; depth++) {
			double currentX = FOVEA_CONE_DISTANCE * depth;
			double currentY = 0;
			if(currentX * currentX + currentY * currentY <= FOVEA_DISTANCE * FOVEA_DISTANCE)
				foveaCircles.add(new Circle(currentX, currentY, depth));
			
			for(int i = 0; i < depth; i++) {
				currentX -= FOVEA_CONE_DISTANCE / 2;
				currentY += FOVEA_CONE_DISTANCE * Math.sqrt(3) / 2;
				if(currentX * currentX + currentY * currentY <= FOVEA_DISTANCE * FOVEA_DISTANCE)
					foveaCircles.add(new Circle(currentX, currentY, depth));
			}
			for(int i = 0; i < depth; i++) {
				currentX -= FOVEA_CONE_DISTANCE;
				if(currentX * currentX + currentY * currentY <= FOVEA_DISTANCE * FOVEA_DISTANCE)
					foveaCircles.add(new Circle(currentX, currentY, depth));
			}
			for(int i = 0; i < depth; i++) {
				currentX -= FOVEA_CONE_DISTANCE / 2;
				currentY -= FOVEA_CONE_DISTANCE * Math.sqrt(3) / 2;
				if(currentX * currentX + currentY * currentY <= FOVEA_DISTANCE * FOVEA_DISTANCE)
					foveaCircles.add(new Circle(currentX, currentY, depth));
			}
			for(int i = 0; i < depth; i++) {
				currentX += FOVEA_CONE_DISTANCE / 2;
				currentY -= FOVEA_CONE_DISTANCE * Math.sqrt(3) / 2;
				if(currentX * currentX + currentY * currentY <= FOVEA_DISTANCE * FOVEA_DISTANCE)
					foveaCircles.add(new Circle(currentX, currentY, depth));
			}
			for(int i = 0; i < depth; i++) {
				currentX += FOVEA_CONE_DISTANCE;
				if(currentX * currentX + currentY * currentY <= FOVEA_DISTANCE * FOVEA_DISTANCE)
					foveaCircles.add(new Circle(currentX, currentY, depth));
			}
			for(int i = 0; i < depth - 1; i++) {
				currentX += FOVEA_CONE_DISTANCE / 2;
				currentY += FOVEA_CONE_DISTANCE * Math.sqrt(3) / 2;
				if(currentX * currentX + currentY * currentY <= FOVEA_DISTANCE * FOVEA_DISTANCE)
					foveaCircles.add(new Circle(currentX, currentY, depth));
			}
		}
		
		return foveaCircles;
	}

	public static double getInterCircleDistance(double eccentricity) {
		if(eccentricity < FOVEA_DISTANCE)
			return FOVEA_CONE_DISTANCE;
		else
			return FOVEA_CONE_DISTANCE+ PREEXP_PARAM*Math.exp((eccentricity - FOVEA_DISTANCE)/DISTANCE_PARAM);
	}
	
}
