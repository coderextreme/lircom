package impact;

public class OutLineHandler implements LineHandler {
	public void receive(String line) {
		System.out.print(line);
		System.out.flush();
	}
}
