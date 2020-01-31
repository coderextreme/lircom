package solitaire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.*;
import java.beans.*;
import org.json.*;

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
	Vector<CardItem> cards = new Vector<CardItem>();

	public Stack(int stack_no, int x, int y, int off, String direction, JFrame jf, Game game) {
		//System.err.println("Got here A");
		gui = new JPanel();
		this.stack_no = stack_no;
		this.x = x;
		this.y = y;
		this.game = game;
		Game.stacks.add(this);
		totstack++;
		//System.err.println("Got here B");
		JSONObject obj = new JSONObject();
		obj.put("command", "NEWSTACK");
		obj.put("stack", stack_no);
		obj.put("x", x);
		obj.put("y", y);
		obj.put("offset", off);
		obj.put("direction", direction);
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
		directcb = new JCheckBox(direction);
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
		System.err.println("WRITING "+obj);
		Log.write(obj);
		Log.write(Game.cards);
	}
	public Stack(int x, int y, int off, String direction, JFrame jf, Game game) {
		this(totstack, x, y, off, direction, jf, game);
	}
	public void actionPerformed(ActionEvent ae) {
		System.err.println("Action Performed");
		if (ae.getSource() == offsetjtf) {
			gui.setLayout(new StackLayout(Integer.parseInt(offsetjtf.getText()), directcb.getText()));
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
		if (directcb.getText().equals(StackLayout.X)) {
			directcb.setText(""+StackLayout.Y);
		} else {
			directcb.setText(""+StackLayout.X);
		}
		gui.setLayout(new StackLayout(Integer.parseInt(offsetjtf.getText()), directcb.getText()));
		gui.setSize(gui.getLayout().preferredLayoutSize(gui));
		jf.invalidate();
		jf.validate();
		gui.invalidate();
		gui.validate();
		gui.repaint();
		jf.repaint();
	}
	static public void move(CardItem icim, Stack fromStack, Stack toStack, CardItem toCard) {
		move(icim, fromStack, toStack, toStack.indexOf(toCard));
	}

	// when the to-stack is empty
	static public void move(CardItem icim, Stack fromStack, Stack toStack, int toPosition) {
		JSONObject obj = new JSONObject();
		obj.put("command", "MOVE");
		obj.put("fromStack", fromStack.stack_no);
		obj.put("fromPosition", fromStack.indexOf(icim));
		obj.put("toPosition", toPosition);
		obj.put("toStack", toStack.stack_no);
		icim.toString(obj);
		System.err.println("MOVING "+obj);

		Log.write(obj);
		Log.write(Game.cards);
	}

	public void remove(CardItem icim) {
		cards.remove(icim);
		if (gui != null) {
			System.err.println("removing from GUI");
			gui.remove(icim);
			gui.setSize(gui.getLayout().preferredLayoutSize(gui));
			icim.setStack(null);
			jf.invalidate();
			jf.validate();
			gui.invalidate();
			gui.validate();
			gui.repaint();
			jf.repaint();
		}
	}
	public void insertElementAt(CardItem icim, Integer pos) {
		Stack toStack = Game.getStack(stack_no);
		cards.insertElementAt(icim, pos < toStack.size() ? pos : toStack.size());
		icim.setStack(toStack);
		if (gui != null) {
			System.err.println("Adding to GUI");
			gui.add(icim, pos);
			gui.setSize(gui.getLayout().preferredLayoutSize(gui));
		}

		jf.invalidate();
		jf.validate();
		gui.invalidate();
		gui.validate();
		gui.repaint();
		jf.repaint();
	}
	public boolean add(CardItem obj) {
		insertElementAt(obj, size());
		return true;
	}
	public CardItem elementAt(int i) {
		return cards.elementAt(i);
	}
	public CardItem getTopCard() {
		return elementAt(0);
	}
	public CardItem getSecondCard() {
		return elementAt(1);
	}
	public int indexOf(CardItem cim) {
		return cards.indexOf(cim);
	}
	public int size() {
		return cards.size();
	}
	public boolean isEmpty() {
		return cards.isEmpty();
	}
	public boolean isBottomCard(CardItem icim) {
		return icim.equals(cards.lastElement());
	}
	public CardItem getRandomCard() {
		Vector<CardItem> carditems = Game.cards;
		if (carditems.size() == 0) {
			return null;
		} else {
			return (CardItem)carditems.get(r.nextInt(carditems.size()));
		}
	}
	public void printStack() {
		Iterator<CardItem> i = cards.iterator();
		while (i.hasNext()) {
			CardItem cim = i.next();
			System.err.println("stack "+cim.getStack()+" suit "+CardItem.convertSuit(cim.getSuit())+" rank "+CardItem.convertRank(cim.getRank())+" position "+cim.getPosition()+" faceUp "+cim.getFaceUp());
		}
	}
	static public boolean allFaceUp () {
		Iterator<CardItem> i = Game.cards.iterator();
		while (i.hasNext()) {
			CardItem cim = i.next();
			if (!cim.getFaceUp()) {
				return false;
			}
		}
		return true;
	}
}
