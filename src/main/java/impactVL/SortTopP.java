package impactVL;
import java.awt.*;

class FlagTopP extends Personality {
	public Object clone() {
		return new FlagTopP();
	}
	public void step(int x, int y) {
		try {
			Personality rightNeighbor = Common.personalities[x+1][y];
			if (!getCellFull(Common.LEFT) &&
				rightNeighbor.getCellFull(Common.LEFT)) {
				boolean data = rightNeighbor.consumeLeftOutput();
				Common.personalities[x][y] = new SortTopP();
				Common.cells[x][y].setPersonality(Common.personalities[x][y]);
				Common.personalities[x][y].setLeftOutput(data);
			}
		} catch (ConsumptionException ce) {
			System.err.println("Hit a consumption exception 20");
		}
	}
	public void paint(Graphics g) {
		g.drawString("FT", SX, 3*M-SY);
	}
}

class FlagTopLessP extends Personality {
	public Object clone() {
		return new FlagTopLessP();
	}
	public void step(int x, int y) {
		try {
			Personality rightNeighbor = Common.personalities[x+1][y];
			if (!getCellFull(Common.LEFT) &&
				rightNeighbor.getCellFull(Common.LEFT)) {
				boolean data = rightNeighbor.consumeLeftOutput();
				//if (data) {
					//Common.personalities[x][y] = new SortTopP();
				//} else {
					Common.personalities[x][y] = new SortTopLessP();
				//}
				Common.cells[x][y].setPersonality(Common.personalities[x][y]);
				Common.personalities[x][y].setLeftOutput(data);
			}
		} catch (ConsumptionException ce) {
			System.err.println("Hit a consumption exception 21");
		}
	}
	public void paint(Graphics g) {
		g.drawString("FTL", SX, 3*M-SY);
	}
}

class FlagTopGreaterP extends Personality {
	public Object clone() {
		return new FlagTopGreaterP();
	}
	public void step(int x, int y) {
		try {
			Personality rightNeighbor = Common.personalities[x+1][y];
			if (!getCellFull(Common.LEFT) &&
				rightNeighbor.getCellFull(Common.LEFT)) {
				boolean data = rightNeighbor.consumeLeftOutput();
				//if (data) {
					//Common.personalities[x][y] = new SortTopP();
				//} else {
					Common.personalities[x][y] = new SortTopGreaterP();
				//}
				Common.cells[x][y].setPersonality(Common.personalities[x][y]);
				Common.personalities[x][y].setLeftOutput(data);
			}
		} catch (ConsumptionException ce) {
			System.err.println("Hit a consumption exception 22");
		}
	}
	public void paint(Graphics g) {
		g.drawString("FTG", SX, 3*M-SY);
	}
}

class SortTopLessP extends Personality {
	public Object clone() {
		return new SortTopLessP();
	}
	public void step(int x, int y) {
		try {
			Personality rightNeighbor = Common.personalities[x+1][y];
			Personality bottomNeighbor = Common.personalities[x][y+1];
			setBottomOutput(rightNeighbor.getLeftOutput());
			if (!getCellFull(Common.LEFT) &&
			    rightNeighbor.getCellFull(Common.LEFT) && 
			    bottomNeighbor.getCellFull(Common.TOP)) {
				boolean bottomdata = bottomNeighbor.consumeTopOutput();
				boolean topdata = rightNeighbor.consumeLeftOutput();
				Common.personalities[x][y] = new FlagTopLessP();
				Common.cells[x][y].setPersonality(Common.personalities[x][y]);
				Common.personalities[x][y].setLeftOutput(bottomdata);
				Common.personalities[x][y].setBottomOutput(topdata);
			}
		} catch (ConsumptionException ce) {
			System.err.println("Hit a consumption exception 23");
		}
	}
	public void paint(Graphics g) {
		g.drawString("STL", SX, 3*M-SY);
	}
}

