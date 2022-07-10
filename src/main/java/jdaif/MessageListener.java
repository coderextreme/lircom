package jdaif;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.GatewayIntent;
import javax.security.auth.login.LoginException;
import java.util.List;

public class MessageListener extends ListenerAdapter
{
	public static JDA jda;
    public static void main(String[] args)
	    throws LoginException
    {
        jda = JDABuilder.createDefault(args[0])
            .addEventListeners(new MessageListener())
	    .build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
	System.err.println("message received");
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
	System.err.println("member joined");
    }
}
