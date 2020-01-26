package lircom {
import java.io.*;

public class Kanal extends Channel {
        public function Kanal() throws Exception {
            try {
                setNick("Kanal");
            } catch (var e:Exception) {
            }
            joincmd = "^[ \t]*tilslut[ \t]*$";
            leavecmd = "^[ \t]*exit[ \t]*$";
            helpcmd = "^[ \t]*hj?lp[ \t]*$";
            whocmd = "^[ \t]*hvem[ \t]*$";
            helpmsg = "Brug komandoen \"tilslut\" for at tilslutte til en kanal og komandoen \"exit\" for at forlade kanalen. For at finde ud af hvem som er p? kanalen, skriv \"hvem\". Husk at have kanalen valgt til h?jre.";
        }
	public function Kanal(is:InputStream, os:OutputStream) throws Exception {
            super(is, os);
            setNick("Kanal");
            joincmd = "^[ \t]*tilslut[ \t]*$";
            leavecmd = "^[ \t]*exit[ \t]*$";
            helpcmd = "^[ \t]*hj?lp[ \t]*$";
            whocmd = "^[ \t]*hvem[ \t]*$";
            helpmsg = "Brug komandoen \"tilslut\" for at tilslutte til en kanal og komandoen \"exit\" for at forlade kanalen. For at finde ud af hvem som er p? kanalen, skriv \"hvem\". Husk at have kanalen valgt til h?jre.";
	}
	public function addActions():void {
	    new Action(joincmd, JOIN_ACTION);
	    new Action(leavecmd, LEAVE_ACTION);
	    new Action(whocmd, WHO_ACTION);
	    new Action(helpcmd, HELP_ACTION);
	    new Action(normalcmd, NORMAL_ACTION);
	}
}
}