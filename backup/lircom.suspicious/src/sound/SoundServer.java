package lircom;

import java.net.*;
import java.io.*;


public class SoundServer extends Thread {
	private ServerSocket ss;
	static public void main(String args[]) throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: java SoundServer localport");
		}
		SoundServer s = new SoundServer(Integer.parseInt(args[0]));
		s.start();
	}
	public SoundServer(int port) throws Exception {
		ss = new ServerSocket(port);
	}
	public void run() {
		try {
			for (;;) {
				Socket client = ss.accept();
				InputStream is = client.getInputStream();
				OutputStream os = client.getOutputStream();
				// new SoundSender(os).start();
				new SoundReceiver(is).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
