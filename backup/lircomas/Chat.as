package lircom {
import java.io.*;
import java.util.*;
import java.awt.event.*;
import java.awt.*;
import java.net.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

class FromGUI extends InputStream {
	var ba:int= 0;
        var pos:int= 0;
        var buffer:Array;
	public function FromGUI() {
	}
	public function write(b:Array):void {
		synchronized (this) {
                        if (buffer == null) {
                                buffer = (byte [])b.clone();
                                ba = buffer.length;
                                pos = 0;
                        } else {
                                var newbuffer:Array= new byte[buffer.length+b.length];
                                var i:int;
                                for (i = 0; i < buffer.length; i++) {
                                    newbuffer[i] = buffer[i];
                                }
                                for (var j:int= 0; j < b.length; j++) {
                                    newbuffer[i+j] = b[j];
                                }
                                ba += b.length;
                                buffer = newbuffer;
                        }
		}
	}
	public function available():int {
		while (ba == 0) {
			try {
				Thread.sleep(500);
			} catch (var e:Exception) {
			}
		}
		synchronized (this) {
			return ba;
		}
	}
	public function read():int {
		while (ba == 0) {
			try {
				Thread.sleep(500);
			} catch (var e:Exception) {
			}
		}
		synchronized (this) {
			var b:int= buffer[pos];
			ba--;
                        pos++;
                        if (ba == 0) {
                            buffer = null;
                            pos = 0;
                        }
			return b;
		}
	}
	public function read(b:Array, off:int, len:int):int {
		while (ba == 0) {
			try {
				Thread.sleep(500);
			} catch (var e:Exception) {
			}
		}
		synchronized (this) {
			if (len <= ba) {
                                for (var i:int= 0; i < len; i++) {
                                    b[i+off] = buffer[pos];
                                    pos++;
                                }
				ba -= len;
				return len;
			} else {
                                for (var i:int= 0; i < ba; i++) {
                                    b[i+off] = buffer[pos];
                                    pos++;
                                }
				var bytes:int= ba;
				ba = 0;
                                buffer = null;
                                pos = 0;
				return bytes;
			}
		}
	}
	public function read(b:Array):int {
		return read(b, 0, b.length);
	}
}

class FromServer extends OutputStream {
	var c:Chat;
	public function FromServer() {
	}
	public function setChat(c:Chat):void {
		this.c = c;
	}
	public function write(b:int):void {
		// System.err.println("wrote "+b);
                var m:Message= new Message("Tiny Byte", "Tiny Byte", b+"", "__");
		c.displayToScreen(m);  // TODO UTF-8
	}
	public function write(buf:Array, off:int, len:int):void {
		try {
			var line:String= new String(buf, off, len); 
			// System.err.println(c.getNick()+" received "+line);
			c.processLine(line);
		} catch (var e:Exception) {
			e.printStackTrace();
		}
	}
}

class HTMLJCheckBox extends JCheckBox {
	var item:String= null;
	public function HTMLJCheckBox(name:String, item:String) {
		super(name);
		this.item = item;
	}
}

class MouseLabel extends JLabel implements MouseListener, ActionListener {
	var jf:JFrame= null;
	var t:Trade= null;
	var nick1:String= null;
	var nick2:String= null;
	var ipaddr:String= null;
	var trade:JButton= null;
	var offer:JButton= null;
	var accept:JButton= null;
	var cancel:JButton= null;
	var reject:JButton= null;
	var items:JPanel= null;
	var fromitems:JPanel= null;
	var nnip:String= "";
	public function MouseLabel(nick1:String, nick2:String, ipaddr:String, t:Trade) {
		super(nick2);
		this.nick1 = nick1;
		this.ipaddr = ipaddr;
		this.nick2 = nick2;
		var ip1:String= "127.0.0.1";
		try {
			ip1 = InetAddress.getLocalHost().getHostName();
		} catch (var e:Exception) {
		}
		nnip = "nick1="+nick1+"&nick2="+nick2+"&ip1="+ip1+"&ip2="+ipaddr;
		// System.err.println(nnip);
		
		jf = new JFrame(nick2);
		var nicktitle:JLabel= new JLabel("Trading With "+nick2);
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


		var buttons:JPanel= new JPanel();
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
	public function run():void {
		var sb:StringBuffer= new StringBuffer();
		try {
			// System.err.println("Getting the other parties offer");
			if (!t.getOffer(nnip, sb)) {
				throw new Exception("REJECT"); // other party cancelled
			}
		} catch (var e:Exception) {
			trade.setEnabled(true);
			reject.setEnabled(false);
			return;
		}
			System.err.println("Retrieved "+sb);
			var st:StringTokenizer= new StringTokenizer(sb.toString(), "\n");
			
			for (var i:int= 0; i < items.getComponentCount(); i++) {
				var c:Component= items.getComponent(i);
				if (c instanceof HTMLJCheckBox) {
					var jcb:HTMLJCheckBox= HTMLJCheckBox(c);
					jcb.setEnabled(false);
				}
			}
			var jl:JLabel= null;
			jl = new JLabel("From "+nick2);
			fromitems.add(jl);
			while (st.hasMoreTokens()) {
				var item:String= st.nextToken();
				System.err.println("Getting item "+item);
				var fp:int= item.indexOf("|");
				if (fp >= 0) {
					var sp:int= item.indexOf("|", fp+1);
					var tp:int= item.indexOf("|", sp+1);
					var nck:String= item.substring(0, fp);
					var ip:String= item.substring(fp+1, sp);
					var name:String= item.substring(sp+1, tp);
					var html:String= item.substring(tp+1);
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
	public function reinitItems():void throws Exception {
		t.clear();
		items.removeAll();
		fromitems.removeAll();
		System.err.println("Removing items 1");
		var sb:StringBuffer= new StringBuffer();
		// begin trading by getting the users inventory
		t.startTrade(nnip, sb);
		var st:StringTokenizer= new StringTokenizer(sb.toString(), "\n");
		while (st.hasMoreTokens()) {
			var item:String= st.nextToken();
			System.err.println("Trade "+item);
			var fp:int= item.indexOf("|");
			var sp:int= item.indexOf("|", fp+1);
			var tp:int= item.indexOf("|", sp+1);
			var name:String= item.substring(sp+1, tp);
			var html:String= item.substring(tp+1);
			System.err.println("New checkbox "+name);
			var jcb:HTMLJCheckBox= new HTMLJCheckBox(name, item+"\n");
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
	public function actionPerformed(ae:ActionEvent):void {
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
				} catch(var e:Exception) {
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
				} catch (var e:Exception) {
					e.printStackTrace();
				}
			} else if (ae.getActionCommand().equals("Reject Offer")) {
				try {
					t.acceptOffer(nnip, "REJECT");
				} catch (var e:Exception) {
					e.printStackTrace();
				}
				trade.setEnabled(true);
				accept.setEnabled(false);
				reject.setEnabled(false);
			} else if (ae.getActionCommand().equals("Cancel Trade")) {
				try {
					t.makeOffer(nnip, "REJECT");
				} catch (var e:Exception) {
					e.printStackTrace();
				}
				trade.setEnabled(true);
				offer.setEnabled(false);
				cancel.setEnabled(false);
			}
		} catch (var e:Exception) {
			e.printStackTrace();
		}
	}
	public function mouseClicked(e:MouseEvent):void {
	}
	public function mouseEntered(e:MouseEvent):void {
	}
	public function mouseExited(e:MouseEvent):void {
	}
	public function mousePressed(e:MouseEvent):void {
	}
	public function mouseReleased(e:MouseEvent):void {
		try {
		} catch (var ex:Exception) {
			ex.printStackTrace();
		}
		if (!nick1.equals(nick2)) {
			jf.setSize(400, 600);
			jf.setVisible(true);
		}
	}
}
public class Chat extends ClientOnServer implements WindowListener, ActionListener, FocusListener, ReceiveChatInterface {
	var ta:JEditorPane= null;
	var tf:JTextField= null;
	var jp:JList= null;
	var history:Vector= new Vector();
	var current:int= 0;
	var t:Trade= null;
	var contacts:Vector= new Vector();
	var html:StringBuffer= new StringBuffer("<html><head><title>Chat</title></head><body><h1><font color='white'>Start Chatting</font></h1><div align='left'></div></body></html>");
	var color:String= "white";
	var smilies:Smiley= new Smiley();
	// Synth synthesizer = new Synth();
	var sci:SendCommandInterface;
        var lang:String= "en";
        var sound:Boolean= false;
        public function Chat(is:InputStream, os:OutputStream) throws Exception {
            super(is, os);
        }
        public function Chat(s:Socket) throws Exception {
            super(s);
	}
        public function Chat() throws Exception {
                super();
        }
        public function Chat(host:String, port:int) throws Exception {
            super(host, port);
        }
        public function Chat(host:String, port:int, pcon:PossibleConnection) throws Exception {
            super(host, port);
         }
	public function setColor(color:String):void {
		this.color = color;
	}
	var client_messages:Hashtable= new Hashtable();
        public function flushContacts():void {
            try {
                var dlm:DefaultListModel= DefaultListModel(jp.getModel());
                dlm.clear();
                contacts.clear();
            } catch (var e:Exception) {
                e.printStackTrace();
            }
        }
	public function addChatter(path:String, chatter:String):void {
		var dlm:DefaultListModel= DefaultListModel(jp.getModel());
		if (!dlm.contains(chatter)) {
			contacts.add(0, path);
			dlm.add(0, chatter);
		}
	}
	public function removeChatter(chatter:String):void {
		var dlm:DefaultListModel= DefaultListModel(jp.getModel());
		var index:int= dlm.indexOf(chatter);
		if (index >= 0) {
			contacts.remove(index);
			dlm.remove(index);
		}
	}
	public function processLine(line:String):Boolean throws Exception {
		var m:Message= Message.parse(line);
	        if (!seenMessage(m, client_messages)) {
			if (m.error.equals(K100)) {
				System.err.println("Found error "+m.error+" in message "+m.message+" from "+m.from);
				var chatter:String= m.message.intern();
				removeChatter(chatter);
			} else {
				System.err.println("No error, adding "+m.from);
				var path:String= m.from.intern();
				var chatter:String= path;
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
					var rec:Hashtable= prepareToSend(m);
					send(m, rec);
				} catch (var msge:Message) {
					messageException(msge);
				}
			}
			return true;
		} else {
			return false;
	        }
	}
	var gui_messages:Hashtable= new Hashtable();
	class ProcessCommand implements ActionListener {
            public function actionPerformed(ae:ActionEvent):void {
                var line:String= tf.getText();
		if (line.trim().equals("")) {
			return;
		}
                history.add(line);
                current = history.size();
                var clino:Array= jp.getSelectedIndices();
		// we will fill in who the message is to below
		var rec:Hashtable= new Hashtable();
		var ircrec:Hashtable= new Hashtable();
                for (var i:int= 0; i < clino.length; i++) {
		    var contact:String= String(contacts.get(clino[i]));
		    var ircind:int= contact.indexOf("irc:");
		    if (ircind == 0) {
			    ircrec.put(contact.substring(4), contact.substring(4));
		    } else {
			    rec.put(contact, contact);
		    }
                }
                if (clino.length == 0) {
		    rec.put("*", "*");
                }
		var from:String;
		if (sci != null) {
			from = sci.getUser();
		} else {
			from = getNick();
		}
	        var m:Message= new Message(rec, from, line, lang);
		try {
			var clientrec:Hashtable= prepareToSend(m);
                        send(m, clientrec);
		} catch (var msge:Message) {
			messageException(msge);
		} catch (var e:Exception) {
			e.printStackTrace();
		}
		System.err.println("Sending "+line);
	        if (!seenMessage(m, gui_messages)) {
			try {
				if (sci != null) {
					if (ircrec.size() > 0) {
						var irci:Iterator= ircrec.keySet().iterator();
						while (irci.hasNext()) {
							sci.msgSend(line, String(irci.next()));
						}
					} else {
							if (sci instanceof Heathens) {
								sci.msgSend(m.generate());
							} else {
								sci.msgSend(line);
							}
					}
				}
				say(from, "says", line, color);
			} catch (var e:Exception) {
				e.printStackTrace();
			}
		}
                tf.setText("");
        }
	}
	public function say(nick:String, verb:String, message:String, color:String):void {
		if (sound) {
                    //  synthesizer.speak(nick+" "+verb+" "+message);
                }
		if (verb.equals("says")) {
			addToEnd("<div align='left'><h3><font color='"+color+"'>&lt;"+nick+"&gt;"+smilies.replace(message)+"</font></h3></div>");
		} else {
			addToEnd("<div align='left'><h3><font color='"+color+"'>"+nick+" "+verb+" "+message+"</font></h3></div>");
		}
	}
	public function scrollToBottom():void {

	}
	public function addToEnd(text:String):void {
	    text = quoteURL(text);
	    
	    if (ta != null && !text.trim().equals("")) {
		try {
                 
                    var doc:HTMLDocument= HTMLDocument(ta.getDocument());
                    var html:Element= doc.getDefaultRootElement();
                    var body:Element= html.getElement(html.getElementCount()-1);
                    var div:Element= body.getElement(body.getElementCount()-1);
                    doc.insertBeforeStart(div, text);
                    var r:Rectangle= ta.getBounds();
                    r.y = r.height-100;
                    r.height = 100;
                    ta.scrollRectToVisible(r);
		} catch (var e:Exception) {
			e.printStackTrace();
		}
	    } else {
                    System.err.println("ta is "+ta+" text is "+ text);
            }
	}
	public function quoteURL(text:String):String {
	    var http:int= text.indexOf("http://");
	    var httpq:int= text.indexOf("'http://");
	    var httpqq:int= text.indexOf("\"http://");
	    while (http >= 0&& httpq != http-1&& httpqq != http-1) {
		var sp:int= text.indexOf("<", http);
		var sp2:int= text.indexOf(" ", http);
		System.err.println("http="+http+" sp="+sp+" sp2="+sp2);
		if (sp > sp2 && sp2 != -1) {
			sp = sp2;
		}
		if (sp >= 0) {
			text = text.substring(0, http) + "<a href='" + text.substring(http, sp) + "'>" + text.substring(http, sp) + "</a>" + text.substring(sp);
		} else {
			text = text.substring(0, http) + "<a href='" + text.substring(http) + "'>"+text.substring(http)+"</a>";
		}
                var endanchor:int= text.indexOf("</a>");
		http = text.indexOf("http://", endanchor);
		httpq = text.indexOf("'http://", endanchor);
		httpqq = text.indexOf("\"http://", endanchor);
	    }
	    return text;
	}
	public function tabComplete():void {
		var dlm:DefaultListModel= DefaultListModel(jp.getModel());
		var en:Enumeration= dlm.elements();
		var txt:String= tf.getText();
		if (txt.trim().equals("")) {
			return;
		}
		var sp:int= txt.lastIndexOf(" ");
		var prenick:String;
		if (sp >= 0) {
			prenick = txt.substring(sp+1);
		} else {
			prenick = txt;
		}
		while (en.hasMoreElements()) {
			var chtr:String= String(en.nextElement());
			if (chtr.toLowerCase().startsWith(prenick.toLowerCase()))  {
				if (sp >= 0) {
					tf.setText(txt.substring(0, sp+1) + chtr);
				} else {
					tf.setText(chtr+", ");
				}
			}
		}
	}
	public function init():void {
	    try {
		tf.addActionListener(new ProcessCommand());
		tf.addFocusListener(this);
                ta.setEditable(true);
		ta.setText(html.toString());
		jp.setModel(new DefaultListModel());
		setColor("white");
		tf.addKeyListener(new KeyListener() {
			public function keyPressed(e:KeyEvent):void {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					previous();
				} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					next();
				} else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					tabComplete();
				}
			}
			public function keyReleased(e:KeyEvent):void {
			}
			public function keyTyped(e:KeyEvent):void {
			}
		});
		addActions();
		t = new Trade("http://localhost");
	    } catch (var e:Exception) {
		System.err.println("Exception "+e);
		e.printStackTrace();
	    }
	}

	public function next():void {
		current++;
		if (current >= history.size()) {
		    current = history.size();
                    tf.setText("");
		} else if (history.size() > 0) {
                    tf.setText(history.elementAt(current).toString());
		}
	}
	public function previous():void {
                if (current > 0) {
                        current--;
                }
                if (history.size() > 0) {
			tf.setText(history.elementAt(current).toString());
		}

        }
	public function inventory():StringBuffer throws Exception {
		var sb:StringBuffer= new StringBuffer();
		var table:StringBuffer= new StringBuffer();
		// short version of nnip
		var ip1:String= "127.0.0.1";
		try {
			ip1 = InetAddress.getLocalHost().getHostName();
		} catch (var e:Exception) {
		}
		var nnip:String= "nick1="+getNick()+"&ip1="+ip1;
		t.inventory(nnip, sb);
		var st:StringTokenizer= new StringTokenizer(sb.toString(), "\n");
		table.append("<table border='1'>");
		while (st.hasMoreTokens()) {
			var item:String= st.nextToken();
			var fp:int= item.indexOf("|");
			var sp:int= item.indexOf("|", fp+1);
			var tp:int= item.indexOf("|", sp+2);
			var name:String= item.substring(sp+1, tp);
			var html:String= item.substring(tp+1);
			table.append("<tr><td>"+name+"</td><td>"+html+"</td></tr>");
		}
		table.append("<tr><td>Schizophrenics Chat Log</td><td><a href='http://localhost/chat/roomchat.txt'>Chat Log</a></td></tr>");
		table.append("</table>");
		return table;
	}
	var ircnicks:HashMap= new HashMap();

	public synchronized function displayToScreen(m:Message):void {
	    try  {
                m.translate(lang);
	        var text:String= quoteURL(m.message);
	        if (!seenMessage(m, gui_messages)) {
			say(m.nick, "says", text, color);
		}
	    } catch (var e:Exception) {
		e.printStackTrace();
	    }
	}
	public function windowClosing(we:WindowEvent):void {
                var m:Message= new Message("*", getNick(), "has exited", "en");
                try {
		    var rec:Hashtable= prepareToSend(m);
                    send(m, rec);
                } catch (var msge:Message) {
		    messageException(msge);
                } catch (var e:Exception) {
                    e.printStackTrace();
                }
		System.exit(0);
	}
	public function windowActivated(we:WindowEvent):void {
	}
	public function windowClosed(we:WindowEvent):void {
	}
	public function windowDeactivated(we:WindowEvent):void {
	}
	public function windowDeiconified(we:WindowEvent):void {
	}
	public function windowIconified(we:WindowEvent):void {
	}
	public function windowOpened(we:WindowEvent):void {
	}
	public function focusGained(fe:FocusEvent):void {
	}
	public function focusLost(fe:FocusEvent):void {
		tabComplete();
		tf.requestFocus();
	}
	var source:Boolean= false;
	public function actionPerformed(ae:ActionEvent):void {
		if (ae.getActionCommand().equals("View Source")) {
			source = !source;
			var text:String= ta.getText();
			if (source) {
				ta.setContentType("text/plain");
			} else {
				ta.setContentType("text/html");
			}
			ta.setText(text);
		}
	}
	public function addActions():void {
	}
	public function setSendCommandInterface(bridge:SendCommandInterface):void throws Exception {
		sci = bridge;
		setNick(sci.getUser());
	}

	// begin receive interface
	public function receive(from:String, message:String, color:String):void {
		say(from, "says", message, color);
	}
	public function receive(from:String, message:String):void {
		System.err.println("Message in receive is "+message);
		say(from, "says", message, "FFFFFF");
	}
	public function receive(from:String, file:InputStream):void {
	}
	public function receiveAction(from:String, act:String):void {
		say(from, "", act, "00FF00");
	}
	public function receiveJoin(network:String, room:String, person:String):void {
		say(person, "joins", room, "0000FF");
		addChatter(network+person, person);
		addChatter(network+room, room);
	}
	public function receivePresence(network:String, room:String, person:String):void {
		addChatter(network+person, person);
	}
	public function receiveLeave(room:String, person:String):void {
		say(person, "leaves", room, "0000FF");
		if (sci != null && sci.getUser().equals(person)) {
			removeChatter(room);
		}
		removeChatter(person);
	}
	public function receiveQuit(person:String):void {
		say(person, "quits", "", "0000FF");
		removeChatter(person);
	}
	public function receiveRoom(network:String, room:String, numUsers:int, topic:String):void {
		addChatter(network+room, room);
	}
	public function receiveNick(network:String, oldnick:String, newnick:String):void {
		say(oldnick, "changes nick to", newnick, "0000FF");
		removeChatter(oldnick);
		addChatter(network+newnick, newnick);
		if (oldnick.equals(getNick())) {
			try {
				setNick(newnick);
			} catch (var e:Exception) {
				e.printStackTrace();
			}
		}
	}
        // end receive interface
        public function setLanguage(lang:String):void {
            this.lang = lang;
        }
        public function getLanguage():String {
		return lang;
	}
        public function toggleSound():void {
            sound = !sound;
        }
}
}