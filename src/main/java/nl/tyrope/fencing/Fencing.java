package nl.tyrope.fencing;

import ic2.api.item.IC2Items;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import nl.tyrope.fencing.Refs.MetaValues;
import nl.tyrope.fencing.blocks.ElectricFenceBlock;
import nl.tyrope.fencing.blocks.FenceBlock;
import nl.tyrope.fencing.blocks.PaintedFenceBlock;
import nl.tyrope.fencing.creativetab.FencingTab;
import nl.tyrope.fencing.items.ElectricFenceBlockItem;
import nl.tyrope.fencing.items.FenceBlockItem;
import nl.tyrope.fencing.items.FencePoleItem;
import nl.tyrope.fencing.items.PaintedFenceBlockItem;
import nl.tyrope.fencing.proxy.Common;
import nl.tyrope.fencing.tileEntity.ElectricFenceEntity;
import nl.tyrope.fencing.util.BarbedDmg;
import nl.tyrope.fencing.util.ElecDmg;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = "fencing", name = "Fencing", version = "0.5.0-dev", dependencies = "after:IC2")
public class Fencing {

	@Instance(value = "fencing")
	public static Fencing instance;

	@SidedProxy(clientSide = "nl.tyrope.fencing.proxy.Client", serverSide = "nl.tyrope.fencing.proxy.Common")
	public static Common proxy;

	private FencePoleItem fencePole;
	private FenceBlock fenceBlock;
	private ElectricFenceBlock electricFenceBlock;
	private PaintedFenceBlock paintedFenceBlock;

