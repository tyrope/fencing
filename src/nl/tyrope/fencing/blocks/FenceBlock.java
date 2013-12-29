package nl.tyrope.fencing.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.Refs.MetaValues;
import nl.tyrope.fencing.tileEntities.FenceEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FenceBlock extends BlockContainer {

	public FenceBlock(int id) {
		super(id, Material.wood);
		setUnlocalizedName("fenceBlock");

		setStepSound(Block.soundWoodFootstep);
		setHardness(1.2F);
		setBlockBounds(0.0F, 0.0F, 0.4375F, 1F, 1F, 0.5625F);

		setCreativeTab(CreativeTabs.tabDecorations);
	}

	// Make sure you give the proper block when you break it.
	@Override
	public int damageDropped(int metadata) {
		return metadata;
	}

	// Add all fences to creative menu.
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int unknown, CreativeTabs tab, List subItems) {
		ItemStack stack;
		for (int ix = 0; ix < 4; ix++) {
			stack = new ItemStack(this, 1, ix);
			subItems.add(stack);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int i, int j) {
		return Refs.FenceItemIcons[j]; //FIXME Always renders metadata 0.
	}

	// Make sure you set this as your TileEntity class relevant for the block!
	@Override
	public TileEntity createNewTileEntity(World world) {
		return new FenceEntity();
	}

	// You don't want the normal render type, or it wont render properly.
	@Override
	public int getRenderType() {
		return -1;
	}

	// It's not an opaque cube, so you need this.
	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	// It's not a normal block, so you need this too.
	public boolean renderAsNormalBlock() {
		return false;
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z,
			Entity entity) {
		int meta = world.getBlockMetadata(x, y, z);

		if (meta == MetaValues.FenceSilly) {
			entity.motionX *= 0.1D;
			entity.motionZ *= 0.1D;
		} else if (meta == MetaValues.FenceBarbed) {
			entity.attackEntityFrom(DamageSource.cactus, 1.0F);
		}
	}
}