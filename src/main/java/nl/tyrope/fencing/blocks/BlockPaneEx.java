package nl.tyrope.fencing.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.ForgeDirection;

public class BlockPaneEx extends BlockPane {

	public BlockPaneEx(int par1, String par2Str, String par3Str,
			Material par4Material, boolean par5) {
		super(par1, par2Str, par3Str, par4Material, par5);
	}

	/**
	 * Gets passed in the blockID of the block adjacent and supposed to return
	 * true if its allowed to connect to the type of blockID passed in. Args:
	 * blockID
	 */
	@Override
	public boolean canPaneConnectTo(IBlockAccess iba, int x, int y, int z,
			ForgeDirection dir) {
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
}
