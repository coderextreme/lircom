package impact;

import java.net.Socket;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.io.Writer;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

class Proxy implements LineHandler {
	static Proxy proxy = null;
	ImpactClient chat = null;
	class Joint {
		String jointId;
		String jointName;
		HashSet<Joint> children = new HashSet<Joint>();
		HashSet<Joint> parents = new HashSet<Joint>();
		Double x;
		Double y;
		Double z;
	}
	Joint root = null;
	class Joints extends HashMap<String, Joint> {
		// map from to joint to from joint
	}
	Joints joints = new Joints();
	class Bone {
		String from;
		String to;
	}
	class Bones extends HashSet<Bone> {
		// map from to joint to from joint
	}
	Bones bones = new Bones();
	boolean initialized = false;
	boolean bvhJointsInitialized = false;
	public Proxy(String host, int port) {
		try {
			chat = new ImpactClient(this, new Socket(host, port), Long.toString(System.currentTimeMillis()));
			chat.start();
			select();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Problems instantiating communications");
		}
		proxy = this;
	}
	synchronized static public Proxy getProxy() {
		return proxy;
	}
	public void insert(Line segment) {
		segment.insert(this);
	}
	public void insert(Polygon arc) {
		arc.insert(this);
	}
	public void insert(Point node) {
		node.insert(this);
	}
	public void insert(GraphObject obj) {
		obj.insert(this);
	}
	public void remove(Line segment) {
		segment.remove(this);
	}
	public void remove(Polygon arc) {
		arc.remove(this);
	}
	public void remove(Point node) {
		node.remove(this);
	}
	public void remove(GraphObject obj) {
		obj.remove(this);
	}
	public void update(Line segment) {
		segment.update(this);
	}
	public void update(Polygon arc) {
		arc.update(this);
	}
	public void update(Point node) {
		node.update(this);
	}
	public void update(GraphObject obj) {
		obj.update(this);
	}
        public void select() {
		this.send("NODE||SELECT");
		this.send("ARC||SELECT");
		this.send("SEGMENT||SELECT");
	}
	public void send(String line) {
		try {
			lircom.Message m = new lircom.Message("*", chat.getNick(), line, "__", "Impact");
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
	public void printMotion(Joint joint, Writer w) throws IOException {
		w.write(joint.x+" "+-joint.y+" "+joint.z+" ");
		Iterator<Joint> itr = joint.children.iterator();
		while (itr.hasNext()) {
			Joint node = itr.next();
			if (node != null) {
				printMotion(node, w);
			}
		}
	}
	public String repeat(int indent, String repeatee) {
		StringBuffer sb = new StringBuffer("");
		for (int i = 0; i < indent; i++) {
			sb.append(repeatee);
		}
		return sb.toString();
	}
	public void printHierarchy(Joint joint, Writer w, int indent) throws IOException {
		//  System.err.println("got here");
		w.write(repeat(indent, "\t")+(indent == 0 ? "ROOT " : "JOINT ")+joint.jointId+"\n");
		w.write(repeat(indent, "\t")+"{\n");
		w.write(repeat(indent+1, "\t")+"\tOFFSET "+joint.x+" "+-joint.y+" "+joint.z+"\n");
		w.write(repeat(indent+1, "\t")+"\tCHANNELS 3 Xposition Yposition Zposition\n");
		Iterator<Joint> itr = joint.children.iterator();
		while (itr.hasNext()) {
			joint =  itr.next();
			if (joint != null) {
				printHierarchy(joint, w, indent+1);
			}
		}
		w.write(repeat(indent, "\t")+"}\n");
	}
	public void receiveMocapSpewBVH(String line, Writer w) throws IOException {
		// System.err.println("Received "+line);
		String[] data = line.split(" ");
		Bone bone = null;
		Joint joint = null;
		if (!bvhJointsInitialized) {
			w.write("HIERARCHY\n");
		}

		for (int c = 0; c < data.length; c++) {
			String command = data[c];
			if (command.length() <= 2) {
				continue;
			}
			String args = command.substring(2);
			if (command.startsWith("F:")) {
				// System.err.println("Command in Proxy is "+command);
				bone = new Bone();
				bone.from = args;
				bones.add(bone);
			} else if (command.startsWith("T:")) {
				// System.err.println("Command in Proxy is "+command);
				if (bone != null) {
					bone.to = args;
					bones.add(bone);  // this will have no from
				} else {
					System.err.println("Bone is null, "+command);
				}

			} else if (command.startsWith("J:")) {
				// System.err.println("Command in Proxy is "+command);
				joint = new Joint();
				joint.jointName = args;
				joints.put(joint.jointName, joint);
			} else if (command.startsWith("L:")) {
				// System.err.println("Command in Proxy is "+command);
				joint.jointId = joint.jointName.substring(0,1)+args;
				joints.put(joint.jointId, joint);
			} else if (command.startsWith("X:")) {
				joint.x = 10*Double.valueOf(args)-5;
			} else if (command.startsWith("Y:")) {
				joint.y = 10*Double.valueOf(args)-5;
			} else if (command.startsWith("Z:")) {
				joint.z = 10*Double.valueOf(args);
			}
		}
		
		Iterator<Bone> boneIterator = bones.iterator();
		while (boneIterator.hasNext()) {
			bone = boneIterator.next();
			// System.err.println("Dump bone "+bone.from+" -> "+bone.to);
			Joint jointFrom = joints.get(bone.from);
			Joint jointTo = joints.get(bone.to);
			// System.err.println("Dump joint "+jointFrom.jointId+" -> "+jointTo.jointId);
			if (jointFrom != null) {
				jointFrom.children.add(jointTo);
			}
			if (jointTo != null) {
				jointTo.parents.add(jointFrom);
			}
		}
		root = joint;
		while (joint != null) {
			root = joint;
			joint = null;
			Iterator<Joint> parItr = root.parents.iterator();
			while (parItr.hasNext()) {
				Joint par = parItr.next();
				if (par != null) {
					joint = par;
					System.err.println("Found joint "+joint.jointId+" "+joint.jointName);
					break;
				}
			}
		}
		joint = root;
		if (!bvhJointsInitialized) {
			bvhJointsInitialized = true;
			printHierarchy(root, w, 0);
			w.write("MOTION\n");
		}
		if (root != null) {
			printMotion(root, w);
			w.write("\n");
		}
	}
	public void receiveMocap(String line) {
		// System.err.println("Received "+line);
		String[] data = line.split(" ");
		Bones bones = new Bones();
		Bone bone = null;
		Joint joint = null;
		Joints joints = new Joints();
		for (int c = 0; c < data.length; c++) {
			String command = data[c];
			if (command.length() <= 2) {
				continue;
			}
			// System.err.println(command);
			String args = command.substring(2);
			if (command.startsWith("F:")) {
				// System.err.println("Command in Proxy is "+command);
				bone = new Bone();
				bone.from = args;
				bones.add(bone);
			} else if (command.startsWith("T:")) {
				// System.err.println("Command in Proxy is "+command);
				if (bone != null) {
					bone.to = args;
					bones.add(bone);  // this will have no from
				} else {
					System.err.println("Bone is null, "+command);
				}
			} else if (command.startsWith("J:")) {
				// System.err.println("Command in Proxy is "+command);
				joint = new Joint();
				joint.jointName = args;
				joints.put(joint.jointName, joint);
			} else if (command.startsWith("L:")) {
				//System.err.println("Command in Proxy is "+command);
				joint.jointId = args;
				//System.err.println("id is "+joint.jointId);
				joints.put(joint.jointId, joint);
			} else if (command.startsWith("X:")) {
				joint.x = 10*Double.valueOf(args)-5;
			} else if (command.startsWith("Y:")) {
				joint.y = 10*Double.valueOf(args)-5;
			} else if (command.startsWith("Z:")) {
				joint.z = 10*Double.valueOf(args);
				joint = joints.get(joint.jointId);
				if (joint != null) {
					// System.err.println("Found joint "+joint.jointId+" -> ("+joint.x+", "+joint.y+", "+joint.z+")");
				}
			}
		}
		Iterator<Bone> boneIterator = bones.iterator();
		int bone_segment = bones.size() + 1;
		while (boneIterator.hasNext()) {
			bone = boneIterator.next();
			// System.err.println("Dump bone "+bone.from+" -> "+bone.to);
			Joint jointFrom = joints.get(bone.from);
			Joint jointTo = joints.get(bone.to);
			line = "";
			if (initialized) {
				line = "NODE|"+bone.from+"|DELETE";
				// System.err.println(line);
				Point.receive(line);
				line = "NODE|"+bone.to+"|DELETE";
				// System.err.println(line);
				Point.receive(line);
				line = "SEGMENT|"+bone_segment+"|DELETE|"+bone.from+"|"+bone.to;
				// System.err.println(line);
				Line.receive(line);
				bone_segment++;
			}
			if (!initialized) {
				initialized = true;
				line = "NODE|"+bone.from+"|INSERT";
				// System.err.println(line);
				Point.receive(line);
				line = "NODE|"+bone.to+"|INSERT";
				// System.err.println(line);
				Point.receive(line);
				line = "SEGMENT|"+bone_segment+"|INSERT|"+bone.from+"|"+bone.to;
				// System.err.println(line);
				Line.receive(line);
				bone_segment++;
			}
			if (jointFrom != null) {
				line = "NODE|"+bone.from+"|UPDATE|1|0|0|1|"+jointFrom.x+"|"+-jointFrom.y+"|"+jointFrom.z+"|0.0|0.0|0.0";
				// System.err.println(line);
				Point.receive(line);
			}
			if (jointTo != null) {
				line = "NODE|"+bone.to+"|UPDATE|0|1|0|1|"+jointTo.x+"|"+-jointTo.y+"|"+jointTo.z+"|0.0|0.0|0.0";
				// System.err.println(line);
				Point.receive(line);
			}
		}
		boneIterator = bones.iterator();
		bone_segment = bones.size() + 1;
		while (boneIterator.hasNext()) {
			bone = boneIterator.next();
			line = "SEGMENT|"+bone_segment+"|UPDATE|"+bone.from+"|"+bone.to;
			// System.err.println(line);
			Line.receive(line);
			bone_segment++;
		}
	}
	public void receiveCppon(String line) {
		// System.err.println("Received "+line);
		String[] data = line.split(";");
		Bones bones = new Bones();
		Bone bone = null;
		Joint joint = null;
		Joints joints = new Joints();
		for (int c = 0; c < data.length; c++) {
			String command = data[c];
			String args = command;
			// System.err.println(command);
			if (command.indexOf("=") >= 0) {
			} else if (command.indexOf("setFrom") >= 0) {
				bone = new Bone();
				int firstQuote = args.indexOf('"')+1;
				int lastQuote = args.lastIndexOf('"');
				bone.from = args.substring(firstQuote, lastQuote);
				bones.add(bone);
			} else if (command.indexOf("setTo") >= 0) {
				int firstQuote = args.indexOf('"')+1;
				int lastQuote = args.lastIndexOf('"');
				bone.to = args.substring(firstQuote, lastQuote);
				// bones.add(bone);  // No duplicates

			} else if (command.indexOf("setDEF") >= 0) {
				int firstQuote = args.indexOf('"')+1;
				int lastQuote = args.lastIndexOf('"');
				int firstDot = args.indexOf('.');
				int firstUnderscore = args.indexOf('_');
				//System.err.println("Command in Proxy is "+command+" dot "+firstDot);
				String id = command.substring(firstUnderscore+1, firstUnderscore+2)+command.substring(6, firstUnderscore);
				joint = new Joint();
				joint.jointName = args.substring(firstQuote, lastQuote);
				//System.err.println("PUT id: "+id);
				joint.jointId = id;
				//System.err.println("PUT Name: "+joint.jointName+" id: "+joint.jointId);
				joints.put(joint.jointName, joint);
				joints.put(joint.jointId, joint);
			} else if (command.indexOf("setPoint") >= 0) {
				int firstDot = args.indexOf('.');
				int firstUnderscore = args.indexOf('_');
				//System.err.println("Command in Proxy is "+command+" dot "+firstDot);
				String id = command.substring(firstUnderscore+1, firstUnderscore+2)+command.substring(6, firstUnderscore);
				//System.err.println("GET id: "+id);
				joint = joints.get(id);
				//System.err.println("GET Name: "+joint.jointName+" id: "+joint.jointId);
				int firstBrace = args.indexOf('{')+1;
				int lastBrace = args.lastIndexOf('}')-1;
				String xyz = args.substring(firstBrace, lastBrace);
				String[] xyzarray = xyz.split(",");

				joint.x = 10*Double.valueOf(xyzarray[0])-5;
				joint.y = 10*Double.valueOf(xyzarray[1])-5;
				joint.z = 10*Double.valueOf(xyzarray[2]);
			}
		}
		Iterator<Bone> boneIterator = bones.iterator();
		int bone_segment = bones.size() + 1;
		while (boneIterator.hasNext()) {
			bone = boneIterator.next();
			// System.err.println("Dump bone "+bone.from+" -> "+bone.to);
			Joint jointFrom = joints.get(bone.from);
			Joint jointTo = joints.get(bone.to);
			line = "";
			if (initialized) {
				line = "NODE|"+bone.from+"|DELETE";
				// System.err.println(line);
				Point.receive(line);
				line = "NODE|"+bone.to+"|DELETE";
				// System.err.println(line);
				Point.receive(line);
				line = "SEGMENT|"+bone_segment+"|DELETE|"+bone.from+"|"+bone.to;
				// System.err.println(line);
				Line.receive(line);
				bone_segment++;
			}
			if (!initialized) {
				initialized = true;
				line = "NODE|"+bone.from+"|INSERT";
				// System.err.println(line);
				Point.receive(line);
				line = "NODE|"+bone.to+"|INSERT";
				// System.err.println(line);
				Point.receive(line);
				line = "SEGMENT|"+bone_segment+"|INSERT|"+bone.from+"|"+bone.to;
				// System.err.println(line);
				Line.receive(line);
				bone_segment++;
			}
			if (jointFrom != null) {
				line = "NODE|"+bone.from+"|UPDATE|0|0|1|1|"+jointFrom.x+"|"+-jointFrom.y+"|"+jointFrom.z+"|0.0|0.0|0.0";
				// System.err.println(line);
				Point.receive(line);
			}
			if (jointTo != null) {
				line = "NODE|"+bone.to+"|UPDATE|1|0|1|1|"+jointTo.x+"|"+-jointTo.y+"|"+jointTo.z+"|0.0|0.0|0.0";
				// System.err.println(line);
				Point.receive(line);
			}
		}
		boneIterator = bones.iterator();
		bone_segment = bones.size() + 1;
		while (boneIterator.hasNext()) {
			bone = boneIterator.next();
			line = "SEGMENT|"+bone_segment+"|UPDATE|"+bone.from+"|"+bone.to;
			// System.err.println(line);
			Line.receive(line);
			bone_segment++;
		}
	}
	public void receive(String line) {
		receive("Impact", line);
	}
	public void receiveImpact(String line) {
		String[] data = line.split(" ");
		// System.err.println(line);
		for (int c = 0; c < data.length; c++) {
			String command = data[c];
			if (command.indexOf("NODE") >= 0) {
				// System.err.println(command);
				Point.receive(command);
			} else if (command.indexOf("SEGMENT") >= 0) {
				// System.err.println(command);
				Line.receive(command);
			} else if (command.indexOf("ARC") >= 0) {
				// System.err.println(command);
				Polygon.receive(command);
			}
		}
	}
	public void receive(String nick, String line) {
		Impact3D.cmd = Impact3D.UPDATE;
		try {
			if (nick.startsWith("Impact")) {
				receiveImpact(line);
			} else if (nick.startsWith("Mocap")) {
				receiveMocap(line);
				// receiveMocapSpewBVH(line, new FileWriter("MOCAP.bvh"));
			} else if (nick.startsWith("Cppon")) {
				receiveCppon(line);
			}
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}
