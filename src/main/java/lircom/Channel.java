package lircom;

import java.util.*;
import java.io.*;
import java.util.regex.*;

public class Channel extends Chat {
	Hashtable subscribers = new Hashtable();
	Hashtable subscribernicks = new Hashtable();
        String joincmd = "^[ \t]*enter[ \t]*$";
        String leavecmd = "^[ \t]*leave[ \t]*$";
        String helpcmd = "^[ \t]*help[ \t]*$";
        String whocmd = "^[ \t]*who[ \t]*$";
        String normalcmd = "^.*$";
        String helpmsg = "Use the command \"enter\" to join a room and the command \"leave\" to leave a room.  To find out who is in a room, type \"who\".  Be sure to have the channel selected on the right";
	static final int JOIN_ACTION = 0;
	static final int LEAVE_ACTION = 1;
	static final int WHO_ACTION = 2;
	static final int HELP_ACTION = 3;
	static final int NORMAL_ACTION = 4;
        public Channel() throws Exception {
            try {
                setNick("Channel");
            } catch (Exception e) {
            }
        }
	public void addActions() {
	    new Action(joincmd, JOIN_ACTION);
	    new Action(leavecmd, LEAVE_ACTION);
	    new Action(whocmd, WHO_ACTION);
	    new Action(helpcmd, HELP_ACTION);
	    new Action(normalcmd, NORMAL_ACTION);
	}
	public Channel(InputStream is, OutputStream os) throws Exception {
		super(is, os);
		setNick("Channel");
	}
	Vector actions = new Vector();
	class Action {
		Pattern pattern;
		int action;
		public Action(String event, int response) {
			pattern = Pattern.compile(event, Pattern.UNICODE_CASE|Pattern.CASE_INSENSITIVE);
			action = response;
			actions.add(this);
		}
	}
	public void fireActions(Message receivedMessage) throws Exception {
		// more than one action can fire at the same time
		Iterator i = actions.iterator();
		while (i.hasNext()) {
			Action a = (Action)i.next();
			actionPerformed(a, receivedMessage);
		}
	}
	public void actionPerformed(Action a, Message receivedMessage) throws Exception {
		System.err.println("Testing "+receivedMessage.message+" against "+a.pattern);
		Matcher matcher = a.pattern.matcher(receivedMessage.message);
		String sender = receivedMessage.from;
		System.err.println("Sender is "+sender);
		String nick = receivedMessage.nick;
		String msg = receivedMessage.message;
		Hashtable testrec = receivedMessage.rec;
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
				Message m = new Message(sender, getNick(), helpmsg, "en");
				try {
					// send back to sender
					Hashtable rec = prepareToSend(m);
					send(m, rec);
				} catch (Message msge) {
					messageException(msge);
				} catch (Exception e) {
				}
				}
				break;
			case WHO_ACTION:
				{
				StringBuffer subs = new StringBuffer();
				Iterator i = subscribernicks.keySet().iterator();
				while (i.hasNext()) {
					String sub = i.next().toString();
					subs.append(" ");
					subs.append(subscribernicks.get(sub));
				}
				Message m = new Message(sender, getNick(), subs.toString(), "__");
				try {
					// send back to sender
					Hashtable rec = prepareToSend(m);
					send(m, rec);
				} catch (Message msge) {
					messageException(msge);
				} catch (Exception e) {
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
	public boolean processLine(String line) throws Exception {
		// System.err.println(getNick()+" received "+line);
                // super.receive(line);
		boolean b = super.processLine(line);
		if (b) {
			System.err.println("Processing "+line);
			Message receivedMessage = Message.parse(line);
			fireActions(receivedMessage);
		} else {
			System.err.println("Message found");
		}
		return b;
	}
	public void send(Hashtable subscribers, Message m, String sender) throws Exception {
/*
		Iterator i = subscribers.keySet().iterator();
		while (i.hasNext()) {
                    String sub = i.next().toString();
                    if (!sub.equals(sender) && !sub.equals(getAddressPortClient()) && !sender.equals(getAddressPortClient())) {
                        m.to = sub;
*/
			Hashtable subs = (Hashtable)subscribers.clone();
			subs.remove(sender);
			m = new Message(subs, m.nick, m.message, m.language);
			try {
			    Hashtable rec = prepareToSend(m);
                            if (rec == null || rec.size() == 0) {
                                throw new Exception("No such recipient");
                            }
                            send(m, rec);
			} catch (Message msge) {
				messageException(msge);
			} catch (Exception e) {
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
