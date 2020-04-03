package lircom;

import java.io.InputStream;

public interface SendChatInterface {
	void username(String username);
	void password(String password);
	void nickname(String nick);
	void sendJoin(String room);
	void sendLeave(String room);
	void sendQuit();
	void send(String to, String message);
	void send(String to, InputStream file);
	void sendAction(String to, String message);
	void sendPing();
	// people on system
	void requestPeople();
	// rooms on system
	void requestRooms();
	// people in room
	void requestPeople(String room);
	// rooms a person is in
	void requestRooms(String person);
}
