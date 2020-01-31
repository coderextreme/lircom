package impact;

import javax.sound.sampled.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class Receiver extends Thread {
	String url;
	boolean stop = false;
	public Receiver(String url) throws Exception {
		if (url == null) {
			this.url = "http://localhost/cgi-bin/receive.cgi";
		} else {
			this.url = url;
		}
		// System.err.println("Receiver URL is "+this.url);
	}
	static public void main(String args[]) throws Exception {
		OutLineHandler lh = new OutLineHandler();
		Receiver r = new Receiver(args[0]);
		r.start();
	}
	public void stopReceiving() {
		stop = true;
	}
	public void run() {
		try {
			URL u = new URL(url);
			while (!stop) {
				InputStream is = u.openStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String line = null;
				while ((line = br.readLine()) != null) {
					System.out.print(line);
					int gt = line.lastIndexOf(">");
					if (gt >= 0) {
						line = line.substring(gt+1);
						if (line.length() > 0) {
							// System.err.println("receiving "+line);
							Proxy.getProxy().receive(line);
						}
					}
				}
				Thread.sleep(1000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
