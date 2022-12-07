package vk.com.merofunk.filter.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import vk.com.merofunk.filter.Main;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;

public class ReloadCommand implements CommandExecutor, TabCompleter
{
    Main main;
    
    public ReloadCommand(final Main main) {
        main.getCommand("Filter").setExecutor((CommandExecutor)this);
        main.getCommand("Filter").setTabCompleter((TabCompleter)this);
        this.main = main;
    }
    
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("Filter.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', this.main.getConfig().getString("commands.no-perm")));
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "\u041d\u0435\u0434\u043e\u0441\u0442\u0430\u0442\u043e\u0447\u043d\u043e \u0430\u0440\u0433\u0443\u043c\u0435\u043d\u0442\u043e\u0432.");
            return true;
        }
        if (args[0].equalsIgnoreCase("reload")) {
            this.main.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "\u041a\u043e\u043d\u0444\u0438\u0433\u0443\u0440\u0430\u0446\u0438\u044f \u043f\u0435\u0440\u0435\u0437\u0430\u0433\u0440\u0443\u0436\u0435\u043d\u0430.");
        }
        return true;
    }
    
    public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias, final String[] args) {
        final List<String> list = new ArrayList<String>();
        if (args.length == 1) {
            list.add("reload");
            return list;
        }
        return null;
    }
}
