package lircom;

import java.io.*;
import java.util.*;
import java.net.*;

class NickThread extends Thread {
	String nick1 = "";
	String nick2 = "";
	String ip2 = "";
	String action = "";
	String action2 = "";
	Trade t = null;
	public NickThread(Trade t, String nick1, String nick2, String ip2, String action, String action2) {
		this.t = t;
		this.nick1 = nick1;
		this.nick2 = nick2;
		this.ip2 = ip2;
		this.action = action;
		this.action2 = action2;
	}
	public void run() {
		try {
			String nnip = "nick1="+nick1+"&nick2="+nick2+"&ip1="+InetAddress.getLocalHost().getHostName()+"&ip2="+ip2;
			StringBuffer sb = new StringBuffer();
			t.startTrade(nnip, sb); // trade.php
			// sb should have a list of your items

			// sb should contain the list of your items you want to trade
			t.makeOffer(nnip, action, sb);  // offer.php
			if (t.getOffer(nnip, sb)) { // gettradeitems.php
				// sb contains the list of items the other party wants to trade
				t.acceptOffer(nnip, action2); // accept.php
				t.transferGoods(nnip); // exchange.php
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

public class Trade extends Vector<Object> {
	String url = "";
	// String ipaddress = "69.107.89.126";
	String ipaddress = "127.0.0.1";
	public static void main(String args[]) throws Exception {
		Trade t = new Trade("http://localhost/chat/");
		// Trade t = new Trade("http://adsl-69-107-89-126.dsl.pltn13.pacbell.net/icbm/");

		// Trade t = new Trade("http://localhost/icbm/");
		NickThread yt = new NickThread(t, "yottzumm", "HiveChild", t.ipaddress+"YOTTZUMM", "OFFER", "ACCEPT");
		yt.start();
		NickThread hct = new NickThread(t, "HiveChild", "yottzumm", t.ipaddress+"YOTTZUMM", "OFFER", "ACCEPT");
		hct.start();
	}
	public Trade(String url) {
		this.url = url;
	}
	public void add(HTMLJCheckBox jcb) {
		super.add((Object)jcb);
	}
	public void startTrade(String nnip, StringBuffer sb) throws Exception {
		InputStream is = new URL(url+"trade.php?"+nnip).openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		is.close();
		br.close();
	}
	public void inventory(String nnip, StringBuffer sb) throws Exception {
		InputStream is = new URL(url+"inventory.php?"+nnip).openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		is.close();
		br.close();
	}
	public void makeOffer(String nnip, String action) throws Exception {
		Iterator i = iterator();
		StringBuffer sb = new StringBuffer();
		while (i.hasNext()) {
			HTMLJCheckBox jcb = (HTMLJCheckBox)i.next();
			if (jcb.isSelected()) {
				sb.append(jcb.item);
				System.err.println("Trading "+jcb.item);
			}
		}
		makeOffer(nnip, action, sb);
	}
	public void makeOffer(String nnip, String action, StringBuffer sb) throws Exception {
		InputStream is = new URL(url+"offer.php?"+nnip+"&action="+action+"&items="+URLEncoder.encode(sb.toString(), "UTF-8")).openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.equals("REJECT")) {
				throw new Exception("REJECT");
			}
		}
		is.close();
		br.close();
	}
	public boolean getOffer(String nnip, StringBuffer sb) throws Exception {
		int o = 0;  // number of offers
		int r = 0; // number of rejects
		long starttime = System.currentTimeMillis();
		long timeout = 60000; // 60 second time out
		long currtime = System.currentTimeMillis();
		do {
			sb.delete(0, sb.length());
			InputStream is = new URL(url+"gettradeitems.php?"+nnip).openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String offer = br.readLine(); // # offers
			System.err.println(offer);
			if (offer != null) {
				try {
					o = Integer.parseInt(offer);
				} catch (Exception ae) {
					ae.printStackTrace();
				}
			}
			String reject = br.readLine(); // # rejects
			System.err.println(reject);
			if (reject != null) {
				try {
					r = Integer.parseInt(reject);
				} catch (Exception ae) {
					ae.printStackTrace();
				}
			}
			String line = null;
			while ((line = br.readLine()) != null) {
				System.err.println(line);
				sb.append(line);
				sb.append("\n");
			}
			is.close();
			br.close();
			currtime = System.currentTimeMillis();
			if (o < 2 && r == 0) {  // wait for a few seconds
				Thread.sleep(3000);
			}
		} while (o < 2 && r == 0 && currtime - starttime < timeout);
		if (r > 0) {
			throw new Exception("REJECT");
		}
		return o >= 2;
	}
	public void acceptOffer(String nnip, String action2) throws Exception {
		InputStream is = new URL(url+"accept.php?"+nnip+"&action="+action2).openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.equals("REJECT")) {
				throw new Exception("REJECT");
			}
		}
		is.close();
		br.close();
	}
	public void transferGoods(String nnip) throws Exception {
		int a = 0;  // number of accepts
		int r = 0; // number of rejects
		long starttime = System.currentTimeMillis();
		long timeout = 60000; // 60 second time out
		long currtime = System.currentTimeMillis();
		do {
			InputStream is = new URL(url+"exchange.php?"+nnip).openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String accept = br.readLine(); // # offers
			System.err.println(accept);
			if (accept != null) {
				try {
					a = Integer.parseInt(accept);
				} catch (Exception ae) {
					ae.printStackTrace();
				}
			}
			String reject = br.readLine(); // # rejects
			System.err.println(reject);
			if (reject != null) {
				try {
					r = Integer.parseInt(reject);
				} catch (Exception ae) {
					ae.printStackTrace();
				}
			}
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			is.close();
			br.close();
		} while (a + r < 2 && currtime - starttime < timeout);
	}
}
