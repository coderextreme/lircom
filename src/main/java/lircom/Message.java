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
import java.io.PrintStream;
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
    public long error = 0;
    public String language = "en";
    public String application = "Chat";
    public static String thisApplication = null;
    long sequenceno = 0;
    private static long sequencehigh = 0;
    /** Creates a new instance of Message */
    static private PrintStream logStream = System.err;
    private static void log(String message) {
	    logStream.println("Message: "+message);
    }
    private static void log(Exception e) {
	    e.printStackTrace(logStream);
    }
    public Message(java.util.Hashtable<String,String> rec, String nick, String message, String lang) {
	this.rec = rec;
        this.from = "";
        this.nick = nick;
	this.message = message;
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
    public Message(String to, String nick, long error, String message, String lang) {
	this(to, nick, message, lang);
	this.error = error;
    }
    public Message(String to, String nick, String message, String lang, String application) {
	this(to, nick, 0L, message, lang);
	this.application = application;
    }
    private Message() {
        this.timestamp = System.currentTimeMillis();
    }
    static public Message parse(String line) {
	try {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(line);
		Message m = Message.parse(node);
		/*
		if (m != null && m.application != null && thisApplication != null && !m.application.equals(thisApplication)) {
			log("Filter: "+thisApplication);
			log("Message application: "+m.application);
			return null;
		}
		*/
		return m;
	} catch (JsonProcessingException e) {
		log(e);
		return null;
	}
    }
    public String translate(String targetLanguage) {
	    log("language is "+ this.language);
	    log("target language is "+ targetLanguage);
	    log("input message "+ this.message);
	if (!this.language.equals("__") && !this.language.equals(targetLanguage)) {
		try {
			return BabelFish.translate(message, this.language, targetLanguage);
		} catch (Exception e) {
			log(e);
		}
	}
	return this.message;
    }
    static public Message parse(JsonNode node) {
	if (node != null && !"".equals(node.toString().trim())) {
		if (node.isArray()) {
		    // recipients
		    Message message = new Message();
		    message.rec = new java.util.Hashtable<String,String>();
		    for (JsonNode recNode : node.get(0)) {
			message.rec.put(recNode.asText(), recNode.asText());
			log("Recipient: "+recNode.asText());
		    }
		    message.from = node.get(1).asText();
		    message.timestamp = node.get(2).asLong();
		    message.sequenceno = node.get(3).asLong();
		    message.error = node.get(4).asLong();
		    message.language = node.get(5).asText();
		    message.nick = node.get(6).asText();
		    message.application = node.get(7).asText();
		    message.message = node.get(8).asText();
		    log("Incoming: "+node.toString());
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
		generator.writeNumber(this.error);
		generator.writeString(this.language);
		generator.writeString(this.nick);
		// generator.writeNumber(this.sequencehigh);
		generator.writeString(this.application);
		generator.writeString(this.message);
		generator.writeEndArray();
		generator.close();
	} catch (IOException e) {
		log(e);
	}
	return stream.toString();
    }
}
