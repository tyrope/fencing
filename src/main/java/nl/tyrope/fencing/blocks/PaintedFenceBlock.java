package nl.tyrope.fencing.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import nl.tyrope.fencing.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PaintedFenceBlock extends FenceBlock {

	public PaintedFenceBlock(int id) {
		super(id);
		setUnlocalizedName("fenceBlockWood");
		setCreativeTab(Refs.creativeTab);

		setStepSound(Block.soundWoodFootstep);
		setHardness(1.2f);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegistry) {
		textures = new Icon[16];
		for (int i = 0; i < 16; i++) {
			textures[i] = iconRegistry.registerIcon("fencing:fencePainted"
					+ ItemDye.dyeColorNames[ItemDye.dyeColorNames.length - 1
							- i]);
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
