package solitaire;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.json.*;


public class CardItem extends JLabel implements ListCellRenderer, MouseListener, MouseMotionListener, Cloneable {
	private Integer rank;
	private Integer suit;
	private boolean faceUp = false;
	private Stack stack;
	private ImageIcon ii = null;
	private static Game game;
	private static Hashtable map = new Hashtable();
	static public final String cardset = "cardset-standard/";
	private static ImageIcon background = new ImageIcon(CardItem.class.getResource(cardset+"back101.gif"));


	static public final int Hearts = 0;
	static public final int Spades = 1;
	static public final int Diamonds = 2;
	static public final int Clubs = 3;

	static public final int Ace = 1;
	static public final int Two = 2;
	static public final int Three = 3;
	static public final int Four = 4;
	static public final int Five = 5;
	static public final int Six = 6;
	static public final int Seven = 7;
	static public final int Eight = 8;
	static public final int Nine = 9;
	static public final int Ten = 10;
	static public final int Jack = 11;
	static public final int Queen = 12;
	static public final int King = 13;

	static String [] suff = new String [] {
		"h.gif",
		"s.gif",
		"d.gif",
		"c.gif"
	};

	
	static {
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				String srank;
				if (irank < 10) {
					srank = "0"+irank;
				} else {
					srank = Integer.toString(irank);
				}
				map.put(irank+"|"+isuit, cardset+srank+suff[isuit]);
			}
		}
	}
			
	public CardItem() {	
		this.faceUp = false;
		System.err.println("New Card Item");
		System.err.println("Setting background constructor");
		setIcon(background);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	public CardItem(Integer suit, Integer rank, boolean faceUp, Stack stack, int position) {	
		this();
		this.suit = suit;
		this.rank = rank;
		this.faceUp = faceUp;
		this.stack = stack;
		if (this.stack != null) {
			this.stack.insertElementAt(this, position);
		}
		String icon = (String)map.get(rank+"|"+suit);
		System.err.println("Setting icon to "+icon);
		setGIFname(icon);
	}
	public void setGIFname(String gifname) {
		System.err.println("setIcon is "+gifname);
		ii = new ImageIcon(getClass().getResource(gifname));
		setIcon(ii);
	}
	public Dimension getPreferredSize() {
		return new Dimension(background.getIconWidth(), background.getIconHeight());
	}
	public Dimension getMinimumSize() {
		return new Dimension(background.getIconWidth(), background.getIconHeight());
	}
	public void toString(JSONObject obj) {
		obj.put("rank", rank);
		obj.put("suit", suit);
		obj.put("faceUp", faceUp);
		if (stack == null) {
			obj.put("fromStack", -1);
			obj.put("fromPosition", 0);
		} else {
			obj.put("fromStack", stack.stack_no);
			obj.put("fromPosition", stack.indexOf(this));
		}
	}
	public String toString() {
		return rank+"|"+suit;
	}
	public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		System.err.println("Icon is "+value);
		setGIFname(value.toString());
		return this;
	}
	public boolean getFaceUp() {
		return faceUp;
	}
	public void setFaceUpFE(boolean faceUp) {
		JSONObject obj = new JSONObject();
		if (!this.faceUp && faceUp) {
			System.err.println("VISIBLE");
			obj.put("command", "VISIBLE");
		} else if (this.faceUp && !faceUp) {
			System.err.println("INVISIBLE");
			obj.put("command", "INVISIBLE");
		} else {
			return; // NO ACTION
		}
		this.faceUp = faceUp;
		toString(obj);
		Log.write(obj);
		Log.write(Game.cards);
	}
	public void setFaceUpBE(boolean faceUp) {
		this.faceUp = faceUp;
		System.err.println("Setting face up "+faceUp);
		if (!faceUp) {
			System.err.println("Setting backround");
			setIcon(background);
		} else {
			System.err.println("Loading card "+this);
			String gifname = (String)map.get(this.rank+"|"+this.suit);
			System.err.println("Loading icon "+gifname);
			ii = new ImageIcon(getClass().getResource(gifname));
			setIcon(ii);
			setSize(ii.getIconWidth(), ii.getIconHeight());
		}
	}
	public void setGame(Game g) {
		game = g;
	}
	public void 	mouseDragged(MouseEvent me) {
		game.mouseDragged(me);
	}
	public void 	mouseMoved(MouseEvent me) {
		game.mouseMoved(me);
	}
	public void 	mouseClicked(MouseEvent e) {
		game.mouseClicked(e);
	}
	public void 	mouseEntered(MouseEvent e) {
		game.mouseEntered(e);
	}
	public void 	mouseExited(MouseEvent e) {
		game.mouseExited(e);
	}
	public void 	mousePressed(MouseEvent e) {
		game.mousePressed(e);
	}
	public void 	mouseReleased(MouseEvent e)  {
		game.mouseReleased(e);
	}
	static public int convertRank(String irank) {
		if (irank.equals("Ace")) return Ace;
		if (irank.equals("Two")) return Two;
		if (irank.equals("Three")) return Three;
		if (irank.equals("Four")) return Four;
		if (irank.equals("Five")) return Five;
		if (irank.equals("Six")) return Six;
		if (irank.equals("Seven")) return Seven;
		if (irank.equals("Eight")) return Eight;
		if (irank.equals("Nine")) return Nine;
		if (irank.equals("Ten")) return Ten;
		if (irank.equals("Jack")) return Jack;
		if (irank.equals("Queen")) return Queen;
		if (irank.equals("King")) return King;
		return 0;
	}
	static public String convertRank(int irank) {
		switch (irank) {
		case Ace:
			return "Ace";
		case Two:
			return "Two";
		case Three:
			return "Three";
		case Four:
			return "Four";
		case Five:
			return "Five";
		case Six:
			return "Six";
		case Seven:
			return "Seven";
		case Eight:
			return "Eight";
		case Nine:
			return "Nine";
		case Ten:
			return "Ten";
		case Jack:
			return "Jack";
		case Queen:
			return "Queen";
		case King:
			return "King";
		default:
			return null;
		}
	}
	static public int convertSuit(String isuit) {
		if (isuit.equals("Hearts")) return Hearts;
		if (isuit.equals("Spades")) return Spades;
		if (isuit.equals("Diamonds")) return Diamonds;
		if (isuit.equals("Clubs")) return Clubs;
		return -1;
	}
	static public String convertSuit(int isuit) {
		switch(isuit) {
		case Hearts:
			return "Hearts";
		case Spades:
			return "Spades";
		case Diamonds:
			return "Diamonds";
		case Clubs:
			return "Clubs";
		default:
			return null;
		}
	}
	public void setRank(String rank) {
		this.rank = convertRank(rank);
	}
	public void setRank(Integer rank) {
		this.rank = rank;
	}
	public void setSuit(String suit) {
		this.suit = convertSuit(suit);
	}
	public void setSuit(Integer suit) {
		this.suit = suit;
	}
	public Integer getRank() {
		return rank;
	}
	public Integer getSuit() {
		return suit;
	}
	public Object clone() {
		try {
			CardItem ci = (CardItem)super.clone();
			ci.rank = rank;
			ci.suit = suit;
			ci.faceUp = faceUp;
			ci.stack = stack;
			ci.ii = ii;
			return ci;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public int getPosition() {
		return stack.indexOf(this);
	}
	public void setStack(Stack s) {
		stack = s;
	}
	public Stack getStack() {
		return stack;
	}
	public void setSize(Dimension d) {
		super.setSize(d);
		// System.err.println("w "+d.width+" h "+d.height);
	}
	public void setLocation(int x, int y) {
		super.setLocation(x, y);
		// System.err.println("x "+x+" y "+y);
	}
}