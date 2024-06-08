package impact;

import java.util.Hashtable;

public class ImpactClient extends lircom.ClientOnServer {
	public Hashtable client_messages = new Hashtable();
	private Proxy proxy = null;
	public ImpactClient(Proxy proxy, java.net.Socket s, String nick) throws Exception {
		super(s);
		setNick("Impact"+nick);
		lircom.Message.thisApplication = "Impact";
		this.proxy = proxy;
	}
	public lircom.Message processLine(String line) throws Exception {
		// System.err.println("received "+line);
		lircom.Message m = lircom.Message.parse(line);
		if ((m.nick.startsWith("Impact") || m.nick.startsWith("CpponUser") || m.nick.startsWith("MocapUser")) && !m.nick.equals(getNick()) && !seenMessage(m, client_messages)) {
			// System.err.println("Processing "+m.nick+"'s message in ImpactClient "+m.message);
			this.proxy.receive(m.nick, m.message);
			return m;
		} else {
			return null;
		}
	}
}
