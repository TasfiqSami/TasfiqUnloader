package be.wwx.tasfiqunloader;

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
    private static final List<String> SUBCOMMANDS = List.of("toggle", "status", "reload");

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
            case "toggle" -> {
                boolean nowActive = plugin.toggleActive();
                sender.sendMessage(PREFIX + "Plugin is now " + (nowActive ? "enabled" : "disabled"));
            }
            case "status" -> {
                sender.sendMessage(PREFIX + "Plugin is currently " + (plugin.isActive() ? "enabled" : "disabled"));
                sender.sendMessage(PREFIX + "The server is currently " + (plugin.isSleeping() ? "sleeping" : "awake"));
            }
            case "reload" -> {
                plugin.reload();
                sender.sendMessage(PREFIX + "Configuration reloaded");
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length != 1) {
            return List.of();
        }
        String typed = args[0].toLowerCase(Locale.ROOT);
        return SUBCOMMANDS.stream().filter(s -> s.startsWith(typed)).toList();
    }
}
