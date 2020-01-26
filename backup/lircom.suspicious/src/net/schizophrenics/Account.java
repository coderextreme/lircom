package net.schizophrenics;

import java.util.Vector;

public class Account implements java.io.Serializable {
	static final long serialVersionUID = 878815789843958757L;
	private Vector avatars = new Vector();
	private Vector conversations = new Vector();
	public Account() {
	}
	public Conversation newPrivateConversation(String name) {
		Conversation c = new Conversation(name);
		conversations.add(c);
		return c;
	}
	public Avatar newAvatar(String name) {
		Avatar a = new Avatar(name);
		avatars.add(a);
		return a;
	}
	public Message newMessage(Avatar a, String text) throws Exception {
		Message m = new Message(this, a, text);
		return m;
	}
	public void removeAvatar(Avatar a) {
		avatars.remove(a);
	}
	public Vector getAvatars() {
		return avatars;
	}
	public Vector getConversations() {
		return conversations;
	}
}
