package com.jacksonjude.CommandAlias;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class CommandAliasPlugin extends JavaPlugin implements Listener
{
	public static final String ADMIN_PERMISSION = "commandalias.admin";
	public static final String ADMIN_COMMAND = "commandalias";
	
	public static final String COMMAND_ALIASES_KEY = "alias";
	public static final String DEFAULT_RUN_PERMISSION_KEY = "default-permission";
	public static final String DEFAULT_RUN_LEVEL_KEY = "default-level";
	
	public static String DEFAULT_RUN_PERMISSION;
	public static String DEFAULT_RUN_LEVEL;
	
	public static final String ALIAS_NAME_KEY = "name";
	public static final String ALIAS_COMMAND_KEY = "command";
	public static final String ALIAS_PERMISSION_KEY = "permission";
	public static final String ALIAS_LEVEL_KEY = "level";
	
	private FileConfiguration fileConfig;
	private List<Alias> aliases;
	
	private static CommandMap cmap;
	
	@Override
    public void onEnable()
    {
		getCommand(CommandAliasPlugin.ADMIN_COMMAND).setExecutor(new CommandAliasExecutor(this));
		getCommand(CommandAliasPlugin.ADMIN_COMMAND).setTabCompleter(new CommandAliasCompleter());
		
		try
		{
			final Field bukkitCommandMap = this.getServer().getClass().getDeclaredField("commandMap");
			bukkitCommandMap.setAccessible(true);
			cmap = (CommandMap) bukkitCommandMap.get(this.getServer());
        }
		catch (Exception e)
		{
            e.printStackTrace();
        }
		
		saveDefaultConfig();
		loadCommandAliases();
    }
    
    public void loadCommandAliases()
    {
    	fileConfig = getConfig();
    	
    	CommandAliasPlugin.DEFAULT_RUN_PERMISSION = (String) fileConfig.get(DEFAULT_RUN_PERMISSION_KEY);
    	CommandAliasPlugin.DEFAULT_RUN_LEVEL = (String) fileConfig.get(DEFAULT_RUN_LEVEL_KEY);
    	
    	aliases = new ArrayList<Alias>();
    	ConfigurationSection aliasesData = fileConfig.getConfigurationSection(COMMAND_ALIASES_KEY);
    	for (String aliasID : aliasesData.getKeys(false))
    	{
    		Map<String,Object> rawAliasData = aliasesData.getConfigurationSection(aliasID).getValues(false);
    		Alias newAlias = new Alias(aliasID, (String) rawAliasData.get(ALIAS_NAME_KEY), (String) rawAliasData.get(ALIAS_COMMAND_KEY), (String) rawAliasData.get(ALIAS_PERMISSION_KEY), (String) rawAliasData.get(ALIAS_LEVEL_KEY), this);
    		aliases.add(newAlias);
    		
    		Command aliasCommand = new AliasCommand(newAlias.name, new AliasCommandExecutor(newAlias));
    		cmap.register(newAlias.name, aliasCommand);
    		
    		boolean permissionDefined = false;
    		for (Permission p : this.getServer().getPluginManager().getPermissions())
    		{
    			if (p.getName().equals(newAlias.permission))
    			{
    				permissionDefined = true;
    				break;
    			}
    		}
    		    		
    		if (!permissionDefined) this.getServer().getPluginManager().addPermission(new Permission(newAlias.permission));
    	}
    }
}
