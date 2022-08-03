package impact;

import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Dimension;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.nio.*;
import java.awt.event.*;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.awt.*;
import com.jogamp.opengl.util.*;
import com.jogamp.common.nio.Buffers;

/**
 * Impact3D.java <BR>
 * author: Brian Paul (converted to Java by Ron Cemer and Sven Goethel) <P>
 * author: John Carlson (derived from gears demo and picking demo)
 *
 * This version is equal to Brian Paul's version 1.2 1999/10/21
 */

public class Impact3D implements GLEventListener, MouseListener, MouseMotionListener {
  private GLU glu = new GLU();
  private GLCapabilities caps;
  static public Random random = new Random();
  private int key;
  static int name = 1;
  private float asp = 1;

  static ObjectUtility<GraphObject> objects = new ObjectUtility<GraphObject>();

  static boolean shifting = false;
  static boolean control = false;
  private boolean cut = false;
  public static void main(String[] args) {
	try {
		Proxy.getProxy();
		new Impact3D();
	} catch (Throwable t) {
		t.printStackTrace();
	}
  }
  public Impact3D() {
    Frame frame = new Frame("Impact 3D");
    MenuBar menubar = new MenuBar();
    Menu file = new Menu("File");
    menubar.add(file);
    MenuItem save = new MenuItem("Save", new MenuShortcut(KeyEvent.VK_S));
    file.add(save);
    save.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
		save();
	}
    });
    MenuItem newPoint = new MenuItem("New Point", new MenuShortcut(KeyEvent.VK_N));
    file.add(newPoint);
    newPoint.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
		newPoint();
	}
    });
    MenuItem newPolygon = new MenuItem("New Polygon", new MenuShortcut(KeyEvent.VK_O));
    file.add(newPolygon);
    newPolygon.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
		newPolygon();
	}
    });
    MenuItem newTriangle = new MenuItem("New Triangle", new MenuShortcut(KeyEvent.VK_T));
    file.add(newTriangle);
    newTriangle.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
		objects.select(newPoint());
		objects.select(newPoint());
		objects.select(newPoint());
		newPolygon();
	}
    });
    MenuItem newQuadrilateral = new MenuItem("New Quadrilateral", new MenuShortcut(KeyEvent.VK_Q));
    file.add(newQuadrilateral);
    newQuadrilateral.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
		objects.select(newPoint());
		objects.select(newPoint());
		objects.select(newPoint());
		objects.select(newPoint());
		newPolygon();
	}
    });
    Menu edit = new Menu("Edit");
    menubar.add(edit);

    MenuItem cut = new MenuItem("Cut", new MenuShortcut(KeyEvent.VK_X));
    edit.add(cut);
    cut.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
		cut();
	}
    });
    MenuItem copy = new MenuItem("Copy", new MenuShortcut(KeyEvent.VK_C));
    edit.add(copy);
    copy.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
		copy();
	}
    });
    MenuItem paste = new MenuItem("Paste", new MenuShortcut(KeyEvent.VK_V));
    edit.add(paste);
    paste.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
		paste();
	}
    });
    MenuItem selectAll = new MenuItem("Select All", new MenuShortcut(KeyEvent.VK_A));
    edit.add(selectAll);
    selectAll.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
		selectAll();
	}
    });

    MenuItem home = new MenuItem("Reset Locations", new MenuShortcut(KeyEvent.VK_L));
    edit.add(home);
    home.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
		untranslate();
	}
    });
    MenuItem view = new MenuItem("Reset View", new MenuShortcut(KeyEvent.VK_R));
    edit.add(view);
    view.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent ae) {
		resetView();
	}
    });
    frame.setMenuBar(menubar);
    caps = new GLCapabilities(GLProfile.getDefault());
    GLCanvas canvas = new GLCanvas(caps);

    canvas.addGLEventListener(this);
    canvas.addKeyListener(new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
		GLAutoDrawable drawable = (GLAutoDrawable)e.getSource();
		GL gl = (GL)drawable.getGL();
		key = e.getKeyCode();
		float x = 0f;
		float y = 0f;
		float z = 0f;
		switch (key) {
		case KeyEvent.VK_UP:
			if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) != 0) {
				y = 0.5f;
			} else {
				z = -0.5f;
			}
			break;
		case KeyEvent.VK_DOWN:
			if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) != 0) {
				y = -0.5f;
			} else {
				z = 0.5f;
			}
			break;
		case KeyEvent.VK_LEFT:
			x = -0.5f;
			break;
		case KeyEvent.VK_RIGHT:
			x = 0.5f;
			break;
		}
		if (objects.selectedSize() > 0) {
			//System.err.println("x= "+x+" y= "+y+" z= "+z);
			translateSelection(x, y, z);
		} else {
			//System.err.println("vx= "+view_posx+" vy= "+view_posy+" vz= "+view_posz);
			view_posx += x;
			view_posy += y;
			view_posz += z;
		}
    		cmd = UPDATE;
	}
    });
    frame.add(canvas);
    frame.setSize(1024, 768);
    final Animator animator = new Animator(canvas);
    frame.addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          // Run this on another thread than the AWT event queue to
          // make sure the call to Animator.stop() completes before
          // exiting
          new Thread(new Runnable() {
              public void run() {
                animator.stop();
              }
            }).start();
	   Proxy.getProxy().close();
          System.exit(0);
        }
      });
    frame.show();
    try {
    	animator.start();
    } catch (GLException e) {
    	e.printStackTrace();
    }
  }
  void untranslate() {
    cmd = UPDATE;
    objects.untranslate();
  }
  void selectAll() {
    cmd = UPDATE;
    objects.selectAll();
  }
  void dragSelection(float dx, float dy) {
	translateSelection(dx, dy, 0);
  }
  void translateSelection(float x, float y, float z) {
	objects.translateSelection(x, y, z);
  }
  void save() {
        cmd = UPDATE;
	objects.save();
  }
  void paste() {
	cmd = UPDATE;
	objects.paste();
  }
  void copy() {
	cut = false;
	objects.copy();
  }
  void cut() {
	cmd = UPDATE;
	cut = true;
	objects.cut();
  }

  private float view_rotx = 0.0f, view_roty = 0.0f, view_rotz = 0.0f;
  private float view_posx = 0.0f, view_posy = 0.0f, view_posz = 0.0f;
  private int prevMouseX, prevMouseY;
  private boolean mouseRButtonDown = false;

    static final int NOTHING = 0, UPDATE = 1, SELECT = 2;
    static int cmd = UPDATE;

  public Polygon newPolygon() {
	cmd = UPDATE;
	Polygon arc = new Polygon(Impact3D.name++, objects.selected, true);
	objects.add(arc);
	objects.selected.clear();
	return arc;
  }
  public Point newPoint() {
    Point point;
    cmd = UPDATE;
    objects.add(point = new Point(Impact3D.name++, true));
    return point;
  }
  public void resetView() {
    cmd = UPDATE;
    view_rotx = 0;
    view_roty = 0;
    view_posx = 0.0f;
    view_posy = 0.0f;
    view_posz = 0.0f;
  }

  public void init(GLAutoDrawable drawable) {
    // Use debug pipeline
    // drawable.setGL(new DebugGL(drawable.getGL()));
    //System.err.println("init drawable= "+drawable);

    GL2 gl = (GL2)drawable.getGL();
    System.err.println("INIT GL IS: " + gl.getClass().getName());

    //gl.setSwapInterval(1);


/*
    gl.glEnable(GL2.GL_LIGHTING);
    gl.glEnable(GL2.GL_LIGHT0);
    gl.glEnable(GL2.GL_NORMALIZE);
    float pos[] = { 5.0f, 5.0f, 10.0f, 0.0f };

    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, pos, 0);
    gl.glEnable(GL2.GL_CULL_FACE);
*/
    gl.glEnable(GL2.GL_DEPTH_TEST);
    gl.glShadeModel(GL2.GL_SMOOTH);
                
    ((ComponentEvents)drawable).addMouseListener(this);
    ((ComponentEvents)drawable).addMouseMotionListener(this);
  }
    
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    //System.err.println("reshape drawable= "+drawable);
    GL2 gl = (GL2)drawable.getGL();

    asp = (float)height / (float)width;
            
    gl.glViewport(0, 0, width, height);
    gl.glMatrixMode(GL2.GL_PROJECTION);

    //System.err.println("GL_VENDOR: " + gl.glGetString(GL.GL_VENDOR));
    //System.err.println("GL_RENDERER: " + gl.glGetString(GL.GL_RENDERER));
    //System.err.println("GL_VERSION: " + gl.glGetString(GL.GL_VERSION));

    gl.glLoadIdentity();
    glu.gluPerspective(45.0, (float) width / (float) height, 1.0, 600.0);
    // gl.glFrustum(-1.0f, 1.0f, -asp, asp, 5.0f, 600.0f);

    gl.glMatrixMode(GL2.GL_MODELVIEW);
    gl.glLoadIdentity();
    gl.glTranslatef(0.0f, 0.0f, -40.0f);
  }

  public void drawScene(GLAutoDrawable drawable) {
    GL2 gl = (GL2)drawable.getGL();
    gl.glMatrixMode(GL2.GL_MODELVIEW);
    gl.glInitNames();
/*
    if ((drawable instanceof GLJPanel) &&
        !((GLJPanel) drawable).isOpaque() &&
        ((GLJPanel) drawable).shouldPreserveColorBufferIfTranslucent()) {
      gl.glClear(GL.GL_DEPTH_BUFFER_BIT);
    } else {
*/
      gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
/*
    }
    gl.glClear(GL.GL_COLOR_BUFFER_BIT);
*/
            
            
    gl.glPushMatrix();
    gl.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
    gl.glRotatef(view_roty, 0.0f, 1.0f, 0.0f);
    gl.glRotatef(view_rotz, 0.0f, 0.0f, 1.0f);
    gl.glTranslated(view_posx, view_posy, view_posz);
    gl.glPushMatrix();
    Vect otrans = null;
    objects.draw(gl);
    gl.glPopMatrix();
    gl.glPopMatrix();
    drawable.swapBuffers();
  }
    public void dispose(GLAutoDrawable drawable) {
                // TODO
    }
    public void display(GLAutoDrawable drawable) 
    {
      switch(cmd)
        {
        case UPDATE:
	  //System.err.println("UPDATE");
          drawScene(drawable);
	  cmd = NOTHING;
          break;
        case SELECT:
	  // This is the picking code
          GL2 gl = (GL2)drawable.getGL();
	  //System.err.println("SELECT");
          int buffsize = 512;
          double x = (double) prevMouseX, y = (double) prevMouseY;
          int[] viewPort = new int[4];
          IntBuffer selectBuffer = Buffers.newDirectIntBuffer(buffsize);
          int hits = 0;
          gl.glSelectBuffer(buffsize, selectBuffer);
          gl.glRenderMode(GL2.GL_SELECT);
          gl.glMatrixMode(GL2.GL_PROJECTION);
          gl.glPushMatrix();
          gl.glLoadIdentity();
          gl.glGetIntegerv(GL.GL_VIEWPORT, viewPort, 0);

          glu.gluPickMatrix(x, (double) viewPort[3] - y, 0.5, 0.5, viewPort, 0);
          //glu.gluOrtho2D(0.0d, 1.0d, 0.0d, 1.0d);
    	  glu.gluPerspective(45.0, 1/asp, 1.0, 600.0);
          // gl.glFrustum(-1.0f, 1.0f, -asp, asp, 5.0f, 600.0f);
          drawScene(drawable);
          gl.glMatrixMode(GL2.GL_PROJECTION);
          gl.glPopMatrix();
          gl.glFlush();
          hits = gl.glRenderMode(GL2.GL_RENDER);
          processHits(hits, selectBuffer);
          cmd = NOTHING;
          break;
        case NOTHING:
	  drawable.setAutoSwapBufferMode(false);
	  break;
        }
    }

    public void processHits(int hits, IntBuffer buffer)
    {
      System.out.println("---------------------------------");
/*
      System.out.println(" HITS: " + hits);
*/
      int offset = 0;
      int names;
      float z1, z2;
      boolean found = false;
      if (!shifting && !control) {
	objects.clearSelected();
      }
      for (int i=0;i<hits;i++) {
          names = buffer.get(offset); offset++;
          z1 = (float) buffer.get(offset) / 0x7fffffff; offset++;
          z2 = (float) buffer.get(offset) / 0x7fffffff; offset++;
/*
          System.out.println("- - - - - - - - - - - -");
          System.out.println(" hit: " + (i + 1));
          System.out.println(" number of names: " + names);
          System.out.println(" z1: " + z1);
          System.out.println(" z2: " + z2);
          System.out.println(" names: ");
*/

          for (int j=0;j<names;j++) {
	      int nm = buffer.get(offset);
	      //System.err.println("buffer name "+nm);
              if (j==(names-1)) {
		found = objects.select(nm);
              }
              offset++;
            }
          //System.out.println("- - - - - - - - - - - -");
        }
      //System.out.println("---------------------------------");
    }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}

  public void debug(String axis, double x1, double x2, double rho) {
   double d = (x1-x2);
   double a = d/rho;
   System.err.println(axis+"1-"+axis+"2 "+d+" "+axis+"1-"+axis+"2/rho "
	+a+" acos "+(int)(Math.acos(a)*180/Math.PI)+" asin "+(int)(Math.asin(a)*180/Math.PI));
   d = (x2-x1);
   a = d/rho;
   System.err.println(axis+"2-"+axis+"1 "+d+" "+axis+"2-"+axis+"1/rho "
	+a+" acos "+(int)(Math.acos(a)*180/Math.PI)+" asin "+(int)(Math.asin(a)*180/Math.PI));
  }


  // Methods required for the implementation of MouseListener
  public void mouseEntered(MouseEvent e) {
    cmd = UPDATE;
  }
  public void mouseExited(MouseEvent e) {
    // resetView();
  }
  public void mousePressed(MouseEvent e) {
    cmd = SELECT;
    prevMouseX = e.getX();
    prevMouseY = e.getY();
    if ((e.getModifiers() & e.BUTTON3_MASK) != 0) {
      mouseRButtonDown = true;
    }
    setModifiers(e);
  }
    
  public void mouseReleased(MouseEvent e) {
    cmd = UPDATE;
    if ((e.getModifiers() & e.BUTTON3_MASK) != 0) {
      mouseRButtonDown = false;
    }
  }
  public void setModifiers(InputEvent e) {
    if ((e.getModifiersEx() & KeyEvent.SHIFT_DOWN_MASK) != 0) {
	shifting = true;
    } else {
	shifting = false;
    }
    if ((e.getModifiersEx() & (KeyEvent.CTRL_DOWN_MASK|KeyEvent.ALT_DOWN_MASK|KeyEvent.META_DOWN_MASK)) != 0) {
	control = true;
    } else {
	control = false;
    }
  }
    
  public void mouseClicked(MouseEvent e) {
    cmd = UPDATE;
/*
    cmd = SELECT;
    prevMouseX = e.getX();
    prevMouseY = e.getY();
    setModifiers(e);
*/
  }
    
  // Methods required for the implementation of MouseMotionListener
  public void mouseMoved(MouseEvent e) {
    cmd = UPDATE;
  }
  public void rotateView(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    Dimension size = e.getComponent().getSize();

    float thetaY = 360.0f * ( (float)(x-prevMouseX)/(float)size.width);
    float thetaX = 360.0f * ( (float)(prevMouseY-y)/(float)size.height);
    
    prevMouseX = x;
    prevMouseY = y;

    view_rotx += thetaX;
    view_roty += thetaY;
  }
    
  public void mouseDragged(MouseEvent e) {
    cmd = UPDATE;
	if (objects.selectedSize() > 0) {
    		int x = e.getX();
    		int y = e.getY();
    		Dimension size = e.getComponent().getSize();
		float dx = (float)(x-prevMouseX)/(float)size.width*15.5f;
		float dy = (float)(prevMouseY-y)/(float)size.height*11.5f;
	        prevMouseX = x;
	        prevMouseY = y;
		dragSelection(dx, dy);
        } else {
		rotateView(e);
	}
  }
} 
