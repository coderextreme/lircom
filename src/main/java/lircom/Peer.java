package lircom;

import java.net.*;

public class Peer extends Thread {
	private int port = 0;
	private ServerSocket ss;
	static public void main(String args[]) throws Exception {
		lircom.Message.thisApplication = null;
		if (args.length != 1) {
			System.err.println("Usage: java Peer localport");
		}
		Peer s = new Peer();
		s.setPort(Integer.parseInt(args[0]));
		s.start();
	}
	public Peer() throws Exception {
	}
	public void setPort(int port) throws Exception {
		this.port = port;
		ss = new ServerSocket(port);
	}
	public void run() {
		try {
			for (;;) {
				Socket client = ss.accept();
				ClientOnServer c = new ClientOnServer(client);
				System.err.println("new client is "+c.clientno);
                                c.setNick("COS-"+c.clientno);
                                c.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
