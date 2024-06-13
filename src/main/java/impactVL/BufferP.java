package impactVL;
import java.awt.event.*;
import java.awt.*;

public class BufferP extends Personality {
	private StringBuffer in = new StringBuffer();
	private StringBuffer out = new StringBuffer();
	public void keyPressed(KeyEvent e) {
		char c = e.getKeyChar();
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_LEFT:
			break;
		case KeyEvent.VK_BACK_SPACE:
			if (in.length() > 0) {
				in.deleteCharAt(in.length()-1);
				if (observer != null) {
					observer.repaint();
				}
			}
			break;
		default:
			addToInBuffer(c);
			if (observer != null) {
				observer.repaint();
			}
			break;
		}
	}
	public void paint(Graphics g) {
		if (in != null) {
			g.drawString("in:"+in.toString(), SX, 2*M-SY);
		}
		g.drawString("Buffer", SX, 3*M-SY);
		if (out != null) {
			g.drawString("out:"+out.toString(), SX, 4*M-SY);
		}
	}
	public String getIn() {
		return in.toString();
	}
	public String getOut() {
		return out.toString();
	}
	public void setIn(String s) {
		in = new StringBuffer(s);
	}
	public void setOut(String s) {
		out = new StringBuffer(s);
	}
	public void addToInBuffer(char value) {
		in.append(value);
		Common.changed = true;
		tFull = true;
		bFull = true;
		rFull = true;
		lFull = true;
	}
	public boolean getCellFull(int direct) {
		return (in.length() > 0);
	}
	public boolean getOutput() throws ConsumptionException {
		if (in.length() > 0) {
			char ch = in.charAt(0);
			in.deleteCharAt(0);
			//System.err.println("deleted a "+ch);
			Common.changed = true;
			tFull = true;
			bFull = true;
			rFull = true;
			lFull = true;
			if (ch == '1') {
				return true;
			} else {
				return false;
			}
		} else {
			tFull = false;
			bFull = false;
			rFull = false;
			lFull = false;
			throw new ConsumptionException();
		}
	}
	public boolean consumeTopOutput() throws ConsumptionException {
		return getOutput();
	}
	public boolean consumeBottomOutput() throws ConsumptionException {
		return getOutput();
	}
	public boolean consumeLeftOutput() throws ConsumptionException {
		return getOutput();
	}
	public boolean consumeRightOutput() throws ConsumptionException {
		return getOutput();
	}

	public boolean peekOutput() throws ConsumptionException {
		if (in.length() > 0) {
			char ch = in.charAt(0);
			if (ch == '1') {
				return true;
			} else {
				return false;
			}
		} else {
			throw new ConsumptionException();
		}
	}
	public boolean getTopOutput() throws ConsumptionException {
		return peekOutput();
	}
	public boolean getBottomOutput() throws ConsumptionException {
		return peekOutput();
	}
	public boolean getLeftOutput() throws ConsumptionException {
		return peekOutput();
	}
	public boolean getRightOutput() throws ConsumptionException {
		return peekOutput();
	}
	public void setOutputData(int direct, boolean outData) {
		out.append(outData ? "1" : "0");
		Common.changed = true;
	}
	public void step(int x, int y) {
		if (x ==  0) {
			step(x+1, y, Common.LEFT, Common.LEFT, true);
		}
		if (x == Common.PMAXX-1) {
			step(x-1, y, Common.RIGHT, Common.RIGHT, true);
		}
		if (y == 0) {
			step(x, y+1, Common.TOP, Common.TOP, true);
		}
		if (y == Common.PMAXY-1) {
			step(x, y-1, Common.BOTTOM, Common.BOTTOM, true);
		}
		// Common.cells[x-Common.startx][y-Common.starty].repaint();
	}
	public Object clone() {
		return new BufferP();
	}
}
