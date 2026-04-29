package impactVL;
public class EmptyP extends Personality {
	public EmptyP() {}
	public void step(int x, int y) {
	}
	public Object clone() {
		return new EmptyP();
	}
}
