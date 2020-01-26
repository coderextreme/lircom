package lircom {
// screen scraper written in HTTPUNIT for the runestone
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import com.meterware.httpunit.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;

public class Runestone extends Thread implements ActionListener, SendCommandInterface {
    var jtf:JTextField= new JTextField();
    var usertf:JTextField= new JTextField();
    var pass:JPasswordField= new JPasswordField();
    var jb:JButton= new JButton("Connect to website");
    var id:String= "0";
    var bid:int= 1;
    var room:String= "1";
    var user:String= "0";
    var trackerconversation:WebConversation= new WebConversation();
    var userMap:HashMap= new HashMap();
    var userColor:HashMap= new HashMap();
    var roomMap:HashMap= new HashMap();
    var jf:JFrame= new JFrame();
    var chat:ReceiveChatInterface;
    public function Runestone(chat:ReceiveChatInterface, nick:String) throws Exception {
	    	this.chat = chat;
		var jp:JPanel= new JPanel();
		var jl:JLabel= new JLabel("Website");
		
		var url:String= "http://therunestone.org/valhalla/getxml.php";
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
			public function windowClosing(e:WindowEvent):void {
				jf.setVisible(false);
			}
		});
    }
    public function printDocument(d:Element):void throws Exception {
		var input:Source= new DOMSource(d);
		var trans:Transformer= TransformerFactory.newInstance().newTransformer();
		trans.setOutputProperty("indent", "yes");
		var result:Result= new StreamResult(System.out);
		trans.transform(input, result);
    }
    public function getDocumentFromResponse(response:WebResponse):Document throws Exception {
		var is:StringReader= new StringReader(response.getText());
		var f:DocumentBuilderFactory= DocumentBuilderFactory.newInstance();
		var db:DocumentBuilder= f.newDocumentBuilder();
		var d:Document= db.parse(new InputSource(is));
		return d;
    }
    public function actionPerformed(ae:ActionEvent):void {
	try {
			jf.setVisible(false);
			if (chat != null) {
				chat.setSendCommandInterface(this);
			}
			trackerconversation.setAuthorization(usertf.getText().trim(), new String(pass.getPassword()).trim());
			System.err.println("Setting timezone");
			var req:WebRequest= new PostMethodWebRequest(jtf.getText());
			req.setParameter("sendAndLoad", "[type Function]");
			req.setParameter("tz", "0"); // time zone
			req.setParameter("b", Integer.toString(bid)); // last item read from DB
			req.setParameter("c", "tzset"); // command set timezone
			req.setParameter("id", id); // session unique id?
			System.err.println(req.getQueryString());
			var response:WebResponse= trackerconversation.getResponse( req );
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

	} catch (var e:Exception) {
		e.printStackTrace();
	}
    }
    public function msgEntry():void throws Exception {
	var br:BufferedReader= new BufferedReader(new InputStreamReader(System.in));
	var line:String= null;
	while ((line = br.readLine()) != null) {
		msgSend(line);
	}
    }
    public function msgSend(line:String, recip:String):void {
	    msgSend(line); // todo, for now, send to everyone
    }
    public function getUser():String {
	    return usertf.getText();
    }
    public function msgSend(line:String):void {
	try  {

			var req:WebRequest= new PostMethodWebRequest(jtf.getText());
			req.setParameter("sendAndLoad", "[type Function]");
			req.setParameter("a", "undefined"); // ???
			req.setParameter("r", "1"); // room  to talk in
			req.setParameter("b", Integer.toString(bid)); // last item read from DB
			req.setParameter("c", "msg"); // command send message
			req.setParameter("u", "0"); // user (everyone I guess)
			req.setParameter("t", line); // user entry
			req.setParameter("id", id); // session unique id?
			System.err.println(req.getQueryString());
			var response:WebResponse= trackerconversation.getResponse( req );
			handleResponse(response);
	} catch (var e:Exception) {
		e.printStackTrace();
	}
    }
    public function handleResponse(response:WebResponse):void throws Exception {
		var d2:Document= getDocumentFromResponse(response);
		var d:Element= d2.getDocumentElement();
		id = d.getAttribute("id");
		printDocument(d);
		var nl:NodeList= d.getChildNodes();
		for (var ni:int= 0; ni < nl.getLength(); ni++) {
			var n:Node= nl.item(ni);
			if (!(n instanceof Element)) {
				continue;
			}
			var el:Element= Element(n);
			var idid:String= el.getAttribute("id");
			if (idid != null && !idid.trim().equals("")) {
				var temp:int= Integer.parseInt(idid);
				if (temp >= bid) {
					bid = temp+1;
					System.err.println("Setting bid to "+bid);
				}
			}
		}
		// assume avatar changes come first
		nl = d.getElementsByTagName("ravt"); // avatar
		for (var ni:int= 0; ni < nl.getLength(); ni++) {
			var ravt:Element= Element(nl.item(ni));
		}
		nl = d.getElementsByTagName("msg");
		for (var ni:int= 0; ni < nl.getLength(); ni++) {
			var msg:Element= Element(nl.item(ni));
			msg(msg);
		}
		nl = d.getElementsByTagName("rmu"); // remove user
		for (var ni:int= 0; ni < nl.getLength(); ni++) {
			var rmu:Element= Element(nl.item(ni));
			var userid:String= rmu.getAttribute("u");
			System.err.println(userMap.get(userid)+" left!");
			if (chat != null) {
				// chat.removeChatter((String)userMap.get(userid));
				// TODO do room
				chat.receiveLeave("", String(userMap.get(userid)));
			}
			userMap.remove(userid);
		}
		nl = d.getElementsByTagName("mvu"); // move user
		for (var ni:int= 0; ni < nl.getLength(); ni++) {
			var rmu:Element= Element(nl.item(ni));
			var userid:String= rmu.getAttribute("u");
			var roomid:String= rmu.getAttribute("r");
			System.err.println(userMap.get(userid)+" moved to room "+roomMap.get(roomid)+"!");
		}
		nl = d.getElementsByTagName("adu"); // add user
		for (var ni:int= 0; ni < nl.getLength(); ni++) {
			adu(Element(nl.item(ni)));
		}
		nl = d.getElementsByTagName("adr"); // add room
		for (var ni:int= 0; ni < nl.getLength(); ni++) {
			adr(Element(nl.item(ni)));
		}
		nl = d.getElementsByTagName("uclc"); // user color code
		for (var ni:int= 0; ni < nl.getLength(); ni++) {
			uclc(Element(nl.item(ni)));
		}
		nl = d.getElementsByTagName("lin"); // login in ???
		for (var ni:int= 0; ni < nl.getLength(); ni++) {
			var lin:Element= Element(nl.item(0));
			room = lin.getAttribute("r");
			user = lin.getAttribute("u");
			System.err.println("room "+room+" user "+user);
		}
    }
    public function run():void {
	    try {

		    for(;;) {
			var req:WebRequest= new PostMethodWebRequest(jtf.getText());
			req.setParameter("sendAndLoad", "[type Function]");
			req.setParameter("b", Integer.toString(bid)); // last item read from DB
			req.setParameter("c", "msgl"); // command message load
			req.setParameter("id", id); // session unique id?
			System.err.println(req.getQueryString());
			var response:WebResponse= trackerconversation.getResponse( req );
			handleResponse(response);
			Thread.sleep(3000);
		    }
	} catch (var e:Exception) {
		e.printStackTrace();
	}
			    
    }
    public function adu(adu:Element):void { // add user
		var userid:String= adu.getAttribute("u");
		var textnodes:NodeList= adu.getChildNodes();
		for (var nt:int= 0; nt < textnodes.getLength(); nt++) {
			var textn:Node= textnodes.item(nt);
			var username:String= textn.getNodeValue();
			System.err.println("User "+userid+" = "+username);
			userMap.put(userid, username);
			if (chat != null) {
				try {
					// chat.addChatter("flashchat:"+userid, username);
					chat.receivePresence("flashchat:", "", username);
				} catch (var e:Exception) {
					e.printStackTrace();
				}
			}
		}
    }
    public function uclc(uclc:Element):void { // user color code
		var userid:String= uclc.getAttribute("u");
		var textnodes:NodeList= uclc.getChildNodes();
		for (var nt:int= 0; nt < textnodes.getLength(); nt++) {
			var textn:Node= textnodes.item(nt);
			var color:String= textn.getNodeValue();
			color = "#"+Integer.toHexString(Integer.parseInt(color));
			System.err.println("Color "+userid+" = "+color);
			userColor.put(userid, color);
		}
    }
    public function adr(adr:Element):void { // add room
		var roomid:String= adr.getAttribute("r");
		var textnodes:NodeList= adr.getChildNodes();
		for (var nt:int= 0; nt < textnodes.getLength(); nt++) {
			var textn:Node= textnodes.item(nt);
			var roomname:String= textn.getNodeValue();
			System.err.println("Room "+roomid+" = "+roomname);
			roomMap.put(roomid, roomname);
		}
    }
    public function msg(msg:Element):void { // message sent
		var userid:String= msg.getAttribute("u");
		var n:Node= msg.getFirstChild();
		if (chat != null) {
			// chat.say((String)userMap.get(userid), "says", n.getNodeValue(), (String)userColor.get(userid));
			chat.receive(String(userMap.get(userid)), n.getNodeValue(), String(userColor.get(userid)));
		}
    }
    static public function main(String args[]):void throws Exception {
	var ss:Runestone= new Runestone(null, "Guest");
    }
}
}