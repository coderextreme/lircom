package solitaire;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.io.*;
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

class PopupListener extends MouseAdapter {
    Stack toStack;
    JPopupMenu popup;
    Component component;
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
	    component = e.getComponent();
            this.popup.show(e.getComponent(),
                       e.getX(), e.getY());
        }
    }
    public PopupListener(Stack toStack, JPopupMenu popup) {
	this.toStack = toStack;
	this.popup = popup;
    }
}


public class Stack implements ActionListener, ChangeListener {
	PopupListener popupListener;
	int x;
	int y;
	JPanel gui = null;
	int stack_no;
	JTextField offsetjtf = null;
	JCheckBox directcb = null;
	static JFrame jw = null;
	JFrame jf = null;
	static JViewport jvp = null;
	static JPanel jp = null;
	public static Game game = null;
	Random r = new Random();
	Vector<CardItem> cards = new Vector<CardItem>();

	public Stack(int stack_no, JFrame jf) {
		this(stack_no, (100+ stack_no * 100)%700, 50*stack_no % 450, jf);
	}

	public Stack(int stack_no, int x, int y, JFrame jf) {
		this(stack_no, x, y, 20, StackLayout.X, jf);
	}

	public static void build(int x, int y) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("principal", "self");
			obj.put("command", "draw");
			obj.put("object", "stack");
			obj.put("flag", true);
			obj.put("x", x);
			obj.put("y", y);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		// will return toStack
		System.err.println("BUILDING A STACK "+obj);
		Log.write(obj);
	}
	class StackActionListener implements ActionListener {
		String principal;
		String command;
		Stack object;
		Boolean flag;
		public StackActionListener(String principal, String command, Stack object, Boolean flag) {
			this.principal = principal;
			this.command = command;
			this.object = object;
			this.flag = flag;
		}
		public void actionPerformed(ActionEvent ae) {
			setStackCommand(principal, command, object, flag);
		}
	}
	class CardActionListener implements ActionListener {
		String principal;
		String command;
		String object;
		Boolean flag;
		public CardActionListener(String principal, String command, String object, Boolean flag) {
			this.principal = principal;
			this.command = command;
			this.object = object;
			this.flag = flag;
		}
		public void actionPerformed(ActionEvent ae) {
			CardItem.callCardCommand(ae, principal, command, flag);
		}
	}
	public void makeMenu(JPopupMenu popup, String [] principals, String [] commands) {
		JMenuItem menuItem;
/*
		menuItem = new JMenuItem("Draw a card");
		menuItem.addActionListener(new CardActionListener("self", "draw", "card", true));
		popup.add(menuItem);
*/
		menuItem = new JMenuItem("Show a card");
		menuItem.addActionListener(new CardActionListener("self", "see", "card", true));
		popup.add(menuItem);
		menuItem = new JMenuItem("Hide a card");
		menuItem.addActionListener(new CardActionListener("self", "see", "card", false));
		popup.add(menuItem);
		menuItem = new JMenuItem("Discard a stack");
		menuItem.addActionListener(new StackActionListener("self", "discard", this, true));
		popup.add(menuItem);

		menuItem = new JMenuItem("Show a stack");
		menuItem.addActionListener(new StackActionListener("self", "see", this, true));
		popup.add(menuItem);
		menuItem = new JMenuItem("Hide a stack");
		menuItem.addActionListener(new StackActionListener("self", "see", this, false));
		popup.add(menuItem);
		menuItem = new JMenuItem("Create a new private stack");
		menuItem.addActionListener(new StackActionListener("self", "draw", this, true));
		popup.add(menuItem);
		menuItem = new JMenuItem("Make a private stack public");
		menuItem.addActionListener(new StackActionListener("public", "use", this, true));
		popup.add(menuItem);
		menuItem = new JMenuItem("Create a new public stack");
		menuItem.addActionListener(new StackActionListener("public", "draw", this, true));
		popup.add(menuItem);
	}
	public Stack(int stack_no, int x, int y, int off, String direction, JFrame jf) {
		this.stack_no = stack_no;
		Game.stacks.put(stack_no, this);
		gui = new JPanel();
		JPopupMenu popup = new JPopupMenu();

		makeMenu(popup, new String[] {"public", "self"}, new String[] {"draw", "see", "use", "discard"});

		this.popupListener = new PopupListener(this, popup);
		//System.err.println("Got here A");
		this.x = x;
		this.y = y;
		this.game = game;
		//System.err.println("Got here B");
		//System.err.println("Got here C");
		setLayout(off, direction);
		gui.setLocation(x, y);
		setLayout(off, direction);
		JLabel stackBottom = new StackBottom(this, game);
		gui.add(stackBottom);
		setSize();
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
		// jf.getContentPane().add(gui);
		//System.err.println("Got here F");
	}
	public void actionPerformed(ActionEvent ae) {
		System.err.println("Action Performed");
		if (ae.getSource() == offsetjtf) {
			setLayout(offsetjtf.getText(), directcb.getText());
			setSize();
			jf.invalidate();
			jf.validate();
			gui.invalidate();
			gui.validate();
			gui.repaint();
			jf.repaint();
			// jw.setVisible(false);
		}
	}
	public void setSize() {
		gui.setSize(gui.getLayout().preferredLayoutSize(gui));
		// gui.setSize(400,100);
	}
	public void setLayout(int offset, String direction) {
		gui.setLayout(new StackLayout(offset, direction));
		// gui.setLayout(null);
	}
	public void setLayout(String offset, String direction) {
		setLayout(Integer.parseInt(offset), direction);
	}
	public void stateChanged(ChangeEvent e) {
		System.err.println("State Changed");
		if (directcb.getText().equals(StackLayout.X)) {
			directcb.setText(""+StackLayout.Y);
		} else {
			directcb.setText(""+StackLayout.X);
		}
		setLayout(offsetjtf.getText(), directcb.getText());
		setSize();
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
		try {
			obj.put("command", "use");
			obj.put("object", "card");
			obj.put("flag", true);
			obj.put("principal", "public");
			obj.put("fromStack", fromStack.stack_no);
			obj.put("fromPosition", fromStack.indexOf(icim));
			obj.put("toPosition", toPosition);
			obj.put("toStack", toStack.stack_no);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		icim.toString(obj);
		System.err.println("MOVING "+obj+" to "+toPosition);

		Log.write(obj);
	}

	public void replaceAll(JSONArray array) {
		ListIterator<CardItem> i = cards.listIterator();
		while (i.hasNext()) {
			CardItem cim = i.next();
			if (cim != null) {
				gui.remove(cim);
				cim.setStack(Game.getStack(0)); // back to draw pile
				i.remove();
			}
		}
		for (int c = 0; c < array.length(); c++) {
			boolean isNull = array.isNull(c);
			Integer code = array.optInt(c);
			System.err.println(c+" "+isNull+" "+code);
			if (code > -1 && !isNull) {
				System.err.println("Entering "+code);
				CardItem cim = Game.cards.get(code);
				if (cim != null) {
					cim.setStack(this);
					add(cim);
				}
			}
		}
		setSize();
		jf.invalidate();
		jf.validate();
		gui.invalidate();
		gui.validate();
		gui.repaint();
		jf.repaint();
	}
	public void remove(CardItem icim) {
		cards.remove(icim);
		if (gui != null) {
			System.err.println("removing from GUI");
			gui.remove(icim);
			icim.setStack(Game.getStack(0));

			setSize();
			jf.invalidate();
			jf.validate();
			gui.invalidate();
			gui.validate();
			gui.repaint();
			jf.repaint();
		}
	}
	public void insertElementAt(CardItem icim, Integer pos) {
		Stack toStack = this;
		Stack fromStack = icim.getStack();
		if (fromStack != null) {
			icim.removeMouseListener(fromStack.popupListener);
		}
		icim.setStack(toStack);
		icim.addMouseListener(toStack.popupListener);
		if (!cards.contains(icim)) {
			cards.add(icim);
			if (gui != null) {
				gui.add(icim, 0);
				setSize();
			}
		}

		jf.invalidate();
		jf.validate();
		gui.invalidate();
		gui.validate();
		gui.repaint();
		jf.repaint();
	}
	public boolean add(CardItem obj) {
		insertElementAt(obj, 0);
		obj.setStack(this);
		return true;
	}
	public CardItem elementAt(int i) {
		if (size() == 0 || i == -1) {
			return null;
		}
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
		Hashtable<Integer,CardItem> carditems = Game.cards;
		if (carditems.size() == 0) {
			return null;
		} else {
			return (CardItem)carditems.get(r.nextInt(carditems.size()));
		}
	}
	static public boolean allFaceUp () {
		Iterator<CardItem> i = Game.cards.values().iterator();
		while (i.hasNext()) {
			CardItem cim = i.next();
			if (!cim.getFaceUp()) {
				return false;
			}
		}
		return true;
	}
	public void setStackCommandBE(String principal, String command, Stack object, Boolean flag) {
	}
	public void setStackCommand(String principal, String command, Stack object, Boolean flag) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("principal", principal);
			obj.put("command", command);
			obj.put("object", "stack");
			obj.put("fromStack", object.stack_no);
			obj.put("flag", flag);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.write(obj);
	}
}
