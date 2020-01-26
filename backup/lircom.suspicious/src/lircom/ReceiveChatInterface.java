package lircom;

import java.io.*;

public interface ReceiveChatInterface {
	void receive(String from, String message, String color);
	void receive(String from, String message);
	void receive(String from, InputStream file);
	void receiveAction(String from, String message);
	void receiveJoin(String network, String room, String person);
	void receivePresence(String network, String room, String person);
	void receiveLeave(String room, String person);
	void receiveQuit(String party);
	void setSendCommandInterface(SendCommandInterface send) throws Exception ;
	void receiveRoom(String network, String room, int numUsers, String topic);
	void receiveNick(String network, String oldnick, String newnick);
        public String getLanguage();
}
