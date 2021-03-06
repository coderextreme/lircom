package f00f.net.irc.martyr.replies;

import java.util.logging.Level;
import java.util.logging.Logger;

import f00f.net.irc.martyr.InCommand;
import f00f.net.irc.martyr.util.ParameterIterator;

public class WhoisServerReply extends AbstractWhoisReply
{
    static Logger log = Logger.getLogger("WhoisServerReply");

    private String serverName;
	private String serverDesc;

	/**
	 * Factory constructor.
	 * */
	public WhoisServerReply()
	{
	}

	public WhoisServerReply( String params )
	{
		super( params );
	}

	public String getIrcIdentifier()
	{
		return "312";
	}

	/**
	 * @return the DNS name of the server
	 * */
	public String getServerName()
	{
		return serverName;
	}

	/** 
	 * @return the free-form description of the server
	 * */
	public String getServerDescription()
	{
		return serverDesc;
	}

	protected void parseParams( ParameterIterator pi )
	{
		serverName = (String)pi.next(); // Server name
		serverDesc = (String)pi.next(); // Server description
		log.log(Level.FINE, "WhoisServerReply: server name: " + serverName);
		log.log(Level.FINE, "WhoisServerReply: server desc: " + serverDesc);
	}

	public InCommand parse( String prefix, String identifier, String params )
	{
		return new WhoisServerReply( params );
	}

}

