package impactVL;
import java.awt.*;

public class DontKnowP extends Personality {
	public void step(int x, int y) {
		step(x+1, y, Common.LEFT, Common.LEFT);
		step(x-1, y, Common.RIGHT, Common.BOTTOM);
		step(x, y-1, Common.BOTTOM, Common.RIGHT);
	}
	public void paint(Graphics g) {
		drawArrow(g, M,M+M/2,M*5,M+M/2);
		drawArrow(g, 4*M,3*M+M/2,M+M/2,0);
		drawArrow(g, M+M/2,4*M,0,3*M+M/2);
		g.drawString("Don't Know", SX, 3*M-SY);
	}
	public Object clone() {
		return new DontKnowP();
	}
}
