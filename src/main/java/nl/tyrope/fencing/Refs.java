package nl.tyrope.fencing;

import net.minecraft.item.Item;
import net.minecraft.util.DamageSource;
import nl.tyrope.fencing.blocks.ElectricFenceBlock;
import nl.tyrope.fencing.blocks.FenceBlock;
import nl.tyrope.fencing.blocks.FenceTopBlock;
import nl.tyrope.fencing.blocks.PaintedFenceBlock;

import org.apache.logging.log4j.Logger;

public class Refs {
	// Fence subnames
	public final static String[] fenceSubNames = { "String", "Iron", "Silly",
			"Barbed", "Wood", "Cut" };
	public final static String[] elecFenceSubNames = { "Tin", "Copper" };

	// Misc
	public static float textureSize = 32;
	/** Whether or not to drop the center item when cutting a fence. */
	public static boolean dropCenter;
	public static Logger logger;

	// Items & Blocks
	public static class ItemsBlocks {
		public static Item Pole;
		public static FenceBlock Fence;
		public static FenceTopBlock FenceTop;
		public static ElectricFenceBlock ElecFence;
		public static PaintedFenceBlock PaintedFence;
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
