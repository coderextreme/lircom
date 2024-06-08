package net.coderextreme.impactVL;
import java.awt.*;

abstract public class Personality {
	public static int M = Common.M;
	public static int SX = Common.SX;
	public static int SY = Common.SY;
	protected boolean lOutput = false;
	protected boolean rOutput = false;
	protected boolean tOutput = false;
	protected boolean bOutput = false;
	protected boolean lFull = false;
	protected boolean rFull = false;
	protected boolean tFull = false;
	protected boolean bFull = false;
	protected Cell observer = null;
	public abstract void step(int x, int y);
	public boolean step(int xn, int yn, int from, int to, boolean outNotFull) {
		Personality neighbor = Common.personalities[xn][yn];
		return step(neighbor, from, to, outNotFull);
	}
	public boolean step(int xn, int yn, int from, int to) {
		Personality neighbor = Common.personalities[xn][yn];
		return step(neighbor, from, to);
	}
	public boolean step(Personality neighbor, int from, int to) {
		return step(neighbor, from, to, false);
	}
	public boolean step(Personality neighbor, int from, int to, boolean outNotFull) {
		boolean inData = false;
		boolean neighborFull = neighbor.getCellFull(from);
		boolean full = getCellFull(to);
		if (neighborFull && (!full || outNotFull)) {
			try {
				inData = neighbor.getInputData(from);
				setOutputData(to, inData);
			} catch (ConsumptionException ce) {
				ce.printStackTrace();
				System.err.println("Hit a consumption exception!!!!");
			}
		}
		return inData;
	}

	public boolean consumeTopOutput() throws ConsumptionException {
		if (!tFull) {
			throw new ConsumptionException();
		}
		tFull = false;
		Common.changed = true;
		return tOutput;
	}
	public boolean consumeBottomOutput() throws ConsumptionException {
		if (!bFull) {
			throw new ConsumptionException();
		}
		bFull = false;
		Common.changed = true;
		return bOutput;
	}
	public boolean consumeLeftOutput() throws ConsumptionException {
		if (!lFull) {
			throw new ConsumptionException();
		}
		lFull = false;
		Common.changed = true;
		return lOutput;
	}
	public boolean consumeRightOutput() throws ConsumptionException {
		if (!rFull) {
			throw new ConsumptionException();
		}
		rFull = false;
		Common.changed = true;
		return rOutput;
	}
	public String getTopDisplay() {
		return tOutput ? "1" : "0";
	}
	public String getBottomDisplay() {
		return bOutput ? "1" : "0";
	}
	public String getLeftDisplay() {
		return lOutput ? "1" : "0";
	}
	public String getRightDisplay() {
		return rOutput ? "1" : "0";
	}
	public boolean getTopOutput() throws ConsumptionException {
		return tOutput;
	}
	public boolean getBottomOutput() throws ConsumptionException {
		return bOutput;
	}
	public boolean getLeftOutput() throws ConsumptionException {
		return lOutput;
	}
	public boolean getRightOutput() throws ConsumptionException {
		return rOutput;
	}
	public void setTopOutput(boolean out) {
		tFull = true;
		Common.changed = true;
		tOutput = out;
	}
	public void setBottomOutput(boolean out) {
		bFull = true;
		Common.changed = true;
		bOutput = out;
	}
	public void setLeftOutput(boolean out) {
		lFull = true;
		Common.changed = true;
		lOutput = out;
	}
	public void setRightOutput(boolean out) {
		rFull = true;
		Common.changed = true;
		rOutput = out;
	}
	public boolean getCellFull(int direct) {
		boolean full = false;
		if (direct == Common.LEFT) {
			full = lFull;
		} else if (direct == Common.RIGHT) {
			full = rFull;
		} else if (direct == Common.TOP) {
			full = tFull;
		} else if (direct == Common.BOTTOM) {
			full = bFull;
		}
		return full;
	}
	public boolean getInputData(int direct) throws ConsumptionException {
		boolean inData = false;
		if (direct == Common.LEFT) {
			inData = consumeLeftOutput();
		} else if (direct == Common.RIGHT) {
			inData = consumeRightOutput();
		} else if (direct == Common.TOP) {
			inData = consumeTopOutput();
		} else if (direct == Common.BOTTOM) {
			inData = consumeBottomOutput();
		}
		return inData;
	}
	public void setOutputData(int direct, boolean outData) {
		if (direct == Common.LEFT) {
			setLeftOutput(outData);
		} else if (direct == Common.RIGHT) {
			setRightOutput(outData);
		} else if (direct == Common.TOP) {
			setTopOutput(outData);
		} else if (direct == Common.BOTTOM) {
			setBottomOutput(outData);
		}
	}
	public void paint(Graphics g) {
	}
	public void drawHArrow(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
		if (x1 < x2) {
			g.drawLine(x1, y1, x1+2, y1+2);
			g.drawLine(x1, y1, x1+2, y1-2);
		} else {
			g.drawLine(x1, y1, x1-2, y1+2);
			g.drawLine(x1, y1, x1-2, y1-2);
		}
	}
	public void drawVArrow(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
		if (y1 < y2) {
			g.drawLine(x1, y1, x1+2, y1+2);
			g.drawLine(x1, y1, x1-2, y1+2);
		} else {
			g.drawLine(x1, y1, x1+2, y1-2);
			g.drawLine(x1, y1, x1-2, y1-2);
		}
	}
	public void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
		g.drawLine(x1, y1, x2, y2);
		double th = Math.atan2(y1 - y2, x2 - x1);
		g.fillArc(x1-M/2, y1-M/2, M, M, (int)(th*180/3.1416)-20, 40);
	}
	public void setObserver(Cell c) {
		observer = c;
	}
	abstract public Object clone();
}
