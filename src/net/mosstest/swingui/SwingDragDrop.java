package net.mosstest.swingui;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


public class SwingDragDrop {
	static JFrame frame;
	static JLabel picture;

	public static void main(String[] args) throws IOException {
		setupFrame();
		setupLabels();
		frame.repaint();
	}

	public static void setupFrame() {
		frame = new JFrame("Drag and Drop Test");
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setLayout(null);
	}

	public static void setupLabels() throws IOException {
		ImageIcon icon = new ImageIcon(ImageIO
				.read(new File("item_switch.png")).getScaledInstance(64, 64,
						Image.SCALE_REPLICATE));
		picture = new JLabel(icon);
		picture.setSize(64, 64);
		picture.setLocation(200, 200);
		picture.addMouseListener(new DnDListener(picture));
		frame.add(picture);
	}
}
