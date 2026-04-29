package f00f.net.irc.martyr.dcc;

public class UnauthorizedDccException extends DccException
{
	private static final long serialVersionUID = 1L;
	public UnauthorizedDccException( String msg )
	{
		super(msg);
	}
}


