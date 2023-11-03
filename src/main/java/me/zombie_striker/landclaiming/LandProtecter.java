package me.zombie_striker.landclaiming;

import me.zombie_striker.landclaiming.claimedobjects.ClaimedBlock;
import me.zombie_striker.landclaiming.claimedobjects.ClaimedLand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class LandProtecter implements Listener {

	private LandClaiming plugin;

	public LandProtecter(LandClaiming lc) {
		plugin = lc;
	}

	@EventHandler
	public void onExplodeBlock(BlockExplodeEvent e) {
		e.setCancelled(plugin.isInArea(e.getBlock().getLocation()));
	}

	@EventHandler
	public void onExplodeEntity(EntityExplodeEvent e) {
		e.setCancelled((plugin.isInArea(e.getLocation()) && (e.getEntityType() == EntityType.PRIMED_TNT)));
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		// ClaimedLand cl = plugin.getArea(e.getClickedBlock().getLocation());
		if (e.getClickedBlock() != null) {
			if (plugin.getLockedBlock(e.getClickedBlock().getLocation()) != null) {
				ClaimedBlock cb = plugin.getLockedBlock(e.getClickedBlock().getLocation());
				if (cb.getOwner().equals(e.getPlayer().getUniqueId())
						|| cb.getGuests().contains(e.getPlayer().getUniqueId())
						|| e.getPlayer().hasPermission(plugin.PERM_ADMIN)) {

				} else {
					e.getPlayer().sendMessage(plugin.getMessage(plugin.LOCKEDCHEST));
					e.setCancelled(true);
					return;
				}
			}

			if (plugin.isInArea(e.getClickedBlock().getLocation())) {
				ClaimedLand cl = plugin.getArea(e.getClickedBlock().getLocation());
				if (cl.getOwner().equals(e.getPlayer().getUniqueId())
						|| cl.getGuests().contains(e.getPlayer().getUniqueId())
						|| e.getPlayer().hasPermission(plugin.PERM_ADMIN)) {

				} else {
					if (LandClaiming.ENABLEINTERACTABLEBLOCKS
							&& LandClaiming.interactAbleMaterials.contains(e.getClickedBlock().getType().name())) {

					} else {
						e.getPlayer().sendMessage(plugin.getMessage(plugin.ClaimedCHEST));
						e.setCancelled(true);
						return;
					}
				}
			}
		}
		/*
		 * if(cl != null){ }else
		 * if((!cl.getGuests().contains(e.getPlayer().getUniqueId())) && (cl.getOwner
		 * ()==null||!cl.getOwner().equals(e.getPlayer().getUniqueId())) &&
		 * (!e.getPlayer ().hasPermission(plugin.PERM_ADMIN)&&!e.getPlayer().isOp())){
		 * e.getPlayer
		 * ().sendMessage(plugin.PREFIX+plugin.getMessage(plugin.INTERACTEVENT ).replace
		 * ("%owner%",Bukkit.getOfflinePlayer(cl.getOwner()).getName()));
		 * e.setCancelled(true); } }
		 */
	}

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (plugin.isInArea(e.getBlock().getLocation())) {
			ClaimedLand cl = plugin.getArea(e.getBlock().getLocation());
			if (!cl.getGuests().contains(e.getPlayer().getUniqueId())
					&& !cl.getOwner().equals(e.getPlayer().getUniqueId())
					&& !e.getPlayer().hasPermission(plugin.PERM_ADMIN)) {
				e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.INTERACTEVENT).replace("%owner%",
						Bukkit.getOfflinePlayer(cl.getOwner()).getName()));
				e.setCancelled(true);

			}
		}

		if (plugin.getLockedBlock(e.getBlock().getLocation()) != null) {
			ClaimedBlock cb = plugin.getLockedBlock(e.getBlock().getLocation());
			if (cb.getOwner().equals(e.getPlayer().getUniqueId())
					|| cb.getGuests().contains(e.getPlayer().getUniqueId()) || e.getPlayer().isOp()
					|| e.getPlayer().hasPermission(plugin.PERM_ADMIN)) {
				plugin.claimedBlock.remove(cb);
				e.getPlayer().sendMessage(plugin.PREFIX + ChatColor.WHITE + " The block has been unlocked");
			} else {
				e.getPlayer().sendMessage(plugin.getMessage(plugin.LOCKEDCHEST));
				e.setCancelled(true);
				return;
			}
		}

	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (plugin.isInArea(e.getBlock().getLocation())) {
			ClaimedLand cl = plugin.getArea(e.getBlock().getLocation());
			if ((!cl.getGuests().contains(e.getPlayer().getUniqueId()))
					&& (!cl.getOwner().equals(e.getPlayer().getUniqueId()))
					&& (!e.getPlayer().hasPermission(plugin.PERM_ADMIN) && !e.getPlayer().isOp())) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.INTERACTEVENT).replace("%owner%",
						Bukkit.getOfflinePlayer(cl.getOwner()).getName()));

			} else {
				// If this is claimed land, and it is owned by the player
				/**
				if (e.getBlock().getType().name().contains("CHEST") && e.getBlock().getType() != Material.ENDER_CHEST) {
					ClaimedBlock cb = new ClaimedBlock(e.getBlock().getWorld(), e.getBlock().getLocation().getBlockX(),
							e.getBlock().getLocation().getBlockY(), e.getBlock().getLocation().getBlockZ(),
							e.getPlayer().getUniqueId());
					plugin.claimedBlock.add(cb);
					e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.LOCKBLOCK));
				}*/
			}
		}

	}

}
