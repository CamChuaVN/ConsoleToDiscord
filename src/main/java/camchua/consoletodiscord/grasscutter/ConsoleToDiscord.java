package camchua.consoletodiscord.grasscutter;

import ch.qos.logback.classic.Logger;
import emu.grasscutter.Grasscutter;
import emu.grasscutter.plugin.Plugin;
import emu.grasscutter.server.event.EventHandler;
import emu.grasscutter.server.event.HandlerPriority;
import emu.grasscutter.server.event.internal.ServerLogEvent;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.stream.Collectors;

public class ConsoleToDiscord extends Plugin {


    private JDA bot;
    private PluginConfig pluginConfig;
    private String token;
    private String channelId;

    @Override
    public void onLoad() {
        init();
    }

    @Override
    public void onEnable() {
        initBot();
        initConsoleOut();
    }

    void init() {
        File config = new File(this.getDataFolder(), "config.json");
        try {
            if(!config.exists()) {
                if(!config.exists() && !config.createNewFile()) {

                } else {
                    try(FileWriter writer = new FileWriter(config)) {
                        InputStream configStream = this.getResource("config.json");
                        if(configStream == null) {

                        } else {
                            writer.write(new BufferedReader(
                                    new InputStreamReader(configStream)).lines().collect(Collectors.joining("\n"))
                            ); writer.close();
                        }
                    }
                }
            }

            this.pluginConfig = Grasscutter.getGsonFactory().fromJson(new FileReader(config), PluginConfig.class);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        token = this.pluginConfig.token;
        channelId = this.pluginConfig.channelId;
    }
    void initBot() {
        if(token.isEmpty() || channelId.isEmpty()) {
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
            return;
        }
    }

    void initConsoleOut() {
        new ConsoleReader(this);
    }


    public void sendMessage(String msg) {
        TextChannel channel = bot.getTextChannelById(channelId);
        if(channel == null) return;
        channel.sendMessage(msg).queue();
    }

}
