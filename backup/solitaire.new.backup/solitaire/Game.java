import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

class Element implements Comparable { 
	double value = -100;
	public boolean equals(Element e) {
 		return e.getClass() == getClass() && e.value == value;
	}
	public void print(PrintStream ps) {
		ps.println(getClass().getName()+" "+value);
	}
	public int compareTo(Object e) {
		int d = getClass().getName().compareTo(e.getClass().getName());
		if (d == 0) {
			if (value < ((Element)e).value) {
				d = -1;
			} else if (value > ((Element)e).value) {
				d = 1;
			}
		}
		return d;
	}
}

class OneOfElement extends Element {
	public OneOfElement(int value) {
		this.value = value;
	}
}

class TruthElement extends Element {
	public TruthElement(double value) {
		this.value = value;
	}
}

class FromStackElement extends OneOfElement {
	public FromStackElement(int value) {
		super(value);
	}
}

class ToStackElement extends OneOfElement {
	public ToStackElement(int value) {
		super(value);
	}
}

class PositionElement extends OneOfElement {
	public PositionElement(int value) {
		super(value);
	}
}

class RankElement extends OneOfElement {
	public RankElement(int value) {
		super(value);
	}
}

class RankDifferenceElement extends OneOfElement {
	public RankDifferenceElement(int value) {
		super(value);
	}
}

class SuitDifferenceElement extends OneOfElement {
	public SuitDifferenceElement(int value) {
		super(value);
	}
}

class ClassificationElement extends OneOfElement {
	public ClassificationElement(int value) {
		super(value);
	}
}

class StacksSameElement extends TruthElement {
	public StacksSameElement(double value) {
		super(value);
	}
}

class CardNullElement extends TruthElement {
	public CardNullElement(double value) {
		super(value);
	}
}

class CardFaceUpElement extends TruthElement {
	public CardFaceUpElement(double value) {
		super(value);
	}
}

class CardOnFromStackElement extends TruthElement {
	public CardOnFromStackElement(double value) {
		super(value);
	}
}

class CardIsBottomElement extends TruthElement {
	public CardIsBottomElement(double value) {
		super(value);
	}
}

class BelowNullElement extends TruthElement {
	public BelowNullElement(double value) {
		super(value);
	}
}

class BelowFaceUpElement extends TruthElement {
	public BelowFaceUpElement(double value) {
		super(value);
	}
}

class ToStackEmptyElement extends TruthElement {
	public ToStackEmptyElement(double value) {
		super(value);
	}
}

class SubLogic {
	double stacksSame = 0.5;
	double cimNull = 0.5;
	double cimFaceUp = 0.5;
	double cimOnFromStack = 0.5;
	double isBottom = 0.5;
	double belowNull = 0.5;
	double belowFaceUp = 0.5;
	double toEmpty = 0.5;
	int position = -100;
	int rank = -100;
	int rankDifference = -100;
	int suitDifference = -100;
	int classification = -100;
	ArrayList elements = new ArrayList();

