package solitaire;

import java.util.*;
import java.io.*;

// Joins are ordered, or conjunctions
// Unions are unordered, or disjunctions


/////////////////// GENERICS ////////////////////////
interface VersionSpace {
	void print(PrintStream sb, int level);
	void indent(PrintStream sb, int level);
	boolean equals(VersionSpace vs);
	void update(VersionSpace vs);
}

interface CompositeVersionSpace extends VersionSpace {
}

abstract class JoinVersionSpace extends ArrayList implements CompositeVersionSpace {
	// version spaces in a join are maintained separately
	public void update(VersionSpace vs) {
		add(vs);
	}
	public void indent(PrintStream sb, int level) {
		for (int i = 0; i < level; i++) {
			sb.print(" ");
		}
	}
	public boolean equals(VersionSpace vs) {
		// System.err.println("Got here B1");
		boolean same = getClass().equals(vs.getClass());
		if (same && this != vs) {
			// System.err.println(" got here size "+size());
			JoinVersionSpace cvs = (JoinVersionSpace)vs;
			// System.err.println(" got here cvs.size "+cvs.size());
			if (size() == cvs.size()) {
				for (int vses = 0;
				     same && vses < size() && vses < cvs.size();
                                     vses++) {
					// System.err.println(" got here AB");
					VersionSpace vs1 = (VersionSpace)get(vses);
					// System.err.println(" got here AC");
					VersionSpace vs2 = (VersionSpace)cvs.get(vses);
					// System.err.println(" got here AD");
					if (!vs1.equals(vs2)) {
						same = false;	
						// System.err.println(" same 1 "+same);
					} else {
						// System.err.println(" same 2 "+same);
					}
					// System.err.println(" got here AE");
					// System.err.println(" vses "+vses);
				}
				// System.err.println("got here out");
			} else {
				same = false;
				// System.err.println(" same 3 "+same);
			}
			// System.err.println(" same 4 "+same);
		}
		// System.err.println(" same 5 "+same);
		return same;
	}
}

abstract class UnionVersionSpace extends HashSet implements CompositeVersionSpace {
	// equals is defined in HashSet
	// version spaces in a union are glommed together
	public void update(VersionSpace vs) {
		if (vs instanceof UnionVersionSpace) {
			UnionVersionSpace cvs = (UnionVersionSpace)vs;
			addAll(cvs);
		} else {
			add(vs);
		}
	}
	public void indent(PrintStream sb, int level) {
		for (int i = 0; i < level; i++) {
			sb.print(" ");
		}
	}
	public boolean equals(VersionSpace vs) {
		UnionVersionSpace uvs = (UnionVersionSpace)vs;
		return super.equals(uvs);
	}
}

///////////////////// ACTIONS /////////////////////////

abstract class ActionVersionSpace extends UnionVersionSpace {
	public void indent(PrintStream sb, int level) {
		for (int i = 0; i < level; i++) {
			sb.print(" ");
		}
	}
	public void update(VersionSpace vs) {
		ActionVersionSpace avs = (ActionVersionSpace)vs;
		addAll(avs);
	}
}

class CardVersionSpace extends JoinVersionSpace {
	CardItem cim;
	public CardVersionSpace(int rank, int suit, boolean faceUp, int stack, int position) {
		this.cim = new CardItem(suit, rank, faceUp, stack, position);
		add(this.cim);
	}
	public CardVersionSpace(CardItem cim) {
		this.cim = (CardItem)cim.clone();
		add(this.cim);
	}
	public Stack getStack() {
		return Game.getStack(cim.getStack());
	}
	public int getRank() {
		return cim.getRank();
	}
	public int getSuit() {
		return cim.getSuit();
	}
	public boolean isVisible() {
		return cim.getFaceUp();
	}
	public int getStackNumber() {
		return cim.getStack();
	}
	public int getPosition() {
		return cim.getPosition();
	}
	public boolean equals(VersionSpace vs) {
		CardVersionSpace cvs = (CardVersionSpace)vs;
		return this.cim.equals(cvs.cim);
	}
	public void print(PrintStream sb, int level) {
	}
}

