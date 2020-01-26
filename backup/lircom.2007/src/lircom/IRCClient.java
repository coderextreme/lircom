package lircom;

import f00f.net.irc.martyr.*;
import f00f.net.irc.martyr.services.*;
import f00f.net.irc.martyr.clientstate.*;
import f00f.net.irc.martyr.clientstate.Channel;
import f00f.net.irc.martyr.commands.*;
import java.util.*;
import java.io.*;

class MyAutoJoin extends AutoJoin {
	String channel = "";
	public MyAutoJoin(IRCConnection connection, String channel) {
		super(connection, channel);
		this.channel = channel;
	}
	public IRCConnection getConnection() {
		return super.getConnection();
	}
	public Channel getChannel() {
		return getConnection().getClientState().getChannel(channel);
	}
	public String getServer() {
		return getConnection().getClientState().getServer();
	}
}

public class IRCClient extends ClientState implements Observer {
	private static List<MyAutoJoin> channels = new ArrayList<MyAutoJoin>();
	private IRCConnection connection;
	public static void main(String args[]) {
		for (int a = 0; a < args.length; a++) {
			try {
				IRCClient clientState = new IRCClient(args[a]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public IRCClient(String file) throws IOException {
		Map currentConnectionInfo = new HashMap(6);
		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		String line = null;
		while ((line = br.readLine()) != null) {
			int colon = line.indexOf(":");
			if (colon >= 0) {
				String key = line.substring(0, colon);
				String value = line.substring(colon+1);
				currentConnectionInfo.put(key, value);
			}
			if (line.startsWith("/connect")) {
				connection = new IRCConnection( this );
				connect(connection, currentConnectionInfo);
			}
			if (line.startsWith("/join")) {
				MyAutoJoin join = new MyAutoJoin(connection, line.substring(line.indexOf(" ")+1));
				channels.add(join);
			}
		}
	}
	public void connect(IRCConnection connection, Map info) {
		String server = (String)info.get("Server");
		int port = Integer.parseInt((String)info.get("Port"));
	 	String nick = (String)info.get("Nickname");
		String user = (String)info.get("User");
		String name = (String)info.get("Name");
		String pass = (String)info.get("Password");
		connection.addCommandObserver(this);
		AutoRegister autoReg = new AutoRegister(connection, nick, user, name);
		AutoReconnect autoRecon = new AutoReconnect( connection );
		AutoResponder autoRes = new AutoResponder( connection );
		autoRecon.go( server, port );
		if (pass != null && !pass.trim().equals("")) {
                	RawCommand rc = new RawCommand("PASS", pass);
                        connection.sendCommand(rc);
			MessageCommand mc = new MessageCommand("NickServ", "identify "+pass);
			connection.sendCommand(mc);
                }
	}
	public void update(Observable o, Object arg) {
		System.err.println(arg.getClass().getName());
		if (arg instanceof MessageCommand) {
			MessageCommand mc = (MessageCommand)arg;
			if (mc.getMessage().equals("who")) {
				Iterator<MyAutoJoin> i = channels.iterator();
				while (i.hasNext()) {
					MyAutoJoin r = i.next();
					f00f.net.irc.martyr.clientstate.Channel ch = r.getChannel();
					if (ch != null) {
						Enumeration e = ch.getMembers();
						String memberList = "";
						while (e.hasMoreElements()) {
							Member m = (Member)e.nextElement();
							memberList += m.getNick().getNick() + " ";
						}
						MessageCommand mc2 = new MessageCommand(mc.getSource(), r.getServer()+"/"+r.getChannel().getName()+": "+memberList);
						connection.sendCommand(mc2);
					} else {
						// not a channel
					}
				}
			} else {
				String line = "<"+mc.getSource().toString()+">"+mc.getMessage();
				System.err.println("received "+line);
				// if networks are linked twice, ignore message
				if (line.indexOf("><") >= 0) {
					return;
				}
				send(line, mc.getDest());
			}
		} else if (arg instanceof JoinCommand) {
			JoinCommand jc = (JoinCommand)arg;
			String line = jc.getUser().getNick()+" has joined "+jc.getChannel();
			send(line, jc.getChannel());
		} else if (arg instanceof PartCommand) {
			PartCommand pc = (PartCommand)arg;
			String line = pc.getUser().getNick()+" has left "+pc.getChannel();
			send(line, pc.getChannel());
		} else if (arg instanceof NickCommand) {
			NickCommand nc = (NickCommand)arg;
			String line = nc.getOldNick()+" has changed names to "+nc.getNick();
			send(line);
		} else if (arg instanceof QuitCommand) {
			QuitCommand qc = (QuitCommand)arg;
			String line = qc.getUser()+" has quit ("+qc.getReason()+")";
			send(line);
		}
	}
	public void send(String line, String skipChannel) {
		Iterator<MyAutoJoin> i = channels.iterator();
		while (i.hasNext()) {
			MyAutoJoin r = i.next();
			if (r.getConnection() != connection || !r.getChannel().getName().equalsIgnoreCase(skipChannel)) {
				MessageCommand mc2 = new MessageCommand(r.getChannel().getName(), line);
				r.getConnection().sendCommand(mc2);
			}
		}
	}
	public void send(String line) {
		Iterator<MyAutoJoin> i = channels.iterator();
		while (i.hasNext()) {
			MyAutoJoin r = i.next();
			if (r.getConnection() != connection) {
				MessageCommand mc2 = new MessageCommand(r.getChannel().getName(), line);
				r.getConnection().sendCommand(mc2);
			}
		}
	}
}
