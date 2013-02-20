package com.shanesmith.plugin.pluginwall;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PluginWall extends JavaPlugin implements Listener
{
	// Declarations
	
	// onEnable
    public void onEnable()
    {
    	// Register events
    	getServer().getPluginManager().registerEvents(this, this);
    }
    
    // onDisable
    public void onDisable()
    {
        // Don't need to do anything on plugin disable
    }
    
    @EventHandler
    public void onSignChange(SignChangeEvent ev)
    {
    	// Get useful information
    	Player player = ev.getPlayer();
    	Block block = ev.getBlock();
    	String[] lines = ev.getLines();
    	
    	if (player.hasPermission("PluginWall.create") && lines[1].length() > 0 &&
    		(lines[0].equalsIgnoreCase("[PLW]") || lines[0].equalsIgnoreCase("[PluginWall]") 
    		|| lines[0].equalsIgnoreCase("[Plugin]")) )
    	{
    		if (block.getType().equals(Material.WALL_SIGN) || block.getType().equals(Material.SIGN))
    		{
    			// Try to get a reference to the requested plugin
    			Plugin plugin = getServer().getPluginManager().getPlugin(lines[1]);
    			
    			// Get a reference to Sign Block
    			//Sign sign = (Sign) block.getState();
    			
    			// Update the Sign
    			if (plugin == null)
    			{
    				// If the plugin does not exist, output an error message
    				ev.setLine(0, ChatColor.DARK_RED+"Plugin was");
    				ev.setLine(1, ChatColor.DARK_RED+"Not Found");
    				ev.setLine(2, ChatColor.BLACK+"Name is case");
    				ev.setLine(3, ChatColor.BLACK+"sensitive.");
    			}
    			else
    			{
    				// If the plugin was found, display information about it.
    				// Get useful details
    				String version = plugin.getDescription().getVersion();
    				List<String> authors = plugin.getDescription().getAuthors();
    				String website = plugin.getDescription().getWebsite();
    				if (website==null) website = "";
    				website = website.replace("http://www.", "");
    				website = website.replace("http://", "");
    				// Display those details
    				ev.setLine(0, ChatColor.DARK_GREEN+plugin.getName());
    				ev.setLine(1, ChatColor.DARK_GREEN+version);
    				ev.setLine(2, ChatColor.BLUE+website);
    				ev.setLine(3, ChatColor.WHITE+authors.get(0));
    			}
    		}
    	}
    }
}

