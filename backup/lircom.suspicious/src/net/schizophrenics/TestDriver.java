package net.schizophrenics;

public class TestDriver {
	static public void main(String[] args) {
		Server s = Server.connect();
		Account acct = s.newAccount();
		Avatar a = acct.newAvatar("yottzumm");
		Conversation c = s.newPublicConversation("Chat Room 1");
		c.addMember(a);
		send(acct, a, c, "Hello, World!");

		// now second chatter enters
		Account acct2 = s.newAccount();
		Avatar a2 = acct2.newAvatar("john");
		Conversation c2 = (Conversation)s.getConversations().get(0);
		Message m2 = (Message)c2.getMessages().get(0);
		Avatar a3 = m2.getAvatar();
		Conversation c3 = acct2.newPrivateConversation("Chat Room 2");
		c3.addMember(a2);
		send(acct2, a2, c3, "Goodbye, Blue Moon");

		// now second chatter attempts to impersonate first
		send(acct, a2, c3, "NO GOOD!!");
		send(acct2, a, c3, "NO GOOD!!");
		send(acct, a3, c3, "NO GOOD!!");
		c3.addMember(a);
		send(acct, a, c3, "GOOD!!");
		c.removeMember(a);
		c.removeMember(a2);
		// c.print();
		c3.removeMember(a);
		c3.removeMember(a2);
		// c3.print();
		acct.removeAvatar(a3);
		acct.removeAvatar(a);
		acct2.removeAvatar(a2);
	}
	static public void send(Account acct, Avatar a, Conversation c, String text) {
		try {
			Message m = acct.newMessage(a, text);
			c.addMessage(m);
		} catch (Exception e) {
			System.err.print("Error: ");
			System.err.print(e.getMessage());
			System.err.print(", trying to send message as ");
			System.err.println(a.getName());

		}
		// c.print();
	}
}
