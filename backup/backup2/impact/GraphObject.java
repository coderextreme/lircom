package impact;

import java.util.List;
import java.util.Iterator;
import java.util.Vector;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.*;
import com.jogamp.common.nio.Buffers;

public abstract class GraphObject {
	List<GraphObject> graphObjects = new Vector<GraphObject>();
	protected int name;
	abstract void draw(GL2 gl);
	public abstract Object clone();
	abstract int getName();
        protected GraphObject(int name) {
            this.name = name;
        }
	public void addGraphObject(GraphObject arc) {
		GraphObject tmp = null;
		for (GraphObject go : graphObjects) {
			if (go.name == arc.name) {
				tmp = go;
				break;
			}
		}
		if (tmp == null) {
			graphObjects.add(arc);
		}
	}
	public List<GraphObject> getGraphObjects() {
		return graphObjects;
	}
	public void setGraphObjects(List<GraphObject> graphObjects) {
		this.graphObjects = graphObjects;
	}
	void untranslate() {
	}
	void save() {
	}
	void translateSelection(float x, float y, float z) {
	}
	public void remove(Proxy p) {
	}
	public void insert(Proxy p) {
	}
	public void update(Proxy p) {
	}
}
