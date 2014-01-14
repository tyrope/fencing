package nl.tyrope.fencing.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import nl.tyrope.fencing.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ElectricFenceBlockItem extends FenceBlockItem {

	public ElectricFenceBlockItem(int id) {
		super(id);
		setUnlocalizedName("fenceBlockElectric");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) {
		this.FenceItemIcons = new Icon[Refs.elecFenceSubNames.length];
		for (int i = 0; i < Refs.elecFenceSubNames.length; i++) {
			this.FenceItemIcons[i] = icon.registerIcon(Refs.MODID
					+ ":iconFenceElec" + Refs.elecFenceSubNames[i]);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName()
				+ Refs.elecFenceSubNames[itemstack.getItemDamage()];
	}
}
