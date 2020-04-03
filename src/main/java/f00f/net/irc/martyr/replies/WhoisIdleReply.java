package f00f.net.irc.martyr.replies;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import f00f.net.irc.martyr.InCommand;
import f00f.net.irc.martyr.util.ParameterIterator;

public class WhoisIdleReply extends AbstractWhoisReply
{
    static Logger log = Logger.getLogger("WhoisIdleReply");

    private int idleTime;
	private Date loginTime;

	/**
	 * Factory constructor.
	 * */
	public WhoisIdleReply()
	{
	}

	public WhoisIdleReply( String params )
	{
		super( params );
	}

	public String getIrcIdentifier()
	{
		return "317";
	}

	/**
	 * @return seconds idle
	 * */
	public int getIdleTime()
	{
		return idleTime;
	}

	/** 
	 * @return login time, if provided, null otherwise
	 * */
	public Date getLoginTime()
	{
		return loginTime;
	}

	protected void parseParams( ParameterIterator pi )
	{
		String idleTimeStr = (String)pi.next(); // Idle name
		idleTime = Integer.parseInt( idleTimeStr );
		if( pi.hasNext() && ! pi.nextIsLast() )
		{
			String loginTimeStr = (String)pi.next(); // Idle description
			loginTime = new Date( Long.parseLong( loginTimeStr ) * 1000 );
		}
		log.log(Level.FINE, "WhoisIdleReply: idle: " + idleTime);
		log.log(Level.FINE, "WhoisIdleReply: login: " + loginTime);
	}

	public InCommand parse( String prefix, String identifier, String params )
	{
		return new WhoisIdleReply( params );
	}

}

