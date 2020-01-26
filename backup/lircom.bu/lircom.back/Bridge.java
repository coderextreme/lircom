package lircom;

import java.io.*;
import java.util.*;
import java.net.*;


class Splitter extends Thread {
	InputStream is = null;
	OutputStream sos = null;
	String ircRoom1;
	String ircNick1;
	String ircUser1;
	String ircPass1;
	int lircomPort;
	boolean sent = false;
	Bridge bridge = null;
	public Splitter(InputStream is, OutputStream sos, String ircRoom1, String ircUser1, String ircNick1, String ircPass1, int lircomPort, Bridge bridge) {
		this.is = is;
		this.sos = sos;
		this.ircRoom1 = ircRoom1;
		this.ircUser1 = ircUser1;
		this.ircNick1 = ircNick1;
		this.ircPass1 = ircPass1;
		this.lircomPort = lircomPort;
		this.bridge = bridge;
	}
	public void setSent(boolean s) {
		sent = s;
	}
	public boolean getSent() {
		return sent;
	}
	class Delay extends Thread {
		PrintStream sps;
		String ircRoom1;
		Splitter s;
		public Delay(PrintStream sps, String ircRoom1, Splitter s) {
			this.sps = sps;
			this.ircRoom1 = ircRoom1;
			this.s = s;
		}
		public void run() {
			try {
				sleep(10000);
				if (!s.getSent()) {
					println(sps, "JOIN "+ircRoom1);
					println(sps, "MODE "+ircRoom1);
					println(sps, "WHO "+ircRoom1);
					s.setSent(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	class PingPongOut extends Thread {
		PrintStream sps = null;
		public PingPongOut(PrintStream sps) {
			this.sps = sps;
		}
		public void run() {
			try {
				while (true) {
					sleep(30000);
                                        println(sps, "PING LAG"+System.currentTimeMillis());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public void run() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			PrintStream sps = new PrintStream(sos);
                        if (ircPass1 != null && !ircPass1.trim().equals("")) {
                            println(sps, "PASS "+ircPass1);
                        }
			println(sps, "NICK "+ircNick1);
			println(sps, "USER "+ircUser1+" 0 * :"+(lircomPort == -1? "NotAvailable" : ""+lircomPort));
			new Delay(sps, ircRoom1, this).start();
			sps.flush();
			new PingPongOut(sps).start();
			String line = null;
			while ((line = br.readLine()) != null) {	
				System.err.println("from Server "+line);
				int i = 0;
				if ((i = line.indexOf("PING")) == 0) {
                                        System.err.println("Got A ping!");
                                    	println(sps, "PONG"+line.substring(i+4));
					// new Delay(sps, ircRoom1, this).start();
				} else if ((i = line.indexOf("451")) > 0) {
                                        if (ircPass1 != null && !ircPass1.trim().equals("")) {
                                            println(sps, "PASS "+ircPass1);
                                        }
					println(sps, "NICK "+ircNick1);
					println(sps, "USER "+ircUser1+" 0 * :"+(lircomPort == -1? "NotAvailable" : ""+lircomPort));
				} else if ((i = line.indexOf("352")) > 0) {
					StringTokenizer st = new StringTokenizer(line, " ");
					String server = st.nextToken();
					String code = st.nextToken();
					String yournick = st.nextToken();
					String channel = st.nextToken();
					String identid = st.nextToken();
					String theirhost = st.nextToken();
					String theirserver = st.nextToken();
					String theirnick = st.nextToken();
					String attributes = st.nextToken();
					String unknown = st.nextToken();
					String port = st.nextToken();
					PossibleConnection.add(new PossibleConnection(theirhost, port, theirnick, ""+new Date()));
				}
			}
			System.err.println("Closing");
			sps.close();
			is.close();
			sos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void println(PrintStream sps, String str) {
		System.err.println("to SERVER "+str);
		sps.println(str);
	}
}

public class Bridge extends Thread {

	Socket s1 = null;
	InputStream is1 = null;
	OutputStream os1 = null;
        String ircRoom1 =  "#schizophrenia";

	public static void main (String args[]) throws Exception {
		String ircServer1 = "irc.avalonworks.ca";
		int ircPort1 = 6667;
                String ircRoom1 = "#schizophrenia";
                String ircUser1 = "networkbridge";
                String ircNick1 = "netbrdge1";
                String ircPass1 = "tofuburgers";
                int lircomPort1 = 5891;
		Bridge b = new Bridge(ircServer1, ircPort1, ircRoom1, ircUser1, ircNick1, ircPass1, lircomPort1);
		b.start();
	}
	public Bridge(String ircServer1, int ircPort1, String ircRoom1, String ircUser1, String ircNick1, String ircPass1, int lircomPort1) throws Exception {
		Identd idd = new Identd();
		idd.start();

		s1 = new Socket(ircServer1,ircPort1);
		is1 = s1.getInputStream();
		os1 = s1.getOutputStream();
                this.ircRoom1 = ircRoom1;

		new Splitter(is1, os1, ircRoom1, ircUser1, ircNick1, ircPass1, lircomPort1, this).start();
	}
	public void run() {
		try {
			PrintStream sps = new PrintStream(os1);
			for (;;) {
				sps.println("WHO "+ircRoom1+"\n");
				PossibleConnection.pcons.clear();
				Thread.sleep(30000);
				sps.flush();
				Iterator i = PossibleConnection.iterator();
				while (i.hasNext()) {
					PossibleConnection pcon = (PossibleConnection)i.next();
					System.err.println("****NICK "+pcon.nick);
					System.err.println("****HOST "+pcon.host);
					System.err.println("****PORT "+pcon.port);
                                        System.err.println("****DATE "+pcon.date);
				}
			}
		} catch (Exception e) {
		}
	}
}
