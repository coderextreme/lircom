package lircom;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import javax.media.opengl.*;

import demos.util.*;
import gleem.*;
import gleem.linalg.*;

/**
  Wavelength-dependent refraction demo<br>
  It's a chromatic aberration!<br>
  sgreen@nvidia.com 4/2001<br><p>

  Currently 3 passes - could do it in 1 with 4 texture units<p>

  Ported to Java, Swing and ARB_fragment_program by Kenneth Russell
*/

public class Orbit2 extends GLJPanel implements WindowListener {
  private boolean useRegisterCombiners;
  private Animator animator;
  private volatile boolean quit;

  static public void main(String args[]) {
        JFrame jf = new JFrame("JOGL Test Case");
	Orbit2 canvas = new Orbit2();
	jf.getContentPane().add(canvas);
	jf.setSize(800,500);
	jf.setVisible(true);
	jf.addWindowListener(canvas);
  }
public void 	windowActivated(java.awt.event.WindowEvent e) {}
public void 	windowClosed(java.awt.event.WindowEvent e) {}
public void 	windowClosing(java.awt.event.WindowEvent e) { System.exit(0); }
public void 	windowDeactivated(java.awt.event.WindowEvent e) {}
public void 	windowDeiconified(java.awt.event.WindowEvent e) {}
public void 	windowIconified(java.awt.event.WindowEvent e) {}
public void 	windowOpened(java.awt.event.WindowEvent e) {}

  public Orbit2() {
    super(new GLCapabilities(), null, null);
    setSize(800, 500);
    addGLEventListener(new Listener());
    addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          requestFocus();
        }
      });
    animator = new Animator();
    animator.add(this);
    animator.start();
  }

  class Listener implements GLEventListener {
    private boolean firstRender = true;
    private int vtxProg;
    private int fragProg;
    private int cubemap;
    private int bunnydl;
    private int obj;

    private ExaminerViewer viewer;

    private Time  time = new SystemTime();
    private float animRate = (float) Math.toRadians(-6.0f); // Radians / sec

    private float refract = 1.1f;           // ratio of indicies of refraction
    private float wavelengthDelta = 0.05f;  // difference in refraction for each "wavelength" (R,G,B)
    private float fresnel = 2.0f;           // Fresnel multiplier

    private boolean wire = false;
    private boolean toggleWire = false;


class coord {
	float x;
	float y;
	float z;
};


int resolution = 100;
float e = 5;
float f = 5;
float g = 5;
float h = 5;

coord points[] = new coord[resolution*resolution];
coord oldpoints[] = new coord[resolution*resolution];
coord morphpoints[] = new coord[resolution*resolution];
coord facenormals[][] = new coord[resolution*resolution][6];
coord vertexnormals[] = new coord[resolution*resolution];

    public void init(GLAutoDrawable drawable) {
      GL gl = drawable.getGL();
      // GLU glu = drawable.getGLU();
      float cc = 0.0f;
      gl.glClearColor(cc, cc, cc, 1);
      gl.glColor3f(1,1,1);
      gl.glEnable(GL.GL_DEPTH_TEST);

      gl.glDisable(GL.GL_CULL_FACE);
      if (firstRender) {
        firstRender = false;

        drawable.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
              dispatchKey(e.getKeyChar());
            }
          });

        // Register the window with the ManipManager
        ManipManager manager = ManipManager.getManipManager();
        manager.registerWindow(drawable);

        viewer = new ExaminerViewer(MouseButtonHelper.numMouseButtons());
        viewer.setNoAltKeyMode(true);
        viewer.attach(drawable, new BSphereProvider() {
            public BSphere getBoundingSphere() {
              return new BSphere(new Vec3f(0, 0, 0), 50.0f);
            }
          });
        viewer.setVertFOV((float) (15.0f * Math.PI / 32.0f));
        viewer.setZNear(-10.0f);
        viewer.setZFar(10.0f);
      }

	gl.glShadeModel (GL.GL_SMOOTH);
	gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, new float[] { 1.0f, 1.0f, 1.0f, 1.0f }, 0);
	gl.glMaterialfv(GL.GL_FRONT, GL.GL_SHININESS, new float[] { 50.0f }, 0);
	gl.glLightfv(GL.GL_LIGHT0, GL.GL_POSITION, new float[] { -1.0f, -1.0f, 1.0f, 0.0f }, 0);

	gl.glEnable(GL.GL_LIGHTING);
	gl.glEnable(GL.GL_LIGHT0);
     int i;
     int j;
    for ( i = 0; i < resolution; i++) {
       for ( j = 0; j < resolution; j++) {
	       points[i*resolution+j] = new coord();
	       oldpoints[i*resolution+j] = new coord();
	       morphpoints[i*resolution+j] = new coord();
	       vertexnormals[i*resolution+j] = new coord();
	       facenormals[i*resolution+j] = new coord[6];
	       facenormals[i*resolution+j][0] = new coord();
	       facenormals[i*resolution+j][1] = new coord();
	       facenormals[i*resolution+j][2] = new coord();
	       facenormals[i*resolution+j][3] = new coord();
	       facenormals[i*resolution+j][4] = new coord();
	       facenormals[i*resolution+j][5] = new coord();
      }
    }
    material(gl, GL.GL_FRONT, 0.0215f, 0.1745f, 0.0215f, 0.07568f, 0.61424f, 0.07568f, 0.633f, 0.727811f, 0.633f, 0.6f);
    material(gl, GL.GL_BACK, 1.0f, 1.0f, 1.0f,
		    1.0f, 1.0f, 1.0f,
		    1.0f, 1.0f, 1.0f, 0.6f);
  }
