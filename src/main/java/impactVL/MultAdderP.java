package impactVL;
import javax.swing.*;
import java.awt.*;

public class MultAdderP extends Personality {
	static private Image image = null;
	public MultAdderP() {
		if (image == null) {	
			image = new ImageIcon(this.getClass().getClassLoader().getResource("impactVL/adder.gif")).getImage();
		}
	}
	public void step(int x, int y) {
		Personality topNeighbor = Common.personalities[x][y-1];
		Personality bottomNeighbor = Common.personalities[x][y+1];
		Personality rightNeighbor = Common.personalities[x+1][y];
		Personality leftNeighbor = Common.personalities[x-1][y];
		step(topNeighbor, Common.BOTTOM, Common.BOTTOM);
		if (leftNeighbor.getCellFull(Common.RIGHT) &&
		    bottomNeighbor.getCellFull(Common.TOP) &&
		    rightNeighbor.getCellFull(Common.LEFT)&&
		    !getCellFull(Common.LEFT) &&
		    !getCellFull(Common.RIGHT)
		    ) {
			boolean in1 = false;
			boolean in2 = false;
			boolean in3 = false;
			try {
				in1 = leftNeighbor.consumeRightOutput();
			} catch (ConsumptionException ce) {
				System.err.println("Hit a consumption exception 1");
			}
			try {
				in2 = bottomNeighbor.consumeTopOutput();
			} catch (ConsumptionException ce) {
				System.err.println("Hit a consumption exception 2");
			}
			try {
				in3 = rightNeighbor.consumeLeftOutput();
			} catch (ConsumptionException ce) {
				System.err.println("Hit a consumption exception 3");
			}
			int sum = 0;
			if (in1) {
				sum++;
			}
			if (in2) {
				sum++;
			}
			if (in3) {
				sum++;
			}
			switch (sum) {
			case 0:
				setOutputData(Common.LEFT, false);
				setOutputData(Common.RIGHT, false);
				break;
			case 1:
				setOutputData(Common.LEFT, false);
				setOutputData(Common.RIGHT, true);
				break;
			case 2:
				setOutputData(Common.LEFT, true);
				setOutputData(Common.RIGHT, false);
				break;
			case 3:
				setOutputData(Common.LEFT, true);
				setOutputData(Common.RIGHT, true);
				break;
			}	
		}
	}
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, 5*M, 5*M, observer);
	}
	public Object clone() {
		return new MultAdderP();
	}
}
