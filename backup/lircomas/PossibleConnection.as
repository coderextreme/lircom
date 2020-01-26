/*
 * PossibleConnection.java
 *
 * Created on February 17, 2005, 9:05 PM
 */

package lircom {
/**
 *
 * @author carlsonj
 */
public class PossibleConnection {
    
    /** Creates a new instance of PossibleConnection */
    public function PossibleConnection() {
    }
	var host:String;
	var port:String;
        var nick:String;
        var date:String;
        var connected:Boolean;
	static var pcons:java.util.Hashtable= new java.util.Hashtable();
        var client:ClientOnServer;
	public function PossibleConnection(host:String, port:String, nick:String, date:String) {
		this.host = host;
		this.port = port;
                this.nick = nick;
                this.date = date;
                this.connected = false;
                if (get(host+"|"+port+"|"+nick) == null) {
                    add(this);
                }
	}
        static public function add(con:PossibleConnection):void {
           System.err.println("Adding "+con.host+" "+con.port+" "+con.nick);
           pcons.put(con.host+"|"+con.port+"|"+con.nick, con);
        }
        static function iterator():java.util.Iterator {
                return pcons.keySet().iterator();
        }
        static function get(str:Object):Object {
                return pcons.get(str);
        }
        static function get(host:String, port:String, nick:String):Object {
                var o:Object= get(host+"|"+port+"|"+nick);
                if (o != null) {
                    return o;
                } else {
                    return new PossibleConnection(host, port, nick, "");
                }
        }
}
}