Random random = new Random();
void set_fraction() {
	int choice = random.nextInt(4);
	switch (choice) {
	case 0:
		e += random.nextInt(2) * 2 - 1;
		break;
	case 1:
		f += random.nextInt(2) * 2 - 1;
		break;
	case 2:
		g += random.nextInt(2) * 2 - 1;
		break;
	case 3:
		h += random.nextInt(2) * 2 - 1;
		break;
	}
	if (e < -20) {
		e = -20;
	}
	if (e > 20) {
		e = 20;
	}
	if (f < -20) {
		f = -20;
	}
	if (f > 20) {
		f = 20;
	}
	if (g < 1) {
		g = 1;
	}
	if (g > 12) {
		g = 5;
	}
	if (h < 1) {
		h = 1;
	}
	if (h > 12) {
		h = 5;
	}
}

void generateCoordinates() {
     float theta = 0.0f;
     float phi = 0.0f;
     float delta = (2f * 3.141592653f) / (resolution-1);
     int i;
     int j;

     set_fraction();
     for ( i = 0; i < resolution; i++) {
	for ( j = 0; j < resolution; j++) {
		float rho = e + f * (float)Math.cos(g * theta) * (float)Math.cos(h * phi);
		points[i*resolution+j].x = rho * (float)Math.cos(phi) * (float)Math.cos(theta);
		points[i*resolution+j].y = rho * (float)Math.cos(phi) * (float)Math.sin(theta);
		points[i*resolution+j].z = rho * (float)Math.sin(phi);
		theta += delta;
	}
	phi += delta;
     }
}
void material(GL gl, int frontback, float ambr, float ambg, float ambb,
	     float difr, float difg, float difb,
	     float specr, float specg, float specb, float shine)
{
	gl.glMaterialfv(frontback, GL.GL_AMBIENT, new float[] { ambr, ambg, ambb, 1.0f }, 0);
	gl.glMaterialfv(frontback, GL.GL_DIFFUSE, new float[] { difr, difg, difb, 1.0f }, 0);
	gl.glMaterialfv(frontback, GL.GL_SPECULAR, new float[] {specr, specg, specb, 1.0f }, 0);
	gl.glMaterialf(frontback, GL.GL_SHININESS, shine * 128.0f);
}

void averagenormals(coord normal,
		coord normal1, coord normal2, coord normal3,
		coord normal4, coord normal5, coord normal6) {
		normal.x = (normal1.x + normal2.x + normal3.x + normal4.x + normal5.x + normal6.x) / 6;
		normal.y = (normal1.y + normal2.y + normal3.y + normal4.y + normal5.y + normal6.y) / 6;
		normal.z = (normal1.z + normal2.z + normal3.z + normal4.z + normal5.z + normal6.z) / 6;
}

coord ab = new coord();
coord ac = new coord();

void compute_normal(coord normal, coord point0, coord point1, coord point2)  {
	ab.x = point1.x - point0.x;
	ab.y = point1.y - point0.y;
	ab.z = point1.z - point0.z;
	ac.x = point2.x - point0.x;
	ac.y = point2.y - point0.y;
	ac.z = point2.z - point0.z;
	normal.x = ab.y * ac.z - ab.z * ac.y;
	normal.y = ab.z * ac.x - ab.x * ac.z;
	normal.z = ab.x * ac.y - ab.y * ac.x;
}

