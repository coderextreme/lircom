package lircom {
import java.io.*;
import java.net.*;

class ClientImpl extends ClientOnServer {
	public function ClientImpl(is:InputStream, os:OutputStream) throws Exception {
		super(is, os);
	}
	public function processLine(line:String):Boolean throws Exception {
		var m:Message= Message.parse(line);
		return true;
	}
}

public class Client extends Peer {
	public function Client() throws Exception {
	}
	static public function main(String args[]):void throws Exception {

		if (args.length < 3) {
			System.err.println("Usage: java Client localport remotehost remoteport");
			System.exit(1);
		}
		var stdio:ClientImpl= new ClientImpl(System.in, System.out);
		System.err.println("Your client is "+stdio.clientno);
		var s:Socket= new Socket(args[1], Integer.parseInt(args[2]));
		var client:ClientOnServer= new ClientOnServer(s);
                client.start();
	}
}
}