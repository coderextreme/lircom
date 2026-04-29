package f00f.net.irc.martyr;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import f00f.net.irc.martyr.test.AbstractMartyrTest;

/**
 * JUnit test cases.
 * @author Benjamin Damm
 * */
public class TestIRCConnection extends AbstractMartyrTest
{
    @Test
    public void testMessageParsing()
    {
        new IRCConnection();
        assertTrue(true);
    }
}

