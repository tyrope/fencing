package nl.tyrope.fencing.blocks;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import nl.tyrope.fencing.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FenceBlockElectric extends FenceBlock {
	public FenceBlockElectric(int id) {
		super(id);
	}

	@Override
	public float getExplosionResistance(Entity entity, World world, int x,
			int y, int z, double explosionX, double explosionY,
			double explosionZ) {
		return 10;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegistry) {
		textures = new Icon[Refs.elecFenceSubNames.length];
		for (int i = 0; i < Refs.elecFenceSubNames.length; i++) {
			textures[i] = iconRegistry.registerIcon(Refs.MODID + ":fenceElec"
					+ Refs.elecFenceSubNames[i]);
		}
	}

	// Add all fences to creative menu.
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int unknown, CreativeTabs tab, List subItems) {
		ItemStack stack;
		for (int ix = 0; ix < Refs.elecFenceSubNames.length; ix++) {
			stack = new ItemStack(this, 1, ix);
			subItems.add(stack);
		}
	}
}
