package lircom {
import f00f.net.irc.martyr.*;
import f00f.net.irc.martyr.services.*;
import f00f.net.irc.martyr.clientstate.*;
import f00f.net.irc.martyr.clientstate.Channel;
import f00f.net.irc.martyr.commands.*;
import java.util.*;
import java.io.*;

class MyAutoJoin extends AutoJoin {
	var channel:String= "";
	public function MyAutoJoin(connection:IRCConnection, channel:String) {
		super(connection, channel);
		this.channel = channel;
	}
	public function getConnection():IRCConnection {
		return super.getConnection();
	}
	public function getChannel():Channel {
		return getConnection().getClientState().getChannel(channel);
	}
	public function getServer():String {
		return getConnection().getClientState().getServer();
	}
}

public class IRCClient extends ClientState implements Observer {
	private static List<MyAutoJoin> channels = new ArrayList<MyAutoJoin>();
	private var connection:IRCConnection;
	public static function main(String args[]):void {
		for (var a:int= 0; a < args.length; a++) {
			try {
				var clientState:IRCClient= new IRCClient(args[a]);
			} catch (var e:Exception) {
				e.printStackTrace();
			}
		}
	}
	public function IRCClient(file:String) throws IOException {
		var currentConnectionInfo:Map= new HashMap(6);
		var br:BufferedReader= new BufferedReader(new FileReader(new File(file)));
		var line:String= null;
		while ((line = br.readLine()) != null) {
			var colon:int= line.indexOf(":");
			if (colon >= 0) {
				var key:String= line.substring(0, colon);
				var value:String= line.substring(colon+1);
				currentConnectionInfo.put(key, value);
			}
			if (line.startsWith("/connect")) {
				connection = new IRCConnection( this );
				connect(connection, currentConnectionInfo);
			}
			if (line.startsWith("/join")) {
				var join:MyAutoJoin= new MyAutoJoin(connection, line.substring(line.indexOf(" ")+1));
				channels.add(join);
			}
		}
	}
	public function connect(connection:IRCConnection, info:Map):void {
		var server:String= String(info.get("Server"));
		var port:int= Integer.parseInt(String(info.get("Port")));
	 	var nick:String= String(info.get("Nickname"));
		var user:String= String(info.get("User"));
		var name:String= String(info.get("Name"));
		var pass:String= String(info.get("Password"));
		connection.addCommandObserver(this);
		var autoReg:AutoRegister= new AutoRegister(connection, nick, user, name);
		var autoRecon:AutoReconnect= new AutoReconnect( connection );
		var autoRes:AutoResponder= new AutoResponder( connection );
		autoRecon.go( server, port );
		if (pass != null && !pass.trim().equals("")) {
                	var rc:RawCommand= new RawCommand("PASS", pass);
                        connection.sendCommand(rc);
			var mc:MessageCommand= new MessageCommand("NickServ", "identify "+pass);
			connection.sendCommand(mc);
                }
	}
	public function update(o:Observable, arg:Object):void {
		System.err.println(arg.getClass().getName());
		if (arg instanceof MessageCommand) {
			var mc:MessageCommand= MessageCommand(arg);
			if (mc.getMessage().equals("who")) {
				Iterator<MyAutoJoin> i = channels.iterator();
				while (i.hasNext()) {
					var r:MyAutoJoin= i.next();
					var ch:f00f.net.irc.martyr.clientstate.Channel= r.getChannel();
					if (ch != null) {
						var e:Enumeration= ch.getMembers();
						var memberList:String= "";
						while (e.hasMoreElements()) {
							var m:Member= Member(e.nextElement());
							memberList += m.getNick().getNick() + " ";
						}
						var mc2:MessageCommand= new MessageCommand(mc.getSource(), r.getServer()+"/"+r.getChannel().getName()+": "+memberList);
						connection.sendCommand(mc2);
					} else {
						// not a channel
					}
				}
			} else {
				var line:String= "<"+mc.getSource().toString()+">"+mc.getMessage();
				System.err.println("received "+line);
				// if networks are linked twice, ignore message
				if (line.indexOf("><") >= 0) {
					return;
				}
				send(line, mc.getDest());
			}
		} else if (arg instanceof JoinCommand) {
			var jc:JoinCommand= JoinCommand(arg);
			var line:String= jc.getUser().getNick()+" has joined "+jc.getChannel();
			send(line, jc.getChannel());
		} else if (arg instanceof PartCommand) {
			var pc:PartCommand= PartCommand(arg);
			var line:String= pc.getUser().getNick()+" has left "+pc.getChannel();
			send(line, pc.getChannel());
		} else if (arg instanceof NickCommand) {
			var nc:NickCommand= NickCommand(arg);
			var line:String= nc.getOldNick()+" has changed names to "+nc.getNick();
			send(line);
		} else if (arg instanceof QuitCommand) {
			var qc:QuitCommand= QuitCommand(arg);
			var line:String= qc.getUser()+" has quit ("+qc.getReason()+")";
			send(line);
		}
	}
	public function send(line:String, skipChannel:String):void {
		Iterator<MyAutoJoin> i = channels.iterator();
		while (i.hasNext()) {
			var r:MyAutoJoin= i.next();
			if (r.getConnection() != connection || !r.getChannel().getName().equalsIgnoreCase(skipChannel)) {
				var mc2:MessageCommand= new MessageCommand(r.getChannel().getName(), line);
				r.getConnection().sendCommand(mc2);
			}
		}
	}
	public function send(line:String):void {
		Iterator<MyAutoJoin> i = channels.iterator();
		while (i.hasNext()) {
			var r:MyAutoJoin= i.next();
			if (r.getConnection() != connection) {
				var mc2:MessageCommand= new MessageCommand(r.getChannel().getName(), line);
				r.getConnection().sendCommand(mc2);
			}
		}
	}
}
}