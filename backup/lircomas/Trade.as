package lircom {
import java.io.*;
import java.util.*;
import java.net.*;

class NickThread extends Thread {
	var nick1:String= "";
	var nick2:String= "";
	var ip2:String= "";
	var action:String= "";
	var action2:String= "";
	var t:Trade= null;
	public function NickThread(t:Trade, nick1:String, nick2:String, ip2:String, action:String, action2:String) {
		this.t = t;
		this.nick1 = nick1;
		this.nick2 = nick2;
		this.ip2 = ip2;
		this.action = action;
		this.action2 = action2;
	}
	public function run():void {
		try {
			var nnip:String= "nick1="+nick1+"&nick2="+nick2+"&ip1="+InetAddress.getLocalHost().getHostName()+"&ip2="+ip2;
			var sb:StringBuffer= new StringBuffer();
			t.startTrade(nnip, sb); // trade.php
			// sb should have a list of your items

			// sb should contain the list of your items you want to trade
			t.makeOffer(nnip, action, sb);  // offer.php
			if (t.getOffer(nnip, sb)) { // gettradeitems.php
				// sb contains the list of items the other party wants to trade
				t.acceptOffer(nnip, action2); // accept.php
				t.transferGoods(nnip); // exchange.php
			}
		} catch (var e:Exception) {
			e.printStackTrace();
		}
	}
}

public class Trade extends Vector {
	var url:String= "";
	// String ipaddress = "69.107.89.126";
	var ipaddress:String= "127.0.0.1";
	public static function main(String args[]):void throws Exception {
		var t:Trade= new Trade("http://localhost/chat/");
		// Trade t = new Trade("http://adsl-69-107-89-126.dsl.pltn13.pacbell.net/icbm/");

		// Trade t = new Trade("http://localhost/icbm/");
		var yt:NickThread= new NickThread(t, "yottzumm", "HiveChild", t.ipaddress+"YOTTZUMM", "OFFER", "ACCEPT");
		yt.start();
		var hct:NickThread= new NickThread(t, "HiveChild", "yottzumm", t.ipaddress+"YOTTZUMM", "OFFER", "ACCEPT");
		hct.start();
	}
	public function Trade(url:String) {
		this.url = url;
	}
	public function add(jcb:HTMLJCheckBox):void {
		super.add(jcb);
	}
	public function startTrade(nnip:String, sb:StringBuffer):void throws Exception {
		var is:InputStream= new URL(url+"trade.php?"+nnip).openStream();
		var br:BufferedReader= new BufferedReader(new InputStreamReader(is));
		var line:String= null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		is.close();
		br.close();
	}
	public function inventory(nnip:String, sb:StringBuffer):void throws Exception {
		var is:InputStream= new URL(url+"inventory.php?"+nnip).openStream();
		var br:BufferedReader= new BufferedReader(new InputStreamReader(is));
		var line:String= null;
		while ((line = br.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}
		is.close();
		br.close();
	}
	public function makeOffer(nnip:String, action:String):void throws Exception {
		var i:Iterator= iterator();
		var sb:StringBuffer= new StringBuffer();
		while (i.hasNext()) {
			var jcb:HTMLJCheckBox= HTMLJCheckBox(i.next());
			if (jcb.isSelected()) {
				sb.append(jcb.item);
				System.err.println("Trading "+jcb.item);
			}
		}
		makeOffer(nnip, action, sb);
	}
	public function makeOffer(nnip:String, action:String, sb:StringBuffer):void throws Exception {
		var is:InputStream= new URL(url+"offer.php?"+nnip+"&action="+action+"&items="+URLEncoder.encode(sb.toString(), "UTF-8")).openStream();
		var br:BufferedReader= new BufferedReader(new InputStreamReader(is));
		var line:String= null;
		while ((line = br.readLine()) != null) {
			if (line.equals("REJECT")) {
				throw new Exception("REJECT");
			}
		}
		is.close();
		br.close();
	}
	public function getOffer(nnip:String, sb:StringBuffer):Boolean throws Exception {
		var o:int= 0;  // number of offers
		var r:int= 0; // number of rejects
		var starttime:Number= System.currentTimeMillis();
		var timeout:Number= 60000; // 60 second time out
		var currtime:Number= System.currentTimeMillis();
		do {
			sb.delete(0, sb.length());
			var is:InputStream= new URL(url+"gettradeitems.php?"+nnip).openStream();
			var br:BufferedReader= new BufferedReader(new InputStreamReader(is));
			var offer:String= br.readLine(); // # offers
			System.err.println(offer);
			if (offer != null) {
				try {
					o = Integer.parseInt(offer);
				} catch (var ae:Exception) {
					ae.printStackTrace();
				}
			}
			var reject:String= br.readLine(); // # rejects
			System.err.println(reject);
			if (reject != null) {
				try {
					r = Integer.parseInt(reject);
				} catch (var ae:Exception) {
					ae.printStackTrace();
				}
			}
			var line:String= null;
			while ((line = br.readLine()) != null) {
				System.err.println(line);
				sb.append(line);
				sb.append("\n");
			}
			is.close();
			br.close();
			currtime = System.currentTimeMillis();
			if (o < 2&& r == 0) {  // wait for a few seconds
				Thread.sleep(3000);
			}
		} while (o < 2&& r == 0&& currtime - starttime < timeout);
		if (r > 0) {
			throw new Exception("REJECT");
		}
		return o >= 2;
	}
	public function acceptOffer(nnip:String, action2:String):void throws Exception {
		var is:InputStream= new URL(url+"accept.php?"+nnip+"&action="+action2).openStream();
		var br:BufferedReader= new BufferedReader(new InputStreamReader(is));
		var line:String= null;
		while ((line = br.readLine()) != null) {
			if (line.equals("REJECT")) {
				throw new Exception("REJECT");
			}
		}
		is.close();
		br.close();
	}
	public function transferGoods(nnip:String):void throws Exception {
		var a:int= 0;  // number of accepts
		var r:int= 0; // number of rejects
		var starttime:Number= System.currentTimeMillis();
		var timeout:Number= 60000; // 60 second time out
		var currtime:Number= System.currentTimeMillis();
		do {
			var is:InputStream= new URL(url+"exchange.php?"+nnip).openStream();
			var br:BufferedReader= new BufferedReader(new InputStreamReader(is));
			var accept:String= br.readLine(); // # offers
			System.err.println(accept);
			if (accept != null) {
				try {
					a = Integer.parseInt(accept);
				} catch (var ae:Exception) {
					ae.printStackTrace();
				}
			}
			var reject:String= br.readLine(); // # rejects
			System.err.println(reject);
			if (reject != null) {
				try {
					r = Integer.parseInt(reject);
				} catch (var ae:Exception) {
					ae.printStackTrace();
				}
			}
			var line:String= null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			is.close();
			br.close();
		} while (a + r < 2&& currtime - starttime < timeout);
	}
}
}