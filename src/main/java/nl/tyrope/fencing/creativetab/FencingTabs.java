package nl.tyrope.fencing.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import nl.tyrope.fencing.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FencingTabs {
	public static final CreativeTabs tabFence = new CreativeTabs(
			"Fencing") {
		@SideOnly(Side.CLIENT)
		@Override
		public Item getTabIconItem() {
			return ItemBlock.getItemFromBlock(Refs.ItemsBlocks.Fence);
		}
	};
}
