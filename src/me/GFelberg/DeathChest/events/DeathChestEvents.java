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
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.GFelberg.DeathChest.data.DeathChestConfig;
import me.GFelberg.DeathChest.utils.DeathChestUtils;

public class DeathChestEvents implements Listener {

	private Map<UUID, ItemStack[]> player_items = new HashMap<UUID, ItemStack[]>();
	
	private List<UUID> IsActivated = new ArrayList<UUID>();
	
	private Map<UUID, Location> deathSingleChestLocation = new HashMap<UUID, Location>();
	private Map<UUID, Location> deathDoubleChestLocation = new HashMap<UUID, Location>();
	//private Map<UUID, List<Location>> deathDoubleChestLocation = new HashMap<UUID, List<Location>>();

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

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player p = event.getPlayer();
		Block block = event.getBlock();
		
		if (block.getType() == Material.CHEST) {
			Chest chest = (Chest) block.getState();
			Location chestLocation = chest.getLocation();
			UUID uuid = p.getUniqueId();
			
			if(!(canPlayerBreakChest(uuid, chestLocation))) {
				event.setCancelled(true);
				p.sendMessage(DeathChestUtils.nobreak);
				return;
				
			} else {
				event.setDropItems(false);
				deathSingleChestLocation.remove(uuid);
				deathDoubleChestLocation.remove(uuid);
				
				for (ItemStack item : chest.getBlockInventory().getContents()) {
		            if (item != null) {
		                block.getWorld().dropItemNaturally(chestLocation, item);
		            }
		        }
				p.sendMessage("Foi");	
			}
		}
	}

	private boolean canPlayerBreakChest(UUID uuid, Location chestLocation) {
		boolean isChestInList = deathSingleChestLocation.containsValue(chestLocation) || deathDoubleChestLocation.containsValue(chestLocation);

	    if (!isChestInList) {
	        // Se o baú não estiver em nenhuma das listas, permita que seja quebrado normalmente.
	        return true;
	    }

	    return (deathSingleChestLocation.containsKey(uuid) && deathSingleChestLocation.get(uuid).equals(chestLocation))
	            || (deathDoubleChestLocation.containsKey(uuid) && deathDoubleChestLocation.get(uuid).equals(chestLocation));
	}
	
	/*
	@EventHandler
	public void onClose(InventoryCloseEvent event) {
		Player p = (Player) event.getPlayer();

		if(event.getInventory().getType() == InventoryType.CHEST) {
			Chest chest = (Chest) event.getInventory();
			Location chestLocation = chest.getLocation();
			FileConfiguration customConfig = DeathChestConfig.getConfig();
			
			
			
			
			
			if(!(IsActivated.contains(p.getUniqueId()))) {
				return;
			} else {
				
			}

			
			
			if(customConfig.getString("ChestLocations." + p.getUniqueId().toString()) == null) {
				return;	
			} else {
				
			}
			
			
			
			
if (customConfig.getString("VanishedPlayers." + online.getUniqueId()) == null) {
			if(deathSingleChestLocation.containsKey(p.getUniqueId() + "") {
				
				customConfig.set("ChestLocations." + p.getUniqueId().toString() + ".Chest" + ".X", xLoc);
				
			}chestLocation.getX()) {
				
			}
			
		}
		
		
		if (event.getInventory().getHolder() instanceof Chest) {
			Chest chest = (Chest) event.getInventory().getHolder();
			Location chestLocation = chest.getLocation();

			if (deathSingleChestLocation.containsValue(chestLocation)
					|| (deathDoubleChestLocation.containsKey(p.getUniqueId())
							&& deathDoubleChestLocation.get(p.getUniqueId()).contains(chestLocation))) {
				Block deathChestBlock = chest.getBlock();
				Chest deathChest = (Chest) deathChestBlock.getState();
				Inventory chestInventory = deathChest.getInventory();

				boolean isEmpty = true;
				for (ItemStack item : chestInventory.getContents()) {
					if (item != null && !item.getType().equals(Material.AIR)) {
						isEmpty = false;
						break;
					}
				}

				if (isEmpty) {
					deathChestBlock.setType(Material.AIR);
					if (deathSingleChestLocation.containsValue(chestLocation)) {
						deathSingleChestLocation.values().remove(chestLocation);
					} else if (deathDoubleChestLocation.containsKey(p.getUniqueId())) {
						deathDoubleChestLocation.get(p.getUniqueId()).remove(chestLocation);
						if (deathDoubleChestLocation.get(p.getUniqueId()).isEmpty()) {
							deathDoubleChestLocation.remove(p.getUniqueId());
						}
					}
				}
			}
		}
	}
	*/

	private void summonChests(Player p, double x, double y, double z) {
		Location blockLocation1 = new Location(p.getWorld(), x, y, z);
		Block deathChestBlock1 = blockLocation1.getBlock();
		deathChestBlock1.setType(Material.CHEST);
		Chest deathChest1 = (Chest) deathChestBlock1.getState();
		Inventory chestInventory = deathChest1.getInventory();
		ItemStack[] items = player_items.get(p.getUniqueId());
		ItemStack[] itemsToTransfer = Arrays.copyOfRange(items, 0, Math.min(items.length, 27));
		chestInventory.setContents(itemsToTransfer);

		CharSequence xLoc = String.valueOf((int) x);
		CharSequence yLoc = String.valueOf((int) y);
		CharSequence zLoc = String.valueOf((int) z);
		int filledSlots = getFilledItemSlots(p);
		
		if (filledSlots <= 27) {
			player_items.remove(p.getUniqueId());
			createChestOnConfig(p, xLoc, yLoc, zLoc);
			p.sendMessage(DeathChestUtils.message.replace("%x%", xLoc).replace("%y%", yLoc).replace("%z%", zLoc));
			return;
		} else {
			Location blockLocation2 = new Location(p.getWorld(), x + 1, y, z);
			Block deathChestBlock2 = blockLocation2.getBlock();
			deathChestBlock2.setType(Material.CHEST);
			Chest deathChest2 = (Chest) deathChestBlock2.getState();
			Inventory chestInventory2 = deathChest2.getInventory();
			ItemStack[] itemsToTransfer2 = Arrays.copyOfRange(items, 27, items.length);
			chestInventory2.setContents(itemsToTransfer2);
			player_items.remove(p.getUniqueId());
			createChestOnConfig(p, xLoc, yLoc, zLoc);
			p.sendMessage(DeathChestUtils.message.replace("%x%", xLoc).replace("%y%", yLoc).replace("%z%", zLoc));
		}
	}

	private void createChestOnConfig(Player p, CharSequence xLoc, CharSequence yLoc, CharSequence zLoc) {
		FileConfiguration customConfig = DeathChestConfig.getConfig();
		customConfig.set("ChestLocations." + p.getUniqueId().toString() + ".Player", p.getName());
		customConfig.set("ChestLocations." + p.getUniqueId().toString() + ".Chest" + ".X", xLoc);
		customConfig.set("ChestLocations." + p.getUniqueId().toString() + ".Chest" + ".Y", yLoc);
		customConfig.set("ChestLocations." + p.getUniqueId().toString() + ".Chest" + ".Z", zLoc);
		DeathChestConfig.saveConfig();
	}
	
	
	/*
	private void summonChests(Player p, double x, double y, double z) {
		ItemStack[] items = player_items.get(p.getUniqueId());
		int filledSlots = getFilledItemSlots(p);

		if (filledSlots <= 27) {
			summonSingleChest(p, x, y, z);
			Location blockLocation1 = new Location(p.getWorld(), x, y, z);
			deathSingleChestLocation.put(p.getUniqueId(), blockLocation1);
			player_items.remove(p.getUniqueId());
			p.sendMessage(DeathChestUtils.message.replace("%x%", String.valueOf((int) x))
					.replace("%y%", String.valueOf((int) y)).replace("%z%", String.valueOf((int) z)));
			return;
		} else {
			summonSingleChest(p, x, y, z);
			Location blockLocation1 = new Location(p.getWorld(), x, y, z);
			Location blockLocation2 = new Location(p.getWorld(), x + 1, y, z);
			Block deathChestBlock2 = blockLocation2.getBlock();
			deathChestBlock2.setType(Material.CHEST);
			Chest deathChest2 = (Chest) deathChestBlock2.getState();
			Inventory chestInventory2 = deathChest2.getInventory();
			ItemStack[] itemsToTransfer2 = Arrays.copyOfRange(items, 27, items.length);
			chestInventory2.setContents(itemsToTransfer2);

			List<Location> locations = new ArrayList<>();
			locations.add(blockLocation1);
			locations.add(blockLocation2);
			deathDoubleChestLocation.put(p.getUniqueId(), locations);

			player_items.remove(p.getUniqueId());
			p.sendMessage(DeathChestUtils.message.replace("%x%", String.valueOf((int) x))
					.replace("%y%", String.valueOf((int) y)).replace("%z%", String.valueOf((int) z)));
		}
	}
	*/

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