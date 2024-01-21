package impact;

import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Hashtable;

class Proxy implements LineHandler {
        static Proxy proxy = new Proxy();

	ImpactClient chat = null;
	boolean initialized = false;
	private Proxy() {
		try {
			chat = new ImpactClient(new Socket("localhost", 8180), Long.toString(System.currentTimeMillis()));
			chat.start();
			select(this);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Problems instantiating communications");
		}
	}
	synchronized static public Proxy getProxy() {
		return proxy;
	}
	public void insert(Line segment) {
		segment.insert(proxy);
	}
	public void insert(Polygon arc) {
		arc.insert(proxy);
	}
	public void insert(Point node) {
		node.insert(proxy);
	}
	public void insert(GraphObject obj) {
		obj.insert(proxy);
	}
	public void remove(Line segment) {
		segment.remove(proxy);
	}
	public void remove(Polygon arc) {
		arc.remove(proxy);
	}
	public void remove(Point node) {
		node.remove(proxy);
	}
	public void remove(GraphObject obj) {
		obj.remove(proxy);
	}
	public void update(Line segment) {
		segment.update(proxy);
	}
	public void update(Polygon arc) {
		arc.update(proxy);
	}
	public void update(Point node) {
		node.update(proxy);
	}
	public void update(GraphObject obj) {
		obj.update(proxy);
	}
        public void select(Proxy proxy) {
		proxy.send("NODE||SELECT");
		proxy.send("ARC||SELECT");
		proxy.send("SEGMENT||SELECT");
	}
	public void send(String line) {
		try {
			lircom.Message m = new lircom.Message("*", chat.getNick(), line, "__");
			chat.send(m);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
        public void close() {
		try {
			chat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	class Bone {
		String from;
		String to;
	}
	class Bones extends Hashtable<String, Bone> {
		// map from to joint to from joint
	}
	class Joint {
		String jointId;
		String jointName;
		Double x;
		Double y;
		Double z;
	}
	class Joints extends Hashtable<String, Joint> {
		// map from to joint to from joint
	}
	public void receive(String line) {
		Impact3D.cmd = Impact3D.UPDATE;
		if (line.startsWith("ARC")) {
			Polygon.receive(line);
		} else if (line.startsWith("SEGMENT")) {
			Line.receive(line);
		} else if (line.startsWith("NODE")) {
			Point.receive(line);
		} else {
			// System.err.println("Received "+line);
			String[] data = line.split(" ");
			Bones bones = new Bones();
			Bone bone = null;
			Joint joint = null;
			Joints joints = new Joints();
			for (int c = 0; c < data.length; c++) {
				String command = data[c];
				// System.err.println("Command in proxy is "+command);
				String args = command.substring(2);
				if (command.startsWith("F:")) {
					bone = new Bone();
					bone.from = args;
				} else if (command.startsWith("T:")) {
					bone.to = args;
					bones.put(bone.to, bone);

				} else if (command.startsWith("J:")) {
					joint = new Joint();
					joint.jointName = args;
					joints.put(joint.jointName, joint);
				} else if (command.startsWith("L:")) {
					joint.jointId = joint.jointName.substring(0,1)+args;
					joints.put(joint.jointId, joint);
				} else if (command.startsWith("X:")) {
					joint.x = 10*Double.valueOf(args);
				} else if (command.startsWith("Y:")) {
					joint.y = 10*Double.valueOf(args);
				} else if (command.startsWith("Z:")) {
					joint.z = 10*Double.valueOf(args);
					bone = bones.get(joint.jointId);
					if (bone != null) {
						// System.err.println("Found bone "+bone.from+" -> "+bone.to);
					}
					joint = joints.get(joint.jointId);
					if (joint != null) {
						// System.err.println("Found joint "+joint.jointId+" -> ("+joint.x+", "+joint.y+", "+joint.z+")");
					}
				}
			}
			Iterator<String> boneIterator = bones.keySet().iterator();
			int bone_segment = bones.size() + 1;
			while (boneIterator.hasNext()) {
				bone = bones.get(boneIterator.next());
				// System.err.println("Dump bone "+bone.from+" -> "+bone.to);
				Joint jointFrom = joints.get(bone.from);
				Joint jointTo = joints.get(bone.to);
				line = "";
				if (!initialized) {
					initialized = true;
					line = "NODE|"+bone.from+"|INSERT";
					// System.err.println(line);
					Point.receive(line);
					line = "NODE|"+bone.to+"|INSERT";
					// System.err.println(line);
					Point.receive(line);
					line = "SEGMENT|"+bone_segment+"|INSERT|"+bone.from+"|"+bone.to;
					System.err.println(line);
					Line.receive(line);
					bone_segment++;
				}
				if (jointFrom != null) {
					line = "NODE|"+bone.from+"|UPDATE|1|1|1|1|"+jointFrom.x+"|"+-jointFrom.y+"|"+jointFrom.z+"|0.0|0.0|0.0";
					// System.err.println(line);
					Point.receive(line);
				}
				if (jointTo != null) {
					line = "NODE|"+bone.to+"|UPDATE|1|1|1|1|"+jointTo.x+"|"+-jointTo.y+"|"+jointTo.z+"|0.0|0.0|0.0";
					// System.err.println(line);
					Point.receive(line);
				}
			}
			boneIterator = bones.keySet().iterator();
			bone_segment = bones.size() + 1;
			while (boneIterator.hasNext()) {
				bone = bones.get(boneIterator.next());
				line = "SEGMENT|"+bone_segment+"|UPDATE|"+bone.from+"|"+bone.to;
				// System.err.println(line);
				Line.receive(line);
				bone_segment++;
			}
		}
	}
}
