package lircom {
import java.util.*;
import java.io.*;

public class Smiley {
	var hm:HashMap= new HashMap();
	var dir:String= "/usr/share/pixmaps/gaim/smileys/default/";
	public function Smiley() {
/*
		try {
			BufferedReader br = new BufferedReader(new FileReader(dir+"theme"));

			String line = null;
			while ((line = br.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, "\t ");
				if (!st.hasMoreTokens()) {
					continue;
				}
				String file = st.nextToken();
				if (file.indexOf(".gif") > 0 || file.indexOf(".png") > 0) {
					while (st.hasMoreTokens()) {
						hm.put(st.nextToken(), dir+file);
					}
				}
			}
		} catch (Exception e) {
                    System.err.println("Can't load smilies.  Try on a Linux system.");
			//e.printStackTrace();
		}
*/
	}
	public function replace(line:String):String {
/*
		Iterator i = hm.keySet().iterator();
		while (i.hasNext()) {
			String smile = (String)i.next();
			int sm = line.indexOf(smile);
			while (sm >= 0) {
				line = line.substring(0, sm)+"<img src='file:"+hm.get(smile)+"'>"+line.substring(sm+smile.length());
				sm = line.indexOf(smile);
			}
		}
*/
		return line;
	}
}
}