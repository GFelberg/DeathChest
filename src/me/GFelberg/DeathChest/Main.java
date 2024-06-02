package me.GFelberg.DeathChest;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import me.GFelberg.DeathChest.commands.DeathChest;
import me.GFelberg.DeathChest.data.DeathChestConfig;
import me.GFelberg.DeathChest.events.DeathChestEvents;
import me.GFelberg.DeathChest.utils.DeathChestUtils;

public class Main extends JavaPlugin {

	private static Main instance;

	public void onEnable() {
		instance = this;
		saveDefaultConfig();
		DeathChestUtils.loadVariables();
		getCommand("deathchest").setExecutor(new DeathChest());
		loadDeathChestConfig();
		Bukkit.getPluginManager().registerEvents(new DeathChestEvents(), this);
		Bukkit.getConsoleSender().sendMessage("----------------------------");
		Bukkit.getConsoleSender().sendMessage("DeathChest Plugin Enabled!");
		Bukkit.getConsoleSender().sendMessage("Plugin developed by GFelberg");
		Bukkit.getConsoleSender().sendMessage("----------------------------");
	}

	public static Main getInstance() {
		return instance;
	}

	public void onDisable() {
		Bukkit.getConsoleSender().sendMessage("----------------------------");
		Bukkit.getConsoleSender().sendMessage("DeathChest Plugin Disabled!");
		Bukkit.getConsoleSender().sendMessage("Plugin developed by GFelberg");
		Bukkit.getConsoleSender().sendMessage("----------------------------");
	}
	
	public void loadDeathChestConfig() {
		DeathChestConfig.setupConfig();
		DeathChestConfig.getConfig().options().copyDefaults(true);
		DeathChestConfig.saveConfig();
	}
}