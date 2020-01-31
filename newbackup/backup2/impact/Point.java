package impact;

import java.util.*;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.*;
import com.jogamp.common.nio.Buffers;

public class Point extends GraphObject {
  	private GLUT glut = new GLUT();
	Vect paths;
	Vect trans;
  	float [] white = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
	float color[];
	Point(int name, boolean useProxy) {
        super(name);
   	    paths = new Vect();
   	    if (useProxy) {
		    paths.x = Impact3D.random.nextFloat()*10-5;
		    paths.y = Impact3D.random.nextFloat()*10-5;
		    paths.z = Impact3D.random.nextFloat()*10-5;
   	    }
            trans = new Vect();
	    color = new float[] { Impact3D.random.nextFloat(), Impact3D.random.nextFloat(), Impact3D.random.nextFloat(), Impact3D.random.nextFloat() };
        // System.err.println("**** NAME = "+this.name);
	    if (useProxy) {
	    	Proxy.getProxy().insert(this);
	        Proxy.getProxy().update(this);
	    }
	}
    public int getName() {
		return this.name;
	}
	public void draw(GL2 gl) {
	   double x2 = paths.x+trans.x;
	   double y2 = paths.y+trans.y;
	   double z2 = paths.z+trans.z;
	   // System.out.println("x2= "+x2+" y2= "+y2+" z2= "+z2);
	   gl.glPushMatrix();
	   gl.glTranslated(x2, y2, z2);
	   gl.glPushName(name);
	   if (Impact3D.objects.selected.contains(this)) {
		   gl.glColor4f(white[0], white[1], white[2], white[3]);
	   } else {
		   gl.glColor4f(color[0], color[1], color[2], color[3]);
	   }
	   glut.glutSolidCube(0.1f);
	   gl.glPopName();
	   gl.glPopMatrix();
	}
	public Object clone() {
		Point node = new Point(Impact3D.name++, true);
		node.paths = (Vect)paths.clone();
		node.trans = (Vect)trans.clone();
		node.color[0] = color[0];
		node.color[1] = color[1];
		node.color[2] = color[2];
		node.color[3] = color[3];
	        Proxy.getProxy().update(node);
		return node;
	}
	public void untranslate() {
		trans.x = 0f;
		trans.y = 0f;
		trans.z = 0f;
	        Proxy.getProxy().update(this);
	}
       void translateSelection(float x, float y, float z) {
		trans.x += x;
		trans.y += y;
		trans.z += z;
	        Proxy.getProxy().update(this);
	}
	void save() {
		paths.x += trans.x;
		paths.y += trans.y;
		paths.z += trans.z;
		trans.x = 0f;
		trans.y = 0f;
		trans.z = 0f;
	        Proxy.getProxy().update(this);
	}
	public void insert(Proxy p) {
		String line = "NODE|"+name+"|INSERT";
		p.send(line);
		receive(line);
	}
	public void remove(Proxy p) {
		String line = "NODE|"+name+"|DELETE";
		p.send(line);
		receive(line);
	}
	public void update(Proxy p) {
		String line = "NODE|"+name+"|UPDATE|"+
			color[0]+"|"+color[1]+"|"+color[2]+"|"+color[3]+"|"+
			paths.x+"|"+paths.y+"|"+paths.z+"|"+
			trans.x+"|"+trans.y+"|"+trans.z;
		p.send(line);
		receive(line);
	}
	static public void receive(String line) {
		System.err.println("parsing "+line);
		String [] params = line.split("\\|");
		// System.err.println(params[0]);
		// System.err.println(params[1]);
		// System.err.println(params[2]);
		boolean found = false;
		if (params[2].startsWith("UPDATE")) {
			int nm = Integer.parseInt(params[1]);
			synchronized (Impact3D.objects.shown) {
		   		Iterator<GraphObject> n = Impact3D.objects.shown.iterator();
				while (n.hasNext()) {
					GraphObject obj = n.next();
					if (obj.name == nm) {
						if (obj instanceof Point) {
							Point node = (Point)obj;
							node.setColor(params);
							node.setPosition(params);
						}
						found = true;
					}
				}
			}
			// if the object being updated wasn't found, insert it!
			if (!found) {
				// System.err.println("From Server "+Impact3D.name);
				if (nm >= Impact3D.name) {
					Impact3D.name = nm+1;
				}
				Point node = new Point(nm, false);
				node.setColor(params);
				node.setPosition(params);
				Impact3D.objects.add(node);
			}
		} else if (params[2].startsWith("INSERT")) {
			int nm = Integer.parseInt(params[1]);
			// System.err.println("From Server "+Impact3D.name);
			if (nm >= Impact3D.name) {
				Impact3D.name = nm+1;
			}
			Point node = new Point(nm, false);
			Impact3D.objects.add(node);
		} else if (params[2].startsWith("DELETE")) {
			int nm = Integer.parseInt(params[1]);
			synchronized (Impact3D.objects.shown) {
		   		Iterator<GraphObject> n = Impact3D.objects.shown.iterator();
				while (n.hasNext()) {
					GraphObject node = n.next();
					if (node.name == nm) {
						n.remove();
						node.getGraphObjects().clear();
					}
				}
			}
		} else if (params[2].startsWith("SELECT")) {
			synchronized (Impact3D.objects.shown) {
		   		Iterator<GraphObject> i = Impact3D.objects.shown.iterator();
				while (i.hasNext()) {
					GraphObject obj = i.next();
					if (obj instanceof Point) {
						Proxy.getProxy().insert(obj);
						Proxy.getProxy().update(obj);
					}
				}
			}
		}
	}
	public void setPosition(String params[]) {
		paths.x = Float.parseFloat(params[7]);
		paths.y = Float.parseFloat(params[8]);
		paths.z = Float.parseFloat(params[9]);
		trans.x = Float.parseFloat(params[10]);
		trans.y = Float.parseFloat(params[11]);
		trans.z = Float.parseFloat(params[12]);

	}
	public void setColor(String params[]) {
		color[0] = Float.parseFloat(params[3]);
		color[1] = Float.parseFloat(params[4]);
		color[2] = Float.parseFloat(params[5]);
		color[3] = Float.parseFloat(params[6]);
	}
        public void printColor(String context, float [] c) {
		System.err.println(context+" "+c[0]+" "+c[1]+" "+c[2]+" "+c[3]);
	}
}
