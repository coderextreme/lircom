/*
 * ClientException.java
 *
 * Created on February 19, 2005, 8:35 PM
 */

package lircom {
/**
 *
 * @author carlsonj
 */
public class ClientException extends Exception {
    var clients:java.util.Vector;
    /** Creates a new instance of ClientException */
    public function ClientException() {
    }
    public function add(ci:Long):void {
        if (clients == null) {
             clients = new java.util.Vector();
        }
        clients.add(ci);
    }
    public function doThrow():Boolean {
        return clients != null;
    }
    public function iterator():java.util.Iterator {
        return clients.iterator();
    }
}
}