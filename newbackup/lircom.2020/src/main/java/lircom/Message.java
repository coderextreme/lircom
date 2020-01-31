/*
 * Message.java
 *
 * Created on February 19, 2005, 12:57 PM
 */

package lircom;

import org.json.*;

/**
 *
 * @author carlsonj
 */
public class Message extends Throwable {
    String from = "";
    public java.util.Hashtable rec = new java.util.Hashtable();
    long timestamp = 0; // don't want to make public, but have to so that flooding can be done
    public String nick = "";
    public String message = "";
    public JSONObject json = null;
    public String error = "";
    public String language = "en";
    long sequenceno = 0;
    private static long sequencehigh = 0;
    /** Creates a new instance of Message */

    public Message(java.util.Hashtable rec, String nick, String message, String lang) {
	this();
	this.rec = rec;
        this.from = "";
        this.nick = nick;
	this.message = message;
        this.language = lang;
    }
    public Message(String to, String nick, String message, String lang) {
	this((java.util.Hashtable)null, nick, message, lang);
        rec = new java.util.Hashtable();
	rec.put(to, to);
    }
    public Message(String to, String nick, String error, String message, String lang) {
	this(to, nick, message, lang);
	this.error = error;
    }
    private Message() {
        this.timestamp = System.currentTimeMillis();
        synchronized (getClass()) {
            sequenceno = sequencehigh++;
        }
    }
    static public Message parse(JSONObject obj) {
        System.err.println("Receiving "+obj);
        Message m = new Message();
	JSONArray to = obj.optJSONArray("to");
	if (to != null) {
		for (int t = 0; t < to.length(); t++) {
			m.rec.put(to.optString(t), to.optString(t));
		}
	}
	m.from = obj.optString("from");
        m.timestamp = obj.optLong("timestamp");
        m.sequenceno = obj.optLong("sequenceno");
        m.error = obj.optString("error");
        m.language = obj.optString("language");
        m.nick = obj.optString("nick");
        m.message = obj.optString("message");
        m.json = obj.optJSONObject("json");
        return m;
    }
    public JSONObject generate() {
	JSONObject obj = new JSONObject();
	JSONArray to = new JSONArray();
	java.util.Iterator i = rec.keySet().iterator();
	while (i.hasNext()) {
		String toit = (String)i.next();
		to.put(toit);
	}
	try {
		obj.put("to", to);
		obj.put("from", from);
		obj.put("timestamp", timestamp);
		obj.put("sequenceno", sequenceno);
		obj.put("error", error);
		obj.put("language", language);
		obj.put("nick", nick);
		obj.put("message", message);
		obj.put("json", json);
	} catch (JSONException e) {
		e.printStackTrace();
	}
        System.err.println(System.currentTimeMillis()+" Sending "+obj);
        return obj;
    }
    public void translate(String targetLanguage) {
	    System.err.println("language is "+ language);
	    System.err.println("target language is "+ targetLanguage);
	    System.err.println("message "+ message);
	if (!language.equals("__") && !language.equals(targetLanguage)) {
		try {
			message = BabelFish.translate(message, language, targetLanguage);
			language = targetLanguage;
		} catch (Exception e) {
			message = "Untranslated "+language+" "+message;
		}
	}
	    System.err.println("translated message is "+ message);
    }
}
