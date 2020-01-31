package lircom;

public interface SendCommandInterface {
	void msgSend(String line, String recipient);
	void msgSend(String line);
	String getUser();
}
