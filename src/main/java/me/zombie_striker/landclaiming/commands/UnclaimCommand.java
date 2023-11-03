package me.zombie_striker.landclaiming.commands;


import me.zombie_striker.landclaiming.LandClaiming;
import me.zombie_striker.landclaiming.claimedobjects.ClaimedLand;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class UnclaimCommand implements CommandExecutor, TabCompleter {

	LandClaiming plugin;

	public UnclaimCommand(LandClaiming lc) {
		plugin = lc;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (args.length == 1) {
				if (!p.hasPermission(plugin.PERM_DEFAULT)) {
					p.sendMessage(plugin.PREFIX
							+ plugin.getMessage(plugin.PERMISSION));
				}
				for (ClaimedLand cl : plugin.claimedLand) {
					if (cl.getName().equalsIgnoreCase(args[0])) {
						if (!cl.getOwner().equals(p.getUniqueId()) && !p.hasPermission(plugin.PERM_ADMIN)) {
							p.sendMessage("You are not the owner of " + args[0]
									+ "!");
							return true;
						}
						plugin.claimedLand.remove(cl);
						p.sendMessage(plugin.PREFIX + " This the claimed land "
								+ cl.getName() + " has been unclaimed.");
						return true;
					}
				}
				p.sendMessage(plugin.PREFIX
						+ "There is no claimed land with the name" + args[0]);
				return true;
			}
		}
		sender.sendMessage(plugin.PREFIX + "Usage:");
		sender.sendMessage(plugin.PREFIX
				+ " /unclaim <regionName> // claims the region");
		return false;
	}	private void a(List<String> s, String arg, String test) {
		if (test.startsWith(arg.toLowerCase()))
			s.add(test);
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] args) {
		List<String> s = new ArrayList<>();
		if (args.length == 1) {
			for (ClaimedLand cl : plugin.claimedLand) {
				if (cl.getOwner().equals(((Player)arg0).getUniqueId())) {
					a(s, args[0], cl.getName());					
				}
			}
			return s;
		}
		return null;
	}

}
