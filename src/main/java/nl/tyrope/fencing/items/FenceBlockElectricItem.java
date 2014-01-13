package nl.tyrope.fencing.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import nl.tyrope.fencing.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FenceBlockElectricItem extends FenceBlockItem {

	public static Icon[] FenceItemIcons;

	public FenceBlockElectricItem(int id) {
		super(id, "fenceBlockElectric");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) {
		FenceItemIcons = new Icon[Refs.elecFenceSubNames.length];
		for (int i = 0; i < Refs.elecFenceSubNames.length; i++) {
			FenceItemIcons[i] = icon.registerIcon(Refs.MODID + ":iconFenceElec"
					+ Refs.elecFenceSubNames[i]);
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName()
				+ Refs.elecFenceSubNames[itemstack.getItemDamage()];
	}
}