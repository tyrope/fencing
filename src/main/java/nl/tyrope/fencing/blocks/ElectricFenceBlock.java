package nl.tyrope.fencing.blocks;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.tileEntity.ElectricFenceEntity;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ElectricFenceBlock extends FenceBlock {

	public ElectricFenceBlock(int id) {
		super(id);
		setUnlocalizedName("fenceBlockElectric");
	}

	@Override
	public TileEntity createNewTileEntity(World world) {
		return null;
	}

	@Override
	public TileEntity createTileEntity(World world, int meta) {
		ElectricFenceEntity te = new ElectricFenceEntity();
		return te;
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
		TileEntity te = iba.getBlockTileEntity(x, y, z);

		if (te != null) { // Do you have a TE?
			if (ic2.api.energy.tile.IEnergyConductor.class.isAssignableFrom(te
					.getClass())
					|| ic2.api.energy.tile.IEnergySink.class
							.isAssignableFrom(te.getClass())
					|| ic2.api.energy.tile.IEnergySource.class
							.isAssignableFrom(te.getClass())) {
				// Valid IC2 energy thing...
				return true;
			}
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegistry) {
		textures = new Icon[Refs.elecFenceSubNames.length];
		for (int i = 0; i < Refs.elecFenceSubNames.length; i++) {
			textures[i] = iconRegistry.registerIcon("fencing:fenceElec"
					+ Refs.elecFenceSubNames[i]);
		}
	}

	// Add all fences to creative menu.
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubBlocks(int unknown, CreativeTabs tab, List subItems) {
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
		shock((ElectricFenceEntity) world.getBlockTileEntity(x, y, z), entity);
	}

	// Effects of walking on the fence.
	@Override
	public void onEntityWalking(World world, int x, int y, int z, Entity entity) {
		shock((ElectricFenceEntity) world.getBlockTileEntity(x, y, z), entity);
	}

	private void shock(ElectricFenceEntity fence, Entity target) {
		float dmg = (float) (fence.zap() * Refs.dmgMulti);
		target.attackEntityFrom(Refs.DmgSrcs.electric, dmg);
	}
}
