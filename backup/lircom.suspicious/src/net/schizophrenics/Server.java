package net.schizophrenics;

import java.util.Vector;

public class Server implements java.io.Serializable {
	static final long serialVersionUID = 3000762669117054242L;
	static Server s;
	private Vector conversations = new Vector();
	static public Server connect() {
		if (s == null) {
			s = new Server();
		}
		return s;
	}
	private Server() {
	}
	public Conversation newPublicConversation(String name) {
		Conversation c = new Conversation(name);
		conversations.add(c);
		return c;
	}
	public Vector getConversations() {
		return conversations;
	}
	public Account newAccount() {
		return new Account();
	}
}
