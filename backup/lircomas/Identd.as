package lircom {
import java.net.*;
import java.io.*;

public class Identd extends Thread {
	static final var serverPort:int= 113; // identd server port
	public static function main(String args[]):void {
		var idd:Identd= new Identd();
		idd.start();
	}
	public function run():void {
		try {
			var ss:ServerSocket= new ServerSocket(serverPort);
			while (true) {
				System.err.println("Waiting...");
				// wait until IRC client connects
				var as:Socket= ss.accept();
				var ais:InputStream= as.getInputStream();
				var aos:OutputStream= as.getOutputStream();
				var br:BufferedReader= new BufferedReader(new InputStreamReader(ais));
				var ps:PrintStream= new PrintStream(aos);
				var line:String= br.readLine();
				System.err.println(line);
				line = line+" : USERID : UNIX : firewall";
				ps.println(line);
				System.err.println(line);
				br.close();
				ps.close();
				ais.close();
				aos.close();
				as.close();
			}
		} catch (var e:Exception) {
			e.printStackTrace();
		}
	}
}
}