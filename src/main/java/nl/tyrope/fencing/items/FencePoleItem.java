package nl.tyrope.fencing.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import nl.tyrope.fencing.Refs;

public class FencePoleItem extends Item {

	public FencePoleItem(int id) {
		super(id);

		setMaxStackSize(64);
		setUnlocalizedName("fencePole");
		setCreativeTab(CreativeTabs.tabMaterials);
		setTextureName(Refs.MODID + ":fencePole");

		Refs.PoleID = this.itemID; // Just in case it gets shifted.
	}
}
