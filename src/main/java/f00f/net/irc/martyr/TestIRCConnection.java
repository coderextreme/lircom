package f00f.net.irc.martyr;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

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

