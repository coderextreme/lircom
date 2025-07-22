package impact;

public class OutLineHandler implements LineHandler {
	public void receive(String line) {
		System.out.print(line);
		System.out.flush();
	}
	public void receive(String nick, String line) {
		System.out.print(nick);
		System.out.print("> ");
		System.out.print(line);
		System.out.flush();
	}
}
