package lircom;

import java.net.*;

public class Peer extends Thread {
	private int port = 0;
	private ServerSocket ss;
	static public void main(String args[]) throws Exception {
		lircom.Message.thisApplication = null;
		if (args.length <= 0) {
			System.err.println("Usage: java Peer localPort ... ");
			Peer s = new Peer();
		} else {
			for (int i = 0; i < args.length; i++) {
				Peer s = new Peer(Integer.parseInt(args[i]));
			}
		}
	}
	public Peer() throws Exception {
		this(8180);
	}
	public Peer(int port) throws Exception {
		setPort(port);
		start();
		System.out.println("localhost:"+port);
		System.out.flush();
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
