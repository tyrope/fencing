package nl.tyrope.fencing.items;

import net.minecraft.item.Item;
import nl.tyrope.fencing.creativetab.FencingTabs;

public class FencePoleItem extends Item {

	public FencePoleItem() {
		super();
		setMaxStackSize(64);
		setUnlocalizedName("fencePole");
		setCreativeTab(FencingTabs.tabFence);
		setTextureName("fencing:fencePole");
	}
}
