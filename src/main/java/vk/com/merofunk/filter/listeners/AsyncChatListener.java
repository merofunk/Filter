package vk.com.merofunk.filter.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import vk.com.merofunk.filter.Main;
import org.bukkit.event.Listener;

public class AsyncChatListener implements Listener
{
    Main main;
    
    public AsyncChatListener(final Main main) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)main);
        this.main = main;
    }
    
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent e) {
        final Player player = e.getPlayer();
        if (player.hasPermission("Filter.bypass")) {
            return;
        }
        if (!Main.ChatLock.containsKey(player.getUniqueId())) {
            return;
        }
        final long time = (System.currentTimeMillis() - Main.ChatLock.get(player.getUniqueId())) / 1000L;
        final int cooldown = this.main.getConfig().getInt("cooldown");
        if (time <= cooldown) {
            e.setCancelled(true);
            String cd = (cooldown - time) / 60L + 1L + "\u043c\u0438\u043d.";
            if ((cooldown - time) / 60L + 1L >= 60L) {
                cd = (cooldown - time) / 60L / 60L + "\u0447.";
            }
            if ((cooldown - time) / 60L + 1L <= 1L) {
                cd = cooldown - time + "\u0441\u0435\u043a.";
            }
            for (final String s : this.main.getConfig().getStringList("chat-lock-actions")) {
                if (s.startsWith("[MESSAGE]")) {
                    final String s2 = s.replace("[MESSAGE] ", "").replace("[MESSAGE]", "").replace("%time%", cd);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', s2));
                }
                if (s.startsWith("[TITLE]")) {
                    final String s2 = s.replace("[TITLE] ", "").replace("[TITLE]", "").replace("%time%", cd);
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
            return;
        }
        Main.ChatLock.remove(player.getUniqueId());
    }
}
