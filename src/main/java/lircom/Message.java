/*
 * Message.java
 *
 * Created on February 19, 2005, 12:57 PM
 */

package lircom;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author carlsonj
 */
public class Message extends Throwable {
    String from = "";
    public java.util.Hashtable<String,String> rec = new java.util.Hashtable<String,String>();
    long timestamp = 0; // don't want to make public, but have to so that flooding can be done
    public String nick = "";
    public String message = "";
    public String error = "";
    public String language = "en";
    long sequenceno = 0;
    private static long sequencehigh = 0;
    /** Creates a new instance of Message */
    private static void log(String message) {
	    System.err.println("Message: "+message);
    }

    public Message(java.util.Hashtable<String,String> rec, String nick, String message, String lang) {
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
	this((java.util.Hashtable<String,String>)null, nick, message, lang);
        rec = new java.util.Hashtable<String,String>();
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
        this.timestamp = System.currentTimeMillis();
    }
    static public Message parse(String line) {
	try {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(line);
		Message m = Message.parse(node);
		return m;
	} catch (JsonProcessingException e) {
		e.printStackTrace(System.err);
		return null;
	}
    }
    /*
    static public Message parse(String line) {
                // log("Receiving "+line);
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
		    // log("Parsing long from "+line+" found "+line.substring(sb+1, se));
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
        log(System.currentTimeMillis()+" Sending "+sb);
        return sb.toString();
    }
    */
    public String translate(String targetLanguage) {
	    log("language is "+ this.language);
	    log("target language is "+ targetLanguage);
	    log("input message "+ this.message);
	if (!this.language.equals("__") && !this.language.equals(targetLanguage)) {
		try {
			return BabelFish.translate(message, this.language, targetLanguage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	return this.message;
    }
    static public Message parse(JsonNode node) {
	if (node != null && !"".equals(node.toString().trim())) {
		if (node.isArray()) {
		    log(node.toString());
		    // recipients
		    Message message = new Message();
		    message.rec = new java.util.Hashtable<String,String>();
		    for (JsonNode recNode : node.get(0)) {
			message.rec.put(recNode.asText(), recNode.asText());
			log(recNode.asText());
		    }
		    message.from = node.get(1).asText();
		    message.timestamp = node.get(2).asLong();
		    message.sequenceno = node.get(3).asLong();
		    message.error = node.get(4).asText();
		    message.language = node.get(5).asText();
		    message.nick = node.get(6).asText();
		    message.message = node.get(7).asText();
		    log(message.toString());
		    return message;
		}
	}
	return null;
    }
    public String generate() {
	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	try {
		JsonFactory factory = new JsonFactory();
		JsonGenerator generator = factory.createGenerator(stream);
		generator.writeStartArray();
		generator.writeStartArray();
		java.util.Iterator<String> i = this.rec.keySet().iterator();
		while (i.hasNext()) {
			String to = i.next();
			generator.writeString(to);
		}
		generator.writeEndArray();
		generator.writeString(this.from);
		generator.writeNumber(this.timestamp);
		generator.writeNumber(this.sequenceno);
		generator.writeString(this.error);
		generator.writeString(this.language);
		generator.writeString(this.nick);
		// generator.writeNumber(this.sequencehigh);
		generator.writeString(this.message);
		generator.writeEndArray();
		generator.close();
	} catch (IOException e) {
		e.printStackTrace(System.err);
	}
	return stream.toString();
    }
}
