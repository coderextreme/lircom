package lircom;

import java.net.*;
import java.io.*;

public class Identd extends Thread {
	static final int serverPort = 113; // identd server port
	public static void main (String args[]) {
		Identd idd = new Identd();
		idd.start();
	}
	public void run() {
		try {
			ServerSocket ss = new ServerSocket(serverPort);
			while (true) {
				System.err.println("Waiting...");
				// wait until IRC client connects
				Socket as = ss.accept();
				InputStream ais = as.getInputStream();
				OutputStream aos = as.getOutputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(ais));
				PrintStream ps = new PrintStream(aos);
				String line = br.readLine();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
