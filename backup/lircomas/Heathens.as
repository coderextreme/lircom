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

public class Heathens extends Thread implements ActionListener, SendCommandInterface {
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
    public function Heathens(chat:ReceiveChatInterface, nick:String) throws Exception {
	    	this.chat = chat;
		var jp:JPanel= new JPanel();
		var jl:JLabel= new JLabel("Website");
		
		var url:String= "http://coderextreme.net/helloworld.php";
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
    public function getDocumentFromResponse(response:WebResponse):String throws Exception {
		return response.getText();
    }
    public function actionPerformed(ae:ActionEvent):void {
	try {
			jf.setVisible(false);
			if (chat != null) {
				chat.setSendCommandInterface(this);
			}
			trackerconversation.setAuthorization(usertf.getText().trim(), new String(pass.getPassword()).trim());
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
		// System.err.println(usertf.getText());
	    return usertf.getText();
    }
    public function msgSend(line:String):void {
	try  {
			// System.err.println("Posting");

			var req:WebRequest= new PostMethodWebRequest(jtf.getText());
			req.setParameter("nick", getUser()); // user (everyone I guess)
			req.setParameter("text", line); // user entry
			// System.err.println("Getting response");
			var response:WebResponse= trackerconversation.getResponse( req );
			// System.err.println("Handling response");
			handleResponse(response);
			// System.err.println("Handled");
	} catch (var e:Exception) {
		e.printStackTrace();
	}
    }
    public function handleResponse(response:WebResponse):void throws Exception {
		var s:String= getDocumentFromResponse(response);
		if (s != null && !s.trim().equals("")) {
			if (chat != null) {
				var lines:Array= s.split("\n");
				for (String line : lines) {
					chat.processLine(line);
				}
			} else {
				System.err.println(s);
			}
		}
    }
    public function run():void {
	    try {

		    for(;;) {
			var req:WebRequest= new PostMethodWebRequest(jtf.getText());
			req.setParameter("nick", getUser()); // user (everyone I guess)
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
			// System.err.println("User "+userid+" = "+username);
			userMap.put(userid, username);
			if (chat != null) {
				try {
					chat.receivePresence("coderextreme:", "", username);
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
			// System.err.println("Color "+userid+" = "+color);
			userColor.put(userid, color);
		}
    }
    public function adr(adr:Element):void { // add room
		var roomid:String= adr.getAttribute("r");
		var textnodes:NodeList= adr.getChildNodes();
		for (var nt:int= 0; nt < textnodes.getLength(); nt++) {
			var textn:Node= textnodes.item(nt);
			var roomname:String= textn.getNodeValue();
			// System.err.println("Room "+roomid+" = "+roomname);
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
	var ss:Heathens= new Heathens(null, "Guest");
    }
}
}