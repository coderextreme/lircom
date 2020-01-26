package lircom {
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
	var is:InputStream= null;
	var sos:OutputStream= null;
	var sps:PrintStream= null;
	var ircRoom1:String= "#yottzumm1";
	var ircNick1:String= "Unknown";
	var sent:Boolean= false;
	var chat:ReceiveChatInterface;
	var people:Vector;
	public function IRCSendChat(is:InputStream, sos:OutputStream, ircRoom1:String, ircNick1:String, chat:ReceiveChatInterface) {
		this.is = is;
		this.sos = sos;
		sps = new PrintStream(sos);
		this.ircRoom1 = ircRoom1;
		this.ircNick1 = ircNick1;
		this.chat = chat;
	}
	public function setSent(s:Boolean):void {
		sent = s;
	}
	public function getSent():Boolean {
		return sent;
	}
	class IRCDelay extends Thread {
		var ircRoom1:String;
		var s:IRCSendChat;
		public function IRCDelay(ircRoom1:String, s:IRCSendChat) {
			this.ircRoom1 = ircRoom1;
			this.s = s;
		}
		public function run():void {
			try {
				sleep(10000);
				if (!s.getSent()) {
					sendJoin(ircRoom1);
					println("MODE "+ircRoom1);
					s.setSent(true);
				}
			} catch (var e:Exception) {
				e.printStackTrace();
			}
		}
	}
	class IRCPingPongOut extends Thread {
		public function IRCPingPongOut() {
		}
		public function run():void {
			try {
				while (true) {
					requestPeople(ircRoom1);
					sendPing();
					sleep(29000);
				}
			} catch (var e:Exception) {
				e.printStackTrace();
			}
		}
	}
	public function sendToChat(nick:String, act:String, message:String):void {
	}



	// send interface
	public function username(username:String):void {
		println("USER "+username+" "+username+" afternet.org :"+username);
	}
	public function nickname(nick:String):void {
		println("NICK "+nick);
		ircNick1 = nick;
	}
	public function password(password:String):void {
		if (password != null) {
			println("PASS "+password);
		}
	}
	public function sendJoin(room:String):void {
		println("JOIN "+room);
		ircRoom1 = room;
	}
	public function sendLeave(room:String):void {
		println("PART "+room);
	}
	public function sendQuit():void {
		println("QUIT");
	}
	public function send(to:String, message:String):void {
		println("PRIVMSG "+to+" :"+message);
	}
	public function send(to:String, file:InputStream):void {
	}
	public function sendAction(to:String, message:String):void {
		println("PRIVMSG "+to+" :\001ACTION"+message+"\001");
	}
	public function sendPing():void {
		println("PING LAG"+System.currentTimeMillis());
	}
	public function requestPeople():void {
		println("WHO *");
	}
	public function requestRooms():void {
		println("LIST");
	}
	public function requestPeople(room:String):void {
		println("WHO "+room+" %ctnf,152");
	}
	public function requestRooms(person:String):void {
		println("WHO "+person);
	}
	// end send interface

	public function run():void {
		try {
			var br:BufferedReader= new BufferedReader(new InputStreamReader(is));
			password(null);
			nickname(ircNick1);
			username(ircNick1);
			new IRCDelay(ircRoom1, this).start();
			new IRCPingPongOut().start();
			var line:String= null;
			while ((line = br.readLine()) != null) {	
				System.err.println(line);
				var i:int;
				if ((i = line.indexOf("PRIVMSG")) >= 0) {
					System.err.println("Processing PRIVMSG");
					var sl:int= line.indexOf(":", i);
					var nick:String= ircNick1;
					var ex:int= line.indexOf("!");
					if (ex >= 0) {
						nick = line.substring(1, ex);
					}
					if ((i = line.indexOf("\001")) >= 0) {
						var act:String= line.substring(i+7, line.length()-1);
						chat.receiveAction(nick, act);
					} else {
						var message:String= line.substring(sl+1);
						var lb:int= message.indexOf("{");
						var le:int= message.indexOf("}");
						if (lb > 0|| le == -1|| lb > le) {
							chat.receive(nick, message);
						} else {
							var m:Message= new Message("Goon", nick, message.substring(le+1), message.substring(lb+1, le));
							m.translate(chat.getLanguage());
							chat.receive(nick, m.message);
						}
						
					}
				} else if ((i = line.indexOf("NOTICE")) >= 0) {
					System.err.println("Processing NOTICE");
					var sl:int= line.indexOf(":", i);
					var nick:String= ircNick1;
					var ex:int= line.indexOf("!");
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
					var sl:int= line.indexOf(":", i);
					var nick:String= ircNick1;
					var ex:int= line.indexOf("!");
					if (ex >= 0) {
						nick = line.substring(1, ex);
					}
					chat.receiveJoin("irc:", line.substring(sl+1), nick);
				} else if ((i = line.indexOf("PART")) >= 0) {
					System.err.println("Processing PART");
					var sl:int= line.indexOf(":", i);
					var nick:String= ircNick1;
					var ex:int= line.indexOf("!");
					if (ex >= 0) {
						nick = line.substring(1, ex);
					}
					chat.receiveLeave(line.substring(i+4,sl).trim(), nick);
				} else if ((i = line.indexOf(" 005 ")) >= 0) {
				} else if ((i = line.indexOf("NICK")) >= 0) {
					System.err.println("Processing NICK");
					var cl:int= line.indexOf(":", i);
					var sl:int= line.indexOf(":");
					var ex:int= line.indexOf("!");
					var oldnick:String= ircNick1;
					if (sl >= 0&& ex >= 0) {
						oldnick = line.substring(sl+1, ex);
					}
					var newnick:String= line.substring(cl+1);
					chat.receiveNick("irc:", oldnick, newnick);
				} else if ((i = line.indexOf("QUIT")) >= 0) {
					System.err.println("Processing QUIT");
					var sl:int= line.indexOf(":", i);
					var nick:String= ircNick1;
					var ex:int= line.indexOf("!");
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
					var st:StringTokenizer= new StringTokenizer(line, " ");
					var host:String= st.nextToken();
					var cmd:String= st.nextToken();
					var mynick:String= st.nextToken();
					var channel:String= st.nextToken();
					var user:String= st.nextToken();
					var usermachine:String= st.nextToken();
					var servermachine:String= st.nextToken();
					var othernick:String= st.nextToken();
					chat.receivePresence("irc:", channel, othernick);
				} else if ((i = line.indexOf("322")) >= 0) {
					System.err.println(line);
					var st:StringTokenizer= new StringTokenizer(line, " ");
					var host:String= st.nextToken();
					var cmd:String= st.nextToken();
					var mynick:String= st.nextToken();
					var channel:String= st.nextToken();
					var numUsers:int= Integer.parseInt(st.nextToken());
					var topic:String= line.substring(line.indexOf(":", 1));
					chat.receiveRoom("irc:", channel, numUsers, topic);

				}
			}
			System.err.println("Closing");
			sps.close();
			is.close();
			sos.close();
		} catch (var e:Exception) {
			e.printStackTrace();
		}
	}

	public function println(str:String):void {
		System.err.println("to SERVER "+str);
		sps.println(str);
		sps.flush();
	}
}

