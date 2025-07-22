package impactVL;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.image.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.InputSource;                                                                                         import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.Attributes;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import java.lang.reflect.InvocationTargetException;
import java.lang.NullPointerException;

public class Cell extends JComponent implements MouseMotionListener, MouseListener, KeyListener, Runnable {
	public static String [] personalityClasses = new String[] {
		"impactVL.AndP", "impactVL.BitAdderP", "impactVL.BufferP", "impactVL.CopyP",
		"impactVL.DivisionP", "impactVL.DontKnowP", "impactVL.EmptyP",
		"impactVL.LeftShiftP", "impactVL.LeftTurnP", "impactVL.MultAdderP", "impactVL.PassP",
		"impactVL.RightShiftP", "impactVL.RightTurnP", "impactVL.SortBottomP", "impactVL.SortTopP" };
	public static int classIndex = 0;
	public static int M = Common.M;
	public static int SX = Common.SX;
	public static int SY = Common.SY;
	public static JTextField numTimes = new JTextField();
	public static JTextField interval = new JTextField();
	public static JPanel cellsView = new JPanel();
	public static JFrame ask = new JFrame("Enter number of cells in x and y directions");
	public static JFrame stopdlg = null;
	public static JPanel jp = new JPanel();
	public static JLabel jlx = new JLabel("Width w/o buffers");
	public static JTextField jtfx = new JTextField(" 1");
	public static JLabel jly = new JLabel("Height w/o buffers");
	public static JTextField jtfy = new JTextField(" 1");
	// public static JMenu createMenu = new JMenu("Create");
	public static ButtonGroup bg = new ButtonGroup();
	public static JButton ok = new JButton("OK");
	public static JButton cancel = new JButton("Cancel");
	public static final String REPLACE = "Replace";
	public static final String BACKSPACE = "Backspace";
	public static final String BIT_0 = "0";
	public static final String BIT_1 = "1";
	public static String ink = "";
	Personality cellsPersonality = null;
	public static String pClass = "impactVL.EmptyP";
	int x = 0;
	int y = 0;
	public static File currentFolder = new File(System.getProperty("user.dir"));

