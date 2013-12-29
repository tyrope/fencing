package nl.tyrope.fencing.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.tileEntities.FenceEntity;

public class SillyFenceBlock extends BlockContainer {

	public static int renderId;

	public SillyFenceBlock(int id) {
		super(id, Material.cloth);
		setHardness(1.2F);
		setStepSound(Block.soundClothFootstep);
		setUnlocalizedName("sillyFence");
		setCreativeTab(CreativeTabs.tabDecorations);
		setBlockBounds(0.0F, 0.0F, 0.4375F, 1F, 1F, 0.5625F);
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

	// This is the icon to use for showing the block in your hand.
	public void registerIcons(IconRegister icon) {
		this.blockIcon = icon.registerIcon(Refs.MODID + ":SillyFenceIcon");
	}

    /**
     * Source: net.minecraft.block.BlockSoulSand
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity)
    {
        par5Entity.motionX *= 0.1D;
        par5Entity.motionZ *= 0.1D;
    }
}