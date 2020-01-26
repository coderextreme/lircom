/*
 * Created on May 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lircom;

import javax.swing.JFrame;

import java.awt.GridBagLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JEditorPane;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import javax.swing.JList;
/**
 * @author carlsonj
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Lircom extends JFrame {

	private javax.swing.JPanel jContentPane = null;

	private JMenuBar jJMenuBar = null;
	private JMenu jMenu = null;
	/**
	 * This is the default constructor
	 */
	public Lircom(String title) {
		super(title);
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setJMenuBar(getJJMenuBar());
		this.setSize(300,200);
		this.setContentPane(getJContentPane());
		this.setTitle("JFrame");
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane() {
		if(jContentPane == null) {
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			jContentPane = new javax.swing.JPanel();
			jContentPane.setLayout(new GridBagLayout());
			gridBagConstraints2.gridx = 0;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.weighty = 1.0;
			gridBagConstraints2.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints2.ipady = 122;
			gridBagConstraints2.gridheight = -1;
			gridBagConstraints2.gridwidth = -1;
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 1;
			gridBagConstraints3.weightx = 1.0;
			gridBagConstraints3.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints3.ipadx = 288;
			gridBagConstraints3.gridheight = 0;
			gridBagConstraints3.gridwidth = 0;
			gridBagConstraints4.gridx = 1;
			gridBagConstraints4.gridy = 0;
			gridBagConstraints4.gridwidth = 0;
			gridBagConstraints4.weightx = 1.0;
			gridBagConstraints4.weighty = 1.0;
			gridBagConstraints4.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints4.ipady = 143;
			gridBagConstraints4.gridheight = -1;
			jContentPane.setMinimumSize(new java.awt.Dimension(800,600));
			jContentPane.setMaximumSize(new java.awt.Dimension(800,600));
			jContentPane.setPreferredSize(new java.awt.Dimension(800,600));
			jContentPane.add(getChat_Info_Area(), gridBagConstraints2);
			jContentPane.add(getChat_Text_Field(), gridBagConstraints3);
			jContentPane.add(getMember_List(), gridBagConstraints4);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jJMenuBar	
	 * 	
	 * @return javax.swing.JMenuBar	
	 */    
	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getJMenu());
		}
		return jJMenuBar;
	}
	/**
	 * This method initializes jMenu	
	 * 	
	 * @return javax.swing.JMenu	
	 */    
	private JMenu getJMenu() {
		if (jMenu == null) {
			jMenu = new JMenu();
			jMenu.setName("File");
		}
		return jMenu;
	}
    /**
     * @param args the command line arguments
     */
    static String[] args;
    private String nickname = "Guest";
	/**
	 * This method initializes jEditorPane	
	 * 	
	 * @return javax.swing.JEditorPane	
	 */    
	private JEditorPane getChat_Info_Area() {
		if (chat_Info_Area == null) {
			chat_Info_Area = new JEditorPane();
			chat_Info_Area.setContentType("text/html");
		}
		return chat_Info_Area;
	}
	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getChat_Text_Field() {
		if (chat_Text_Field == null) {
			chat_Text_Field = new JTextField();
		}
		return chat_Text_Field;
	}
	/**
	 * This method initializes jList	
	 * 	
	 * @return javax.swing.JList	
	 */    
	private JList getMember_List() {
		if (member_List == null) {
			member_List = new JList();
			member_List.setPreferredSize(new java.awt.Dimension(100,550));
			member_List.setMaximumSize(new java.awt.Dimension(100,550));
			member_List.setMinimumSize(new java.awt.Dimension(100,550));
		}
		return member_List;
	}
       public static void main(String chatargs[]) {
	args = chatargs;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
		try {
			Lircom lircomwindow = new Lircom("Lircom");
			String classname = "lircom.Chat";
			if (args.length > 0) {
				classname = args[0];
			} else {
				classname = javax.swing.JOptionPane.showInputDialog("Enter classname:", "lircom.Chat");
			}
			if (classname == null) {
				classname = "lircom.Chat";
			}
			lircomwindow.nickname = "Guest";
			if (args.length > 1) {
				lircomwindow.nickname = args[1];
			} else {
				lircomwindow.nickname = javax.swing.JOptionPane.showInputDialog("Enter nickname:");
			}
			if (lircomwindow.nickname == null) {
				lircomwindow.nickname = "Guest";
			}
			lircomwindow.setTitle(lircomwindow.nickname);
			lircomwindow.setChat(classname, lircomwindow.nickname);
			lircomwindow.setVisible(true);
 
                } catch (Exception e) {
			System.err.println("Died");
			e.printStackTrace();
		}
                
            }
        });
    }
    public void setChat(String classname, String nickname) throws Exception {
		try {
			chat = (Chat)(Class.forName(classname).newInstance());
		} catch (Exception e) {
			chat = new Chat();
		}
		chat.setNick(nickname);
		init(chat);
    }
    public void init(Chat chat) throws Exception {
		FromGUI fg = new FromGUI();
		FromServer fs = new FromServer();
		chat.setInputStream(fg);
		chat.setOutputStream(fs);
		fs.setChat(chat);
                //chat.connect(remoteServer, remotePort); 
                System.err.println("Channel id is "+chat.clientno);
                
                chat.jp = getMember_List();
                chat.tf = getChat_Text_Field();
                chat.ta = getChat_Info_Area();
                chat.init();
                chat.start();
                hyperlinkHandler = new Hyperactive();
                chat_Info_Area.addHyperlinkListener(hyperlinkHandler);
    }
    public Chat chat;
	private JEditorPane chat_Info_Area = null;
	private JTextField chat_Text_Field = null;
	private JList member_List = null;
	private Hyperactive hyperlinkHandler;
}