	public boolean equals(SubLogic sl) {
		return (
		belowFaceUp == sl.belowFaceUp &&
		belowNull == sl.belowNull &&
		rankDifference == sl.rankDifference &&
		suitDifference == sl.suitDifference &&
		cimFaceUp == sl.cimFaceUp &&
		cimNull == sl.cimNull &&
		toEmpty == sl.toEmpty &&
		isBottom == sl.isBottom &&
		rank == sl.rank &&
		position == sl.position &&
		cimOnFromStack == sl.cimOnFromStack &&
		stacksSame == sl.stacksSame &&
		classification == sl.classification
		);
	}
	public void print(PrintStream ps) {
		ps.print(","+belowFaceUp+","+belowNull+","+
			rankDifference+","+suitDifference+","+cimFaceUp+","+
			cimNull+","+toEmpty+","+isBottom+","+
			rank+","+position+","+cimOnFromStack+","+stacksSame+
			","+classification);
	}
	public SubLogic(int goodMove, Game g, Logic l, int fromStack, int toStack, CardItem cim, int pos) {
		stacksSame = (fromStack == toStack ? 1.0 : 0.0);
		cimNull = (cim == null ? 1.0 : 0.0);
		if(cim != null) {
			cimFaceUp = (cim.faceUp ? 1.0 : 0.0);
			position = cim.position;
			rank = cim.rank;
			cimOnFromStack = (cim.stack == fromStack ? 1.0 : 0.0);
			isBottom = (g.getStack(fromStack).isBottomCard(cim) ? 1.0 : 0.0);
		}
		CardItem below = g.getStack(toStack).getTopCard();
		belowNull = (below == null ? 1.0 : 0.0);
		if (below != null) {
			belowFaceUp = (below.faceUp ? 1.0 : 0.0);
		}
		if (below != null && cim != null) {
			rankDifference = below.rank - cim.rank;
			/**
			 * suit difference
			 * if 0, same suit == 0
			 * if odd, different color == 1
			 * if even, same color == 2
			 */
			if (below.suit == cim.suit) {
				suitDifference = 0;
			} else if (below.suit + cim.suit % 2 == 1) {
				suitDifference = 1;
			} else {
				suitDifference = 2;
			}
		}
		toEmpty = (g.getStack(toStack).isEmpty() ? 1.0 : 0.0);
		classification = goodMove;
		elements.add(new FromStackElement(fromStack));
		elements.add(new ToStackElement(toStack));
		elements.add(new StacksSameElement(stacksSame));
		elements.add(new CardNullElement(cimNull));
		elements.add(new CardFaceUpElement(cimFaceUp));
		elements.add(new PositionElement(position));
		elements.add(new RankElement(rank));
		elements.add(new CardOnFromStackElement(cimOnFromStack));
		elements.add(new CardIsBottomElement(isBottom));
		elements.add(new BelowNullElement(belowNull));
		elements.add(new BelowFaceUpElement(belowFaceUp));
		elements.add(new RankDifferenceElement(rankDifference));
		elements.add(new SuitDifferenceElement(suitDifference));
		elements.add(new ToStackEmptyElement(toEmpty));
		elements.add(new ClassificationElement(classification));
	}
	public ArrayList getElements() {
		return elements;
	}
}
class Logic {
	static public Logic stacks[][] = new Logic[15][15];
	ArrayList ors = new ArrayList();
	static ArrayList sets = new ArrayList();
	static ArrayList intersections = new ArrayList();
	static public void construct(int goodMove, Game g, int fromStack, int toStack, CardItem cim, int pos) {
		if (stacks[fromStack][toStack] == null) {
			stacks[fromStack][toStack] = new Logic();
		}
		SubLogic sl = new SubLogic(goodMove, g, stacks[fromStack][toStack], fromStack, toStack, cim, pos);
		ArrayList al = sl.getElements();
		Iterator i = stacks[fromStack][toStack].ors.iterator();
		while (i.hasNext()) {
			if (sl.equals((SubLogic)i.next())) {
				System.err.println("logic is same, escaping");
				return; // get out of method
			}
		}
		System.err.println("adding new rule");
		// if not found, add
		stacks[fromStack][toStack].ors.add(sl);
		GameSet s = new GameSet();
		s.addAll(al);
		i = sets.iterator();
		while (i.hasNext()) {
			GameSet o = (GameSet)i.next();

			GameSet intersection = new GameSet();
			intersection.addAll(o);
			intersection.retainAll(s);
/*
			if (s.size() > 0) {
				System.err.println("added set has "+s.size()+" elements");
			}
			if (o.size() > 0) {
				System.err.println("existing set has "+o.size()+" elements");
			}
			if (intersection.size() > 0) {
				System.err.println("Intersection has something in it");
			}

*/
			boolean found = false;
			Iterator j = intersections.iterator();
			while (j.hasNext()) {
				GameSet intersection2 = (GameSet)j.next();
				if (intersection.equals(intersection2)) {
					found = true;
					intersection = intersection2;
					break;
				}
			}
			if (!found) {
				intersections.add(intersection);
				System.err.println("Discovered new concept");
			}
			s.addIntersection(intersection);
			intersection.addSetPair(s);
			o.addIntersection(intersection);
			intersection.addSetPair(o);
		}
		sets.add(s);
		
	}
	static public void print() {
		try {
			FileOutputStream fos = new FileOutputStream("intersections");
			PrintStream ps = new PrintStream(fos);
			Iterator i = intersections.iterator();
			while (i.hasNext()) {
				ps.println("New rule");
				GameSet s = (GameSet)i.next();
				s.print(ps);
			}
			ps.close();
			fos.close();
/*
			FileOutputStream fos = new FileOutputStream("solitaire.names");
			PrintStream ps = new PrintStream(fos);
			ps.println("legalMove.");
			ps.println();
			ps.println("From: -1,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15.");
			ps.println("To: -1,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15.");
			ps.println("belowFaceUp: continuous.");
			ps.println("belowNull: false,true.");
			ps.println("rankDifference: [ordered] -100,-12,-11,-10,-9,-8,-7,-6,-5,-4,-3,-2,-1,0,1,2,3,4,5,6,7,8,9,10,11,12.");
			ps.println("suitDifference: -1,0,1,2.");
			ps.println("cimFaceUp: continuous.");
			ps.println("cimNull: false,true.");
			ps.println("toEmpty: continuous.");
			ps.println("isBottom: continuous.");
			ps.println("rank: [ordered] 0,1,2,3,4,5,6,7,8,9,10,11,12,13.");
			ps.println("position: [ordered] -1,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51.");
			ps.println("cimOnFromStack: continuous.");
			ps.println("stacksSame: continuous.");
			ps.println("legalMove: 0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18.");
			ps.close();
			fos.close();
			fos = new FileOutputStream("solitaire.data");
			ps = new PrintStream(fos);
			
			for (int fs = 0; fs < Stack.totstack; fs++) {
				for (int ts = 0; ts < Stack.totstack; ts++) {
					Logic l = stacks[fs][ts];
					if (l != null) {
						Iterator i = l.ors.iterator();
						while (i.hasNext()) {
							SubLogic sl = (SubLogic)i.next();
							ps.print(fs+","+ts);
							sl.print(ps);
							ps.println();
						}
					}
				}
			}
			ps.close();
			fos.close();
*/
		} catch (Exception e) {
			System.err.println("Cannot create rule file");
			e.printStackTrace();
		}
	}
}

