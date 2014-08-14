/*
 Fencing - A minecraft mod that adds pretty fences to your world!
Copyright (C)2013-2014 Dimitri "Tyrope" Molenaars

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>. 
*/

package nl.tyrope.fencing;

import net.minecraftforge.common.config.Configuration;
import nl.tyrope.fencing.proxy.Common;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

//TODO Depend on after:Thermal Expansion, not IndustrialCraft.
@Mod(modid = "fencing", name = "Fencing", version = "0.6.0-dev", dependencies = "after:IC2")
public class Fencing {

	@Instance(value = "fencing")
	public static Fencing instance;

	@SidedProxy(clientSide = "nl.tyrope.fencing.proxy.Client", serverSide = "nl.tyrope.fencing.proxy.Common")
	public static Common proxy;

	/**
	 * This is code that is executed prior to your mod being initialized.
	 * 
	 * @param event
	 *            The Forge ModLoader pre-initialization event.
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Refs.logger = event.getModLog();
		Refs.logger.info("Logger created.");

		proxy.loadConfigs(new Configuration(event
				.getSuggestedConfigurationFile()));

		proxy.registerItems();
		proxy.registerBlocks();
		proxy.registerRenderers();
		proxy.registerDamageSources();
	}

	/**
	 * This is code that is executed when your mod is being initialized.
	 * 
	 * @param event
	 *            The Forge ModLoader initialization event.
	 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerRecipes();
	}

	/**
	 * This is code that is executed after all mods are initialized.
	 * 
	 * @param event
	 *            The Forge ModLoader post-initialization event.
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}
