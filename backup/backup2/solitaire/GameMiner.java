package solitaire;

import java.io.*;
import java.util.*;
import javax.swing.JFrame;
import org.json.*;

public class GameMiner {
	public static Game game = null;
	public static JFrame jf = null;
	static public Stack getStack(JSONObject obj, String name) {
		if (obj.has(name)) {
			Integer no = obj.getInt(name);
			System.err.println("Retrieving stack "+no);
			Stack s = Game.getStack(no);
			return s;
		}
		return null;
	}
	static public void processObj(JSONObject obj) {
		String command = obj.getString("command");
		System.err.println("GameMiner.Processing "+obj);
		Stack fromStack = getStack(obj,"fromStack");
		Stack toStack = getStack(obj, "toStack");
		Integer toStackNo = obj.optInt("toStack");

		Integer fromPosition = obj.optInt("fromPosition");
		Integer toPosition = obj.optInt("toPosition");
		CardItem cim = null;
		if (fromStack != null && fromPosition != null) {
			cim = fromStack.elementAt(fromPosition);
		}

		Integer rank = null;
		if (obj.has("rank")) {
			rank = obj.getInt("rank");
		}
		Integer suit = null;
		if (obj.has("suit")) {
			suit = obj.getInt("suit");
		}
		if (obj.has("id")) {
			Integer id = obj.optInt("id");
			if (id != null) {
				rank = id % 13 + 1;
				System.err.println(id+" computing rank to "+rank);
				suit = id / 13;
				System.err.println(id+" computing suit to "+suit);
			}
		}

		if (cim != null && suit != null) {
			System.err.println("Setting suit to "+suit);
			cim.setSuit(suit);
		}
		if (cim != null && rank != null) {
			System.err.println("Setting rank to "+rank);
			cim.setRank(rank);
		}

		if (command.equals("MOVE")) {
			fromStack.remove(cim);
			toStack.insertElementAt(cim, toPosition);
		} else if (command.equals("INVISIBLE")) {
			cim.setFaceUpBE(false);
		} else if (command.equals("VISIBLE")) {
			System.err.println("Card is "+cim+" stack is "+cim.getStack());
			cim.setFaceUpBE(true);
		} else if (command.equals("NEWSTACK")) {
			Integer x = obj.getInt("x");
			Integer y = obj.getInt("y");
			Integer offset = obj.getInt("offset");
			String direction = obj.getString("direction");
			new Stack(toStackNo, x, y, offset, direction,
				 jf, game);
		} else if (command.equals("DEAL")) {
			if (toStack == null || toStack.stack_no != toStackNo) {
				toStack = new Stack(
				toStackNo,
				(100+ toStackNo * 100)%700,
				50*toStackNo % 450, 
				20,
				"X",
				jf, game);
			}
			int drawn = obj.getInt("drawn");
			int len =  toStack.size();
			for (int c = 0; toStack != null && c < drawn; c++) {
				CardItem ci = new CardItem();
				toStack.insertElementAt(ci, toStack.size());
			}
		}
	}
}
