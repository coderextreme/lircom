package net.coderextreme.impactVL;
public class EmptyP extends Personality {
	public void step(int x, int y) {
	}
	public Object clone() {
		return new EmptyP();
	}
}
