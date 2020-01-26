package lircom;

import java.net.*;
import java.util.*;
import java.io.*;
import org.json.*;

public abstract class ClientInterface extends Thread {
	public abstract boolean processLine(String message) throws Exception;
	public abstract boolean processObj(JSONObject obj) throws Exception;
	public abstract void send(Message line) throws Exception;
	public abstract void send(JSONObject obj) throws Exception;

	private String nick;
	public void setNick(String nick) throws Exception {
		this.nick = nick;
	}
	public String getNick() {
		return this.nick;
	}
        public boolean seenMessage(Message m, Hashtable messages) {
            String umsg = m.timestamp+","+m.sequenceno+","+m.nick;
            Iterator i = messages.keySet().iterator();
            Vector removes = new Vector();
            while (i.hasNext()) {
                String sumsg = (String)i.next();
                //System.err.println("sumsg "+sumsg+" umsg "+umsg);
                if (umsg.equals(sumsg)) {
                    System.err.println("Found "+umsg+" already not sending");
                    return true;
                }
                int comma = sumsg.indexOf(",");
                long msgtime = Long.parseLong(sumsg.substring(0, comma));
                if (System.currentTimeMillis() - msgtime > 30000) { // save messages for 30 seconds
                    removes.add(sumsg);
                }
            }
            i = removes.iterator();
            while (i.hasNext()) {
                String sumsg = (String)i.next();
                // System.err.println("Removing "+sumsg);
                messages.remove(sumsg);
            }
            messages.put(umsg, m);
            return false;
        }
}
