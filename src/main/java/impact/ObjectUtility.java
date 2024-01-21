package impact;

import java.util.*;

import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.*;
import com.jogamp.opengl.util.*;
import com.jogamp.common.nio.Buffers;

public class ObjectUtility<T extends GraphObject> {
	List<GraphObject> shown = new Vector<GraphObject>();
	List<GraphObject> selected = new Vector<GraphObject>();
	List<GraphObject> clipboard = new Vector<GraphObject>();
	void paste() {
		Iterator<GraphObject> i = clipboard.iterator();
		while (i.hasNext()) {
			GraphObject obj = i.next();
			if (!shown.contains(obj)) {
				shown.add(obj);
				selected.add(obj);
				Proxy.getProxy().insert(obj);
			}
		}
	}
	void copy() {
		clipboard.clear();
		Iterator<GraphObject> i = selected.iterator();
		while (i.hasNext()) {
			GraphObject obj = i.next();
			clipboard.add((GraphObject)obj.clone());
		}
	}
	void cut() {
		clipboard.clear();
		Iterator<GraphObject> i = selected.iterator();
		while (i.hasNext()) {
			GraphObject obj = i.next();
			clipboard.add(obj);
			shown.remove(obj);
			i.remove();
			Proxy.getProxy().remove(obj);
		}
		selected.clear();
	}
	void selectAll() {
		synchronized(shown) {
			Iterator<GraphObject> i = shown.iterator();
			while (i.hasNext()) {
				GraphObject obj = i.next();
				selected.add(obj);
			}
		}
	}
	void selectAll(List<GraphObject> connected) {
		Iterator<GraphObject> i = connected.iterator();
		while (i.hasNext()) {
			GraphObject obj = i.next();
			selected.add(obj);
		}
	}
	boolean select(GraphObject obj) {
		boolean found = false;
		if (Impact3D.control) {
			if (selected.contains(obj)) {
				selected.remove(obj);
			} else {
				selected.add(obj);
				found = true;
			}
		} else {
			if (!selected.contains(obj)) {
				selected.add(obj);
				found = true;
			}
		}
		Iterator<GraphObject> k = obj.getGraphObjects().iterator();
		boolean oneFound = false;
		while (k.hasNext()) {
			GraphObject point = k.next();
			if (point instanceof Point && select(point)) {
				oneFound = true;
			}
		}
		if (oneFound) {
			found = true;
		}
		return found;
	}
	boolean select(int nm) {
		boolean found = false;
		synchronized(shown) {
			Iterator<GraphObject> k = shown.iterator();
			while (k.hasNext()) {
				GraphObject obj = k.next();
				if (obj.isNumber(nm)) {
					select(obj);
					found = true;
				}
			}
		}
	      return found;
        }
	public void draw(GL2 gl) {
		synchronized(shown) {
			Iterator<GraphObject> i = shown.iterator();
			while (i.hasNext()) {
				GraphObject obj = i.next();
				((GraphObject)obj).draw(gl);
			}
		}
	}
	public void untranslate() {
		synchronized(shown) {	
			Iterator<GraphObject> i = shown.iterator();
			while (i.hasNext()) {
				GraphObject obj = i.next();
				((GraphObject)obj).untranslate();
			}
		}
	}
  	void translateSelection(float x, float y, float z) {
		Iterator<GraphObject> i = selected.iterator();
		while (i.hasNext()) {
			System.err.println("Shifting selection "+x+" "+y+" "+z);
			GraphObject obj = i.next();
			((GraphObject)obj).translateSelection(x, y, z);
		}
	}
	void save() {
		synchronized (shown) {
			Iterator<GraphObject> i = shown.iterator();
			while (i.hasNext()) {
				GraphObject obj = i.next();
				((GraphObject)obj).save();
			}
		}
	}
	void add(GraphObject obj) {
		shown.add(obj);
	}
	void addAll(List<GraphObject> objs) {
		selected.addAll(objs);
	}
	void clearSelected() {
		selected.clear();
	}
	int selectedSize() {
		return selected.size();
	}
  }