abstract class CardActionVersionSpace extends ActionVersionSpace {
	// add() works here for CardVersionSpaces
	public void print(PrintStream sb, int level) {
		String name = getClass().getName();

		indent(sb, level);
		sb.print("CardItem cim = new CardItem(suit, rank, faceUp, stack, position);\n");
		indent(sb, level);
		sb.print("CardVersionSpace cvs = new CardVersionSpace(cim);\n");
		indent(sb, level);
		sb.print(name);
		sb.print(" cavs = new "+name+"();\n");
		indent(sb, level);
		sb.print("cavs.add(cvs);\n");
		indent(sb, level);
		sb.print("if (psvs != null) {\n");
		indent(sb, level+1);
		sb.print("psvs.addAction(cavs);\n");
		indent(sb, level);
						
		sb.print("}\n");
	}
	public HashSet getRanks() {
		HashSet hs = new HashSet();
		Iterator i = iterator();
		while (i.hasNext()) {
			CardVersionSpace cvs = (CardVersionSpace)i.next();
			hs.add(new Integer(cvs.getRank()));
		}
		return hs;
	}
	public HashSet getRankDistances(int rank) {
		HashSet hs = new HashSet();
		Iterator i = iterator();
		while (i.hasNext()) {
			CardVersionSpace cvs = (CardVersionSpace)i.next();
			hs.add(new Integer(rank - cvs.getRank()));
		}
		return hs;
	}
	public HashSet getSuits() {
		HashSet hs = new HashSet();
		Iterator i = iterator();
		while (i.hasNext()) {
			CardVersionSpace cvs = (CardVersionSpace)i.next();
			hs.add(new Integer(cvs.getSuit()));
		}
		return hs;
	}
	public HashSet getSuitDistances(int suit) {
		HashSet hs = new HashSet();
		Iterator i = iterator();
		while (i.hasNext()) {
			CardVersionSpace cvs = (CardVersionSpace)i.next();
			hs.add(new Integer(suit - cvs.getSuit()));
		}
		return hs;
	}
	public HashSet getStacks() {
		HashSet hs = new HashSet();
		Iterator i = iterator();
		while (i.hasNext()) {
			CardVersionSpace cvs = (CardVersionSpace)i.next();
			hs.add(new Integer(cvs.getStackNumber()));
		}
		return hs;
	}
	public HashSet getPositions() {
		HashSet hs = new HashSet();
		Iterator i = iterator();
		while (i.hasNext()) {
			CardVersionSpace cvs = (CardVersionSpace)i.next();
			hs.add(new Integer(cvs.getPosition()));
		}
		return hs;
	}
	public boolean allVisible() {
		boolean b = true;
		Iterator i = iterator();
		while (i.hasNext()) {
			CardVersionSpace cvs = (CardVersionSpace)i.next();
			if (!cvs.isVisible()) {
				b = false;
				break;
			}
		}
		return b;
	}
	public boolean allInvisible() {
		boolean b = true;
		Iterator i = iterator();
		while (i.hasNext()) {
			CardVersionSpace cvs = (CardVersionSpace)i.next();
			if (cvs.isVisible()) {
				b = false;
				break;
			}
		}
		return b;
	}
}

class PlayVersionSpace extends CardActionVersionSpace {
}

class PickVersionSpace extends CardActionVersionSpace {
}

class MakeVisibleVersionSpace extends CardActionVersionSpace {
}

class MakeInvisibleVersionSpace extends CardActionVersionSpace {
}

class NewStackVersionSpace extends ActionVersionSpace {
	int stack;
	public NewStackVersionSpace(int stack) {
		this.stack = stack;
	}
	public void print(PrintStream sb, int level) {
		indent(sb, level);
		sb.print("NEWSTACK("+stack+");\n");
	}
	public boolean equals(VersionSpace vs) {
		return super.equals(vs) && stack == ((NewStackVersionSpace)vs).stack;
	}
}

///////////// CONDITIONS ///////////////////////

