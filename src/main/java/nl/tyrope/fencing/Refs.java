/*
 Fencing - A minecraft mod that adds pretty fences to your world!
Copyright (C)2013-2014 Dimitri "Tyrope" Molenaars

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>. 
*/

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
	public final static String[] elecFenceSubNames = { "Tin", "Copper",
			"Nickel" };

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
		public static final int FenceElectricNickel = 2;
	}
}
