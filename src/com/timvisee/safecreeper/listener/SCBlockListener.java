package com.timvisee.safecreeper.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.timvisee.safecreeper.SafeCreeper;

public class SCBlockListener implements Listener {
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Block b = event.getBlock();
		Material bt = b.getType();
		Location l = b.getLocation();
		World w = event.getBlock().getWorld();
		
		switch(bt) {
		case WATER:
		case STATIONARY_WATER:
			// Could a player place water
			if(!hasBypassPermission(event.getPlayer(), "WaterControl", "CanPlaceWater", false))
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WaterControl", "CanPlaceWater", true, true, l))
					event.setCancelled(true);
			break;
			
		case LAVA:
		case STATIONARY_LAVA:
			// Could a player place lava
			if(!hasBypassPermission(event.getPlayer(), "LavaControl", "CanPlaceLava", false))
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "LavaControl", "CanPlaceLava", true, true, l))
					event.setCancelled(true);
			break;
			
		case TNT:
			// Could a player place a TNT block
			if(!hasBypassPermission(event.getPlayer(), "TNTControl", "CanPlaceTNT", false))
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "TNTControl", "CanPlaceTNT", true, true, l))
					event.setCancelled(true);
			break;
			
		default:
			break;
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Block b = event.getBlock();
		Material bt = b.getType();
		Location l = b.getLocation();
		World w = event.getBlock().getWorld();
		
		switch(bt) {
		case TNT:
			// Could a player break a TNT block
			if(!hasBypassPermission(event.getPlayer(), "TNTControl", "CanBreakTNT", false))
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "TNTControl", "CanBreakTNT", true, true, l))
					event.setCancelled(true);
			break;
			
		default:
			break;
		}
	}

	@EventHandler
	public void onBlockBurn(BlockBurnEvent event) {
		Block b = event.getBlock();
		Location l = b.getLocation();
		World w = b.getWorld();
		
		// Could fire burn/break blocks
		if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "FireControl", "EnableBlockFire", true, true, l))
			event.setCancelled(true);
	}

	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		Block b = event.getBlock();
		Location l = b.getLocation();
		World w = b.getWorld();
		
		switch (event.getCause()) {
		case FIREBALL:
			// Could a player use flint and steel to ignite blocks
			if(event.getPlayer() == null)
				break;
			
			if(!hasBypassPermission(event.getPlayer(), "FirechargeControl", "EnableFireCharge", false))
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "FirechargeControl", "EnableFireCharge", true, true, l))
					event.setCancelled(true);
			break;
			
		case FLINT_AND_STEEL:
			// Could a player use flint and steel to ignite blocks
			if(event.getPlayer() == null)
				break;
			
			if(!hasBypassPermission(event.getPlayer(), "FlintAndSteelControl", "EnableFlintAndSteel", false))
				if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "FlintAndSteelControl", "EnableFlintAndSteel", true, true, l))
					event.setCancelled(true);
			break;
			
		case LAVA:
			// Could lava cause fire
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "LavaControl", "LavaFire", true, true, l))
				event.setCancelled(true);
			break;
			
		case LIGHTNING:
			// Could lightning ignite blocks
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "LightningControl", "IgniteBlocks", true, true, l))
				event.setCancelled(true);
			break;
			
		case SPREAD:
			// Could fire spread out
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "FireControl", "FireSpread", true, true, l))
				event.setCancelled(true);
			break;
		}
	}

	@EventHandler
	public void onBlockFromTo(BlockFromToEvent event) {
		Block b = event.getBlock();
		Location l = b.getLocation();
		World w = b.getWorld();
		
		switch(b.getType()) {
		case WATER:
		case STATIONARY_WATER:
			// This is water, check if water can spread
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "WaterControl", "WaterSpread", true, true, l))
				event.setCancelled(true);
			break;
			
		case LAVA:
		case STATIONARY_LAVA:
			// This is lava, check if lava can spread
			if(!SafeCreeper.instance.getConfigManager().getOptionBoolean(w, "LavaControl", "LavaSpread", true, true, l))
				event.setCancelled(true);
			break;
		
		default:
			break;
		}
	}
	
	public boolean hasBypassPermission(Player player, String controlName, String bypassName, boolean def) {
		if(!SafeCreeper.instance.getPermissionsManager().isEnabled())
			return def;
		return SafeCreeper.instance.getPermissionsManager().hasPermission(player, "safecreeper.bypass." + controlName + "." + bypassName, def);
	}
}