abstract class IfCardVersionSpace extends IfVersionSpace {
	CardActionVersionSpace cavs;
	protected IfCardVersionSpace(CardActionVersionSpace cavs) {
		this.cavs = cavs;
	}
	public void initialize(PrintStream sb, int level) {
		indent(sb, level);
		sb.print("if (!processed && ");
	}
	public void print(PrintStream sb, int level) {
		sb.print(") {\n");
		super.print(sb, level);
		indent(sb, level+1);
		sb.print("processed = true;\n");
		indent(sb, level);
		sb.print("}\n");
	}
	public boolean equals(VersionSpace vs) {
		return super.equals(vs) && cavs.equals(((IfCardVersionSpace)vs).cavs);
	}
	public String getFunctionName() {
		return "GenericCardTest";
	}
}

class SuitDifferenceVersionSpace extends IfCardVersionSpace {
	// difference in suit version space
	int delta; // goes from -3 to 3.  -2, 0, 2 mean same color
	public SuitDifferenceVersionSpace (CardActionVersionSpace cavs) {
		super(cavs);
		if (cavs instanceof PickVersionSpace) {
			this.delta = Integer.parseInt(cavs.getSuitDistances(((CardVersionSpace)cavs.iterator().next()).getStack().getTopCard().getSuit()).iterator().next().toString());
		} else {
			this.delta = Integer.parseInt(cavs.getSuitDistances(((CardVersionSpace)cavs.iterator().next()).getStack().getSecondCard().getSuit()).iterator().next().toString());
		}
	}
	public void print(PrintStream sb, int level) {
		initialize(sb, level);
		sb.print("rank - GameMiner.getStack(stack).getTopCard().rank == ");
		sb.print(delta);
		super.print(sb, level);
	}
	public void getParameters(PrintStream sb) {
		sb.print("rank, suit, faceUp, stack, position, psvs");
	}
	public String getFunctionName() {
		return "SuitDifference";
	}
	public boolean equals(VersionSpace vs) {
		return super.equals(vs) && delta == ((SuitDifferenceVersionSpace)vs).delta;
	}
}
class RankDifferenceVersionSpace extends IfCardVersionSpace {
	// difference in rank version space
	int delta; // goes from -12 to 12
	public RankDifferenceVersionSpace (CardActionVersionSpace cavs) {
		super(cavs);
		if (cavs instanceof PickVersionSpace) {
			this.delta = Integer.parseInt(cavs.getRankDistances(((CardVersionSpace)cavs.iterator().next()).getStack().getTopCard().getRank()).iterator().next().toString());
		} else {
			this.delta = Integer.parseInt(cavs.getRankDistances(((CardVersionSpace)cavs.iterator().next()).getStack().getSecondCard().getRank()).iterator().next().toString());
		}
	}
	public void print(PrintStream sb, int level) {
		initialize(sb, level);
		sb.print("rank - GameMiner.getStack(stack).getTopCard().rank == ");
		sb.print(delta);
		super.print(sb, level);
	}
	public void getParameters(PrintStream sb) {
		sb.print("rank, suit, faceUp, stack, position, psvs");
	}
	public String getFunctionName() {
		return "RankDifference";
	}
	public boolean equals(VersionSpace vs) {
		return super.equals(vs) && delta == ((RankDifferenceVersionSpace)vs).delta;
	}
}

class StackEmptyVersionSpace extends IfCardVersionSpace {
	// if stack is empty
	public StackEmptyVersionSpace (CardActionVersionSpace cavs) {
		super(cavs);
	}
	public void print(PrintStream sb, int level) {
		initialize(sb, level);
		sb.print("stack empty");
		super.print(sb, level);
	}
	public void getParameters(PrintStream sb) {
		sb.print("rank, suit, faceUp, stack, position, psvs");
	}
	public String getFunctionName() {
		return "StackEmpty";
	}
}

class VisibleVersionSpace extends IfCardVersionSpace {
	public VisibleVersionSpace (CardActionVersionSpace cavs) {
		super(cavs);
	}
	public void print(PrintStream sb, int level) {
		initialize(sb, level);
		sb.print("faceUp == true");
		super.print(sb, level);
	}
	public void getParameters(PrintStream sb) {
		sb.print("rank, suit, faceUp, stack, position, psvs");
	}
	public String getFunctionName() {
		return "Visible";
	}
}

