package solitaire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.beans.*;

class StackBottom extends JLabel implements MouseListener {
	Stack stack;
	Game game;
	public StackBottom(Stack stack, Game game) {
		super();
		this.stack = stack;
		this.game = game;
		ImageIcon ii = new ImageIcon(getClass().getResource(CardItem.cardset+"back102.gif"));
		setIcon(ii);
		setSize(ii.getIconWidth(), ii.getIconHeight());
		addMouseListener(this);
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
}

public class Stack implements ActionListener, ChangeListener {
	static CardItem[][] cards;
	int x;
	int y;
	JPanel gui = null;
	int stack_no;
	static int totstack = 0;
	JTextField offsetjtf = null;
	JCheckBox directcb = null;
	static JFrame jw = null;
	static JViewport jvp = null;
	static JPanel jp = null;
	JFrame jf = null;
	Game game = null;
	Random r = new Random();

	static {
		init();
	}
	static public void init() {
		cards = new CardItem[CardItem.King][CardItem.Clubs+1];
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				Stack.cards[irank-1][isuit] = new CardItem(
					isuit,
					irank,
					false,
					-1,
					13*(int)isuit+(int)(irank-1));
			}
		}
		Log.write(Stack.cards);
	}
	public Stack(int stack_no, int x, int y, int off, char direction, JFrame jf, Game game) {
		//System.err.println("Got here A");
		gui = new JPanel();
		this.stack_no = stack_no;
		this.x = x;
		this.y = y;
		this.game = game;
		Game.stacks.add(this);
		totstack++;
		//System.err.println("Got here B");
		Log.write("NEWSTACK|"+stack_no+"|"+x+"|"+y+"|"+off+"|"+direction);
		//System.err.println("Got here C");
		gui.setLayout(new StackLayout(off, direction));
		gui.setLocation(x, y);
		gui.setLayout(new StackLayout(off, direction));
		JLabel stackBottom = new StackBottom(this, game);
		gui.add(stackBottom);
		gui.setSize(gui.getLayout().preferredLayoutSize(gui));
		gui.setVisible(true);
		//System.err.println("Got here D");
		if (jw == null) {
			jw = new JFrame("Stack Layout");
			jvp = new JViewport();
			jw.getContentPane().add(jvp);
			jp = new JPanel();
			jvp.add(jp);
			jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		}
		FlowLayout fl = new FlowLayout();
		JPanel st = new JPanel();
		st.setLayout(fl);

		JLabel sn = new JLabel("Stack Number "+stack_no);
		st.add(sn);
		
		JLabel directjl = new JLabel("Direction");
		st.add(directjl);
		directcb = new JCheckBox(""+direction);
		directcb.addChangeListener(this);
		st.add(directcb);
		//System.err.println("Got here E");

		JLabel offsetjl = new JLabel("Offset");
		st.add(offsetjl);
		offsetjtf = new JTextField(""+off);
		st.add(offsetjtf);
		offsetjtf.addActionListener(this);
		jp.add(st);

		jw.pack();
		jw.setLocation(0,600);
		jw.setVisible(true);
		this.jf = jf;
		//System.err.println("Got here F");
		jf.getContentPane().add(gui);
		Log.write(Stack.cards);
	}
	public Stack(int x, int y, int off, char direction, JFrame jf, Game game) {
		this(totstack, x, y, off, direction, jf, game);
	}
	public void actionPerformed(ActionEvent ae) {
		System.err.println("Action Performed");
		if (ae.getSource() == offsetjtf) {
			gui.setLayout(new StackLayout(Integer.parseInt(offsetjtf.getText()), directcb.getText().charAt(0)));
			gui.setSize(gui.getLayout().preferredLayoutSize(gui));
			jf.invalidate();
			jf.validate();
			gui.invalidate();
			gui.validate();
			gui.repaint();
			jf.repaint();
			// jw.setVisible(false);
		}
	}
	public void stateChanged(ChangeEvent e) {
		System.err.println("State Changed");
		if (directcb.getText().charAt(0) == StackLayout.X) {
			directcb.setText(""+StackLayout.Y);
		} else {
			directcb.setText(""+StackLayout.X);
		}
		gui.setLayout(new StackLayout(Integer.parseInt(offsetjtf.getText()), directcb.getText().charAt(0)));
		gui.setSize(gui.getLayout().preferredLayoutSize(gui));
		jf.invalidate();
		jf.validate();
		gui.invalidate();
		gui.validate();
		gui.repaint();
		jf.repaint();
	}
	public void remove(CardItem ci) {
		remove(indexOf(ci));
	}
	public Object remove(int i) {
		CardItem icim = null;
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				CardItem cim = cards[irank-1][isuit];
				if(cim.getStack() == stack_no) {
					if (cim.getPosition() > i) {
						cim.setPosition(cim.getPosition()-1);
/*
						if (cim.getPosition() == 0 && cim.getStack() == 2) {
							System.err.println("NEW TOP!!!!!!!!!!!! stack "+cim.getStack()+" suit "+CardItem.convertSuit(cim.getSuit())+" rank "+CardItem.convertRank(cim.getRank())+" position "+cim.getPosition()+" faceUp "+cim.getFaceUp());
						}
*/
					} else if (cim.getPosition() == i) {
						icim = cim;
					}
				}
			}
		}
		if (gui != null) {
			gui.remove(icim);
			gui.setSize(gui.getLayout().preferredLayoutSize(gui));
		}
		Log.write("PICK|"+stack_no+"|"+i+"|"+icim.toString());
