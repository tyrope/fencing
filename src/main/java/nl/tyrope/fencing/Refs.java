package nl.tyrope.fencing;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.DamageSource;

public class Refs {
	// Forge needs these.
	public final static String MODID = "fencing";
	public final static String VERSION = "0.3.0";

	// Item IDs for future use.
	public static int PoleID;
	public static int FenceID;
	public static int ElecFenceID;

	// Fence subnames
	public final static String[] fenceSubNames = { "String", "Iron", "Silly",
			"Barbed", "Wood" };
	public final static String[] elecFenceSubNames = { "Tin", "Copper" };
	public static CreativeTabs creativeTab;

	public static float textureSize = 32;
	public static int dmgMulti;

	public static class DmgSrcs {
		public static DamageSource barbed;
		public static DamageSource electric;
	}

	public static class MetaValues {
		// Fence metadata
		public static final int FenceString = 0;
		public static final int FenceIron = 1;
		public static final int FenceSilly = 2;
		public static final int FenceBarbed = 3;
		public static final int FenceWood = 4;

		public static final int FenceElectricTin = 0;
		public static final int FenceElectricCopper = 1;
	}
}
