# LandClaiming
Update for LandClaiming Plugin. Includes base release and release that interacts with LuckPerms.


Visit https://dev.bukkit.org/projects/landclaiming for more information around the base plugin

The LuckPerms edition hooks to luckperms and uses the groups to facilitate the claimblock limits. 
Steps to Convert to LuckPerms Edition:
1. Make a backup of your old config file config.yml.
2. Shut down server and replace old LandClaiming jar file with the LuckPerms version.
3. Remove old config to allow for new config generation (of course with backup on hand).
4. Start server up for new config creation with new group names (the plugin will generate a line item for each group within luckperms automatically).
   You can also just update the group names to match luckperms groups in your old config.
6. Update values for each group!
7. Server restart is required for the time being for the plugin to take in updates (maybe future enhancement).

To add in old claims to the new config that was generated, simply copy the claimed: [] and locked: [] lines on the bottom of the old config.yml file
