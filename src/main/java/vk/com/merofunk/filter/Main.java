package vk.com.merofunk.filter;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import vk.com.merofunk.filter.listeners.JoinListener;
import vk.com.merofunk.filter.listeners.CommandListener;
import vk.com.merofunk.filter.commands.ReloadCommand;
import vk.com.merofunk.filter.listeners.AsyncChatListener;
import java.util.UUID;
import java.util.HashMap;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin
{
    public static HashMap<UUID, Long> ChatLock;
    
    public void onEnable() {
        this.saveDefaultConfig();
        new AsyncChatListener(this);
        new ReloadCommand(this);
        new CommandListener(this);
        new JoinListener(this);
        this.msg("&r");
        this.msg("&6Filter &ePlugin Check Launched");
        this.msg("&aVerification completed, enjoy!");
        this.msg("&r");
        Data.load();
        this.saveData((Plugin)this);
        Bukkit.getScheduler().scheduleSyncRepeatingTask((Plugin)this, Data::save, 200L, 100L);
    }
    
    public void onDisable() {
        Data.save();
    }
    
    private void saveData(final Plugin plugin) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, Data::save, 1200L, 1200L);
    }
    
    private void msg(final String msg) {
        final String p = ChatColor.translateAlternateColorCodes('&', "&e" + this.getName() + " | ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', p + msg));
    }
    
    static {
        Main.ChatLock = new HashMap<UUID, Long>();
    }
}
