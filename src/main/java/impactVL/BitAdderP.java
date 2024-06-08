package net.coderextreme.impactVL;
import java.awt.*;
import javax.swing.*;

public class BitAdderP extends Personality {
	static private Image image = null;
	public BitAdderP() {
		if (image == null) {
			image = new ImageIcon(this.getClass().getClassLoader().getResource("impactVL/adder.gif")).getImage();
		}
	}
	public void step(int x, int y) {
		Personality topNeighbor = Common.personalities[x][y-1];
		Personality bottomNeighbor = Common.personalities[x][y+1];
		Personality rightNeighbor = Common.personalities[x+1][y];
		Personality leftNeighbor = Common.personalities[x-1][y];
		if (topNeighbor.getCellFull(Common.BOTTOM) &&
		    bottomNeighbor.getCellFull(Common.TOP) &&
		    rightNeighbor.getCellFull(Common.LEFT)&&
		    !getCellFull(Common.LEFT) &&
		    !getCellFull(Common.RIGHT)
		    ) {
			boolean in1 = false;
			boolean in2 = false;
			boolean in3 = false;
			try {
				in1 = topNeighbor.consumeBottomOutput();
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
			step(leftNeighbor, Common.RIGHT, Common.BOTTOM);
		}
	}
	public void paint(Graphics g) {
		g.drawImage(image, 0, 0, 5*M, 5*M, observer);
	}
	public Object clone() {
		return new BitAdderP();
	}
}
