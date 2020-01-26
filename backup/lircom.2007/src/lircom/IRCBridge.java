package lircom;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import f00f.net.irc.martyr.*;
import f00f.net.irc.martyr.services.*;
import f00f.net.irc.martyr.clientstate.*;
import f00f.net.irc.martyr.clientstate.*;
import f00f.net.irc.martyr.commands.*;
import f00f.net.irc.martyr.replies.*;

class IRCSendChat extends Thread implements SendChatInterface {
	InputStream is = null;
	OutputStream sos = null;
	PrintStream sps = null;
	String ircRoom1 = "#yottzumm1";
	String ircNick1 = "Unknown";
	boolean sent = false;
	ReceiveChatInterface chat;
	Vector people;
	public IRCSendChat(InputStream is, OutputStream sos, String ircRoom1, String ircNick1, ReceiveChatInterface chat) {
		this.is = is;
		this.sos = sos;
		sps = new PrintStream(sos);
		this.ircRoom1 = ircRoom1;
		this.ircNick1 = ircNick1;
		this.chat = chat;
	}
	public void setSent(boolean s) {
		sent = s;
	}
	public boolean getSent() {
		return sent;
	}
	class IRCDelay extends Thread {
		String ircRoom1;
		IRCSendChat s;
		public IRCDelay(String ircRoom1, IRCSendChat s) {
			this.ircRoom1 = ircRoom1;
			this.s = s;
		}
		public void run() {
			try {
				sleep(10000);
				if (!s.getSent()) {
					sendJoin(ircRoom1);
					println("MODE "+ircRoom1);
					s.setSent(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	class IRCPingPongOut extends Thread {
		public IRCPingPongOut() {
		}
		public void run() {
			try {
				while (true) {
					requestPeople(ircRoom1);
					sendPing();
					sleep(29000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void sendToChat(String nick, String act, String message) {
	}



	// send interface
	public void username(String username) {
		println("USER "+username+" "+username+" afternet.org :"+username);
	}
	public void nickname(String nick) {
		println("NICK "+nick);
		ircNick1 = nick;
	}
	public void password(String password) {
		if (password != null) {
			println("PASS "+password);
		}
	}
	public void sendJoin(String room) {
		println("JOIN "+room);
		ircRoom1 = room;
	}
	public void sendLeave(String room) {
		println("PART "+room);
	}
	public void sendQuit() {
		println("QUIT");
	}
	public void send(String to, String message) {
		println("PRIVMSG "+to+" :"+message);
	}
	public void send(String to, InputStream file) {
	}
	public void sendAction(String to, String message) {
		println("PRIVMSG "+to+" :\001ACTION"+message+"\001");
	}
	public void sendPing() {
		println("PING LAG"+System.currentTimeMillis());
	}
	public void requestPeople() {
		println("WHO *");
	}
	public void requestRooms() {
		println("LIST");
	}
	public void requestPeople(String room) {
		println("WHO "+room+" %ctnf,152");
	}
	public void requestRooms(String person) {
		println("WHO "+person);
	}
	// end send interface

	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			password(null);
			nickname(ircNick1);
			username(ircNick1);
			new IRCDelay(ircRoom1, this).start();
			new IRCPingPongOut().start();
			String line = null;
			while ((line = br.readLine()) != null) {	
				System.err.println(line);
				int i;
				if ((i = line.indexOf("PRIVMSG")) >= 0) {
					System.err.println("Processing PRIVMSG");
					int sl = line.indexOf(":", i);
					String nick = ircNick1;
					int ex = line.indexOf("!");
					if (ex >= 0) {
						nick = line.substring(1, ex);
					}
					if ((i = line.indexOf("\001")) >= 0) {
						String act = line.substring(i+7, line.length()-1);
						chat.receiveAction(nick, act);
					} else {
						String message = line.substring(sl+1);
						int lb = message.indexOf("{");
						int le = message.indexOf("}");
						if (lb > 0 || le == -1 || lb > le) {
							chat.receive(nick, message);
						} else {
							Message m = new Message("Goon", nick, message.substring(le+1), message.substring(lb+1, le));
							m.translate(chat.getLanguage());
							chat.receive(nick, m.message);
						}
						
					}
				} else if ((i = line.indexOf("NOTICE")) >= 0) {
					System.err.println("Processing NOTICE");
					int sl = line.indexOf(":", i);
					String nick = ircNick1;
					int ex = line.indexOf("!");
					if (ex >= 0) {
						nick = line.substring(1, ex);
					}
					chat.receive(nick, line.substring(sl+1));
				} else if ((i = line.indexOf("/QUOTE PONG")) >= 0) {
					System.err.println(" got to PONG !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
					println(line.substring(i+7));
					setSent(false);
					new IRCDelay(ircRoom1, this).start();
					/*
				} else if ((i = line.indexOf("PING")) >= 0) {
					System.err.println("Processing PING");
					new IRCDelay(ircRoom1, this).start();
				} else if ((i = line.indexOf("PONG")) >= 0) {
					System.err.println("Processing PONG");
					println("PING"+line.substring(i+4));
					new IRCDelay(ircRoom1, this).start();
					*/
				} else if ((i = line.indexOf("JOIN")) >= 0) {
					System.err.println("Processing JOIN");
					int sl = line.indexOf(":", i);
					String nick = ircNick1;
					int ex = line.indexOf("!");
					if (ex >= 0) {
						nick = line.substring(1, ex);
					}
					chat.receiveJoin("irc:", line.substring(sl+1), nick);
				} else if ((i = line.indexOf("PART")) >= 0) {
					System.err.println("Processing PART");
					int sl = line.indexOf(":", i);
					String nick = ircNick1;
					int ex = line.indexOf("!");
					if (ex >= 0) {
						nick = line.substring(1, ex);
					}
					chat.receiveLeave(line.substring(i+4,sl).trim(), nick);
				} else if ((i = line.indexOf(" 005 ")) >= 0) {
				} else if ((i = line.indexOf("NICK")) >= 0) {
					System.err.println("Processing NICK");
					int cl = line.indexOf(":", i);
					int sl = line.indexOf(":");
					int ex = line.indexOf("!");
					String oldnick = ircNick1;
					if (sl >= 0 && ex >= 0) {
						oldnick = line.substring(sl+1, ex);
					}
					String newnick = line.substring(cl+1);
					chat.receiveNick("irc:", oldnick, newnick);
				} else if ((i = line.indexOf("QUIT")) >= 0) {
					System.err.println("Processing QUIT");
					int sl = line.indexOf(":", i);
					String nick = ircNick1;
					int ex = line.indexOf("!");
					if (ex >= 0) {
						nick = line.substring(1, ex);
					}
					chat.receiveQuit(nick);
				} else if ((i = line.indexOf("451")) >= 0) {
					System.err.println("Processing 451");
					password(null);
					nickname(ircNick1);
					username(ircNick1);
				} else if ((i = line.indexOf("352")) >= 0) {
					StringTokenizer st = new StringTokenizer(line, " ");
					String host = st.nextToken();
					String cmd = st.nextToken();
					String mynick = st.nextToken();
					String channel = st.nextToken();
					String user = st.nextToken();
					String usermachine = st.nextToken();
					String servermachine = st.nextToken();
					String othernick = st.nextToken();
					chat.receivePresence("irc:", channel, othernick);
				} else if ((i = line.indexOf("322")) >= 0) {
					System.err.println(line);
					StringTokenizer st = new StringTokenizer(line, " ");
					String host = st.nextToken();
					String cmd = st.nextToken();
					String mynick = st.nextToken();
					String channel = st.nextToken();
					int numUsers = Integer.parseInt(st.nextToken());
					String topic = line.substring(line.indexOf(":", 1));
					chat.receiveRoom("irc:", channel, numUsers, topic);

				}
			}
			System.err.println("Closing");
			sps.close();
			is.close();
			sos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void println(String str) {
		System.err.println("to SERVER "+str);
		sps.println(str);
		sps.flush();
	}
}

public class IRCBridge extends ClientState implements Observer, ActionListener, SendCommandInterface {
	public static final String CONNECT_BUTTON = "Connect to IRC";

	JFrame jf = new JFrame("IRC Connection Info");
    	JTextField hosttf = new JTextField();
    	JTextField usertf = new JTextField();
    	JTextField porttf = new JTextField();
    	JTextField roomtf = new JTextField();
	JPasswordField passwordtf = new JPasswordField();
	JComboBox urlcb = new JComboBox();
	IRCConnection connection = null;
	ReceiveChatInterface c;

	public void update(Observable o, Object arg) {
		if (arg instanceof MessageCommand) {
			MessageCommand mc = (MessageCommand)arg;
			String message = mc.getMessage();
			String nick = mc.getSource().toString();
			int lb = message.indexOf("{");
			int le = message.indexOf("}");
			if (lb > 0 || le == -1 || lb > le) {
				Message m = new Message("Goon", nick, message.substring(le+1), "en");
				m.translate(c.getLanguage());
				c.receive(nick, m.message);
			} else {
				Message m = new Message("Goon", nick, message.substring(le+1), message.substring(lb+1, le));
				m.translate(c.getLanguage());
				c.receive(nick, m.message);
			}
			// c.receive(mc.getSource().toString(), mc.getMessage());
		} else if (arg instanceof JoinCommand) {
			JoinCommand jc = (JoinCommand)arg;
			c.receiveJoin("irc:", jc.getChannel(), jc.getUser().getNick());
		} else if (arg instanceof NamesReply) {
			f00f.net.irc.martyr.clientstate.Channel ch = getChannel(roomtf.getText());
			Enumeration e = ch.getMembers();
			while (e.hasMoreElements()) {
				Member m = (Member)e.nextElement();
				c.receivePresence("irc:", roomtf.getText(), m.getNick().getNick());
			}
		} else if (arg instanceof ActionCtcp) {
			ActionCtcp ac = (ActionCtcp)arg;
			c.receiveAction(ac.getSource().toString(), ac.getMessage());
		} else if (arg instanceof NickCommand) {
			NickCommand nc = (NickCommand)arg;
			c.receiveNick("irc:", nc.getOldNick(), nc.getNick());
		} else if (arg instanceof PartCommand) {
			PartCommand pc = (PartCommand)arg;
			c.receiveLeave(pc.getChannel(), pc.getUser().getNick());
		} else if (arg instanceof QuitCommand) {
			QuitCommand qc = (QuitCommand)arg;
			c.receiveQuit(qc.getUser().getNick());
		}
	}
	public void actionPerformed(ActionEvent ae) {
		try {
			if (ae.getActionCommand().equals(CONNECT_BUTTON)) {
				connection = new IRCConnection( this );
				connection.addCommandObserver(this);
				String nick = usertf.getText();
				AutoRegister autoReg = new AutoRegister(connection, nick, nick, nick);
				AutoReconnect autoRecon = new AutoReconnect( connection );
				AutoResponder autoRes = new AutoResponder( connection );
				autoRecon.go(hosttf.getText(),Integer.parseInt(porttf.getText()));
				AutoJoin join = new AutoJoin(connection, roomtf.getText());
				jf.setVisible(false);
				String pass = passwordtf.getText();
				if (pass != null && !pass.trim().equals("")) {
					RawCommand rc = new RawCommand("PASS", pass);
					connection.sendCommand(rc);
				}
			} else {
				parseURL(urlcb.getSelectedItem().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void msgSend(String line) {
		msgSend(line, roomtf.getText());
	}
	public void msgSend(String line, String rec) {
		if (line.startsWith("/")) {
			int space = line.indexOf(" ");
			if (line.substring(1,5).equalsIgnoreCase("JOIN") && space > 0) {
				String room = line.substring(space+1);
				JoinCommand jc = new JoinCommand(room);
				connection.sendCommand(jc);
				roomtf.setText(room);
			} else if (line.substring(1,5).equalsIgnoreCase("PART")) {
				if (space > 0) {
					PartCommand pc = new PartCommand(line.substring(space+1));
					connection.sendCommand(pc);
				} else {
					PartCommand pc = new PartCommand(roomtf.getText());
					connection.sendCommand(pc);
				}
			} else if (line.substring(1,5).equalsIgnoreCase("QUIT")) {
				QuitCommand qc = new QuitCommand("Get LirCom, the cool chat");
				connection.sendCommand(qc);
			} else if (line.substring(1,5).equalsIgnoreCase("LIST")) {
			} else if (line.substring(1,6).equalsIgnoreCase("TOPIC")) {
				TopicCommand tc = new TopicCommand(roomtf.getText(), line.substring(space+1));
				connection.sendCommand(tc);
			} else if (line.substring(1,5).equalsIgnoreCase("NICK")) {
				String newnick = line.substring(space+1).trim();
				NickCommand nc = new NickCommand(newnick);
				usertf.setText(newnick);
				connection.sendCommand(nc);
			} else if (line.substring(1,3).equalsIgnoreCase("ME")) {
				ActionCtcp ac = new ActionCtcp(rec, line.substring(3));
				connection.sendCommand(ac);
			} else {
				RawCommand rc = new RawCommand(line.substring(1));
				connection.sendCommand(rc);
			}
		} else {
			if (!c.getLanguage().equals("__") && !c.getLanguage().equals("en")) {
				line = "{"+c.getLanguage()+"}"+line;
			}
			MessageCommand mc = new MessageCommand(rec, line);
			if (connection != null) {
				connection.sendCommand(mc);
			}
		}
	}
	public void parseURL(String url) {
		String host = "irc.afternet.org";
		String port = "6667";
		String room = "#fragileminds";

		int ds = url.indexOf("//");
		int ss = url.indexOf("/", ds+2);
		int col = url.indexOf(":", ds+2);
		if (ss < 0) {
			ss = url.length();
		} else {
			room = url.substring(ss+1);
			if (!room.startsWith("#")) {
				room = "#"+room;
			}
		}
		if (ds >= 0) {
			if (col >= 0) {
				port = url.substring(col+1, ss);
				host = url.substring(ds+2, col);
			} else {
				host = url.substring(ds+2, ss);
			}
		}
		hosttf.setText(host);
		porttf.setText(port);
		roomtf.setText(room);
	}
	public IRCBridge(ReceiveChatInterface c, String nick, String url) {
		this.c = c;
		try {
			c.setSendCommandInterface(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

	    	JButton jb = new JButton(CONNECT_BUTTON);
		jb.addActionListener(this);
		jp.add(jb);

		parseURL(url);

		jp.add(urlcb);
		urlcb.addItem(url);
		urlcb.addItem("irc://irc.afternet.org:6667/fragileminds");
		urlcb.addItem("irc://eu.undernet.org:6667/schizophrenia");
		urlcb.addItem("irc://64.62.233.170:7777/GenDiscussions");
		urlcb.addActionListener(this);

		jp.add(new JLabel("IRC Server"));
		jp.add(hosttf);

		jp.add(new JLabel("Port"));
		jp.add(porttf);

		jp.add(new JLabel("Room"));
		jp.add(roomtf);

		jp.add(new JLabel("User"));
		usertf.setText(nick);
		jp.add(usertf);

		jp.add(new JLabel("Password"));
		jp.add(passwordtf);

		jf.getContentPane().add(jp);
		jf.pack();
		jf.setVisible(true);
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				jf.setVisible(false);
			}
		});
    }
	public String getUser() {
		return usertf.getText();
	}
}
