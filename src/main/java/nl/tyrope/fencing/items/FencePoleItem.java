package nl.tyrope.fencing.items;

import net.minecraft.item.Item;
import nl.tyrope.fencing.Refs;

public class FencePoleItem extends Item {

	public FencePoleItem() {
		super();
		setMaxStackSize(64);
		setUnlocalizedName("fencePole");
		setCreativeTab(Refs.creativeTab);
		setTextureName("fencing:fencePole");
	}
}
