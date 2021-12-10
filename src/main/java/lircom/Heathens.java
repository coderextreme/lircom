package lircom;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

// screen scraper written in HTTPUNIT for the runestone
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.meterware.httpunit.*;
import com.meterware.httpunit.WebResponse;
import com.meterware.httpunit.WebConversation;

public class Heathens extends Thread implements ActionListener, SendCommandInterface {
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
    public Heathens(ReceiveChatInterface chat, String nick) throws Exception {
	    	this.chat = chat;
		JPanel jp = new JPanel();
		JLabel jl = new JLabel("Website");
		
		String url = "http://coderextreme.net/helloworld.php";
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
    public String getDocumentFromResponse(WebResponse response) throws Exception {
		return response.getText();
    }
    public void actionPerformed(ActionEvent ae) {
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
		// System.err.println(usertf.getText());
	    return usertf.getText();
    }
    public void msgSend(String line) {
	try  {
			// System.err.println("Posting");


	    	    	HttpUnitOptions.setScriptingEnabled(false);
			WebRequest req = new PostMethodWebRequest(jtf.getText());
			req.setParameter("nick", getUser()); // user (everyone I guess)
			req.setParameter("text", line); // user entry
			// System.err.println("Getting response");
			WebResponse response = trackerconversation.getResponse( req );
			// System.err.println("Handling response");
			handleResponse(response);
			// System.err.println("Handled");
	} catch (Exception e) {
		e.printStackTrace();
	}
    }
    public void handleResponse(WebResponse response) throws Exception {
		String s = getDocumentFromResponse(response);
		if (s != null && !s.trim().equals("")) {
			if (chat != null) {
				String [] lines = s.split("\n");
				for (String line : lines) {
					chat.processLine(line);
				}
			} else {
				System.err.println(s);
			}
		}
    }
    public void run()     {
	    try {
	    	    HttpUnitOptions.setScriptingEnabled(false);
		    for(;;) {
			WebRequest req = new PostMethodWebRequest(jtf.getText());
			req.setParameter("nick", getUser()); // user (everyone I guess)
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
			// System.err.println("User "+userid+" = "+username);
			userMap.put(userid, username);
			if (chat != null) {
				try {
					chat.receivePresence("coderextreme:", "", username);
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
			// System.err.println("Color "+userid+" = "+color);
			userColor.put(userid, color);
		}
    }
    public void adr(Element adr) { // add room
		String roomid = adr.getAttribute("r");
		NodeList textnodes = adr.getChildNodes();
		for (int nt = 0; nt < textnodes.getLength(); nt++) {
			Node textn = textnodes.item(nt);
			String roomname = textn.getNodeValue();
			// System.err.println("Room "+roomid+" = "+roomname);
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
	Heathens ss = new Heathens(null, "Guest");
    }
}
