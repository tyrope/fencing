package nl.tyrope.fencing.proxy;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.Refs.MetaValues;
import nl.tyrope.fencing.blocks.ElectricFenceBlock;
import nl.tyrope.fencing.blocks.FenceBlock;
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
				"xx", "xx", "xx", 'x', new ItemStack(Items.stick));

		ItemStack pole = new ItemStack(Refs.ItemsBlocks.Pole);

		// Regular pole/X/pole crafting
		GameRegistry.addRecipe(new ItemStack(Refs.ItemsBlocks.Fence, 1,
				MetaValues.FenceString), "xyx", 'x', pole, 'y', new ItemStack(
				Items.string));
		GameRegistry.addRecipe(new ItemStack(Refs.ItemsBlocks.Fence, 1,
				MetaValues.FenceIron), "xyx", 'x', pole, 'y', new ItemStack(
				Items.iron_ingot));
		GameRegistry.addShapelessRecipe(new ItemStack(Refs.ItemsBlocks.Fence, 1,
				MetaValues.FenceSilly), new ItemStack(Items.slime_ball),
				new ItemStack(Refs.ItemsBlocks.Fence));
		GameRegistry.addRecipe(new ItemStack(Refs.ItemsBlocks.Fence, 1,
				MetaValues.FenceBarbed), "xyx", 'x', pole, 'y', new ItemStack(
				Blocks.iron_bars));
		GameRegistry.addRecipe(new ItemStack(Refs.ItemsBlocks.Fence, 1,
				MetaValues.FenceWood), "xyx", 'x', pole, 'y', new ItemStack(
				Items.stick));

		// Repairs
		GameRegistry.addShapelessRecipe(new ItemStack(Refs.ItemsBlocks.Fence, 1,
				MetaValues.FenceString), new ItemStack(Refs.ItemsBlocks.Fence, 1,
				MetaValues.FenceCut), new ItemStack(Items.string));
		GameRegistry.addShapelessRecipe(new ItemStack(Refs.ItemsBlocks.Fence, 1,
				MetaValues.FenceIron), new ItemStack(Refs.ItemsBlocks.Fence, 1,
				MetaValues.FenceCut), new ItemStack(Items.iron_ingot));
		GameRegistry.addShapelessRecipe(new ItemStack(Refs.ItemsBlocks.Fence, 1,
				MetaValues.FenceBarbed), new ItemStack(Refs.ItemsBlocks.Fence, 1,
				MetaValues.FenceCut), new ItemStack(Blocks.iron_bars));
		GameRegistry.addShapelessRecipe(new ItemStack(Refs.ItemsBlocks.Fence, 1,
				MetaValues.FenceWood), new ItemStack(Refs.ItemsBlocks.Fence, 1,
				MetaValues.FenceCut), new ItemStack(Items.stick));

		for (int i = 0; i < 16; i++) {
			GameRegistry.addShapelessRecipe(new ItemStack(Refs.ItemsBlocks.PaintedFence, 1,
					i), new ItemStack(Refs.ItemsBlocks.Fence, 1, MetaValues.FenceWood),
					new ItemStack(Items.dye, 1, 15 - i));
		}

		// Intermod compatibility.
		if (Loader.isModLoaded("IC2")) {
			Refs.logger.info("Loading electric fence recipes:");
			
			ItemStack tinIngot = null, copperIngot = null; 

			// Tin
			Refs.logger.info("Tin loaded.");
			GameRegistry.addRecipe(new ItemStack(Refs.ItemsBlocks.ElecFence, 1,
					MetaValues.FenceElectricTin), "xyx", 'x', pole, 'y',
					tinIngot);
			// Repair
			GameRegistry.addShapelessRecipe(new ItemStack(
					Refs.ItemsBlocks.ElecFence, 1, MetaValues.FenceElectricTin),
						new ItemStack(Refs.ItemsBlocks.Fence, 1, MetaValues.FenceCut),
						tinIngot);
			
			// Copper
			Refs.logger.info("Copper loaded.");
				GameRegistry.addRecipe(new ItemStack(Refs.ItemsBlocks.ElecFence, 1,
						MetaValues.FenceElectricCopper), "xyx", 'x', pole, 'y',
						copperIngot);
			// Repair
			GameRegistry.addShapelessRecipe(new ItemStack(
					Refs.ItemsBlocks.ElecFence, 1, MetaValues.FenceElectricCopper),
					new ItemStack(Refs.ItemsBlocks.Fence, 1, MetaValues.FenceCut),
					copperIngot);
			
			Refs.logger.info("Complete.");
		}
	}
}