import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import javax.swing.*;

public class Main implements GLEventListener {
    public static void main(String[] args) {
        GLProfile profile = GLProfile.get(GLProfile.GL3);
        GLCapabilities caps = new GLCapabilities(profile);
        GLCanvas canvas = new GLCanvas(caps);

        Main listener = new Main();
        canvas.addGLEventListener(listener);

        JFrame frame = new JFrame("JogAmp Test");
        frame.getContentPane().add(canvas);
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        canvas.requestFocusInWindow();
    }

    @Override public void init(GLAutoDrawable d) {
        GL3 gl = d.getGL().getGL3();
        gl.glClearColor(0.2f, 0.4f, 0.6f, 1.0f);
    }
    @Override public void display(GLAutoDrawable d) {
        d.getGL().getGL3().glClear(GL.GL_COLOR_BUFFER_BIT);
    }
    @Override public void reshape(GLAutoDrawable d, int x, int y, int w, int h) {}
    @Override public void dispose(GLAutoDrawable d) {}
}
