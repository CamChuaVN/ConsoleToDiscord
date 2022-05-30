package camchua.consoletodiscord.minecraft;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.bukkit.ChatColor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ConsoleReader extends AbstractAppender {

    private ConsoleToDiscord plugin;

    public ConsoleReader(ConsoleToDiscord plugin) {
        super("Log4JAppender", null, PatternLayout.newBuilder().withPattern("[%d{HH:mm:ss} %level]: %msg").build(), false);
        this.plugin = plugin;
    }

    @Override
    public boolean isStarted() {
        return true;
    }

    @Override
    public void append(LogEvent e) {
        Date date = new Date(e.getTimeMillis());
        String prefix = "[" + sdf.format(date) + " " + e.getLevel().name() + "]";
        String msg = ChatColor.stripColor(e.getMessage().getFormattedMessage().replaceAll("\u007F[a-z,A-Z,0-9]", ""));
        String result = prefix + ": " + msg;
        plugin.sendMessage(result);
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

}
