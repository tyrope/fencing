package nl.tyrope.fencing.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.renderer.FenceBlockRenderer;

public class FenceTopBlock extends Block {

	protected IIcon[] textures;

	public FenceTopBlock() {
		super(Material.wood);
		setBlockName("fenceTopBlock");
		setStepSound(Block.soundTypeWood);
		setBlockTextureName("fencing:fenceWood");
	}

	@Override
	public boolean isReplaceable(IBlockAccess blockAccess, int x, int y, int z) {
		return true;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public int getRenderType() {
		return FenceBlockRenderer.renderID;
	}

	@Override
	public float getExplosionResistance(Entity entity, World world, int x,
			int y, int z, double explosionX, double explosionY,
			double explosionZ) {
		Block block = world.getBlock(x, y - 1, z);
		if (!(block instanceof FenceBlock)) {
			return 10;
		}

		return block.getExplosionResistance(entity, world, x, y, z, explosionX,
				explosionY, explosionZ);
	}

	public void onEntityCollidedWithBlock(World world, int x, int y, int z,
			Entity entity) {
		// only when the entity is on top of this block.
		if (MathHelper.abs((float) (entity.posY - entity.yOffset - y - 0.3F)) > 0.1F) {
			return;
		}

		Block block = world.getBlock(x, y - 1, z);
		if (!(block instanceof FenceBlock)) {
			return;
		}

		block.onEntityCollidedWithBlock(world, x, y - 1, z, entity);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess blockAccess, int x,
			int y, int z) {
		Block block = blockAccess.getBlock(x, y - 1, z);
		if (!(block instanceof FenceBlock)) {
			return;
		}

		block.setBlockBoundsBasedOnState(blockAccess, x, y - 1, z);

		this.minX = block.getBlockBoundsMinX();
		this.minY = ((int) block.getBlockBoundsMinY());
		this.minZ = block.getBlockBoundsMinZ();
		this.maxX = block.getBlockBoundsMaxX();
		this.maxY = ((int) block.getBlockBoundsMinY()) + 0.3D;
		this.maxZ = block.getBlockBoundsMaxZ();
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x,
			int y, int z) {
		Block block = world.getBlock(x, y - 1, z);
		if (!(block instanceof FenceBlock)) {
			return null;
		}

		AxisAlignedBB boundingBox = block.getCollisionBoundingBoxFromPool(
				world, x, y - 1, z);
		if (boundingBox == null) {
			return null;
		} else {
			return boundingBox.addCoord(0, 0.3D, 0);
		}
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x,
			int y, int z) {
		return null;
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world, int x, int y,
			int z, Vec3 vecStart, Vec3 vecEnd) {
		return null;
	}
}
