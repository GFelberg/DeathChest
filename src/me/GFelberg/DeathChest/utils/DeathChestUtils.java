package me.GFelberg.DeathChest.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.GFelberg.DeathChest.Main;

public class DeathChestUtils {

	public static String prefix, message, nobreak;

	public static void loadVariables() {
		prefix = Main.getInstance().getConfig().getString("DeathChest.Prefix").replace("&", "§");
		message = Main.getInstance().getConfig().getString("DeathChest.MessageLocation").replace("&", "§");
		nobreak = Main.getInstance().getConfig().getString("DeathChest.NoBreak").replace("&", "§");
	}

	public void reloadConfig(Player p) {
		Main.getInstance().reloadConfig();
		loadVariables();
		p.sendMessage(prefix + " " + ChatColor.GREEN + "Plugin reloaded successfully!");
		Bukkit.getConsoleSender().sendMessage("============================================");
		Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "DeathChest Plugin reloaded");
		Bukkit.getConsoleSender().sendMessage("============================================");
	}

	public void helpPage(Player p) {
		HelpPageUtils helpUtils = new HelpPageUtils();
		p.sendMessage(ChatColor.WHITE + "-----------------------------------------");
		p.sendMessage(ChatColor.AQUA + "DeathChest - Help Commands");
		p.sendMessage(ChatColor.YELLOW + "/dc help: " + helpUtils.getHelp_page());
		p.sendMessage(ChatColor.YELLOW + "/dc reload : " + helpUtils.getHelp_reload());
		p.sendMessage(ChatColor.WHITE + "-----------------------------------------");
	}
}