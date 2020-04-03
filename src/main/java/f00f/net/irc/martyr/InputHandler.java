package f00f.net.irc.martyr;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple class to help manage input from the stream.
 */
public class InputHandler extends Thread {
	static Logger log = Logger.getLogger("InputHandler");

	private BufferedReader reader;
	private IRCConnection connection;
	private final LinkedList<String> messages;

	private final Object eventMonitor;

	private static int serialGen = 0;
	private int serialNumber = serialGen++;
	private boolean doShutdown = false;

	public InputHandler( BufferedReader reader,
        IRCConnection connection,
        Object eventMonitor )
    {

        super("InputHandler");
        this.reader = reader;
        this.connection = connection;
        messages = new LinkedList<String>();
        this.eventMonitor = eventMonitor;

        log.log(Level.FINE,"IRCConnection: New");
    }

	/**
	 * Set the shutdown flag, so that after next read, or on any error, the thread
	 * will just exit.
	 */
	public void signalShutdown() {
		synchronized (this) {
			doShutdown = true;
		}
	}

	/**
	 * @return true if there are messages waiting to be processed.
	 */
	public boolean pendingMessages() {
		synchronized (messages) {
			return !messages.isEmpty();
		}
	}

	/**
	 * Gets the message at the top of the message queue and removes it from the
	 * message queue.
	 *
	 * @return Message from top of list.
	 */
	public String getMessage() {
		synchronized (messages) {
			return messages.removeFirst();
		}
	}

	/**
	 * Waits for input from the server. When input arrives, it is added to a queue
	 * and eventMonitor.notifyAll() is called.
	 */
	public void run() {
		log.log(Level.FINE, "IRCConnection: Running");
		try {

			String str;
			while (true) {
				synchronized (this) {
					if (doShutdown) {
						return;
					}
				}
				str = reader.readLine();
				if (str == null) {
					connection.socketError(new IOException("Socket disconnected"));
					return;
				}
				synchronized (messages) {
					messages.addLast(str);
				}
				synchronized (eventMonitor) {
					eventMonitor.notifyAll();
				}
			}
		} catch (IOException ioe) {
			if (doShutdown) {
				return;
			}
			connection.socketError(ioe);
		} finally {
			log.log(Level.FINE, "IRCConnection: Input handler has DIED!");
		}
	}

	public String toString() {
		return "InputHandler[" + serialNumber + "]";
	}

	// ----- END InputHandler --------------------------------------------
}
