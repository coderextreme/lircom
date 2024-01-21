/*
 * ClientOnServer.java
 *
 * Created on February 13, 2005, 7:21 PM
 */

package lircom;

import java.net.*;
import java.util.*;
import java.io.*;

public class ClientOnServer extends Thread implements Errors {
	static Hashtable<String,String> errors = null;
	protected InputStream input;
	protected PrintWriter output;
	protected static Hashtable<Long,ClientOnServer> clients = new Hashtable<Long,ClientOnServer>();
	long clientno;
	String addressportclient;
	static long cs = 0;
        private String clientnick = "ClientOnServer";
        PossibleConnection pcon = null;
        public ClientOnServer() throws Exception {
	    synchronized (clients) {
		    clientno = System.currentTimeMillis()+cs;
		    cs++;
	    }
            clients.put(clientno, this);
            if (errors == null) {
		errors = new Hashtable<String,String>();
		errors.put(K100, V100);
		//errors.put(K101, V101);
            }
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
                                        System.err.println((int)c+"_____________"+sb);
                                        System.err.flush();
					continue;
				}
				
				line = sb.toString();
                                sb = new StringBuffer();
                         */
                        BufferedReader br = new BufferedReader(new InputStreamReader(input));
                        while ((line = br.readLine()) != null) {
                                try {
                                    if (line.trim().equals("")) {
                                        continue;
                                    }
                                    // System.err.println(getNick()+" received in ClientOnServer "+line);
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
	public boolean processLine(String line) throws Exception {
		Message m = Message.parse(line);
		if (!seenMessage(m, server_messages)) {
			try {
				Hashtable rec = prepareToSend(m);
				send(m, rec);
			} catch (Message msge) {
				messageException(msge);
			}
			return true;
		} else {
			return false;
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
				System.err.println("Adding ALL Clients");
				newrec.put("*", "*"); // send to who is left
				rec = (Hashtable<Long,ClientOnServer>)(clients.clone());
				// rec.remove(clientno);
				break;
			} else {
			    System.err.println("Not broadcast");
			    String recstr = to;
			    int aftercomma = 0;
			    String left = to;
			    do {
				//System.err.println("recstr is "+recstr);
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
			    System.err.println("Someone left "+left);
			    newrec.put(left, left); // send to who is left
			    System.err.println("recstr is "+recstr);
			    int colon = recstr.lastIndexOf(":");
			    if (colon >= 0) {
			    	recstr = recstr.substring(colon+1);
			    }
			    if (recstr.trim().equals("")) {
			    	return new Hashtable();
			    }
			    System.err.println("recstr is "+recstr);
			    ClientOnServer r = (ClientOnServer)clients.get(Long.valueOf(recstr));
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
		//System.err.println("Receiving in core client on server "+line);
                try {
                    if (output != null) {
                        output.println(line);
                        output.flush();
                    } else {
                        System.err.println("output is null..."+line);
                    }
                } catch (Exception e) {
                    try {
                        close();
                    } catch (Exception ex) {
                            System.err.println("Error closing "+ex.getMessage());
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
                            //System.err.println("Official send:");
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
                System.err.println("No recipient found in message");
            }                 
        }
        static Hashtable<String, Message> server_messages = new Hashtable<String, Message>();
        public boolean seenMessage(Message m, Hashtable<String, Message> messages) {
            String umsg = m.timestamp+","+m.sequenceno+","+m.nick;
            Iterator i = messages.keySet().iterator();
            Vector<String> removes = new Vector<String>();
            while (i.hasNext()) {
                String sumsg = (String)i.next();
                //System.err.println("sumsg "+sumsg+" umsg "+umsg);
                if (umsg.equals(sumsg)) {
                    System.err.println("Found "+umsg+" on "+addressportclient+"  already not sending");
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
        public synchronized void send(Message m) throws Exception {
		// System.err.println(getNick()+" sending to server "+m.generate());
		send(m.generate());
	}
        public synchronized void send(Message m, ClientOnServer c) throws Exception {
	    // System.err.println(getNick()+" writing to "+c.getNick()+" "+m.generate());
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
