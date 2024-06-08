package net.coderextreme.impactVL;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.net.URL;

class Module implements Cloneable {  // aka Node
	Vector endPoints; // links around outside of module
	Vector links; // links between submodules
	Vector modules; // submodules
	String name; // name of machine or module (filename?)
	int width; // width in personalities
	int height; // height in personalities
	int id;
	static int idsequence = 0;
	public Module(String name) {
		this.name = name;
		this.width = width;
		this.height = height;
		endPoints = new Vector();
		links = new Vector();
		modules = new Vector();
		id = idsequence++;
	}
	public Module() {
		this("Node");
	}
	public Object clone() {
		return new Module(name);
	}
	public void addModule(Module m) {
		modules.addElement(m);
	}
	public void addLink(Link l) {
		links.addElement(l);
	}
	public void addLinkEndpoint(LinkEndpoint ep) {
		endPoints.addElement(ep);
	}
}

class LinkEndpoint {
	Module module;
	int x;  // location within module
	int y;
	int dir; // direction of I/O
	public LinkEndpoint(Module module, int x, int y, int dir) {
		this.module = module;
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
}

class Link {
	LinkEndpoint from;
	LinkEndpoint to;
	// maybe have a list of joining personalities which connect from and to
	public Link(LinkEndpoint from, LinkEndpoint to) {
		this.from = from;
		this.to = to;
	}
}

interface Rectangular {
	int getX();
	int getY();
	int getWidth();
	int getHeight();
	Rectangle getBounds();
	HashSet leftLines();
	HashSet rightLines();
	HashSet topLines();
	HashSet bottomLines();
}

class AcquireLabel {
	boolean prompt = true;
	String label = "x";
	public AcquireLabel(String label, boolean p) {
		this.label = label;
		this.prompt = p;
	}
	public VisualEndpoint acquire(VisualModule mod) throws Exception {
		VisualEndpoint end;
		if (prompt) {
			end = new VisualEndpoint(mod, "Enter "+label+" end label:", label);
		} else {
			end = new VisualEndpoint(mod, label);
		}
		return end;
	}
}

class Select {
    public static String ask(VisualMachine vm, String prompt, String clazzname,  ButtonGroup bg) {

        String result = "EmptyP";

        if (EventQueue.isDispatchThread()) {

            JPanel panel = new JPanel();
            panel.add(new JLabel(prompt));
            DefaultComboBoxModel model = new DefaultComboBoxModel();
	    Enumeration<AbstractButton> i = bg.getElements();
	    while (i.hasMoreElements()) {
		String option = i.nextElement().getText();
		System.err.println("Adding option "+option);
                model.addElement(option);
            }
            JComboBox comboBox = new JComboBox(model);
	    comboBox.setSelectedItem(clazzname);
            panel.add(comboBox);

            int iResult = JOptionPane.showConfirmDialog(vm, panel, prompt, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            switch (iResult) {
                case JOptionPane.OK_OPTION:
        	   result = (String) comboBox.getSelectedItem();
            }
        }
	return result;
    }
}

class VisualEndpoint extends JLabel implements Rectangular {
	VisualModule module;
	VisualLink link;
	String label;
	HashSet rightLines = new HashSet();
	HashSet leftLines = new HashSet();
	HashSet topLines = new HashSet();
	HashSet bottomLines = new HashSet();
	public VisualEndpoint(VisualModule m, String prompt, String def) throws Exception {
		setOpaque(true);
		module = m;
		label = javax.swing.JOptionPane.showInputDialog(m, prompt, def);
		if (label == null) {
			throw new Exception("Unnamed endpoint");
		}
		setText(label);
	}
	public VisualEndpoint(VisualModule m, String label) throws Exception {
		module = m;
		this.label = label;
		setText(label);
	}
	public void setLink(VisualLink l) {
		link = l;
	}
	public HashSet leftLines() {
		return leftLines;
	}
	public HashSet rightLines() {
		return rightLines;
	}
	public HashSet topLines() {
		return topLines;
	}
	public HashSet bottomLines() {
		return bottomLines;
	}
	public VisualMachine getParent() {
		return (VisualMachine)super.getParent();
	}
}

class VisualLink {
	VisualEndpoint from;
	VisualEndpoint to;
}

class VisualModule extends JLabel implements Rectangular {
	Module module;
	Cell cell;
	VisualMachine machine; // vm module is in 
	VisualMachine parent; // opened up vm of module
	HashSet rightLines = new HashSet();
	HashSet leftLines = new HashSet();
	HashSet topLines = new HashSet();
	HashSet bottomLines = new HashSet();
	public VisualModule(String clazzName) {
		super("");
		// We don't need this as Cell draw it
		String text = clazzName != null ? (clazzName.lastIndexOf(".") > 0 ? clazzName.substring(clazzName.lastIndexOf(".")+1) : clazzName ) : "Node";
		module = new Module(text);
		if (text != null) {
			System.err.println("cell "+clazzName);
			// Do not specify a URL. TODO
			URL icon = this.getClass().getClassLoader().getResource("impactVL/"+text+".gif");
			if (icon != null) {
				System.err.println("Setting "+text+" icon url "+icon.toString());
				setIcon(new ImageIcon(icon, text));
				ImageIcon clicon = (ImageIcon)getIcon();
				clicon.setImage(clicon.getImage().getScaledInstance(75, 75,Image.SCALE_DEFAULT));
			}
		}
	}
	public VisualModule(Module mod) {
		this(mod.name);
		module = mod;
	}
	public void init(VisualMachine vm, MouseEvent e, Selecter s, Placer p) {
		setParent(vm);
		addMouseListener(s);
		addMouseListener(p);
		addMouseMotionListener(s);
		setBorder(new BevelBorder(BevelBorder.RAISED));
		setSize(75,75);
		setLocation(e.getX(), e.getY());
		cell = setModulePersonality(e);
		vm.add(this);

		vm.invalidate();
		vm.validate();
		vm.repaint();
	}
	public Cell setModulePersonality(MouseEvent e) {
		// creates a personality from current pClass
		Cell c = new Cell(e.getX(), e.getY());
		try {
			Personality p = (Personality)(Class.forName("net.coderextreme.impactVL."+Impact.pClass).getDeclaredConstructor().newInstance());
			if (p != null) {
				c.setPersonality(p);
			} else {
				System.err.println("Class not found, continuing with image, if found");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.err.println("Can't set cell personality in CreateModule.setModulePersonality, skipping");
		}
		c.repaint();
		return c;
	}
	public void paint(Graphics g) {
		super.paint(g);
		try {
			Icon icon = getIcon();
			if (icon != null) {
				icon.paintIcon(this, g, getX(), getX());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (cell != null) {
			cell.paint(g);
		}
	}
	public void setMachine(VisualMachine vm) {
		machine = vm;
	}
	public VisualMachine getMachine() {
		return machine;
	}
	public void setParent(VisualMachine vm) {
		parent = vm;
	}
	public VisualMachine getParent() {
		return parent;
	}
	public HashSet leftLines() {
		return leftLines;
	}
	public HashSet rightLines() {
		return rightLines;
	}
	public HashSet topLines() {
		return topLines;
	}
	public HashSet bottomLines() {
		return bottomLines;
	}
}

class Placer implements MouseListener, MouseMotionListener {
	Command cmd;
	public void setCommand(Command c) {
		cmd = c;
	}
	public void mouseClicked(MouseEvent e) {
		if (cmd != null) {
			cmd.mouseClicked(e);
		}
	}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {
		if (cmd != null) {
			cmd.mousePressed(e);
		}
	}
	public void mouseReleased(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {
		if (cmd != null) {
			cmd.mouseDragged(e);
		}
	}
	public void mouseMoved(MouseEvent e) {
		if (cmd != null) {
			cmd.mouseMoved(e);
		}
	}
}

class Command implements MouseListener, MouseMotionListener {
	public void mouseClicked(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {}
}

class CreateModule extends Command {
	Selecter s;
	Placer p;
	ButtonGroup bg;
	boolean creating = false;
	public CreateModule(Selecter s, Placer p, ButtonGroup bg) {
		this.s = s;
		this.p = p;
		this.bg = bg;
	}
	public void setCreating(boolean b) {
		creating = b;
	}
	public void mouseClicked(MouseEvent e) {
		if (creating && e.getComponent() instanceof VisualMachine) {
			VisualMachine vm = (VisualMachine)e.getComponent();
			// String modulename = "net.coderextreme.impactVL."+Select.ask(vm, "Enter node class or image:", Impact.pClass, bg);
			// String modulename = javax.swing.JOptionPane.showInputDialog(vm, "Enter node name:", Impact.pClass);
			String modulename = Impact.pClass;

			if (modulename == null) {
				return;
			}
			addModule(vm, modulename, e);

		}
	}
	public VisualModule addModule(VisualMachine vm, VisualModule mod, MouseEvent e) {
		VisualModule module = new VisualModule(mod.module);
		module.init(vm, e, s, p);
		return module;
	}
	public VisualModule addModule(VisualMachine vm, String modulename, MouseEvent e) {
		VisualModule module = new VisualModule(modulename);
		module.init(vm, e, s, p);
		return module;
	}
}

class Cut extends Command {
	Selecter s;
	public Cut(Selecter s) {
		this.s = s;
	}
	public void mousePressed(MouseEvent e) {
		Component c = e.getComponent();
		if (c instanceof VisualModule) {
			VisualModule mod = (VisualModule)c;
			VisualMachine vm = mod.getParent();
			vm.remove(mod);
			s.setClipboard(mod);
		} else if (c instanceof VisualEndpoint) {
			VisualEndpoint vep = (VisualEndpoint)c;
			VisualMachine vm = vep.getParent();
			vm.remove(vep);
		}
	}
}
	
// Not Used
class CutLink extends Command {
	public void mousePressed(MouseEvent e) {
		Component c = e.getComponent();
		if (c instanceof VisualEndpoint) {
			VisualEndpoint vep = (VisualEndpoint)c;
			VisualMachine vm = vep.getParent();
			vm.remove(vep);
		}
	}
}

class Copy extends Command {
	Selecter s;
	Placer p;
	public Copy(Selecter s, Placer p) {
		this.s = s;
		this.p = p;
	}
	public void mousePressed(MouseEvent e) {
		Component c = e.getComponent();
		if (c instanceof VisualModule) {
			VisualModule mod = (VisualModule)c;
			s.setClipboard(mod);
		} else {
			System.err.println("Not cutting a Node.");
		}
	}
}

class Paste extends Command {
	Selecter s;
	Placer p;
	CreateModule cm;
	public Paste(Selecter s, Placer p, CreateModule cm) {
		this.s = s;
		this.p = p;
		this.cm = cm;
	}
	public void mousePressed(MouseEvent e) {
		if (e.getComponent() instanceof VisualMachine) {
			VisualModule clipboard = s.getClipboard();
			if (clipboard != null) {
				VisualMachine vm = (VisualMachine)e.getComponent();
				cm.addModule(vm, clipboard, e);
			} else {
				System.err.println("Nothing in clipboard");
			}
		}
	}
}

class ExpandModule extends Command {
	Selecter s;
	Placer p;
	public ExpandModule(Selecter s, Placer p) {
		this.s = s;
		this.p = p;
	}
	public void mousePressed(MouseEvent e) {
		VisualModule mod = s.getCurrent();
		if (mod != null) {
			VisualMachine vm = mod.getMachine();
			if (vm == null) {
				vm = new VisualMachine(mod, p, s);
			} else {
				System.err.println("Found vm");
			}
			vm.getFrame().setVisible(true);
		} else {
			System.err.println("No node");
		}
	} 
}

class CreateLink {
	static boolean linking = false;
	Placer p;
	public CreateLink(Placer p) {
		this.p = p;
	}
	public void setLinking(boolean b) {
		linking = b;
	}
	public boolean isLinking() {
		return linking;
	}
	public void link(VisualModule frommod, VisualModule tomod) {
		if (linking) {
			VisualMachine.link(frommod, tomod, new AcquireLabel("out", true), new AcquireLabel("in", true), p);
		}
	}
}

class Selecter extends Command {
	static VisualModule old;
	static VisualModule current;
	static VisualModule clipboard;
	int dx;
	int dy;
	CreateLink cl;
	public Selecter(CreateLink cl) {
		this.cl = cl;
	}
	public void mousePressed(MouseEvent e) {
		if (old != null) {
			old.setBorder(new BevelBorder(BevelBorder.RAISED));
		}
		current = (VisualModule)e.getComponent();
		if (current instanceof VisualModule) {
			current.setBorder(new BevelBorder(BevelBorder.LOWERED));
			if (old != null  && current != null && current != old) {
				cl.link(old, current);
				// current.setBorder(new BevelBorder(BevelBorder.RAISED));
				// current = null;
			}
			old = current;
			dx = e.getX();
			dy = e.getY();
		}
	}
	public void mouseDragged(MouseEvent e) {
		current = (VisualModule)e.getComponent();
		if (old instanceof VisualModule && current instanceof VisualModule) {
			int x = old.getX();
			int y = old.getY();
			old.setLocation(x+e.getX()-dx, y+e.getY()-dy);
			VisualMachine vm = (VisualMachine)old.getParent();
			vm.invalidate();
			vm.validate();
			vm.repaint();
		}
	}
	public VisualModule getCurrent() {
		return current;
	}
	public void setCurrent(VisualModule mod) {
		current = mod;
	}
	public void setLinking(boolean b) {
		old = null;
		current = null;
		cl.setLinking(b);
	}
	public boolean isLinking() {
		return cl.isLinking();
	}
	public VisualModule getClipboard() {
		return clipboard;
	}
	public void setClipboard(VisualModule mod) {
		clipboard = mod;
	}
}

class VisualMachine extends JPanel implements MouseMotionListener {
	Vector links = new Vector(); // links between submodules
	VisualModule mainModule;
	HashSet modules = new HashSet(); // links between submodules
	int mx; // mouse location
	int my;
	JFrame frame;
	Selecter selecter;
	static Vector machines = new Vector();
	public VisualMachine(VisualModule mod, Placer p, Selecter s) {
		mainModule = mod;
		mainModule.setMachine(this);
		machines.add(this);
		frame = new JFrame(mainModule.getText());
		frame.setSize(800,600);
		Container c = frame.getContentPane();
		setLayout(null);
		Dimension d = new Dimension(2000,2000);
		setMinimumSize(d);
		setPreferredSize(d);
		JScrollPane jsp = new JScrollPane(this);
		c.add(jsp, BorderLayout.CENTER);
		selecter = s;
		addMouseListener(p);
		addMouseMotionListener(this);
	}
	public void saveLinks(PrintStream ps) {
		ps.println("Node "+mainModule.getText()+" {");
		Iterator i = links.iterator();
		while (i.hasNext()) {
			VisualLink l = (VisualLink)i.next();
			VisualEndpoint frompt = l.from;
			VisualEndpoint topt = l.to;
			ps.println("\tRoute from "+frompt.module.getText()+"."+frompt.module.module.id+"."+frompt.label+" to "+topt.module.getText()+"."+topt.module.module.id+"."+topt.label+";");
		}
		ps.println("}");
	}
	static public void saveMachines(PrintStream ps) {
		Iterator m = machines.iterator();
		while (m.hasNext()) {
			VisualMachine vm = (VisualMachine)m.next();
			vm.saveLinks(ps);
		}
	}
	public void addLink(VisualLink l) {
		links.addElement(l);
		VisualModule from = l.from.module;
		if (from != null) {
			l.from.setLocation(from.getX()+from.getWidth(), (from.getY()+from.getHeight())/2);
		}
		l.from.setSize(40,10);
		add(l.from);

		VisualModule to = l.to.module;
		if (to != null) {
			l.to.setLocation(to.getX()-l.to.getWidth(), (to.getY()+to.getHeight())/2);
		}
		l.to.setSize(40,10);
		add(l.to);
	}
	public void addLinkedModule(VisualModule mod) {
		modules.add(mod);
	}
	public void remove(VisualModule mod) {
		super.remove(mod);
		modules.remove(mod);
		Iterator i = links.iterator();
		while (i.hasNext()) {
			VisualLink l = (VisualLink)i.next();
			VisualEndpoint frompt = l.from;
			VisualEndpoint topt = l.to;
			if (frompt.module == mod || topt.module == mod) {
				if (frompt != null) {
					super.remove(frompt);
				}
				if (topt != null) {
					super.remove(topt);
				}
				i.remove();
			}
		}
		invalidate();
		validate();
		repaint();
	}
	public void remove(VisualEndpoint vep) {
		Iterator i = links.iterator();
		while (i.hasNext()) {
			VisualLink l = (VisualLink)i.next();
			VisualEndpoint frompt = l.from;
			VisualEndpoint topt = l.to;
			if (frompt == vep || topt == vep) {
				System.err.println("Cutting Route");
				super.remove(frompt);
				super.remove(topt);
				i.remove();
			}
		}
		invalidate();
		validate();
		repaint();
	}
	public JFrame getFrame() {
		return frame;
	}
	public void paint(Graphics g) {
		super.paint(g);
		setLinks();
		setLabels();
		setLinks();
		paintLinks(g);
	}
	public boolean contains(HashSet lines, Integer i) {
		Iterator it = lines.iterator();
		while (it.hasNext()) {
			Integer in = (Integer)it.next();
			if (in.equals(i)) {
				return true;
			}
		}
		return false;
	}
	public int avoid(HashSet lines, int initial) {
		int offset = 10; // offset between lines
		int y = initial;
		Integer i = y;
		int mult = 0;
		int loop = 0;
		do {
			y = initial + offset * mult;
			i = y;
			if (loop % 2 == 0) {
				mult++;
			} else {
				offset = -offset;
			}
			loop++;
		} while (lines != null && contains(lines, i));
		if (lines != null) {
			lines.add(i);
		}
		return y;
	}

	public void setLinkLabels(VisualModule from, VisualEndpoint frompt, VisualModule to, VisualEndpoint topt) {
		HashSet fromleftLines = null;
		HashSet fromrightLines = null;
		HashSet fromtopLines = null;
		HashSet frombottomLines = null;
		HashSet toleftLines = null;
		HashSet torightLines = null;
		HashSet totopLines = null;
		HashSet tobottomLines = null;
		Rectangular f;
		if (from != null) {
			fromleftLines = from.leftLines;
			fromrightLines = from.rightLines;
			fromtopLines = from.topLines;
			frombottomLines = from.bottomLines;
			f = from;
		} else {
			f = frompt;
		}
		Rectangular t;
		if (to != null) {
			toleftLines = to.leftLines;
			torightLines = to.rightLines;
			totopLines = to.topLines;
			tobottomLines = to.bottomLines;
			t = to;
		} else {
			t = topt;
		}
		if ((int)f.getX() + (int)f.getWidth() < (int)t.getX()) {
			int yl1 = avoid(fromrightLines, (int)f.getY()+(int)f.getHeight()/2);
			int yl2 = avoid(toleftLines, (int)t.getY()+(int)t.getHeight()/2);
			frompt.setLocation( (int)f.getX()+(int)f.getWidth(), yl1 - frompt.getHeight()/2);
			topt.setLocation( (int)t.getX() - (int)topt.getWidth(), yl2 - topt.getHeight()/2);
		} else if ((int)f.getX() > (int)t.getX() + (int)t.getWidth()) {
			int yl1 = avoid(fromleftLines, (int)f.getY()+(int)f.getHeight()/2);
			int yl2 = avoid(torightLines, ((int)t.getY()+(int)t.getHeight()/2));
			frompt.setLocation( (int)f.getX() - frompt.getWidth(), yl1 - frompt.getHeight()/2);
			topt.setLocation( (int)t.getX()+(int)t.getWidth(), yl2 - topt.getHeight()/2);
		} else if ((int)f.getY() + (int)f.getHeight() < (int)t.getY()) {
			int xl1 = avoid(frombottomLines, (int)f.getX()+(int)f.getWidth()/2);
			int xl2 = avoid(totopLines, (int)t.getX()+(int)t.getWidth()/2);
			frompt.setLocation( (int)xl1 - frompt.getWidth()/2, (int)f.getY()+(int)f.getHeight());
			topt.setLocation( (int)xl2 - topt.getWidth()/2, (int)t.getY()-topt.getHeight());
		} else if ((int)f.getY() > (int)t.getY() + (int)t.getHeight()) {
			int xl1 = avoid(fromtopLines, (int)f.getX()+(int)f.getWidth()/2);
			int xl2 = avoid(tobottomLines, (int)t.getX()+(int)t.getWidth()/2);
			frompt.setLocation( (int)xl1 - frompt.getWidth()/2, (int)f.getY()-frompt.getHeight());
			topt.setLocation( (int)xl2 - topt.getWidth()/2, (int)t.getY()+(int)t.getHeight());
		}
	}
	public void setLabels() {
		Iterator i = links.iterator();
		while (i.hasNext()) {
			VisualLink l = (VisualLink)i.next();
			VisualEndpoint frompt = l.from;
			VisualEndpoint topt = l.to;
			VisualModule from = frompt.module;
			VisualModule to = topt.module;
			setLinkLabels(from, frompt, to, topt);
		}
	}
	public void paintLink(Graphics g, Rectangle from, Rectangle to,
			HashSet fromleftLines,
			HashSet fromrightLines,
			HashSet fromtopLines,
			HashSet frombottomLines,
			HashSet toleftLines,
			HashSet torightLines,
			HashSet totopLines,
			HashSet tobottomLines,
			HashSet hlines,
			HashSet vlines, String fromLabel, String toLabel) {
		hlines.addAll(fromleftLines);
		hlines.addAll(fromrightLines);
		vlines.addAll(fromtopLines);
		vlines.addAll(frombottomLines);
		hlines.addAll(torightLines);
		hlines.addAll(torightLines);
		vlines.addAll(totopLines);
		vlines.addAll(tobottomLines);
		FontMetrics fm = getFontMetrics(getFont());

		if ((int)from.getX() + (int)from.getWidth() < (int)to.getX()) {
			int yl1 = avoid(fromrightLines, (int)from.getY()+(int)from.getHeight()/2);
			int xl1 = avoid(vlines, ((int)from.getX()+(int)from.getWidth()+(int)to.getX())/2);
			int yl2 = avoid(toleftLines, (int)to.getY()+(int)to.getHeight()/2);
			g.drawLine(
				(int)from.getX()+(int)from.getWidth(),
				yl1,
				xl1,
				yl1
				);
			g.drawLine(
				xl1,
				yl1,
				xl1,
				yl2
				);
			g.drawLine(
				xl1,
				yl2,
				(int)to.getX(),
				yl2
				);
		} else if ((int)from.getX() > (int)to.getX() + (int)to.getWidth()) {
			int yl1 = avoid(fromleftLines, (int)from.getY()+(int)from.getHeight()/2);
			int xl1 = avoid(vlines, ((int)from.getX()+(int)to.getX()+(int)to.getWidth())/2);
			int yl2 = avoid(torightLines, ((int)to.getY()+(int)to.getHeight()/2));
			g.drawLine(
				(int)from.getX(),
				yl1,
				xl1,
				yl1
				);
			g.drawLine(
				xl1,
				yl1,
				xl1,
				yl2
				);
			g.drawLine(
				xl1,
				yl2,
				(int)to.getX()+(int)to.getWidth(),
				yl2
				);
		} else if ((int)from.getY() + (int)from.getHeight() < (int)to.getY()) {
			int xl1 = avoid(frombottomLines, (int)from.getX()+(int)from.getWidth()/2);
			int yl1 = avoid(hlines, ((int)from.getY()+(int)from.getHeight()+(int)to.getY())/2);
			int xl2 = avoid(totopLines, (int)to.getX()+(int)to.getWidth()/2);
			g.drawLine(
				xl1,
				(int)from.getY()+(int)from.getHeight(),
				xl1,
				yl1
				);
			g.drawLine(
				xl1,
				yl1,
				xl2,
				yl1
				);
			g.drawLine(
				xl2,
				yl1,
				xl2,
				(int)to.getY()
				);
		} else if ((int)from.getY() > (int)to.getY() + (int)to.getHeight()) {
			int xl1 = avoid(fromtopLines, (int)from.getX()+(int)from.getWidth()/2);
			int yl1 = avoid(hlines, ((int)to.getY()+(int)to.getHeight()+(int)from.getY())/2);
			int xl2 = avoid(tobottomLines, (int)to.getX()+(int)to.getWidth()/2);
			g.drawLine(
				xl1,
				(int)from.getY(),
				xl1,
				yl1
				);
			g.drawLine(
				xl1,
				yl1,
				xl2,
				yl1
				);
			g.drawLine(
				xl2,
				yl1,
				xl2,
				(int)to.getY()+(int)to.getHeight()
				);
		}
	}
	public void paintLinks(Graphics g) {
		HashSet vlines = new HashSet();
		HashSet hlines = new HashSet();
		FontMetrics fm = getFontMetrics(getFont());
		Iterator i = links.iterator();
		while (i.hasNext()) {
			VisualLink l = (VisualLink)i.next();
			Rectangular from = l.from.module;
			if (from == null) {
				from = l.from;
			}
			Rectangular to = l.to.module;
			if (to == null) {
				to = l.to;
			}
			paintLink(g, from.getBounds(), to.getBounds(),
				from.leftLines(),
				from.rightLines(),
				from.topLines(),
				from.bottomLines(),
				to.leftLines(),
				to.rightLines(),
				to.topLines(),
				to.bottomLines(),
				hlines,
				vlines,
				l.from.label, l.to.label);
		}
		VisualModule current = selecter.getCurrent();
		if (current != null && selecter.isLinking()) {
			Rectangle r = new Rectangle(mx, my, 1, 1);
			paintLink(g, current.getBounds(), r,
				hlines,
				hlines,
				vlines,
				vlines,
				hlines,
				hlines,
				vlines,
				vlines,
				hlines,
				vlines,
				"from", "to");
		}
	}
	public void mouseDragged(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		repaint();
	}
	public void mouseMoved(MouseEvent e) {
		mx = e.getX();
		my = e.getY();
		repaint();
	}
	public void setLinks() {
		Iterator mi = modules.iterator();
		while (mi.hasNext()) {
			VisualModule mod = (VisualModule)mi.next();
			mod.rightLines = new HashSet();
			mod.leftLines = new HashSet();
			mod.topLines = new HashSet();
			mod.bottomLines = new HashSet();
		}
	}
	static public void link(VisualModule frommod, VisualModule tomod, AcquireLabel fromal, AcquireLabel toal, Placer placer) {
		VisualMachine fromvm = (VisualMachine)frommod.getParent();
		VisualMachine tovm = (VisualMachine)tomod.getParent();
		if (fromvm == tovm) {
			VisualEndpoint from;
			VisualEndpoint to;
			try {
				from = fromal.acquire(frommod);
				from.addMouseListener(placer);
				to = toal.acquire(tomod);
				to.addMouseListener(placer);
			} catch (Exception e) {
				return;  // problem naming object
			}

			from.module = frommod;
			to.module = tomod;
			VisualLink l = new VisualLink();
			from.setLink(l);
			l.from = from;
			to.setLink(l);
			l.to = to;
			fromvm.addLink(l);

			fromvm.addLinkedModule(frommod);
			frommod.invalidate();
			frommod.validate();
			fromvm.invalidate();
			fromvm.validate();
			frommod.repaint();
			fromvm.repaint();

			tovm.addLinkedModule(tomod);
			tomod.invalidate();
			tomod.validate();
			tovm.invalidate();
			tovm.validate();
			tomod.repaint();
			tovm.repaint();
		}
	}
}

public class Impact extends JFrame implements WindowListener {
	Placer p;
	Selecter s;
	CreateModule cm;
	ExpandModule em;
	Cut ct;
	Copy cpy;
	Paste paste;
	public static String pClass = "EmptyP";
	public void init() {
		getContentPane().setLayout(new BorderLayout());
		p = new Placer();
		CreateLink cl = new CreateLink(p);
		s = new Selecter(cl);
		ButtonGroup bgNodeClasses = new ButtonGroup();
		cm = new CreateModule(s, p, bgNodeClasses);
		em = new ExpandModule(s, p);
		ct = new Cut(s);
		cpy = new Copy(s, p);
		paste = new Paste(s, p, cm);

		JMenuBar jmb = new JMenuBar();
		JMenu jm = new JMenu("File");
		jmb.add(jm);

		AbstractAction newmach = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				VisualModule mod = new VisualModule("Main");
				VisualMachine vm = new VisualMachine(mod, p, s);
				vm.getFrame().setVisible(true);
			}
		};
		newmach.putValue(Action.NAME, "New Route Graph");
		JMenuItem newmachine = new JMenuItem(newmach);
		jm.add(newmachine);

		AbstractAction openmach = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				VisualModule mod = new VisualModule("Main");
				VisualMachine vm = new VisualMachine(mod, p, s);
				Cell.addCells();
				InputStream is = null;
				try {
					// JNLP/JavaWebStart seems to be deprecated, but you are welcome to try
					// If someone wants to make this work with OpenWebStart, be my guest.
					//FileOpenService fos = (FileOpenService)ServiceManager.lookup("javax.jnlp.FileOpenService");
					//FileContents fc = fos.openFileDialog(null, null);
					//is = fc.getInputStream();
					is = new FileInputStream("machineoutput.rg");
				} catch (Exception e) {
					try {
						JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
						jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
						jfc.setFileFilter(new FileNameExtensionFilter("Files ending in .rg", "rg"));

						int rv = jfc.showOpenDialog(vm);
						if (rv != JFileChooser.APPROVE_OPTION) {
							return;
						}
						is = new FileInputStream(jfc.getSelectedFile()); 
					} catch (Exception e2) {
						e2.printStackTrace();
						return;
					}
				}
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			Cell.openMachine(br);
			br.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		int id = 0;
		VisualModule [][] modulearray = new VisualModule[Common.PMAXX][Common.PMAXY];
		for (int y = 0; y < Common.PMAXY; y++) {
			for (int x = 0; x < Common.PMAXX; x++) {
				MouseEvent me = new MouseEvent(
					vm,
					id++,
					System.currentTimeMillis(),
					0, x*150, y*50,
					1, false);
							
				Personality p = Common.cells[x][y].getPersonality();
				if (p == null) {
					modulearray[x][y] = cm.addModule(vm, "EmptyP", me);
				} else {
					String name = p.getClass().getName();
					modulearray[x][y] = cm.addModule(vm, name, me);
				}
			}
		}
		for (int y = 0; y < Common.PMAXY; y++) {
			for (int x = 0; x < Common.PMAXX; x++) {
				if (y < Common.PMAXY-1 && x > 0 && x < Common.PMAXX-1 ) {
					VisualMachine.link(modulearray[x][y],
							modulearray[x][y+1],
							new AcquireLabel("out", false),
							new AcquireLabel("in", false),
							p);
					VisualMachine.link(modulearray[x][y+1],
							modulearray[x][y],
							new AcquireLabel("out", false),
							new AcquireLabel("in", false),
							p);
				}
				if (x < Common.PMAXX-1 && y > 0 && y < Common.PMAXY-1 ) {
					VisualMachine.link(modulearray[x][y],
							modulearray[x+1][y],
							new AcquireLabel("out", false),
							new AcquireLabel("in", false),
							p);
					VisualMachine.link(modulearray[x+1][y],
							modulearray[x][y],
							new AcquireLabel("out", false),
							new AcquireLabel("in", false),
							p);
					}
				}
				}
				vm.getFrame().setVisible(true);
			}
		};
		openmach.putValue(Action.NAME, "Open Route Graph");
		JMenuItem openmachine = new JMenuItem(openmach);
		jm.add(openmachine);

		AbstractAction saveact = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				JFileChooser jfc = new JFileChooser(System.getProperty("user.dir"));
				jfc.setDialogType(JFileChooser.SAVE_DIALOG);
				jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
				jfc.setFileFilter(new FileNameExtensionFilter("Files ending in .rg", "rg"));

				int rv = jfc.showOpenDialog(null);
				PrintStream ps;
				if (rv != JFileChooser.APPROVE_OPTION) {
					ps = System.out;
					return;
				} else {
					try {
						ps = new PrintStream(new FileOutputStream(jfc.getSelectedFile())); 
					} catch (Exception exfo) {
						ps = System.out;
						exfo.printStackTrace();
					}
				}
				VisualMachine.saveMachines(ps);
			}
		};
		saveact.putValue(Action.NAME, "Save");
		JMenuItem save = new JMenuItem(saveact);
		jm.add(save);

