package lircom;
import java.util.*;
import java.io.*;
import java.util.regex.*;

public class Smiley {
	HashMap hm = new HashMap();
	String dir = "/usr/share/pixmaps/gaim/smileys/default/";
	final String mIrcColors[] = {
		"white",
		"black",
		"blue",
		"green",
		"red",
		"brown",
		"purple",
		"orange",
		"yellow",
		"lime",
		"cyan",
		"aqua",
		"royal",
		"pink",
		"grey",
		"silver"
	};
	public Smiley () {
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
	static public void main(String [] args) {
		Smiley s = new Smiley();
		String r = s.replace("0,01,129x9,x9,9x99,99x,9x,999,999");
		System.err.println(r);
	}
	public String replace(String line) {
		Pattern p = Pattern.compile("(([0-9][0-9]?)(,([0-9][0-9]?))?)?");
		Matcher m1 = p.matcher(line);
		Integer bgColor = null;
		while (m1.find()) {
			String front = line.substring(0, m1.start());
			String startspan = "<span ";
			if (m1.group(2) != null) {
				startspan += "style='";
				int fgcolor = Integer.parseInt(m1.group(2));
				if (fgcolor == 99) {
					startspan += "color:transparent;";
				} else {
					startspan += "color:"+mIrcColors[ fgcolor % mIrcColors.length]+";";
				}
				if (m1.group(4) != null) {
					bgColor = Integer.parseInt(m1.group(4));
				}
				 if (bgColor != null) {
					if (bgColor == 99) {
						startspan += "background-color:transparent;";
					} else {
						startspan += "background-color:"+ mIrcColors[ bgColor % mIrcColors.length]+";";
					}
				}
				startspan += "'";
			} else {
				bgColor = null;
			}
			startspan += ">";
			String colored = "";
			String end = "";
			if (line.indexOf("", m1.end()) >= 0) {
				colored = line.substring(m1.end(), line.indexOf("", m1.end()));
				end = line.substring(line.indexOf("", m1.end()));
			} else {
				colored = line.substring(m1.end());
			}
			String endspan = "</span>";
			line = front+startspan+colored+endspan+end;
			m1 = p.matcher(line);
		}
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
