package net.mosstest.swingui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving dnD events.
 * The class that is interested in processing a dnD
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addDnDListener<code> method. When
 * the dnD event occurs, that object's appropriate
 * method is invoked.
 *
 * @see DnDEvent
 */
public class DnDListener implements MouseListener {
	
	/** The start y. */
	private double startX, startY;
	
	/** The end y. */
	private double endX, endY;
	
	/** The holder. */
	private JLabel holder;
	
	/**
	 * Instantiates a new dn d listener.
	 *
	 * @param parent the parent
	 */
	public DnDListener (JLabel parent) {
		startX = 0;
		startY = 0;
		endX = 0;
		endY = 0;
		holder = parent;
	}
	
	/**
	 * Prints the diagnostics.
	 */
	public void printDiagnostics () {
		System.out.println("START X: "+startX+" START Y: "+startY);
		System.out.println("END X: "+endX+" END Y: "+endY);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent arg0) {
		PointerInfo info = MouseInfo.getPointerInfo();
		Point location = info.getLocation();
		startX = location.getX();
		startY = location.getY();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
		PointerInfo info = MouseInfo.getPointerInfo();
		Point location = info.getLocation();
		endX = location.getX() - 35; //35, 60: modifiers that need to be present or there
		endY = location.getY() - 60; //is a significant offset when placement occurs.
		holder.setLocation((int)endX, (int)endY);
		printDiagnostics();
	}
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
