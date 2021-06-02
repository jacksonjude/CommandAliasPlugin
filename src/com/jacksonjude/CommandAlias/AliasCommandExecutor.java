package com.jacksonjude.CommandAlias;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AliasCommandExecutor implements CommandExecutor
{
	private Alias alias;
	
	public AliasCommandExecutor(Alias alias)
	{
		this.alias = alias;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		String response = alias.runAlias(sender, args);
		sender.sendMessage(alias.name + " - " + response);
		
		return true;
	}
}
