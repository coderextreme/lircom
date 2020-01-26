package impact;
//=================================================================================
// Picking 0.2                                                       (Thomas Bladh)
//=================================================================================
// A simple picking example using java/jogl. This is far from a complete solution 
// but it should give you an idea of how to include picking in your assigment 
// solutions.
//
// Notes: * Based on example 13-3 (p 542) in the "OpenGL Programming Guide"
//        * This version should handle overlapping objects correctly.
//---------------------------------------------------------------------------------
import java.awt.*;
import java.awt.event.*;
import java.awt.Canvas.*;
import java.nio.*;
import java.util.*;
import javax.media.opengl.*;
import javax.media.opengl.awt.*;
import javax.media.opengl.glu.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.*;
import com.jogamp.common.nio.*;

public class Picking
{
  static GLUT glut = new GLUT();
  static private float view_rotx = 0.0f, view_roty = 0.0f, view_rotz = 0.0f;
  static private float view_posx = 0.0f, view_posy = 0.0f, view_posz = 0.0f;
  static private int name = 1;
  static private char key;
  static Random random = new Random();
  static private Vect ov;
  static private Vect paths[] = new Vect [random.nextInt(5)+2];
  static private float color[][] = new float[paths.length][3];
  public static void main(String[] args) 
  {
    new Picking();
  }
  	
  Picking()
  {
    Frame frame = new Frame("Picking Example");
    GLDrawableFactory factory = GLDrawableFactory.getFactory(GLProfile.getDefault());
    GLCanvas drawable = new GLCanvas();
    Renderer r = new Renderer();
    drawable.addGLEventListener(r);
    drawable.addMouseListener(r);
    drawable.addMouseMotionListener(r);
    frame.add(drawable);
    frame.setSize(400, 400);
    final Animator animator = new Animator(drawable);
    frame.addWindowListener(new WindowAdapter()
      {
        public void windowClosing(WindowEvent e) 
        {
          animator.stop();
          System.exit(0);
        }
      });
    frame.show();
    animator.start();	
  }

  static class Renderer implements GLEventListener, MouseListener, MouseMotionListener 
  {
    static final int NOTHING = 0, UPDATE = 1, SELECT = 2;
    int cmd = UPDATE;
    int mouse_x, mouse_y;
	
    private GLU glu = new GLU();
    private GLAutoDrawable gldrawable;
		
    public void init(GLAutoDrawable drawable) 
    {
      GL2 gl = (GL2)drawable.getGL();
      this.gldrawable = drawable;
      gl.glEnable(gl.GL_CULL_FACE);
      gl.glEnable(gl.GL_DEPTH_TEST);
      gl.glEnable(gl.GL_NORMALIZE);
      gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
      Vect nv = new Vect();
      nv.x = random.nextFloat()*10-5;
      nv.y = random.nextFloat()*10-5;
      nv.z = random.nextFloat()*10-5;
      Vect ov = new Vect();
      ov.x = random.nextFloat()*10-5;
      ov.y = random.nextFloat()*10-5;
      ov.z = random.nextFloat()*10-5;
      for (int i = 0; i < paths.length; i++) {
	    System.err.println("initing paths "+ov.x+" "+nv.x);
            paths[i] = nv;
    	    color[i] = new float[] { random.nextFloat(), random.nextFloat(), random.nextFloat(), random.nextFloat() };
	    ov = nv;
   	    nv = new Vect();
	    nv.x = random.nextFloat()*10-5;
	    nv.y = random.nextFloat()*10-5;
	    nv.z = random.nextFloat()*10-5;
      }
    }
    	
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) 
    {
      GL2 gl = (GL2)drawable.getGL();
      float h = (float) height / (float) width;
      gl.glViewport(0, 0, width, height);
      gl.glMatrixMode(gl.GL_PROJECTION);
      gl.glLoadIdentity();
      // glu.gluOrtho2D(0.0f,1.0f,0.0f,1.0f);
      glu.gluOrtho2D(-10.0f,10.0f,-10.0f,10.0f);
    }
/*
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL2 gl = (GL2)drawable.getGL();

    float h = (float)height / (float)width;
            
    gl.glViewport(0, 0, width, height);
    gl.glMatrixMode(gl.GL_PROJECTION);

    System.err.println("GL_VENDOR: " + gl.glGetString(gl.GL_VENDOR));
    System.err.println("GL_RENDERER: " + gl.glGetString(gl.GL_RENDERER));
    System.err.println("GL_VERSION: " + gl.glGetString(gl.GL_VERSION));
    gl.glLoadIdentity();
    gl.glFrustum(-1.0f, 1.0f, -h, h, 5.0f, 60.0f);
    gl.glMatrixMode(gl.GL_MODELVIEW);
    gl.glLoadIdentity();
    gl.glTranslatef(0.0f, 0.0f, -40.0f);
  }
*/


