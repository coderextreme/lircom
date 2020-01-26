package lircom;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;

public class SoundClient implements ActionListener {
	public static void main(String args[]) throws Exception {
		Socket client = new Socket(args[0], Integer.parseInt(args[1]));
		InputStream is = client.getInputStream();
		OutputStream os = client.getOutputStream();
		new SoundReceiver(is).start();
		new SoundSender(os).start();
		SoundClient sc = new SoundClient("Sound Client");
	}
	public SoundClient(String title) {
		JFrame jf = new JFrame(title);
		JButton quit = new JButton("Quit");
		quit.addActionListener(this);
		jf.getContentPane().add(quit);
		jf.pack();
		jf.setLocation(400,400);
		jf.setVisible(true);
	}
	public void actionPerformed(ActionEvent ae) {
		System.exit(0);
	}
}
