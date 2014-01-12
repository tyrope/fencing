package nl.tyrope.fencing;

public class Refs {
	// Forge needs these.
	public final static String MODID = "fencing";
	public final static String VERSION = "0.2.0";

	// Item IDs for future use.
	public static int PoleID;
	public static int FenceID;

	// Fence subnames
	public final static String[] subNames = { "String", "Iron", "Silly",
			"Barbed" };

	public static float textureSize = 32;

	public static class MetaValues {
		// Fence metadata
		public static final int FenceString = 0;
		public static final int FenceIron = 1;
		public static final int FenceSilly = 2;
		public static final int FenceBarbed = 3;

	}
}
