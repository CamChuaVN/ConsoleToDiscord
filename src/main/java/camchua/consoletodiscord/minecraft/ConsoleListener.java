package camchua.consoletodiscord.minecraft;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

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

        new BukkitRunnable() {
            public void run() {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), msg);
            }
        }.runTaskLater(plugin , 0);
    }

}
