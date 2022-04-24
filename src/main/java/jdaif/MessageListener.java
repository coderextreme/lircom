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
        jda = JDABuilder.createDefault(args[0], GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_EMOJIS)
            .addEventListeners(new MessageListener())
            .setActivity(Activity.playing("Type !ping"))
	    .build();
        //You can also add event listeners to the already built JDA instance
        // Note that some events may not be received if the listener is added after calling build()
        // This includes events such as the ReadyEvent
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
	MessageListener.jda.getPresence().setStatus(OnlineStatus.ONLINE);
	TextChannel textChannel = event.getGuild().getTextChannelsByName("lircom-language-independent-relay-communications",true).get(0);
	textChannel.sendMessage("message received").queue();
        Message msg = event.getMessage();
        if (msg.getContentRaw().equals("!ping"))
        {
            MessageChannel channel = event.getChannel();
            long time = System.currentTimeMillis();
            channel.sendMessage("Pong!") /* => RestAction<Message> */
                   .queue(response /* => Message */ -> {
                       response.editMessageFormat("Pong: %d ms", System.currentTimeMillis() - time).queue();
                   });
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
	MessageListener.jda.getPresence().setStatus(OnlineStatus.ONLINE);
        Guild guild = event.getGuild(); // Get the guild that the user joined.
        User user = event.getUser();    // Get the user that joined.
        JDA client = event.getJDA();    // Get the already existing JDA instance.

	TextChannel textChannel = guild.getTextChannelsByName("lircom-language-independent-relay-communications",true).get(0);
	textChannel.sendMessage("member joined").queue();

        List<TextChannel> channels = guild.getTextChannelsByName("lircom-language-independent-relay-communications", true); // Get the list of channels in the guild that matches that name.

        for (TextChannel channel : channels) { // Loops through the channels and sends a message to each one.
            channel.sendMessage("New member joined: " + user).queue();
        }
    }
}
