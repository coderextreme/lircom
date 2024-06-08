package net.coderextreme.impactVL;
import java.awt.*;

public class PassP extends Personality {
	public void step(int x, int y) {
		step(x+1, y, Common.LEFT, Common.LEFT);
		step(x-1, y, Common.RIGHT, Common.RIGHT);
		step(x, y+1, Common.TOP, Common.TOP);
		step(x, y-1, Common.BOTTOM, Common.BOTTOM);
	}
	public void paint(Graphics g) {
		drawArrow(g, M,M+M/2,5*M,M+M/2);
		drawArrow(g, 4*M,3*M+M/2,0,3*M+M/2);
		drawArrow(g, 3*M+M/2,M,3*M+M/2,5*M);
		drawArrow(g, M+M/2,4*M,M+M/2,0);
		g.drawString("Pass", SX, 3*M-SY);
	}
	public Object clone() {
		return new PassP();
	}
}
