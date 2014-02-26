package nl.tyrope.fencing.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import nl.tyrope.fencing.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PaintedFenceBlock extends FenceBlock {

	public PaintedFenceBlock() {
		super();
		setBlockName("fenceBlockWoodPainted");
		setCreativeTab(Refs.creativeTab);

		setStepSound(Block.soundTypeWood);
		setHardness(1.2f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegistry) {
		textures = new IIcon[16];
		for (int i = 0; i < 16; i++) {
			textures[i] = iconRegistry.registerIcon("fencing:fencePainted"
					+ ItemDye.field_150923_a[15 - i]);
		}
	}

	// Add all fences to creative menu.
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int unknown, CreativeTabs tab, List subItems) {
		ItemStack stack;
		for (int ix = 0; ix < 16; ix++) {
			stack = new ItemStack(this, 1, ix);
			subItems.add(stack);
		}
	}

	@Override
	protected void affectEntity(int meta, Entity entity) {
	}
}
