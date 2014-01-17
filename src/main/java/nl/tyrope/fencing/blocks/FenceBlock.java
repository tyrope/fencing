package nl.tyrope.fencing.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
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
		setCreativeTab(Refs.creativeTab);

		setStepSound(Block.soundWoodFootstep);
		setHardness(1.2f);
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

	@Override
	public float getExplosionResistance(Entity entity, World world, int x,
			int y, int z, double explosionX, double explosionY,
			double explosionZ) {
		int meta = world.getBlockMetadata(x, y, z);
		if (meta == Refs.MetaValues.FenceIron
				|| meta == Refs.MetaValues.FenceBarbed) {
			return 20;
		} else {
			return 10;
		}
	}

	@Override
	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
	 * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
	 */
	public void addCollisionBoxesToList(World par1World, int par2, int par3,
			int par4, AxisAlignedBB par5AxisAlignedBB, List par6List,
			Entity par7Entity) {
		AxisAlignedBB axisalignedbb1 = this.getCollisionBoundingBoxFromPool(
				par1World, par2, par3, par4);

		if (axisalignedbb1 != null
				&& par5AxisAlignedBB.intersectsWith(axisalignedbb1)) {
			par6List.add(axisalignedbb1);
		}
	}

	// Hitbox
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x,
			int y, int z) {
		return getHitBox(world, x, y, z).expand(0, 0.3f, 0);
	}

	// Wireframe
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x,
			int y, int z) {
		return getHitBox(world, x, y, z);
	}

	public AxisAlignedBB getHitBox(IBlockAccess iba, int x, int y, int z) {
		// NESW
		boolean[] connect = new boolean[] {
				this.canConnectTo(iba, x, y, z - 1),
				this.canConnectTo(iba, x + 1, y, z),
				this.canConnectTo(iba, x, y, z + 1),
				this.canConnectTo(iba, x - 1, y, z) };

		int cc = 0;
		for (boolean b : connect) {
			if (b) {
				cc++;
			}
		}

		float f1 = 0.4375f, f2 = 0.5625f;
		float xMin = 0, zMin = 0, xMax = 1, zMax = 1;

		if (cc == 3) {
			// T
			if (!connect[0]) {
				// No north
				zMin = f1;
			} else if (!connect[1]) {
				// No east
				xMax = f2;
			} else if (!connect[2]) {
				// No south
				zMax = f2;
			} else if (!connect[3]) {
				// No east
				xMin = f1;
			}
		} else if (cc == 2) {
			// Straight... or corner.
			if (!connect[0]) {
				// No north
				zMin = f1;
			}
			if (!connect[1]) {
				// No east
				xMax = f2;
			}
			if (!connect[2]) {
				// No south
				zMax = f2;
			}
			if (!connect[3]) {
				// No east
				xMin = f1;
			}
		} else if (cc == 1) {
			// Straight.
			if (connect[0] || connect[2]) {
				// N/S
				xMin = f1;
				xMax = f2;
			} else {
				// E/W
				zMin = f1;
				zMax = f2;
			}
		} // if it's 0 or 4 it's an X and should keep the full bounding box.

		return AxisAlignedBB.getAABBPool().getAABB(x + xMin, y, z + zMin,
				x + xMax, y + 1, z + zMax);
	}

	protected boolean canConnectTo(IBlockAccess iba, int x, int y, int z) {
		Block block = Block.blocksList[iba.getBlockId(x, y, z)];
		if (block == null) {
			return false;
		} else if (block.blockMaterial.isOpaque()
				&& block.renderAsNormalBlock()) {
			// We'll connect against full 1x1x1 blocks.
			return true;
		} else if (block instanceof FenceBlock || block instanceof BlockFence
				|| block instanceof BlockPane || block instanceof BlockWall) {
			// Of course we connect to our own.
			// And to vanilla fences, iron bars, glass panes and walls.
			return true;
		}
		return false;
	}

	public int getRenderType() {
		return renderId;
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
		textures = new Icon[Refs.fenceSubNames.length];
		for (int i = 0; i < Refs.fenceSubNames.length; i++) {
			textures[i] = iconRegistry.registerIcon("fencing:fence"
					+ Refs.fenceSubNames[i]);
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
		for (int ix = 0; ix < Refs.fenceSubNames.length; ix++) {
			stack = new ItemStack(this, 1, ix);
			subItems.add(stack);
		}
	}

	// Effects of touching the fence.
	public void onEntityCollidedWithBlock(World world, int x, int y, int z,
			Entity entity) {
		affectEntity(world.getBlockMetadata(x, y, z), entity);
	}

	// Effects of walking on the fence.
	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		// FIXME Not triggered due to heightened hitbox.
		affectEntity(world.getBlockMetadata(x, y, z), entity);
	}

	protected void affectEntity(int meta, Entity entity) {
		if (meta == MetaValues.FenceSilly) {
			entity.motionX *= 0.1D;
			entity.motionZ *= 0.1D;
		} else if (meta == MetaValues.FenceBarbed) {
			entity.attackEntityFrom(Refs.DmgSrcs.barbed, Refs.dmgMulti);
		}
	}
}