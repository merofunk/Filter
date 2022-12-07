package vk.com.merofunk.filter.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import vk.com.merofunk.filter.Main;
import org.bukkit.event.Listener;

public class JoinListener implements Listener
{
    Main main;
    
    public JoinListener(final Main main) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)main);
        this.main = main;
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final Player player = e.getPlayer();
        if (!player.hasPlayedBefore()) {
            Main.ChatLock.put(player.getUniqueId(), System.currentTimeMillis());
            for (final String s : this.main.getConfig().getStringList("first-join-actions")) {
                if (s.startsWith("[MESSAGE]")) {
                    final String s2 = s.replace("[MESSAGE] ", "").replace("[MESSAGE]", "");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', s2));
                }
                if (s.startsWith("[TITLE]")) {
                    final String s2 = s.replace("[TITLE] ", "").replace("[TITLE]", "");
                    final String[] split = s2.split(";");
                    player.sendTitle(split[0].replace("&", "§"), split[1].replace("&", "§"), 20, 15, 20);
                }
                if (s.startsWith("[SOUND]")) {
                    final String s2 = s.replace("[SOUND] ", "").replace("[SOUND]", "");
                    final String[] split = s2.split(";");
                    final Sound sound = Sound.valueOf(split[0]);
                    final int volume = Integer.parseInt(split[1]);
                    final int pitch = Integer.parseInt(split[2]);
                    player.playSound(player.getLocation(), sound, (float)volume, (float)pitch);
                }
            }
        }
    }
}
