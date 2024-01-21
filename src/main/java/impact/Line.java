package impact;

import java.util.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.*;
import com.jogamp.common.nio.Buffers;

public  class Line extends GraphObject {
  	float [] white = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
	Line(String name, List<GraphObject> nodes, boolean useProxy) {
            super(name);
	    if (nameToNumber.get(name) == null) {
		nameToNumber.put(name, Integer.parseInt(name));
	    }
	    for (GraphObject node : nodes) {
			// segments connect nodes
		    if (node instanceof Point) {
			    addGraphObject(node);
			    node.addGraphObject(this);
		    }
	    }
	    if (useProxy) {
	        Proxy.getProxy().insert(this);
	    }
	}
        public String getName() {
		return this.name;
	}
	public void draw(GL2 gl) {
	
	   gl.glPushName(nameToNumber.get(name));
	   gl.glBegin(gl.GL_LINES);
	   List<GraphObject> objs = getGraphObjects();
	   int size = objs.size(); 
	   for (int i = 0; i < size; i++) {
		GraphObject obj = (GraphObject)objs.get(i);
		if (obj instanceof Point) {
			Point node = (Point)obj;
			double x = node.paths.x+node.trans.x;
			double y = node.paths.y+node.trans.y;
			double z = node.paths.z+node.trans.z;
		   if (Impact3D.objects.selected.contains(this)) {
			   gl.glColor4f(white[0], white[1], white[2], white[3]);
		   } else {
			   gl.glColor4f(node.color[0], node.color[1], node.color[2], node.color[3]);
		   }
		   gl.glVertex3d(x, y, z);
		}
	   }
	   gl.glEnd();
	   gl.glPopName();
	}
	public Object clone() {
		Integer ni = Impact3D.name++;
		String nm = ni.toString();
		nameToNumber.put(nm, ni);
		Line segment = new Line(nm, getGraphObjects(), true);
		Proxy.getProxy().update(segment);
		return segment;
	}
	public void insert(Proxy p) {
		StringBuffer sb = new StringBuffer();
		sb.append("SEGMENT|"+name+"|INSERT");
	   	List<GraphObject> objs = getGraphObjects();
	        int size = objs.size(); 
	        for (int i = 0; i < size; i++) {
		     GraphObject obj = (GraphObject)objs.get(i);
		     sb.append("|");
	             sb.append(obj.name);
		     System.err.println("There's a graph object with name "+obj.name);
	        }
		p.send(sb.toString());
		receive(sb.toString());
	}
	public void remove(Proxy p) {
		String line = "SEGMENT|"+name+"|DELETE";
		p.send(line);
		receive(line);
	}
	// update updates connection relationships, and is same as insert
	public void update(Proxy p) {
		StringBuffer sb = new StringBuffer();
		sb.append("SEGMENT|"+name+"|UPDATE");
	   	List<GraphObject> objs = getGraphObjects();
	        int size = objs.size(); 
	        for (int i = 0; i < size; i++) {
		     GraphObject obj = (GraphObject)objs.get(i);
		     sb.append("|");
	             sb.append(obj.name);
	        }
		p.send(sb.toString());
		receive(sb.toString());
	}
	static public void receive(String line) {
		String [] params = line.split("\\|");
		 if (params[2].startsWith("INSERT")) {
			String nm = params[1];
			List<GraphObject> nodes = new ArrayList<GraphObject>();
			for (int i = 3; i < params.length; i++) {
				String nodenm = params[i];
				synchronized(Impact3D.objects.shown) {
					Iterator<GraphObject> n = Impact3D.objects.shown.iterator();
					while (n.hasNext()) {
						
						GraphObject obj = n.next();
						if (obj instanceof Point && obj.name.equals(nodenm)) {
							nodes.add(obj);
						}
					}
				}
			}
			int ni = Integer.parseInt(nm);
			if (ni >= Impact3D.name) {
				Impact3D.name = ni+1;
			}
			nameToNumber.put(nm, ni);
			Line segment = new Line(nm, nodes, false);
			Impact3D.objects.add(segment);
		 } else if (params[2].startsWith("UPDATE")) {
			boolean found = false;
			String nm = params[1];
			List<GraphObject> nodes = new ArrayList<GraphObject>();
			for (int i = 3; i < params.length; i++) {
				String nodenm = params[i];
				synchronized(Impact3D.objects.shown) {
					Iterator<GraphObject> n = Impact3D.objects.shown.iterator();
					while (n.hasNext()) {
						
						GraphObject obj = n.next();
						if (obj instanceof Point && obj.name.equals(nodenm)) {
							nodes.add(obj);
						}
					}
				}
			}
			int n = Integer.parseInt(params[1]);
			if (n >= Impact3D.name) {
				Impact3D.name = n+1;
			}
			synchronized (Impact3D.objects.shown) {
		   		Iterator<GraphObject> ni = Impact3D.objects.shown.iterator();
				while (ni.hasNext()) {
					GraphObject obj = ni.next();
					if (obj.name.equals(nm)) {
						if (obj instanceof Line) {
							Line segment = (Line)obj;
							found = true;
							segment.setGraphObjects(nodes);
						}
					}
				}
			}
			if (!found) {
				nameToNumber.put(nm, n);
				Line segment = new Line(nm, nodes, false);
				Impact3D.objects.add(segment);
			}
		} else if (params[2].startsWith("DELETE")) {
			int nm = Integer.parseInt(params[1]);
			synchronized (Impact3D.objects.shown) {
		   		Iterator<GraphObject> a = Impact3D.objects.shown.iterator();
				while (a.hasNext()) {
					GraphObject obj = a.next();
					if (obj.name.equals(nm)) {
						a.remove();
					}
				}				
			}
		} else if (params[2].startsWith("SELECT")) {
			synchronized (Impact3D.objects.shown) {
		   		Iterator<GraphObject> i = Impact3D.objects.shown.iterator();
				while (i.hasNext()) {
					GraphObject obj = i.next();
					if (obj instanceof Line) {
						Proxy.getProxy().insert(obj);
						Proxy.getProxy().update(obj);
					}
				}
			}
		}
	}
  }
