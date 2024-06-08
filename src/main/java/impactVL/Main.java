import java.io.*;
import javax.xml.stream.*;

public class Main {
    public static void main(String[] args) throws XMLStreamException {
	    XMLInputFactory inFactory = XMLInputFactory.newInstance();
	    try {
		// fIS is only visible within this try{} block
		FileInputStream fIS = new FileInputStream("blenderExport.xml");
		XMLStreamReader xmlReader = inFactory.createXMLStreamReader(fIS);
		XmlWrapperDecorator wr = new XmlWrapperDecorator(xmlReader);
		while (wr.hasNext()) {
		    int eventType = wr.next();
		    System.out.println(eventType);
		}
	    } catch (Exception e) {
		    e.printStackTrace();
	    }
    }
}
