package me.zombie_striker.landclaiming.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.zombie_striker.landclaiming.LandClaimer;
import me.zombie_striker.landclaiming.LandClaiming;
import me.zombie_striker.landclaiming.claimedobjects.ClaimedLand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class ClaimCommand implements CommandExecutor, TabCompleter {

	LandClaiming plugin;

	public ClaimCommand(LandClaiming lc) {
		this.plugin = lc;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length >= 1) {
				if (args[0].equalsIgnoreCase("info")) {
					if (args.length >= 2) {
						for (ClaimedLand cl : plugin.claimedLand) {
							if (cl.getName().equalsIgnoreCase(args[1])) {
								p.sendMessage(plugin.PREFIX + " Showing stats for land :\" " + cl.getName() + "\".");
								p.sendMessage(
										plugin.PREFIX + " Owner : " + Bukkit.getOfflinePlayer(cl.getOwner()).getName());
								String guests = "";
								for (UUID uuid : cl.getGuests()) {
									guests += (Bukkit.getOfflinePlayer(uuid).getName()) + ", ";
								}
								p.sendMessage(plugin.PREFIX + " Guests : " + guests);
								p.sendMessage(plugin.PREFIX + " Size : "
										+ (cl.getMaxLoc().getBlockX() - cl.getMinLoc().getBlockX() + 1) + " x "
										+ (cl.getMaxLoc().getBlockZ() - cl.getMinLoc().getBlockZ() + 1));
								return true;
							}
						}
					} else {
						sender.sendMessage(plugin.PREFIX + " Names of all the claimed lands");
						for (ClaimedLand cl : plugin.claimedLand) {
							if (cl.getName().length() != 0) {
								p.sendMessage("-" + cl.getName() + "  --owner: "
										+ Bukkit.getOfflinePlayer(cl.getOwner()).getName());
							}
						}
						return true;
					}
				} else if (args[0].equalsIgnoreCase("myclaims")) {

					sender.sendMessage(plugin.PREFIX + " Names of all the claimed lands owned by you:");
					for (ClaimedLand cl : plugin.claimedLand) {
						if (cl.getOwner().equals(p.getUniqueId()))
							if (cl.getName().length() != 0) {
								p.sendMessage("-" + cl.getName());
							}
					}
					return true;

				} else if (args[0].equalsIgnoreCase("?") || args[0].equalsIgnoreCase("help")) {
					sendMessage(sender);
					return true;
				} else {
					if (args.length == 1) {
						if (!p.hasPermission(plugin.PERM_DEFAULT)) {
							p.sendMessage(plugin.PREFIX + plugin.getMessage(plugin.PERMISSION));
							return true;
						}
						for (ClaimedLand cl : plugin.claimedLand) {
							if (cl.getName().equalsIgnoreCase(args[0])) {
								p.sendMessage(plugin.PREFIX + " This name has already been taken.");
								return true;
							}
						}
						LandClaimer.claimingMode.put(p.getUniqueId(), args[0]);
						p.sendMessage(plugin.PREFIX + "Click any block to claim a corner.");
						return true;
					}
					if (args.length == 3) {
						for (ClaimedLand cl : plugin.claimedLand) {
							if (cl.getName().equalsIgnoreCase(args[0]) && (cl.getOwner().equals(p.getUniqueId())
									|| p.isOp() || p.hasPermission(plugin.PERM_ADMIN))) {
								@SuppressWarnings("deprecation")
								OfflinePlayer p2 = Bukkit.getOfflinePlayer(args[2]);
								if (p2 == null) {
									p.sendMessage(plugin.PREFIX + " That player does not exist.");
									return true;
								}
								if (args[1].equalsIgnoreCase("add")) {
									cl.getGuests().add(p2.getUniqueId());
									p.sendMessage(plugin.PREFIX + " Added player.");
								} else if (args[1].equalsIgnoreCase("remove")) {
									cl.getGuests().remove(p2.getUniqueId());
									p.sendMessage(plugin.PREFIX + " Removed player.");
								}
								return true;
							}
						}
					}
				}
			}
			sendMessage(sender);
		}
		return false;
	}

	public void sendMessage(CommandSender sender) {

		sender.sendMessage(plugin.PREFIX + ChatColor.WHITE + "Usage:");
		sender.sendMessage(plugin.PREFIX + " /claim <regionName> " + ChatColor.WHITE
				+ "// lets the player claim land. Claimed land claims all blocks in the Y direction above and below the specified area");
		sender.sendMessage(
				plugin.PREFIX + " /claim myclaims " + ChatColor.WHITE + "// shows all claimed land that you own");
		sender.sendMessage(
				plugin.PREFIX + " /claim info <regionName> " + ChatColor.WHITE + "//shows stats for that region");
		sender.sendMessage(plugin.PREFIX + " /claim info  " + ChatColor.WHITE + "//shows stats for all region");
		sender.sendMessage(plugin.PREFIX + " /claim <regionname> <add/remove> <plasyername>" + ChatColor.WHITE
				+ " // adds a player to the region");
		sender.sendMessage(plugin.PREFIX + " /claim <?, help>" + ChatColor.WHITE + " // shows this help menu");
	}

	private void a(List<String> s, String arg, String test) {
		if (test.startsWith(arg.toLowerCase()))
			s.add(test);
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] args) {
		List<String> s = new ArrayList<>();
		if (args.length == 1) {
			a(s, args[0], "help");
			a(s, args[0], "myclaims");
			a(s, args[0], "info");
			a(s, args[0], "myclaims");
			for (ClaimedLand cl : plugin.claimedLand) {
				if (cl.getOwner().equals(((Player)arg0).getUniqueId())) {
					a(s, args[0], cl.getName());					
				}
			}
			return s;
		}
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("info")) {
				for (ClaimedLand cl : plugin.claimedLand) {
					if (cl.getOwner().equals(((Player)arg0).getUniqueId())) {
						a(s, args[0], cl.getName());					
					}
				}
				return s;				
			}else {
				for (ClaimedLand cl : plugin.claimedLand) {
					if(args[0].equalsIgnoreCase(cl.getName())) {
						a(s, args[0], "add");
						a(s, args[0], "remove");
						return s;
					}
				}				
			}
		}
		
		return null;
	}
}