class NotVisibleVersionSpace extends IfCardVersionSpace {
	public NotVisibleVersionSpace (CardActionVersionSpace cavs) {
		super(cavs);
	}
	public void print(PrintStream sb, int level) {
		initialize(sb, level);
		sb.print("faceUp == false");
		super.print(sb, level);
	}
	public void getParameters(PrintStream sb) {
		sb.print("rank, suit, faceUp, stack, position, psvs");
	}
	public String getFunctionName() {
		return "Invisible";
	}
}

class TopCardVersionSpace extends IfCardVersionSpace {
	public TopCardVersionSpace (CardActionVersionSpace cavs) {
		super(cavs);
	}
	public void print(PrintStream sb, int level) {
		initialize(sb, level);
		sb.print("position == 0");
		super.print(sb, level);
	}
	public void getParameters(PrintStream sb) {
		sb.print("rank, suit, faceUp, stack, position, psvs");
	}
	public String getFunctionName() {
		return "TopCard";
	}
}

class NotTopCardVersionSpace extends IfCardVersionSpace {
	public NotTopCardVersionSpace (CardActionVersionSpace cavs) {
		super(cavs);
	}
	public void print(PrintStream sb, int level) {
		initialize(sb, level);
		sb.print("position != 0");
		super.print(sb, level);
	}
	public void getParameters(PrintStream sb) {
		sb.print("rank, suit, faceUp, stack, position, psvs");
	}
	public String getFunctionName() {
		return "NotTopCard";
	}
}

class EqualRankVersionSpace extends IfCardVersionSpace {
	// tests to see whether a card is a certain rank
	int rank;
	public EqualRankVersionSpace(CardActionVersionSpace cavs) {
		super(cavs);
		this.rank = Integer.parseInt(cavs.getRanks().iterator().next().toString());
	}
	public void print(PrintStream sb, int level) {
		initialize(sb, level);
		sb.print("rank == ");
		sb.print(rank);
		super.print(sb, level);
	}
	public void getParameters(PrintStream sb) {
		sb.print(rank+", suit, faceUp, stack, position, psvs");
	}
	public String getFunctionName() {
		return "EqualRank";
	}
	public boolean equals(VersionSpace vs) {
		return super.equals(vs) && rank == ((EqualRankVersionSpace)vs).rank;
	}
}

class NotEqualRankVersionSpace extends IfCardVersionSpace {
	// tests to see whether a card is a certain rank
	int rank;
	public NotEqualRankVersionSpace(CardActionVersionSpace cavs) {
		super(cavs);
		this.rank = Integer.parseInt(cavs.getRanks().iterator().next().toString());
	}
	public void print(PrintStream sb, int level) {
		initialize(sb, level);
		sb.print("rank != ");
		sb.print(rank);
		super.print(sb, level);
	}
	public void getParameters(PrintStream sb) {
		sb.print(rank+", suit, faceUp, stack, position, psvs");
	}
	public String getFunctionName() {
		return "NotEqualRank";
	}
	public boolean equals(VersionSpace vs) {
		return super.equals(vs) && rank == ((NotEqualRankVersionSpace)vs).rank;
	}
}

class EqualSuitVersionSpace extends IfCardVersionSpace {
	// tests to see whether a card is a certain suit
	int suit;
	public EqualSuitVersionSpace(CardActionVersionSpace cavs) {
		super(cavs);
		this.suit = Integer.parseInt(cavs.getSuits().iterator().next().toString());
	}
	public void print(PrintStream sb, int level) {
		initialize(sb, level);
		sb.print("suit == ");
		sb.print(suit);
		super.print(sb, level);
	}
	public void getParameters(PrintStream sb) {
		sb.print("rank, "+suit+", faceUp, stack, position, psvs");
	}
	public String getFunctionName() {
		return "EqualSuit";
	}
	public boolean equals(VersionSpace vs) {
		return super.equals(vs) && suit == ((EqualSuitVersionSpace)vs).suit;
	}
}

