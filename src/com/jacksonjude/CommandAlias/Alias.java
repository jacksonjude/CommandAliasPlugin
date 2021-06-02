package com.jacksonjude.CommandAlias;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;

public class Alias
{
	private final CommandAliasPlugin plugin;
	
	public final String id;
	public String name;
	public String command;
	public String permission;
	public CommandLevel level;
	
	public enum CommandLevel
	{
		SERVER,
		PLAYER
	}
	
	public Alias(String id, String name, String command, String permission, String level, CommandAliasPlugin plugin)
	{
		this.id = id;
		this.name = name;
		this.command = command;
		if (permission == null || permission.equals(""))
			this.permission = CommandAliasPlugin.DEFAULT_RUN_PERMISSION;
		else
			this.permission = permission;
		if (level == null || level.equals(""))
			this.level = CommandLevel.valueOf(CommandAliasPlugin.DEFAULT_RUN_LEVEL.toUpperCase());
		else
			this.level = CommandLevel.valueOf(level.toUpperCase());
		this.plugin = plugin;
	}
	
	public String runAlias(CommandSender sender, String[] args)
	{
		if (!sender.hasPermission(this.permission)) return "Insufficient permissions (" + this.permission + ")";
		
		String finalCommand = new String(command);
		
		finalCommand = finalCommand.replaceAll("\\$\\{s\\}", sender.getName());
		finalCommand = finalCommand.replaceAll("\\$\\{a\\}", "@a");
		
		for (int i=0; i < args.length; i++)
		{
			finalCommand = finalCommand.replaceAll("\\$\\{" + (i+1) + ".*?\\}", args[i]);
		}
		
		Matcher matcher = Pattern.compile("\\$\\{(\\d),(.*?)\\}").matcher(finalCommand);
		while (matcher.find())
		{
			String argNumber = matcher.group(1);
			String defaultArg = matcher.group(2);
			finalCommand = finalCommand.replaceAll("\\$\\{" + argNumber + ".*?\\}", defaultArg);
		}
		
		boolean response = false;
		switch (this.level)
		{
		case SERVER:
			response = plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), finalCommand);
			break;
		case PLAYER:
			response = plugin.getServer().dispatchCommand(sender, finalCommand);
			break;
		}
		
		if (response)
			return "Completed successfully";
		else
			return "Returned error";		
	}
}
