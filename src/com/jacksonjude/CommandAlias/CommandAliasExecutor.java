package com.jacksonjude.CommandAlias;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandAliasExecutor implements CommandExecutor
{
	private CommandAliasPlugin plugin;
	
	public CommandAliasExecutor(CommandAliasPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if (!sender.hasPermission(CommandAliasPlugin.ADMIN_PERMISSION))
		{
			sender.sendMessage(ChatColor.RED + "You cannot edit CommandAlias config");
			return true;
		}
		
		if (args.length == 0)
		{
			sender.sendMessage(ChatColor.GOLD + "/commandalias reload" + ChatColor.GRAY + " - reloads configuration" + "\n" + ChatColor.GOLD);
			return true;
		}
		
		switch (args[0].toLowerCase())
		{
		case "reload":
			plugin.reloadConfig();
			plugin.loadCommandAliases();
			sender.sendMessage(ChatColor.GREEN + "CommandAlias config reloaded");
			
			return true;
		}
		
		return false;
	}
}
