package nl.tyrope.fencing;

import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;

public class Refs {
	// Fence subnames
	public final static String[] fenceSubNames = { "String", "Iron", "Silly",
			"Barbed", "Wood", "Cut" };
	public final static String[] elecFenceSubNames = { "Tin", "Copper" };

	// Misc
	public static CreativeTabs creativeTab;
	public static float textureSize = 32;
	public static boolean dropCenter;
	public static Logger logger;

	// Items & Blocks
	public static class ItemsBlocks {
		public static Item Pole;
		public static Block Fence;
		public static Block ElecFence;
		public static Block PaintedFence;
	}

	// Damage
	public static class DmgSrcs {
		public static DamageSource barbed;
		public static DamageSource electric;
	}

	public static int dmgMulti;

	// Fence metadata
	public static class MetaValues {
		public static final int FenceString = 0;
		public static final int FenceIron = 1;
		public static final int FenceSilly = 2;
		public static final int FenceBarbed = 3;
		public static final int FenceWood = 4;
		public static final int FenceCut = 5;

		public static final int FenceElectricTin = 0;
		public static final int FenceElectricCopper = 1;
	}
}
