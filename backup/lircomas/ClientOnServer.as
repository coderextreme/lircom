/*
 * ClientOnServer.java
 *
 * Created on February 13, 2005, 7:21 PM
 */

package lircom {
import java.net.*;
import java.util.*;
import java.io.*;

public class ClientOnServer extends Thread implements Errors {
	static var errors:Hashtable= null;
	protected var input:InputStream;
	protected var output:PrintWriter;
	protected static var clients:Hashtable= new Hashtable();
	var clientno:Long;
	var addressportclient:String;
	static var cs:Number= 0;
        private var clientnick:String= "ClientOnServer";
        var pcon:PossibleConnection= null;
        public function ClientOnServer() throws Exception {
	    synchronized (clients) {
		    clientno = new Long(System.currentTimeMillis()+cs);
		    cs++;
	    }
            clients.put(clientno, this);
            if (errors == null) {
		errors = new Hashtable();
		errors.put(K100, V100);
		//errors.put(K101, V101);
            }
            addressportclient = getLocation();
        }
        public function getAddressPortClient():String {
            return addressportclient;
        }
        public function setNick(nick:String):void throws Exception {
            clientnick = nick;
            addressportclient = getLocation();
        }
        public function getNick():String {
                return clientnick;
        }
        public function setInputStream(is:InputStream):void {
                input = is;
        }
        public function setOutputStream(os:OutputStream):void {
            	output = new PrintWriter(os);
        }
	public function ClientOnServer(is:InputStream, os:OutputStream) throws Exception {
                this();
		setInputStream(is);
                setOutputStream(os);
	}
        public function ClientOnServer(host:String, port:int) throws Exception {
            this();
            var client:Socket= new Socket(host, port);
            setInputStream(client.getInputStream());
            setOutputStream(client.getOutputStream());
        }
        public function connect(host:String, port:int):void throws Exception {
            var client:Socket= new Socket(host, port);
            setInputStream(client.getInputStream());
            setOutputStream(client.getOutputStream());
        }
	// for actual clients agent on the server
	public function ClientOnServer(client:Socket) throws Exception {
                this(client.getInputStream(), client.getOutputStream());
	}
        public function close():void throws Exception {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (var e:Exception) {
                e.printStackTrace();
            }
            clients.remove(clientno);
        }
	public function run():void {
		try {
			var line:String= null;
                        /*
			int c = -1;
			StringBuffer sb = new StringBuffer();
			while (input != null && (c = input.read()) != -1) {
                                if (c == '\0') {
                                    continue; // discard nulls for now
                                }
				if (c != '\n') {
					sb.append((char)c);
                                        System.err.println((int)c+"_____________"+sb);
                                        System.err.flush();
					continue;
				}
				
				line = sb.toString();
                                sb = new StringBuffer();
                         */
                        var br:BufferedReader= new BufferedReader(new InputStreamReader(input));
                        while ((line = br.readLine()) != null) {
                                try {
                                    if (line.trim().equals("")) {
                                        continue;
                                    }
                                    System.err.println(getNick()+" received "+line);
				    processLine(line);
                                } catch (var ce:ClientException) {
                                    ce.printStackTrace();
                                }
			
			}
			if (input != null) {
				input.close();
			}
			if (output != null) {
				output.close();
			}
		} catch (var e:Exception) {
			e.printStackTrace();
		}
		clients.remove(this.clientno);
	}
	public function processLine(line:String):Boolean throws Exception {
		var m:Message= Message.parse(line);
		if (!seenMessage(m, server_messages)) {
			try {
				var rec:Hashtable= prepareToSend(m);
				send(m, rec);
			} catch (var msge:Message) {
				messageException(msge);
			}
			return true;
		} else {
			return false;
		}
	}
        
	public function prepareToSend(m:Message):Hashtable throws Exception, Message {
		// add from this client
		var from:String= m.from;
		prependFrom(m);
		var rec:Hashtable= new Hashtable(); // hashtable of client #s
		var i:Iterator= m.rec.keySet().iterator();
		var newrec:Hashtable= new Hashtable();
		while (i.hasNext()) { // go through existing list
			var to:String= String(i.next());
			if (to.equals("*")) {
				System.err.println("Adding ALL Clients");
				newrec.put("*", "*"); // send to who is left
				rec = Hashtable(clients.clone());;
				// rec.remove(clientno);
				break;
			} else {
			    System.err.println("Not broadcast");
			    var recstr:String= to;
			    var aftercomma:int= 0;
			    var left:String= to;
			    do {
				//System.err.println("recstr is "+recstr);
				var comma:int= to.indexOf(",", aftercomma);
				
				if (comma > aftercomma) {
				    recstr = to.substring(aftercomma, comma);
				    aftercomma = comma+1;
				    left = to.substring(aftercomma);
				} else {
				    recstr = to.substring(aftercomma);
				    left = "";
				    break;
				}
			    } while (recstr.equals(getAddressPortClient()));
			    System.err.println("Someone left "+left);
			    newrec.put(left, left); // send to who is left
			    System.err.println("recstr is "+recstr);
			    var colon:int= recstr.lastIndexOf(":");
			    if (colon >= 0) {
			    	recstr = recstr.substring(colon+1);
			    }
			    if (recstr.trim().equals("")) {
			    	return new Hashtable();
			    }
			    System.err.println("recstr is "+recstr);
			    var r:ClientOnServer= ClientOnServer(clients.get(Long.valueOf(recstr)));
			    if (r != null) {
			    	System.err.println("Recipient is "+recstr);
			    	rec.put(r.clientno, r);
			    } else {
			    	System.err.println("Recipient not found "+recstr);
			    }
			}
		}
		m.rec = newrec;
		return rec;
	}
        public function prependFrom(m:Message):void {
            if (m.from.length() > 0) {
                if (m.from.indexOf(getAddressPortClient()) < 0) {
                    m.from = getAddressPortClient()+","+m.from;
                }
            } else {
                m.from = getAddressPortClient();
            }
        }
	public function send(line:String):void throws Exception {
		//System.err.println("Receiving in core client on server "+line);
                try {
                    if (output != null) {
                        output.println(line);
                        output.flush();
                    } else {
                        System.err.println("output is null..."+line);
                    }
                } catch (var e:Exception) {
                    try {
                        close();
                    } catch (var ex:Exception) {
                            System.err.println("Error closing "+ex.getMessage());
                    }
                    throw e;
                }
	}
        public function send(m:Message, recipient:Hashtable):void throws Exception {
            if (recipient != null && recipient.size() > 0) {
                var i:Iterator= recipient.keySet().iterator();
                var ce:ClientException= new ClientException();
                while (i.hasNext()) {
                        var ci:Long= Long(i.next());
                        var c:ClientOnServer= ClientOnServer(clients.get(ci));
                        try {
                            //System.err.println("Official send:");
			    if (c != this) {
				    send(m, c);
			    }
                        } catch (var e:Exception) {
                            ce.add(ci);
                        }
                }
                if (ce.doThrow()) {
                        throw ce;
                }
            } else {
                System.err.println("No recipient found in message");
            }                 
        }
        static var server_messages:Hashtable= new Hashtable();
        public function seenMessage(m:Message, messages:Hashtable):Boolean {
            var umsg:String= m.timestamp+","+m.sequenceno+","+m.nick;
            var i:Iterator= messages.keySet().iterator();
            var removes:Vector= new Vector();
            while (i.hasNext()) {
                var sumsg:String= String(i.next());
                //System.err.println("sumsg "+sumsg+" umsg "+umsg);
                if (umsg.equals(sumsg)) {
                    System.err.println("Found "+umsg+" on "+addressportclient+"  already not sending");
                    return true;
                }
                var comma:int= sumsg.indexOf(",");
                var msgtime:Number= Long.parseLong(sumsg.substring(0, comma));
                if (System.currentTimeMillis() - msgtime > 30000) { // save messages for 30 seconds
                    removes.add(sumsg);
                }
            }
            i = removes.iterator();
            while (i.hasNext()) {
                var sumsg:String= String(i.next());
                // System.err.println("Removing "+sumsg);
                messages.remove(sumsg);
            }
            messages.put(umsg, m);
            return false;
        }
        public synchronized function send(m:Message):void throws Exception {
		// System.err.println(getNick()+" sending to server "+m.generate());
		send(m.generate());
	}
        public synchronized function send(m:Message, c:ClientOnServer):void throws Exception {
	    // System.err.println(getNick()+" writing to "+c.getNick()+" "+m.generate());
            c.send(m.generate());
        }
        public function getLocation():String throws Exception {
		var nis:Enumeration= NetworkInterface.getNetworkInterfaces();
		while (nis.hasMoreElements()) {
			var ni:NetworkInterface= NetworkInterface(nis.nextElement());
			if (!ni.getName().equals("lo")) {
				var ias:Enumeration= ni.getInetAddresses();
				while (ias.hasMoreElements()) {
					var ia:InetAddress= InetAddress(ias.nextElement());
                                        // TODO remove getNick()
					// return ia+":"+getNick()+":"+clientno;
					return ia+":"+clientno;
				}
			}
		}
		return null;
	}
	public function messageException(m:Message):void {
		try {
			var rec:Hashtable= prepareToSend(m);
			send(m, rec);
		} catch (var msge:Message) {
			msge.printStackTrace();
		} catch (var e:Exception) {
			e.printStackTrace();
		}
	}
}
}