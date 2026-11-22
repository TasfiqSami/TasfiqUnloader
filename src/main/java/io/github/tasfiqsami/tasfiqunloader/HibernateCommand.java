package io.github.tasfiqsami.tasfiqunloader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * /hibernate [toggle|status|reload]. Access is controlled by the tasfiqunloader.toggle permission.
 */
final class HibernateCommand implements CommandExecutor, TabCompleter {

    private static final String PREFIX = "[TasfiqUnloader] ";
    private static final List<String> SUBCOMMANDS = Arrays.asList("toggle", "status", "reload");

    private final TasfiqUnloader plugin;

    HibernateCommand(TasfiqUnloader plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 1) {
            return false;
        }
        String sub = args.length == 0 ? "toggle" : args[0].toLowerCase(Locale.ROOT);

        switch (sub) {
            case "toggle":
                boolean nowActive = plugin.toggleActive();
                sender.sendMessage(PREFIX + "Plugin is now " + (nowActive ? "enabled" : "disabled"));
                return true;
            case "status":
                sender.sendMessage(PREFIX + "Plugin is currently " + (plugin.isActive() ? "enabled" : "disabled"));
                sender.sendMessage(PREFIX + "The server is currently " + (plugin.isSleeping() ? "sleeping" : "awake"));
                return true;
            case "reload":
                plugin.reload();
                sender.sendMessage(PREFIX + "Configuration reloaded");
                return true;
            default:
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        String typed = args[0].toLowerCase(Locale.ROOT);
        List<String> matches = new ArrayList<String>();
        for (String sub : SUBCOMMANDS) {
            if (sub.startsWith(typed)) {
                matches.add(sub);
            }
        }
        return matches;
    }
}
