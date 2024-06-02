package me.GFelberg.DeathChest.data;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class DeathChestConfig {

	private static File file;
	private static FileConfiguration customFile;

	public static void setupConfig() {
		file = new File(Bukkit.getServer().getPluginManager().getPlugin("DeathChest").getDataFolder(), "chestLocations.yml");

		if (!(file.exists())) {
			try {
				file.createNewFile();
				Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "The chestLocations.yml file has been created");
			} catch (IOException e) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not create the chestLocations.yml file");
			}
		}

		customFile = YamlConfiguration.loadConfiguration(file);
	}

	public static FileConfiguration getConfig() {
		return customFile;
	}

	public static void saveConfig() {
		try {
			customFile.save(file);
			Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "The chestLocations.yml file has been saved");
		} catch (IOException e) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Could not save the chestLocations.yml file");
		}
	}

	public static void reloadConfig() {
		customFile = YamlConfiguration.loadConfiguration(file);
		Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "The homes.yml file has been reload");
	}
}