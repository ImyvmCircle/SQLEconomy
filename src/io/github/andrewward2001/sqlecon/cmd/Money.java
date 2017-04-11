package io.github.andrewward2001.sqlecon.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.andrewward2001.sqlecon.SQLEconomy;
import io.github.andrewward2001.sqlecon.SQLEconomyActions;

public class Money implements CommandExecutor {

    private final SQLEconomy plugin;

    private String moneyUnit;

    public Money(SQLEconomy plugin) {
        this.plugin = plugin;

        moneyUnit = SQLEconomy.moneyUnit;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("money") || cmd.getName().equalsIgnoreCase("m")) {
            if (args.length == 0 && sender.hasPermission("sqleconomy.money")) {
                Player player;
                if (sender instanceof Player) {
                    player = (Player) sender;
                    int money = SQLEconomyActions.getMoney(player.getUniqueId());

                    sender.sendMessage(ChatColor.GREEN + "Money: " + ChatColor.WHITE + money + " " + moneyUnit);

                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED + "You must be a player to do that!");

                    return false;
                }

            } else if (args.length == 1) {

                if (args[0].equalsIgnoreCase("give")) {
                    if (sender.hasPermission("sqleconomy.money.give"))
                        sender.sendMessage(ChatColor.RED
                                + "Incorrect Usage! /money give [player] <amount> --> player is optional.");
                    else
                        sender.sendMessage(ChatColor.RED + "You don't have permission to give others money!");

                    return false;
                } else if (args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")) {
                    if(sender.hasPermission("sqleconomy.money.remove"))
                        sender.sendMessage(
                                ChatColor.RED + "Incorrect Usage! /money remove [player] <amount> --> player is optional.");
                    else
                        sender.sendMessage(ChatColor.RED + "You don't have permission to take money!");

                    return false;
                } else if (args[0].equalsIgnoreCase("transfer")) {
                    if (sender.hasPermission("sqleconomy.money.transfer"))
                        sender.sendMessage(ChatColor.RED
                                + "Incorrect Usage! /money transfer <player> <amount>");
                    else
                        sender.sendMessage(ChatColor.RED + "You don't have permission to transfer money!");

                } else {
                    if (sender.hasPermission("sqleconomy.money.seeothers")) {
                        int money = SQLEconomyActions.getMoney(args[0]);

                        if (money != -1)
                            sender.sendMessage(ChatColor.GREEN + "Money: " + ChatColor.WHITE + money + " " + moneyUnit);
                        else
                            sender.sendMessage(
                                    ChatColor.DARK_RED + "Error: Couldn't find user " + ChatColor.ITALIC + args[0]);

                        return true;
                    }

                    return false;
                }
            } else if (args.length == 2) {
                Player player;
                if (sender instanceof Player) {
                    player = (Player) sender;
                    if (args[0].equalsIgnoreCase("give")) {
                        if (sender.hasPermission("sqleconomy.money.give"))
                            if (plugin.isInteger(args[1])) {
                                SQLEconomyActions.giveMoney(player.getUniqueId(), Integer.parseInt(args[1]), false);
                                sender.sendMessage("Gave " + ChatColor.GREEN + args[1] + ChatColor.WHITE + " " + moneyUnit
                                        + " to yourself. Congratulations.");

                                return true;
                            } else {
                                sender.sendMessage("Amount is not a number!");
                            }
                        else
                            sender.sendMessage(ChatColor.RED + "You don't have permission to give others money!");

                        return false;
                    } else if (args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")) {
                        if (sender.hasPermission("sqleconomy.money.remove"))
                            if (plugin.isInteger(args[1])) {
                                SQLEconomyActions.removeMoney(player.getUniqueId(), Integer.parseInt(args[1]), false);
                                sender.sendMessage("Removed " + ChatColor.RED + args[1] + ChatColor.WHITE + " " + moneyUnit
                                        + " from yourself.");

                                return true;
                            } else {
                                sender.sendMessage("Amount is not a number!");
                            }
                        else
                            sender.sendMessage(ChatColor.RED + "You don't have permission to take money!");

                        return false;
                    } else if (args[0].equalsIgnoreCase("transfer")) {
                        if (sender.hasPermission("sqleconomy.money.transfer"))
                            sender.sendMessage(ChatColor.RED
                                    + "Incorrect Usage! /money transfer <player> <amount>");
                        else
                            sender.sendMessage(ChatColor.RED + "You don't have permission to transfer money!");

                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "You must be a player to do that!");

                    return false;
                }
            } else if (args.length == 3) {
                if (sender instanceof Player) {
                    if (args[0].equalsIgnoreCase("give")) {
                        if (sender.hasPermission("sqleconomy.money.give"))
                            if (plugin.isInteger(args[2])) {
                                SQLEconomyActions.giveMoney(args[1], Integer.parseInt(args[2]), false);
                                sender.sendMessage("Gave " + ChatColor.GREEN + args[2] + ChatColor.WHITE + " " + moneyUnit
                                        + " to " + args[1] + ".");

                                return true;
                            } else {
                                sender.sendMessage("Amount is not a number!");
                            }
                        else
                            sender.sendMessage(ChatColor.RED + "You don't have permission to give others money!");

                        return false;
                    } else if (args[0].equalsIgnoreCase("take") || args[0].equalsIgnoreCase("remove")) {
                        if (sender.hasPermission("sqleconomy.money.remove"))
                            if (plugin.isInteger(args[2])) {
                                SQLEconomyActions.removeMoney(args[1], Integer.parseInt(args[2]), false);
                                sender.sendMessage("Removed " + ChatColor.RED + args[2] + ChatColor.WHITE + " " + moneyUnit
                                        + " from " + args[1] + ".");

                                return true;
                            } else {
                                sender.sendMessage("Amount is not a number!");
                            }
                        else
                            sender.sendMessage(ChatColor.RED + "You don't have permission to take money!");

                        return false;
                    } else if (args[0].equalsIgnoreCase("transfer")) {
                        if (sender.hasPermission("sqleconomy.money.transfer")) {
                            if (SQLEconomyActions.transferMoney(args[1], ((Player) sender).getUniqueId(), Integer.parseInt(args[2]))) {
                                sender.sendMessage(ChatColor.GREEN
                                        + "Successfully transferred " + args[2] + " " + moneyUnit + " to " + args[1]);

                                sender.sendMessage(ChatColor.GREEN + "Money: " + ChatColor.WHITE + SQLEconomyActions.getMoney(((Player) sender).getUniqueId()) + " " + moneyUnit);
                            } else
                                sender.sendMessage(ChatColor.RED + "Error transferring money! Do you have enough?");
                        } else
                            sender.sendMessage(ChatColor.RED + "You don't have permission to transfer money!");

                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "You must be a player to do that!");

                    return false;
                }
            }
        }

        return false;
    }

}
