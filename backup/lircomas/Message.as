/*
 * Message.java
 *
 * Created on February 19, 2005, 12:57 PM
 */

package lircom {
/**
 *
 * @author carlsonj
 */
public class Message extends Throwable {
    var from:String= "";
    public var rec:java.util.Hashtable= new java.util.Hashtable();
    var timestamp:Number= 0; // don't want to make public, but have to so that flooding can be done
    public var nick:String= "";
    public var message:String= "";
    public var error:String= "";
    public var language:String= "en";
    var sequenceno:Number= 0;
    private static var sequencehigh:Number= 0;
    /** Creates a new instance of Message */

    public function Message(rec:java.util.Hashtable, nick:String, message:String, lang:String) {
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
    public function Message(to:String, nick:String, message:String, lang:String) {
	this(java.util.Hashtable(null), nick, message, lang);
        rec = new java.util.Hashtable();
	rec.put(to, to);
    }
    public function Message(to:String, nick:String, error:String, message:String, lang:String) {
	this(to, nick, message, lang);
	this.error = error;
    }
    public function stripCRNL():void {
	// don't process messages with newlines, remove everything following the
	// newline
	var nl:int= message.indexOf("\n");
	if (nl >= 0) {
		message = this.message.substring(0, nl);
	}
	var cr:int= message.indexOf("\r");
	if (cr >= 0) {
		message = message.substring(0, cr);
	}
    }
    private function Message() {
    }
    static public function parse(line:String):Message {
                System.err.println("Receiving "+line);
                var m:Message= new Message();
                //to 
        	var tb:int= line.indexOf("{");
		var toe:int= tb;
		var tob:int= line.indexOf("{", toe+1);
		while (toe+1== tob) {
		    toe = line.indexOf("}", tob+1);
		    var to:String= line.substring(tob+1,toe);
		    m.rec.put(to, to);
		    tob = line.indexOf("{", toe);
                }
		var te:int= line.indexOf("}", toe+1);
                //from
		var fb:int= line.indexOf("{", te+1);
		var fe:int= line.indexOf("}", fb+1);
		// timestamp
		var sb:int= line.indexOf("{", fe+1);
		var se:int= line.indexOf("}", sb+1);
                // sequence no
                var qb:int= line.indexOf("{", se+1);
                var qe:int= line.indexOf("}", qb+1);
		// error
		var eb:int= line.indexOf("{", qe+1);
		var ee:int= line.indexOf("}", eb+1);
                // language
                var lb:int= line.indexOf("{", ee+1);
                var le:int= line.indexOf("}", lb+1);
		// nick
		var nb:int= line.indexOf("{", le+1);
		var ne:int= line.indexOf("}", nb+1);
                // TODO handle nicks with braces in them
                if (fb >= 0&& fe >= 0) {
                    m.from = line.substring(fb+1, fe);
                }
                if (sb >= 0&& se >= 0) {
                    m.timestamp = Long.parseLong(line.substring(sb+1, se));
                }
                if (qb >= 0&& qe >= 0) {
                    m.sequenceno = Long.parseLong(line.substring(qb+1, qe));
                }
                if (eb >= 0&& ee >= 0) {
                    m.error = line.substring(eb+1, ee);
                }
                if (lb >= 0&& le >= 0) {
                    m.language = line.substring(lb+1, le);
                }
                if (nb >= 0&& ne >= 0) {
                    m.nick = line.substring(nb+1, ne);
                }
                if (ne >= 0) {
                    m.message = line.substring(ne+1);
                }
		m.stripCRNL();
                return m;
    }
    public function generate():String {
        var sb:StringBuffer= new StringBuffer();
        sb.append("{");
	var i:java.util.Iterator= rec.keySet().iterator();
	while (i.hasNext()) {
		var to:String= String(i.next());
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
    public function translate(targetLanguage:String):void {
	    System.err.println("language is "+ language);
	    System.err.println("target language is "+ targetLanguage);
	    System.err.println("message "+ message);
	if (!language.equals("__") && !language.equals(targetLanguage)) {
		try {
			message = BabelFish.translate(message, language, targetLanguage);
			language = targetLanguage;
		} catch (var e:Exception) {
			message = "Untranslated "+language+" "+message;
		}
	}
	    System.err.println("translated message is "+ message);
    }
}
}