import java.io.*;
import java.util.*;

public class GameMiner {
	static public void main(String arg[]) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		ProgramsVersionSpace psvs = new ProgramsVersionSpace();
		while((line = br.readLine()) != null) {
			processLine(line, null, null);
		}
		// psvs.print(System.out, 0);
	}
	static public void processLine(String line, ProgramsVersionSpace psvs, CardItem aci) {
		StringTokenizer st = new StringTokenizer(line, "|");
		String command = st.nextToken();
		String stack = st.nextToken();
		if (command.equals("PICK")) {
			String position = st.nextToken();
			String rank = st.nextToken();
			String suit = st.nextToken();
			// System.err.println(order+"|"+command+"|"+stack+"|"+position);
			int istack = Integer.parseInt(stack);
			Stack sm = Game.getStack(istack);
			if (sm != null && sm.stack_no == istack) {
				CardItem cim;
				if (aci == null) {
					cim = (CardItem)sm.remove(Integer.parseInt(position));
				} else {
					cim = aci;
				}
				Log.write("PICK|"+stack+"|"+position+"|"+cim.rank+"|"+cim.suit);
				CardVersionSpace cvs = new CardVersionSpace(cim);
				PickVersionSpace pvs = new PickVersionSpace();
				pvs.add(cvs);
				if (psvs != null) {
					psvs.addAction(pvs);
				}
			}
		} else if (command.equals("PLAY")) {
			String position = st.nextToken();
			String rank = st.nextToken();
			String suit = st.nextToken();
			// System.err.println(order+"|"+command+"|"+stack+"|"+position+"|"+rank+"|"+suit);
			// find the card in the stacks
			CardItem cim = null;
			int istack = Integer.parseInt(stack);
			int irank = Integer.parseInt(rank);
			int isuit = Integer.parseInt(suit);
			Stack sm = Game.getStack(istack);
			cim = Stack.cards[irank-1][isuit];
			sm = Game.getStack(istack);
			if (sm != null && sm.stack_no == istack) {
				if (aci == null) {
					sm.insertElementAt(cim, Integer.parseInt(position));
				}
				Log.write("PLAY|"+stack+"|"+position+"|"+cim.rank+"|"+cim.suit);
				CardVersionSpace cvs = new CardVersionSpace(cim);
				PlayVersionSpace pvs = new PlayVersionSpace();
				pvs.add(cvs);
				if (psvs != null) {
					psvs.addAction(pvs);
				}
			}
		} else if (command.equals("INVISIBLE")) {
			String position = st.nextToken();
			String rank = st.nextToken();
			String suit = st.nextToken();
			// System.err.println(order+"|"+command+"|"+stack+"|"+position+"|"+rank+"|"+suit);
			int istack = Integer.parseInt(stack);
			Stack sm = Game.getStack(istack);
			if (sm != null && sm.stack_no == istack) {
				CardItem cim;
				if (aci == null) {
					cim = (CardItem)sm.elementAt(Integer.parseInt(position));
					cim.setFaceUp(false);
				} else {
					cim = aci;
				}
				//cim.setSuit(suit);
				//cim.setRank(rank);
				Log.write("INVISIBLE|"+stack+"|"+position+"|"+rank+"|"+suit);
				CardVersionSpace cvs = new CardVersionSpace(cim);
				MakeInvisibleVersionSpace mivs = new MakeInvisibleVersionSpace();
				mivs.add(cvs);
				if (psvs != null) {
					psvs.addAction(mivs);
				}
			}
		} else if (command.equals("VISIBLE")) {
			String position = st.nextToken();
			String rank = st.nextToken();
			String suit = st.nextToken();
			// System.err.println(order+"|"+command+"|"+stack+"|"+position+"|"+rank+"|"+suit);
			int istack = Integer.parseInt(stack);
			Stack sm = Game.getStack(istack);
			if (sm != null && sm.stack_no == istack) {
				CardItem cim;
				if (aci == null) {
					cim = (CardItem)sm.elementAt(Integer.parseInt(position));
					cim.setFaceUp(true);
				} else {
					cim = aci;
				}
				// cim.setSuit(suit);
				// cim.setRank(rank);
				Log.write("VISIBLE|"+stack+"|"+position+"|"+rank+"|"+suit);
				CardVersionSpace cvs = new CardVersionSpace(cim);
				MakeVisibleVersionSpace mvvs = new MakeVisibleVersionSpace();
				mvvs.add(cvs);
				if (psvs != null) {
					psvs.addAction(mvvs);
				}
			}
		} else if (command.equals("NEWSTACK")) {
			String x = st.nextToken();
			String y = st.nextToken();
			// System.err.println(order+"|"+command+"|"+stack+"|"+x+"|"+y);
			Log.write("NEWSTACK|"+stack+"|"+x+"|"+y);
			if (aci == null) {
				System.err.println("Creating stack "+stack);
				new Stack(
					Integer.parseInt(stack),
					Integer.parseInt(x),
					Integer.parseInt(y));
			}
			NewStackVersionSpace nsvs = new NewStackVersionSpace(Integer.parseInt(stack));
			if (psvs != null) {
				psvs.addAction(nsvs);
			}
		}
	}
}
