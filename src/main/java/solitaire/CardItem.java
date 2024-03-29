package solitaire;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


public class CardItem extends JLabel implements ListCellRenderer, MouseListener, Cloneable {
	private int rank;
	private int suit;
	private boolean faceUp = false;
	private int stack;
	private int position;
	private ImageIcon ii = null;
	private static Game game;
	private static Hashtable<String,String> map = new Hashtable<String,String>();
	private static ImageIcon background = null;

	static public final String cardset = "/solitaire/cardset-standard/";

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
	}
	public CardItem(int suit, int rank, boolean faceUp, int stack, int position) {	
	        if (background == null) {
			background = new ImageIcon(getClass().getResource(cardset+"back101.gif"));
		}
		this.suit = suit;
		this.rank = rank;
		this.faceUp = faceUp;
		this.stack = stack;
		this.position = position;
		addMouseListener(this);
		setIcon((String)map.get(rank+"|"+suit));
	}
	public void setIcon(String gifname) {
		ii = new ImageIcon(getClass().getResource(gifname));
		setIcon(ii);
		setSize(ii.getIconWidth(), ii.getIconHeight());
		setFaceUp(faceUp);
	}
	public Dimension getPreferredSize() {
		return new Dimension(ii.getIconWidth(), ii.getIconHeight());
	}
	public Dimension getMinimumSize() {
		return new Dimension(ii.getIconWidth(), ii.getIconHeight());
	}
	public String toStringModel() {
		return rank+"|"+suit+"|"+faceUp;
	}
	public String toString() {
		return rank+"|"+suit;
	}
	public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
	{
		setIcon(value.toString());
		return this;
	}
	public boolean getFaceUp() {
		return faceUp;
	}
	public void setFaceUp(boolean faceUp) {
		if (!this.faceUp && faceUp) {
			if (stack == -1) {
				Log.write("VISIBLE|0|0|"+toString());
			} else {
				Log.write("VISIBLE|"+stack+"|"+position+"|"+toString());
			}
		} else if (this.faceUp && !faceUp) {
			if (stack == -1) {
				Log.write("INVISIBLE|0|0|"+toString());
			} else {
				Log.write("INVISIBLE|"+stack+"|"+position+"|"+toString());
			}
		}
		this.faceUp = faceUp;
		if (!faceUp) {
			setIcon(background);
		} else {
			setIcon(ii);
		}
		Log.write(Stack.cards);
	}
	public void setGame(Game g) {
		game = g;
	}
	public void 	mouseClicked(MouseEvent e) {
		game.mouseClicked(e);
	}
	public void 	mouseEntered(MouseEvent e) {
	}
	public void 	mouseExited(MouseEvent e) {
	}
	public void 	mousePressed(MouseEvent e) {
	}
	public void 	mouseReleased(MouseEvent e)  {
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
	public void setRank(int rank) {
		this.rank = rank;
	}
	public void setSuit(String suit) {
		this.suit = convertSuit(suit);
	}
	public void setSuit(int suit) {
		this.suit = suit;
	}
	public int getRank() {
		return rank;
	}
	public int getSuit() {
		return suit;
	}
	public Object clone() {
		try {
			CardItem ci = (CardItem)super.clone();
			ci.rank = rank;
			ci.suit = suit;
			ci.faceUp = faceUp;
			ci.stack = stack;
			ci.position = position;
			ci.ii = ii;
			return ci;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void setPosition(int p) {
		position = p;
	}
	public int getPosition() {
		return position;
	}
	public void setStack(int s) {
		stack = s;
	}
	public int getStack() {
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
