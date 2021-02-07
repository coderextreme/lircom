/*
 * Message.java
 *
 * Created on February 19, 2005, 12:57 PM
 */

package lircom;

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
    public String error = "";
    public String language = "en";
    long sequenceno = 0;
    private static long sequencehigh = 0;
    /** Creates a new instance of Message */

    public Message(java.util.Hashtable rec, String nick, String message, String lang) {
	this.rec = rec;
        this.from = "";
        this.nick = nick;
	this.message = message;
	this.stripCRNL();
        this.timestamp = System.currentTimeMillis();
        this.language = lang;
        synchronized (getClass()) {
            sequenceno = sequencehigh++;
        }
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
    public void stripCRNL() {
	// don't process messages with newlines, remove everything following the
	// newline
	int nl = message.indexOf("\n");
	if (nl >= 0) {
		message = this.message.substring(0, nl);
	}
	int cr = message.indexOf("\r");
	if (cr >= 0) {
		message = message.substring(0, cr);
	}
    }
    private Message() {
    }
    static public Message parse(String line) {
                System.err.println("Receiving "+line);
                Message m = new Message();
                //to 
        	int tb = line.indexOf("{");
		int toe = tb;
		int tob = line.indexOf("{", toe+1);
		while (toe+1 == tob) {
		    toe = line.indexOf("}", tob+1);
		    String to = line.substring(tob+1,toe);
		    m.rec.put(to, to);
		    tob = line.indexOf("{", toe);
                }
		int te = line.indexOf("}", toe+1);
                //from
		int fb = line.indexOf("{", te+1);
		int fe = line.indexOf("}", fb+1);
		// timestamp
		int sb = line.indexOf("{", fe+1);
		int se = line.indexOf("}", sb+1);
                // sequence no
                int qb = line.indexOf("{", se+1);
                int qe = line.indexOf("}", qb+1);
		// error
		int eb = line.indexOf("{", qe+1);
		int ee = line.indexOf("}", eb+1);
                // language
                int lb = line.indexOf("{", ee+1);
                int le = line.indexOf("}", lb+1);
		// nick
		int nb = line.indexOf("{", le+1);
		int ne = line.indexOf("}", nb+1);
                // TODO handle nicks with braces in them
                if (fb >= 0 && fe >= 0) {
                    m.from = line.substring(fb+1, fe);
                }
                if (sb >= 0 && se >= 0) {
		    System.err.println("Parsing long from "+line+" found "+line.substring(sb+1, se));
                    m.timestamp = Long.parseLong(line.substring(sb+1, se));
                }
                if (qb >= 0 && qe >= 0) {
                    m.sequenceno = Long.parseLong(line.substring(qb+1, qe));
                }
                if (eb >= 0 && ee >= 0) {
                    m.error = line.substring(eb+1, ee);
                }
                if (lb >= 0 && le >= 0) {
                    m.language = line.substring(lb+1, le);
                }
                if (nb >= 0 && ne >= 0) {
                    m.nick = line.substring(nb+1, ne);
                }
                if (ne >= 0) {
                    m.message = line.substring(ne+1);
                }
		m.stripCRNL();
                return m;
    }
    public String generate() {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
	java.util.Iterator i = rec.keySet().iterator();
	while (i.hasNext()) {
		String to = (String)i.next();
		sb.append("{");
		sb.append(to);
		sb.append("}");
	}
        sb.append("}");
        sb.append("{");
        sb.append(from);
        sb.append("}");
        sb.append("{");
        sb.append(timestamp);
        sb.append("}");
        sb.append("{");
        sb.append(sequenceno);
        sb.append("}");
        sb.append("{");
        sb.append(error);
        sb.append("}");
        sb.append("{");
        sb.append(language);
        sb.append("}");
        sb.append("{");
        sb.append(nick);
        sb.append("}");
        sb.append(message);
        System.err.println(System.currentTimeMillis()+" Sending "+sb);
        return sb.toString();
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
