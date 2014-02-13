package net.mosstest.swingui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

public class DnDListener implements MouseListener {
	
	private double startX, startY;
	private double endX, endY;
	private JLabel holder;
	
	public DnDListener (JLabel parent) {
		startX = 0;
		startY = 0;
		endX = 0;
		endY = 0;
		holder = parent;
	}
	
	public void printDiagnostics () {
		System.out.println("START X: "+startX+" START Y: "+startY);
		System.out.println("END X: "+endX+" END Y: "+endY);
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		PointerInfo info = MouseInfo.getPointerInfo();
		Point location = info.getLocation();
		startX = location.getX();
		startY = location.getY();
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		PointerInfo info = MouseInfo.getPointerInfo();
		Point location = info.getLocation();
		endX = location.getX() - 35; //35, 60: modifiers that need to be present or there
		endY = location.getY() - 60; //is a significant offset when placement occurs.
		holder.setLocation((int)endX, (int)endY);
		printDiagnostics();
	}
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
