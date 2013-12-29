package nl.tyrope.fencing.items;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class FenceBlockItem extends ItemBlock {
	private final static String[] subNames = { "string", "iron", "silly",
			"barbed" };

	public FenceBlockItem(int id) {
		super(id);
		setHasSubtypes(true);
		setUnlocalizedName("fenceBlock");
	}

	@Override
	public int getMetadata(int damageValue) {
		return damageValue;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName() + "." + subNames[itemstack.getItemDamage()];
	}
}