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
	public StringFenceBlock stringFence;
	public IronFenceBlock ironFence;
	public BarbedFenceBlock barbedFence;
	public SillyFenceBlock sillyFence;

	private int fencePoleID, stringFenceID, ironFenceID, sillyFenceID,
			barbedFenceID;

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
		fencePoleID = config
				.get(Configuration.CATEGORY_ITEM, "fencePole", 5000).getInt();
		stringFenceID = config.get(Configuration.CATEGORY_BLOCK, "stringFence",
				500).getInt();
		ironFenceID = config
				.get(Configuration.CATEGORY_BLOCK, "ironFence", 501).getInt();
		sillyFenceID = config.get(Configuration.CATEGORY_BLOCK, "sillyFence",
				502).getInt();
		barbedFenceID = config.get(Configuration.CATEGORY_BLOCK, "barbedFence",
				503).getInt();
		config.save();
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
		fencePole = new FencePoleItem(fencePoleID);
		LanguageRegistry.addName(fencePole, "Fence Pole");
		GameRegistry.addRecipe(new ItemStack(fencePole), "xx", "xx", "xx", 'x',
				new ItemStack(Item.stick));

		stringFence = new StringFenceBlock(stringFenceID);
		MinecraftForge.setBlockHarvestLevel(stringFence, "axe", 0);
		GameRegistry.registerBlock(stringFence, "stringFence");
		LanguageRegistry.addName(stringFence, "String Fence");
		GameRegistry.addRecipe(new ItemStack(stringFence), "xyx", 'x',
				new ItemStack(fencePole), 'y', new ItemStack(Item.silk));
		GameRegistry.registerTileEntity(StringFenceEntity.class, "stringFence");

		ironFence = new IronFenceBlock(ironFenceID);
		MinecraftForge.setBlockHarvestLevel(ironFence, "pickaxe", 0);
		GameRegistry.registerBlock(ironFence, "ironFence");
		LanguageRegistry.addName(ironFence, "Iron Fence");
		GameRegistry.addRecipe(new ItemStack(ironFence), "xyx", 'x',
				new ItemStack(fencePole), 'y', new ItemStack(Item.ingotIron));
		GameRegistry.registerTileEntity(IronFenceEntity.class, "ironFence");

		sillyFence = new SillyFenceBlock(sillyFenceID);
		MinecraftForge.setBlockHarvestLevel(sillyFence, "axe", 0);
		GameRegistry.registerBlock(sillyFence, "sillyFence");
		LanguageRegistry.addName(sillyFence, "Silly String Fence");
		GameRegistry.addShapelessRecipe(new ItemStack(sillyFence),
				new ItemStack(Item.slimeBall), new ItemStack(stringFence));
		GameRegistry.registerTileEntity(SillyFenceEntity.class, "sillyFence");

		barbedFence = new BarbedFenceBlock(barbedFenceID);
		MinecraftForge.setBlockHarvestLevel(barbedFence, "pickaxe", 0);
		GameRegistry.registerBlock(barbedFence, "barbedFence");
		LanguageRegistry.addName(barbedFence, "Barbed Wire Fence");
		GameRegistry.addRecipe(new ItemStack(barbedFence), "xyx", 'x',
				new ItemStack(fencePole), 'y', new ItemStack(Block.fenceIron));
		GameRegistry.registerTileEntity(BarbedFenceEntity.class, "barbedFence");

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
