package impact;

interface LineHandler {
	void receive(String line);
	void receive(String nick, String line);
}
