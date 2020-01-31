package impact;

import java.util.*;

public class ImpactClient extends lircom.ClientOnServer {
	public ImpactClient(java.net.Socket s, String nick) throws Exception {
		super(s);
		setNick("Impact"+nick);
	}
	public Hashtable client_messages = new Hashtable();
	public boolean processLine(String line) throws Exception {
		// System.err.println("received "+line);
		lircom.Message m = lircom.Message.parse(line);
		if (m.nick.startsWith("Impact") && !m.nick.equals(getNick()) && !seenMessage(m, client_messages)) {
			// System.err.println("Processing "+m.message);
			Proxy.getProxy().receive(m.message);
			return true;
		} else {
			return false;
		}
	}
}
