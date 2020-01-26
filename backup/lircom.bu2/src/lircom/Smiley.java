package lircom;
import java.util.*;
import java.io.*;

public class Smiley {
	HashMap hm = new HashMap();
	String dir = "/usr/share/pixmaps/gaim/smileys/default/";
	public Smiley () {
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
			e.printStackTrace();
		}
	}
	public String replace(String line) {
		Iterator i = hm.keySet().iterator();
		while (i.hasNext()) {
			String smile = (String)i.next();
			int sm = line.indexOf(smile);
			while (sm >= 0) {
				line = line.substring(0, sm)+"<img src='file:"+hm.get(smile)+"'>"+line.substring(sm+smile.length());
				sm = line.indexOf(smile);
			}
		}
		return line;
	}
}
