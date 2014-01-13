package nl.tyrope.fencing.items;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import nl.tyrope.fencing.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FenceBlockItem extends ItemBlock {

	public static Icon[] FenceItemIcons;

	public FenceBlockItem(int id) {
		this(id, "fenceBlock");
	}

	public FenceBlockItem(int id, String unlocalizedName) {
		super(id);
		setHasSubtypes(true);
		setUnlocalizedName(unlocalizedName);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister icon) {
		FenceItemIcons = new Icon[Refs.fenceSubNames.length];
		for (int i = 0; i < Refs.fenceSubNames.length; i++) {
			FenceItemIcons[i] = icon.registerIcon(Refs.MODID + ":iconFence"
					+ Refs.fenceSubNames[i]);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(ItemStack i, int j) {
		return FenceItemIcons[i.getItemDamage()];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIconFromDamage(int dmg) {
		return FenceItemIcons[dmg];
	}

	@Override
	public int getMetadata(int damageValue) {
		return damageValue;
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return getUnlocalizedName()
				+ Refs.fenceSubNames[itemstack.getItemDamage()];
	}
}