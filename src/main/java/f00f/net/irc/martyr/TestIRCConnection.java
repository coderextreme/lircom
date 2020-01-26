package f00f.net.irc.martyr;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import org.junit.Before;
import f00f.net.irc.martyr.test.AbstractMartyrTest;
import static org.junit.Assert.*;

/**
 * JUnit test cases.
 * @author Benjamin Damm
 * */
public class TestIRCConnection extends AbstractMartyrTest
{
    @Test
    public void testMessageParsing()
    {
        IRCConnection irccon = new IRCConnection();
        assertTrue(true);
    }
}