public class IRCBridge extends ClientState implements Observer, ActionListener, SendCommandInterface {
	public static final var CONNECT_BUTTON:String= "Connect to IRC";

	var jf:JFrame= new JFrame("IRC Connection Info");
    	var hosttf:JTextField= new JTextField();
    	var usertf:JTextField= new JTextField();
    	var porttf:JTextField= new JTextField();
    	var roomtf:JTextField= new JTextField();
	var passwordtf:JPasswordField= new JPasswordField();
	var urlcb:JComboBox= new JComboBox();
	var connection:IRCConnection= null;
	var c:ReceiveChatInterface;
	var lircomwindow:MainWindow;

	public function update(o:Observable, arg:Object):void {
		if (arg instanceof MessageCommand) {
			var mc:MessageCommand= MessageCommand(arg);
			var message:String= mc.getMessage();
			var nick:String= mc.getSource().toString();
			var lb:int= message.indexOf("{");
			var le:int= message.indexOf("}");
			if (lb > 0|| le == -1|| lb > le) {
				var m:Message= new Message("Goon", nick, message.substring(le+1), "en");
				m.translate(c.getLanguage());
				c.receive(nick, m.message);
			} else {
				var m:Message= new Message("Goon", nick, message.substring(le+1), message.substring(lb+1, le));
				m.translate(c.getLanguage());
				c.receive(nick, m.message);
			}
			// c.receive(mc.getSource().toString(), mc.getMessage());
		} else if (arg instanceof JoinCommand) {
			var jc:JoinCommand= JoinCommand(arg);
			c.receiveJoin("irc:", jc.getChannel(), jc.getUser().getNick());
		} else if (arg instanceof NamesReply) {
			var ch:f00f.net.irc.martyr.clientstate.Channel= getChannel(roomtf.getText());
			var e:Enumeration= ch.getMembers();
			while (e.hasMoreElements()) {
				var m:Member= Member(e.nextElement());
				c.receivePresence("irc:", roomtf.getText(), m.getNick().getNick());
			}
		} else if (arg instanceof ActionCtcp) {
			var ac:ActionCtcp= ActionCtcp(arg);
			c.receiveAction(ac.getSource().toString(), ac.getMessage());
		} else if (arg instanceof NickCommand) {
			var nc:NickCommand= NickCommand(arg);
			c.receiveNick("irc:", nc.getOldNick(), nc.getNick());
		} else if (arg instanceof PartCommand) {
			var pc:PartCommand= PartCommand(arg);
			c.receiveLeave(pc.getChannel(), pc.getUser().getNick());
		} else if (arg instanceof QuitCommand) {
			var qc:QuitCommand= QuitCommand(arg);
			c.receiveQuit(qc.getUser().getNick());
		}
	}
	public function actionPerformed(ae:ActionEvent):void {
		try {
			if (ae.getActionCommand().equals(CONNECT_BUTTON)) {
				lircomwindow.setVisible(true);
				connection = new IRCConnection( this );
				connection.addCommandObserver(this);
				var nick:String= usertf.getText();
				var autoReg:AutoRegister= new AutoRegister(connection, nick, nick, nick);
				var autoRecon:AutoReconnect= new AutoReconnect( connection );
				var autoRes:AutoResponder= new AutoResponder( connection );
				autoRecon.go(hosttf.getText(),Integer.parseInt(porttf.getText()));
				var join:AutoJoin= new AutoJoin(connection, roomtf.getText());
				jf.setVisible(false);
				var pass:String= passwordtf.getText();
				if (pass != null && !pass.trim().equals("")) {
					var rc:RawCommand= new RawCommand("PASS", pass);
					connection.sendCommand(rc);
				}
			} else {
				parseURL(urlcb.getSelectedItem().toString());
			}
		} catch (var e:Exception) {
			e.printStackTrace();
		}
	}
	public function msgSend(line:String):void {
		msgSend(line, roomtf.getText());
	}
	public function msgSend(line:String, rec:String):void {
		if (line.startsWith("/")) {
			var space:int= line.indexOf(" ");
			if (line.substring(1,5).equalsIgnoreCase("JOIN") && space > 0) {
				var room:String= line.substring(space+1);
				var jc:JoinCommand= new JoinCommand(room);
				connection.sendCommand(jc);
				roomtf.setText(room);
			} else if (line.substring(1,5).equalsIgnoreCase("PART")) {
				if (space > 0) {
					var pc:PartCommand= new PartCommand(line.substring(space+1));
					connection.sendCommand(pc);
				} else {
					var pc:PartCommand= new PartCommand(roomtf.getText());
					connection.sendCommand(pc);
				}
			} else if (line.substring(1,5).equalsIgnoreCase("QUIT")) {
				var qc:QuitCommand= new QuitCommand("Get LirCom, the cool chat");
				connection.sendCommand(qc);
			} else if (line.substring(1,5).equalsIgnoreCase("LIST")) {
			} else if (line.substring(1,6).equalsIgnoreCase("TOPIC")) {
				var tc:TopicCommand= new TopicCommand(roomtf.getText(), line.substring(space+1));
				connection.sendCommand(tc);
			} else if (line.substring(1,5).equalsIgnoreCase("NICK")) {
				var newnick:String= line.substring(space+1).trim();
				var nc:NickCommand= new NickCommand(newnick);
				usertf.setText(newnick);
				connection.sendCommand(nc);
			} else if (line.substring(1,3).equalsIgnoreCase("ME")) {
				var ac:ActionCtcp= new ActionCtcp(rec, line.substring(3));
				connection.sendCommand(ac);
			} else {
				var rc:RawCommand= new RawCommand(line.substring(1));
				connection.sendCommand(rc);
			}
		} else {
			if (!c.getLanguage().equals("__") && !c.getLanguage().equals("en")) {
				line = "{"+c.getLanguage()+"}"+line;
			}
			var mc:MessageCommand= new MessageCommand(rec, line);
			if (connection != null) {
				connection.sendCommand(mc);
			}
		}
	}
	public function parseURL(url:String):void {
		var host:String= "irc.afternet.org";
		var port:String= "6667";
		var room:String= "#thevillage";

		var ds:int= url.indexOf("//");
		var ss:int= url.indexOf("/", ds+2);
		var col:int= url.indexOf(":", ds+2);
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
	public function IRCBridge(c:ReceiveChatInterface, nick:String, url:String, lircomwindow:MainWindow) {
		this.c = c;
		this.lircomwindow = lircomwindow;
		try {
			c.setSendCommandInterface(this);
		} catch (var e:Exception) {
			e.printStackTrace();
		}
		var jp:JPanel= new JPanel();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));

		parseURL(url);

		jp.add(urlcb);
		urlcb.addItem(url);
		urlcb.addItem("irc://irc.afternet.org:6667/thevillage");
		urlcb.addItem("irc://irc.ircstorm.net:6667/schizophrenia");
		urlcb.addItem("irc://eu.undernet.org:6667/schizophrenia");
		urlcb.addItem("irc://208.69.42.178:7777/GenDiscussions");
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

	    	var jb:JButton= new JButton(CONNECT_BUTTON);
		jb.addActionListener(this);
		jp.add(jb);


		jf.getContentPane().add(jp);
		jf.pack();
		jf.setVisible(true);
		jf.addWindowListener(new WindowAdapter() {
			public function windowClosing(e:WindowEvent):void {
				jf.setVisible(false);
			}
		});
    }
	public function getUser():String {
		return usertf.getText();
	}
}
}