package net.coderextreme.impactVL;
public class Division {
	private static int NUMDECIMALDIGITS = 16;
	static public void main(String args[]) throws Exception {

		double n = Double.parseDouble(args[0]);
		double d = Double.parseDouble(args[1]);
		System.out.println("Java "+(n / d));
		long time;

		try {
			time = System.currentTimeMillis();
			double result2 = NewtonRaphson(n, d);
			System.out.println("Newton Raphson with loops "+result2);
			System.out.println("Time="+(System.currentTimeMillis()-time));
		} catch (Exception e) {
			System.err.println("Failed to compute Newton Raphson");
		}

		try {
			time = System.currentTimeMillis();
			double result = divide(n, d);
			System.out.println("John's Algorithm "+result);
			System.out.println("Time="+(System.currentTimeMillis()-time));
		} catch (Exception e) {
			System.err.println("Failed to compute John's Algorithm");
		}

		if ((d >= 1 && d < 2) || (d <= -1 && d > -2)) {
			time = System.currentTimeMillis();
			double result3 = UnrolledNewtonRaphson(d);
			System.out.println("Newton Raphson Unrolled "+(n*result3));
			System.out.println("Time="+(System.currentTimeMillis()-time));
		} else {
			System.out.println("divisor should be in [1,2) or (-2,-1]");
		}

		try {
			if (d <= 0) {
				System.out.println("denominator should be natural number");
			} else if (n == 1) {
				time = System.currentTimeMillis();
				String result4 = chainedPersonalities((long)d);
				System.out.println("ChainedPersonalities in Binary "+result4);
				System.out.println("Time="+(System.currentTimeMillis()-time));
			} else {
				System.out.println("numerator should 1");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed at chainedPersonalities");
		}
		
	}
	// ASSUMPTION: |b| >= 1 and |b| < 2 ("normalized" base, exponent elsewher)
	// compute 1/b
	static public double UnrolledNewtonRaphson (double b) throws Exception {
		if (b == 0 || b == -0) {
			throw new Exception("Can't divide by 0");
		}
		boolean negative = false;
		if (b < 0) {
			negative = !negative;
			b = -b;
		}
		double y = 1;
		if (b > 1.5) {
			y = 0.5;
		}
		double t = 0.0;
		// run through Newton Raphson 5 times (with extra term for speed).
		t = (1 - b * y);
		y = y * (1 + t + t * t);
		t = (1 - b * y);
		y = y * (1 + t + t * t);
		t = (1 - b * y);
		y = y * (1 + t + t * t);
		t = (1 - b * y);
		y = y * (1 + t + t * t);
		t = (1 - b * y);
		y = y * (1 + t + t * t);
		t = (1 - b * y);
		y = y * (1 + t + t * t);
		t = (1 - b * y);
		y = y * (1 + t + t * t);
		t = (1 - b * y);
		y = y * (1 + t + t * t);
		t = (1 - b * y);
		y = y * (1 + t + t * t);
		t = (1 - b * y);
		y = y * (1 + t + t * t);
		if (negative) {
			y = -y;
		}
		return y;
	}
	static public double divide (double n, double d) throws Exception {
		if (n == 0) {
			return 0;
		}
		if (d == 0 || d == -0) {
			throw new Exception("Can't divide by 0");
		}
		int first = 1;
		int second = 1;
		String negative = "";
		if (n < 0 && d < 0) {
		} else if (n < 0) {
			negative = "-";
		} else if (d < 0) {
			negative = "-";
		}
		if (n < 0) {
			n = -n;
		}
		if (d < 0) {
			d = -d;
		}
		int exp = 0;
		while (n < 1) {
			exp = exp - 1;
			n *= 10;
		}
		while (n > 10) {
			exp = exp + 1;
			n /= 10;
		}
		while (d < 1) {
			exp = exp + 1;
			d *= 10;
		}
		while (d > 10) {
			exp = exp - 1;
			d /= 10;
		}
		int numtimes = 0;
		while (n - d >= 0) {
			numtimes++;
			n -= d;
		}
		StringBuffer sb = new StringBuffer(negative+(first*numtimes)+".");
		int precision = 0;
		for (; precision < NUMDECIMALDIGITS && n != 0; precision++) {
			n *= 10;
			numtimes = 0;
			while (n - d >= 0) {
				numtimes++;
				n -= d;
			}
			sb.append(""+numtimes);
		}
		if (precision == 0) {
			sb.append("0");
		}
		if (exp != 0) {
			sb.append("E"+exp);
		}
		return Double.parseDouble(sb.toString());
	}
	static public double NewtonRaphson (double a, double b) throws Exception {
		if (b == 0 || b == -0) {
			throw new Exception("Can't divide by 0");
		}
		int add = 0;
		int mul = 0;
		boolean negative = false;
		if (a < 0) {
			negative = !negative;
			a = -a;
		}
		if (b < 0) {
			negative = !negative;
			b = -b;
		}
		double y = Double.MIN_VALUE;
/*
		y = 1;
		if (b > 1.5) {
			y = 0.5;
		}
*/
		double oldy = 0;
		int times = 0;
		do {
			oldy = y;
			double t = (1 - b * y);
			add++;
			mul++;
			y = y * (1 + t + t * t);
			add += 2;
			mul += 2;
			times++;
		} while(oldy - y  != 0.0);
		if (negative) {
			a = -a;
		}
		System.out.println("times = "+times+" adds = "+add+" muls = "+mul);
		return a * y;
	}
	static String chainedPersonalities(long b) {
		StringBuffer abin = new StringBuffer("1");
		int sigdig = 80;
		StringBuffer out = new StringBuffer();
		boolean done = false;
		while (sigdig > 0 && !done) {
			long a = Long.parseLong(abin.toString(), 2);
			if (a < b) {
				// System.err.print("0");
				out.append("0");
				abin.append("0");
				sigdig--;
			} else if (a > b) {
				// System.err.print("1");
				out.append("1");
				sigdig--;
				a -= b;
				abin.replace(0, abin.length(), Long.toBinaryString(a)+"0");
			} else {
				// System.err.print("1");
				out.append("1");
				sigdig--;
				done = true;
			}
		}
		// System.err.println(" ");
		out.replace(1, 1, ".");
		return out.toString();
	}
}
