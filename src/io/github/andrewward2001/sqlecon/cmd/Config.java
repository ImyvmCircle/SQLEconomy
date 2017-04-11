package io.github.andrewward2001.sqlecon.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import io.github.andrewward2001.sqlecon.SQLEconomy;

public class Config implements CommandExecutor {

    private final SQLEconomy plugin;

    private final FileConfiguration config;

    public Config(SQLEconomy plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender.hasPermission("sqleconomy.config"))
            if (args.length == 2) {
                if(cmd.getName().equalsIgnoreCase("sqle-config")) {
                    if(args[0].equalsIgnoreCase("DatabasePort") || args[0].equalsIgnoreCase("TaxRate")) {
                        config.set(args[0], Integer.parseInt(args[1]));
                    } else {
                        config.set(args[0], args[1]);
                    }
                    plugin.saveConfig();
                    sender.sendMessage(ChatColor.GREEN + "[SQLEconomy-Config] Successfully set config value " + args[0] + " to " + args[1]);
                    return true;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "[SQLEconomy] Too many or too few args!");
                return false;
            }
        else
            sender.sendMessage("You don't have permission to change the SQLEconomy configuration!");

        return false;
    }

}
