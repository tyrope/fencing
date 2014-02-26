package nl.tyrope.fencing.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import nl.tyrope.fencing.Refs;

public class FencingTab extends CreativeTabs {

	public FencingTab() {
		super("Fencing");
	}

	@Override
	public Item getTabIconItem() {
		return ItemBlock.getItemFromBlock(Refs.ItemsBlocks.Fence);
	}
}
