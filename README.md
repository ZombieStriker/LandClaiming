# LandClaiming
Update v1.5.1  for LandClaiming plugin. The release is now a single release for both base and LuckPerms integration.

Visit https://dev.bukkit.org/projects/landclaiming for more information around the base plugin.

New Commands added to facilitate on-the-go updates to group limits (these commands will work for both LuckPerms Integration or default):
	-/landClaim setGroupLimit <groupame> <limit> // This command will set a claim block limit for a specific group. Should auto tab-complete with all available groups. 
	-/landClaim getGroupLimit <groupame> // This command will return the limit for set group
	-/landClaim help //shows this help menu
		
Note: Any changes using these commands will instantly take effect and will over-write configuration file

LuckPerms Integration:
This plugin now integrates with LuckPerms. 

To enable integration:
1. Stop server.
2. Back up current configuration file Config.yml in the LandClaiming folder (in case).
3. Update jar file to v1.2.3 of LandClaim (if not updated yet)
	- If you just installed and have no Config.yml file, start the server with the new update to create the Config.yml file before moving forward. 
4. Update the following value in the Plugin's Config.yml file:
	-isluckperms: 'True'
5. Start server up to load new configuration. This should not clear your old claims. The backup is in case. 
	- There will be a new section in the configuration file called "lpgroup" under options. This section in the configuration is auto-updated by the server. 
	- All new LuckPerms groups are added automatically to the configuration on restart (maybe future feature for refreshing configuration from file).
	- If old claims are wiped, copy them from the bottom of the backup configuration file and place them into the new one and restart.

If you change the Config.yml file manually, ensure you restart shortly after. 
Current changes are auto-saving. This means that any changes within the configuration will be written over if something changes on the server.
