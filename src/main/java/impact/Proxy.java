package impact;

import java.net.Socket;

class Proxy implements LineHandler {
        static Proxy proxy = new Proxy();

	ImpactClient chat = null;
	private Proxy() {
		try {
			chat = new ImpactClient(new Socket("localhost", 8180), Long.toString(System.currentTimeMillis()));
			chat.start();
			select(this);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Problems instantiating communications");
		}
	}
	synchronized static public Proxy getProxy() {
		return proxy;
	}
	public void insert(Polygon arc) {
		arc.insert(proxy);
	}
	public void insert(Point node) {
		node.insert(proxy);
	}
	public void insert(GraphObject obj) {
		obj.insert(proxy);
	}
	public void remove(Polygon arc) {
		arc.remove(proxy);
	}
	public void remove(Point node) {
		node.remove(proxy);
	}
	public void remove(GraphObject obj) {
		obj.remove(proxy);
	}
	public void update(Polygon arc) {
		arc.update(proxy);
	}
	public void update(Point node) {
		node.update(proxy);
	}
	public void update(GraphObject obj) {
		obj.update(proxy);
	}
        public void select(Proxy proxy) {
		proxy.send("NODE||SELECT");
		proxy.send("ARC||SELECT");
	}
	public void send(String line) {
		try {
			lircom.Message m = new lircom.Message("*", chat.getNick(), line, "__");
			chat.send(m);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
        public void close() {
		try {
			chat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void receive(String line) {
		Impact3D.cmd = Impact3D.UPDATE;
		if (line.startsWith("ARC")) {
			// System.err.println("Received ARC");
			Polygon.receive(line);
		} else if (line.startsWith("NODE")) {
			// System.err.println("Received NODE");
			Point.receive(line);
		}
	}
}
