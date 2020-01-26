package impact;

import java.util.*;

import javax.media.opengl.*;

public  class Polygon extends GraphObject {
  	float [] white = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
	Polygon(int name, List<GraphObject> nodes, boolean useProxy) {
            super(name);
	    for (GraphObject node : nodes) {
			// arcs connect nodes
		    if (node instanceof Point) {
			    addGraphObject(node);
			    node.addGraphObject(this);
		    }
	    }
	    if (useProxy) {
	        Proxy.getProxy().insert(this);
	    }
	}
        public int getName() {
		return this.name;
	}
	public void draw(GL2 gl) {
	
	   gl.glPushName(name);
	   gl.glBegin(gl.GL_POLYGON);
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
		Polygon arc = new Polygon(Impact3D.name++, getGraphObjects(), true);
		Proxy.getProxy().update(arc);
		return arc;
	}
	public void insert(Proxy p) {
		StringBuffer sb = new StringBuffer();
		sb.append("ARC|"+name+"|INSERT");
	   	List<GraphObject> objs = getGraphObjects();
	        int size = objs.size(); 
	        for (int i = 0; i < size; i++) {
		     GraphObject obj = (GraphObject)objs.get(i);
		     sb.append("|");
	             sb.append(obj.name);
	        }
		p.send(sb.toString());
		// receive(sb.toString());
	}
	public void remove(Proxy p) {
		String line = "ARC|"+name+"|DELETE";
		p.send(line);
		// receive(line);
	}
	// update updates connection relationships, and is same as insert
	public void update(Proxy p) {
		StringBuffer sb = new StringBuffer();
		sb.append("ARC|"+name+"|UPDATE");
	   	List<GraphObject> objs = getGraphObjects();
	        int size = objs.size(); 
	        for (int i = 0; i < size; i++) {
		     GraphObject obj = (GraphObject)objs.get(i);
		     sb.append("|");
	             sb.append(obj.name);
	        }
		p.send(sb.toString());
		// receive(sb.toString());
	}
	static public void receive(String line) {
		String [] params = line.split("\\|");
		 if (params[2].startsWith("INSERT")) {
			int nm = Integer.parseInt(params[1]);
			List<GraphObject> nodes = new Vector<GraphObject>();
			for (int i = 3; i < params.length; i++) {
				int nodenm = Integer.parseInt(params[i]);
				synchronized(Impact3D.objects.shown) {
					Iterator<GraphObject> n = Impact3D.objects.shown.iterator();
					while (n.hasNext()) {
						
						GraphObject obj = n.next();
						if (obj instanceof Point && obj.name == nodenm) {
							nodes.add(obj);
						}
					}
				}
			}
			int n = Integer.parseInt(params[1]);
			if (n >= Impact3D.name) {
				Impact3D.name = n+1;
			}
			Polygon arc = new Polygon(n, nodes, false);
			Impact3D.objects.add(arc);
		 } else if (params[2].startsWith("UPDATE")) {
			boolean found = false;
			int nm = Integer.parseInt(params[1]);
			List<GraphObject> nodes = new Vector<GraphObject>();
			for (int i = 3; i < params.length; i++) {
				int nodenm = Integer.parseInt(params[i]);
				synchronized(Impact3D.objects.shown) {
					Iterator<GraphObject> n = Impact3D.objects.shown.iterator();
					while (n.hasNext()) {
						
						GraphObject obj = n.next();
						if (obj instanceof Point && obj.name == nodenm) {
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
					if (obj.name == nm) {
						if (obj instanceof Polygon) {
							Polygon arc = (Polygon)obj;
							found = true;
							arc.setGraphObjects(nodes);
						}
					}
				}
			}
			if (!found) {
				Polygon arc = new Polygon(n, nodes, false);
				Impact3D.objects.add(arc);
			}
		} else if (params[2].startsWith("DELETE")) {
			int nm = Integer.parseInt(params[1]);
			synchronized (Impact3D.objects.shown) {
		   		Iterator<GraphObject> a = Impact3D.objects.shown.iterator();
				while (a.hasNext()) {
					GraphObject obj = a.next();
					if (obj.name == nm) {
						a.remove();
					}
				}				
			}
		} else if (params[2].startsWith("SELECT")) {
			synchronized (Impact3D.objects.shown) {
		   		Iterator<GraphObject> i = Impact3D.objects.shown.iterator();
				while (i.hasNext()) {
					GraphObject obj = i.next();
					if (obj instanceof Polygon) {
						Proxy.getProxy().insert(obj);
						Proxy.getProxy().update(obj);
					}
				}
			}
		}
	}
        void translateSelection(float x, float y, float z) {
		for (GraphObject obj : getGraphObjects()) {
			obj.translateSelection(x, y, z);
		}
        }
  }
