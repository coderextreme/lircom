package impactVL;
public class Common {

	public static int PMAXX = 11;
	public static int PMAXY = 10;
	public static int MMAXX = 10;
	public static int MMAXY = 9;
	public static Cell cells[][] = new Cell[PMAXX][PMAXY];
	public static Personality personalities[][] = new Personality[PMAXX][PMAXY];
	public static Personality modulePersonalities[][] = null;
	public static final int TOP = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int BOTTOM = 3;
	public static boolean changed = false;
	public static boolean color = false;
	public static boolean forever = false;
	public static int M = 15; // size of subcell in pixels
					// multiply by 5 to get cell size
	public static final int SX = 4;
	public static final int SY = 3;
	public static int starty = 0;
	public static int startx = 0;
}