	/**
	 * This is code that is executed prior to your mod being initialized into of
	 * Minecraft.
	 * 
	 * @param event
	 *            The Forge ModLoader pre-initialization event
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {

		// Logger
		Refs.logger = event.getModLog();
		Refs.logger.info("Logger created.");

		// Create creative tab.
		Refs.creativeTab = new FencingTab();

		// Load configuration file.
		Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		config.load();

		// Fetch values.
		Refs.dmgMulti = config.get(
				"misc",
				"damage_multiplier",
				1,
				"Multiplier for fence-inflicted damage."
						+ System.getProperty("line.separator")
						+ "Default: (1x)"
						+ System.getProperty("line.separator")
						+ "0.5hearts for barbed,"
						+ System.getProperty("line.separator")
						+ "max. 1 heart for tin &"
						+ System.getProperty("line.separator")
						+ "max. 2 hearts for copper.").getInt();
		Refs.dropCenter = config.get(
				"misc",
				"drop_on_cut",
				true,
				"Whether or not to drop the center"
						+ System.getProperty("line.separator")
						+ "item when cutting a fence.").getBoolean(true);

		// Save them in case they weren't set before.
		config.save();

		// Make items and blocks
		fencePole = new FencePoleItem();
		Refs.ItemsBlocks.Pole = fencePole;

		fenceBlock = new FenceBlock();
		Refs.ItemsBlocks.Fence = fenceBlock;

		paintedFenceBlock = new PaintedFenceBlock();
		Refs.ItemsBlocks.PaintedFence = paintedFenceBlock;

		// register blocks
		GameRegistry.registerBlock(fenceBlock, FenceBlockItem.class,
				"FenceBlockItem");
		GameRegistry.registerBlock(paintedFenceBlock,
				PaintedFenceBlockItem.class, "PaintedFenceBlockItem");

		// Make damage objects.
		Refs.DmgSrcs.barbed = new BarbedDmg();

		// Intermod compatibility.
		if (Loader.isModLoaded("IC2")) {
			Refs.logger
					.info("IndustrialCraft 2 detected, Registering electric fences.");
			// Make block
			electricFenceBlock = new ElectricFenceBlock();
			Refs.ItemsBlocks.ElecFence = electricFenceBlock;

			// register block & entity
			GameRegistry.registerBlock(electricFenceBlock,
					ElectricFenceBlockItem.class, "ElectricFenceBlockItem");
			GameRegistry.registerTileEntity(ElectricFenceEntity.class,
					"ElectricFenceEntity");

			// Register damage source
			Refs.DmgSrcs.electric = new ElecDmg();
		}
	}

	/**
	 * This is code that is executed when your mod is being initialized in
	 * Minecraft.
	 * 
	 * @param event
	 *            The Forge ModLoader initialization event
	 */
	@EventHandler
	public void init(FMLInitializationEvent event) {
		GameRegistry.addRecipe(new ItemStack(fencePole, 4), "xx", "xx", "xx",
				'x', new ItemStack(Items.stick));

		ItemStack pole = new ItemStack(fencePole);

		// Regular pole/X/pole crafting
		GameRegistry.addRecipe(new ItemStack(fenceBlock, 1,
				MetaValues.FenceString), "xyx", 'x', pole, 'y', new ItemStack(
				Items.string));
		GameRegistry.addRecipe(new ItemStack(fenceBlock, 1,
				MetaValues.FenceIron), "xyx", 'x', pole, 'y', new ItemStack(
				Items.iron_ingot));
		GameRegistry.addShapelessRecipe(new ItemStack(fenceBlock, 1,
				MetaValues.FenceSilly), new ItemStack(Items.slime_ball),
				new ItemStack(fenceBlock));
		GameRegistry.addRecipe(new ItemStack(fenceBlock, 1,
				MetaValues.FenceBarbed), "xyx", 'x', pole, 'y', new ItemStack(
				Blocks.iron_bars));
		GameRegistry.addRecipe(new ItemStack(fenceBlock, 1,
				MetaValues.FenceWood), "xyx", 'x', pole, 'y', new ItemStack(
				Items.stick));

		// Repairs
		GameRegistry.addShapelessRecipe(new ItemStack(fenceBlock, 1,
				MetaValues.FenceString), new ItemStack(fenceBlock, 1,
				MetaValues.FenceCut), new ItemStack(Items.string));
		GameRegistry.addShapelessRecipe(new ItemStack(fenceBlock, 1,
				MetaValues.FenceIron), new ItemStack(fenceBlock, 1,
				MetaValues.FenceCut), new ItemStack(Items.iron_ingot));
		GameRegistry.addShapelessRecipe(new ItemStack(fenceBlock, 1,
				MetaValues.FenceBarbed), new ItemStack(fenceBlock, 1,
				MetaValues.FenceCut), new ItemStack(Blocks.iron_bars));
		GameRegistry.addShapelessRecipe(new ItemStack(fenceBlock, 1,
				MetaValues.FenceWood), new ItemStack(fenceBlock, 1,
				MetaValues.FenceCut), new ItemStack(Items.stick));

		for (int i = 0; i < 16; i++) {
			GameRegistry.addShapelessRecipe(new ItemStack(paintedFenceBlock, 1,
					i), new ItemStack(fenceBlock, 1, MetaValues.FenceWood),
					new ItemStack(Items.dye, 1, 15 - i));
		}

		// Intermod compatibility.
		if (Loader.isModLoaded("IC2")) {
			Refs.logger.info("Loading electric fence recipes:");

			// Load items
			ItemStack cableTin = IC2Items.getItem("tinCableItem");
			if (cableTin != null) {
				Refs.logger.info("Tin loaded.");
				GameRegistry.addRecipe(new ItemStack(electricFenceBlock, 1,
						MetaValues.FenceElectricTin), "xyx", 'x', pole, 'y',
						cableTin);
				// Repair
				GameRegistry.addShapelessRecipe(new ItemStack(
						electricFenceBlock, 1, MetaValues.FenceElectricTin),
						new ItemStack(fenceBlock, 1, MetaValues.FenceCut),
						cableTin);
			}
			ItemStack cableCopper = IC2Items.getItem("copperCableItem");
			if (cableCopper != null) {
				Refs.logger.info("Copper loaded.");
				GameRegistry.addRecipe(new ItemStack(electricFenceBlock, 1,
						MetaValues.FenceElectricCopper), "xyx", 'x', pole, 'y',
						cableCopper);
				// Repair
				GameRegistry.addShapelessRecipe(new ItemStack(
						electricFenceBlock, 1, MetaValues.FenceElectricCopper),
						new ItemStack(fenceBlock, 1, MetaValues.FenceCut),
						cableCopper);
			}
			Refs.logger.info("Complete.");

			proxy.registerRenderers(new FenceBlock[] { fenceBlock,
					paintedFenceBlock, electricFenceBlock });
		} else {
			proxy.registerRenderers(new FenceBlock[] { fenceBlock,
					paintedFenceBlock });
		}
	}

	/**
	 * This is code that is executed after all mods are initialized in Minecraft
	 * 
	 * @param event
	 *            The Forge ModLoader post-initialization event
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}
