package camchua.consoletodiscord.grasscutter;

import emu.grasscutter.Grasscutter;
import emu.grasscutter.server.event.EventHandler;
import emu.grasscutter.server.event.HandlerPriority;
import emu.grasscutter.server.event.internal.ServerLogEvent;

import java.text.SimpleDateFormat;

public class ConsoleReader  {

    private ConsoleToDiscord plugin;

    public ConsoleReader(ConsoleToDiscord plugin) {
        this.plugin = plugin;

        EventHandler handler = new EventHandler<>(ServerLogEvent.class);
        handler.priority(HandlerPriority.NORMAL);
        handler.listener(event -> onEvent((ServerLogEvent) event));
        Grasscutter.getPluginManager().registerListener(handler);
    }

    public void onEvent(ServerLogEvent event) {
        String msg = event.getConsoleMessage().replaceAll("\u001B\\[[;\\d]*m", "");
        plugin.sendMessage(msg);
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

}
