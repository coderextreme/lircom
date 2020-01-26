import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class CardItemMiner implements Cloneable {

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

	public int rank;
	public int suit;
	public boolean faceUp;
	public int stack;
	public int position;
	public CardItemMiner(String suit, String rank, boolean faceUp, int stack, int position) {	
		this(convertSuit(suit), convertRank(rank), faceUp, stack, position);
	}
	public Object clone() {
		CardItemMiner cim = null;
		try {
			cim = (CardItemMiner)super.clone();
		} catch (CloneNotSupportedException cnse) {
			cnse.printStackTrace();
		}
		return cim;
	}
	static public CardItemMiner construct(String suit, String rank, boolean faceUp, int stack, int position) {	
		int irank = convertRank(rank);
		int isuit = convertSuit(suit);
		CardItemMiner cim = StackMiner.cards[irank-1][isuit];
		cim.suit = isuit;
		Log.write(16, "REVEAL1", StackMiner.cards);
		cim.rank = irank;
		Log.write(17, "REVEAL2", StackMiner.cards);
		cim.faceUp = faceUp;
		Log.write(18, "REVEAL3", StackMiner.cards);
		cim.stack = stack;
		Log.write(19, "REVEAL4", StackMiner.cards);
		cim.position = position;
		Log.write(20, "REVEAL5", StackMiner.cards);
		return cim;
	}
	public CardItemMiner(int suit, int rank, boolean faceUp, int stack, int position) {	
		this.suit = suit;
		Log.write(1, "INIT1", StackMiner.cards);
		this.rank = rank;
		Log.write(2, "INIT2", StackMiner.cards);
		this.faceUp = faceUp;
		Log.write(3, "INIT3", StackMiner.cards);
		this.stack = stack;
		Log.write(4, "INIT4", StackMiner.cards);
		this.position = position;
		Log.write(5, "INIT5", StackMiner.cards);
		// System.err.println("NEW "+suit+" "+rank+" "+faceUp+" "+stack+" "+position);
	}
	public void setFaceUp(boolean faceUp) {
		this.faceUp = faceUp;
		if (faceUp) {
			Log.write(12, "VISIBLE", StackMiner.cards);
		} else {
			Log.write(13, "INVISIBLE", StackMiner.cards);
		}
	}
	public boolean equals(CardItemMiner cim) {
		return rank == cim.rank && suit == cim.suit &&
			faceUp == cim.faceUp && stack == cim.stack &&
			position == cim.position;
	}
	public void setRank(String rank) {
		this.rank = convertRank(rank);
		Log.write(14, "CHANGE RANK", StackMiner.cards);
	}
	public void setSuit(String suit) {
		this.suit = convertSuit(suit);
		Log.write(15, "CHANGE SUIT", StackMiner.cards);
	}
	public int getRank() {
		return rank;
	}
	public int getSuit() {
		return suit;
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
}
