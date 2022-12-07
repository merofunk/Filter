package vk.com.merofunk.filter.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.Sound;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import vk.com.merofunk.filter.Main;
import org.bukkit.event.Listener;

public class CommandListener implements Listener
{
    Main main;
    
    public CommandListener(final Main main) {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)main);
        this.main = main;
    }
    
    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent e) {
        final Player player = e.getPlayer();
        if (this.main.getConfig().getBoolean("block-commands.enable") && e.getMessage().contains(":")) {
            for (final String sz : this.main.getConfig().getStringList("block-commands.actions")) {
                if (sz.startsWith("[MESSAGE]")) {
                    final String s2 = sz.replace("[MESSAGE] ", "").replace("[MESSAGE]", "");
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', s2));
                }
                if (sz.startsWith("[TITLE]")) {
                    final String s2 = sz.replace("[TITLE] ", "").replace("[TITLE]", "");
                    final String[] split = s2.split(";");
                    player.sendTitle(split[0].replace("&", "§"), split[1].replace("&", "§"), 20, 15, 20);
                }
                if (sz.startsWith("[SOUND]")) {
                    final String s2 = sz.replace("[SOUND] ", "").replace("[SOUND]", "");
                    final String[] split = s2.split(";");
                    final Sound sound = Sound.valueOf(split[0]);
                    final int volume = Integer.parseInt(split[1]);
                    final int pitch = Integer.parseInt(split[2]);
                    player.playSound(player.getLocation(), sound, (float)volume, (float)pitch);
                }
            }
        }
        for (final String s3 : this.main.getConfig().getStringList("lock-commands")) {
            if (e.getMessage().startsWith("/" + s3)) {
                e.setCancelled(true);
                if (player.hasPermission("newbiechat.bypass")) {
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
                    for (final String sz2 : this.main.getConfig().getStringList("chat-lock-actions")) {
                        if (sz2.startsWith("[MESSAGE]")) {
                            final String s4 = sz2.replace("[MESSAGE] ", "").replace("[MESSAGE]", "").replace("%time%", cd);
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s4));
                        }
                        if (sz2.startsWith("[TITLE]")) {
                            final String s4 = sz2.replace("[TITLE] ", "").replace("[TITLE]", "").replace("%time%", cd);
                            final String[] split2 = s4.split(";");
                            player.sendTitle(split2[0].replace("&", "§"), split2[1].replace("&", "§"), 20, 15, 20);
                        }
                        if (sz2.startsWith("[SOUND]")) {
                            final String s4 = sz2.replace("[SOUND] ", "").replace("[SOUND]", "");
                            final String[] split2 = s4.split(";");
                            final Sound sound2 = Sound.valueOf(split2[0]);
                            final int volume2 = Integer.parseInt(split2[1]);
                            final int pitch2 = Integer.parseInt(split2[2]);
                            player.playSound(player.getLocation(), sound2, (float)volume2, (float)pitch2);
                        }
                    }
                    return;
                }
                Main.ChatLock.remove(player.getUniqueId());
            }
        }
    }
}
