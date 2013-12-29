package nl.tyrope.fencing;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import nl.tyrope.fencing.blocks.*;
import nl.tyrope.fencing.items.*;
import nl.tyrope.fencing.proxy.Common;
import nl.tyrope.fencing.tileEntities.*;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraftforge.common.Configuration;

@Mod(modid = Refs.MODID, version = Refs.VERSION)
@NetworkMod(clientSideRequired = true)
public class Fencing {

	@Instance(value = Refs.MODID)
	public static Fencing instance;

	@SidedProxy(clientSide = "nl.tyrope.fencing.proxy.Client", serverSide = "nl.tyrope.fencing.proxy.Common")
	public static Common proxy;

	public FencePoleItem fencePole;
	public Block fenceBlock;

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
		int fencePoleID = config.get(Configuration.CATEGORY_ITEM, "fencePole",
				5000).getInt();
		int fenceBlockID = config.get(Configuration.CATEGORY_BLOCK,
				"fenceBlock", 500).getInt();
		config.save();
		fencePole = new FencePoleItem(fencePoleID);
		fenceBlock = new FenceBlock(fenceBlockID);
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
		LanguageRegistry.addName(fencePole, "Fence Pole");
		GameRegistry.addRecipe(new ItemStack(fencePole, 4), "xx", "xx", "xx",
				'x', new ItemStack(Item.stick));

		MinecraftForge.setBlockHarvestLevel(fenceBlock, "axe", 0);

		// register block & TE
		GameRegistry.registerBlock(fenceBlock, "FenceBlockItem");
		GameRegistry.registerTileEntity(FenceEntity.class, "fenceBlock");

		// Add names
		final String[] fenceNames = { "String Fence", "Iron Fence",
				"Silly String Fence", "Barbed Wire Fence" };
		for (int ix = 0; ix < 4; ix++) {
			ItemStack fenceStack = new ItemStack(fenceBlock, 1, ix);
			LanguageRegistry.addName(fenceStack,
					fenceNames[fenceStack.getItemDamage()]);
		}

		GameRegistry.addRecipe(new ItemStack(fenceBlock, 1, 0), "xyx", 'x',
				new ItemStack(fencePole), 'y', new ItemStack(Item.silk));
		GameRegistry.addRecipe(new ItemStack(fenceBlock, 1, 1), "xyx", 'x',
				new ItemStack(fencePole), 'y', new ItemStack(Item.ingotIron));
		GameRegistry.addShapelessRecipe(new ItemStack(fenceBlock, 1, 2),
				new ItemStack(Item.slimeBall), new ItemStack(fenceBlock));
		GameRegistry.addRecipe(new ItemStack(fenceBlock, 1, 3), "xyx", 'x',
				new ItemStack(fencePole), 'y', new ItemStack(Block.fenceIron));

		proxy.registerRenderers();
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
