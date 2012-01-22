package org.beide.tntdispenser;

import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;

/*
 * This file is part of tntDispenser.
 * 
 * tntDispenser is free software: you can redistribute it and/or modify *
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * tntDispenser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with tntDispenser.  If not, see <http://www.gnu.org/licenses/>.
 */

public class TntDispenserEventHandler extends BlockListener {
	
	private static Logger log = Logger.getLogger("Minecraft");
	FileConfiguration config;
	JavaPlugin plugin;
	
	TntDispenserEventHandler(JavaPlugin p, FileConfiguration c) {
		super();
		plugin = p;
		config = c;
	}
	
	public void onBlockDispense(BlockDispenseEvent event) {
		
		Block dispenser = event.getBlock();
		if(!config.contains(dispenser.getWorld().getName() + ".enabled")) {
			config.set(dispenser.getWorld().getName() + ".enabled", false);
			plugin.saveConfig();
		}
		if(config.getBoolean(dispenser.getWorld().getName() + ".enabled")) {
			if((dispenser.getType() == Material.DISPENSER) && (event.getItem().getType() == Material.TNT) && !event.isCancelled()) {
				Block blockToChange;
				switch(dispenser.getData()) {
					case 2:
						//North
						blockToChange = dispenser.getRelative(BlockFace.EAST);
						break;
					case 3:
						//South
						blockToChange = dispenser.getRelative(BlockFace.WEST);
						break;
					case 4:
						//West
						blockToChange = dispenser.getRelative(BlockFace.NORTH);
						break;
					case 5:
						//East
						blockToChange = dispenser.getRelative(BlockFace.SOUTH);
						break;
					default:
						log.info("Direction not found: " + dispenser.getData());
						blockToChange = dispenser.getRelative(BlockFace.UP);
				}
				event.setCancelled(true);
				if(blockToChange.isEmpty() || blockToChange.isLiquid()) {
					blockToChange.setType(Material.TNT);
				}
			}
		}
	}
}
