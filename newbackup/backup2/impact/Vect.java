package impact;

public class Vect {
	public double x = 0;
	public double y = 0;
	public double z = 0;
	public Vect() {
	}
	public Object clone() {
		Vect v = new Vect();
		v.x = x;
		v.y = y;
		v.z = z;
		return v;
	}
}
