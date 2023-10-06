
package be.wwx.tasfiqunloader;

import org.bukkit.Chunk;
import java.io.IOException;
import java.util.Scanner;
import java.net.URL;
import java.util.Collection;
import java.util.Set;
import java.util.Iterator;
import org.bukkit.Server;
import org.bukkit.World;
import java.util.HashSet;
import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener
{
    private FileConfiguration a;
    private static boolean b;
    
    public Main() {
        this.a = this.getConfig();
    }
    
    public void onDisable() {
        try {
            Bukkit.getScheduler().cancelTasks((Plugin)this);
        }
        catch (Throwable t) {}
    }
    
    public boolean onCommand(final CommandSender commandSender, final Command command, final String s, final String[] array) {
        if (command.getName().equalsIgnoreCase("TasfiqUnloader") && (commandSender.isOp() || commandSender.hasPermission("tasfiqunloader.toggle"))) {
            if (array.length != 1) {
                be.wwx.tasfiqunloader.a.a = !be.wwx.tasfiqunloader.a.a;
                commandSender.sendMessage("[TasfiqUnloader] Plugin is now " + (be.wwx.tasfiqunloader.a.a ? "enabled" : "disabled"));
                this.saveConfig();
                return true;
            }
            if (array[0].equalsIgnoreCase("reload")) {
                this.reloadConfig();
                return true;
            }
            if (array[0].equalsIgnoreCase("status")) {
                commandSender.sendMessage(String.format("[TasfiqUnloader] Plugin is currently %s", be.wwx.tasfiqunloader.a.a ? "enabled" : "disabled"));
                commandSender.sendMessage(String.format("[TasfiqUnloader] The server is currently %s", (this.getServer().getOnlinePlayers().size() == 0 && Main.b) ? "sleeping" : "awake"));
                return true;
            }
        }
        return false;
    }
    
    public void onEnable() {
        this.saveDefaultConfig();
        this.a.addDefault("enabled", (Object)Boolean.TRUE);
        this.a.addDefault("unloadChunks", (Object)Boolean.TRUE);
        this.a.addDefault("sleepMillis", (Object)1000L);
        this.a.addDefault("blacklist", (Object)new ArrayList());
        this.a.options().copyDefaults(true);
        be.wwx.tasfiqunloader.a.a = this.a.getBoolean("enabled");
        be.wwx.tasfiqunloader.a.b = this.a.getBoolean("unloadChunks");
        be.wwx.tasfiqunloader.a.c = this.a.getLong("sleepMillis");
        be.wwx.tasfiqunloader.a.d = this.a(this.a);
        this.saveConfig();
        final HashSet<String> set = new HashSet<String>();
        Plugin[] plugins;
        for (int length = (plugins = this.getServer().getPluginManager().getPlugins()).length, i = 0; i < length; ++i) {
            final Plugin plugin = plugins[i];
            if (be.wwx.tasfiqunloader.a.d.contains(plugin.getName().toLowerCase())) {
                set.add(plugin.getName());
            }
        }
        final HashSet<String> elements;
        if ((elements = set).size() == 0) {
            final Object o;
            final int n;
            final Server server;
            final Iterator<World> iterator;
            World world;
            final int e;
            Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this, () -> {
                Bukkit.getServer();
                ((Server)o).getOnlinePlayers().size();
                if (n == 0 && be.wwx.tasfiqunloader.a.a) {
                    if (be.wwx.tasfiqunloader.a.e != 0) {
                        this.getLogger().info("Saving worlds before Hibernation of Tasfiq Unloader");
                        server.getWorlds().iterator();
                        while (iterator.hasNext()) {
                            world = iterator.next();
                            world.save();
                        }
                        this.getLogger().info("Worlds saved!");
                        be.wwx.tasfiqunloader.a.e = 0;
                        return;
                    }
                    else {
                        try {
                            Thread.sleep(be.wwx.tasfiqunloader.a.c);
                            this.a();
                            return;
                        }
                        catch (Exception ex) {
                            return;
                        }
                    }
                }
                be.wwx.tasfiqunloader.a.e = e;
                return;
            }, 0L, 1L);
            Main.b = true;
        }
        else {
            this.getLogger().warning("Blacklisted plugins were found (plugins that will stop working if TasfiqUnloader is enabled). Standard hibernation will be disabled, but TasfiqUnloader will still try to unload chunks. (check the Hibernate config and edit the blacklist if you want to force enable TasfiqUnloader)");
            this.getLogger().warning("Blacklisted plugins: " + String.join(", ", elements));
        }
        if (be.wwx.tasfiqunloader.a.b) {
            this.a();
        }
        this.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this);
    }
    
    private Set<String> a(FileConfiguration iterator) {
        final HashSet set = new HashSet(iterator.getStringList("blacklist"));
        final HashSet<String> set2 = new HashSet<String>();
        iterator = (FileConfiguration)set.iterator();
        while (((Iterator)iterator).hasNext()) {
            final String s;
            if ((s = ((Iterator<String>)iterator).next()).startsWith("http")) {
                try {
                    final Scanner scanner = new Scanner(new URL(s).openStream(), "UTF-8");
                    while (scanner.hasNextLine()) {
                        final String nextLine;
                        if (!(nextLine = scanner.nextLine()).equals("") && !nextLine.startsWith("#")) {
                            set2.add(nextLine.toLowerCase());
                        }
                    }
                }
                catch (IOException ex) {
                    this.getLogger().severe("Failed to get blacklist from URL: " + s);
                }
            }
            else {
                set2.add(s.toLowerCase());
            }
        }
        return set2;
    }
    
    private void a() {
        int i = 0;
        final Iterator<World> iterator = (Iterator<World>)Bukkit.getWorlds().iterator();
        while (iterator.hasNext()) {
            Chunk[] loadedChunks;
            for (int length = (loadedChunks = iterator.next().getLoadedChunks()).length, j = 0; j < length; ++j) {
                if (loadedChunks[j].unload(true)) {
                    ++i;
                }
            }
        }
        if (i > 0) {
            this.getLogger().info(String.format("Unloaded %d chunks", i));
            this.b();
        }
    }
    
    private void b() {
        final long freeMemory = Runtime.getRuntime().freeMemory();
        System.gc();
        final long l;
        if ((l = (Runtime.getRuntime().freeMemory() - freeMemory) / 1024L / 1024L) > 0L) {
            this.getLogger().info(String.format("%d MB memory freed using Java garbage collector", l));
        }
    }
    
    static {
        Main.b = false;
    }
}
