package solitaire;

import java.io.*;
import java.util.*;

public class EventMiner {
	static final int TUPLESIZE = 6;
	static public void main(String arg[]) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		HashMap<String,String>[] args = (HashMap<String,String>[])new HashMap<?,?>[TUPLESIZE];
		for (int a = 0; a < TUPLESIZE; a++) {
			args[a] = new HashMap<String,String>();
		}
		ArrayList<ArrayList<String>> lines = new ArrayList<ArrayList<String>>();
		while((line = br.readLine()) != null) {
			StringTokenizer st = new StringTokenizer(line, "|");
			int a = 0;
			ArrayList<String> v = new ArrayList<String>();
			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				args[a].put(token, token);
				a++;
				v.add(token);
			}
			lines.add(v);
		}
		for (int a = 0; a < TUPLESIZE; a++) {
			for (Iterator i = args[a].keySet().iterator(); i.hasNext();) {
				String s = args[a].get(i.next()).toString();
				System.out.print(" ");
				System.out.print(s);
			}
			System.out.println();
		}
			System.out.println();
			System.out.println();
			System.out.println();
			System.out.println();
		HashMap<String, HashMap<Integer, HashMap<String,String>>>[] cxr = (HashMap<String, HashMap<Integer, HashMap<String,String>>>[])new HashMap<?,?>[TUPLESIZE];
		for (int a = 0; a < TUPLESIZE; a++) {
			cxr[a] = new HashMap<String, HashMap<Integer, HashMap<String,String>>>();
		}
		for (int i = 0; i < lines.size(); i++) {
			ArrayList<String> v = (ArrayList<String>)lines.get(i);
			String ss[] = new String[v.size()];
			for (int j = 0; j < v.size(); j++) {
				ss[j] = v.get(j).toString();
			}
			for (int j = 0; j < v.size(); j++) {
				// System.out.print(ss[j]);
				HashMap<Integer, HashMap<String,String>> ht = (HashMap<Integer, HashMap<String,String>>)cxr[j].get(ss[j]);
				if (ht == null) {
					ht = new HashMap<Integer, HashMap<String,String>>();
				}
				for (int k = 0; k < v.size(); k++) {
					int l = k;
					HashMap<String,String> ht2 = (HashMap<String,String>)ht.get(l);
					if (ht2 == null) {
						ht2 = new HashMap<String,String>();
					}
					// System.out.print(" ");
					// System.out.print(ss[k]);
					ht2.put(ss[k], "");
					ht.put(l, ht2);
				}
				cxr[j].put(ss[j], ht);
				// System.out.println();
			}
			// System.out.println();
		}
		for (int a = 0; a < TUPLESIZE; a++) {
			HashMap col = cxr[a];
			Iterator i = col.keySet().iterator();
			System.out.print("COL ");
			System.out.print(a);
			System.out.print(" ");
			while (i.hasNext()) {
				Object s = i.next();
				System.out.print(s);

				HashMap ht = (HashMap)col.get(s);
				Iterator j = ht.keySet().iterator();
				while (j.hasNext()) {
					Object sel = j.next();
					System.out.print("\n\t");
					System.out.print(sel);
					HashMap ht2 = (HashMap)ht.get(sel);
					if (ht2 == null) {
						System.out.println(" null");
					} else {
						Iterator k = ht2.keySet().iterator();
						while (k.hasNext()) {
							Object sel2 = k.next();
							System.out.print(" ");
							System.out.print(sel2);
						}
					}
				}
				System.out.println();
			}
			System.out.println();
		}
	}
}
