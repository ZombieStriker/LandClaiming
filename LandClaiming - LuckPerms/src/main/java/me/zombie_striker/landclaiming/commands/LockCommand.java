package me.zombie_striker.landclaiming.commands;

import me.zombie_striker.landclaiming.LandClaimer;
import me.zombie_striker.landclaiming.LandClaiming;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class LockCommand implements CommandExecutor, TabCompleter {

	private LandClaiming plugin;

	public LockCommand(LandClaiming LC) {
		this.plugin = LC;

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length <= 0) {
				if (cmd.getName().equalsIgnoreCase("lock")) {
					LandClaimer.lockingMode.put(p.getUniqueId(), 0);
					p.sendMessage(plugin.PREFIX + "Click any block to lock it.");

				} else if (cmd.getName().equalsIgnoreCase("unlock")) {
					LandClaimer.lockingMode.put(p.getUniqueId(), 1);
					p.sendMessage(plugin.PREFIX + "Click any block to unlock it.");
				}
			} else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {

				sender.sendMessage(plugin.PREFIX + "Usage:");
				sender.sendMessage(plugin.PREFIX + " /lock // locks a block");
				sender.sendMessage(plugin.PREFIX + " /unlock // unlocks a block");
				sender.sendMessage(
						plugin.PREFIX + " /lock add <player> // allows a player to interact with a locked block");
				sender.sendMessage(plugin.PREFIX
						+ " /lock remove <player> // stops a player from interacting with a locked block");
				sender.sendMessage(plugin.PREFIX + " /lock <?, help> // shows this help menu");

			} else if (args[0].equalsIgnoreCase("add")) {
				if (cmd.getName().equalsIgnoreCase("lock")) {
					if (args.length >= 2) {
						LandClaimer.lockingMode.put(p.getUniqueId(), 2);
						Player p2 = Bukkit.getPlayer(args[1]);
						if (p2 == null) {
							p.sendMessage(plugin.PREFIX + "This player does not exist");
							return false;
						}
						LandClaimer.lockingPlayer.put(p.getUniqueId(), p2.getUniqueId());
						p.sendMessage(plugin.PREFIX + "Click any block to add the player.");
					}
				}
			} else if (args[0].equalsIgnoreCase("remove")) {
				if (cmd.getName().equalsIgnoreCase("lock")) {
					if (args.length >= 2) {
						LandClaimer.lockingMode.put(p.getUniqueId(), 3);
						Player p2 = Bukkit.getPlayer(args[1]);
						if (p2 == null) {
							p.sendMessage(plugin.PREFIX + "This player does not exist");
							return false;
						}
						LandClaimer.lockingPlayer.put(p.getUniqueId(), p2.getUniqueId());
						p.sendMessage(plugin.PREFIX + "Click any block to remove the player.");
					}
				}
			}
		}
		return false;
	}

	private void a(List<String> s, String arg, String test) {
		if (test.startsWith(arg.toLowerCase()))
			s.add(test);
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] args) {
		List<String> s = new ArrayList<>();
		if(arg1.getName().equals("lock")) {
			if(args.length == 1) {
				a(s, args[0], "add");
				a(s, args[0], "remove");
				return s;
			}
		}
		return null;
	}
}