    public void dispose(GLAutoDrawable drawable) {
                // TODO
    }

    public void display(GLAutoDrawable drawable) 
    {
      GL2 gl = (GL2)drawable.getGL();
      switch(cmd)
        {
        case UPDATE:
          drawScene(gl);
          break;
        case SELECT:
          int buffsize = 512;
          double x = (double) mouse_x, y = (double) mouse_y;
          int[] viewPort = new int[4];
          IntBuffer selectBuffer = Buffers.newDirectIntBuffer(buffsize);
          int hits = 0;
          gl.glGetIntegerv(gl.GL_VIEWPORT, viewPort, 0);
          gl.glSelectBuffer(buffsize, selectBuffer);
          gl.glRenderMode(gl.GL_SELECT);
          gl.glInitNames();
          gl.glMatrixMode(gl.GL_PROJECTION);
          gl.glPushMatrix();
          gl.glLoadIdentity();
          glu.gluPickMatrix(x, (double) viewPort[3] - y, 5.0d, 5.0d, viewPort, 0);
          glu.gluOrtho2D(0.0d, 1.0d, 0.0d, 1.0d);
          drawScene(gl);
          gl.glMatrixMode(gl.GL_PROJECTION);
          gl.glPopMatrix();
          gl.glFlush();
          hits = gl.glRenderMode(gl.GL_RENDER);
          processHits(hits, selectBuffer);
          cmd = UPDATE;
          break;
        }
    }

    public void processHits(int hits, IntBuffer buffer)
    {
      System.out.println("---------------------------------");
      System.out.println(" HITS: " + hits);
      int offset = 0;
      int names;
      float z1, z2;
      for (int i=0;i<hits;i++)
        {
          System.out.println("- - - - - - - - - - - -");
          System.out.println(" hit: " + (i + 1));
          names = buffer.get(offset); offset++;
          z1 = (float) buffer.get(offset) / 0x7fffffff; offset++;
          z2 = (float) buffer.get(offset) / 0x7fffffff; offset++;
          System.out.println(" number of names: " + names);
          System.out.println(" z1: " + z1);
          System.out.println(" z2: " + z2);
          System.out.println(" names: ");

          for (int j=0;j<names;j++)
            {
              System.out.print("       " + buffer.get(offset)); 
              if (j==(names-1))
                System.out.println("<-");
              else
                System.out.println();
              offset++;
            }
          System.out.println("- - - - - - - - - - - -");
        }
      System.out.println("---------------------------------");
    }
		
    public int viewPortWidth(GL2 gl)
    {
      int[] viewPort = new int[4];
      gl.glGetIntegerv(gl.GL_VIEWPORT, viewPort, 0);
      return viewPort[2];
    }

    public int viewPortHeight(GL2 gl)
    {
      int[] viewPort = new int[4];
      gl.glGetIntegerv(gl.GL_VIEWPORT, viewPort, 0);
      return viewPort[3];
    }

