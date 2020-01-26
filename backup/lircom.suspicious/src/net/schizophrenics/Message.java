package net.schizophrenics;

public class Message implements java.io.Serializable {
	static final long serialVersionUID = -6950402109493535300L;
	private Avatar a;
	private String text;
	public Message(Account acc, Avatar a, String text) throws Exception {
		if (acc.getAvatars().indexOf(a) >= 0) {
			this.a = a;
			this.text = text;
		} else {
			throw new Exception("You are using an illegal avatar. Shame on you!");
		}
	}
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("<");
		buff.append(a.getName());
		buff.append(">");
		buff.append(text);
		return buff.toString();
	}
	public Avatar getAvatar() {
		return a;
	}
}
