package lircom;

import java.io.*;
import java.net.*;

class ClientImpl extends ClientOnServer {
	public ClientImpl(InputStream is, OutputStream os) throws Exception {
		super(is, os);
	}
	public boolean processLine(String line) throws Exception {
		Message m = Message.parse(line);
		return true;
	}
}

public class Client extends Peer {
	public Client() throws Exception {
	}
	static public void main(String args[]) throws Exception {

		if (args.length < 3) {
			System.err.println("Usage: java Client localport remotehost remoteport");
			System.exit(1);
		}
		ClientImpl stdio = new ClientImpl(System.in, System.out);
		System.err.println("Your client is "+stdio.clientno);
		Socket s = new Socket(args[1], Integer.parseInt(args[2]));
		ClientOnServer client = new ClientOnServer(s);
                client.start();
	}
}
