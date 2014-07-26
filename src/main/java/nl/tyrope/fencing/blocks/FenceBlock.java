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
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.Refs.MetaValues;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class FenceBlock extends BlockContainer {

	public int renderId;
	IIcon[] textures;

	public FenceBlock() {
		super(Material.wood);
		setBlockName("fenceBlock");
		setCreativeTab(Refs.creativeTab);

		setHarvestLevel("axe", 0);
		setStepSound(Block.soundTypeWood);
		setHardness(1.2f);
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z,
			EntityPlayer player, int side, float hitX, float hitY, float hitZ) {
		if (player.getCurrentEquippedItem() == null) {
			// Be gone, Null Pointer Exception!
			return false;
		}

		int meta = world.getBlockMetadata(x, y, z);
		if (meta != MetaValues.FenceCut) {
			// Cut the fence.
			if (player.getCurrentEquippedItem().getItem() == Items.shears) {
				// player is using shears.
				world.setBlock(x, y, z, Refs.ItemsBlocks.Fence,
						MetaValues.FenceCut, 3);
				ItemStack is = player.getCurrentEquippedItem();
				player.setCurrentItemOrArmor(0, is);
				if (Refs.dropCenter && !world.isRemote) {
					// TODO Don't drop to creative players.
					// EntityPlayerMP.theItemInWorldManager.isCreative()
					switch (meta) {
					case Refs.MetaValues.FenceString:
						is.damageItem(1, player);
						player.dropItem(Items.string, 1);
						break;
					case Refs.MetaValues.FenceIron:
						is.damageItem(2, player);
						player.dropItem(Items.iron_ingot, 1);
						break;
					case Refs.MetaValues.FenceBarbed:
						is.damageItem(2, player);
						player.dropItem(
								Item.getItemFromBlock(Blocks.iron_bars), 1);
						break;
					case Refs.MetaValues.FenceWood:
						is.damageItem(1, player);
						player.dropItem(Items.stick, 1);
						break;
					case Refs.MetaValues.FenceSilly:
						is.damageItem(1, player);
						player.dropItem(Items.string, 1);
						player.dropItem(Items.slime_ball, 1);
						break;
					}
				}
				return true;
			} else if (player.getCurrentEquippedItem() == new ItemStack(
					Items.slime_ball) && meta == MetaValues.FenceString) {
				// Upgrade a string fence with a slime ball.
				world.setBlock(x, y, z, Refs.ItemsBlocks.Fence,
						MetaValues.FenceSilly, 3);
				// TODO Don't take items from creative players
				// EntityPlayerMP.theItemInWorldManager.isCreative()
				ItemStack is = player.getCurrentEquippedItem();
				is.stackSize = is.stackSize - 1;
				player.setCurrentItemOrArmor(0, is);
			}
			return false;
		} else {
			// Repair fence.
			if (player.getCurrentEquippedItem() == new ItemStack(Items.string)) {
				world.setBlock(x, y, z, Refs.ItemsBlocks.Fence,
						MetaValues.FenceString, 3);
			} else if (player.getCurrentEquippedItem() == new ItemStack(
					Items.iron_ingot)) {
				world.setBlock(x, y, z, Refs.ItemsBlocks.Fence,
						MetaValues.FenceIron, 3);
			} else if (player.getCurrentEquippedItem() == new ItemStack(
					Item.getItemFromBlock(Blocks.iron_bars))) {
				world.setBlock(x, y, z, Refs.ItemsBlocks.Fence,
						MetaValues.FenceBarbed, 3);
			} else if (player.getCurrentEquippedItem() == new ItemStack(
					Items.stick)) {
				world.setBlock(x, y, z, Refs.ItemsBlocks.Fence,
						MetaValues.FenceWood, 3);
			} else {
				// Invalid item. continue as if nothing happened. Because
				// nothing happened.
				return false;
			}
			// Valid item, remove one from the stack.
			// TODO Don't take items from creative players
			// EntityPlayerMP.theItemInWorldManager.isCreative()
			ItemStack is = player.getCurrentEquippedItem();
			is.stackSize = is.stackSize - 1;
			player.setCurrentItemOrArmor(0, is);
			return true;
		}
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

	// Hitbox
	@Override
	public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x,
			int y, int z) {
		if (world.getBlockMetadata(x, y, z) == Refs.MetaValues.FenceCut
				&& world.getBlock(x, y, z) == Refs.ItemsBlocks.Fence) {
			// XXX Hitbox at [0,-1, 0] might cause issues later.
			return AxisAlignedBB.getAABBPool().getAABB(0, -1, 0, 0, -1, 0);
		} else {
			return getHitBox(world, x, y, z).expand(0, 0.3f, 0);
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
		// NWSE
		boolean[] connect = getConnections(blockAccess, x, y, z);

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
				// No west
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
				// No west
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