		AbstractAction quitact = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				System.exit(0);
			}
		};

		quitact.putValue(Action.NAME, "Quit");
		JMenuItem quit = new JMenuItem(quitact);
		jm.add(quit);


		/////////////////////////////////////////////////////////////////
		JMenu jm2 = new JMenu("Edit");
		jmb.add(jm2);


		AbstractAction cutact = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				s.setLinking(false);
				cm.setCreating(false);
				p.setCommand(ct);
			}
		};

		cutact.putValue(Action.NAME, "Cut");
		JMenuItem cutmi = new JMenuItem(cutact);
		jm2.add(cutmi);

		AbstractAction copyact = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				s.setLinking(false);
				cm.setCreating(false);
				p.setCommand(cpy);
			}
		};

		copyact.putValue(Action.NAME, "Copy");
		JMenuItem copymi = new JMenuItem(copyact);
		jm2.add(copymi);

		AbstractAction pasteact = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				s.setLinking(false);
				cm.setCreating(false);
				p.setCommand(paste);
			}
		};

		pasteact.putValue(Action.NAME, "Paste");
		JMenuItem pastemi = new JMenuItem(pasteact);
		jm2.add(pastemi);
		jmb.add(jm2);

		// From Cell.java
		JMenu jm3 = new JMenu("Create");
		jmb.add(jm3);

		String [] clazzList = new String [] {
			"AndP",
			"BitAdderP",
			"BufferP",
			"CopyP",
			"DivisionP",
			"DontKnowP",
			"EmptyP",
			"LeftShiftP",
			"LeftTurnP",
			"MultAdderP",
			"PassP",
			"RightShiftP",
			"RightTurnP",
			"SortBottomP",
			"SortTopP"
		};

		for (int c = 0; c < clazzList.length; c++) {
			createRadio(jm3, clazzList[c], bgNodeClasses);
		}
		setJMenuBar(jmb);









		JToolBar jtb = new JToolBar();
		getContentPane().add(jtb, BorderLayout.PAGE_START);

		ButtonGroup bg = new ButtonGroup();

		AbstractAction select = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				s.setLinking(false);
				cm.setCreating(false);
				p.setCommand(null);
			}
		};
		select.putValue(Action.NAME, "Select");
		JButton selectbut = jtb.add(select);
		bg.add(selectbut);

		AbstractAction newmod = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				s.setLinking(false);
				cm.setCreating(true);
				p.setCommand(cm);
			}
		};
		newmod.putValue(Action.NAME, "New Node");
		JButton newmodbut = jtb.add(newmod);
		bg.add(newmodbut);

		AbstractAction newlink = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				s.setLinking(true);
				cm.setCreating(false);
			}
		};
		newlink.putValue(Action.NAME, "New Route");
		JButton newlinkbut = jtb.add(newlink);
		bg.add(newlinkbut);

		AbstractAction expand = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				s.setLinking(false);
				cm.setCreating(false);
				p.setCommand(em);
			}
		};

		expand.putValue(Action.NAME, "Expand Node");
		JButton expandbut = jtb.add(expand);
		bg.add(expandbut);

		AbstractAction cut = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				s.setLinking(false);
				cm.setCreating(false);
				p.setCommand(ct);
			}
		};
		cut.putValue(Action.NAME, "Cut Reference to Clipboard");
		JButton cutbut = jtb.add(cut);
		bg.add(cutbut);

		AbstractAction copy = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				s.setLinking(false);
				cm.setCreating(false);
				p.setCommand(cpy);
			}
		};
		copy.putValue(Action.NAME, "Copy Reference to Clipboard");
		JButton copybut = jtb.add(copy);
		bg.add(copybut);

		AbstractAction pasteaction = new AbstractAction() {
			public void actionPerformed(ActionEvent ae) {
				System.err.println(getValue(NAME));
				s.setLinking(false);
				cm.setCreating(false);
				p.setCommand(paste);
			}
		};
		pasteaction.putValue(Action.NAME, "Paste Reference from Clipboard");
		JButton pastebut = jtb.add(pasteaction);
		bg.add(pastebut);

		pack();
		setVisible(true);
		addWindowListener(this);
	}
	public void createRadio(JMenu jm, String clazz, ButtonGroup bgNodeClasses) {
		JRadioButtonMenuItem jmi = new JRadioButtonMenuItem(clazz);
		bgNodeClasses.add(jmi);
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				pClass = clazz;
			}
		});
		jm.add(jmi);
	}

	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {
		System.exit(0);
	}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
											      

	public static void main(String args[]) {
		Impact i = new Impact();
		i.init();
	}
}
