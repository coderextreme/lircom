package solitaire;

import java.io.*;
import java.util.*;
import javax.swing.*;
import org.json.*;

class GUIThread extends Thread {
	JFrame jf;
	int toStackNo;
	JSONArray cards;
	public GUIThread(int toStackNo, JFrame jf, JSONArray cards) {
		this.toStackNo = toStackNo;
		this.jf = jf;
		this.cards = cards;
	}
	public synchronized void run() {
		Stack toStack = Game.stacks.get(toStackNo);
		if (toStack == null) {
			int x = (int)Math.floor(toStackNo/5) * 200;
			int y = 100*toStackNo % 500;

			toStack = new Stack(this.toStackNo, x, y, this.jf);
		}
		this.jf.getContentPane().add(toStack.gui);
		if (cards != null) {
			System.err.println("Putting "+cards+" into stack "+toStack.stack_no);
			toStack.replaceAll(cards);
		}
	}
}

public class GameMiner {
	public static Game game = null;
	static public Stack getStack(JSONObject obj, String name) {
		if (obj.has(name)) {
			try {
				Integer no = obj.getInt(name);
				System.err.println("Retrieving stack "+no);
				Stack s = Game.getStack(no);
				return s;
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	static public void processObj(JSONObject obj) {
		try {
			String command = obj.getString("command");
			String principal = obj.optString("principal");
			Boolean flag = obj.optBoolean("flag");
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

			if (command.equals("use")) {
				fromStack.remove(cim);
				toStack.insertElementAt(cim, toPosition);
			} else if (command.equals("draw")) {
				if (obj.getString("object").equals("stack")) {
					JSONArray cards = obj.optJSONArray("cards");
					SwingUtilities.invokeLater(new GUIThread(toStackNo, game.jf, cards));
				} else {
					fromStack.insertElementAt(cim, 0);
				}
			} else if (command.equals("see")) {
				JSONArray cards = obj.optJSONArray("visible");
				System.err.println(cards);
				for (int c = 0; c < cards.length(); c++) {
					CardItem icim = Game.cards.get(c);
					icim.setCardCommandBE(principal, command, !cards.isNull(c), c);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
