package impact;

import java.util.List;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

import com.jogamp.opengl.GL2;

public abstract class GraphObject {
	List<GraphObject> graphObjects = new Vector<GraphObject>();
	static Map<String, Integer> nameToNumber = new HashMap<String, Integer>();
	protected String name;
	abstract void draw(GL2 gl);
	public abstract Object clone();
	abstract String getName();
	public boolean isNumber(int ni) {
		return nameToNumber.get(name) == ni;
	}
        protected GraphObject(String name) {
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
