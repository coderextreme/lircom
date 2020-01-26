package dammfine.justin;

import f00f.net.irc.martyr.GenericAutoService;
import f00f.net.irc.martyr.InCommand;
import f00f.net.irc.martyr.State;
import f00f.net.irc.martyr.commands.MessageCommand;
import f00f.net.irc.martyr.replies.NamesEndReply;

public class MessageMonitor extends GenericAutoService
{
	private Justin justin;

	public MessageMonitor( Justin justin )
	{
		super( justin.getConnection() );
		this.justin = justin;

		enable();
	}

	public void updateCommand( InCommand command )
	{
		if( command instanceof MessageCommand )
		{
			MessageCommand msg = (MessageCommand)command;
			justin.incomingMessage( msg );
		}
		else if( command instanceof NamesEndReply )
		{
			justin.printMembers();
		}
	}

	protected void updateState( State state )
	{
		if( state == State.UNCONNECTED && justin.shouldQuit() )
			System.exit(0);
	}
}



