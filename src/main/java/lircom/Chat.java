package lircom;

import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

class FromGUI extends InputStream {
	int ba = 0;
        int pos = 0;
        byte [] buffer;
	public FromGUI() {
	}
	public void write(byte [] b) {
		synchronized (this) {
                        if (buffer == null) {
                                buffer = (byte [])b.clone();
                                ba = buffer.length;
                                pos = 0;
                        } else {
                                byte [] newbuffer = new byte[buffer.length+b.length];
                                int i;
                                for (i = 0; i < buffer.length; i++) {
                                    newbuffer[i] = buffer[i];
                                }
                                for (int j = 0; j < b.length; j++) {
                                    newbuffer[i+j] = b[j];
                                }
                                ba += b.length;
                                buffer = newbuffer;
                        }
		}
	}
	public int available() {
		while (ba == 0) {
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
		synchronized (this) {
			return ba;
		}
	}
	public int read() {
		while (ba == 0) {
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
		synchronized (this) {
			int b = buffer[pos];
			ba--;
                        pos++;
                        if (ba == 0) {
                            buffer = null;
                            pos = 0;
                        }
			return b;
		}
	}
	public int read(byte[] b, int off, int len) {
		while (ba == 0) {
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
		synchronized (this) {
			if (len <= ba) {
                                for (int i = 0; i < len; i++) {
                                    b[i+off] = buffer[pos];
                                    pos++;
                                }
				ba -= len;
				return len;
			} else {
                                for (int i = 0; i < ba; i++) {
                                    b[i+off] = buffer[pos];
                                    pos++;
                                }
				int bytes = ba;
				ba = 0;
                                buffer = null;
                                pos = 0;
				return bytes;
			}
		}
	}
	public int read(byte[] b) {
		return read(b, 0, b.length);
	}
}

class FromServer extends OutputStream {
	Chat c;
	public FromServer() {
	}
	public void setChat(Chat c) {
		this.c = c;
	}
	public void write(int b) {
		// System.err.println("wrote "+b);
                Message m = new Message("Tiny Byte", "Tiny Byte", b+"", "__");
		c.displayToScreen(m);  // TODO UTF-8
	}
	public void write(byte [] buf, int off, int len) {
		try {
			String line = new String(buf, off, len); 
			// System.err.println(c.getNick()+" received "+line);
			c.processLine(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class HTMLJCheckBox extends JCheckBox {
	String item = null;
	public HTMLJCheckBox(String name, String item) {
		super(name);
		this.item = item;
	}
}

class MouseLabel extends JLabel implements MouseListener, ActionListener {
	JFrame jf = null;
	Trade t = null;
	String nick1 = null;
	String nick2 = null;
	String ipaddr = null;
	JButton trade = null;
	JButton offer = null;
	JButton accept = null;
	JButton cancel = null;
	JButton reject = null;
	JPanel items = null;
	JPanel fromitems = null;
	String nnip = "";
	public MouseLabel(String nick1, String nick2, String ipaddr, Trade t) {
		super(nick2);
		this.nick1 = nick1;
		this.ipaddr = ipaddr;
		this.nick2 = nick2;
		String ip1 = "127.0.0.1";
		try {
			ip1 = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
		}
		nnip = "nick1="+nick1+"&nick2="+nick2+"&ip1="+ip1+"&ip2="+ipaddr;
		// System.err.println(nnip);
		
		jf = new JFrame(nick2);
		JLabel nicktitle = new JLabel("Trading With "+nick2);
		// JLabel jlipaddr = new JLabel(ipaddr);
		trade = new JButton("Get Inventory");
		trade.addActionListener(this);

		offer = new JButton("Make Offer");
		offer.addActionListener(this);
		offer.setEnabled(false);

		cancel = new JButton("Cancel Trade");
		cancel.addActionListener(this);
		cancel.setEnabled(false);

		accept = new JButton("Accept Offer");
		accept.addActionListener(this);
		accept.setEnabled(false);

		reject = new JButton("Reject Offer");
		reject.addActionListener(this);
		reject.setEnabled(false);

		items = new JPanel();
		items.setLayout(new BoxLayout(items, BoxLayout.Y_AXIS));


		JPanel buttons = new JPanel();
		buttons.setLayout(new BoxLayout(buttons, BoxLayout.Y_AXIS));
		buttons.add(trade);
		buttons.add(offer);
		buttons.add(cancel);
		buttons.add(accept);
		buttons.add(reject);


		fromitems = new JPanel();
		fromitems.setLayout(new BoxLayout(fromitems, BoxLayout.Y_AXIS));

		jf.getContentPane().setLayout(new BorderLayout());
		jf.getContentPane().add("North", nicktitle);
		jf.getContentPane().add("West", buttons);
		jf.getContentPane().add("Center", items);
		jf.getContentPane().add("East", fromitems);
		this.t = t;
	}
class WaitForOffer extends Thread {
	public void run() {
		StringBuffer sb = new StringBuffer();
		try {
			// System.err.println("Getting the other parties offer");
			if (!t.getOffer(nnip, sb)) {
				throw new Exception("REJECT"); // other party cancelled
			}
		} catch (Exception e) {
			trade.setEnabled(true);
			reject.setEnabled(false);
			return;
		}
			System.err.println("Retrieved "+sb);
			StringTokenizer st = new StringTokenizer(sb.toString(), "\n");
			
			for (int i = 0; i < items.getComponentCount(); i++) {
				Component c = items.getComponent(i);
				if (c instanceof HTMLJCheckBox) {
					HTMLJCheckBox jcb = (HTMLJCheckBox)c;
					jcb.setEnabled(false);
				}
			}
			JLabel jl = null;
			jl = new JLabel("From "+nick2);
			fromitems.add(jl);
			while (st.hasMoreTokens()) {
				String item = st.nextToken();
				System.err.println("Getting item "+item);
				int fp = item.indexOf("|");
				if (fp >= 0) {
					int sp = item.indexOf("|", fp+1);
					int tp = item.indexOf("|", sp+1);
					String nck = item.substring(0, fp);
					String ip = item.substring(fp+1, sp);
					String name = item.substring(sp+1, tp);
					String html = item.substring(tp+1);
					if (nck.equals(nick2) && ip.equals(ipaddr)) {
						jl = new JLabel(name);
						fromitems.add(jl);
					}
				}
				
			}
			items.invalidate();
			items.validate();
			items.repaint();
			jf.invalidate();
			jf.validate();
			jf.repaint();
			accept.setEnabled(true);

	}
}
	public void reinitItems() throws Exception {
		t.clear();
		items.removeAll();
		fromitems.removeAll();
		System.err.println("Removing items 1");
		StringBuffer sb = new StringBuffer();
		// begin trading by getting the users inventory
		t.startTrade(nnip, sb);
		StringTokenizer st = new StringTokenizer(sb.toString(), "\n");
		while (st.hasMoreTokens()) {
			String item = st.nextToken();
			System.err.println("Trade "+item);
			int fp = item.indexOf("|");
			int sp = item.indexOf("|", fp+1);
			int tp = item.indexOf("|", sp+1);
			String name = item.substring(sp+1, tp);
			String html = item.substring(tp+1);
			System.err.println("New checkbox "+name);
			HTMLJCheckBox jcb = new HTMLJCheckBox(name, item+"\n");
			t.add(jcb);
			items.add(jcb);
			jcb.setVisible(true);
		}
		items.invalidate();
		items.validate();
		items.repaint();
		jf.invalidate();
		jf.validate();
		jf.repaint();
	}
	public void actionPerformed(ActionEvent ae) {
		try {
			if (ae.getActionCommand().equals("Get Inventory")) {
				reinitItems();
				offer.setEnabled(true);
				cancel.setEnabled(true);
				trade.setEnabled(false);
			} else if (ae.getActionCommand().equals("Make Offer")) {
				System.err.println("Offering");
				try {
					t.makeOffer(nnip, "OFFER");
					new WaitForOffer().start();
					reject.setEnabled(true);
					offer.setEnabled(false);
					cancel.setEnabled(false);
				} catch(Exception e) {
					trade.setEnabled(true);
					offer.setEnabled(false);
					cancel.setEnabled(false);
				}
			} else if (ae.getActionCommand().equals("Accept Offer")) {
				trade.setEnabled(true);
				accept.setEnabled(false);
				reject.setEnabled(false);
				try {
					t.acceptOffer(nnip, "ACCEPT");
					t.transferGoods(nnip);
					t.clear();
					System.err.println("Removing items 2");
					items.removeAll();
					fromitems.removeAll();
					items.invalidate();
					items.validate();
					items.repaint();
					jf.invalidate();
					jf.validate();
					jf.repaint();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (ae.getActionCommand().equals("Reject Offer")) {
				try {
					t.acceptOffer(nnip, "REJECT");
				} catch (Exception e) {
					e.printStackTrace();
				}
				trade.setEnabled(true);
				accept.setEnabled(false);
				reject.setEnabled(false);
			} else if (ae.getActionCommand().equals("Cancel Trade")) {
				try {
					t.makeOffer(nnip, "REJECT");
				} catch (Exception e) {
					e.printStackTrace();
				}
				trade.setEnabled(true);
				offer.setEnabled(false);
				cancel.setEnabled(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void mouseClicked(MouseEvent e) {
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
		try {
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (!nick1.equals(nick2)) {
			jf.setSize(400, 600);
			jf.setVisible(true);
		}
	}
}
public class Chat extends ClientOnServer implements WindowListener, ActionListener, FocusListener, ReceiveChatInterface {
	JEditorPane ta = null;
	JTextField tf = null;
	JList<String> jp = null;
	Vector<String> history = new Vector<String>();
	int current = 0;
	Trade t = null;
	Vector<String> contacts = new Vector<String>();
	StringBuffer html = new StringBuffer("<html><head><title>Chat</title></head><body><h1><font color='white'>Start Chatting</font></h1><div align='left'></div></body></html>");
	String color = "white";
	Smiley smilies = new Smiley();
	Synth synthesizer = new Synth();
	SendCommandInterface sci;
        String lang = "en";
        boolean sound = false;
        public Chat(InputStream is, OutputStream os) throws Exception {
            super(is, os);
        }
        public Chat(Socket s) throws Exception {
            super(s);
	}
        public Chat() throws Exception {
                super();
        }
        public Chat(String host, int port) throws Exception {
            super(host, port);
        }
        public Chat(String host, int port, PossibleConnection pcon) throws Exception {
            super(host, port);
         }
	public void setColor(String color) {
		this.color = color;
	}
	Hashtable<String, Message> client_messages = new Hashtable<String, Message>();
        public void flushContacts() {
            try {
                DefaultListModel<String> dlm = (DefaultListModel<String>)jp.getModel();
                dlm.clear();
                contacts.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	public void addChatter(String path, String chatter) {
                DefaultListModel<String> dlm = (DefaultListModel<String>)jp.getModel();
		if (!dlm.contains(chatter)) {
			contacts.add(0, path);
			dlm.add(0, chatter);
		}
	}
	public void removeChatter(String chatter) {
                DefaultListModel<String> dlm = (DefaultListModel<String>)jp.getModel();
		int index = dlm.indexOf(chatter);
		if (index >= 0) {
			contacts.remove(index);
			dlm.remove(index);
		}
	}
	public Message processLine(String line) throws Exception {
		Message m = super.processLine(line);
		// Message m = Message.parse(line);
	        if (m != null && !seenMessage(m, client_messages)) {
			/*
			if (m.error.equals(K100)) {
				System.err.println("Found error "+m.error+" in message "+m.message+" from "+m.from);
				String chatter = m.message.intern();
				removeChatter(chatter);
			} else {
			*/
			String path = m.from.intern();
			String chatter = path;
			chatter = m.nick.intern();
			addChatter(path, chatter);

			// System.err.println("Displaying "+m.message);
			displayToScreen(m);
			// receive(line); // for channel
			jp.invalidate();
			jp.validate();
			jp.repaint();
			// if the client is also a server
			try {
				Hashtable rec = prepareToSend(m);
				send(m, rec);
			} catch (Message msge) {
				messageException(msge);
			}
			return m;
		} else {
			return null;
	        }
	}
	Hashtable<String, Message> gui_messages = new Hashtable<String, Message>();
	class ProcessCommand implements ActionListener {
            public void actionPerformed(ActionEvent ae) {
                String line = tf.getText();
		if (line.trim().equals("")) {
			return;
		}
                history.add(line);
                current = history.size();
                int [] clino = jp.getSelectedIndices();
		// we will fill in who the message is to below
		Hashtable<String,String> rec = new Hashtable<String,String>();
		Hashtable<String,String> ircrec = new Hashtable<String,String>();
                for (int i = 0; i < clino.length; i++) {
		    String contact = (String)contacts.get(clino[i]);
		    int ircind = contact.indexOf("irc:");
		    if (ircind == 0) {
			    ircrec.put(contact.substring(4), contact.substring(4));
		    } else {
			    rec.put(contact, contact);
		    }
                }
                if (clino.length == 0) {
		    rec.put("*", "*");
                }
		String from;
		if (sci != null) {
			from = sci.getUser();
		} else {
			from = getNick();
		}
	        Message m = new Message(rec, from, line, lang);
		try {
			Hashtable clientrec = prepareToSend(m);
                        send(m, clientrec);
		} catch (Message msge) {
			messageException(msge);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.err.println("Sending "+line);
	        if (!seenMessage(m, gui_messages)) {
			try {
				if (sci != null) {
					if (ircrec.size() > 0) {
						Iterator irci = ircrec.keySet().iterator();
						while (irci.hasNext()) {
							sci.msgSend(line, (String)irci.next());
						}
					} else {
						/*
							if (sci instanceof Heathens) {
								sci.msgSend(m.generate());
							} else {
							*/
								sci.msgSend(line);
								/*
							}
							*/
					}
				}
				say(from, "says", line, color);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
                tf.setText("");
        }
	}
	public void say(String nick, String verb, String message, String color) {
		if (sound) {
                    synthesizer.speak(nick+" "+verb+" "+message);
                }
		if (verb.equals("says")) {
			addToEnd("<div align='left'><h3><font color='"+color+"'>&lt;"+nick+"&gt;"+smilies.replace(message)+"</font></h3></div>");
		} else {
			addToEnd("<div align='left'><h3><font color='"+color+"'>"+nick+" "+verb+" "+message+"</font></h3></div>");
		}
	}
	public void scrollToBottom() {

	}
	public void addToEnd(String text) {
	    text = quoteURL(text);
	    
	    if (ta != null && !text.trim().equals("")) {
		try {
                 
                    HTMLDocument doc = (HTMLDocument)ta.getDocument();
                    Element html = doc.getDefaultRootElement();
                    Element body = html.getElement(html.getElementCount()-1);
                    Element div = body.getElement(body.getElementCount()-1);
                    doc.insertBeforeStart(div, text);

SwingUtilities.invokeLater(new Runnable() {
        public void run() {
                    Rectangle r = ta.getBounds();
                    r.y = r.height-100;
                    r.height = 100;
		    // r = new Rectangle(0,ta.getHeight()-2,1,1);
                    ta.scrollRectToVisible(r);
        }
      });
		} catch (Exception e) {
			e.printStackTrace();
		}
	    } else {
                    System.err.println("ta is "+ta+" text is "+ text);
            }
	}
	public String quoteURL(String text) {
	    int http = text.indexOf("http://");
	    int httpq = text.indexOf("'http://");
	    int httpqq = text.indexOf("\"http://");
	    while (http >= 0 && httpq != http-1 && httpqq != http-1) {
		int sp = text.indexOf("<", http);
		int sp2 = text.indexOf(" ", http);
		System.err.println("http="+http+" sp="+sp+" sp2="+sp2);
		if (sp > sp2 && sp2 != -1) {
			sp = sp2;
		}
		if (sp >= 0) {
			text = text.substring(0, http) + "<a href='" + text.substring(http, sp) + "'>" + text.substring(http, sp) + "</a>" + text.substring(sp);
		} else {
			text = text.substring(0, http) + "<a href='" + text.substring(http) + "'>"+text.substring(http)+"</a>";
		}
                int endanchor = text.indexOf("</a>");
		http = text.indexOf("http://", endanchor);
		httpq = text.indexOf("'http://", endanchor);
		httpqq = text.indexOf("\"http://", endanchor);
	    }
	    return text;
	}
	public void tabComplete() {
                DefaultListModel<String> dlm = (DefaultListModel<String>)jp.getModel();
		Enumeration en = dlm.elements();
		String txt = tf.getText();
		if (txt.trim().equals("")) {
			return;
		}
		int sp = txt.lastIndexOf(" ");
		String prenick;
		if (sp >= 0) {
			prenick = txt.substring(sp+1);
		} else {
			prenick = txt;
		}
		while (en.hasMoreElements()) {
			String chtr = (String)en.nextElement();
			if (chtr.toLowerCase().startsWith(prenick.toLowerCase()))  {
				if (sp >= 0) {
					tf.setText(txt.substring(0, sp+1) + chtr);
				} else {
					tf.setText(chtr+", ");
				}
			}
		}
	}
	public void init() {
	    try {
		tf.addActionListener(new ProcessCommand());
		tf.addFocusListener(this);
                ta.setEditable(true);
		ta.setText(html.toString());
		jp.setModel(new DefaultListModel());
		setColor("white");
		tf.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					previous();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					next();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					tabComplete();
				}
			}
			public void keyReleased(KeyEvent e) {
			}
			public void keyTyped(KeyEvent e) {
			}
		});
		addActions();
		t = new Trade("http://localhost");
	    } catch (Exception e) {
		System.err.println("Exception "+e);
		e.printStackTrace();
	    }
	}

	public void next() {
		current++;
		if (current >= history.size()) {
		    current = history.size();
                    tf.setText("");
		} else if (history.size() > 0) {
                    tf.setText(history.elementAt(current).toString());
		}
	}
	public void previous() {
                if (current > 0) {
                        current--;
                }
                if (history.size() > 0) {
			tf.setText(history.elementAt(current).toString());
		}

        }
	public StringBuffer inventory() throws Exception {
		StringBuffer sb = new StringBuffer();
		StringBuffer table = new StringBuffer();
		// short version of nnip
		String ip1 = "127.0.0.1";
		try {
			ip1 = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
		}
		String nnip = "nick1="+getNick()+"&ip1="+ip1;
		t.inventory(nnip, sb);
		StringTokenizer st = new StringTokenizer(sb.toString(), "\n");
		table.append("<table border='1'>");
		while (st.hasMoreTokens()) {
			String item = st.nextToken();
			int fp = item.indexOf("|");
			int sp = item.indexOf("|", fp+1);
			int tp = item.indexOf("|", sp+2);
			String name = item.substring(sp+1, tp);
			String html = item.substring(tp+1);
			table.append("<tr><td>"+name+"</td><td>"+html+"</td></tr>");
		}
		table.append("<tr><td>Schizophrenics Chat Log</td><td><a href='http://localhost/chat/roomchat.txt'>Chat Log</a></td></tr>");
		table.append("</table>");
		return table;
	}
	HashMap ircnicks = new HashMap();

	public synchronized void displayToScreen(Message m) {
	    try  {
                m.translate(lang);
	        String text = quoteURL(m.message);
	        if (!seenMessage(m, gui_messages)) {
			say(m.nick, "says", text, color);
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	public void windowClosing(WindowEvent we) {
                Message m = new Message("*", getNick(), "has exited", "en");
                try {
		    Hashtable rec = prepareToSend(m);
                    send(m, rec);
                } catch (Message msge) {
		    messageException(msge);
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
	public void focusGained(FocusEvent fe) {
	}
	public void focusLost(FocusEvent fe) {
		/*
		tabComplete();
		tf.requestFocus();
		*/
	}
	boolean source = false;
	public void actionPerformed(ActionEvent ae) {
		if (ae.getActionCommand().equals("View Source")) {
			source = !source;
			String text = ta.getText();
			if (source) {
				ta.setContentType("text/plain");
			} else {
				ta.setContentType("text/html");
			}
			ta.setText(text);
		}
	}
	public void addActions() {
	}
	public void setSendCommandInterface(SendCommandInterface bridge) throws Exception {
		sci = bridge;
		setNick(sci.getUser());
	}

	// begin receive interface
	public void receive(String from, String message, String color) {
		say(from, "says", message, color);
	}
	public void receive(String from, String message) {
		System.err.println("Message in receive is "+message);
		say(from, "says", message, "FFFFFF");
	}
	public void receive(String from, InputStream file) {
	}
	public void receiveAction(String from, String act) {
		say(from, "", act, "00FF00");
	}
	public void receiveJoin(String network, String room, String person) {
		say(person, "joins", room, "0000FF");
		addChatter(network+person, person);
		addChatter(network+room, room);
	}
	public void receivePresence(String network, String room, String person) {
		addChatter(network+person, person);
	}
	public void receiveLeave(String room, String person) {
		say(person, "leaves", room, "0000FF");
		if (sci != null && sci.getUser().equals(person)) {
			removeChatter(room);
		}
		removeChatter(person);
	}
	public void receiveQuit(String person) {
		say(person, "quits", "", "0000FF");
		removeChatter(person);
	}
	public void receiveRoom(String network, String room, int numUsers, String topic) {
		addChatter(network+room, room);
	}
	public void receiveNick(String network, String oldnick, String newnick) {
		say(oldnick, "changes nick to", newnick, "0000FF");
		removeChatter(oldnick);
		addChatter(network+newnick, newnick);
		if (oldnick.equals(getNick())) {
			try {
				setNick(newnick);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
        // end receive interface
        public void setLanguage(String lang) {
            this.lang = lang;
        }
        public String getLanguage() {
		return lang;
	}
        public void toggleSound() {
            sound = !sound;
        }
}
