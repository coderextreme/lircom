package impactVL;
import java.awt.*;

public class RightTurnP extends Personality {
	public void step(int x, int y) {
		step(x, y-1, Common.BOTTOM, Common.LEFT);
		step(x, y+1, Common.TOP, Common.RIGHT);
		step(x-1, y, Common.RIGHT, Common.BOTTOM);
		step(x+1, y, Common.LEFT, Common.TOP);
	}
	public void paint(Graphics g) {
		drawArrow(g, M,M+M/2,M+M/2,0);
		drawArrow(g, 3*M+M/2,M,5*M,M+M/2);
		drawArrow(g, 4*M,3*M+M/2,3*M+M/2,5*M);
		drawArrow(g, M+M/2,4*M,0,3*M+M/2);
		g.drawString("Right Turn", SX, 3*M-SY);
	}
	public Object clone() {
		return new RightTurnP();
	}
}