	public Cell(int x, int y) {
		this();
		this.x = x;
		this.y = y;
	}
	public Cell() {
		super();
		super.setBackground(Color.white);
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
	}
	public void setPersonality(Personality p) {
		this.cellsPersonality = p;
		System.err.println("set personality "+this.cellsPersonality);
		if (p != null) {
			p.setObserver(this);
		}
	}
	public Personality getPersonality() {
		System.err.println("get personality "+this.cellsPersonality);
		return this.cellsPersonality;
	}
	public void setXY(int x, int y) {
	}
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			if (Common.starty > 0)
				Common.starty--;
			movePersonalities();
			break;
		case KeyEvent.VK_DOWN:
			if (Common.starty + Common.PMAXY < Common.PMAXY) 
				Common.starty++;
			movePersonalities();
			break;
		case KeyEvent.VK_LEFT:
			if (Common.startx > 0)
				Common.startx--;
			movePersonalities();
			break;
		case KeyEvent.VK_RIGHT:
			if (Common.startx + Common.PMAXX < Common.PMAXX) 
				Common.startx++;
			movePersonalities();
			break;
		default:
			if (this.cellsPersonality instanceof BufferP) {
				System.err.println("key pressed personality "+this.cellsPersonality);
				((BufferP)this.cellsPersonality).keyPressed(e);
			}
			break;
		}
	}
	static public void movePersonalities() {
		for (int y = 0; y < Common.PMAXY; y++) {
			for (int x = 0; x < Common.PMAXX; x++) {
				if (Common.cells[x][y] != null) {
					if (x+Common.startx >= 0 && x+Common.startx < Common.PMAXX &&
					    y+Common.starty >= 0 && y+Common.starty < Common.PMAXY) {
						Common.cells[x][y].setPersonality(Common.personalities[x+Common.startx][y+Common.starty]);
					} else {
						Common.cells[x][y].setPersonality(null);
					}
					Common.cells[x][y].repaint();
				}
			}
		}
		cellsView.invalidate();
		cellsView.validate();
		cellsView.repaint();
	}
	public void keyReleased(KeyEvent e) {
	}
	public void keyTyped(KeyEvent e) {
	}
	public void mouseDragged(MouseEvent me) {
		requestFocus();
	}
	public void mouseMoved(MouseEvent me) {
		requestFocus();
	}

	public static void setModulePersonalities(int x, int y) {
		if (Common.modulePersonalities != null) {
			
			for (int mx = 0; mx < Common.modulePersonalities.length; mx++) {
				for (int my = 0; my < Common.modulePersonalities[mx].length; my++) {
					Personality mp = Common.modulePersonalities[mx][my];
					if (x+mx >= 0 && x+mx < Common.PMAXX && y+my >= 0 && y+my < Common.PMAXY) {
						Cell c = Common.cells[x+mx][y+my];
						if (mp != null) {
							Personality p = Common.personalities[x+mx][y+my] = (Personality)(mp.clone());
							if (c != null && p != null) {
								c.setPersonality(p);
								c.repaint();
							} else {
								System.err.println("cell is "+c+" personality is "+p);
							}
						}
					}
				}
			}
		}
	}
	public void mouseClicked(MouseEvent e) {
		System.err.println("Click "+x+","+y);
		try {
			Personality p =	Common.personalities[x+Common.startx][y+Common.starty];
			if (pClass.equals("impactVL.Module")) {
				setModulePersonalities(x, y);
				System.err.println("Setting Module "+x+","+y);
			} else if (p instanceof BufferP) {
					System.err.println(ink);
					switch (ink) {
						case BIT_0:
						case BIT_1:
							((BufferP)p).addToInBuffer(ink);
							repaint();
							break;
						case BACKSPACE:
							((BufferP)p).backspace();
							repaint();
							break;
						case REPLACE:
							try {
								setPersonalityFromPopup(x, y);
							} catch (InvocationTargetException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InstantiationException ex) {
								ex.printStackTrace(System.err);
							}
							break;
						default:
							break;
					}
			} else if (x+Common.startx >= 0 && y+Common.starty >= 0 && x+Common.startx < Common.PMAXX && y+Common.starty < Common.PMAXY) {
				try {
					setPersonalityFromPopup(x, y);
				} catch (InvocationTargetException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InstantiationException ex) {
					ex.printStackTrace(System.err);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) 
	{
		if((infoflags & ImageObserver.ALLBITS) != 0) {
			return false;
		}
		return true;
	}
	public class PopupAction implements ActionListener {
		String pclass = "impactVL.EmptyP";
		public PopupAction(String pClass) {
			this.pclass = pClass;
		    
		}
		public void actionPerformed(ActionEvent ae) {
			pClass = this.pclass;
		}
	}
	public void createPersonalityMenu(String displayName, String className, ButtonGroup bg, JMenu jm) {
		JMenuItem jmi = new JRadioButtonMenuItem(displayName);
		bg.add(jmi);
		jmi.addActionListener(new PopupAction(className));
		jm.add(jmi);
	}
	public void paint(Graphics g) {
		g.setFont(new Font("Helvetica", Font.PLAIN, M - 2));
		g.setColor(Color.white);
		super.paint(g);
		// g.fillRect(0,0,5*M,5*M);
		g.setColor(Color.black);
		Personality p = this.cellsPersonality;
		if (p != null && p instanceof BufferP)  {
			p.paint(g);
		}
		g.drawLine(0,0,0,5*M);
		g.drawLine(0,5*M,5*M,5*M);
		g.drawLine(5*M,5*M,5*M,0);
		g.drawLine(5*M,0,0,0);

		// System.err.println("personality "+this.cellsPersonality);
		if (p == null || p instanceof BufferP || p instanceof EmptyP) {
		} else {
			p.paint(g);
			g.drawLine(3*M,0,3*M,M);
			g.drawLine(3*M,M,4*M,M);
			g.drawLine(4*M,M,4*M,0);
			if (p != null && p.getCellFull(Common.TOP)) 
				g.drawString(p.getTopDisplay(), 3*M+SX, M-SY);

			g.drawLine(M,5*M,M,4*M);
			g.drawLine(M,4*M,2*M,4*M);
			g.drawLine(2*M,4*M,2*M,5*M);
			if (p != null && p.getCellFull(Common.BOTTOM))
				g.drawString(p.getBottomDisplay(), M+SX, 5*M-SY);

			g.drawLine(0,M,M,M);
			g.drawLine(M,M,M,2*M);
			g.drawLine(M,2*M,0,2*M);
			if (p != null && p.getCellFull(Common.LEFT))
				g.drawString(p.getLeftDisplay(), 0+SX, 2*M-SY);

			g.drawLine(5*M,3*M,4*M,3*M);
			g.drawLine(4*M,3*M,4*M,4*M);
			g.drawLine(4*M,4*M,5*M,4*M);
			if (p != null && p.getCellFull(Common.RIGHT))
				g.drawString(p.getRightDisplay(), 4*M+SX, 4*M-SY);
		}
	}
	public Dimension getPreferredSize() {
		return new Dimension(5*M,5*M);
	}
	public static void main(String args[]) {
		new Cell(args);
	}
	public Cell(String args[]) {
		this();
		String personalities_str = "4x4";
		if (args.length > 0) {
			personalities_str = args[0];
		}
		if (personalities_str.equals("cell")) {
			Common.PMAXX = 3;
			Common.PMAXY = 3;
		} else if (personalities_str.equals("division")) {
			Common.PMAXX = 3;
			Common.PMAXY = 10;
		}
			
		class ClosingFrame extends JFrame implements WindowListener {
			public ClosingFrame() {
				addWindowListener(this);
			}
			public void windowActivated(WindowEvent e) {}
			public void windowClosed(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				System.err.println("Closing");
				System.exit(0);
			}
			public void windowDeactivated(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowOpened(WindowEvent e) {}

		}
		JFrame jf = new ClosingFrame();
		cellsView.setLayout(new GridLayout(Common.PMAXY,Common.PMAXX));
		JScrollPane jsp = new JScrollPane(cellsView);

		int y = 0;
		int x = 0;
		Common.personalities[x][y] = new EmptyP();
		for (x = 1; x < Common.PMAXX-1; x++) {
			Common.personalities[x][y] = new BufferP();
		}
		Common.personalities[x][y] = new EmptyP();

		if (personalities_str.equals("cell")) {
			// simple adder
			Common.personalities[0][1] = new BufferP();
			Common.personalities[1][1] = new MultAdderP();
			Common.personalities[2][1] = new BufferP();
		} else if (personalities_str.equals("4x4")) {
			// 4 x 4 bit multiplier
			Common.personalities[0][1] = new BufferP();
			Common.personalities[1][1] = new DontKnowP();
			Common.personalities[2][1] = new RightTurnP();
			Common.personalities[3][1] = new DontKnowP();
			Common.personalities[4][1] = new RightTurnP();
			Common.personalities[5][1] = new DontKnowP();
			Common.personalities[6][1] = new RightTurnP();
			Common.personalities[7][1] = new DontKnowP();
			Common.personalities[8][1] = new RightTurnP();
			Common.personalities[9][1] = new RightTurnP();
			Common.personalities[10][1] = new BufferP();

			for (int z = 2; z < 8; z+=2) {
				Common.personalities[0][z] = new BufferP();
				Common.personalities[1][z] = new PassP();
				Common.personalities[2][z] = new AndP();
				Common.personalities[3][z] = new PassP();
				Common.personalities[4][z] = new AndP();
				Common.personalities[5][z] = new PassP();
				Common.personalities[6][z] = new AndP();
				Common.personalities[7][z] = new PassP();
				Common.personalities[8][z] = new AndP();
				Common.personalities[9][z] = new PassP();
				Common.personalities[10][z] = new BufferP();

				Common.personalities[0][z+1] = new BufferP();
				Common.personalities[1][z+1] = new LeftTurnP();
				Common.personalities[2][z+1] = new MultAdderP();
				Common.personalities[3][z+1] = new DontKnowP();
				Common.personalities[4][z+1] = new MultAdderP();
				Common.personalities[5][z+1] = new DontKnowP();
				Common.personalities[6][z+1] = new MultAdderP();
				Common.personalities[7][z+1] = new DontKnowP();
				Common.personalities[8][z+1] = new MultAdderP();
				Common.personalities[9][z+1] = new DontKnowP();
				Common.personalities[10][z+1] = new BufferP();
			}
			Common.personalities[0][8] = new BufferP();
			Common.personalities[1][8] = new PassP();
			Common.personalities[2][8] = new AndP();
			Common.personalities[3][8] = new PassP();
			Common.personalities[4][8] = new AndP();
			Common.personalities[5][8] = new PassP();
			Common.personalities[6][8] = new AndP();
			Common.personalities[7][8] = new PassP();
			Common.personalities[8][8] = new AndP();
			Common.personalities[9][8] = new PassP();
			Common.personalities[10][8] = new BufferP();
		} else if (personalities_str.equals("division")) {
			for (y = 1; y < Common.PMAXY-1; y++) {
				Common.personalities[0][y] = new BufferP();
				Common.personalities[1][y] = new DivisionP();
				Common.personalities[2][y] = new BufferP();
				((BufferP)Common.personalities[2][y]).addToInBuffer(BIT_1);
				((BufferP)Common.personalities[2][y]).addToInBuffer(BIT_0);
				((BufferP)Common.personalities[2][y]).addToInBuffer(BIT_0);
				((BufferP)Common.personalities[2][y]).addToInBuffer(BIT_0);
				((BufferP)Common.personalities[2][y]).addToInBuffer(BIT_0);
				((BufferP)Common.personalities[2][y]).addToInBuffer(BIT_0);
				((BufferP)Common.personalities[2][y]).addToInBuffer(BIT_0);
				((BufferP)Common.personalities[2][y]).addToInBuffer(BIT_1);
			}
			((BufferP)Common.personalities[1][0]).addToInBuffer(BIT_1);
			((BufferP)Common.personalities[1][0]).addToInBuffer(BIT_1);
		} else {
			// random personalities
			Random r = new Random();
			for (y = 1; y < Common.PMAXY-1; y++) {
				x = 0;
				Common.personalities[x][y] = new BufferP();
				for (x = 1; x < Common.PMAXX-1; x++) {
					switch (r.nextInt(8)) {
					case 0:
						Common.personalities[x][y] = new RightTurnP();
						break;
					case 1:
						Common.personalities[x][y] = new LeftTurnP();
						break;
					case 2:
						Common.personalities[x][y] = new PassP();
						break;
					case 3:
						Common.personalities[x][y] = new LeftShiftP();
						break;
					case 4:
						Common.personalities[x][y] = new RightShiftP();
						break;
					case 5:
						Common.personalities[x][y] = new MultAdderP();
						break;
					case 6:
						Common.personalities[x][y] = new AndP();
						break;
					default:
						Common.personalities[x][y] = new DontKnowP();
						break;
					}
				}
				x = Common.PMAXX-1;
				Common.personalities[x][y] = new BufferP();
			}
		}
		// continures here
		y = Common.PMAXY-1;
		x = 0;
		Common.personalities[x][y] = new EmptyP();
		for (x = 1; x < Common.PMAXX-1; x++) {
			Common.personalities[x][y] = new BufferP();
		}
		Common.personalities[x][y] = new EmptyP();
		addCells();

		JMenuBar jmb = new JMenuBar();
		jf.setJMenuBar(jmb);
		JMenu jm = new JMenu("File");
		jmb.add(jm);
		JMenuItem jmi = new JMenuItem("New");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ok.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						clear();
					}
				});
				cancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent ae) {
						ask.setVisible(false);
					}
				});
				jp.add(jlx);
				jp.add(jtfx);
				jp.add(jly);
				jp.add(jtfy);
				jp.add(ok);
				jp.add(cancel);
				ask.getContentPane().add(jp);
				ask.pack();
				ask.setVisible(true);
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Open Inputs...");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser(currentFolder);
				jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
				jfc.setFileFilter(new FileNameExtensionFilter("Files ending in .rg", "rg"));
				int rv = jfc.showOpenDialog(cellsView);
				if (rv != JFileChooser.APPROVE_OPTION) {
					return;
				}
				try {
					currentFolder = jfc.getCurrentDirectory();
					FileInputStream fis = new FileInputStream(jfc.getSelectedFile()); 
					BufferedReader br = new BufferedReader(new InputStreamReader(fis));
					cellsView.removeAll();
					openInputs(br, true);
					addCells();
					cellsView.invalidate();
					cellsView.validate();
					cellsView.repaint();
					br.close();
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Open Node...");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser(currentFolder);
				jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
				jfc.setFileFilter(new FileNameExtensionFilter("Files ending in .rg", "rg"));
				int rv = jfc.showOpenDialog(cellsView);
				if (rv != JFileChooser.APPROVE_OPTION) {
					return;
				}
				try {
					currentFolder = jfc.getCurrentDirectory();
					FileInputStream fis = new FileInputStream(jfc.getSelectedFile()); 
					BufferedReader br = new BufferedReader(new InputStreamReader(fis));
					cellsView.removeAll();
					openModule(br);
					addCells();
					cellsView.invalidate();
					cellsView.validate();
					cellsView.repaint();
					br.close();
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Open Route Graph...");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser(currentFolder);
				jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
				jfc.setFileFilter(new FileNameExtensionFilter("Files ending in .rg", "rg"));
				int rv = jfc.showOpenDialog(cellsView);
				if (rv != JFileChooser.APPROVE_OPTION) {
					return;
				}
				try {
					currentFolder = jfc.getCurrentDirectory();
					FileInputStream fis = new FileInputStream(jfc.getSelectedFile()); 
					BufferedReader br = new BufferedReader(new InputStreamReader(fis));
					cellsView.removeAll();
					openMachine(br);
					addCells();
					cellsView.invalidate();
					cellsView.validate();
					cellsView.repaint();
					br.close();
					fis.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Save Inputs As...");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser(currentFolder);
				jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
				jfc.setFileFilter(new FileNameExtensionFilter("Files ending in .rg", "rg"));
				int rv = jfc.showSaveDialog(cellsView);
				if (rv != JFileChooser.APPROVE_OPTION) {
					return;
				}
				
				try {
					currentFolder = jfc.getCurrentDirectory();
					FileOutputStream fos = new FileOutputStream(jfc.getSelectedFile());
					PrintStream oos = new PrintStream(fos);
					saveInputs(oos);
					oos.close();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Save Node As...");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser(currentFolder);
				jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
				jfc.setFileFilter(new FileNameExtensionFilter("Files ending in .rg", "rg"));
				int rv = jfc.showSaveDialog(cellsView);
				if (rv != JFileChooser.APPROVE_OPTION) {
					return;
				}
				try {
					currentFolder = jfc.getCurrentDirectory();
					FileOutputStream fos = new FileOutputStream(jfc.getSelectedFile());
					PrintStream oos = new PrintStream(fos);
					saveModule(oos);
					oos.close();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Save Route Group As...");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				JFileChooser jfc = new JFileChooser(currentFolder);
				jfc.removeChoosableFileFilter(jfc.getAcceptAllFileFilter());
				jfc.setFileFilter(new FileNameExtensionFilter("Files ending in .rg", "rg"));
				int rv = jfc.showSaveDialog(cellsView);
				if (rv != JFileChooser.APPROVE_OPTION) {
					return;
				}
				
				try {
					currentFolder = jfc.getCurrentDirectory();
					FileOutputStream fos = new FileOutputStream(jfc.getSelectedFile());
					PrintStream oos = new PrintStream(fos);
					saveMachine(oos);
					oos.close();
					fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		jm.add(jmi);

		DefaultHandler mh = new DefaultHandler() {
		    String currentPersonality = "impactVL.EmptyP";
		    int x = 0;
		    int maxY = 0;
		    int maxX = 0;
		    ArrayList<ArrayList<Personality>> rows = new ArrayList<ArrayList<Personality>>();
		    ArrayList<Personality> currentRow = new ArrayList<Personality>();
		    @Override
		    public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
			switch (qName.toLowerCase()) {
			}
		    }
		    @Override
		    public void endElement(String uri, String localName, String qName) throws SAXException {
			switch (qName.toLowerCase()) {
			    case "table":
				System.err.println("Parsing "+qName+" "+maxX+" "+maxY);
				Common.startx = 0;
				Common.starty = 0;
				Common.PMAXX = maxX;
				Common.PMAXY = maxY;
				Common.MMAXX = maxX - 2;
				Common.MMAXY = maxY - 2;

				cellsView.setLayout(new GridLayout(Common.PMAXY,Common.PMAXX));
				Common.cells = new Cell[Common.PMAXX][Common.PMAXY];
				Common.personalities = new Personality[Common.PMAXX][Common.PMAXY];
				for (int py = 0; py < Common.PMAXY; py++) {
					for (int px = 0; px < Common.PMAXX; px++) {
						Personality p = null;
						try {
							p = rows.get(py).get(px);
						} catch	(IndexOutOfBoundsException e) {
							System.err.println("Oops, there doesn't seem to be a personality at "+px+","+px);
						}
						Common.personalities[px+Common.startx][py+Common.starty] = p;
					}
				}
				cellsView.removeAll();
				addCells();
				cellsView.invalidate();
				cellsView.validate();
				cellsView.repaint();
				ask.setVisible(false);
				currentPersonality = "impactVL.EmptyP";
				x = 0;
				maxY = 0;
				maxX = 0;
				rows = new ArrayList<ArrayList<Personality>>();
				currentRow = new ArrayList<Personality>();
				break;
			    case "tr":
				// System.err.println("Parsing "+qName);
				rows.add(currentRow);
		    		currentRow = new ArrayList<Personality>();
				x = 0;
				maxY++;
				break;
			    case "td":
				// System.err.println("Parsing "+qName+" "+currentPersonality);
				try {
					Personality p = null;
					try {
						if (currentPersonality != null && currentPersonality.startsWith("'")) {
							currentPersonality = currentPersonality.substring(1);
						}
						Integer.parseInt(currentPersonality);
						System.err.println("reading a buffer "+x+" "+maxY+" "+currentPersonality);
						p = new BufferP();
						if (p instanceof BufferP) {
							((BufferP)p).setIn(currentPersonality);
						}
					} catch (java.lang.NumberFormatException e) {
						if (currentPersonality == null || currentPersonality.trim().equals("")) {
							currentPersonality = "Empty";
						}
						System.err.println("instantiating "+x+" "+maxY+" "+currentPersonality);
						Class curperClass = Class.forName("impactVL."+currentPersonality+"P");
						p = (Personality)(curperClass.getDeclaredConstructor().newInstance());
					}
					currentRow.add(p);
				} catch (InvocationTargetException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InstantiationException e) {
					e.printStackTrace(System.err);
				}
				x++;
				if (x > maxX) {
					maxX = x;
			    	}
				break;
			    case "th":
				break;
			}
		    }

		    @Override
		    public void characters(char[] ch, int start, int length) throws SAXException {
		    	if (length > 0) {
				currentPersonality = new String(ch, start, length).trim();
				System.err.println("GOT "+currentPersonality);
			}
		    }
		};

		jmi = new JMenuItem("Import Module from Clipboard");
		jmi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				java.awt.EventQueue.invokeLater(() -> {
				    try {
					Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
					String html = (String)clipboard.getData(DataFlavor.allHtmlFlavor);
					html = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+html.substring(html.indexOf("<"));
					html = html.replaceAll("<br>", "");
					System.err.println(html);
					InputSource is = new InputSource(new StringReader(html));
					SAXParserFactory parserFactory = SAXParserFactory.newInstance();
					SAXParser parser = parserFactory.newSAXParser();
					XMLReader reader = parser.getXMLReader();
					reader.setContentHandler(mh);
					reader.parse(is);
				    } catch (SAXException | IOException | ParserConfigurationException | UnsupportedFlavorException e) {
					e.printStackTrace(System.err);
				    }
				});
			}
		});
		jm.add(jmi);
		jmi = new JMenuItem("Export Module to Clipboard");
		jmi.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				StringBuffer sb = new StringBuffer();
				Cell.saveMachine(sb);
				clipboard.setContents(new HtmlSelection(sb.toString()), null);

			}
		});
		jm.add(jmi);

		jmi = new JMenuItem("Exit");
		jmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				System.exit(0);
			}
		});
		jm.add(jmi);







		/*
		createPersonalityMenu("Module", "impactVL.Module", bg, createMenu);
		createPersonalityMenu("And", "impactVL.AndP", bg, createMenu);
		createPersonalityMenu("Bit Adder", "impactVL.BitAdderP", bg, createMenu);
		createPersonalityMenu("Buffer", "impactVL.BufferP", bg, createMenu);
		createPersonalityMenu("Copy", "impactVL.CopyP", bg, createMenu);
		createPersonalityMenu("Division", "impactVL.DivisionP", bg, createMenu);
		createPersonalityMenu("Don't Know", "impactVL.DontKnowP", bg, createMenu);
		createPersonalityMenu("Empty/Flaw", "impactVL.EmptyP", bg, createMenu);
		createPersonalityMenu("Left Shift", "impactVL.LeftShiftP", bg, createMenu);
		createPersonalityMenu("Left Turn", "impactVL.LeftTurnP", bg, createMenu);
		createPersonalityMenu("Multiply Adder", "impactVL.MultAdderP", bg, createMenu);
		createPersonalityMenu("Pass", "impactVL.PassP", bg, createMenu);
		createPersonalityMenu("Right Shift", "impactVL.RightShiftP", bg, createMenu);
		createPersonalityMenu("Right Turn", "impactVL.RightTurnP", bg, createMenu);
		createPersonalityMenu("Sort Bottom", "impactVL.SortBottomP", bg, createMenu);
		createPersonalityMenu("Sort Top", "impactVL.SortTopP", bg, createMenu);
		jmb.add(createMenu);
		*/

		JPanel tools = new JPanel();
		JButton adv = new JButton("Advance");
		adv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Common.cells[0][0].step();
				// new Thread(Common.cells[0][0]).start();
			}
		});
		JButton intr = new JButton("Interrupt");
		intr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				Common.forever = false;
			}
		});
		tools.add(adv);
		tools.add(new JLabel("Number of steps"));
		numTimes.setColumns(3);
		numTimes.setText("100");
		tools.add(numTimes);
		tools.add(intr);
		tools.add(new JLabel("Pause interval"));
		interval.setColumns(6);
		tools.add(interval);
		tools.add(new JLabel("Inkpots:"));
		ButtonGroup inkpots = new ButtonGroup();

		JRadioButton jrb = new JRadioButton(BIT_0);
		jrb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ink = BIT_0;
			}
		});
		inkpots.add(jrb);
		tools.add(jrb);

		jrb = new JRadioButton(BIT_1);
		jrb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ink = BIT_1;
			}
		});
		inkpots.add(jrb);
		tools.add(jrb);

		jrb = new JRadioButton(BACKSPACE);
		jrb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ink = BACKSPACE;
			}
		});
		inkpots.add(jrb);
		tools.add(jrb);

		jrb = new JRadioButton(REPLACE);
		jrb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				ink = REPLACE;
			}
		});
		inkpots.add(jrb);
		tools.add(jrb);


		jf.getContentPane().add("North", tools);
		jf.getContentPane().add("Center", jsp);
		jf.setSize(1000,1000);
		jf.pack();
		jf.setVisible(true);
	}
	private static long lineno = 0;
	public static String readLine(BufferedReader br) {
		String s = "";
		try {
			lineno++;
			s = br.readLine();
			System.err.println("Got "+s+" on line "+lineno);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Load on line "+lineno);
			throw new NullPointerException();
		}
		return s;
	}
	public static boolean openModule(BufferedReader br) {
		pClass = "impactVL.Module";
		Common.MMAXX = Integer.parseInt(readLine(br));
		Common.MMAXY = Integer.parseInt(readLine(br));
		Common.modulePersonalities = new Personality[Common.MMAXX][Common.MMAXY];
		try {
			int n = 0;
			String pname = null;
			do {
				pname = readLine(br);
				if (pname == null || pname.equals("-------------")) {
					break;
				}
				int mx = Integer.parseInt(readLine(br))-1;
				int my = Integer.parseInt(readLine(br))-1;
				System.err.println("pname is "+pname);
				if (mx >= 0 && mx < Common.MMAXX && my >= 0 && my < Common.MMAXY) {
					Personality p = Common.modulePersonalities[mx][my] = (Personality)Class.forName(pname).getDeclaredConstructor().newInstance();
					Common.personalities[mx+1][my+1] = p;
					Common.cells[mx+1][my+1] = new Cell(mx+1, my+1);
					Common.cells[mx+1][my+1].setPersonality(p);
					p.lOutput = Boolean.parseBoolean(readLine(br));
					p.rOutput = Boolean.parseBoolean(readLine(br));
					p.tOutput = Boolean.parseBoolean(readLine(br));
					p.bOutput = Boolean.parseBoolean(readLine(br));
					p.lFull = Boolean.parseBoolean(readLine(br));
					p.rFull = Boolean.parseBoolean(readLine(br));
					p.tFull = Boolean.parseBoolean(readLine(br));
					p.bFull = Boolean.parseBoolean(readLine(br));
				} else {
					System.err.println("Out of bounds "+mx+","+my);
				}
				n++;
			} while (true && n < Common.MMAXX * Common.MMAXY);
			if (pname == null) {
				return false; // EOF
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.err.println("Failed to load module on line "+lineno);
			return false; // bail
		}
	}
	public static boolean openInputs(BufferedReader br, boolean onlyInputs) {
		try {
			Common.PMAXX = Integer.parseInt(readLine(br));
			Common.PMAXY = Integer.parseInt(readLine(br));
			if (!onlyInputs) {
				cellsView.setLayout(new GridLayout(Common.PMAXY, Common.PMAXX));
				Common.cells = new Cell[Common.PMAXX][Common.PMAXY];
				Common.personalities = new Personality[Common.PMAXX][Common.PMAXY];
			}
			String line = null;
			int n = 0;
			try {
				do {
					line = readLine(br);
					if (line == null || line.equals("-------------")) {
						break;
					}
					int x = Integer.parseInt(line);
					int y = Integer.parseInt(readLine(br));
					BufferP bp = new BufferP();
					if (x >= 0 && x < Common.PMAXX && y >= 0 && y < Common.PMAXY) {
						Common.personalities[x][y] = bp;
						Common.cells[x][y] = new Cell(x, y);
						Common.cells[x][y].setPersonality(bp);
					} else {
						System.err.println("Out of bounds "+x+","+y);
					}
					bp.setIn(readLine(br));
					bp.setOut(readLine(br));
					n++;
				} while (true && n < Common.PMAXX * Common.PMAXY);
			} catch (Exception e)  {
				e.printStackTrace();
			}
			System.err.println("Read "+n+" buffers");
			if (line == null) {
				return false; // EOF
			} else {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to load inputs on line "+lineno);
			return false; // bail
		}
	}
	public static boolean openMachine(BufferedReader br) {
		boolean NOTEOF = true;
		try {
			NOTEOF = openInputs(br, false);
			if (NOTEOF) {
				NOTEOF = openModule(br);
			}
			setModulePersonalities(1, 1);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to load machine");
		}
		return NOTEOF;
	}
	public static void clear() {
		try {
			Common.startx = 0;
			Common.starty = 0;
			int newx = Integer.parseInt(jtfx.getText().trim());
			int newy = Integer.parseInt(jtfy.getText().trim());
			Common.PMAXX = newx + 2;
			Common.PMAXY = newy + 2;
			Common.MMAXX = newx;
			Common.MMAXY = newy;

			cellsView.setLayout(new GridLayout(Common.PMAXY,Common.PMAXX));
			Common.cells = new Cell[Common.PMAXX][Common.PMAXY];
			Common.personalities = new Personality[Common.PMAXX][Common.PMAXY];
			System.err.println("pwidth = "+Common.PMAXX+" pheight = "+Common.PMAXY);
			int y = 0;
			int x = 0;
			Common.personalities[x][y] = new EmptyP();
			for (x = 1; x < Common.PMAXX-1; x++) {
				Common.personalities[x][y] = new BufferP();
			}
			Common.personalities[x][y] = new EmptyP();
			for (y = 1; y < Common.PMAXY-1; y++) {
				x = 0;
				Common.personalities[x][y] = new BufferP();
				for (x = 1; x < Common.PMAXX-1; x++) {

					Common.personalities[x][y] = new EmptyP();
				}
				x = Common.PMAXX-1;
				Common.personalities[x][y] = new BufferP();
			}
			y = Common.PMAXY-1;
			x = 0;
			Common.personalities[x][y] = new EmptyP();
			for (x = 1; x < Common.PMAXX-1; x++) {
				Common.personalities[x][y] = new BufferP();
			}
			Common.personalities[x][y] = new EmptyP();
			// movePersonalities();
			cellsView.removeAll();
			addCells();
			cellsView.invalidate();
			cellsView.validate();
			cellsView.repaint();
			ask.setVisible(false);
		} catch (Exception e) {
			e.printStackTrace();
			ask.setVisible(false);
		}
	}
	public static void saveModule(PrintStream oos) {
		try {
			oos.println(Common.PMAXX-2);
			oos.println(Common.PMAXY-2);
			for (int y = 1; y < Common.PMAXY-1; y++) {
				for (int x = 1; x < Common.PMAXX-1; x++) {
					Personality p = Common.personalities[x][y];
					if (p != null) {
						System.err.println(p.getClass().getName());
						oos.println(p.getClass().getName());
						oos.println(x);
						oos.println(y);
						oos.println(p.lOutput);
						oos.println(p.rOutput);
						oos.println(p.tOutput);
						oos.println(p.bOutput);
						oos.println(p.lFull);
						oos.println(p.rFull);
						oos.println(p.tFull);
						oos.println(p.bFull);
					}
				}
			}
			oos.println("-------------");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Save");
		}
	}
	public static void saveMachine(StringBuffer sb) {
		try {
			sb.append("<html>");
			sb.append("<body>");
			saveModule(sb);
			sb.append("</body>");
			sb.append("</html>");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Save");
		}
	}
	public static void saveInputs(PrintStream oos) {
		try {
			oos.println(Common.PMAXX);
			oos.println(Common.PMAXY);
			for (int y = 0; y < Common.PMAXY; y++) {
				for (int x = 0; x < Common.PMAXX; x++) {
					if (Common.personalities[x][y] instanceof BufferP) {
						BufferP bp = (BufferP)Common.personalities[x][y];
						if (bp != null) {
							oos.println(x);
							oos.println(y);
							oos.println(bp.getIn());
							oos.println(bp.getOut());
						}
					}
				}
			}
			oos.println("-------------");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Save");
		}
	}
	public static void saveMachine(PrintStream oos) {
		try {
			saveInputs(oos);
			saveModule(oos);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Save");
		}
	}
	public static void saveModule(StringBuffer sb) {
		try {
			sb.append("<table>");
			for (int y = 0; y < Common.PMAXY; y++) {
				sb.append("<tr>");
				for (int x = 0; x < Common.PMAXX; x++) {
					Personality p = Common.personalities[x][y];
					String persName = "P";
					if (p != null) {
						persName = p.getClass().getName();
					}
					persName = persName.substring(persName.indexOf(".")+1);
					persName = persName.substring(0, persName.length()-1);
					if (p instanceof BufferP) {
						String in = "'"+((BufferP)p).getIn();
						if (!in.equals("'")) {
							persName = "'"+((BufferP)p).getIn();  // grab the input buffer
						}
					}
					sb.append("<td>"+persName+"</td>");
				}
				sb.append("</tr>");
			}
			sb.append("</table>");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Failed to Save");
		}
	}
	public void run() {
		Common.changed = false;
		for(int i = 0; Common.forever; i++) {
			step();
			if (!Common.changed) {
				break;
			}
		}
	}
	public void step() {
		int nt = 1;
		try {
			nt = Integer.parseInt(numTimes.getText());
		} catch (Exception e) {
			Common.forever = true;
		}
		int intv = 0;
		try {
			intv = Integer.parseInt(interval.getText());
		} catch (Exception e) {
		}
		int y = 0;
		int x = 0;
		Common.changed = false;
		for(int i = 0; i < nt; i++) {
			if (Common.color) {
				// black squares in checker board
				for (y = 0; y < Common.PMAXY; y+=2) {
					for (x = 0; x < Common.PMAXX; x+=2) {
						Personality p = Common.personalities[x][y];
						if (p != null) {
							p.step(x, y);
						}
					}
				}
				for (y = 1; y < Common.PMAXY; y+=2) {
					for (x = 1; x < Common.PMAXX; x+=2) {
						Personality p = Common.personalities[x][y];
						if (p != null) {
							p.step(x, y);
						}
					}
				}
				Common.color = false;
			} else {
				// red squares in checker board
				for (x = 1; x < Common.PMAXX; x+=2) {
					for (y = 0; y < Common.PMAXY; y+=2) {
						Personality p = Common.personalities[x][y];
						if (p != null) {
							p.step(x, y);
						}
					}
				}
				for (x = 0; x < Common.PMAXX; x+=2) {
					for (y = 1; y < Common.PMAXY; y+=2) {
						Personality p = Common.personalities[x][y];
						if (p != null) {
							p.step(x, y);
						}
					}
				}
				Common.color = true;
			}
			/*
			if (!Common.changed) {
				break;
			}
			*/
			try {
				for (y = 0; y < Common.PMAXY; y++) {
					for (x = 0; x < Common.PMAXX; x++) {
						Common.cells[x][y].repaint();
					}
				}
				Thread.sleep(intv * 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		for (y = 0; y < Common.PMAXY; y++) {
			for (x = 0; x < Common.PMAXX; x++) {
				Common.cells[x][y].repaint();
			}
		}
		if (!Common.changed) {
			if (stopdlg == null) {
				stopdlg = new JFrame("Simulation stopped");
				JPanel jp = new JPanel();
				JLabel jl = new JLabel("Simulation stopped");
				JButton jb = new JButton("OK");
				class Confirm implements ActionListener {
					JFrame stopdlg = null;
					public Confirm(JFrame stopdlg) {
						this.stopdlg = stopdlg;
					}
					public void actionPerformed(ActionEvent ae) {
						stopdlg.setVisible(false);
					}
				}
				jb.addActionListener(new Confirm(stopdlg));
				jp.add(jl);
				jp.add(jb);
				stopdlg.getContentPane().add("Center", jp);
				stopdlg.pack();
			}
			stopdlg.setVisible(true);
		}
	}
	public void setPersonalityFromPopup(int x, int y) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
		int pc = 0;
		for (; pc < personalityClasses.length; pc++) {
			System.err.println("per class index "+pc+" "+pClass+" "+personalityClasses[pc]);
			if (pClass.equals(personalityClasses[pc])) {
				pc++;
				classIndex = pc % personalityClasses.length;
				System.err.println("per class index2 "+pc+" "+pClass+" "+personalityClasses[classIndex]);
				System.err.println("Class index "+classIndex);
				pClass = personalityClasses[classIndex];
				break;
			}
		}
		System.err.println("Setting personality "+x+" "+y+" "+pClass);

	     	if (!"impactVL.Module".equals(pClass)) {
			Personality p = (Personality)(Class.forName(pClass).getDeclaredConstructor().newInstance());
			Common.personalities[x+Common.startx][y+Common.starty] = p;
			setPersonality(p);
			repaint();
		} else {
			System.err.println("Not sure what to do about impactVL.Module 'personality'");
		}
	}
	static public void addCells() {
		for (int y = 0; y < Common.PMAXY; y++) {
			for (int x = 0; x < Common.PMAXX; x++) {
			//	if (Common.personalities[x][y] != null && Common.cells[x][y] == null) {
					Cell c = Common.cells[x][y] = new Cell(x, y);
					c.setPersonality(Common.personalities[x][y]);
					c.repaint();
					cellsView.add(c);
			//	}
			}
		}
	}
}
