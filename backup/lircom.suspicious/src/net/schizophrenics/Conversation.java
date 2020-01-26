package net.schizophrenics;

import java.util.Vector;
import java.util.Iterator;

public class Conversation implements java.io.Serializable {
	static final long serialVersionUID = 2044985898223956603L;
	private Vector messages = new Vector();
	private Vector members = new Vector();
	private String name = "Conversation";
	public Conversation(String name) {
		this.name = name;
	}
	public void addMessage(Message m) throws Exception {
		Iterator memberi = members.iterator();
		boolean added = false;
		while (memberi.hasNext()) {
			if (!added && (Avatar)memberi.next() == m.getAvatar()) {
				messages.add(m);
				added = true;
			}
		}
		if (!added) {
			throw new Exception("You are not a member of that conversation");
		} else {
			// now send message to people
			memberi = members.iterator();
			while (memberi.hasNext()) {
				Avatar a = (Avatar)memberi.next();
				a.receive(m, this);
			}
		}
	}
	public Message getMessage(int i) {
		return (Message)messages.get(i);
	}
	public void addMember(Avatar m) {
		members.add(m);
	}
	public void removeMember(Avatar m) {
		members.remove(m);
	}
	public Vector getMessages() {
		return messages;
	}
	public Vector getMembers() {
		return members;
	}
	public String getName() {
		return name;
	}
	public void print() {
		Iterator memberi = members.iterator();
		System.out.println(name);
		while (memberi.hasNext()) {
			System.out.print(((Avatar)memberi.next()).getName());
			System.out.print(" ");
		}
		System.out.println();
		Iterator msgi = messages.iterator();
		while (msgi.hasNext()) {
			System.out.println(msgi.next());
		}
		System.out.println();
	}
}
