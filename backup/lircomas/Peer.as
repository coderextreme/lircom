package lircom {
import java.net.*;

public class Peer extends Thread {
	private var port:int= 0;
	private var ss:ServerSocket;
	static public function main(String args[]):void throws Exception {
		if (args.length != 1) {
			System.err.println("Usage: java Peer localport");
		}
		var s:Peer= new Peer();
		s.setPort(Integer.parseInt(args[0]));
		s.start();
	}
	public function Peer() throws Exception {
	}
	public function setPort(port:int):void throws Exception {
		this.port = port;
		ss = new ServerSocket(port);
	}
	public function run():void {
		try {
			for (;;) {
				var client:Socket= ss.accept();
				var c:ClientOnServer= new ClientOnServer(client);
				System.err.println("new client is "+c.clientno);
                                c.setNick("COS-"+c.clientno);
                                c.start();
			}
		} catch (var e:Exception) {
			e.printStackTrace();
		}
	}

}
}