package nl.tyrope.fencing;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import nl.tyrope.fencing.Refs.MetaValues;
import nl.tyrope.fencing.blocks.BlockPaneEx;
import nl.tyrope.fencing.blocks.FenceBlock;
import nl.tyrope.fencing.items.FenceBlockItem;
import nl.tyrope.fencing.items.FencePoleItem;
import nl.tyrope.fencing.proxy.Common;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = Refs.MODID, version = Refs.VERSION)
@NetworkMod(clientSideRequired = true)
public class Fencing {

	@Instance(value = Refs.MODID)
	public static Fencing instance;

	@SidedProxy(clientSide = "nl.tyrope.fencing.proxy.Client", serverSide = "nl.tyrope.fencing.proxy.Common")
	public static Common proxy;

	public FencePoleItem fencePole;
	public FenceBlock fenceBlock;

	/**
	 * This is code that is executed prior to your mod being initialized into of
	 * Minecraft.
	 * 
	 * @param event
	 *            The Forge ModLoader pre-initialization event
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configuration config = new Configuration(
				event.getSuggestedConfigurationFile());
		config.load();
		Refs.PoleID = config
				.get(Configuration.CATEGORY_ITEM, "fencePole", 5000).getInt();
		Refs.FenceID = config.get(Configuration.CATEGORY_BLOCK, "fenceBlock",
				500).getInt();
		config.save();
		fencePole = new FencePoleItem(Refs.PoleID);
		fenceBlock = new FenceBlock(Refs.FenceID);

		// This might potentially break other mods...
		int replaceID = Block.fenceIron.blockID;
		Block.blocksList[replaceID] = null;
		Block.blocksList[replaceID] = new BlockPaneEx(replaceID, "iron_bars",
				"iron_bars", Material.iron, true);
		replaceID = Block.thinGlass.blockID;
		Block.blocksList[replaceID] = null;
		Block.blocksList[replaceID] = new BlockPaneEx(Block.thinGlass.blockID,
				"glass", "glass_pane_top", Material.glass, false);
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
				'x', new ItemStack(Item.stick));

		MinecraftForge.setBlockHarvestLevel(fenceBlock, "axe", 0);

		// register block
		GameRegistry.registerBlock(fenceBlock, FenceBlockItem.class,
				"FenceBlockItem");

		GameRegistry.addRecipe(new ItemStack(fenceBlock, 1, 0), "xyx", 'x',
				new ItemStack(fencePole), 'y', new ItemStack(Item.silk));
		GameRegistry.addRecipe(new ItemStack(fenceBlock, 1,
				MetaValues.FenceIron), "xyx", 'x', new ItemStack(fencePole),
				'y', new ItemStack(Item.ingotIron));
		GameRegistry.addShapelessRecipe(new ItemStack(fenceBlock, 1,
				MetaValues.FenceSilly), new ItemStack(Item.slimeBall),
				new ItemStack(fenceBlock));
		GameRegistry.addRecipe(new ItemStack(fenceBlock, 1,
				MetaValues.FenceBarbed), "xyx", 'x', new ItemStack(fencePole),
				'y', new ItemStack(Block.fenceIron));

		proxy.registerRenderers(fenceBlock);
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
