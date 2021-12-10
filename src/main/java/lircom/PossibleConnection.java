/*
 * PossibleConnection.java
 *
 * Created on February 17, 2005, 9:05 PM
 */

package lircom;

/**
 *
 * @author carlsonj
 */
public class PossibleConnection {
    
    /** Creates a new instance of PossibleConnection */
    public PossibleConnection() {
    }
	String host;
	String port;
        String nick;
        String date;
        boolean connected;
	static java.util.Hashtable<Object, PossibleConnection> pcons = new java.util.Hashtable<Object, PossibleConnection>();
        ClientOnServer client;
	public PossibleConnection(String host, String port, String nick, String date) {
		this.host = host;
		this.port = port;
                this.nick = nick;
                this.date = date;
                this.connected = false;
                if (get(host+"|"+port+"|"+nick) == null) {
                    add(this);
                }
	}
        static public void add(PossibleConnection con) {
           System.err.println("Adding "+con.host+" "+con.port+" "+con.nick);
           pcons.put(con.host+"|"+con.port+"|"+con.nick, con);
        }
        static java.util.Iterator iterator() {
                return pcons.keySet().iterator();
        }
        static PossibleConnection get(Object str) {
                return pcons.get(str);
        }
        static PossibleConnection get(String host, String port, String nick) {
                PossibleConnection o = get(host+"|"+port+"|"+nick);
                if (o != null) {
                    return o;
                } else {
                    return new PossibleConnection(host, port, nick, "");
                }
        }
}
