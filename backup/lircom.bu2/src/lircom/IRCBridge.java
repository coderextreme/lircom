package lircom;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


class IRCSendChat extends Thread implements SendChatInterface {
	InputStream is = null;
	OutputStream sos = null;
	PrintStream sps = null;
	String ircRoom1 = "#yottzumm1";
	String ircNick1 = "Unknown";
	boolean sent = false;
	ReceiveChatInterface chat;
	Vector people;
	Party me;
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
		println("USER "+username+" 0 * :"+username);
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
		println("WHO "+room);
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
						chat.receive(nick, line.substring(sl+1));
						
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

public class IRCBridge implements ActionListener, SendCommandInterface {
	JFrame jf = new JFrame("IRC Connection Info");
    	JTextField hosttf = new JTextField();
    	JTextField usertf = new JTextField();
    	JTextField porttf = new JTextField();
    	JTextField roomtf = new JTextField();
	SendChatInterface sendchat;
	ReceiveChatInterface c;

	public void actionPerformed(ActionEvent ae) {
		try {
			// Identd idd = new Identd();
			// idd.start();

			Socket s1 = new Socket(hosttf.getText(),Integer.parseInt(porttf.getText()));
			InputStream is1 = s1.getInputStream();
			OutputStream os1 = s1.getOutputStream();

			sendchat = new IRCSendChat(is1, os1, roomtf.getText(), usertf.getText(), c);
			((Thread)sendchat).start();
			c.setSendCommandInterface(this);
			jf.setVisible(false);
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
				sendchat.sendJoin(room);
				roomtf.setText(room);
			} else if (line.substring(1,5).equalsIgnoreCase("PART")) {
				if (space > 0) {
					sendchat.sendLeave(line.substring(space+1));
				} else {
					sendchat.sendLeave(roomtf.getText());
				}
			} else if (line.substring(1,5).equalsIgnoreCase("QUIT")) {
				sendchat.sendQuit();
			} else if (line.substring(1,5).equalsIgnoreCase("LIST")) {
				sendchat.requestRooms();
			} else if (line.substring(1,5).equalsIgnoreCase("NICK")) {
				String newnick =line.substring(space+1).trim();
				sendchat.nickname(newnick);
				usertf.setText(newnick);
			} else if (line.substring(1,3).equalsIgnoreCase("ME")) {
				sendchat.sendAction(rec, line.substring(3));
			} else {
				c.receive(hosttf.getText(), "Unknown Command");
			}
		} else {
			sendchat.send(rec, line);
		}
	}
	public IRCBridge(ReceiveChatInterface c, String nick) {
		this.c = c;
		JPanel jp = new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

		jp.add(new JLabel("IRC Server"));
		hosttf.setText("irc.schizophrenics.net");
		jp.add(hosttf);

		jp.add(new JLabel("Port"));
		porttf.setText("6667");
		jp.add(porttf);

		jp.add(new JLabel("Room"));
		roomtf.setText("#gendiscussions");
		jp.add(roomtf);

		jp.add(new JLabel("User"));
		usertf.setText(nick);
		jp.add(usertf);

	    	JButton jb = new JButton("Connect to IRC");
		jb.addActionListener(this);
		jp.add(jb);

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
