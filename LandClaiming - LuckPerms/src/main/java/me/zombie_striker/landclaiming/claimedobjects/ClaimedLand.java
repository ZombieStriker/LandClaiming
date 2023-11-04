package me.zombie_striker.landclaiming.claimedobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ClaimedLand {

	Location minLoc;
	Location maxLoc;
	String name;

	UUID owner;
	List<UUID> guests = new ArrayList<UUID>();
	
	private static final String SPLIT = "@@#" ;

	public ClaimedLand(World world, int minX, int minZ, int maxX, int maxZ,
			UUID owner,String name) {
		minLoc = new Location(world, minX, 0, minZ);
		maxLoc = new Location(world, maxX, 250, maxZ);
		this.name = name;
		this.owner = owner;
	}

	public ClaimedLand(String serializedString) {
		String[] s = serializedString.split( SPLIT);
		owner = UUID.fromString(s[0]);
		minLoc = new Location(Bukkit.getWorld(s[1]), Integer.valueOf(s[2]), 0,
				Integer.valueOf(s[3]));
		maxLoc = new Location(Bukkit.getWorld(s[1]), Integer.valueOf(s[4]),
				250, Integer.valueOf(s[5]));
		name = s[6];
		if (!s[7].equalsIgnoreCase("NoGuests")) {
			for (String ss : s[7].split("&&"))
				if (ss.length() > 0)
					guests.add(UUID.fromString(ss));
		}
	}

	public String serialize() {
		String s = owner.toString() +  SPLIT+ minLoc.getWorld().getName() + SPLIT
				+ minLoc.getBlockX() + SPLIT + minLoc.getBlockZ() +  SPLIT
				+ maxLoc.getBlockX() +  SPLIT + maxLoc.getBlockZ() +  SPLIT+name+ SPLIT;
		if (guests.size() == 0) {
			s = s + "NoGuests";
		} else {
			for (UUID uuid : guests) {
				s = s + uuid.toString() +"&&";
			}
		}
		return s;
	}

	public String getName(){
		return this.name;
	}
	public Location getMinLoc(){
		return this.minLoc;
	}
	public Location getMaxLoc(){
		return this.maxLoc;
	}
	public UUID getOwner(){
		return this.owner;
	}
	public List<UUID> getGuests(){
		return this.guests;
	}
}
