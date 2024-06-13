package impactVL;
import java.awt.*;

class FlagBottomP extends Personality {
	public Object clone() {
		return new FlagBottomP();
	}
	public void step(int x, int y) {
		try {
			Personality rightNeighbor = Common.personalities[x+1][y];
			if (!getCellFull(Common.LEFT) &&
				rightNeighbor.getCellFull(Common.LEFT)) {
				boolean data = rightNeighbor.consumeLeftOutput();
				Common.personalities[x][y] = new SortBottomP();
				Common.cells[x][y].setPersonality(Common.personalities[x][y]);
				Common.personalities[x][y].setLeftOutput(data);
			}
		} catch (ConsumptionException ce) {
			System.err.println("Hit a consumption exception 10");
		}
	}
	public void paint(Graphics g) {
		g.drawString("FB", SX, 3*M-SY);
	}
}

class FlagBottomLessP extends Personality {
	public Object clone() {
		return new FlagBottomLessP();
	}
	public void step(int x, int y) {
		try {
			Personality rightNeighbor = Common.personalities[x+1][y];
			if (!getCellFull(Common.LEFT) &&
				rightNeighbor.getCellFull(Common.LEFT)) {
				boolean data = rightNeighbor.consumeLeftOutput();
				//if (data) {
					//Common.personalities[x][y] = new SortBottomP();
				//} else {
					Common.personalities[x][y] = new SortBottomLessP();
				//}
				Common.cells[x][y].setPersonality(Common.personalities[x][y]);
				Common.personalities[x][y].setLeftOutput(data);
			}
		} catch (ConsumptionException ce) {
			System.err.println("Hit a consumption exception 11");
		}
	}
	public void paint(Graphics g) {
		g.drawString("FBL", SX, 3*M-SY);
	}
}

class FlagBottomGreaterP extends Personality {
	public Object clone() {
		return new FlagBottomGreaterP();
	}
	public void step(int x, int y) {
		try {
			Personality rightNeighbor = Common.personalities[x+1][y];
			if (!getCellFull(Common.LEFT) &&
				rightNeighbor.getCellFull(Common.LEFT)) {
				boolean data = rightNeighbor.consumeLeftOutput();
				//if (data) {
					//Common.personalities[x][y] = new SortBottomP();
				//} else {
					Common.personalities[x][y] = new SortBottomGreaterP();
				//}
				Common.cells[x][y].setPersonality(Common.personalities[x][y]);
				Common.personalities[x][y].setLeftOutput(data);
			}
		} catch (ConsumptionException ce) {
			System.err.println("Hit a consumption exception 12");
		}
	}
	public void paint(Graphics g) {
		g.drawString("FBG", SX, 3*M-SY);
	}
}

class SortBottomLessP extends Personality {
	public Object clone() {
		return new SortBottomLessP();
	}
	public void step(int x, int y) {
		try {
			Personality topNeighbor = Common.personalities[x][y-1];
			Personality rightNeighbor = Common.personalities[x+1][y];
			setTopOutput(rightNeighbor.getLeftOutput());
			if (!getCellFull(Common.LEFT) &&
			    rightNeighbor.getCellFull(Common.LEFT) && 
			    topNeighbor.getCellFull(Common.BOTTOM)) {
				boolean bottomdata = rightNeighbor.consumeLeftOutput();
				boolean topdata = topNeighbor.consumeBottomOutput();
				Common.personalities[x][y] = new FlagBottomLessP();
				Common.cells[x][y].setPersonality(Common.personalities[x][y]);
				Common.personalities[x][y].setLeftOutput(bottomdata);
				Common.personalities[x][y].setTopOutput(bottomdata);
			}
		} catch (ConsumptionException ce) {
			System.err.println("Hit a consumption exception 13");
		}
	}
	public void paint(Graphics g) {
		g.drawString("SBL", SX, 3*M-SY);
	}
}

class SortBottomGreaterP extends Personality {
	public Object clone() {
		return new SortBottomGreaterP();
	}
	public void step(int x, int y) {
		try {
			Personality topNeighbor = Common.personalities[x][y-1];
			Personality rightNeighbor = Common.personalities[x+1][y];
			setTopOutput(rightNeighbor.getLeftOutput());
			if (!getCellFull(Common.LEFT) &&
			    rightNeighbor.getCellFull(Common.LEFT) && 
			    topNeighbor.getCellFull(Common.BOTTOM)) {
				boolean bottomindata = rightNeighbor.consumeLeftOutput();
				boolean topindata = topNeighbor.consumeBottomOutput();
				Common.personalities[x][y] = new FlagBottomGreaterP();
				Common.cells[x][y].setPersonality(Common.personalities[x][y]);
				Common.personalities[x][y].setLeftOutput(topindata);
				Common.personalities[x][y].setTopOutput(bottomindata);
			}
		} catch (ConsumptionException ce) {
			System.err.println("Hit a consumption exception 14");
		}
	}
	public void paint(Graphics g) {
		g.drawString("SBG", SX, 3*M-SY);
	}
}

public class SortBottomP extends Personality {
	public Object clone() {
		return new SortBottomP();
	}
	public void step(int x, int y) {
		Personality topNeighbor = Common.personalities[x][y-1];
		Personality rightNeighbor = Common.personalities[x+1][y];
		if (rightNeighbor.getCellFull(Common.LEFT)) {
			try {
				setTopOutput(rightNeighbor.getLeftOutput());
			} catch (ConsumptionException ce) {
				System.err.println("Hit a consumption exception 15");
			}
		}
		if (!getCellFull(Common.LEFT) &&
		    rightNeighbor.getCellFull(Common.LEFT) &&
		    topNeighbor.getCellFull(Common.BOTTOM)) {
			try {
				boolean topdata = topNeighbor.getBottomOutput();
				boolean bottomdata = rightNeighbor.consumeLeftOutput();
				if (topdata && !bottomdata) {
					// never swap
					Common.personalities[x][y] = new FlagBottomLessP();
					Common.cells[x][y].setPersonality(Common.personalities[x][y]);
					Common.personalities[x][y].setLeftOutput(bottomdata);
				} else if (!topdata && bottomdata) {
					// swap
					Common.personalities[x][y] = new FlagBottomGreaterP();
					Common.cells[x][y].setPersonality(Common.personalities[x][y]);
					Common.personalities[x][y].setLeftOutput(topdata);
				} else {
					Common.personalities[x][y] = new FlagBottomP();
					Common.cells[x][y].setPersonality(Common.personalities[x][y]);
					Common.personalities[x][y].setLeftOutput(bottomdata);
				}
				Common.personalities[x][y].setTopOutput(bottomdata);
			} catch (ConsumptionException ce) {
				System.err.println("Hit a consumption exception 16");
			}
		}
	}
	public void paint(Graphics g) {
		g.drawString("SB", SX, 3*M-SY);
	}
}
