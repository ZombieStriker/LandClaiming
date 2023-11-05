package me.zombie_striker.landclaiming.commands;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.zombie_striker.landclaiming.LandClaiming;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;

public class LandClaimCommand implements CommandExecutor, TabCompleter {

	LandClaiming plugin;

	public LandClaimCommand(LandClaiming lc) {
		this.plugin = lc;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
				if(args.length == 3){
					if (p.isOp() || p.hasPermission(plugin.PERM_ADMIN)){
						if ((args[0].equalsIgnoreCase("setGroupLimit")) && (args[1] != null) && (args[2] != null)) {
							if (plugin.getLuckPermsStatus().equals("true")){
								for(String currGroup : plugin.getConfig().getConfigurationSection(plugin.MAXCLAIMINT + ".lpgroup").getKeys(false)){
									if (args[1].equalsIgnoreCase(currGroup)) {
										String groupValue = ".lpgroup." + currGroup;
										p.sendMessage(plugin.PREFIX + ChatColor.WHITE +"Setting Group limit for " 
												+ ChatColor.RED + currGroup + ChatColor.WHITE + " to " 
												+ ChatColor.GREEN + args[2]);
										plugin.getConfig().set(plugin.MAXCLAIMINT + groupValue, Integer.parseInt(args[2]));
										plugin.save();
										return true;
									}
								}
							}
							else {
								for(String currGroup : plugin.getConfig().getConfigurationSection(plugin.MAXCLAIMINT + ".group").getKeys(false)){
									if (args[1].equalsIgnoreCase(currGroup)) {
										String groupValue = ".group." + currGroup;
										p.sendMessage(plugin.PREFIX + ChatColor.WHITE +"Setting Group limit for " 
												+ ChatColor.RED + currGroup + ChatColor.WHITE + " to " 
												+ ChatColor.GREEN + args[2]);
										plugin.getConfig().set(plugin.MAXCLAIMINT + groupValue, Integer.parseInt(args[2]));
										plugin.save();
										return true;
									}
								}
							}
						}
						else {
								p.sendMessage(plugin.PREFIX + "Something went wrong. Please ensure all arguments are present. Use /landClaim help for more info");
								return false;
						}
					}
					else
						p.sendMessage(plugin.PREFIX + "Insufficient Permissons. Please verify your access and try again.");
						return true;
					}
			else if(args.length == 2){
				if (p.isOp() || p.hasPermission(plugin.PERM_ADMIN)){
					if ((args[0].equalsIgnoreCase("getGroupLimit")) && (args[1] != null)) {
						if (plugin.getLuckPermsStatus().equals("true")) {
							for(String currGroup : plugin.getConfig().getConfigurationSection(plugin.MAXCLAIMINT + ".lpgroup").getKeys(false)) {
								if (args[1].equalsIgnoreCase(currGroup)) {
									String groupValue = ".lpgroup." + currGroup;
									int setLimit = plugin.getConfig().getInt(plugin.MAXCLAIMINT + groupValue);
									p.sendMessage(plugin.PREFIX + ChatColor.WHITE +"Group limit for " 
											+ ChatColor.RED + currGroup + ChatColor.WHITE + " is currently set to " 
											+ ChatColor.GREEN + setLimit);
									return true;
								}
							}
						}
						else {
							for(String currGroup : plugin.getConfig().getConfigurationSection(plugin.MAXCLAIMINT + ".group").getKeys(false)) {
								if (args[1].equalsIgnoreCase(currGroup)) {
									String groupValue = ".group." + currGroup;
									int setLimit = plugin.getConfig().getInt(plugin.MAXCLAIMINT + groupValue);
									p.sendMessage(plugin.PREFIX + ChatColor.WHITE +"Group limit for " 
											+ ChatColor.RED + currGroup + ChatColor.WHITE + " is currently set to " 
											+ ChatColor.GREEN + setLimit);
									return true;
								}
							}
						}
					}
					else {
							p.sendMessage(plugin.PREFIX + "Something went wrong. Please ensure all arguments are present. Use /landClaim help for more info");
							return false;
					}
				}
				else
					p.sendMessage(plugin.PREFIX + "Insufficient Permissons. Please verify your access and try again.");
					return true;
				}
		}
		sendMessage(sender);
		return false;
	}

	public void sendMessage(CommandSender sender) {
		sender.sendMessage(plugin.PREFIX + ChatColor.WHITE + "Usage:");
		sender.sendMessage(plugin.PREFIX + " /claim <?, help>" + ChatColor.WHITE + " // shows help related to claiming land process");
		sender.sendMessage(plugin.PREFIX + " /unclaim <?, help>" + ChatColor.WHITE + " // shows help related to unclaiming land process");
		sender.sendMessage(plugin.PREFIX + " /landClaim setGroupLimit <groupame> <limit>" + ChatColor.WHITE 
				+" // Updates maximum amount of blocks a group can claim");
		sender.sendMessage(plugin.PREFIX + " /landClaim GroupLimit <groupame>" + ChatColor.WHITE 
				+" // Shows current limit for a selected group");
		sender.sendMessage(plugin.PREFIX + " /landClaim help" + ChatColor.WHITE + " // shows this help menu");
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
			a(s, args[0], "getGroupLimit");
			a(s, args[0], "setGroupLimit");
			return s;
		}
		if (args.length == 2) {
			if (plugin.getLuckPermsStatus().equals("true")) {
				LuckPerms api = LuckPermsProvider.get();
				api.getGroupManager().loadAllGroups();
				Set<Group> groupList = api.getGroupManager().getLoadedGroups();
				if (groupList == null) {
					a(s, args[1], "groupName");
					return s;
				}
				else {
					for (Group currGroup : groupList)
						a(s, args[1], currGroup.getName());
					return s;
				}
			}
			else {
				//improvement needed here to read non LP groups from config.
				a(s, args[1], "groupName");
				return s;
			}
		}	
		if (args.length == 3) {
			a(s, args[1], "<limit>");
			return s;
		}
		return null;
	}
}