void morph(int i, float m) {
	morphpoints[i].x = (points[i].x - oldpoints[i].x) * m + oldpoints[i].x;
	morphpoints[i].y = (points[i].y - oldpoints[i].y) * m + oldpoints[i].y;
	morphpoints[i].z = (points[i].z - oldpoints[i].z) * m + oldpoints[i].z;
}

public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {}

public void display(GLAutoDrawable drawable) {
      GL gl = drawable.getGL();
      // GLU glu = drawable.getGLU();
      gl.glClear(GL.GL_COLOR_BUFFER_BIT|GL.GL_DEPTH_BUFFER_BIT);
      viewer.update(gl);
      ManipManager.getManipManager().updateCameraParameters(drawable, viewer.getCameraParameters());
      ManipManager.getManipManager().render(drawable, gl);

   int i;
   int j;
   float m;

   generateCoordinates();
   for (m = 0; m < 1.0; m+=.0625)
   {
     gl.glClear (GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
     // morph all the points
     for ( i = 0; i < resolution; i++) {
     	for ( j = 0; j < resolution; j++) {
          morph(i*resolution+j, m);
	}
     }
     // compute face normals and vertex normals
     for ( i = 0; i < resolution; i++) {
     	for ( j = 0; j < resolution; j++) {
		int i0 = i * resolution;
		int i0j = i0 + j;
		int ip1 = (i + 1) % resolution * resolution;
		int im1 = (i + resolution - 1) % resolution * resolution;
		int jp1 = (j + 1) % resolution;
		int jm1 = (j + resolution - 1) % resolution;
		compute_normal(facenormals[i0j][0], morphpoints[i0j], morphpoints[i0+jp1], morphpoints[ip1+j]);
		compute_normal(facenormals[i0j][1], morphpoints[i0j], morphpoints[ip1+j], morphpoints[ip1+jm1]);
		compute_normal(facenormals[i0j][2], morphpoints[i0j], morphpoints[ip1+jm1], morphpoints[i0+jm1]);
		compute_normal(facenormals[i0j][3], morphpoints[i0j], morphpoints[i0+jm1], morphpoints[im1+j]);
		compute_normal(facenormals[i0j][4], morphpoints[i0j], morphpoints[im1+j], morphpoints[im1+jp1]);
		compute_normal(facenormals[i0j][5], morphpoints[i0j], morphpoints[im1+jp1], morphpoints[i0+jp1]);
	     averagenormals(vertexnormals[i0j],
		facenormals[i0j][0],
		facenormals[i0j][1],
		facenormals[i0j][2],
		facenormals[i0j][3],
		facenormals[i0j][4],
		facenormals[i0j][5]);
	}
     }
     for ( i = 0; i < resolution-1; i++) {
        gl.glBegin(GL.GL_TRIANGLE_STRIP);
	gl.glColor3f(1.0f, 1.0f, 0.5f);
     	for ( j = 0; j < resolution; j++) {
	     coord point = morphpoints[i*resolution+j];
	     coord normal = vertexnormals[i*resolution+j];
	     gl.glNormal3f(normal.x, normal.y, normal.z);
	     // gl.glNormal3f(point.x, point.y, point.z);
	     gl.glVertex3f(point.x, point.y, point.z);
	     point = morphpoints[(i+1)*resolution+j];
	     normal = vertexnormals[(i+1)*resolution+j];
	     gl.glNormal3f(normal.x, normal.y, normal.z);
	     //gl.glNormal3f(point.x, point.y, point.z);
	     gl.glVertex3f(point.x, point.y, point.z);
	}
   	gl.glEnd();
    }
   }
   for ( i = 0; i < resolution; i++) {
     for ( j = 0; j < resolution; j++) {
	   int i0 = i * resolution;
	   int i0j = i0 + j;
	   oldpoints[i0j].x = points[i0j].x;
	   oldpoints[i0j].y = points[i0j].y;
	   oldpoints[i0j].z = points[i0j].z;
     }
   }
}

public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height)
{
   GL gl = drawable.getGL();
   gl.glViewport (x, y,  width,  height);
   gl.glMatrixMode (GL.GL_PROJECTION);
   gl.glLoadIdentity ();
   gl.glOrtho (-30.0, 30.0, -30.0, 30.0, 30.0, -30.0);
   gl.glMatrixMode (GL.GL_MODELVIEW);
   gl.glLoadIdentity ();
}
    private void dispatchKey(char k) {
    }
}
}
