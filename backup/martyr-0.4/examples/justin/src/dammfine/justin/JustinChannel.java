package dammfine.justin;

import org.apache.log4j.Logger;

import f00f.net.irc.martyr.InCommand;
import f00f.net.irc.martyr.Mode;
import f00f.net.irc.martyr.clientstate.Channel;
import f00f.net.irc.martyr.replies.NamesReply;

public class JustinChannel extends Channel
{

private Justin justin;
private static Logger log = Logger.getLogger(JustinChannel.class);

public JustinChannel( String chanName, Justin justin )
{
	super( chanName );

	this.justin = justin;
}

/**
 * We override the cononical version of addMember.
 */
public void addMember( String name, InCommand why )
{
	super.addMember( name, why );

	if( ! (why instanceof NamesReply) )
		justin.printMembers();
}

/**
 * We override the cononical version of removeMember.
 */
public void removeMember( String name, InCommand why )
{
	super.removeMember( name, why );
	justin.printMembers();
}

public void setMode( Mode mode )
{
	super.setMode( mode );
	
	log.debug( "Mode: " + mode );
}

public String toString()
{
	return "JustinChannel";
}


}