/*
		if (icim.getStack() == 2) {
			System.err.println("REMOVING stack "+icim.getStack()+" suit "+CardItem.convertSuit(icim.getSuit())+" rank "+CardItem.convertRank(icim.getRank())+" position "+icim.getPosition()+" faceUp "+icim.getFaceUp());
		}
*/
		icim.setStack(-1);
		icim.setPosition(-1);
		jf.invalidate();
		jf.validate();
		gui.invalidate();
		gui.validate();
		gui.repaint();
		jf.repaint();
		Log.write(Stack.cards);
		return icim;
	}
	public void insertElementAt(CardItem icim, int pos) {
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				CardItem cim = cards[irank-1][isuit];
				if(cim.getStack() == stack_no) {
					if (cim.getPosition() >= pos) {
						cim.setPosition(cim.getPosition()+1);
					}
				}
			}
		}
		icim.setStack(stack_no);
		icim.setPosition(pos);
/*
		if (icim.getStack() == 2) {
			System.err.println("ADDING stack "+icim.getStack()+" suit "+CardItem.convertSuit(icim.getSuit())+" rank "+CardItem.convertRank(icim.getRank())+" position "+icim.getPosition()+" faceUp "+icim.getFaceUp());
		}
		if (icim.getPosition() == 0 && icim.getStack() == 2) {
			System.err.println("NEW TOP************ stack "+icim.getStack()+" suit "+CardItem.convertSuit(icim.getSuit())+" rank "+CardItem.convertRank(icim.getRank())+" position "+icim.getPosition()+" faceUp "+icim.getFaceUp());
		}
*/
		if (gui != null) {
			gui.add(icim, pos);
			gui.setSize(gui.getLayout().preferredLayoutSize(gui));
		}
		Log.write("PLAY|"+stack_no+"|"+pos+"|"+icim.toString());
		jf.invalidate();
		jf.validate();
		gui.invalidate();
		gui.validate();
		gui.repaint();
		jf.repaint();
		Log.write(Stack.cards);
	}
	public boolean add(CardItem obj) {
		insertElementAt(obj, 0);
		return true;
	}
	public CardItem elementAt(int i) {
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				CardItem cim = cards[irank-1][isuit];
				if(cim.getStack() == stack_no && cim.getPosition() == i) {
					return cim;
				}
			}
		}
		return null;
	}
	public CardItem getTopCard() {
		return elementAt(0);
	}
	public CardItem getSecondCard() {
		return elementAt(1);
	}
	public int indexOf(CardItem cim) {
		return cim.getPosition();
	}
	public int size() {
		int size = 0;
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				CardItem cim = cards[irank-1][isuit];
				if(cim.getStack() == stack_no) {
					size++;
				}
			}
		}
		return size;
	}
	public boolean isEmpty() {
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				CardItem cim = cards[irank-1][isuit];
				if(cim.getStack() == stack_no) {
					return false;
				}
			}
		}
		return true;
	}
	public CardItem findCard(int rank, int suit) {
		CardItem cim = cards[rank-1][suit];
		if (cim.getRank() == rank && cim.getSuit() == suit) {
			return cim;
		} else {
			return null;
		}
	}
	public boolean isBottomCard(CardItem icim) {
		int maxpos = -1;
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				CardItem cim = cards[irank-1][isuit];
				if(cim.getStack() == stack_no) {
					if (cim.getPosition() > maxpos) {
						maxpos = cim.getPosition();
					}
				}
			}
		}
		return icim.getPosition() == maxpos;
	}
	public CardItem getRandomCard() {
		ArrayList<CardItem> carditems = new ArrayList<CardItem>();
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				CardItem cim = cards[irank-1][isuit];
				if(cim.getStack() == stack_no && cim.getFaceUp()) {
					if (cim.getPosition() < 0 || cim.getPosition() > 51) {
						System.err.println("Error illegal position");
					}
					carditems.add(cim);
				}
			}
		}
		if (carditems.size() == 0) {
			return null;
		} else {
			return (CardItem)carditems.get(r.nextInt(carditems.size()));
		}
	}
	public void printStack() {
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				CardItem cim = cards[irank-1][isuit];
				if(cim.getStack() == stack_no) {
					System.err.println("stack "+cim.getStack()+" suit "+CardItem.convertSuit(cim.getSuit())+" rank "+CardItem.convertRank(cim.getRank())+" position "+cim.getPosition()+" faceUp "+cim.getFaceUp());
				}
			}
		}
	}
	static public boolean allFaceUp () {
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				CardItem cim = cards[irank-1][isuit];
				if (!cim.getFaceUp()) {
					return false;
				}
			}
		}
		return true;
	}
}
