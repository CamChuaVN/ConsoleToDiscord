package camchua.consoletodiscord.grasscutter;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.command.CommandMap;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static emu.grasscutter.utils.Language.translate;
public class ConsoleListener extends ListenerAdapter {

    private ConsoleToDiscord plugin;
    private String channelId;

    public ConsoleListener(String channelId, ConsoleToDiscord plugin) {
        this.channelId = channelId;
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(!event.getChannel().getId().equals(channelId)) return;
        if(event.getAuthor().isBot()) return;

        final String msg = event.getMessage().getContentRaw();

        try {
            CommandMap.getInstance().invoke(null, null, msg);
        } catch(Exception e) {
            Grasscutter.getLogger().error(translate("messages.game.command_error"), e);
        }
    }

}