class GameSet extends TreeSet {
	ArrayList setPairs = new ArrayList(); // intersections
	public void addIntersection(GameSet intersection) { // for normal sets
		SetPair sp = new SetPair();
		sp.elementsNotInIntersection.addAll(this);
		sp.elementsNotInIntersection.removeAll(intersection);
		setPairs.add(sp);
	}
	public void addSetPair(GameSet otherset) { // for intersections
		SetPair sp = new SetPair();
		sp.elementsNotInIntersection.addAll(otherset);
		sp.elementsNotInIntersection.removeAll(this);
		setPairs.add(sp);
	}
	public void print(PrintStream ps) {
		Iterator i = iterator();
		ps.println("Applies to "+setPairs.size()+" rules, with "+size()+" in agreement");
		while (i.hasNext()) {
			Element e = (Element)i.next();
			e.print(ps);
		}
		Iterator spi = setPairs.iterator();
		while (spi.hasNext()) {
			SetPair sp = (SetPair)spi.next();
			sp.print(ps);
		}
	}
}

class SetPair {
	GameSet elementsNotInIntersection = new GameSet();
	public void print(PrintStream ps) {
		Iterator i = elementsNotInIntersection.iterator();
		while (i.hasNext()) {
			Element e = (Element)i.next();
			ps.print("\t");
			e.print(ps);
		}
	}
}

public class Game extends Thread implements MouseListener, MouseMotionListener, ActionListener, WindowListener {
	static ArrayList stacks = new ArrayList();
	static void init() {
		stacks.clear();
	}
	JFrame jf = new JFrame("Write your own solitare");
	ProgramsVersionSpace psvs;
	Random r = new Random();
	Stack picked = null;
	boolean randomrun = false;

