package me.zombie_striker.landclaiming;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import me.zombie_striker.landclaiming.claimedobjects.ClaimedBlock;
import me.zombie_striker.landclaiming.claimedobjects.ClaimedLand;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class LandClaimer implements Listener {

	private LandClaiming plugin;

	public HashMap<UUID, Location> firstBlock = new HashMap<UUID, Location>();
	public static HashMap<UUID, String> claimingMode = new HashMap<>();

	public static HashMap<UUID, Integer> lockingMode = new HashMap<UUID, Integer>();
	public static HashMap<UUID, UUID> lockingPlayer = new HashMap<UUID, UUID>();
	public static List<Material> doortypes = new ArrayList<Material>();
	static {
		try {
			doortypes.add(Material.IRON_DOOR);

			doortypes.add(Material.ACACIA_DOOR);
			doortypes.add(Material.DARK_OAK_DOOR);
			doortypes.add(Material.JUNGLE_DOOR);
			doortypes.add(Material.SPRUCE_DOOR);
			doortypes.add(Material.BIRCH_DOOR);
			doortypes.add(Material.OAK_DOOR);
		} catch (Error | Exception e) {
			doortypes.add(Material.matchMaterial("WOODEN_DOOR"));
			doortypes.add(Material.matchMaterial("WOOD_DOOR"));
		}
	}

	public LandClaimer(LandClaiming lc) {
		plugin = lc;
	}

	@EventHandler
	public void onClick(PlayerInteractEvent e) {
		if (lockingMode.containsKey(e.getPlayer().getUniqueId()) && lockingMode.get(e.getPlayer().getUniqueId()) == 0) {
			if (e.getPlayer().hasPermission(plugin.PERM_DEFAULT) || e.getPlayer().hasPermission(plugin.PERM_ADMIN)
					|| e.getPlayer().isOp()) {
				if (plugin.getLockedBlock(e.getClickedBlock().getLocation()) != null) {
					e.setCancelled(true);
					lockingMode.remove(e.getPlayer().getUniqueId());
					e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.ALREADYLOCKBLOCK));
					return;
				} else {
					ClaimedBlock cb = new ClaimedBlock(e.getClickedBlock().getWorld(),
							e.getClickedBlock().getLocation().getBlockX(),
							e.getClickedBlock().getLocation().getBlockY(),
							e.getClickedBlock().getLocation().getBlockZ(), e.getPlayer().getUniqueId());
					plugin.claimedBlock.add(cb);
					if (doortypes.contains(e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType())) {
						Block b = e.getClickedBlock().getLocation().add(0, 1, 0).getBlock();
						ClaimedBlock cb2 = new ClaimedBlock(b.getWorld(), b.getLocation().getBlockX(),
								b.getLocation().getBlockY(), b.getLocation().getBlockZ(), e.getPlayer().getUniqueId());
						plugin.claimedBlock.add(cb2);
					}
					if (doortypes.contains(e.getClickedBlock().getLocation().subtract(0, 1, 0).getBlock().getType())) {
						Block b = e.getClickedBlock().getLocation().subtract(0, 1, 0).getBlock();
						ClaimedBlock cb2 = new ClaimedBlock(b.getWorld(), b.getLocation().getBlockX(),
								b.getLocation().getBlockY(), b.getLocation().getBlockZ(), e.getPlayer().getUniqueId());
						plugin.claimedBlock.add(cb2);
					}
					lockingMode.remove(e.getPlayer().getUniqueId());
					e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.LOCKBLOCK));
					e.setCancelled(true);
					return;
				}
			} else {
				e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.PERMISSION));
				lockingMode.remove(e.getPlayer().getUniqueId());
			}
		}
		if (lockingMode.containsKey(e.getPlayer().getUniqueId()) && lockingMode.get(e.getPlayer().getUniqueId()) == 1) {
			if (e.getPlayer().hasPermission(plugin.PERM_DEFAULT) || e.getPlayer().hasPermission(plugin.PERM_ADMIN)
					|| e.getPlayer().isOp()) {
				if (plugin.getLockedBlock(e.getClickedBlock().getLocation()) == null) {
					e.setCancelled(true);
					lockingMode.remove(e.getPlayer().getUniqueId());
					e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.NOTLOCKEDBLOCK));
					return;
				} else if (plugin.getLockedBlock(e.getClickedBlock().getLocation()).getOwner()
						.equals(e.getPlayer().getUniqueId()) || e.getPlayer().isOp()
						|| e.getPlayer().hasPermission(plugin.PERM_ADMIN)) {
					ClaimedBlock cb = plugin.getLockedBlock(e.getClickedBlock().getLocation());
					plugin.claimedBlock.remove(cb);
					if (doortypes.contains(e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType())) {
						Block b = e.getClickedBlock().getLocation().add(0, 1, 0).getBlock();
						ClaimedBlock cb2 = plugin.getLockedBlock(b.getLocation());
						plugin.claimedBlock.remove(cb2);
					}
					if (doortypes.contains(e.getClickedBlock().getLocation().subtract(0, 1, 0).getBlock().getType())) {
						Block b = e.getClickedBlock().getLocation().subtract(0, 1, 0).getBlock();
						ClaimedBlock cb2 = plugin.getLockedBlock(b.getLocation());
						plugin.claimedBlock.remove(cb2);
					}
					lockingMode.remove(e.getPlayer().getUniqueId());
					e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.UNLOCKBLOCK));
					e.setCancelled(true);
					return;
				}
			} else {
				e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.PERMISSION));
				lockingMode.remove(e.getPlayer().getUniqueId());
			}
		}
		if (lockingMode.containsKey(e.getPlayer().getUniqueId()) && lockingMode.get(e.getPlayer().getUniqueId()) == 2) { // add
																															// guest
			if (e.getPlayer().hasPermission(plugin.PERM_DEFAULT) || e.getPlayer().hasPermission(plugin.PERM_ADMIN)
					|| e.getPlayer().isOp()) {
				if (plugin.getLockedBlock(e.getClickedBlock().getLocation()) == null) {
					e.setCancelled(true);
					lockingMode.remove(e.getPlayer().getUniqueId());
					lockingPlayer.remove(e.getPlayer().getUniqueId());
					e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.NOTLOCKEDBLOCK));
					return;
				} else if (plugin.getLockedBlock(e.getClickedBlock().getLocation()).getOwner()
						.equals(e.getPlayer().getUniqueId()) || e.getPlayer().isOp()
						|| e.getPlayer().hasPermission(plugin.PERM_ADMIN)) {
					ClaimedBlock cb = plugin.getLockedBlock(e.getClickedBlock().getLocation());
					cb.getGuests().add(lockingPlayer.get(e.getPlayer().getUniqueId()));
					if (doortypes.contains(e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType())) {
						Block b = e.getClickedBlock().getLocation().add(0, 1, 0).getBlock();
						ClaimedBlock cb2 = plugin.getLockedBlock(b.getLocation());
						cb2.getGuests().add(lockingPlayer.get(e.getPlayer().getUniqueId()));
					}
					if (doortypes.contains(e.getClickedBlock().getLocation().subtract(0, 1, 0).getBlock().getType())) {
						Block b = e.getClickedBlock().getLocation().subtract(0, 1, 0).getBlock();
						ClaimedBlock cb2 = plugin.getLockedBlock(b.getLocation());
						cb2.getGuests().add(lockingPlayer.get(e.getPlayer().getUniqueId()));
					}
					lockingMode.remove(e.getPlayer().getUniqueId());
					lockingPlayer.remove(e.getPlayer().getUniqueId());
					e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.ADDGUEST));
					e.setCancelled(true);
					return;
				}
			} else {
				e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.PERMISSION));
				lockingMode.remove(e.getPlayer().getUniqueId());
			}
		}
		if (lockingMode.containsKey(e.getPlayer().getUniqueId()) && lockingMode.get(e.getPlayer().getUniqueId()) == 3) {// remove
																														// guest
			if (e.getPlayer().hasPermission(plugin.PERM_DEFAULT) || e.getPlayer().hasPermission(plugin.PERM_ADMIN)
					|| e.getPlayer().isOp()) {
				if (plugin.getLockedBlock(e.getClickedBlock().getLocation()) == null) {
					e.setCancelled(true);
					lockingMode.remove(e.getPlayer().getUniqueId());
					lockingPlayer.remove(e.getPlayer().getUniqueId());
					e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.NOTLOCKEDBLOCK));
					return;
				} else if (plugin.getLockedBlock(e.getClickedBlock().getLocation()).getOwner()
						.equals(e.getPlayer().getUniqueId()) || e.getPlayer().isOp()
						|| e.getPlayer().hasPermission(plugin.PERM_ADMIN)) {
					ClaimedBlock cb = plugin.getLockedBlock(e.getClickedBlock().getLocation());
					cb.getGuests().remove(lockingPlayer.get(e.getPlayer().getUniqueId()));
					if (doortypes.contains(e.getClickedBlock().getLocation().add(0, 1, 0).getBlock().getType())) {
						Block b = e.getClickedBlock().getLocation().add(0, 1, 0).getBlock();
						ClaimedBlock cb2 = plugin.getLockedBlock(b.getLocation());
						cb2.getGuests().remove(lockingPlayer.get(e.getPlayer().getUniqueId()));
					}
					if (doortypes.contains(e.getClickedBlock().getLocation().subtract(0, 1, 0).getBlock().getType())) {
						Block b = e.getClickedBlock().getLocation().subtract(0, 1, 0).getBlock();
						ClaimedBlock cb2 = plugin.getLockedBlock(b.getLocation());
						cb2.getGuests().remove(lockingPlayer.get(e.getPlayer().getUniqueId()));
					}
					lockingMode.remove(e.getPlayer().getUniqueId());
					lockingPlayer.remove(e.getPlayer().getUniqueId());
					e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.REMOVEGUEST));
					e.setCancelled(true);
					return;
				}
			} else {
				e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.PERMISSION));
				lockingMode.remove(e.getPlayer().getUniqueId());
			}
		}

		if (claimingMode.containsKey(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			if (e.getPlayer().hasPermission(plugin.PERM_DEFAULT) || e.getPlayer().hasPermission(plugin.PERM_ADMIN)) {
				if (firstBlock.containsKey(e.getPlayer().getUniqueId())) {
					if (firstBlock.get(e.getPlayer().getUniqueId()).equals(e.getClickedBlock().getLocation())) {
						return;
					}

					int xmin = Math.min(firstBlock.get(e.getPlayer().getUniqueId()).getBlockX(),
							e.getClickedBlock().getLocation().getBlockX());
					int xmax = Math.max(firstBlock.get(e.getPlayer().getUniqueId()).getBlockX(),
							e.getClickedBlock().getLocation().getBlockX());
					int zmin = Math.min(firstBlock.get(e.getPlayer().getUniqueId()).getBlockZ(),
							e.getClickedBlock().getLocation().getBlockZ());
					int zmax = Math.max(firstBlock.get(e.getPlayer().getUniqueId()).getBlockZ(),
							e.getClickedBlock().getLocation().getBlockZ());

					int lands = plugin.getConfig().getInt(plugin.MAXCLAIMINT2);
					for (Entry<String, Integer> ent : plugin.maxLands.entrySet()) {
						if (e.getPlayer().hasPermission(ent.getKey())) {
							if (ent.getValue() < 0)
								lands = Integer.MAX_VALUE;
							else
								lands = ent.getValue();
						}
					}

					if (plugin.getTotalClaimedBlocks(e.getPlayer().getUniqueId())
							+ ((xmax - xmin) * (zmax - zmin)) > lands) {
						e.getPlayer()
								.sendMessage(plugin.PREFIX + (plugin.getMessage(plugin.MAXCLAIM)
										.replace("%maxblocks%", lands + "").replace("cblocks",
												plugin.getTotalClaimedBlocks(e.getPlayer().getUniqueId()) + "")));
						claimingMode.remove(e.getPlayer().getUniqueId());
						firstBlock.remove(e.getPlayer().getUniqueId());
						return;
					}

					if (!plugin.isIntersecting(e.getPlayer().getWorld(), xmin, zmin, xmax, zmax)) {
						ClaimedLand cl = new ClaimedLand(e.getClickedBlock().getWorld(), xmin, zmin, xmax, zmax,
								e.getPlayer().getUniqueId(), claimingMode.get(e.getPlayer().getUniqueId()));
						plugin.claimedLand.add(cl);
						e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.CLAIMEDLAND)
								.replace("%name%", claimingMode.get(e.getPlayer().getUniqueId())));
						firstBlock.remove(e.getPlayer().getUniqueId());
						claimingMode.remove(e.getPlayer().getUniqueId());
					} else {
						e.getPlayer().sendMessage(plugin.getMessage(plugin.NOTCLAIM));
						firstBlock.remove(e.getPlayer().getUniqueId());
						claimingMode.remove(e.getPlayer().getUniqueId());
						return;
					}
				} else {
					if (e.getClickedBlock() == null)
						return;
					firstBlock.put(e.getPlayer().getUniqueId(), e.getClickedBlock().getLocation());
					e.getPlayer()
							.sendMessage(plugin.PREFIX + (plugin.getMessage(plugin.CLAIMCORNER)
									.replace("%x%", e.getClickedBlock().getLocation().getBlockX() + "")
									.replace("%z%", e.getClickedBlock().getLocation().getBlockZ() + "")));
					e.getPlayer()
							.sendMessage(plugin.PREFIX + " Click the other corner of the region you want to claim.");
				}

			} else {
				e.getPlayer().sendMessage(plugin.PREFIX + plugin.getMessage(plugin.PERMISSION));
				claimingMode.remove(e.getPlayer().getUniqueId());
			}
		}
	}
}
