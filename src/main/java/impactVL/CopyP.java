package net.coderextreme.impactVL;
import java.awt.*;
import javax.swing.*;

public class CopyP extends Personality {
	static private Image image = null;
	public CopyP() {
		if (image == null) {
			image = new ImageIcon(this.getClass().getClassLoader().getResource("impactVL/copy.gif")).getImage();
		}
	}
	public void step(int x, int y) {
		// TODO this right
		step(x+1, y, Common.RIGHT, Common.TOP);
		step(x, y-1, Common.RIGHT, Common.RIGHT);
		step(x-1, y, Common.BOTTOM, Common.BOTTOM);
	}
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, 5*M, 5*M, observer);
	}
	public Object clone() {
		return new CopyP();
	}
}
