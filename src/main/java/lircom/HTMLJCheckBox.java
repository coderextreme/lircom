package lircom;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;

public class HTMLJCheckBox extends JCheckBox {
	String item = null;
	private static final long serialVersionUID = 1L;
	public HTMLJCheckBox(String name, String item) {
		super(name);
		this.item = item;
	}
}