  public void drawScene(GL2 gl) {
    gl.glMatrixMode(gl.GL_MODELVIEW);
    gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
            
    gl.glPushMatrix();
    gl.glRotatef(view_rotx, 1.0f, 0.0f, 0.0f);
    gl.glRotatef(view_roty, 0.0f, 1.0f, 0.0f);
    gl.glRotatef(view_rotz, 0.0f, 0.0f, 1.0f);
    gl.glPushMatrix();
    Vect ov = new Vect();
    ov.x += random.nextFloat()*0.1-0.05;
    ov.y += random.nextFloat()*0.1-0.05;
    ov.z += random.nextFloat()*0.1-0.05;
    for (int i = 0; i < paths.length; i++) {
	    gl.glMaterialfv(gl.GL_FRONT, gl.GL_AMBIENT_AND_DIFFUSE, color[i], 0);
	
	    wire(gl, ov, paths[i]);
	    ov = paths[i];
	    paths[i].x += random.nextFloat()*0.1-0.05;
	    paths[i].y += random.nextFloat()*0.1-0.05;
	    paths[i].z += random.nextFloat()*0.1-0.05;
    }

    gl.glPopMatrix();
    gl.glPopMatrix();

    gl.glMatrixMode(gl.GL_PROJECTION);
    //gl.glPushMatrix();
    switch (key) {
    case 'k':
	view_posz = 1;
	key = '\0';
	break;
    case 'j':
	view_posz = -1;
	key = '\0';
	break;
    case 'h':
        view_posx = 1;
	key = '\0';
	break;
    case 'l':
        view_posx = -1;
	key = '\0';
	break;
    }
    gl.glTranslatef(view_posx, view_posy, view_posz);
    view_posx = 0;
    view_posy = 0;
    view_posz = 0;
    //gl.glPopMatrix();
    name = 1;
  }
  public static void wire(GL2 gl, Vect ov, Vect nv) {
   double x1 = ov.x;
   double y1 = ov.y;
   double z1 = ov.z;
   double x2 = nv.x;
   double y2 = nv.y;
   double z2 = nv.z;

   gl.glPushMatrix();
   gl.glTranslated(x1, y1, z1);
   gl.glShadeModel(gl.GL_FLAT);
   gl.glPushName(name++);
   glut.glutSolidCube(1.0f);
   // gl.glPopName();
   gl.glPopMatrix();

   gl.glPushName(name++);
   gl.glBegin(gl.GL_POLYGON);
   double radius = 0.5;
   gl.glVertex3d(x1-radius, y1-radius, z1-radius);
   gl.glVertex3d(x1+radius, y1+radius, z1+radius);
   gl.glVertex3d(x1, y1, z1);
   gl.glVertex3d(x1+radius, y1+radius, z1+radius);
   gl.glVertex3d(x2+radius, y2+radius, z2+radius);
   gl.glVertex3d(x2, y2, z2);
   gl.glVertex3d(x2-radius, y2-radius, z2-radius);
   gl.glEnd();
   // gl.glPopName();
  }
/*
    public void drawScene(GL2 gl)
    {
      gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);

      // Colors
      float red[] =   {1.0f,0.0f,0.0f,1.0f};
      float green[] = {0.0f,1.0f,0.0f,1.0f};
      float blue[] =  {0.0f,0.0f,1.0f,1.0f};
	
      // Red rectangle
      GLRectangleEntity r1 = new GLRectangleEntity(gl, glu);
      r1.x = 0.15f;
      r1.y = 0.25f;
      r1.w = 0.4f;
      r1.h = 0.4f;
      r1.c = red;
      r1.id = 10;
      r1.draw();

      // Green rectangle
      GLRectangleEntity r2 = new GLRectangleEntity(gl, glu);
      r2.x = 0.35f;
      r2.y = 0.45f;
      r2.w = 0.4f;
      r2.h = 0.4f;
      r2.c = green;
      r2.id = 20;
      r2.draw();

      // Blue rectangle
      GLRectangleEntity r3 = new GLRectangleEntity(gl, glu);
      r3.x = 0.45f;
      r3.y = 0.15f;
      r3.w = 0.4f;
      r3.h = 0.4f;
      r3.c = blue;
      r3.id = 30;
      r3.draw();

      gl.glFlush();
    }
*/

    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}
				
    public void mousePressed(MouseEvent e) 
    {
      cmd = SELECT;
      mouse_x = e.getX();
      mouse_y = e.getY();
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}
    
    public abstract class GLEntity
    {
      float x, y, z;
      float[] c;
      int id = 0;
      boolean outline = true;
      GL2 gl;
      GLU glu;
      public GLEntity(GL2 gl, GLU glu)
      {
        this.gl = gl;
        this.glu = glu;
      }
      public void draw()
      {
        gl.glPushName(id);
        _draw();
      }
      public abstract void _draw();
    }

    public class GLRectangleEntity extends GLEntity
    {
      float w = 0.1f;
      float h = 0.1f;
      public GLRectangleEntity(GL2 gl, GLU glu)
      {
        super(gl, glu);
      }
      public void _draw()
      {
        if (outline)
          gl.glPolygonMode(gl.GL_FRONT, gl.GL_LINE);
        else
          gl.glPolygonMode(gl.GL_FRONT, gl.GL_FILL);

        gl.glColor4fv(c, 0);
        gl.glBegin(gl.GL_POLYGON);
        gl.glVertex3f(x, y, z);
        gl.glVertex3f(x + w, y, z);
        gl.glVertex3f(x + w, y + h, z);
        gl.glVertex3f(x, y + h, z);
        gl.glEnd();
      }			
    }
  }
}
