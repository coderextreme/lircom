package lircom;

import java.io.*;

class FromServer extends OutputStream {
	Chat c;
	public FromServer() {
	}
	public void setChat(Chat c) {
		this.c = c;
	}
	public void write(int b) {
		// System.err.println("wrote "+b);
                Message m = new Message("Tiny Byte", "Tiny Byte", b+"", "__");
		c.displayToScreen(m);  // TODO UTF-8
	}
	public void write(byte [] buf, int off, int len) {
		try {
			String line = new String(buf, off, len); 
			// System.err.println(c.getNick()+" received "+line);
			c.processLine(line);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