class NotEqualSuitVersionSpace extends IfCardVersionSpace {
	// tests to see whether a card is a certain suit
	int suit;
	public NotEqualSuitVersionSpace(CardActionVersionSpace cavs) {
		super(cavs);
		this.suit = Integer.parseInt(cavs.getSuits().iterator().next().toString());
	}
	public void print(PrintStream sb, int level) {
		initialize(sb, level);
		sb.print("suit != ");
		sb.print(suit);
		super.print(sb, level);
	}
	public void getParameters(PrintStream sb) {
		sb.print("rank, "+suit+", faceUp, stack, position, psvs");
	}
	public String getFunctionName() {
		return "NotEqualSuit";
	}
	public boolean equals(VersionSpace vs) {
		return super.equals(vs) && suit == ((NotEqualSuitVersionSpace)vs).suit;
	}
}

//////////// STACK //////////////////////

class StackItem implements Cloneable {
	int current_action;
	ProgramVersionSpace current_program;
	public StackItem(ProgramVersionSpace pvs) {
		current_program = pvs;
		current_action = 0;
	}
	public Object clone() {
		try {
			StackItem si = (StackItem)super.clone();
			si.current_action = current_action;
			// copy the program, so a bunch of duplicate items
			// won't get added
			si.current_program = (ProgramVersionSpace)current_program.clone();
			return si;
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return this; // if all else fails, report and send back non clone
		}
	}
}

class CPUStack extends java.util.Stack implements Cloneable {
	static ArrayList stacks = new ArrayList();
	public Object clone() {
		// System.err.println("Cloning "+this);
		CPUStack stack = (CPUStack)super.clone();  // new stack
		stack.clear();
		Iterator i = iterator();
		while (i.hasNext()) {
			StackItem si = (StackItem)((StackItem)i.next()).clone();
			si.current_program.stack = stack;
			stack.add(si);
		}
		// System.err.println("Cloned "+stack);
		return stack;
	}
}

////////////// CONTROL STRUCTURES ////////////////////

abstract class IfVersionSpace extends ProgramsVersionSpace {
	public IfVersionSpace() {
	}
	public void print(PrintStream sb, int level) {
		// indent(sb, level);
		Iterator i = iterator();
		HashSet printed = new HashSet();
		while (i.hasNext()) {
			ProgramVersionSpace pvs = (ProgramVersionSpace)i.next();
			// don't print duplicate pvs'es
			if (!printed.contains(pvs)) {
				printed.add(pvs);
				if (pvs.size() > 0) {
					indent(sb, level+1);
					sb.print(getFunctionName()+pvs.prog_no+"(");
					getParameters(sb);
					sb.print(");\n");
				}
			}
		}
	}
	public void pushAll(ProgramVersionSpace pvs ) {
		Iterator i = iterator();
		HashSet printed = new HashSet();
		while (i.hasNext()) {
			CPUStack stack = (CPUStack)pvs.stack.clone();
			ProgramVersionSpace newpvs = (ProgramVersionSpace)i.next();
			newpvs.setCPUStack(stack);
		}
	}
	abstract public void getParameters(PrintStream sb);
	public String getFunctionName() {
		return "GenericTest";
	}
	public boolean equals(VersionSpace vs) {
		return super.equals(vs);
	}
}

//////////// PROGRAM AND PROGRAMS //////////////////////

class ProgramVersionSpace extends JoinVersionSpace {
	// join of action version spaces and other program version spaces
	CPUStack stack;
	static int prog_count = 0;
	int prog_no = 0;
	String name = "";
	public ProgramVersionSpace(CPUStack stack) {
		setCPUStack(stack);
		prog_no =  prog_count;
		prog_count++;
	}
	public void setCPUStack(CPUStack stack) {
		this.stack = stack;
		StackItem si = new StackItem(this);
		stack.push(si);
	}
	public int hashCode() {
		return prog_no;
	}
	public String getFunctionName() {
		return name;
	}
	public void setFunctionName(String name) {
		this.name = name;
	}
	public Object clone() {
		ProgramVersionSpace pvs = (ProgramVersionSpace)super.clone();
		// pvs.stack = (CPUStack)stack.clone();  // don't clone, recurs
		pvs.prog_no =  prog_count;
		prog_count++;
		return pvs;
	}