	static public void main(String args[]) {
		Game g = new Game();
		Stack deck = g.deal();
		if (args.length > 0) {
			g.randomrun = true;
		}
		g.psvs = new ProgramsVersionSpace();

		g.jf.setSize(800,600);
		g.jf.getContentPane().setLayout(null);
		g.jf.getContentPane().add(deck.gui);
		g.jf.addMouseListener(g);
/*
		g.jf.addMouseMotionListener(g);
*/
		Stack waste = new Stack(0, 130, 0, StackLayout.Y, g.jf, g);
		g.jf.getContentPane().add(waste.gui);
		for (int st = 0; st < 7; st++) { // working stacks
			Stack nw = new Stack(st*80+150, 130, 20, StackLayout.Y, g.jf, g);
			g.jf.getContentPane().add(nw.gui);
		}
		for (int st = 0; st < 4; st++) { // final stacks
			Stack nw = new Stack(st*80+270, 0, 0, StackLayout.Y, g.jf, g);
			g.jf.getContentPane().add(nw.gui);
		}
		g.moveToStacks(1, 9);
		g.topCardFaceUp(9);

		g.picked = new Stack(0, 400, 20, StackLayout.Y, g.jf, g);
		g.jf.getContentPane().add(g.picked.gui);

		g.jf.getContentPane().invalidate();
		g.jf.getContentPane().validate();
		g.jf.getContentPane().repaint();
		JMenuBar jmb = new JMenuBar();
		g.jf.setJMenuBar(jmb);
		JMenu file = new JMenu("File");
		jmb.add(file);
		JMenuItem dumpLogic = new JMenuItem("Dump Logic");
		file.add(dumpLogic);
		dumpLogic.addActionListener(g);

		g.jf.setVisible(true);
		g.jf.addWindowListener(g);
		g.start();
	}
        public void windowClosing(WindowEvent we) {
		Logic.print();
                System.exit(0);
        }
        public void windowActivated(WindowEvent we) {
        }
        public void windowClosed(WindowEvent we) {
        }
        public void windowDeactivated(WindowEvent we) {
        }
        public void windowDeiconified(WindowEvent we) {
        }
        public void windowIconified(WindowEvent we) {
        }
        public void windowOpened(WindowEvent we) {
        }
	public void actionPerformed(ActionEvent ae) {
		Logic.print();
	}
	void topCardFaceUp(int deck) {
		if (deck > 2) {
			getStack(deck).getTopCard().setFaceUp(true);
			topCardFaceUp(deck-1);
		}
	}
	void moveToStacks(int fromDeck, int toDeck) {
		if (toDeck > 2) {
			moveCards(toDeck-2, fromDeck, toDeck);
			moveToStacks(fromDeck, toDeck-1);
		}
	}
	void moveCards(int numCards, int fromDeck, int toDeck) {
		if (numCards > 0) {
			CardItem cim = getStack(fromDeck).elementAt(0);
			moveCard(fromDeck, toDeck, cim, 0, 10);
			moveCards(numCards-1, fromDeck, toDeck);
		}
	}
	void moveCard(int fromStack, int toStack, CardItem cim, int pos, int pc) {
		Logic.construct(pc, this, fromStack, toStack, cim, pos);
		getStack(fromStack).remove(cim);
		getStack(toStack).insertElementAt(cim, pos);
	}
	static public Stack getStack(int i) {
		Iterator smi = stacks.iterator();
		while (smi.hasNext()) {
			Stack sm = (Stack)smi.next();
			if (sm != null && sm.stack_no == i) {
				// System.err.println("Found stack");
				return sm;
			}
		}
		System.err.println("Didn't find stack, oops "+i);
		return null;
	}
	public Stack deal() {
		System.err.println("initializing");
		Stack deck = new Stack(0, 0, 0, StackLayout.X, jf, this);
		Stack shuffledDeck = new Stack(0, 0, 0, StackLayout.X, jf, this);
		try {
		    int pos = 0;
		    // create deck
		    for (int v = CardItem.Ace; v <= CardItem.King; v++) {
			for (int s = CardItem.Hearts; s <= CardItem.Clubs; s++) {
			    CardItem d = Stack.cards[v-1][s];
			    d.setGame(this);
			    d.setFaceUp(false);
			    deck.insertElementAt(d, pos);
			    pos++;
			}
		    }
		System.err.println("Done initializing, shuffling");
		    // shuffle the cards
		while (deck.size() > 0) {
			int crd = r.nextInt(deck.size());
			CardItem d = (CardItem)deck.elementAt(crd);
			moveCard(deck.stack_no, shuffledDeck.stack_no, d, 0, 11);
		}
		System.err.println("done shuffling");
		} catch (Exception e) {
		    e.printStackTrace();
		}
		return shuffledDeck;
	}
	MouseEvent olde;
	public void 	mouseClicked(MouseEvent me) {
		try {
			if (olde == me) {
				return;
			}
		} finally {
			olde = me;
		}
		Component c = me.getComponent();
		if ((me.getModifiers() & InputEvent.BUTTON3_MASK) != 0) {
			// flip card
			if (c instanceof CardItem) {
				CardItem ci = (CardItem)c;
				ci.setFaceUp(!ci.getFaceUp());
			}
		} else if ((me.getModifiers() & InputEvent.BUTTON2_MASK) != 0) {
			if (c instanceof CardItem) {
				// flip entire stack over
				CardItem ci = (CardItem)c;
				Stack flippy = getStack(ci.stack);
				while (picked != flippy && flippy.size() > 0) {
					CardItem s = (CardItem)flippy.elementAt(0);
					moveCard(flippy.stack_no, picked.stack_no, s, 0, 12);
				        s.setFaceUp(!s.getFaceUp());
				}
				while (picked != null && picked != flippy && picked.size() > 0) {
					CardItem s = (CardItem)picked.elementAt(picked.size()-1);
					moveCard(picked.stack_no, flippy.stack_no, s, 0, 13);
				}
				jf.getContentPane().invalidate();
				jf.getContentPane().validate();
				flippy.gui.invalidate();
				flippy.gui.validate();
				flippy.gui.repaint();
				jf.getContentPane().repaint();
			}
	        } else if (c instanceof CardItem) {
			CardItem ci = (CardItem)c;
			if (picked != null && picked.size() > 0) {
				// play the cards
				if (getStack(ci.stack) != null && picked != getStack(ci.stack)) {
					while (picked.size() > 0) {
						CardItem cipick = (CardItem)picked.elementAt(picked.size()-1);
						moveCard(picked.stack_no, ci.stack, cipick, 0, 14);
					}
				}
			} else {
				// pick up the cards
				Stack s = getStack(ci.stack);
				int i = ci.position;
/*
				while (i < s.size()) {
					CardItem cipick = (CardItem)s.elementAt(i);
					moveCard(s.stack_no, picked.stack_no, cipick, 0, 15);
				}
*/
				while (i >= 0) {
					CardItem cipick = (CardItem)s.elementAt(i);
					moveCard(s.stack_no, picked.stack_no, cipick, 0, 16);
					i--;
				}
			}
	        } else if (c instanceof StackBottom) {
			Stack bottom = (Stack)((StackBottom)c).stack;
			if (picked != null && bottom != picked && picked.size() > 0) {
				// play the cards
				while (picked.size() > 0) {
					CardItem cipick = (CardItem)picked.elementAt(picked.size()-1);
					moveCard(picked.stack_no, bottom.stack_no, cipick, 0, 17);
				}
			}
		} else {
			// create a whole new stack with the cards
			Stack nw = new Stack(me.getX(), me.getY(), 20, StackLayout.Y, jf, this);
			while (picked != null && nw != picked && picked.size() > 0) {
				CardItem cipick = (CardItem)picked.elementAt(picked.size()-1);
				moveCard(picked.stack_no, nw.stack_no, cipick, 0, 18);
			}
			jf.getContentPane().add(nw.gui);
		}
		jf.getContentPane().invalidate();
		jf.getContentPane().validate();
		jf.getContentPane().repaint();
	}
	public void 	mouseEntered(MouseEvent e) {
	}
	public void 	mouseExited(MouseEvent e) {
	}
	public void 	mousePressed(MouseEvent e) {
	}
	public void 	mouseReleased(MouseEvent e)  {
	}
	public void 	mouseDragged(MouseEvent e) {
	}
	public void 	mouseMoved(MouseEvent e) {
	}
	/**
	* Phase 2: Play
	*/
	public void methodicalrun() {
		/*

		At anytime, while the game isn't finished you can:
		*/
		// while (	!Stack.allFaceUp() )
		while (	getStack(10).size() < 13 ||
			getStack(11).size() < 13 ||
			getStack(12).size() < 13 ||
			getStack(13).size() < 13 ) {
			for (int fromStack = 1; fromStack <= 13; fromStack++) {
			for (int toStack = 1; toStack <= 13; toStack++) {
				CardItem cim = getStack(fromStack).getTopCard();
				CardItem rcim = getStack(fromStack).getRandomCard(); // bottom card
				/*
				Move any face up top card to working stacks, if
				the card below the moved card is the face up
				top card and is opposite color and one greater
				than the card being moved.  The card being moved
				should not be an ace
				*/
				if (checkMoveToWorking(fromStack, toStack, cim)) {
					moveNeighborCards(cim.position, fromStack, toStack, true, 1);
				} else
				/*
				Move a neighboring set of cards on a working
				stack including the top card, not including any
				face down card to another working stack if
				target stack has a face up top card one greater
				than the bottom moved card, and the suits are
				of opposite color.
				*/
				if (checkMoveGroup(fromStack, toStack, rcim)) {
					moveNeighborCards(rcim.position, fromStack, toStack, true, 2);
				} else
				/*
				Move a face up ace on top of a stack to final
				stack if the final stack is empty.
				*/
				if (checkMoveAce(fromStack, toStack, cim)) {
					moveNeighborCards(cim.position, fromStack, toStack, true, 3);
				} else
				/*
				Move any face up card on top of a stack to
				final stack if final stack has a top card one
				less than the moved card, and the suits are the
				same.  was HIGH PRIORITY
				*/
				if (checkMoveToFinal(fromStack, toStack, cim)) {
					moveNeighborCards(cim.position, fromStack, toStack, true, 4);
				} else
				/*
				Move a king from the waste or final to empty
				working stack if the king is the top card of
				waste or final.
				*/
				if (checkMoveKingToWorking(fromStack, toStack, cim)) {
					moveNeighborCards(cim.position, fromStack, toStack, true, 5);
				} else 
				/*
				Move a neighboring set of cards on a working
				stack including the top card, with the bottom
				card a face up king, not including any face
				down card to another working stack if target
				stack is empty.
				*/
				if (checkMoveKingGroup(fromStack, toStack, rcim)) {
					moveNeighborCards(rcim.position, fromStack, toStack, true, 6);
				} else
				/*
				Flip over the top card on a working stack, if
				it is face down.
				*/
				if (checkFaceDown(fromStack, toStack, cim)) {
					moveNeighborCards(cim.position, fromStack, toStack, true, 7);
				} else
				/*
				Move the top card from talon to waste and turn
				face up (Alternatively, move top 3 cards from
				talon to the waste, or fewer if there aren't 3
				cards in the talon)
				*/
				if (checkMoveFromTalon(fromStack, toStack, cim)) {
					moveNeighborCards(cim.position, fromStack, toStack, true, 8);
				} else 
				/*
				When the talon is exhausted turn waste over to
				form a new talon
				*/
				if (checkTalonExhausted(fromStack, toStack, cim)) {
					flipOver(fromStack, toStack, false, 9);
				} else {
					badMove(fromStack, toStack, cim);
				}
/*
			if (f < 0.025) { // ZA
			} else if (f < 0.35) { // ZB
			} else if (f < 0.45) { // ZC
			} else if (f < 0.60) { // ZD
			} else if (f < 0.65) { // ZE
			} else if (f < 0.90) { // ZF
			} else if (f < 0.95) { // ZG
			} else if (f < 0.99) { // ZH
			} else if (f < 1.0) { // ZI
			}
*/
			jf.getContentPane().invalidate();
			jf.getContentPane().validate();
			jf.getContentPane().repaint();
			}
			}
		}
		Logic.print();
	}
	public void run() {
		if (randomrun) {
			randomrun();
		} else {
			methodicalrun();
		}
	}
	public void randomrun() {
		/*

		At anytime, while the game isn't finished you can:
		*/
		// while (	!Stack.allFaceUp() )
		while (	getStack(10).size() < 13 ||
			getStack(11).size() < 13 ||
			getStack(12).size() < 13 ||
			getStack(13).size() < 13 )
			{
			float f = r.nextFloat();
			if (f < 0.025) { // ZA
				/*
				Move the top card from talon to waste and turn
				face up (Alternatively, move top 3 cards from
				talon to the waste, or fewer if there aren't 3
				cards in the talon)
				*/
				int fromStack = 1;
				int toStack = 2;
				CardItem cim = getStack(fromStack).getTopCard();
				if (checkMoveFromTalon(fromStack, toStack, cim)) {
					moveNeighborCards(cim.position, fromStack, toStack, true, 1);
				}
			} else if (f < 0.35) { // ZB
				/*
				Move any face up top card to working stacks, if
				the card below the moved card is the face up
				top card and is opposite color and one greater
				than the card being moved.  The card being moved
				should not be an ace
				*/
				int fromStack = -1;
				if (f < 0.17) {
					fromStack = 2; // waste only
				} else if (f < 0.20) {
					fromStack = r.nextInt(4)+10; // a final stack
				} else {
					// if not final stack
					fromStack = r.nextInt(9)+1; // do not include initial
				}
				CardItem cim = getStack(fromStack).getTopCard();
				for (int toStack = 3; toStack <= 9; toStack++) {
					if (checkMoveToWorking(fromStack, toStack, cim)) {
						moveNeighborCards(cim.position, fromStack, toStack, true, 2);
						break;
					}
				}
			} else if (f < 0.45) { // ZC
				/*
				Move a face up ace on top of a stack to final
				stack if the final stack is empty.
				*/
				int fromStack = r.nextInt(Stack.totstack-1)+1; // do not include initial
				CardItem cim = getStack(fromStack).getTopCard();
				for (int toStack = 10; toStack <= 13; toStack++) {
					if (checkMoveAce(fromStack, toStack, cim)) {
						moveNeighborCards(cim.position, fromStack, toStack, true, 3);
						break;
					}
				}
			} else if (f < 0.60) { // ZD
				/*
				Move any face up card on top of a stack to
				final stack if final stack has a top card one
				less than the moved card, and the suits are the
				same.  was HIGH PRIORITY
				*/
				int fromStack = r.nextInt(Stack.totstack-1)+1; // do not include initial
				CardItem cim = getStack(fromStack).getTopCard();
				for (int toStack = 10; toStack <= 13; toStack++) {
					if (checkMoveToFinal(fromStack, toStack, cim)) {
						moveNeighborCards(cim.position, fromStack, toStack, true, 4);
						break;
					}
				}
			} else if (f < 0.65) { // ZE
				/*
				Move a king from the waste or final to empty
				working stack if the king is the top card of
				waste or final.
				*/
				int fromStack = r.nextInt(5);
				if (fromStack == 0) {
					fromStack = 2; // waste stack
				} else {
					fromStack += 9;  // final stacks
				}
				CardItem cim = getStack(fromStack).getTopCard();
				for (int toStack = 3; toStack <= 9; toStack++) {
					if (checkMoveKingToWorking(fromStack, toStack, cim)) {
						moveNeighborCards(cim.position, fromStack, toStack, true, 5);
						break;
					}
				}
			} else if (f < 0.90) { // ZF

				/*
				Move a neighboring set of cards on a working
				stack including the top card, not including any
				face down card to another working stack if
				target stack has a face up top card one greater
				than the bottom moved card, and the suits are
				of opposite color.
				*/
				int fromStack = r.nextInt(7)+3; // only the working stacks
				CardItem cim = getStack(fromStack).getRandomCard(); // bottom card
				for (int toStack = 3; toStack <= 9; toStack++) {
					if (checkMoveGroup(fromStack, toStack, cim)) {
						moveNeighborCards(cim.position, fromStack, toStack, true, 6);
						break;
					}
				}
			} else if (f < 0.95) { // ZG
				/*
				Move a neighboring set of cards on a working
				stack including the top card, with the bottom
				card a face up king, not including any face
				down card to another working stack if target
				stack is empty.
				*/
				int fromStack = r.nextInt(7)+3; // only the working stacks
				CardItem cim = getStack(fromStack).getRandomCard(); // bottom card
				for (int toStack = 3; toStack <= 9; toStack++) {
					if (checkMoveKingGroup(fromStack, toStack, cim)) {
						moveNeighborCards(cim.position, fromStack, toStack, true, 7);
						break;
					}
				}
			} else if (f < 0.99) { // ZH
				/*
				Flip over the top card on a working stack, if it is face down.
				*/
				for (int fromStack = 3; fromStack <= 9; fromStack++) {
					int toStack = fromStack;  // move from a stack  to the same stack
					CardItem cim = getStack(fromStack).getTopCard();
					if (checkFaceDown(fromStack, toStack, cim)) {
						moveNeighborCards(cim.position, fromStack, toStack, true, 8);
					}
				}
			} else if (f < 1.0) { // ZI

				/*
				When the talon is exhausted turn waste over to form a new talon
				*/
				int fromStack = 2;
				int toStack = 1;
				if (checkTalonExhausted(fromStack, toStack, null)) {
					flipOver(fromStack, toStack, false, 9);
				}
			}
			jf.getContentPane().invalidate();
			jf.getContentPane().validate();
			jf.getContentPane().repaint();
		}
		Logic.print();
	}
				/*
				When the talon is exhausted turn waste over to
				form a new talon
				*/
	public boolean checkTalonExhausted(int fromStack, int toStack, CardItem cim) {
		if (fromStack == 2 && toStack == 1 && getStack(toStack).isEmpty() && getStack(fromStack).size() > 1) {
			return true;
		}
		return false;
	}
				/*
				Move the top card from talon to waste and turn
				face up (Alternatively, move top 3 cards from
				talon to the waste, or fewer if there aren't 3
				cards in the talon)
				*/
	public boolean checkMoveFromTalon(int fromStack, int toStack, CardItem cim) {
		if (cim != null && cim.stack == fromStack) {
			if (cim.position == 0 &&
			    fromStack == 1 &&
			    toStack == 2) { // ZA
				return true;
			}
		}
		return false;
	}
				/*
				Move any face up top card to working stacks, if
				the card below the moved card is the face up
				top card and is opposite color and one greater
				than the card being moved.  The card being moved
				should not be an ace
				*/
	public boolean checkMoveToWorking(int fromStack, int toStack, CardItem cim) {
		CardItem below = getStack(toStack).getTopCard();
		if (cim != null && cim.stack == fromStack) {
			if (toStack >= 3 &&
			    toStack <= 9 &&
			    fromStack >= 1 &&
			    fromStack <= 13 &&
			    toStack != fromStack &&
			    cim.position == 0 &&
			    cim.faceUp &&
			    cim.rank != 1 &&
			    below != null &&
			    below.faceUp &&
			    (below.suit + cim.suit) % 2 == 1 &&
			    below.rank - 1 == cim.rank) { // ZB
				return true;
			}
		}
		return false;
	}
				/*
				Move a face up ace on top of a stack to final
				stack if the final stack is empty.
				*/
	public boolean checkMoveAce(int fromStack, int toStack, CardItem cim) {
		if (cim != null && cim.stack == fromStack) {
			if (toStack >= 10 &&
			    toStack <= 13 &&
			    fromStack >= 1 &&
			    fromStack <= 13 &&
			    toStack != fromStack &&
			    cim.position == 0 &&
			    cim.rank == 1 &&
			    cim.faceUp &&
			    (!getStack(fromStack).isBottomCard(cim) ||
				 fromStack < 10) &&
			    getStack(toStack).isEmpty()) { // ZC
				return true;
			}
		}
		return false;
	}
				/*
				Move any face up card on top of a stack to
				final stack if final stack has a top card one
				less than the moved card, and the suits are the
				same.  was HIGH PRIORITY
				*/
	public boolean checkMoveToFinal(int fromStack, int toStack, CardItem cim) {
		CardItem below = getStack(toStack).getTopCard();
		if (cim != null && cim.stack == fromStack) {
			if (toStack >= 10 &&
			    toStack <= 13 &&
			    fromStack >= 1 &&
			    fromStack <= 13 &&
			    toStack != fromStack &&
			    cim.position == 0 &&
			    cim.faceUp &&
			    below != null &&
			    below.faceUp &&
			    below.suit == cim.suit &&
			    below.rank + 1 == cim.rank) { // ZD
				return true;
			}
		}
		return false;
	}
				/*
				Move a king from the waste or final to empty
				working stack if the king is the top card of
				waste or final.
				*/
	public boolean checkMoveKingToWorking(int fromStack, int toStack, CardItem cim) {
		if (cim != null && cim.stack == fromStack) {
			if (toStack >= 3 &&
			    toStack <= 9 &&
			    (fromStack == 2 || (
				fromStack >= 10 &&
				fromStack <= 13)) &&
			    cim.position == 0 &&
			    cim.faceUp &&
			    cim.rank == 13 &&
			    getStack(toStack).isEmpty())  { // ZE
				return true;
			}
		}
		return false;
	}
				/*
				Move a neighboring set of cards on a working
				stack including the top card, not including any
				face down card to another working stack if
				target stack has a face up top card one greater
				than the bottom moved card, and the suits are
				of opposite color.
				*/
	public boolean checkMoveGroup(int fromStack, int toStack, CardItem cim) {
		CardItem below = getStack(toStack).getTopCard();
		if (cim != null && cim.stack == fromStack) {
			if (toStack >= 3 &&
			    toStack <= 9 &&
			    fromStack >= 3 &&
			    fromStack <= 9 &&
			    toStack != fromStack &&
			    cim.faceUp &&
			    below != null &&
                            (below.suit + cim.suit) % 2 == 1 &&
                            below.rank - 1 == cim.rank && 
                            below.faceUp) { // ZF
				return true;
			}
		}
		return false;
	}
				/*
				Move a neighboring set of cards on a working
				stack including the top card, with the bottom
				card a face up king, not including any face
				down card to another working stack if target
				stack is empty.
				*/
	public boolean checkMoveKingGroup(int fromStack, int toStack, CardItem cim) {
		if (cim != null && cim.stack == fromStack) {
			if (toStack >= 3 &&
			    toStack <= 9 &&
			    fromStack >= 3 &&
			    fromStack <= 9 &&
			    toStack != fromStack &&
			    cim.rank == 13 &&
			    cim.faceUp &&
			    !getStack(fromStack).isBottomCard(cim) &&
			    getStack(toStack).isEmpty()) { // ZG
				return true;
			}
		}
		return false;
	}
				/*
				Flip over the top card on a working stack, if
				it is face down.
				*/
	public boolean checkFaceDown(int fromStack, int toStack, CardItem cim) {
		if (fromStack >= 3 &&
		    fromStack <= 9 &&
		    fromStack == toStack &&
		    cim != null &&
		    cim.position == 0 &&
		    !cim.faceUp) { // ZH
			return true;
		}
		return false;
	}
	void badMove(int fromStack, int toStack, CardItem cim) {
		if (cim != null) {
			Logic.construct(0, this, fromStack, toStack, cim, cim.position);
		} else {
			Logic.construct(0, this, fromStack, toStack, cim, -1);
		}
	}
	void moveNeighborCards(int pos, int fromStack, int toStack, boolean faceUp, int pc) {
		if (pos >= 0) {
			CardItem cim = getStack(fromStack).elementAt(pos);
			moveCard(fromStack, toStack, cim, 0, pc);
			cim.setFaceUp(faceUp);
			moveNeighborCards(pos-1, fromStack, toStack, faceUp, pc);
		}
	}
	void flipOver(int fromStack, int toStack, boolean faceUp, int pc) {
		if (!getStack(fromStack).isEmpty()) {
			CardItem cim = getStack(fromStack).elementAt(0);
			moveCard(fromStack, toStack, cim, 0, pc);
			cim.setFaceUp(faceUp);
			flipOver(fromStack, toStack, faceUp, pc);
		}
	}
}
