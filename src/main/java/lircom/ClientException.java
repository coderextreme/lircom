/*
 * ClientException.java
 *
 * Created on February 19, 2005, 8:35 PM
 */

package lircom;

/**
 *
 * @author carlsonj
 */
public class ClientException extends Exception {
    java.util.ArrayList clients;
    /** Creates a new instance of ClientException */
    public ClientException() {
    }
    public void add(Long ci) {
        if (clients == null) {
             clients = new java.util.ArrayList();
        }
        clients.add(ci);
    }
    public boolean doThrow() {
        return clients != null;
    }
    public java.util.Iterator iterator() {
        return clients.iterator();
    }
}
