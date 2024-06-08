import javax.xml.stream.*;
import javax.xml.stream.util.*;

public class XmlWrapperDecorator extends StreamReaderDelegate {
    private final XMLStreamReader reader;

    public XmlWrapperDecorator(XMLStreamReader reader) {
        this.reader = reader;
    }

    @Override
    public boolean hasNext() throws XMLStreamException {
        return reader.hasNext();
    }

    @Override
    public int next() throws XMLStreamException {
        return reader.next();
    }
}
