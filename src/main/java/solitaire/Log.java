package solitaire;

import java.io.FileWriter;
import java.util.*;

public class Log {
	static FileWriter fw = null;
	static FileWriter fw2 = null;
	static int stamp = 0;
	static lircom.ClientOnServer cos;
	static boolean enabled = true;
	public static void initialize(SolitaireClient solclient) throws Exception {
		cos = solclient;
	}
	static long ts = 0;
	public static void write(String msg) {
		try {
/*
			if  (fw == null) {
				fw = new FileWriter("log"+System.currentTimeMillis()+".txt");
			}
			//String out = stamp + "|" + msg + "\n";
			String out = msg + "\n";
			//stamp++;
			// System.out.print(out);
			fw.write(out);
			fw.flush();
*/
			if (enabled) {
				if (cos !=  null) {
					synchronized(cos) {
						lircom.Message m = new lircom.Message("*", cos.getNick(), msg, "__");
						// System.err.println("writing "+msg);
						cos.send(m);
						// cos.receive(m.generate());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// ArrayList playHistory = new ArrayList<CardItem[][]>();
	public static void write(CardItem[][] cards) {
		/*
		CardItem[][]  play = new CardItem[CardItem.King][CardItem.Clubs+1];
		CardItem[][] lastPlay = null;
		if (playHistory.size() > 0) {
			lastPlay = playHistory.lastElement();
		}
		ArrayList differences = new ArrayList();
		for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
			for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
				 play[irank-1][isuit] = cards[irank-1][isuit];
				 if (lastPlay != null) {
					 if (lastPlay[irank-1][isuit].rank != play[irank-1][isuit].rank) {
						 differences.add(new RankDifference(irank-1, isuit, lastPlay[irank-1][isuit].rank, play[irank-1][isuit].rank));
					}
	suit
	faceUp
	stack
	position
				}
			}
		}
		playHistory.add(play);
		*/
/*
		// print out in Quinlan's FOIL format
		try {
			if  (fw2 == null) {
				fw2 = new FileWriter("state"+System.currentTimeMillis()+".txt");
				fw2.write("*RANK: ");
				boolean first = true;
				for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
					if (first) {
						first = false;
					} else {
						fw2.write(",");
					}
					fw2.write(irank+"");
				}
				fw2.write("\n");

				fw2.write("SUIT: ");
				first = true;
				for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
					if (first) {
						first = false;
					} else {
						fw2.write(",");
					}
					fw2.write(isuit+"");
				}
				fw2.write("\n");

				fw2.write("FACEUP: false,true\n");

				fw2.write("#STACK: ");
				first = true;
				for (int st = -1; st < 15; st++) {
					if (first) {
						first = false;
					} else {
						fw2.write(",");
					}
					fw2.write(st+"");
				}
				fw2.write("\n");

				fw2.write("*POSITION: ");
				first = true;
				for (int po = -1; po < 52; po++) {
					if (first) {
						first = false;
					} else {
						fw2.write(",");
					}
					fw2.write(po+"");
				}
				fw2.write("\n\n");

				fw2.write("CARDS(");
				first = true;
				for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
					for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
						if (first) {
							first = false;
						} else {
							fw2.write(",");
						}
						fw2.write("RANK,");
						fw2.write("SUIT,");
						fw2.write("FACEUP,");
						fw2.write("STACK,");
						fw2.write("POSITION");
					}
				}
				fw2.write(")\n");
			}
			boolean first = true;
			for (int isuit = CardItem.Hearts; isuit <= CardItem.Clubs; isuit++) {
				for (int irank = CardItem.Ace; irank <= CardItem.King; irank++) {
					if (first) {
						first = false;
					} else {
						fw2.write(",");
					}
					CardItem cim = cards[irank-1][isuit];
					if (cim != null) {
						fw2.write(cim.rank+",");
						fw2.write(cim.suit+",");
						fw2.write(cim.faceUp+",");
						fw2.write(cim.stack+",");
						fw2.write(cim.position+"");
					} else {
						fw2.write("-1,");
						fw2.write("0,");
						fw2.write("false,");
						fw2.write("-1,");
						fw2.write("-1");
					}
				}
			}
			fw2.write("\n");
			fw2.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
*/
	}
}
