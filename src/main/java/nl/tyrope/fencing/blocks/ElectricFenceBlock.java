package nl.tyrope.fencing.blocks;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.tileEntity.ElectricFenceEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ElectricFenceBlock extends FenceBlock {

	public ElectricFenceBlock() {
		super();
		setBlockName("fenceBlockElectric");
	}

	@Override
	public TileEntity createNewTileEntity(World world, int i) {
		return new ElectricFenceEntity();
	}

	@Override
	public float getExplosionResistance(Entity entity, World world, int x,
			int y, int z, double explosionX, double explosionY,
			double explosionZ) {
		return 10;
	}

	@Override
	protected boolean canConnectTo(IBlockAccess iba, int x, int y, int z) {
		// Can you connect to something as a fence?
		if (super.canConnectTo(iba, x, y, z)) {
			return true;
		}

		// Can you connect to something as a cable?
		TileEntity te = iba.getTileEntity(x, y, z);

		if (te != null) { // Do you have a TE?
			//TODO Detect if the tile entity is a type we want to connect to... somehow.
			return false;
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister iconRegistry) {
		textures = new IIcon[Refs.elecFenceSubNames.length];
		for (int i = 0; i < Refs.elecFenceSubNames.length; i++) {
			textures[i] = iconRegistry.registerIcon("fencing:fenceElec"
					+ Refs.elecFenceSubNames[i]);
		}
	}

	// Add all fences to creative menu.
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(Item unknown, CreativeTabs tab, List subItems) {
		ItemStack stack;
		for (int ix = 0; ix < Refs.elecFenceSubNames.length; ix++) {
			stack = new ItemStack(this, 1, ix);
			subItems.add(stack);
		}
	}

	// Effects of touching the fence.
	@Override
	public void onEntityCollidedWithBlock(World world, int x, int y, int z,
			Entity entity) {
		shock((ElectricFenceEntity) world.getTileEntity(x, y, z), entity);
	}

	// Effects of walking on the fence.
	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		// FIXME Not triggered due to heightened hitbox.
		shock((ElectricFenceEntity) world.getTileEntity(x, y, z), entity);
	}

	private void shock(ElectricFenceEntity fence, Entity target) {
		float dmg = (float) (fence.zap() * Refs.dmgMulti);
		target.attackEntityFrom(Refs.DmgSrcs.electric, dmg);
	}
}
