package impact;

import javax.sound.sampled.*;
import java.net.*;
import java.io.*;

public class Sender extends Thread {
	String url;
	Receiver receiver;
	boolean sending = true;
	public Sender(String url, Receiver r) {
		this.receiver = r;
		if (url == null) {
			this.url = "http://localhost/cgi-bin/send.cgi";
		} else {
			this.url = url;
		}
		// System.err.println("Sender URL is "+this.url);
	}
	static public void main(String args[]) throws Exception {
		new Sender(args[0], null).start();
	}
	public synchronized void send(String line) throws Exception {
		if (line.length() > 0) {
			URL u = new URL(url+"&text="+URLEncoder.encode(line, "UTF-8"));
			System.err.println(u.toString());
			receive(u);
		}
	}
	public void close() throws Exception {
		sending = false;
		if (receiver != null) {
			receiver.stopReceiving();
		}
		InputStream is = new URL(url+"&remove=true").openStream();
		is.close();
	}
	public void receive (URL u) throws Exception {
		InputStream is = u.openStream();
		String line;
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		while((line = br.readLine()) != null) {
			if (line.length() > 0) {
				System.out.println("Sending in Sender "+line);
				Proxy.getProxy().receive("", line);
			}
		}
		is.close();
	}
	public void run() {
		try {
			while (sending) {
				URL u = new URL(url);
				receive(u);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
