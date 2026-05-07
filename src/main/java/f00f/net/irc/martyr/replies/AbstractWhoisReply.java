package f00f.net.irc.martyr.replies;

import java.util.logging.Level;
import java.util.logging.Logger;

import f00f.net.irc.martyr.util.ParameterIterator;

public abstract class AbstractWhoisReply extends GenericReply
{
    static Logger log = Logger.getLogger("AbstractWhoisReply");

    private String target = null;

	/**
	 * Factory constructor.
	 * */
	public AbstractWhoisReply()
	{
	}

	public AbstractWhoisReply( String params )
	{
		ParameterIterator<String> pi = getParams( params );
		parseParams( pi );
	}

	public abstract String getIrcIdentifier();

	/**
	 * Parse the parameters, but the target param has already been
	 * stripped off.
     *
     * @param pi Parameter iterator that will parse the parameters
	 * */
	protected abstract void parseParams( ParameterIterator<String> pi );

	/**
	 * @return the target of the whois
	 * */
	public String getTarget()
	{
		return target;
	}

	/**
	 * @param params the params string passed to "parse".
	 * @return a parameter iterator, with the whois target already
	 * stripped off.
	 * */
	protected ParameterIterator<String> getParams( String params )
	{
		ParameterIterator<String> pi = new ParameterIterator<String>(params);
		pi.next(); // throw away our own nick
		this.target = pi.next();
		log.log(Level.FINE, "AbstractWhoisReply: Whois target: " + target);

		return pi;
	}

}

