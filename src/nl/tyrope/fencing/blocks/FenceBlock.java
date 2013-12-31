package nl.tyrope.fencing.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.Refs.MetaValues;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FenceBlock extends BlockContainer {

	public int renderId;
	Icon[] textures;

	public FenceBlock(int id) {
		super(id, Material.wood);
		setUnlocalizedName("fenceBlock");
		setCreativeTab(CreativeTabs.tabDecorations);

		setStepSound(Block.soundWoodFootstep);
		setHardness(1.2f);

	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		// TODO Change bounds depending on neighbors.
		// setBlockBounds(0.4375f, 0.0f, 0.0f, 0.5625f, 1f, 1f); // North/South
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int ID) {
		// TODO Change bounds to fit new neighbors.
	}

	public int getRenderType() {
		return renderId;
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	// Icon Rendering
	@Override
	@SideOnly(Side.CLIENT)
	public Icon getIcon(int side, int meta) {
		return textures[meta];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegistry) {
		textures = new Icon[4];
		for (int i = 0; i < 4; i++) {
			textures[i] = iconRegistry.registerIcon(Refs.MODID + ":fence"
					+ Refs.subNames[i]);
		}
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

	// Effects of touching the fence.
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