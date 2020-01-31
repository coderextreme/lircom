package lircom;

import f00f.net.irc.martyr.*;
import f00f.net.irc.martyr.services.*;
import f00f.net.irc.martyr.clientstate.*;
import f00f.net.irc.martyr.clientstate.Channel;
import f00f.net.irc.martyr.commands.*;
import f00f.net.irc.martyr.replies.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.lang.reflect.Method;

class MyAutoJoin2 extends AutoJoin {
	String channel = "";
	public MyAutoJoin2(IRCConnection connection, String channel) {
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

public class IRCScrape extends ClientState implements Observer {
	private static List<MyAutoJoin2> channels = new ArrayList<MyAutoJoin2>();
	private IRCConnection connection;
	FileWriter hosts;
	public static void main(String args[]) {
		for (int a = 0; a < args.length; a++) {
			try {
				IRCScrape clientState = new IRCScrape(args[a]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public IRCScrape(String file) throws IOException {
		hosts = new FileWriter("hosts.txt", true);
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
				MyAutoJoin2 join = new MyAutoJoin2(connection, line.substring(line.indexOf(" ")+1));
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
		// System.err.println(arg.getClass().getName());
		if (arg instanceof MessageCommand) {
			MessageCommand mc = (MessageCommand)arg;
			try {
				String host = mc.getSource().getHost();
				host = host.substring(host.indexOf("."))+"\n";
				hosts.write(host, 0, host.length());
				hosts.flush();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
}
