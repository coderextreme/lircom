package lircom;

// screen scraper written in HTTPUNIT for the runestone
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import com.meterware.httpunit.*;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebConversation;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class Runestone extends Thread implements ActionListener, SendCommandInterface {
    JTextField jtf = new JTextField();
    JTextField usertf = new JTextField();
    JPasswordField pass = new JPasswordField();
    JButton jb = new JButton("Connect to website");
    String id = "0";
    int bid = 1;
    String room = "1";
    String user = "0";
    WebConversation trackerconversation = new WebConversation();
    HashMap<String,String> userMap = new HashMap<String,String>();
    HashMap<String,String> userColor = new HashMap<String,String>();
    HashMap<String,String> roomMap = new HashMap<String,String>();
    JFrame jf = new JFrame();
    ReceiveChatInterface chat;
    public Runestone(ReceiveChatInterface chat, String nick) throws Exception {
	    	this.chat = chat;
		JPanel jp = new JPanel();
		JLabel jl = new JLabel("Website");
		
		String url = "http://therunestone.org/valhalla/getxml.php";
		jtf.setText(url);
		jb.addActionListener(this);
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		jp.add(jl);
		jp.add(new JLabel("Chat location (register first) "));
		jp.add(jtf);
		jp.add(new JLabel("User"));
		usertf.setText(nick);
		jp.add(usertf);
		jp.add(new JLabel("Password"));
		jp.add(pass);
		jp.add(jb);
		jf.getContentPane().add(jp);
		jf.pack();
		jf.setVisible(true);
		jf.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				jf.setVisible(false);
			}
		});
    }
    public void printDocument(Element d) throws Exception {
		Source input = new DOMSource(d);
		Transformer trans = TransformerFactory.newInstance().newTransformer();
		trans.setOutputProperty("indent", "yes");
		Result result = new StreamResult(System.out);
		trans.transform(input, result);
    }
    public Document getDocumentFromResponse(WebResponse response) throws Exception {
		StringReader is = new StringReader(response.getText());
		DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = f.newDocumentBuilder();
		Document d = db.parse(new InputSource(is));
		return d;
    }
    public void actionPerformed(ActionEvent ae) {
	try {
			jf.setVisible(false);
			if (chat != null) {
				chat.setSendCommandInterface(this);
			}
			trackerconversation.setAuthorization(usertf.getText().trim(), new String(pass.getPassword()).trim());
			System.err.println("Setting timezone");
	    	    	HttpUnitOptions.setScriptingEnabled(false);
			WebRequest req = new PostMethodWebRequest(jtf.getText());
			req.setParameter("sendAndLoad", "[type Function]");
			req.setParameter("tz", "0"); // time zone
			req.setParameter("b", Integer.toString(bid)); // last item read from DB
			req.setParameter("c", "tzset"); // command set timezone
			req.setParameter("id", id); // session unique id?
			System.err.println(req.getQueryString());
			WebResponse response = trackerconversation.getResponse( req );
			handleResponse(response); // handle lout


			System.err.println("Logging in");
			req = new PostMethodWebRequest(jtf.getText());
			req.setParameter("sendAndLoad", "[type Function]");
			req.setParameter("tz", "0"); // time zone
			req.setParameter("l", "en"); // language
			req.setParameter("ps", new String(pass.getPassword())); // password
			req.setParameter("lg", usertf.getText()); // login
			req.setParameter("b", Integer.toString(bid)); // last item read from DB
			req.setParameter("c", "lin"); // command (login)
			req.setParameter("id", id); // unique id?
			req.setParameter("r", "1"); // room
			System.err.println(req.getQueryString());
			response = trackerconversation.getResponse( req );
			handleResponse(response);

			/*
			System.err.println("Moving to main room");
			req = new PostMethodWebRequest(jtf.getText());
			req.setParameter("sendAndLoad", "[type Function]");
			req.setParameter("r", "1"); // room  to move to
			req.setParameter("b", Integer.toString(bid)); // last item read from DB
			req.setParameter("c", "mvu"); // command move user between rooms
			req.setParameter("id", id); // session unique id?
			System.err.println(req.getQueryString());
			response = trackerconversation.getResponse( req );
			handleResponse(response);
			*/
			start();
			// if no GUI, use command line
			if (chat == null) {
				msgEntry();
			}

	} catch (Exception e) {
		e.printStackTrace();
	}
    }
    public void msgEntry() throws Exception {
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	String line = null;
	while ((line = br.readLine()) != null) {
		msgSend(line);
	}
    }
    public void msgSend(String line, String recip) {
	    msgSend(line); // todo, for now, send to everyone
    }
    public String getUser() {
	    return usertf.getText();
    }
    public void msgSend(String line) {
	try  {

	    	    	HttpUnitOptions.setScriptingEnabled(false);
			WebRequest req = new PostMethodWebRequest(jtf.getText());
			req.setParameter("sendAndLoad", "[type Function]");
			req.setParameter("a", "undefined"); // ???
			req.setParameter("r", "1"); // room  to talk in
			req.setParameter("b", Integer.toString(bid)); // last item read from DB
			req.setParameter("c", "msg"); // command send message
			req.setParameter("u", "0"); // user (everyone I guess)
			req.setParameter("t", line); // user entry
			req.setParameter("id", id); // session unique id?
			System.err.println(req.getQueryString());
			WebResponse response = trackerconversation.getResponse( req );
			handleResponse(response);
	} catch (Exception e) {
		e.printStackTrace();
	}
    }
    public void handleResponse(WebResponse response) throws Exception {
		Document d2 = getDocumentFromResponse(response);
		Element d = d2.getDocumentElement();
		id = d.getAttribute("id");
		printDocument(d);
		NodeList nl = d.getChildNodes();
		for (int ni = 0; ni < nl.getLength(); ni++) {
			Node n = nl.item(ni);
			if (!(n instanceof Element)) {
				continue;
			}
			Element el = (Element)n;
			String idid = el.getAttribute("id");
			if (idid != null && !idid.trim().equals("")) {
				int temp = Integer.parseInt(idid);
				if (temp >= bid) {
					bid = temp+1;
					System.err.println("Setting bid to "+bid);
				}
			}
		}
		// assume avatar changes come first
		nl = d.getElementsByTagName("ravt"); // avatar
		for (int ni = 0; ni < nl.getLength(); ni++) {
			Element ravt = (Element)nl.item(ni);
		}
		nl = d.getElementsByTagName("msg");
		for (int ni = 0; ni < nl.getLength(); ni++) {
			Element msg = (Element)nl.item(ni);
			msg(msg);
		}
		nl = d.getElementsByTagName("rmu"); // remove user
		for (int ni = 0; ni < nl.getLength(); ni++) {
			Element rmu = (Element)nl.item(ni);
			String userid = rmu.getAttribute("u");
			System.err.println(userMap.get(userid)+" left!");
			if (chat != null) {
				// chat.removeChatter((String)userMap.get(userid));
				// TODO do room
				chat.receiveLeave("", (String)userMap.get(userid));
			}
			userMap.remove(userid);
		}
		nl = d.getElementsByTagName("mvu"); // move user
		for (int ni = 0; ni < nl.getLength(); ni++) {
			Element rmu = (Element)nl.item(ni);
			String userid = rmu.getAttribute("u");
			String roomid = rmu.getAttribute("r");
			System.err.println(userMap.get(userid)+" moved to room "+roomMap.get(roomid)+"!");
		}
		nl = d.getElementsByTagName("adu"); // add user
		for (int ni = 0; ni < nl.getLength(); ni++) {
			adu((Element)nl.item(ni));
		}
		nl = d.getElementsByTagName("adr"); // add room
		for (int ni = 0; ni < nl.getLength(); ni++) {
			adr((Element)nl.item(ni));
		}
		nl = d.getElementsByTagName("uclc"); // user color code
		for (int ni = 0; ni < nl.getLength(); ni++) {
			uclc((Element)nl.item(ni));
		}
		nl = d.getElementsByTagName("lin"); // login in ???
		for (int ni = 0; ni < nl.getLength(); ni++) {
			Element lin = (Element)nl.item(0);
			room = lin.getAttribute("r");
			user = lin.getAttribute("u");
			System.err.println("room "+room+" user "+user);
		}
    }
    public void run()     {
	    try {

	    	    HttpUnitOptions.setScriptingEnabled(false);
		    for(;;) {
			WebRequest req = new PostMethodWebRequest(jtf.getText());
			req.setParameter("sendAndLoad", "[type Function]");
			req.setParameter("b", Integer.toString(bid)); // last item read from DB
			req.setParameter("c", "msgl"); // command message load
			req.setParameter("id", id); // session unique id?
			System.err.println(req.getQueryString());
			WebResponse response = trackerconversation.getResponse( req );
			handleResponse(response);
			Thread.sleep(3000);
		    }
	} catch (Exception e) {
		e.printStackTrace();
	}
			    
    }
    public void adu(Element adu) { // add user
		String userid = adu.getAttribute("u");
		NodeList textnodes = adu.getChildNodes();
		for (int nt = 0; nt < textnodes.getLength(); nt++) {
			Node textn = textnodes.item(nt);
			String username = textn.getNodeValue();
			System.err.println("User "+userid+" = "+username);
			userMap.put(userid, username);
			if (chat != null) {
				try {
					// chat.addChatter("flashchat:"+userid, username);
					chat.receivePresence("flashchat:", "", username);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
    }
    public void uclc(Element uclc) { // user color code
		String userid = uclc.getAttribute("u");
		NodeList textnodes = uclc.getChildNodes();
		for (int nt = 0; nt < textnodes.getLength(); nt++) {
			Node textn = textnodes.item(nt);
			String color = textn.getNodeValue();
			color = "#"+Integer.toHexString(Integer.parseInt(color));
			System.err.println("Color "+userid+" = "+color);
			userColor.put(userid, color);
		}
    }
    public void adr(Element adr) { // add room
		String roomid = adr.getAttribute("r");
		NodeList textnodes = adr.getChildNodes();
		for (int nt = 0; nt < textnodes.getLength(); nt++) {
			Node textn = textnodes.item(nt);
			String roomname = textn.getNodeValue();
			System.err.println("Room "+roomid+" = "+roomname);
			roomMap.put(roomid, roomname);
		}
    }
    public void msg(Element msg) { // message sent
		String userid = msg.getAttribute("u");
		Node n = msg.getFirstChild();
		if (chat != null) {
			// chat.say((String)userMap.get(userid), "says", n.getNodeValue(), (String)userColor.get(userid));
			chat.receive((String)userMap.get(userid), n.getNodeValue(), (String)userColor.get(userid));
		}
    }
    static public void main(String args[]) throws Exception {
	Runestone ss = new Runestone(null, "Guest");
    }
}
