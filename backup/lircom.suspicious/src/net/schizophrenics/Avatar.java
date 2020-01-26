package net.schizophrenics;

public class Avatar implements java.io.Serializable {
	static final long serialVersionUID = 2971157455873258205L;
	private String name = "Dummy Avatar";
	public Avatar(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void receive(Message m, Conversation c) {
		System.out.print("<");
		System.out.print(c.getName());
		System.out.print(">");
		System.out.println(m.toString());
	}
}
