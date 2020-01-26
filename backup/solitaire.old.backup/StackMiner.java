import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;

public class StackMiner extends Vector {
	static CardItemMiner[][] cards;
	String stack_no;
	String x;
	String y;
	int stno;
	static {
		init();
	}
	static public void init() {
		cards = new CardItemMiner[CardItemMiner.King][CardItemMiner.Clubs+1];
		for (int isuit = CardItemMiner.Hearts; isuit <= CardItemMiner.Clubs; isuit++) {
			for (int irank = CardItemMiner.Ace; irank <= CardItemMiner.King; irank++) {
				StackMiner.cards[irank-1][isuit] = new CardItemMiner(
					isuit,
					irank,
					false,
					0,
					13*(int)isuit+(int)(irank-1));
			}
		}
	}
	public StackMiner(String stack_no, String x, String y) {
		this.stack_no = stack_no;
		this.x = x;
		this.y = y;
		this.stno = Integer.parseInt(stack_no);
	}
	public Object remove(int i) {
		CardItemMiner icim = (CardItemMiner)super.remove(i);
		for (int isuit = CardItemMiner.Hearts; isuit <= CardItemMiner.Clubs; isuit++) {
			for (int irank = CardItemMiner.Ace; irank <= CardItemMiner.King; irank++) {
				CardItemMiner cim = cards[irank-1][isuit];
				if(cim.stack == stno) {
					if (cim.position > i) {
						cim.position--;
						Log.write(9, "MOVE--", cards);
					}
				}
			}
		}
		icim.stack = -1;
		Log.write(10, "REMOVE FROM STACK", cards);
		icim.position = -1;
		Log.write(11, "REMOVE FROM POSITION", cards);
		return icim;
	}
	public void insertElementAt(CardItemMiner icim, int pos) {
		super.insertElementAt(icim, pos);
		for (int isuit = CardItemMiner.Hearts; isuit <= CardItemMiner.Clubs; isuit++) {
			for (int irank = CardItemMiner.Ace; irank <= CardItemMiner.King; irank++) {
				CardItemMiner cim = cards[irank-1][isuit];
				if(cim.stack == stno) {
					if (cim.position >= pos) {
						cim.position++;
						Log.write(6, "MOVE++", cards);
					}
				}
			}
		}
		icim.stack = stno;
		Log.write(7, "ADD TO STACK", cards);
		icim.position = pos;
		Log.write(8, "ADD POSITION", cards);
	}
	public CardItemMiner getTopCard() {
		for (int isuit = CardItemMiner.Hearts; isuit <= CardItemMiner.Clubs; isuit++) {
			for (int irank = CardItemMiner.Ace; irank <= CardItemMiner.King; irank++) {
				CardItemMiner cim = cards[irank-1][isuit];
				if(cim.stack == stno && cim.position == 0) {
					return cim;
				}
			}
		}
		return null;
	}
	public CardItemMiner getSecondCard() {
		for (int isuit = CardItemMiner.Hearts; isuit <= CardItemMiner.Clubs; isuit++) {
			for (int irank = CardItemMiner.Ace; irank <= CardItemMiner.King; irank++) {
				CardItemMiner cim = cards[irank-1][isuit];
				if(cim.stack == stno && cim.position == 1) {
					return cim;
				}
			}
		}
		return null;
	}
}
