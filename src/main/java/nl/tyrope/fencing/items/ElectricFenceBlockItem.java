package nl.tyrope.fencing.items;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import nl.tyrope.fencing.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ElectricFenceBlockItem extends FenceBlockItem {

	public ElectricFenceBlockItem(Block block) {
		super(block);
		setUnlocalizedName("fenceBlockElectric");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister icon) {
		this.FenceItemIcons = new IIcon[Refs.elecFenceSubNames.length];
		for (int i = 0; i < Refs.elecFenceSubNames.length; i++) {
			this.FenceItemIcons[i] = icon.registerIcon("fencing:iconFenceElec"
					+ Refs.elecFenceSubNames[i]);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName()
				+ Refs.elecFenceSubNames[itemstack.getItemDamage()];
	}
}