class SortTopGreaterP extends Personality {
	public Object clone() {
		return new SortTopGreaterP();
	}
	public void step(int x, int y) {
		try {
			Personality rightNeighbor = Common.personalities[x+1][y];
			Personality bottomNeighbor = Common.personalities[x][y+1];
			setBottomOutput(rightNeighbor.getLeftOutput());
			if (!getCellFull(Common.LEFT) &&
			    rightNeighbor.getCellFull(Common.LEFT) && 
			    bottomNeighbor.getCellFull(Common.TOP)) {
				boolean topdata = rightNeighbor.consumeLeftOutput();
				boolean bottomdata = bottomNeighbor.consumeTopOutput();
				Common.personalities[x][y] = new FlagTopGreaterP();
				Common.cells[x][y].setPersonality(Common.personalities[x][y]);
				Common.personalities[x][y].setLeftOutput(topdata);
				Common.personalities[x][y].setBottomOutput(topdata);
			}
		} catch (ConsumptionException ce) {
			System.err.println("Hit a consumption exception 24");
		}
	}
	public void paint(Graphics g) {
		g.drawString("STG", SX, 3*M-SY);
	}
}

public class SortTopP extends Personality {
	public Object clone() {
		return new SortTopP();
	}
	public void step(int x, int y) {
		Personality bottomNeighbor = Common.personalities[x][y+1];
		Personality rightNeighbor = Common.personalities[x+1][y];
		if (rightNeighbor.getCellFull(Common.LEFT)) {
			try {
				setBottomOutput(rightNeighbor.getLeftOutput());
			} catch (ConsumptionException ce) {
				System.err.println("Hit a consumption exception 25");
			}
		}
		if (!getCellFull(Common.LEFT) &&
		    rightNeighbor.getCellFull(Common.LEFT) &&
		    bottomNeighbor.getCellFull(Common.TOP)) {
			try {
				boolean topdata = rightNeighbor.consumeLeftOutput();
				boolean bottomdata = bottomNeighbor.getTopOutput();
				if (topdata && !bottomdata) {
					// never swap
					Common.personalities[x][y] = new FlagTopGreaterP();
					Common.cells[x][y].setPersonality(Common.personalities[x][y]);
					Common.personalities[x][y].setLeftOutput(topdata);
				} else if (!topdata && bottomdata) {
					// swap
					Common.personalities[x][y] = new FlagTopLessP();
					Common.cells[x][y].setPersonality(Common.personalities[x][y]);
					Common.personalities[x][y].setLeftOutput(bottomdata);
				} else {
					Common.personalities[x][y] = new FlagTopP();
					Common.cells[x][y].setPersonality(Common.personalities[x][y]);
					Common.personalities[x][y].setLeftOutput(topdata);
				}
				Common.personalities[x][y].setBottomOutput(topdata);
			} catch (ConsumptionException ce) {
				System.err.println("Hit a consumption exception 26");
			}
		}
	}
/*
	public void step(int x, int y) {
		Personality bottomNeighbor = Common.personalities[x][y+1];
		Personality rightNeighbor = Common.personalities[x+1][y];
		if (rightNeighbor.getCellFull(Common.LEFT)) {
			try {
				setBottomOutput(rightNeighbor.getLeftOutput());
			} catch (ConsumptionException ce) {
				System.err.println("Hit a consumption exception 27");
			}
		}
		if (!getCellFull(Common.LEFT) &&
		    rightNeighbor.getCellFull(Common.LEFT) && 
		    bottomNeighbor.getCellFull(Common.TOP)) {
			try {
				boolean topdata = rightNeighbor.consumeLeftOutput();
				boolean bottomdata = bottomNeighbor.consumeTopOutput();
				if (!flip && topdata && !bottomdata) {
					neverflip = true;
				} else if (!neverflip && !flip && !topdata && bottomdata) {
					// swap
					flip = true;
				}
				if (flip && !neverflip) {
					setLeftOutput(bottomdata);
				} else {
					setLeftOutput(topdata);
				}
			} catch (ConsumptionException ce) {
				System.err.println("Hit a consumption exception 28");
			}
		}
	}
*/
	public void paint(Graphics g) {
		g.drawString("ST", SX, 3*M-SY);
	}
}
