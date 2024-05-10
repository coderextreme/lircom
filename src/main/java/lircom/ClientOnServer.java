/*
 * ClientOnServer.java
 *
 * Created on February 13, 2005, 7:21 PM
 */

package lircom;

import java.net.*;
import java.util.*;
import java.io.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientOnServer extends Thread implements Errors {
	protected InputStream input;
	protected PrintWriter output;
	protected static Hashtable<Long,ClientOnServer> clients = new Hashtable<Long,ClientOnServer>();
	long clientno;
	String addressportclient;
	static long cs = 0;
        private String clientnick = "ClientOnServer";
        PossibleConnection pcon = null;
	private PrintStream logStream = System.err;
	private void log(String message) {
		logStream.println(getNick()+": "+message);
	}
        public ClientOnServer() throws Exception {
	    synchronized (clients) {
		    clientno = System.currentTimeMillis()+cs;
		    cs++;
	    }
            clients.put(clientno, this);
            addressportclient = getLocation();
        }
        public String getAddressPortClient() {
            return addressportclient;
        }
        public void setNick(String nick) throws Exception {
            clientnick = nick;
            addressportclient = getLocation();
        }
        public String getNick() {
                return clientnick;
        }
        public void setInputStream(InputStream is) {
                input = is;
        }
        public void setOutputStream(OutputStream os) {
            	output = new PrintWriter(os);
        }
	public ClientOnServer(InputStream is, OutputStream os) throws Exception {
                this();
		setInputStream(is);
                setOutputStream(os);
	}
        public ClientOnServer(String host, int port) throws Exception {
            this();
            Socket client = new Socket(host, port);
            setInputStream(client.getInputStream());
            setOutputStream(client.getOutputStream());
        }
        public void connect(String host, int port) throws Exception {
            Socket client = new Socket(host, port);
            setInputStream(client.getInputStream());
            setOutputStream(client.getOutputStream());
        }
	// for actual clients agent on the server
	public ClientOnServer(Socket client) throws Exception {
                this(client.getInputStream(), client.getOutputStream());
	}
        public void close() throws Exception {
            try {
                if (input != null) {
                    input.close();
                }
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            clients.remove(clientno);
        }
	public void run() {
		try {
			String line = null;
                        /*
			int c = -1;
			StringBuffer sb = new StringBuffer();
			while (input != null && (c = input.read()) != -1) {
                                if (c == '\0') {
                                    continue; // discard nulls for now
                                }
				if (c != '\n') {
					sb.append((char)c);
                                        log((int)c+"_____________"+sb);
                                        logStream.flush();
					continue;
				}
				
				line = sb.toString();
                                sb = new StringBuffer();
                         */
			/*
			JsonNode node = null;
			do {
				ObjectMapper mapper = new ObjectMapper();
				try {
					node = mapper.readTree(input);
				} catch (SocketException disc) {
					disc.printStackTrace(logStream);
					// reconnect();
					node = null;
				}
				if (node != null && !"".equals(node.toString().trim())) {
					log(node.toString());
					processNode(node);
				}
			} while (node != null);
			*/
                        BufferedReader br = new BufferedReader(new InputStreamReader(input));
                        while ((line = br.readLine()) != null) {
                                try {
                                    if (line.trim().equals("")) {
                                        continue;
                                    }
                                    // log("received in ClientOnServer "+line);
				    processLine(line);
                                } catch (ClientException ce) {
                                    ce.printStackTrace();
                                }
			
			}
			if (input != null) {
				input.close();
			}
			if (output != null) {
				output.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		clients.remove(this.clientno);
	}
	public Message processNode(JsonNode node) throws Exception {
		if (node != null) {
			log("processing node "+node);
			Message m = Message.parse(node);
			return processMessage(m);
		} else {
			return null;
		}
	}
	public Message processLine(String line) throws Exception {
		if (line != null) {
			// log("processing line "+line);
			/*
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(line);
			return processNode(node);
			*/
			Message m = Message.parse(line);
			return processMessage(m);
		} else {
			return null;
		}
	}
	public Message processMessage(Message m) throws Exception {
		if (!seenMessage(m, server_messages)) {
			log("not seen message "+m.message);
			try {
				Hashtable rec = prepareToSend(m);
				send(m, rec);
			} catch (Message msge) {
				messageException(msge);
			}
			return m;
		} else {
			return null;
		}
	}
        
	public Hashtable prepareToSend(Message m) throws Exception, Message {
		// add from this client
		String from = m.from;
		prependFrom(m);
		Hashtable<Long,ClientOnServer> rec = new Hashtable<Long,ClientOnServer>(); // hashtable of client #s
		Iterator i = m.rec.keySet().iterator();
		Hashtable<String,String> newrec = new Hashtable<String,String>();
		while (i.hasNext()) { // go through existing list
			String to = (String)i.next();
			if (to.equals("*")) {
				log("Adding ALL Clients");
				newrec.put("*", "*"); // send to who is left
				rec = (Hashtable<Long,ClientOnServer>)(clients.clone());
				// rec.remove(clientno);
				break;
			} else {
			    log("Not broadcast");
			    String recstr = to;
			    int aftercomma = 0;
			    String left = to;
			    do {
				//log("recstr is "+recstr);
				int comma = to.indexOf(",", aftercomma);
				
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
			    log("Someone left "+left);
			    newrec.put(left, left); // send to who is left
			    log("recstr is "+recstr);
			    int colon = recstr.lastIndexOf(":");
			    if (colon >= 0) {
			    	recstr = recstr.substring(colon+1);
			    }
			    if (recstr.trim().equals("")) {
			    	return new Hashtable();
			    }
			    log("recstr is "+recstr);
			    ClientOnServer r = (ClientOnServer)clients.get(Long.valueOf(recstr));
			    if (r != null) {
			    	log("Recipient is "+recstr);
			    	rec.put(r.clientno, r);
			    } else {
			    	log("Recipient not found "+recstr);
			    }
			}
		}
		m.rec = newrec;
		return rec;
	}
        public void prependFrom(Message m) {
            if (m.from.length() > 0) {
                if (m.from.indexOf(getAddressPortClient()) < 0) {
                    m.from = getAddressPortClient()+","+m.from;
                }
            } else {
                m.from = getAddressPortClient();
            }
        }
	public void send(String line) throws Exception {
		//log("Receiving in core client on server "+line);
                try {
                    if (output != null) {
                        output.println(line);
                        output.flush();
                    } else {
                        log("output is null..."+line);
                    }
                } catch (Exception e) {
                    try {
                        close();
                    } catch (Exception ex) {
                            log("Error closing "+ex.getMessage());
                    }
                    throw e;
                }
	}
        public void send(Message m, Hashtable recipient) throws Exception {
            if (recipient != null && recipient.size() > 0) {
                Iterator i = recipient.keySet().iterator();
                ClientException ce = new ClientException();
                while (i.hasNext()) {
                        Long ci = (Long)i.next();
                        ClientOnServer c = (ClientOnServer)clients.get(ci);
                        try {
                            //log("Official send:");
			    if (c != this) {
				    send(m, c);
			    }
                        } catch (Exception e) {
                            ce.add(ci);
                        }
                }
                if (ce.doThrow()) {
                        throw ce;
                }
            } else {
                log("No recipient found in message");
            }                 
        }
        static Hashtable<String, Message> server_messages = new Hashtable<String, Message>();
        public boolean seenMessage(Message m, Hashtable<String, Message> messages) {
	    if (m == null) {
		    log("Message m is null in seenMessage");
		    return true;
	    }
            String umsg = m.timestamp+","+m.sequenceno+","+m.nick+","+getNick();
            Iterator i = messages.keySet().iterator();
            Vector<String> removes = new Vector<String>();
            while (i.hasNext()) {
                String sumsg = (String)i.next();
                //log("sumsg "+sumsg+" umsg "+umsg);
                if (umsg.equals(sumsg)) {
                    log("Found "+umsg+" on "+addressportclient+"  already not sending");
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
                log("Removing "+sumsg);
                messages.remove(sumsg);
            }
            log("Saving "+umsg);
            messages.put(umsg, m);
            return false;
        }
        public synchronized void send(Message m) throws Exception {
		// log("sending to server "+m.generate());
		send(m.generate());
	}
        public synchronized void send(Message m, ClientOnServer c) throws Exception {
	    // log("writing to "+c.getNick()+" "+m.generate());
            c.send(m.generate());
        }
        public String getLocation() throws Exception {
		Enumeration nis = NetworkInterface.getNetworkInterfaces();
		while (nis.hasMoreElements()) {
			NetworkInterface ni = (NetworkInterface)nis.nextElement();
			if (!ni.getName().equals("lo")) {
				Enumeration ias = ni.getInetAddresses();
				while (ias.hasMoreElements()) {
					InetAddress ia = (InetAddress)ias.nextElement();
                                        // TODO remove getNick()
					// return ia+":"+getNick()+":"+clientno;
					return ia+":"+clientno;
				}
			}
		}
		return null;
	}
	public void messageException(Message m) {
		try {
			Hashtable rec = prepareToSend(m);
			send(m, rec);
		} catch (Message msge) {
			msge.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
