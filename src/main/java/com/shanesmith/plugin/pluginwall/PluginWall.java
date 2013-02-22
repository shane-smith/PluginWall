package com.shanesmith.plugin.pluginwall;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
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
    		if (block.getType().equals(Material.WALL_SIGN))
    		{
    			// Try to get a reference to the requested plugin
    			Plugin plugin = getServer().getPluginManager().getPlugin(lines[1]);
    			
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
    				if (website == null) website = "";
    				website = website.replace("http://www.", "");
    				website = website.replace("http://", "");
    				// Display those details
    				ev.setLine(0, ChatColor.DARK_BLUE+"[PluginWall]");
    				ev.setLine(1, plugin.getName());
    				ev.setLine(2, version);
    				ev.setLine(3, authors.get(0));
    			}
    		}
    	}
    }
    
    /**
     * Stops a player from accessing chests.
     * @param ev The event to be handled
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent ev)
    {
    	Player p = ev.getPlayer();
    	Block block = ev.getClickedBlock();
    	if (block != null && block.getType().equals(Material.WALL_SIGN))
    	{
    		// Get a reference to Sign Block
            Sign sign = (Sign) block.getState();
            
            // Try to get a reference to the requested plugin (will fail if this is not a PluginWall sign)
			Plugin plugin = getServer().getPluginManager().getPlugin( sign.getLine(1) );
			
			// Display information about the plugin to the user
			if (plugin != null)
			{
				// Get a reference to the PluginDescriptionFile
				PluginDescriptionFile pdf = plugin.getDescription();
				
				// Display information to the player
				// START LINE
				p.sendMessage(ChatColor.GOLD+"========================================");
				// PLUGIN NAME & VERSION
				p.sendMessage(ChatColor.YELLOW+"Plugin: "+ChatColor.WHITE+pdf.getFullName());
				// DESCRIPTION
				p.sendMessage(ChatColor.YELLOW+"Description: "+ChatColor.WHITE+pdf.getDescription());
				// AUTHORS
				if (pdf.getAuthors() != null)
				{
					List<String> authorList = pdf.getAuthors();
					String authors = ChatColor.YELLOW+"Author";
					if (authorList.size() != 1)
						authors += "s";
					authors += ":"+ChatColor.WHITE;
					for (String author : authorList)
						authors += " " + author;
					p.sendMessage(authors);
				}
				// WEBSITE
				if (pdf.getWebsite() != null)
				{
					p.sendMessage(ChatColor.YELLOW+"Website: "+ChatColor.WHITE+pdf.getWebsite());
				}
				// COMMANDS
				if (pdf.getCommands() != null)
				{
					Map<String, Map<String, Object>> commands = pdf.getCommands();
					String cmds = ChatColor.YELLOW+"Commands:"+ChatColor.WHITE;
					Set<String> commandSet = commands.keySet();
					for (String command : commandSet)
						cmds += " /" + command;
					p.sendMessage(cmds);
				}
				// END LINE
				p.sendMessage(ChatColor.GOLD+"========================================");
			}
    	}
    }
}

