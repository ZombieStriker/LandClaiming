package me.zombie_striker.landclaiming.claimedobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class ClaimedBlock {

	Location loc;
	UUID owner;
	List<UUID> guests = new ArrayList<UUID>();
	
	private static final String SPLIT = "@@#";

	public ClaimedBlock(World world, int x,int y,int z,
			UUID owner) {
		loc = new Location(world, x,y,z);
		this.owner = owner;
	}

	public ClaimedBlock(String serializedString) {
		String[] s = serializedString.split(SPLIT);
		owner = UUID.fromString(s[0]);
		loc = new Location(Bukkit.getWorld(s[1]), Integer.valueOf(s[2]), Integer.valueOf(s[3]),
				Integer.valueOf(s[4]));
		if (!s[5].equalsIgnoreCase("NoGuests")) {
			for (String ss : s[5].split("&&"))
				if (ss.length() > 0)
					guests.add(UUID.fromString(ss));
		}
	}

	public String serialize() {
		String s = owner.toString() +SPLIT + loc.getWorld().getName() + SPLIT
				+loc.getBlockX() + SPLIT + loc.getBlockY()+ SPLIT
				+ loc.getBlockZ() + SPLIT;
		if (guests.size() == 0) {
			s = s + "NoGuests";
		} else {
			for (UUID uuid : guests) 
				s = s + uuid.toString() + "&&";
		}
		return s;
	}
	public Location getLocation(){
		return this.loc;
	}
	public UUID getOwner(){
		return this.owner;
	}
	public List<UUID> getGuests(){
		return this.guests;
	}
}
