package nl.tyrope.fencing.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.Refs.MetaValues;
import nl.tyrope.fencing.creativetab.FencingTabs;
import nl.tyrope.fencing.renderer.FenceBlockRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FenceBlock extends BlockContainer {

	protected IIcon[] textures;

	public FenceBlock() {
		super(Material.wood);
		setBlockName("fenceBlock");
		setCreativeTab(FencingTabs.tabFence);

		setHarvestLevel("axe", 0);
		setStepSound(Block.soundTypeWood);
		setHardness(1.2f);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		ItemStack equippedStack = player.getCurrentEquippedItem();
		if (equippedStack == null) {
			return false;
		}

		int metadata = world.getBlockMetadata(x, y, z);
		if (metadata == MetaValues.FenceCut) {
			// Repair fence.
			if (equippedStack.getItem() == Items.string) {
				world.setBlock(x, y, z, Refs.ItemsBlocks.Fence,
						MetaValues.FenceString, 3);
			} else if (equippedStack.getItem() == Items.iron_ingot) {
				world.setBlock(x, y, z, Refs.ItemsBlocks.Fence,
						MetaValues.FenceIron, 3);
			} else if (equippedStack.getItem() == Item.getItemFromBlock(
					Blocks.iron_bars)) {
				world.setBlock(x, y, z, Refs.ItemsBlocks.Fence,
						MetaValues.FenceBarbed, 3);
			} else if (equippedStack.getItem() == Items.stick) {
				world.setBlock(x, y, z, Refs.ItemsBlocks.Fence,
						MetaValues.FenceWood, 3);
			} else {
				return false;
			}

			if (player.capabilities.isCreativeMode) {
				return true;
			}

			if (--equippedStack.stackSize > 0) {
				player.setCurrentItemOrArmor(0, equippedStack);
			} else {
				player.setCurrentItemOrArmor(0, null);
			}

			return true;
		} else if (equippedStack.getItem() == Items.shears) {
			// Cut the fence.
			world.setBlock(x, y, z, Refs.ItemsBlocks.Fence,
					MetaValues.FenceCut, 3);

			if (!Refs.dropCenter
					|| world.isRemote
					|| player.capabilities.isCreativeMode) {
				return true;
			}

			switch (metadata) {
			case Refs.MetaValues.FenceString:
				equippedStack.damageItem(1, player);
				player.dropItem(Items.string, 1);
				break;
			case Refs.MetaValues.FenceIron:
				equippedStack.damageItem(2, player);
				player.dropItem(Items.iron_ingot, 1);
				break;
			case Refs.MetaValues.FenceBarbed:
				equippedStack.damageItem(2, player);
				player.dropItem(
						Item.getItemFromBlock(Blocks.iron_bars), 1);
				break;
			case Refs.MetaValues.FenceWood:
				equippedStack.damageItem(1, player);
				player.dropItem(Items.stick, 1);
				break;
			case Refs.MetaValues.FenceSilly:
				equippedStack.damageItem(1, player);
				player.dropItem(Items.string, 1);
				player.dropItem(Items.slime_ball, 1);
				break;
			}

			return true;
		} else if (equippedStack.getItem() == Items.slime_ball
				&& metadata == MetaValues.FenceString) {
			// Upgrade a string fence with a slime ball.
			world.setBlock(x, y, z, Refs.ItemsBlocks.Fence,
					MetaValues.FenceSilly, 3);

			if (player.capabilities.isCreativeMode) {
				return true;
			}

			if (--equippedStack.stackSize > 0) {
				player.setCurrentItemOrArmor(0, equippedStack);
			} else {
				player.setCurrentItemOrArmor(0, null);
			}

			return true;
		}

		return false;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	/**
	 * Adds all intersecting collision boxes to a list. (Be sure to only add boxes to the list if they intersect the
	 * mask.) Parameters: World, X, Y, Z, mask, list, colliding entity
	 */
	public void addCollisionBoxesToList(World world, int x, int y, int z,
			AxisAlignedBB mask, List list, Entity entity) {
		AxisAlignedBB axisalignedbb1 = this.getCollisionBoundingBoxFromPool(
				world, x, y, z);

		if (axisalignedbb1 != null && mask.intersectsWith(axisalignedbb1)) {
			list.add(axisalignedbb1);
		}
	}

	@Override
	public MovingObjectPosition collisionRayTrace(World world,
			int x, int y, int z, Vec3 startVec, Vec3 endVec) {
		AxisAlignedBB boundingBox = getBoundingBox(world, x, y, z);
		setBlockBounds((float) boundingBox.minX, (float) boundingBox.minY,
				(float) boundingBox.minZ, (float) boundingBox.maxX,
				(float) boundingBox.maxY, (float) boundingBox.maxZ);

		return super.collisionRayTrace(world, x, y, z, startVec, endVec);
	}

	// Hitbox
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x,
			int y, int z) {
		if (world.getBlockMetadata(x, y, z) == Refs.MetaValues.FenceCut
				&& world.getBlock(x, y, z) == Refs.ItemsBlocks.Fence) {
			return null;
		} else {
			return getHitBox(world, x, y, z).expand(0, 0.5f, 0);
		}
	}

	// Wireframe
	@Override
	public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x,
			int y, int z) {
		return getHitBox(world, x, y, z);
	}

	public boolean[] getConnections(IBlockAccess blockAccess, int x, int y, int z) {
		return new boolean[] { this.canConnectTo(blockAccess, x, y, z - 1),
				this.canConnectTo(blockAccess, x + 1, y, z),
				this.canConnectTo(blockAccess, x, y, z + 1),
				this.canConnectTo(blockAccess, x - 1, y, z) };
	}

	public boolean[] getPoleConnections(IBlockAccess blockAccess, int x, int y, int z) {
		return new boolean[] { blockAccess.getBlock(x, y, z - 1) instanceof FenceBlock,
				blockAccess.getBlock(x + 1, y, z) instanceof FenceBlock,
				blockAccess.getBlock(x, y, z + 1) instanceof FenceBlock,
				blockAccess.getBlock(x - 1, y, z) instanceof FenceBlock };
	}

	public AxisAlignedBB getHitBox(IBlockAccess blockAccess, int x, int y, int z) {
		return getBoundingBox(blockAccess, x, y, z).offset(x, y, z);
	}

	public AxisAlignedBB getBoundingBox(IBlockAccess blockAccess, int x, int y, int z) {
		// NWSE
		boolean[] connections = getConnections(blockAccess, x, y, z);

		int nbConnection = 0;
		for (boolean b : connections) {
			if (b) {
				nbConnection++;
			}
		}

		float unit = 1/16.0F;
		float f1 = unit * 7, f2 = unit * 9;
		float xMin = 0, zMin = 0, xMax = 1, zMax = 1;

		if (nbConnection == 3) {
			// T
			if (!connections[0]) {
				// No north
				zMin = f1;
			} else if (!connections[1]) {
				// No west
				xMax = f2;
			} else if (!connections[2]) {
				// No south
				zMax = f2;
			} else if (!connections[3]) {
				// No east
				xMin = f1;
			}
		} else if (nbConnection == 2) {
			// Straight... or corner.
			if (!connections[0]) {
				// No north
				zMin = f1;
			}
			if (!connections[1]) {
				// No west
				xMax = f2;
			}
			if (!connections[2]) {
				// No south
				zMax = f2;
			}
			if (!connections[3]) {
				// No east
				xMin = f1;
			}
		} else if (nbConnection == 1) {
			// Straight.
			if (connections[0] || connections[2]) {
				// N/S
				xMin = f1;
				xMax = f2;
			} else {
				// E/W
				zMin = f1;
				zMax = f2;
			}
		} // if it's 0 or 4 it's an X and should keep the full bounding box.

		return AxisAlignedBB.getBoundingBox(xMin, 0, zMin, xMax, 1, zMax);
	}

	protected boolean canConnectTo(IBlockAccess blockAccess, int x, int y, int z) {
		Block block = blockAccess.getBlock(x, y, z);
		if (block == null) {
			return false;
		} else if (block.getMaterial().isOpaque()
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

	@Override
	public int getRenderType() {
		return FenceBlockRenderer.renderID;
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
	public IIcon getIcon(int side, int metadata) {
		return textures[metadata];
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegistry) {
		textures = new IIcon[Refs.fenceSubNames.length];
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item itemBlock, CreativeTabs tab, List subItems) {
		ItemStack stack;
		for (int ix = 0; ix < Refs.fenceSubNames.length; ix++) {
			stack = new ItemStack(this, 1, ix);
			subItems.add(stack);
		}
	}

	// Effects of touching the fence.
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z,
			Entity entity) {
		affectEntity(world.getBlockMetadata(x, y, z), entity);
	}

	// Effects of walking on the fence.
	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		// FIXME Not triggered due to heightened hitbox.
		affectEntity(world.getBlockMetadata(x, y, z), entity);
	}

	protected void affectEntity(int metadata, Entity entity) {
		if (metadata == MetaValues.FenceSilly) {
			entity.motionX *= 0.1D;
			entity.motionZ *= 0.1D;
		} else if (metadata == MetaValues.FenceBarbed) {
			entity.attackEntityFrom(Refs.DmgSrcs.barbed, Refs.dmgMulti);
		}
	}
}