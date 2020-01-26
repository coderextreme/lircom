package lircom {
import java.io.*;
import java.util.*;
import java.net.*;


class Splitter extends Thread {
	var is:InputStream= null;
	var sos:OutputStream= null;
	var ircRoom1:String;
	var ircNick1:String;
	var ircUser1:String;
	var ircPass1:String;
	var lircomPort:int;
	var sent:Boolean= false;
	var bridge:Bridge= null;
	public function Splitter(is:InputStream, sos:OutputStream, ircRoom1:String, ircUser1:String, ircNick1:String, ircPass1:String, lircomPort:int, bridge:Bridge) {
		this.is = is;
		this.sos = sos;
		this.ircRoom1 = ircRoom1;
		this.ircUser1 = ircUser1;
		this.ircNick1 = ircNick1;
		this.ircPass1 = ircPass1;
		this.lircomPort = lircomPort;
		this.bridge = bridge;
	}
	public function setSent(s:Boolean):void {
		sent = s;
	}
	public function getSent():Boolean {
		return sent;
	}
	class Delay extends Thread {
		var sps:PrintStream;
		var ircRoom1:String;
		var s:Splitter;
		public function Delay(sps:PrintStream, ircRoom1:String, s:Splitter) {
			this.sps = sps;
			this.ircRoom1 = ircRoom1;
			this.s = s;
		}
		public function run():void {
			try {
				sleep(10000);
				if (!s.getSent()) {
					println(sps, "JOIN "+ircRoom1);
					println(sps, "MODE "+ircRoom1);
					println(sps, "WHO "+ircRoom1);
					s.setSent(true);
				}
			} catch (var e:Exception) {
				e.printStackTrace();
			}
		}
	}
	class PingPongOut extends Thread {
		var sps:PrintStream= null;
		public function PingPongOut(sps:PrintStream) {
			this.sps = sps;
		}
		public function run():void {
			try {
				while (true) {
					sleep(30000);
                                        println(sps, "PING LAG"+System.currentTimeMillis());
				}
			} catch (var e:Exception) {
				e.printStackTrace();
			}
		}
	}
	public function run():void {
		try {
			var br:BufferedReader= new BufferedReader(new InputStreamReader(is));
			var sps:PrintStream= new PrintStream(sos);
                        if (ircPass1 != null && !ircPass1.trim().equals("")) {
                            println(sps, "PASS "+ircPass1);
                        }
			println(sps, "NICK "+ircNick1);
			println(sps, "USER "+ircUser1+" 0 * :"+(lircomPort == -1? "NotAvailable" : ""+lircomPort));
			new Delay(sps, ircRoom1, this).start();
			sps.flush();
			new PingPongOut(sps).start();
			var line:String= null;
			while ((line = br.readLine()) != null) {	
				System.err.println("from Server "+line);
				var i:int= 0;
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
					var st:StringTokenizer= new StringTokenizer(line, " ");
					var server:String= st.nextToken();
					var code:String= st.nextToken();
					var yournick:String= st.nextToken();
					var channel:String= st.nextToken();
					var identid:String= st.nextToken();
					var theirhost:String= st.nextToken();
					var theirserver:String= st.nextToken();
					var theirnick:String= st.nextToken();
					var attributes:String= st.nextToken();
					var unknown:String= st.nextToken();
					var port:String= st.nextToken();
					PossibleConnection.add(new PossibleConnection(theirhost, port, theirnick, ""+new Date()));
				}
			}
			System.err.println("Closing");
			sps.close();
			is.close();
			sos.close();
		} catch (var e:Exception) {
			e.printStackTrace();
		}
	}
	public function println(sps:PrintStream, str:String):void {
		System.err.println("to SERVER "+str);
		sps.println(str);
	}
}

public class Bridge extends Thread {

	var s1:Socket= null;
	var is1:InputStream= null;
	var os1:OutputStream= null;
        var ircRoom1:String=  "#schizophrenia";

	public static function main(String args[]):void throws Exception {
		var ircServer1:String= "irc.avalonworks.ca";
		var ircPort1:int= 6667;
                var ircRoom1:String= "#schizophrenia";
                var ircUser1:String= "networkbridge";
                var ircNick1:String= "netbrdge1";
                var ircPass1:String= "tofuburgers";
                var lircomPort1:int= 8180;
		var b:Bridge= new Bridge(ircServer1, ircPort1, ircRoom1, ircUser1, ircNick1, ircPass1, lircomPort1);
		b.start();
	}
	public function Bridge(ircServer1:String, ircPort1:int, ircRoom1:String, ircUser1:String, ircNick1:String, ircPass1:String, lircomPort1:int) throws Exception {
		var idd:Identd= new Identd();
		idd.start();

		s1 = new Socket(ircServer1,ircPort1);
		is1 = s1.getInputStream();
		os1 = s1.getOutputStream();
                this.ircRoom1 = ircRoom1;

		new Splitter(is1, os1, ircRoom1, ircUser1, ircNick1, ircPass1, lircomPort1, this).start();
	}
	public function run():void {
		try {
			var sps:PrintStream= new PrintStream(os1);
			for (;;) {
				sps.println("WHO "+ircRoom1+"\n");
				PossibleConnection.pcons.clear();
				Thread.sleep(30000);
				sps.flush();
				var i:Iterator= PossibleConnection.iterator();
				while (i.hasNext()) {
					var pcon:PossibleConnection= PossibleConnection(i.next());
					System.err.println("****NICK "+pcon.nick);
					System.err.println("****HOST "+pcon.host);
					System.err.println("****PORT "+pcon.port);
                                        System.err.println("****DATE "+pcon.date);
				}
			}
		} catch (var e:Exception) {
		}
	}
}
}