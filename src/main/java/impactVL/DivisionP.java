package net.coderextreme.impactVL;
import java.awt.*;

public class DivisionP extends Personality {
	// linked ones computes 1/b where b is an integer >= 1
	boolean agather = true;
	boolean bgather = true;
	boolean aend = false;
	boolean bend = false;
	boolean aouting = false;
	boolean preaout = false;
	boolean flag = false;
	boolean aflag = false;
	boolean bflag = false;
	long a = 0;
	long b = 0;
	int leftshifts = 0;
        boolean sentaone = false;
	public void step (int x, int y) {
		Personality rightNeighbor = Common.personalities[x+1][y];
		Personality topNeighbor = Common.personalities[x][y-1];
		boolean bottomfull = getCellFull(Common.BOTTOM);
		boolean leftfull = getCellFull(Common.LEFT);
		boolean rnfull = rightNeighbor.getCellFull(Common.LEFT);
	 	boolean tnfull = topNeighbor.getCellFull(Common.BOTTOM);
		if (bgather) {
			System.err.println("bgather true");
			if (!bflag) {
				System.err.println("bflag false");
				if (rnfull) {
					Common.changed = true;
					System.err.println("rnfull true");
					b <<= 1;
					try {
						if (rightNeighbor.consumeLeftOutput()) {
							b |= 1L;
						} else {
							b &= ~1L;
						}
						System.err.println("*****************************b "+b);
						bflag = true;
					} catch (ConsumptionException ce) {
						ce.printStackTrace();
					}
				}
			} else {
				if (rnfull) {
					System.err.println("rnfull true");
					try {
						Common.changed = true;
						bend =  rightNeighbor.consumeLeftOutput();
						System.err.println("*****************************bend "+bend);
						bflag = false;
					} catch (ConsumptionException ce) {
						ce.printStackTrace();
					}
				}
				if (bend) {
					System.err.println("bend true");
					Common.changed = true;
					bgather = false;
					if (!bgather && !agather) {
						preaout = true;
					}
				}
			}
		}
		if (agather) {
			System.err.println("agather true");
			if (!aflag) {
				System.err.println("aflag false");
				if (tnfull) {
					Common.changed = true;
					System.err.println("tnfull true");
					a <<= 1;
					leftshifts++;
					System.err.println("leftshifts++ a = "+a);
					try {
						if (topNeighbor.consumeBottomOutput()) {
							a |= 1L;
						} else {
							a &= ~1L;
						}
						System.err.println("*****************************a "+a);
						aflag = true;
					} catch (ConsumptionException ce) {
						ce.printStackTrace();
					}
				}
			} else {
				if (tnfull) {
					System.err.println("tnfull true");
					try {
						Common.changed = true;
						aend = topNeighbor.consumeBottomOutput();
						System.err.println("*****************************aend "+aend);
						aflag = false;
					} catch (ConsumptionException ce) {
						ce.printStackTrace();
					}
				}
				if (aend) {
					System.err.println("aend true");
					Common.changed = true;
					agather = false;
					if (!bgather && !agather) {
						preaout = true;
					}
				}
			}
		} else if (!leftfull && preaout && !aouting) {
			// reciprocal goes left and remainder goes down
			System.err.println("x,y "+x+", "+y);
			System.err.println("putting out results");
			Common.changed = true;
			if (a < b) {
				Common.personalities[x][y].setLeftOutput(false);
				a <<= 1;
				leftshifts++;
				System.err.println("leftshifts++ a < b, a = "+a);
				aouting = true;
				preaout = false;
			} else if (a > b) {
				Common.personalities[x][y].setLeftOutput(true);
				a -= b;
				a <<= 1;
				leftshifts++;
				System.err.println("leftshifts++ a > b, a = "+a);
				aouting = true;
				preaout = false;
			} else {
				// end
				Common.personalities[x][y].setLeftOutput(true);
				Common.changed = true;
				agather = true;
				bgather = true;
				aend = false;
				bend = false;
				aouting = false;
				preaout = false;
				flag = false;
				aflag = false;
				bflag = false;
				a = 0;
				b = 0;
				leftshifts = 0;
        			sentaone = false;
			}
		} else if (!bottomfull && !preaout && aouting) {
			System.err.println("aouting");
			Common.changed = true;
			if (!flag) {
				System.err.println("flag false");
				if (leftshifts > 0) {
					leftshifts--;
					long masked = a & (1 << leftshifts);
					System.err.println("shifting out "+leftshifts+" bits masked="+masked);
					if (masked != 0) {
						sentaone = true;
						Common.personalities[x][y].setBottomOutput(true);
					} else if (sentaone) {
						Common.personalities[x][y].setBottomOutput(false);
					}
					flag = true;
				}
			} else {
				if (leftshifts > 0) {
					System.err.println("flag true");
					flag = false;
					if (sentaone) {
						Common.personalities[x][y].setBottomOutput(false);
					}
				} else {
					System.err.println("THE END");
					// the end of a
					Common.personalities[x][y].setBottomOutput(true);
					agather = true;
					bgather = true;
					aend = false;
					bend = false;
					aouting = false;
					preaout = false;
					flag = false;
					aflag = false;
					bflag = false;
					a = 0;
					b = 0;
					leftshifts = 0;
        				sentaone = false;
				}
			}
		
		}
	}
	public void paint(Graphics g) {
		g.drawString("Division", SX, 3*M-SY);
	}
	public Object clone() {
		return new DivisionP();
	}
}
