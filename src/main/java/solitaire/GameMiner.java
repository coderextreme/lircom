package solitaire;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.swing.JFrame;

public class GameMiner {
	public static Game game = null;
	public static JFrame jf = null;
	static public void main(String arg[]) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String line;
		while((line = br.readLine()) != null) {
			processLine(line);
		}
	}
	static public void processLine(String line) {
		// System.err.println("GameMiner.Processing "+line);
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
				CardItem cim = (CardItem)sm.remove(Integer.parseInt(position));
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
				sm.insertElementAt(cim, Integer.parseInt(position));
			}
		} else if (command.equals("INVISIBLE")) {
			String position = st.nextToken();
			String rank = st.nextToken();
			String suit = st.nextToken();
			// System.err.println(order+"|"+command+"|"+stack+"|"+position+"|"+rank+"|"+suit);
			int istack = Integer.parseInt(stack);
			Stack sm = Game.getStack(istack);
			if (sm != null && sm.stack_no == istack) {
				CardItem cim = (CardItem)sm.elementAt(Integer.parseInt(position));
				cim.setFaceUp(false);
			}
		} else if (command.equals("VISIBLE")) {
			String position = st.nextToken();
			String rank = st.nextToken();
			String suit = st.nextToken();
			// System.err.println(order+"|"+command+"|"+stack+"|"+position+"|"+rank+"|"+suit);
			int istack = Integer.parseInt(stack);
			Stack sm = Game.getStack(istack);
			if (sm != null && sm.stack_no == istack) {
				CardItem cim = (CardItem)sm.elementAt(Integer.parseInt(position));
				cim.setFaceUp(true);
			}
		} else if (command.equals("NEWSTACK")) {
			String x = st.nextToken();
			String y = st.nextToken();
			String off = st.nextToken();
			String direction = st.nextToken();
			// System.err.println("Creating stack "+stack);
			new Stack(
				Integer.parseInt(stack),
				Integer.parseInt(x),
				Integer.parseInt(y),
				Integer.parseInt(off),
				direction.charAt(0),
				jf, game);
			// System.err.println("Done creating stack "+stack);
		}
	}
}
