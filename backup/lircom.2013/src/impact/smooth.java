import java.awt.event.*;
import javax.swing.*;

import javax.media.opengl.*;
import javax.media.opengl.awt.*;
import javax.media.opengl.glu.*;
import com.jogamp.opengl.util.gl2.*;

/**
 * This program demonstrates smooth shading. A smooth shaded polygon is drawn in
 * a 2-D projection.
 * 
 * @author Kiet Le (java port)
 */
public class smooth
  extends JFrame
    implements GLEventListener, KeyListener
{
  private GLU glu;
  private GLUT glut;
  private GLCanvas canvas;

  public smooth()
  {
    super("smooth");
  
    canvas = new GLCanvas();
    canvas.addGLEventListener(this);
    canvas.addKeyListener(this);
   
    getContentPane().add(canvas);
  }

  public void run()
  {
    setSize(500, 500);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
    canvas.requestFocusInWindow();
  }

  public static void main(String[] args)
  {
    new smooth().run();
  }

  public void init(GLAutoDrawable drawable)
  {
    GL2 gl = (GL2)drawable.getGL();
    glu = new GLU();
    glut = new GLUT();

    // gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
    gl.glShadeModel(gl.GL_SMOOTH);
  }

  public void display(GLAutoDrawable drawable)
  {
    GL2 gl = (GL2)drawable.getGL();

    gl.glClear(gl.GL_COLOR_BUFFER_BIT);
    triangle(gl);
    gl.glFlush();
  }

  public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h)
  {
    GL2 gl = (GL2)drawable.getGL();

    gl.glViewport(0, 0, w, h);
    gl.glMatrixMode(gl.GL_PROJECTION);
    gl.glLoadIdentity();
    if (w <= h) glu.gluOrtho2D(0.0, 30.0, 0.0, 30.0 * (float) h / (float) w);
    else glu.gluOrtho2D(0.0, 30.0 * (float) w / (float) h, 0.0, 30.0);
    gl.glMatrixMode(gl.GL_MODELVIEW);
  }

  public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
      boolean deviceChanged)
  {
  }

  private void triangle(GL2 gl)
  {
    gl.glBegin(gl.GL_TRIANGLES);
    gl.glColor3f(1.0f, 0.0f, 0.0f);
    gl.glVertex2f(5.0f, 5.0f);
    gl.glColor3f(0.0f, 1.0f, 0.0f);
    gl.glVertex2f(25.0f, 5.0f);
    gl.glColor3f(0.0f, 0.0f, 1.0f);
    gl.glVertex2f(5.0f, 25.0f);
    gl.glEnd();
  }

  public void keyTyped(KeyEvent key)
  {
  }

  public void keyPressed(KeyEvent key)
  {
    switch (key.getKeyCode()) {
      case KeyEvent.VK_ESCAPE:
        System.exit(0);
        break;

      default:
        break;
    }
  }

  public void keyReleased(KeyEvent key)
  {
  }

  public void dispose(GLAutoDrawable drawable) {
                // TODO
  }

}
