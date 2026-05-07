package lircom;

import java.io.*;

public class FromGUI extends InputStream {
	int ba = 0;
        int pos = 0;
        byte [] buffer;
	public FromGUI() {
	}
	public void write(byte [] b) {
		synchronized (this) {
                        if (buffer == null) {
                                buffer = b.clone();
                                ba = buffer.length;
                                pos = 0;
                        } else {
                                byte [] newbuffer = new byte[buffer.length+b.length];
                                int i;
                                for (i = 0; i < buffer.length; i++) {
                                    newbuffer[i] = buffer[i];
                                }
                                for (int j = 0; j < b.length; j++) {
                                    newbuffer[i+j] = b[j];
                                }
                                ba += b.length;
                                buffer = newbuffer;
                        }
		}
	}
	public int available() {
		while (ba == 0) {
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
		synchronized (this) {
			return ba;
		}
	}
	public int read() {
		while (ba == 0) {
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
		synchronized (this) {
			int b = buffer[pos];
			ba--;
                        pos++;
                        if (ba == 0) {
                            buffer = null;
                            pos = 0;
                        }
			return b;
		}
	}
	public int read(byte[] b, int off, int len) {
		while (ba == 0) {
			try {
				Thread.sleep(500);
			} catch (Exception e) {
			}
		}
		synchronized (this) {
			if (len <= ba) {
                                for (int i = 0; i < len; i++) {
                                    b[i+off] = buffer[pos];
                                    pos++;
                                }
				ba -= len;
				return len;
			} else {
                                for (int i = 0; i < ba; i++) {
                                    b[i+off] = buffer[pos];
                                    pos++;
                                }
				int bytes = ba;
				ba = 0;
                                buffer = null;
                                pos = 0;
				return bytes;
			}
		}
	}
	public int read(byte[] b) {
		return read(b, 0, b.length);
	}
}
