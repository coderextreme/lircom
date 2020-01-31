package solitaire;

import java.io.FileWriter;
import java.util.*;
import org.json.*;

public class Log {
	static FileWriter fw = null;
	static FileWriter fw2 = null;
	static int stamp = 0;
	static lircom.ClientInterface cos;
	static boolean enabled = true;
	public static void initialize(SolitaireClient solclient) throws Exception {
		cos = solclient;
	}
	static long ts = 0;
	public static void write(JSONObject obj) {
		try {
			if (enabled) {
				if (cos !=  null) {
					synchronized(cos) {
						System.err.println("SENDING "+obj);
						cos.send(obj);
					}
				} else {
					System.err.println("NOT SENDING CLIENT IS NULL");
				}
			} else {
				System.err.println("NOT SENDING DISABLED");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void write(String msg) {
		try {
			if (enabled) {
				if (cos !=  null) {
					synchronized(cos) {
						lircom.Message m = new lircom.Message("*", cos.getNick(), msg, "__");
						// System.err.println("writing "+msg);
						cos.send(m);
						// cos.receive(m.generate());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
