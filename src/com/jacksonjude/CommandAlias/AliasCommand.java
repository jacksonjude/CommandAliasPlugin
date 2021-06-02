package com.jacksonjude.CommandAlias;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class AliasCommand extends Command
{
	private CommandExecutor exe;
	 
    public AliasCommand(String name, CommandExecutor exe) {
        super(name);
    	this.exe = exe;
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args)
    {
        if (exe != null)
        {
            exe.onCommand(sender, this, commandLabel, args);
        }
        return false;
    }
}
