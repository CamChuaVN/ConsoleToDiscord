package camchua.consoletodiscord.minecraft;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConsoleToDiscord extends JavaPlugin {

    private String token = "";
    private String channelId = "";

    private JDA bot;

    @Override
    public void onEnable() {
        FileManager.setup(this);
        init();
        initBot();
        initConsoleOut();
    }

    void init() {
        FileConfiguration config = FileManager.getFileConfig(FileManager.Files.CONFIG);
        token = config.getString("token");
        channelId = config.getString("channelId");
    }
    void initBot() {
        if(token.isEmpty() || channelId.isEmpty()) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        try {
            JDABuilder builder = JDABuilder.createDefault(token);
            builder.disableCache(CacheFlag.MEMBER_OVERRIDES);
            builder.enableCache(CacheFlag.VOICE_STATE);
            builder.setBulkDeleteSplittingEnabled(false);
            builder.setCompression(Compression.NONE);

            builder.addEventListeners(new ConsoleListener(channelId, this));

            bot = builder.build();
        } catch(Exception e) {
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
    }

    void initConsoleOut() {
        Logger log = (Logger) LogManager.getRootLogger();
        log.addAppender(new ConsoleReader(this));
    }


    public void sendMessage(String msg) {
        TextChannel channel = bot.getTextChannelById(channelId);
        if(channel == null) return;
        channel.sendMessage(msg).queue();
    }

}
