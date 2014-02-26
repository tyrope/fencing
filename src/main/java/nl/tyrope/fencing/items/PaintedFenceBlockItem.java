package nl.tyrope.fencing.items;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PaintedFenceBlockItem extends FenceBlockItem {
	public PaintedFenceBlockItem(Block block) {
		super(block);
		setUnlocalizedName("fenceBlockWoodPainted");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister icon) {
		FenceItemIcons = new IIcon[16];
		for (int i = 0; i < 16; i++) {
			FenceItemIcons[i] = icon.registerIcon("fencing:iconFencePainted"
					+ ItemDye.field_150923_a[15 - i]);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName() + "."
				+ ItemDye.field_150923_a[15 - itemstack.getItemDamage()];
	}
}
