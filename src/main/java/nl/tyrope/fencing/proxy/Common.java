package nl.tyrope.fencing.proxy;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.Refs.MetaValues;
import nl.tyrope.fencing.blocks.ElectricFenceBlock;
import nl.tyrope.fencing.blocks.FenceBlock;
import nl.tyrope.fencing.blocks.FenceTopBlock;
import nl.tyrope.fencing.blocks.PaintedFenceBlock;
import nl.tyrope.fencing.items.ElectricFenceBlockItem;
import nl.tyrope.fencing.items.FenceBlockItem;
import nl.tyrope.fencing.items.FencePoleItem;
import nl.tyrope.fencing.items.PaintedFenceBlockItem;
import nl.tyrope.fencing.tileEntity.ElectricFenceEntity;
import nl.tyrope.fencing.util.BarbedDmg;
import nl.tyrope.fencing.util.ElecDmg;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class Common {
	public void loadConfigs(Configuration config) {
		config.load();

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

		config.save();
	}

	public void registerRenderers() {
	}

	public void registerItems() {
		GameRegistry.registerItem(Refs.ItemsBlocks.Pole = new FencePoleItem(), "fencePole");
	}

	public void registerBlocks() {
		GameRegistry.registerBlock(Refs.ItemsBlocks.Fence = new FenceBlock(), FenceBlockItem.class, "FenceBlockItem");
		GameRegistry.registerBlock(Refs.ItemsBlocks.FenceTop = new FenceTopBlock(), "FenceTopBlock");
		GameRegistry.registerBlock(Refs.ItemsBlocks.PaintedFence = new PaintedFenceBlock(), PaintedFenceBlockItem.class, "PaintedFenceBlockItem");

		if (Loader.isModLoaded("IC2")) {
			GameRegistry.registerBlock(Refs.ItemsBlocks.ElecFence = new ElectricFenceBlock(), ElectricFenceBlockItem.class, "ElectricFenceBlockItem");
		}
	}

	public void registerTileEntities() {
		if (Loader.isModLoaded("IC2")) {
			GameRegistry.registerTileEntity(ElectricFenceEntity.class, "ElectricFenceEntity");
		}
	}

	public void registerDamageSources() {
		Refs.DmgSrcs.barbed = new BarbedDmg();

		if (Loader.isModLoaded("IC2")) {
			Refs.DmgSrcs.electric = new ElecDmg();
		}
	}

	public void registerRecipes() {
		// Items Recipes
		GameRegistry.addRecipe(new ItemStack(Refs.ItemsBlocks.Pole, 4),
				"xx", "xx", "xx", 'x', Items.stick);

		ItemStack fenceString = new ItemStack(Refs.ItemsBlocks.Fence, 1, MetaValues.FenceString);
		ItemStack fenceIron = new ItemStack(Refs.ItemsBlocks.Fence, 1, MetaValues.FenceIron);
		ItemStack fenceSilly = new ItemStack(Refs.ItemsBlocks.Fence, 1, MetaValues.FenceSilly);
		ItemStack fenceBarbed = new ItemStack(Refs.ItemsBlocks.Fence, 1, MetaValues.FenceBarbed);
		ItemStack fenceWood = new ItemStack(Refs.ItemsBlocks.Fence, 1, MetaValues.FenceWood);
		ItemStack fenceCut = new ItemStack(Refs.ItemsBlocks.Fence, 1, MetaValues.FenceCut);

		// Regular pole/X/pole crafting
		GameRegistry.addRecipe(fenceString, "xyx", 'x', Refs.ItemsBlocks.Pole, 'y', Items.string);
		GameRegistry.addRecipe(fenceIron, "xyx", 'x', Refs.ItemsBlocks.Pole, 'y', Items.iron_ingot);
		GameRegistry.addShapelessRecipe(fenceSilly, Items.slime_ball, fenceString);
		GameRegistry.addRecipe(fenceBarbed, "xyx", 'x', Refs.ItemsBlocks.Pole, 'y', Blocks.iron_bars);
		GameRegistry.addRecipe(fenceWood, "xyx", 'x', Refs.ItemsBlocks.Pole, 'y', Items.stick);

		// Repairs
		GameRegistry.addShapelessRecipe(fenceString, fenceCut, Items.string);
		GameRegistry.addShapelessRecipe(fenceIron, fenceCut, Items.iron_ingot);
		GameRegistry.addShapelessRecipe(fenceBarbed, fenceCut, Blocks.iron_bars);
		GameRegistry.addShapelessRecipe(fenceWood, fenceCut, Items.stick);

		for (int i = 0; i < 16; i++) {
			GameRegistry.addShapelessRecipe(new ItemStack(Refs.ItemsBlocks.PaintedFence, 1,
					i), fenceWood, new ItemStack(Items.dye, 1, 15 - i));
		}

		// Intermod compatibility.
		if (Loader.isModLoaded("IC2")) {
			Refs.logger.info("Loading electric fence recipes :");

			// TODO : Create tinIngot and copperIngot items & register them in OreDictionary
			ItemStack tinIngot = null, copperIngot = null;

			ItemStack fenceTin = new ItemStack(Refs.ItemsBlocks.ElecFence, 1, MetaValues.FenceElectricTin);
			ItemStack fenceCopper = new ItemStack(Refs.ItemsBlocks.ElecFence, 1, MetaValues.FenceElectricCopper);

			// Tin
			Refs.logger.info("Tin loaded.");
			GameRegistry.addRecipe(fenceTin, "xyx", 'x', Refs.ItemsBlocks.Pole, 'y', tinIngot);

			// Repair
			GameRegistry.addShapelessRecipe(fenceTin, fenceCut, tinIngot);
			
			// Copper
			Refs.logger.info("Copper loaded.");
			GameRegistry.addRecipe(fenceCopper, "xyx", 'x', Refs.ItemsBlocks.Pole, 'y', copperIngot);

			// Repair
			GameRegistry.addShapelessRecipe(fenceCopper, fenceCut, copperIngot);
			
			Refs.logger.info("Loading electric fence recipes : Complete.");
		}
	}
}