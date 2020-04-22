package com.pcg;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

public class CircleDrawPane extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Circle> circles;
	private float scale = 20;
	
	public CircleDrawPane(ArrayList<Circle> cs) {
		super();
		circles = cs;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.repaint();
		Color col = g.getColor();
		g.setColor(Color.BLACK);
		for(Circle c : circles)
			g.drawOval((int)(c.x * scale + this.getParent().getWidth()/2), (int)(c.y * scale + this.getParent().getHeight()/2), (int) scale, (int) scale);
		g.setColor(col);
	}
}
