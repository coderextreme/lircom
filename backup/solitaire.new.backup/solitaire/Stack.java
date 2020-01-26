import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;

class StackBottom extends JLabel implements MouseListener {
	Stack stack;
	Game game;
	public StackBottom(Stack stack, Game game) {
		super();
		this.stack = stack;
		this.game = game;
		String str = CardItem.cardset+"back102.gif";
		ImageIcon ii // = new ImageIcon(str);
                                = new ImageIcon(this.getClass().getClassLoader().getResource(str));
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
	JFrame jw = null;
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
	}
	public Stack(int stack_no, int x, int y) {
		this.x = x;
		this.y = y;
		this.stack_no = stack_no;
		Game.stacks.add(this);
	}
	public Stack(int x, int y, int off, char direction, JFrame jf, Game game) {
		gui = new JPanel();
		this.x = x;
		this.y = y;
		stack_no = totstack;
		totstack++;
		this.game = game;
		Game.stacks.add(this);
		GameMiner.processLine("NEWSTACK|"+stack_no+"|"+x+"|"+y, null, null);
		Log.write(Stack.cards);
		gui.setLayout(new StackLayout(off, direction));
		gui.setLocation(x, y);
		gui.setLayout(new StackLayout(off, direction));
		JLabel stackBottom = new StackBottom(this, game);
		gui.add(stackBottom);
		gui.setSize(gui.getLayout().preferredLayoutSize(gui));
		gui.setVisible(true);
		jw = new JFrame("Stack Layout");
		jw.getContentPane().setLayout(new FlowLayout());

		JLabel sn = new JLabel("Stack Number "+stack_no);
		jw.getContentPane().add(sn);
		
		JLabel directjl = new JLabel("Direction");
		jw.getContentPane().add(directjl);
		directcb = new JCheckBox(""+direction);
		directcb.addChangeListener(this);
		jw.getContentPane().add(directcb);

		JLabel offsetjl = new JLabel("Offset");
		jw.getContentPane().add(offsetjl);
		offsetjtf = new JTextField(""+off);
		jw.getContentPane().add(offsetjtf);
		offsetjtf.addActionListener(this);

		jw.pack();
		jw.setLocation(0,600);
		jw.setVisible(true);
		this.jf = jf;
	}
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == offsetjtf) {
			gui.setLayout(new StackLayout(Integer.parseInt(offsetjtf.getText()), directcb.getText().charAt(0)));
			gui.setSize(gui.getLayout().preferredLayoutSize(gui));
			jf.invalidate();
			jf.validate();
			gui.invalidate();
			gui.validate();
			gui.repaint();
			jf.repaint();
			jw.setVisible(false);
		}
	}
	public void stateChanged(ChangeEvent e) {
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
				if(cim.stack == stack_no) {
					if (cim.position > i) {
						cim.position--;
/*
						if (cim.position == 0 && cim.stack == 2) {
							System.err.println("NEW TOP!!!!!!!!!!!! stack "+cim.stack+" suit "+CardItem.convertSuit(cim.suit)+" rank "+CardItem.convertRank(cim.rank)+" position "+cim.position+" faceUp "+cim.faceUp);
						}
*/
					} else if (cim.position == i) {
						icim = cim;
					}
				}
			}
		}
		if (gui != null) {
			gui.remove(icim);
			gui.setSize(gui.getLayout().preferredLayoutSize(gui));
		}
		GameMiner.processLine("PICK|"+stack_no+"|"+i+"|"+icim.toString(), null, icim);
/*
		if (icim.stack == 2) {
			System.err.println("REMOVING stack "+icim.stack+" suit "+CardItem.convertSuit(icim.suit)+" rank "+CardItem.convertRank(icim.rank)+" position "+icim.position+" faceUp "+icim.faceUp);
		}
*/
		icim.stack = -1;
		icim.position = -1;
		return icim;
	}
	public void insertElementAt(CardItem icim, int pos) {
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				CardItem cim = cards[irank-1][isuit];
				if(cim.stack == stack_no) {
					if (cim.position >= pos) {
						cim.position++;
					}
				}
			}
		}
		icim.stack = stack_no;
		icim.position = pos;
/*
		if (icim.stack == 2) {
			System.err.println("ADDING stack "+icim.stack+" suit "+CardItem.convertSuit(icim.suit)+" rank "+CardItem.convertRank(icim.rank)+" position "+icim.position+" faceUp "+icim.faceUp);
		}
		if (icim.position == 0 && icim.stack == 2) {
			System.err.println("NEW TOP************ stack "+icim.stack+" suit "+CardItem.convertSuit(icim.suit)+" rank "+CardItem.convertRank(icim.rank)+" position "+icim.position+" faceUp "+icim.faceUp);
		}
*/
		if (gui != null) {
			gui.add(icim, pos);
			gui.setSize(gui.getLayout().preferredLayoutSize(gui));
		}
		GameMiner.processLine("PLAY|"+stack_no+"|"+pos+"|"+icim.toString(), null, icim);
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
				if(cim.stack == stack_no && cim.position == i) {
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
		return cim.position;
	}
	public int size() {
		int size = 0;
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				CardItem cim = cards[irank-1][isuit];
				if(cim.stack == stack_no) {
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
				if(cim.stack == stack_no) {
					return false;
				}
			}
		}
		return true;
	}
	public CardItem findCard(int rank, int suit) {
		CardItem cim = cards[rank-1][suit];
		if (cim.rank == rank && cim.suit == suit) {
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
				if(cim.stack == stack_no) {
					if (cim.position > maxpos) {
						maxpos = cim.position;
					}
				}
			}
		}
		return icim.position == maxpos;
	}
	public CardItem getRandomCard() {
		ArrayList carditems = new ArrayList();
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				CardItem cim = cards[irank-1][isuit];
				if(cim.stack == stack_no && cim.faceUp) {
					if (cim.position < 0 || cim.position > 51) {
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
				if(cim.stack == stack_no) {
					System.err.println("stack "+cim.stack+" suit "+CardItem.convertSuit(cim.suit)+" rank "+CardItem.convertRank(cim.rank)+" position "+cim.position+" faceUp "+cim.faceUp);
				}
			}
		}
	}
	static public boolean allFaceUp () {
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				CardItem cim = cards[irank-1][isuit];
				if (!cim.faceUp) {
					return false;
				}
			}
		}
		return true;
	}
}
