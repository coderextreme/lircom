package lircom;

import io.socket.client.*;
import io.socket.emitter.*;
import java.util.Random;
import java.net.URISyntaxException;
import org.json.*;

public abstract class SocketIO extends ClientInterface {
	private Socket socket;
	public void send(Message msg) {
		socket.emit("clientmessage", msg.generate());
	}
	public void send(JSONObject obj) {
		socket.emit("clientcommand", obj);
	}
	public synchronized void start() {
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			@Override
			public void call(Object ... args) {
				System.out.println("Connected!");
				socket.emit("clientjoin", "hi");
				// socket.disconnect();
			}
		}).on("servercommand", new Emitter.Listener() {
			@Override
			public void call(Object ... args) {
				System.err.println("---------------------------------------------------------------");
				try {
					if (args[0] instanceof JSONObject) {
						Message m = new Message(getNick(), "Server", "", "en");
						m.from = "Server";
						m.json = (JSONObject)args[0];
						processObj(m.generate());
					} else {
						System.err.println("servercommand sent a non-JSON object");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).on("servermessage", new Emitter.Listener() {
			@Override
			public void call(Object ... args) {
				try {
					// fake out the nick and from
					Message m = new Message(getNick(), "Server", (String)args[0], "en");
					m.from = "Server";
					processObj(m.generate());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
			@Override
			public void call(Object ... args) {
				System.out.println("Disconnected!");
			}
		});
		socket.connect();
	}
	public SocketIO(String host, int port, String nick) throws URISyntaxException {
		Random r = new Random();
		socket = IO.socket("http://"+host+":"+port);
		try {
			setNick(nick); // if this doen't happen, no messages are processed
		} catch (Exception e) {
			try {
				setNick("Solitaire"+r.nextInt(32000));
			} catch (Exception ex) {
				System.err.println("Couldn't set Nickname");
			}
		}
	}
/*
	public static void main(String args[]) throws URISyntaxException {
		SocketIO sio = new SocketIO("localhost", 8088, "yottzumm");
		sio.start();
		Message m = new Message("*", sio.getNick(), "has entered", "en");
		sio.send(m);
	}
*/
}
