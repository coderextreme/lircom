package solitaire;

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import org.json.*;

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

class SolitaireClient extends lircom.SocketIO {
	public SolitaireClient(String server, int port , String nick) throws Exception {
			// java.net.Socket s = new java.net.Socket(server, port);
			// lircom.Client peer = new lircom.Client();
		super(server, port, "Solitaire"+nick);
	}
	public Hashtable client_messages = new Hashtable();
	public boolean processLine(String line) throws Exception {
		return processObj(new JSONObject(line));
	}
	public boolean processObj(JSONObject obj) throws Exception {
		lircom.Message m = lircom.Message.parse(obj);
		if (!m.nick.equals(getNick()) && !seenMessage(m, client_messages)) {

			System.err.println("Processing "+m.message);
			Log.enabled = false;
			System.err.println("Log.enabled is "+Log.enabled);
			try {
				if (obj.optJSONObject("json") != null) {
					GameMiner.processObj(obj.optJSONObject("json")); // only process JSON
				} else {
					System.err.println("Didn't process "+obj.optString("message"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Log.enabled = true;
			System.err.println("Log enabled is "+Log.enabled);
			return true;
		} else {
			System.err.println("From "+m.nick+" to "+getNick()+" "+obj+" not processed");
			return false;
		}
	}
}

public class Game implements MouseListener, MouseMotionListener, ActionListener, WindowListener {
	boolean dragging = false;
	JLabel dragItem = null;
	int dx = 0;
	int dy = 0;
	int sx = 0;
	int sy = 0;
	static Hashtable<Integer,Stack> stacks = new Hashtable<Integer,Stack>();
	public static Hashtable<Integer, CardItem> cards = new Hashtable<Integer, CardItem>(52);
	static {
		jf = new JFrame("Write your own solitaire");
		cards.clear();
		stacks.clear();
		for (Integer code = 0; code < 52; code++) {
			CardItem cim =  new CardItem();
			cards.put(code, cim);
			cim.setStack(Game.getStack(0));
		}
	}
	static JFrame jf;
	Random r = new Random();
	Stack picked = null;
	boolean randomrun = false;
	boolean methodicalrun = false;
	boolean stop = false;
	boolean stopped = true;
	SolitaireClient cos = null;

	static public void main(String args[]) throws Exception {
		Game g = new Game();
                String mode = "random";
                String host = "localhost";
                int port = 8180;
                if (args.length > 0) {
                        mode = args[0];
                }
                if (args.length > 1) {
                        host = args[1];
                }
                if (args.length > 2) {
                    port = Integer.parseInt(args[2]);
                }
		String nickname = javax.swing.JOptionPane.showInputDialog(g.jf, "Enter player name:");
		g.startGame(mode, host, port, nickname);
	}
	public static CardItem at(int rank, int suit) {
		return cards.get(rank-1+13*suit);
	}
	public void startGame(String mode, String server, int port, String nick) throws Exception {
		boolean dealer;
		GameMiner.game = this;
		Stack.game = this;
		try {
			cos = new SolitaireClient(server, port, nick);
			Log.initialize(cos);
			cos.start();
		} catch (Exception cosex) {
			cosex.printStackTrace();
		}
		if (mode.equals("display")) {
			dealer = false;
			jf.setTitle("You are a player");
		} else if (mode.equals("dealer")) {
			dealer = true;
			jf.setTitle("You are the dealer");
		} else {
			dealer = false;
		}
		deal(dealer);

		jf.setSize(800,600);
		JMenuBar jmb = new JMenuBar();
		jf.setJMenuBar(jmb);
		JMenu file = new JMenu("File");
		jmb.add(file);

		JMenuItem dumpLogic = new JMenuItem("Dump Logic");
		file.add(dumpLogic);
		dumpLogic.addActionListener(this);
		JMenuItem startRandom = new JMenuItem("Start Computer Player");
		file.add(startRandom);
		startRandom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println("Starting Computer Player");
				randomrun = true;
				stop = false;
				try {
					if (stopped == true) {
						PlayerThread pt = new PlayerThread(GameMiner.game);
						pt.start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		JMenuItem stopItem = new JMenuItem("Stop Computer Player");
		file.add(stopItem);
		stopItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println("Stopping Computer Player");
				stop = true;
			}
		});

		jf.addWindowListener(this);
		jf.setVisible(true);
		jf.getContentPane().setLayout(null);
		jf.addMouseListener(this);
		jf.addMouseMotionListener(this);
		jf.getContentPane().invalidate();
		jf.getContentPane().validate();
		jf.getContentPane().repaint();


		if (randomrun || methodicalrun) {
			PlayerThread pt = new PlayerThread(this);
			pt.start();
		}
	}
        public void windowClosing(WindowEvent we) {
		stop = true;
		try {
			while (!stopped) {
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	}
	void topCardFaceUp(int deck) {
		if (deck > 2) {
			getStack(deck).getTopCard().setCardCommand("public", "see", true);
			topCardFaceUp(deck-1);
		}
	}
	void moveCards(int numCards, Stack fromDeck, Stack toDeck) {
		if (numCards > 0) {
			CardItem cim = fromDeck.elementAt(0);
			moveCard(fromDeck, toDeck, cim, 0, 10);
			moveCards(numCards-1, fromDeck, toDeck);
		}
	}
	void moveCard(Stack fromStack, Stack toStack, CardItem cim, int pos, int pc) {
		System.err.println("from "+fromStack+" to "+toStack+" card "+cim+" to pos "+pos);
		fromStack.remove(cim);
		toStack.insertElementAt(cim, pos);
	}
	static public Stack getStack(int i) {
		Stack sm = stacks.get(i);
		if (sm != null && sm.stack_no == i) {
			return sm;
		}
		SwingUtilities.invokeLater(new GUIThread(i, jf, null));
		return sm;
	}
	public Stack deal(boolean dealer) {
		{
		        for (int v = CardItem.Ace; v <= CardItem.King; v++) {
			    for (int s = CardItem.Hearts; s <= CardItem.Clubs; s++) {
			        CardItem d = Game.at(v,s);
			        d.setGame(this);
				if (dealer) {
					d.setCardCommand("self", "draw", true);
				}
			    }
		        }
		}
		return null;
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
		if ((me.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
			if (c instanceof CardItem) {
				mouseReleased(me);
			} else if (c instanceof StackBottom) {
				mouseReleased(me);
			} else {
				System.err.println("Hit background");
				// create a new stack
				Stack.build(me.getX(), me.getY());
			}
		}
		jf.getContentPane().invalidate();
		jf.getContentPane().validate();
		jf.getContentPane().repaint();
	}
	public void 	mouseEntered(MouseEvent me) {
	}
	public void 	mouseExited(MouseEvent me) {
	}
	public void 	mousePressed(MouseEvent me) {
		if ((me.getModifiers() & InputEvent.BUTTON1_MASK) != 0) {
			Component c = me.getComponent();
			sx = me.getX();
			sy = me.getY();
			if (c instanceof JLabel) {
				dragItem = (JLabel)c;
				// System.err.println("drag Item "+dragItem);
			}
		}
	}
	public void 	mouseReleased(MouseEvent me)  {
		Component c = me.getComponent();
		Point p = MouseInfo.getPointerInfo().getLocation();
		SwingUtilities.convertPointFromScreen(p, jf);
		Component c2 = jf.findComponentAt(p);
		if (dragging && dragItem != null) {
			dragging = false;
			if (dragItem instanceof CardItem) {
				if (c2 instanceof CardItem) {
					CardItem ci = (CardItem)c2;
					Stack stack = ci.getStack();
					Stack.move((CardItem)dragItem, ((CardItem)dragItem).getStack(), stack, ci);
				} else if (c2 instanceof StackBottom) {
					Stack stack = (Stack)((StackBottom)c2).stack;
					Stack.move((CardItem)dragItem, ((CardItem)dragItem).getStack(), stack, stack.size());
				}
			} else if (dragItem instanceof StackBottom) {
				Stack stack = (Stack)((StackBottom)dragItem).stack;
				stack.gui.setLocation(nx, ny);
			}
			dragItem = null;
		}
	}
	public void 	mouseDragged(MouseEvent me) {
		if (dragItem != null) {
			dragging = true;
			dx = me.getX() - sx;
			dy = me.getY() - sy;
			jf.getContentPane().paint(dragItem.getParent().getParent().getGraphics());
			paint(dragItem.getParent().getParent().getGraphics());
		}
	}
	public void 	mouseMoved(MouseEvent me) {
	}
	int nx = 0;
	int ny = 0;
	public void paint(Graphics g) {
		// System.err.println("drag Item "+dragItem);
		if (dragItem != null) {
			nx = (int)dragItem.getParent().getLocation().getX() + (int)dragItem.getLocation().getX() + dx;
			ny = (int)dragItem.getParent().getLocation().getY() + (int)dragItem.getLocation().getY() + dy;
			dragItem.getParent().getParent().getGraphics().setColor(Color.BLUE);
			dragItem.getParent().getParent().getGraphics().drawRect(
				nx,
				ny,
				(int)dragItem.getPreferredSize().getWidth(),
				(int)dragItem.getPreferredSize().getHeight());
		}
	}
	/**
	* Phase 2: Play
	*/
	public void methodicalrun() {}
	class PlayerThread extends Thread {
		Game g;
		PlayerThread(Game g) {
			this.g = g;
		}
		public void run() {
			if (g.randomrun) {
				g.stopped = false;
				g.randomrun();
				g.stopped = true;
			} else if (g.methodicalrun) {
				g.stopped = false;
				g.methodicalrun();
				g.stopped = true;
			}
		}
	}
	public void randomrun()  {}
	public boolean checkTalonExhausted(int fromStack, int toStack, CardItem cim) {
		return false;
	}
	public boolean checkMoveFromTalon(int fromStack, int toStack, CardItem cim) {
		return false;
	}
	public boolean checkMoveToWorking(int fromStack, int toStack, CardItem cim) {
		return false;
	}
	public boolean checkMoveAce(int fromStack, int toStack, CardItem cim) {
		return false;
	}
	public boolean checkMoveToFinal(int fromStack, int toStack, CardItem cim) {
		return false;
	}
	public boolean checkMoveKingToWorking(int fromStack, int toStack, CardItem cim) {
		return false;
	}
	public boolean checkMoveGroup(int fromStack, int toStack, CardItem cim) {
		return false;
	}
	public boolean checkMoveKingGroup(int fromStack, int toStack, CardItem cim) {
		return false;
	}
	public boolean checkFaceDown(int fromStack, int toStack, CardItem cim) {
		return false;
	}
	void badMove(int fromStack, int toStack, CardItem cim) {
	}
	void moveNeighborCards(int pos, Stack fromStack, Stack toStack, String command, int pc) {
		if (pos >= 0) {
			CardItem cim = fromStack.elementAt(pos);
			moveCard(fromStack, toStack, cim, 0, pc);
			cim.setCardCommand("self", "use", true);
			moveNeighborCards(pos-1, fromStack, toStack, command, pc);
		}
	}
	void flipOver(Stack fromStack, Stack toStack, String command, int pc) {
		if (!fromStack.isEmpty()) {
			CardItem cim = fromStack.elementAt(0);
			moveCard(fromStack, toStack, cim, 0, pc);
			cim.setCardCommand("self", "used", true);
			flipOver(fromStack, toStack, command, pc);
		}
	}
}
