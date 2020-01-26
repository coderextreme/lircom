package impact;

import java.util.*;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

public class ObjectUtility<T extends GraphObject> {
	List<T> shown = new Vector<T>();
	List<T> selected = new Vector<T>();
	List<T> clipboard = new Vector<T>();
	void paste() {
		Iterator<T> i = clipboard.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			synchronized(shown) {
				if (!shown.contains(obj)) {
					shown.add(obj);
					selected.add(obj);
					Proxy.getProxy().insert(obj);
				}
			}
		}
	}
	void copy() {
		clipboard.clear();
		Iterator<T> i = selected.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			clipboard.add((T)obj.clone());
		}
	}
	void cut() {
		synchronized(shown) {
			clipboard.clear();
			Iterator<T> i = selected.iterator();
			while (i.hasNext()) {
				T obj = i.next();
				clipboard.add(obj);
				shown.remove(obj);
				i.remove();
				Proxy.getProxy().remove(obj);
			}
			selected.clear();
		}
	}
	void selectAll() {
		synchronized(shown) {
			Iterator<T> i = shown.iterator();
			while (i.hasNext()) {
				T obj = i.next();
				selected.add(obj);
			}
		}
	}
	void selectAll(List<T> connected) {
		Iterator<T> i = connected.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			selected.add(obj);
		}
	}
	boolean select(int nm) {
		boolean found = false;
		synchronized(shown) {
			Iterator<T> k = shown.iterator();
			while (k.hasNext()) {
				T obj = k.next();
				if (((T)obj).getName() == nm) {
					found = true;
					if (Impact3D.control) {
						if (selected.contains(obj)) {
							selected.remove(obj);
						} else {
							selected.add(obj);
						}
					} else {
						if (!selected.contains(obj)) {
							selected.add(obj);
						}
					}
				}
			}
		}
		return found;
        }
	public void draw(GL2 gl) {
		synchronized(shown) {
			Iterator<T> i = shown.iterator();
			while (i.hasNext()) {
				T obj = i.next();
				((T)obj).draw(gl);
			}
		}
	}
	public void untranslate() {
		synchronized(shown) {	
			Iterator<T> i = shown.iterator();
			while (i.hasNext()) {
				T obj = i.next();
				((T)obj).untranslate();
			}
		}
	}
  	void translateSelection(float x, float y, float z) {
		Iterator<T> i = selected.iterator();
		while (i.hasNext()) {
			T obj = i.next();
			((T)obj).translateSelection(x, y, z);
		}
	}
	void save() {
		synchronized (shown) {
			Iterator<T> i = shown.iterator();
			while (i.hasNext()) {
				T obj = i.next();
				((T)obj).save();
			}
		}
	}
	void add(T obj) {
		synchronized(shown) {
			shown.add(obj);
		}
	}
	void addAll(List<T> objs) {
		selected.addAll(objs);
	}
	void clearSelected() {
		selected.clear();
	}
	int selectedSize() {
		return selected.size();
	}
  }
