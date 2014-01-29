package nl.tyrope.fencing.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import nl.tyrope.fencing.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PaintedFenceBlockItem extends FenceBlockItem {
	public PaintedFenceBlockItem(int id) {
		super(id);
		setUnlocalizedName("fenceBlockWoodPainted");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) {
		FenceItemIcons = new Icon[16];
		for (int i = 0; i < 16; i++) {
			FenceItemIcons[i] = icon.registerIcon("fencing:iconFencePainted"
					+ ItemDye.dyeColorNames[15 - i]);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName() + "."
				+ ItemDye.dyeColorNames[15 - itemstack.getItemDamage()];
	}
}