	public void add(VersionSpace vs) {
		add(((StackItem)stack.peek()).current_action, vs);
		((StackItem)stack.peek()).current_action++;
	}
	public void print(PrintStream sb, int level) {
		Iterator vses = iterator();
		while (vses.hasNext()) {
			VersionSpace vs = (VersionSpace)vses.next();
			vs.print(sb, level+1);
		}
	}
	public boolean check(VersionSpace vs) {
		StackItem si = (StackItem)stack.peek();
		int size = si.current_program.size();
		if (stack.size() > 3) {  // limit depth of calls
			return false;
		} else if (si.current_action >= 52) { // limited size program
			return false;
		} else if (si.current_action >= size) { // if adding more
			// System.err.println("Null");
			return true;
		} else {
			VersionSpace cvs = (VersionSpace)si.current_program.get(si.current_action);
			if (vs.getClass() == cvs.getClass()) {  // if similar actions
				System.err.println("Similar do not add");
				// TODO generalize/union vs and cvs into a new
				// version space
				((StackItem)stack.peek()).current_action++;
				return false;
			} else { // dissimilar actions, bail
				System.err.println("Dissimilar");
				return true;  // interesting, let's add it
			}
		}
	}
	public boolean equals(VersionSpace vs) {
		return super.equals(vs) && name.equals(((ProgramVersionSpace)vs).name);
	}
}

class ProgramsVersionSpace extends UnionVersionSpace {
	static ArrayList programs = new ArrayList();
	static int reps = 0;
	static int progscount = 0;
	int progno = 0;
	ProgramVersionSpace mainvs;
	public ProgramsVersionSpace() {
		programs.add(this);
		progno = progscount;
		progscount++;
		System.err.println("Adding programs "+progno);
	}
	public void mashDuplicates() {
		HashMap replacement = new HashMap();
		Iterator psvses = programs.iterator();
		while (psvses.hasNext()) {
			ProgramsVersionSpace psvs = (ProgramsVersionSpace)psvses.next();
			System.err.println("Processing ProgramsVersionSpace "+psvs.progno);
			Iterator progs = psvs.iterator();
			while (progs.hasNext()) {
		// System.err.println("Got here 1A");
				ProgramVersionSpace pvs = (ProgramVersionSpace)progs.next();
				Iterator progs2 = psvs.iterator();
				while (progs2.hasNext()) {
		// System.err.println("Got here 1B");
					ProgramVersionSpace pvs2 = (ProgramVersionSpace)progs2.next();
		// System.err.println("Got here 1B1");
					if (pvs != null && pvs2 != null && pvs != pvs2 && pvs.equals(pvs2) ) {
		// System.err.println("Got here 1C");
						
						if (!replacement.containsKey(pvs2)) {
		// System.err.println("Got here 1D");
							replacement.put(pvs2, pvs);
						}
						
					}
		// System.err.println("Got here 1E");
				}
			}
		}
		Iterator repl = replacement.keySet().iterator();
		int c = 0;
		while (repl.hasNext()) {
			ProgramVersionSpace pvs2 = (ProgramVersionSpace)repl.next();
			ProgramVersionSpace pvs = (ProgramVersionSpace)replacement.get(pvs2);
			if (c % 1000 == 0) {
				System.err.println("Replace #"+c+" dup "+pvs2.prog_no+" with "+pvs.prog_no);
			}
			c++;

			//  replace pvs2 with pvs
			psvses = programs.iterator();
			while (psvses.hasNext()) {
		// System.err.println("Got here 1G");
				ArrayList al = new ArrayList();
				ProgramsVersionSpace psvs = (ProgramsVersionSpace)psvses.next();
				ArrayList lpsvs = new ArrayList();
				lpsvs.addAll(psvs);
				ListIterator progs = lpsvs.listIterator(); // get the stacks
				while (progs.hasNext()) {
		// System.err.println("Got here 1H");
					ProgramVersionSpace pvs3 = (ProgramVersionSpace)progs.next();
					if (pvs3.equals(pvs2)) {
						progs.remove();
						progs.add(pvs);
		// System.err.println("Got here 2");
					}
				}
				psvs.clear();
				psvs.addAll(lpsvs);
			}
		}
		// get rid of duplicate pvs in a psvs
		psvses = programs.iterator();
		while (psvses.hasNext()) {
			ProgramsVersionSpace psvs = (ProgramsVersionSpace)psvses.next();
			System.err.println("Processing 2 ProgramsVersionSpace "+psvs.progno);
			HashSet nodups = new HashSet();
			Iterator progs = psvs.iterator(); // get the stacks
			while (progs.hasNext()) {
				ProgramVersionSpace pvs = (ProgramVersionSpace)progs.next();
				if (!nodups.contains(pvs)) {
					nodups.add(pvs);
					// System.err.println("Got here 3");
				}
			}
			psvs.clear();
			psvs.addAll(nodups);
		}
	}

