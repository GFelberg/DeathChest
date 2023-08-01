package me.GFelberg.DeathChest.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.GFelberg.DeathChest.utils.DeathChestUtils;

public class DeathChestEvents implements Listener {

	private Map<UUID, ItemStack[]> player_items = new HashMap<UUID, ItemStack[]>();

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player p = event.getEntity();
		double x = p.getLocation().getX();
		double y = p.getLocation().getY();
		double z = p.getLocation().getZ();

		List<ItemStack> contents = new ArrayList<>(event.getDrops());
		ItemStack[] items = contents.toArray(new ItemStack[0]);
		player_items.put(p.getUniqueId(), items);
		event.getDrops().clear();
		summonChests(p, x, y, z);
	}

	private void summonSingleChest(Player p, double x, double y, double z) {
		Location blockLocation1 = new Location(p.getWorld(), x, y, z);
		Block deathChestBlock1 = blockLocation1.getBlock();
		deathChestBlock1.setType(Material.CHEST);
		Chest deathChest1 = (Chest) deathChestBlock1.getState();
		Inventory chestInventory = deathChest1.getInventory();
		ItemStack[] items = player_items.get(p.getUniqueId());
		ItemStack[] itemsToTransfer = Arrays.copyOfRange(items, 0, Math.min(items.length, 27));
		chestInventory.setContents(itemsToTransfer);
	}

	private void summonChests(Player p, double x, double y, double z) {
		ItemStack[] items = player_items.get(p.getUniqueId());
		int filledSlots = getFilledItemSlots(p);

		if (filledSlots <= 27) {
			summonSingleChest(p, x, y, z);
			player_items.remove(p.getUniqueId());
			p.sendMessage(DeathChestUtils.message.replace("%x%", String.valueOf((int) x))
					.replace("%y%", String.valueOf((int) y)).replace("%z%", String.valueOf((int) z)));
			return;
		} else {
			summonSingleChest(p, x, y, z);
			Location blockLocation2 = new Location(p.getWorld(), x + 1, y, z);
			Block deathChestBlock2 = blockLocation2.getBlock();
			deathChestBlock2.setType(Material.CHEST);
			Chest deathChest2 = (Chest) deathChestBlock2.getState();
			Inventory chestInventory2 = deathChest2.getInventory();
			ItemStack[] itemsToTransfer2 = Arrays.copyOfRange(items, 27, items.length);
			chestInventory2.setContents(itemsToTransfer2);

			player_items.remove(p.getUniqueId());
			p.sendMessage(DeathChestUtils.message.replace("%x%", String.valueOf((int) x))
					.replace("%y%", String.valueOf((int) y)).replace("%z%", String.valueOf((int) z)));
		}
	}

	private int getFilledItemSlots(Player p) {
		ItemStack[] items = player_items.get(p.getUniqueId());
		if (items == null) {
			return 0;
		}
		int filledSlots = 0;
		for (ItemStack item : items) {
			if (item != null) {
				filledSlots++;
			}
		}
		return filledSlots;
	}
}