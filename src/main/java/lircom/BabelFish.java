package lircom;

import java.io.*;
import java.net.*;
import com.meterware.httpunit.*;

public class BabelFish {
    static WebConversation trackerconversation = new WebConversation();
    static public String translate(String message, String from, String to) throws Exception {
		String out = null;
	    	HttpUnitOptions.setScriptingEnabled(false);
		WebRequest req = new PostMethodWebRequest("http://babelfish.yahoo.com/translate_txt");
		req.setParameter("ei", "UTF-8");
		req.setParameter("doit", "done");
		req.setParameter("fr", "bf-home");
		req.setParameter("intl", "1");
		req.setParameter("tt", "urltext");
		req.setParameter("trtext", message);
		req.setParameter("lp", from+"_"+to);
		System.err.println(message+" "+from+" "+to+" "+req.getQueryString());
		WebResponse response = trackerconversation.getResponse( req );
		WebForm[] forms = response.getForms();
		for (int i = 0; i < forms.length; i++) {
			String [] res = forms[i].getParameterValues("p");
			for (int r = 0; r < res.length; r++) {
				System.out.println("p="+res[r]);
				if (res[r] != null) {
					out = res[r];
				}
			}
		}
		if (out == null) {
			throw new Exception("Untranslated "+message);
		}
		return out;
    }
    static public void main(String args[]) throws Exception {
	String out = BabelFish.translate("thank you", "en", "de");
	System.out.println(out);
    }
}