	public void print(PrintStream sb, int level) {
		mashDuplicates();
		sb.print("*/\n");
		sb.print("import java.util.*;\n");
		sb.print("import java.io.*;\n");
		sb.print("public class Solitaire {\n");
		sb.print("  static public void main(String args[]) throws Exception {\n");
		sb.print("    for (int a = 0; a < args.length; a++) {\n"); 
		sb.print("      StackMiner.init();\n");
		sb.print("      GameMiner.init();\n");
		sb.print("      FileReader fr = new FileReader(args[a]);\n");
		sb.print("      BufferedReader br = new BufferedReader(fr);\n");
		sb.print("      String line = null;");
		sb.print("      ProgramsVersionSpace psvs = new ProgramsVersionSpace();\n");
		sb.print("      while ((line = br.readLine()) != null) {\n");
		sb.print("        GameMiner.processLine(line, psvs);\n");
		sb.print("      }\n");
		sb.print("      br.close();\n");
		sb.print("      fr.close();\n");
		sb.print("      psvs.print(System.out, 0);\n");
		sb.print("      psvs.reset();\n");
		sb.print("    }\n");
		sb.print("  }\n");
		sb.print("  static public void PLAY(int rank, int suit, boolean faceUp, int stack, int position) {\n");
		sb.print("    System.out.println(\"PLAY|\"+stack+\"|\"+position+\"|\"+rank+\"|\"+suit);\n");
		sb.print("  }\n");
		sb.print("  static public void NEWSTACK(int stack) {\n");
		sb.print("    System.out.println(\"NEWSTACK|\"+stack+\"|0|0\");\n");
		sb.print("  }\n");
		Iterator psvses = programs.iterator();
		while (psvses.hasNext()) {
			ProgramsVersionSpace psvs = (ProgramsVersionSpace)psvses.next();
			Iterator progs = psvs.iterator(); // get the stacks
			HashSet nodups = new HashSet();
			while (progs.hasNext()) {
				ProgramVersionSpace pvs = (ProgramVersionSpace)progs.next();
				if (!nodups.contains(pvs) && pvs.size() > 0) {
					nodups.add(pvs);
					indent(sb, level);
					sb.print("static public void ");
					sb.print(pvs.getFunctionName()+pvs.prog_no+"(int rank, int suit, boolean faceUp, int stack, int position, ProgramsVersionSpace psvs) {\n");
					indent(sb, level+1);
					sb.print("boolean processed = false;\n");
					pvs.print(sb, level+1);
					indent(sb, level);
					sb.print("}\n");
				}
			}
		}
		sb.print("}\n");
	}
	public void addAction(ActionVersionSpace avs) {
		if (avs instanceof CardActionVersionSpace) {
			// System.err.println("Here A");
			CardActionVersionSpace cavs = (CardActionVersionSpace)avs;
			// Create Conditional Version Spaces
			SuitDifferenceVersionSpace sdvs = null;
			try {
				sdvs = new SuitDifferenceVersionSpace(cavs);
			} catch (Exception sde) {
			}
			RankDifferenceVersionSpace rdvs = null;
			try {
				rdvs = new RankDifferenceVersionSpace(cavs);
			} catch (Exception sde) {
			}
			EqualSuitVersionSpace esvs = new EqualSuitVersionSpace(cavs);
			NotEqualSuitVersionSpace nesvs = new NotEqualSuitVersionSpace(cavs);
			EqualRankVersionSpace ervs = new EqualRankVersionSpace(cavs);
			NotEqualRankVersionSpace nervs = new NotEqualRankVersionSpace(cavs);
			VisibleVersionSpace vvs = new VisibleVersionSpace(cavs);
			NotVisibleVersionSpace nvvs = new NotVisibleVersionSpace(cavs);
			TopCardVersionSpace tcvs = new TopCardVersionSpace(cavs);
			NotTopCardVersionSpace ntcvs = new NotTopCardVersionSpace(cavs);
			// System.err.println("Here B");

			// Add to current progs and Initialize
			if (sdvs != null) {
				addVersionSpace(sdvs);
			}
			if (rdvs != null) {
				addVersionSpace(rdvs);
			}
			addVersionSpace(esvs);
			addVersionSpace(nesvs);
			addVersionSpace(ervs);
			addVersionSpace(nervs);
			addVersionSpace(vvs);
			addVersionSpace(nvvs);
			addVersionSpace(tcvs);
			addVersionSpace(ntcvs);
			// System.err.println("Here C");

			// Add card action
			if (sdvs != null) {
				sdvs.addVersionSpace(cavs);
			}
			if (rdvs != null) {
				rdvs.addVersionSpace(cavs);
			}
			esvs.addVersionSpace(cavs);
			nesvs.addVersionSpace(cavs);
			ervs.addVersionSpace(cavs);
			nervs.addVersionSpace(cavs);
			vvs.addVersionSpace(cavs);
			nvvs.addVersionSpace(cavs);
			tcvs.addVersionSpace(cavs);
			ntcvs.addVersionSpace(cavs);
			// System.err.println("Here D");
		} else {
			addVersionSpace(avs);
		}
		System.err.println(avs.getClass().getName());
		reps++;
		if (reps % 4 == 0) {
			mashDuplicates();
			System.err.println("Done cleaning");
		}
	}
	public boolean consistent(ProgramVersionSpace pvs, VersionSpace vs) {
		return pvs.check(vs);
	}
	public void addVersionSpace(VersionSpace vs) {
		ProgramVersionSpace pvs;
		if (size() == 0 && CPUStack.stacks.size() == 0) {
			// also add as new, to start us off,
			// and to create subroutines
			reset();
		}
		ArrayList al = new ArrayList();

		// add to the end of the top stack program
		Iterator stacks = CPUStack.stacks.iterator();
		while (stacks.hasNext()) {
			CPUStack stack = (CPUStack)stacks.next();
			pvs = ((StackItem)stack.peek()).current_program;

			int act = ((StackItem)stack.peek()).current_action;
			if (act < pvs.size()) {
				// changing the program here
				VersionSpace ovs = (VersionSpace)pvs.get(act);
				// TODO merge the two classes
				if (vs.getClass() == ovs.getClass()) {
					ovs.update(vs); // updates ovs in place
					if (ovs instanceof IfVersionSpace) {
						IfVersionSpace ifvs = (IfVersionSpace)ovs;
						ifvs.pushAll(pvs);
					} else {
						((StackItem)stack.peek()).current_action++;
					}
				}
			} else if (this instanceof IfVersionSpace) {
				// adding to end here
				CPUStack ifstack = (CPUStack)pvs.stack.clone();
				ProgramVersionSpace newpvs = new ProgramVersionSpace(ifstack);
				newpvs.setFunctionName(getFunctionName());
				if (consistent(pvs, newpvs)) {
					// System.err.println("Adding an if");
					al.add(newpvs.stack);
					add(newpvs);
					if (consistent(newpvs, vs)) {
						// System.err.println("Adding if first statement");
						newpvs.add(vs);
					}
				}
			} else {
				// adding to end here
				if (consistent(pvs, vs)) {
					// System.err.println("Adding normal");
					pvs.add(vs);
				} else {
					// System.err.println("Destroying");
					stacks.remove(); // destroy program
				}
			}
		}
		CPUStack.stacks.addAll(al);
	}
	public String getFunctionName() {
		return "AGenericSetOfPrograms";
	}
	public void reset() {
		CPUStack.stacks.clear();
		ProgramVersionSpace pvs;
		if (size() > 0 && mainvs != null) {
			pvs = mainvs;
			pvs.setCPUStack(new CPUStack());
		} else {
			pvs = new ProgramVersionSpace(new CPUStack());
			pvs.setFunctionName("main");
			mainvs = pvs;
			add(pvs);
		}
		CPUStack.stacks.add(pvs.stack);
	}
}
