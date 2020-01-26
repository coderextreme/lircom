package lircom {
import java.io.*;
import java.net.*;
import com.meterware.httpunit.*;

public class BabelFish {
    static var trackerconversation:WebConversation= new WebConversation();
    static public function translate(message:String, from:String, to:String):String throws Exception {
		var out:String= null;
		var req:WebRequest= new PostMethodWebRequest("http://babelfish.yahoo.com/translate_txt");
		req.setParameter("ei", "UTF-8");
		req.setParameter("doit", "done");
		req.setParameter("fr", "bf-home");
		req.setParameter("intl", "1");
		req.setParameter("tt", "urltext");
		req.setParameter("trtext", message);
		req.setParameter("lp", from+"_"+to);
		System.err.println(message+" "+from+" "+to+" "+req.getQueryString());
		var response:WebResponse= trackerconversation.getResponse( req );
		var forms:Array= response.getForms();
		for (var i:int= 0; i < forms.length; i++) {
			var res:String= forms[i].getParameterValue("q");
			if (res != null) {
				out = res;
			}
		}
		if (out == null) {
			throw new Exception("Untranslated "+message);
		}
		return out;
    }
    static public function main(String args[]):void throws Exception {
	var out:String= BabelFish.translate("thank you", "en", "de");
	System.out.println(out);
    }
}
}