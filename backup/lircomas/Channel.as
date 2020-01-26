package lircom {
import java.util.*;
import java.io.*;
import java.util.regex.*;

public class Channel extends Chat {
	var subscribers:Hashtable= new Hashtable();
	var subscribernicks:Hashtable= new Hashtable();
        var joincmd:String= "^[ \t]*enter[ \t]*$";
        var leavecmd:String= "^[ \t]*leave[ \t]*$";
        var helpcmd:String= "^[ \t]*help[ \t]*$";
        var whocmd:String= "^[ \t]*who[ \t]*$";
        var normalcmd:String= "^.*$";
        var helpmsg:String= "Use the command \"enter\" to join a room and the command \"leave\" to leave a room.  To find out who is in a room, type \"who\".  Be sure to have the channel selected on the right";
	static final var JOIN_ACTION:int= 0;
	static final var LEAVE_ACTION:int= 1;
	static final var WHO_ACTION:int= 2;
	static final var HELP_ACTION:int= 3;
	static final var NORMAL_ACTION:int= 4;
        public function Channel() throws Exception {
            try {
                setNick("Channel");
            } catch (var e:Exception) {
            }
        }
	public function addActions():void {
	    new Action(joincmd, JOIN_ACTION);
	    new Action(leavecmd, LEAVE_ACTION);
	    new Action(whocmd, WHO_ACTION);
	    new Action(helpcmd, HELP_ACTION);
	    new Action(normalcmd, NORMAL_ACTION);
	}
	public function Channel(is:InputStream, os:OutputStream) throws Exception {
		super(is, os);
		setNick("Channel");
	}
	var actions:Vector= new Vector();
	class Action {
		var pattern:Pattern;
		var action:int;
		public function Action(event:String, response:int) {
			pattern = Pattern.compile(event, Pattern.UNICODE_CASE|Pattern.CASE_INSENSITIVE);
			action = response;
			actions.add(this);
		}
	}
	public function fireActions(receivedMessage:Message):void throws Exception {
		// more than one action can fire at the same time
		var i:Iterator= actions.iterator();
		while (i.hasNext()) {
			var a:Action= Action(i.next());
			actionPerformed(a, receivedMessage);
		}
	}
	public function actionPerformed(a:Action, receivedMessage:Message):void throws Exception {
		System.err.println("Testing "+receivedMessage.message+" against "+a.pattern);
		var matcher:Matcher= a.pattern.matcher(receivedMessage.message);
		var sender:String= receivedMessage.from;
		System.err.println("Sender is "+sender);
		var nick:String= receivedMessage.nick;
		var msg:String= receivedMessage.message;
		var testrec:Hashtable= receivedMessage.rec;
		if (matcher.matches()) {
			System.err.println(receivedMessage.message+" Matches "+a.pattern);
			switch (a.action) {
			case LEAVE_ACTION:
				if (subscribers.get(sender) != null) {
					subscribers.remove(sender);
					subscribernicks.remove(sender);
				}
				break;
			case JOIN_ACTION:
				subscribers.put(sender, sender);
				subscribernicks.put(sender, nick);
				break;
			case HELP_ACTION:
				{
				var m:Message= new Message(sender, getNick(), helpmsg, "en");
				try {
					// send back to sender
					var rec:Hashtable= prepareToSend(m);
					send(m, rec);
				} catch (var msge:Message) {
					messageException(msge);
				} catch (var e:Exception) {
				}
				}
				break;
			case WHO_ACTION:
				{
				var subs:StringBuffer= new StringBuffer();
				var i:Iterator= subscribernicks.keySet().iterator();
				while (i.hasNext()) {
					var sub:String= i.next().toString();
					subs.append(" ");
					subs.append(subscribernicks.get(sub));
				}
				var m:Message= new Message(sender, getNick(), subs.toString(), "__");
				try {
					// send back to sender
					var rec:Hashtable= prepareToSend(m);
					send(m, rec);
				} catch (var msge:Message) {
					messageException(msge);
				} catch (var e:Exception) {
				}
				}
				break;
			case NORMAL_ACTION:
				if (testrec.get("*") == null) {
					send(subscribers, receivedMessage, sender);
				}
				break;
			}
		}
	}
	public function processLine(line:String):Boolean throws Exception {
		// System.err.println(getNick()+" received "+line);
                // super.receive(line);
		var b:Boolean= super.processLine(line);
		if (b) {
			System.err.println("Processing "+line);
			var receivedMessage:Message= Message.parse(line);
			fireActions(receivedMessage);
		} else {
			System.err.println("Message found");
		}
		return b;
	}
	public function send(subscribers:Hashtable, m:Message, sender:String):void throws Exception {
/*
		Iterator i = subscribers.keySet().iterator();
		while (i.hasNext()) {
                    String sub = i.next().toString();
                    if (!sub.equals(sender) && !sub.equals(getAddressPortClient()) && !sender.equals(getAddressPortClient())) {
                        m.to = sub;
*/
			var subs:Hashtable= Hashtable(subscribers.clone());
			subs.remove(sender);
			m = new Message(subs, m.nick, m.message, m.language);
			try {
			    var rec:Hashtable= prepareToSend(m);
                            if (rec == null || rec.size() == 0) {
                                throw new Exception("No such recipient");
                            }
                            send(m, rec);
			} catch (var msge:Message) {
				messageException(msge);
			} catch (var e:Exception) {
/*
                            subscribers.remove(sub);
                            subscribernicks.remove(sub);
*/
			}
/*
                    }
		}
*/
	}
}
}