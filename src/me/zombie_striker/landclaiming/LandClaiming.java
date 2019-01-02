package me.zombie_striker.landclaiming;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import java.util.UUID;

import me.zombie_striker.landclaiming.claimedobjects.ClaimedBlock;
import me.zombie_striker.landclaiming.claimedobjects.ClaimedLand;
import me.zombie_striker.landclaiming.commands.ClaimCommand;
import me.zombie_striker.landclaiming.commands.LockCommand;
import me.zombie_striker.landclaiming.commands.UnclaimCommand;

import org.bukkit.*;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class LandClaiming extends JavaPlugin {

	public List<ClaimedLand> claimedLand = new ArrayList<ClaimedLand>();
	public List<ClaimedBlock> claimedBlock = new ArrayList<ClaimedBlock>();

	public final String INTERACTEVENT = "landclaiming.message.interactblock";
	public final String CLAIMEDLAND = "landclaiming.message.claimland";

	public final String LOCKBLOCK = "landclaiming.message.lockchest";
	public final String UNLOCKBLOCK = "landclaiming.message.unlockchest";
	public final String ALREADYLOCKBLOCK = "landclaiming.message.alreadylockchest";
	public final String NOTLOCKEDBLOCK = "landclaiming.message.notlockedchest";
	public final String LOCKEDCHEST = "landclaiming.message.lockedchest";
	public final String ClaimedCHEST = "landclaiming.message.claimedchest";

	public final String UNCLAIMLAND = "landclaiming.message.unclaimland";
	public final String CLAIMCORNER = "landclaiming.message.claimcorner";
	public final String NOTCLAIM = "landclaiming.message.notclaim";
	public final String MAXCLAIM = "landclaiming.message.maxclaims";
	public final String MAXCLAIMINT = "landclaiming.options.maxclaimedblocks";
	public final String MAXCLAIMINT2 = "landclaiming.options.maxclaimedblocksDEFAULT";
	public final String PERMISSION = "landclaiming.message.permission";

	public final String ADDGUEST = "landclaiming.message.addguest";
	public final String REMOVEGUEST = "landclaiming.message.removeguest";

	public final String PREFIX = ChatColor.GREEN + "[Land-Claiming]";

	public final Permission PERM_DEFAULT = new Permission("landclaim.default");
	public final Permission PERM_ADMIN = new Permission("landclaim.admin");

	public static List<String> interactAbleMaterials = Arrays.asList(new String[]{ "LEVER", "CHEST", "TRAPPED_CHEST",
			"WOOD_DOOR", "ACACIA_DOOR", "SPRUCE_DOOR", "BIRCH_DOOR", "DARK_OAK_DOOR",
			"JUNGLE_DOOR", "TRAP_DOOR", "STONE_BUTTON","WOOD_BUTTON","ENDER_CHEST",
			"DISPENSER", "DROPPER", "FURNACE" });
	public final String iwc = "landclaiming.options.enableInteractWithinClaims";
	public final String iwce = "landclaiming.options.InteractWithinClaimsExceptions";
	
	public static boolean ENABLEINTERACTABLEBLOCKS = false;

	public static final Permission PERM_ALL = new Permission("landclaim.*");

	public HashMap<String, Integer> maxLands = new HashMap<>();

	private boolean save = false;

	public Object a(String path, Object o) {
		if (getConfig().contains(path))
			return getConfig().get(path);
		getConfig().set(path, o);
		save = true;
		return o;
	}

	@SuppressWarnings("unchecked")
	public void onEnable() {
		a("landclaiming.message.permission", "&4 You do not have permission to complete this action.");

		a("landclaiming.message.interactblock",
				"&4 You cannot interact with blocks in this region. Land claimed by %owner%.");
		a("landclaiming.message.claimland", "&c You have claimed this land as %name%.");

		a("landclaiming.message.lockchest", "&c You have locked this block");
		a("landclaiming.message.unlockchest", "&c You have unlocked this block");
		a("landclaiming.message.alreadylockchest", "&c This block has already been claimed");
		a("landclaiming.message.lockedchest", "&c This block is locked");
		a("landclaiming.message.notlockedchest", "&c This block is not locked");
		a("landclaiming.message.claimedchest", "&c This block is claimed");

		a("landclaiming.message.addguest", "&c You have added %player%");
		a("landclaiming.message.removeguest", "&c You have removed %player%");

		a("landclaiming.message.unclaimland", "&c You have unlcaimed %name%");
		a("landclaiming.message.claimcorner", "&c You claimed this corner at %x% and %z%");
		a("landclaiming.message.notclaim",
				"&c You cannot claimed this land. There is already land claimed in this region.");
		a(MAXCLAIM,
				"&c You have claimed too many blocks! The max amount of blocks are %maxblocks%, and you currently have claimed &cblocks& blocks.");
		a(MAXCLAIMINT + ".group.Default", 5000);
		a(MAXCLAIMINT + ".group.Member", 10000);
		a(MAXCLAIMINT + ".group.op", -1);
		a(MAXCLAIMINT2, 500);
		
		ENABLEINTERACTABLEBLOCKS = (boolean) a(iwc,false);
		
		interactAbleMaterials = (List<String>) a(iwce,interactAbleMaterials);
		
		
		
		if (save)
			saveConfig();

		for (String g : getConfig().getConfigurationSection(MAXCLAIMINT + ".group").getKeys(false)) {
			maxLands.put(g, getConfig().getInt(MAXCLAIMINT + ".group." + g));
		}

		if (getConfig().contains("landclaiming.claimed"))
			for (String s : getConfig().getStringList("landclaiming.claimed")) {
				ClaimedLand cl = new ClaimedLand(s);
				claimedLand.add(cl);
			}
		if (getConfig().contains("landclaiming.locked"))
			for (String s : getConfig().getStringList("landclaiming.locked")) {
				ClaimedBlock cl = new ClaimedBlock(s);
				claimedBlock.add(cl);
			}

		Bukkit.getPluginManager().registerEvents(new LandProtecter(this), this);
		Bukkit.getPluginManager().registerEvents(new LandClaimer(this), this);

		LockCommand lc = new LockCommand(this);
		ClaimCommand cc = new ClaimCommand(this);
		UnclaimCommand uc = new UnclaimCommand(this);

		this.getCommand("lock").setExecutor(lc);
		this.getCommand("unlock").setExecutor(lc);
		this.getCommand("unlock").setTabCompleter(lc);

		this.getCommand("claim").setExecutor(cc);
		this.getCommand("unclaim").setExecutor(uc);
		
		this.getCommand("claim").setTabCompleter(cc);
		this.getCommand("unclaim").setTabCompleter(uc);


		if (Bukkit.getPluginManager().getPlugin("PluginConstructorAPI") == null)
			new DependencyDownloader(this, 276723);

		new Updater(this, 98632, true);
	}

	@Override
	public void onDisable() {
		save();
	}

	public void save() {
		List<String> lips = new ArrayList<>();
		for (ClaimedLand cl : this.claimedLand) {
			lips.add(cl.serialize());
		}
		getConfig().set("landclaiming.claimed", lips);

		List<String> lips2 = new ArrayList<>();
		for (ClaimedBlock cl2 : this.claimedBlock) {
			lips2.add(cl2.serialize());
		}
		getConfig().set("landclaiming.locked", lips2);
		saveConfig();
	}

	public String getMessage(String message) {
		return ChatColor.translateAlternateColorCodes('&', getConfig().getString(message));
	}

	public boolean isInArea(Location l) {
		for (ClaimedLand cl : claimedLand) {
			if ((l.getBlockX() >= cl.getMinLoc().getBlockX() && l.getBlockX() <= cl.getMaxLoc().getBlockX())
					&& (l.getBlockZ() >= cl.getMinLoc().getBlockZ() && l.getBlockZ() <= cl.getMaxLoc().getBlockZ()))
				if (l.getWorld() == cl.getMaxLoc().getWorld())
					return true;
		}
		return false;
	}

	public ClaimedLand getArea(Location l) {
		for (ClaimedLand cl : claimedLand) {
			if ((l.getBlockX() >= cl.getMinLoc().getBlockX() && l.getBlockX() <= cl.getMaxLoc().getBlockX())
					&& (l.getBlockZ() >= cl.getMinLoc().getBlockZ() && l.getBlockZ() <= cl.getMaxLoc().getBlockZ())) {
				if (l.getWorld() == cl.getMaxLoc().getWorld())
					return cl;
			}
		}
		return null;
	}

	public boolean isIntersecting(World w, int xmin, int zmin, int xmax, int zmax) {
		for (ClaimedLand cl : claimedLand) {
			if (cl.getMinLoc().getBlockX() > xmax || cl.getMaxLoc().getBlockX() < xmin
					|| cl.getMinLoc().getBlockZ() > zmax || cl.getMaxLoc().getBlockZ() < zmin)
				continue;
			if (cl.getMaxLoc().getWorld() == w)
				return true;
		}
		return false;
	}

	public int getTotalClaimedBlocks(UUID uuid) {
		int amount = 0;
		for (ClaimedLand cl : this.claimedLand) {
			if (cl.getOwner().equals(uuid)) {
				amount += (cl.getMaxLoc().getBlockX() - cl.getMinLoc().getBlockX())
						* (cl.getMaxLoc().getBlockZ() - cl.getMinLoc().getBlockZ());
			}
		}
		return amount;
	}

	public ClaimedBlock getLockedBlock(Location l) {
		for (ClaimedBlock cb : this.claimedBlock) {
			if (cb.getLocation().equals(l))
				return cb;
		}
		return null;
	}
}
