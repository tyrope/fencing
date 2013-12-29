package nl.tyrope.fencing.tileEntities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import nl.tyrope.fencing.Refs;
import nl.tyrope.fencing.blocks.FenceBlock;

public class FenceEntity extends TileEntity {
	private int modelType;
	private static Block[] Neighbors = new Block[] { null, null, null, null };
	boolean[] canConnectTo = { false, false, false, false };

	public int getModel() {
		// System.out.println(" Model: " + this.modelType); // DEBUG
		return this.modelType;
	}

	public float[] getRotation() {
		// TODO Turn the model.
		return new float[] { 0.0f, 0.0f, 1.0f };
	}

	private void setNeighbors() {
		World world = this.worldObj;
		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;
		Neighbors[0] = Block.blocksList[world.getBlockId(x + 1, y, z)];
		Neighbors[1] = Block.blocksList[world.getBlockId(x, y, z + 1)];
		Neighbors[2] = Block.blocksList[world.getBlockId(x - 1, y, z)];
		Neighbors[3] = Block.blocksList[world.getBlockId(x, y, z - 1)];
	}

	public void blockUpdate() {
		setNeighbors();
		findRenderMethod();
	}

	private void findRenderMethod() {
		int connectedSides = 0;
		for (int i = 0; i < 4; i++) {
			if (Neighbors[i] == null) {
				// Air block or some sort of error... Don't connect to it, it's
				// probably poisonous.
				canConnectTo[i] = false;
			} else if (Neighbors[i].isOpaqueCube()) {
				// Neighbor is a full block.
				canConnectTo[i] = true;
			} else if (Neighbors[i] instanceof FenceBlock) {
				// Neighbor is a Fencing fence.
				canConnectTo[i] = true;
			} else if (Neighbors[i].blockID == Block.fence.blockID
					|| Neighbors[i] instanceof BlockPane) {
				// Neighbor is a vanilla fence or iron bars/glass panes
				canConnectTo[i] = true;
			} else if (Neighbors[i] instanceof BlockStairs) {
				// Neighbor is a stairs block,
				// TODO see if we're against a solid side somehow.
				canConnectTo[i] = false;
			} else {
				// Don't connect.
				canConnectTo[i] = false;
			}
			if (canConnectTo[i]) {
				connectedSides++;
			}
		}
		if (connectedSides < 2) {
			this.modelType = 0;
		} else if (connectedSides == 2) {
			// straight
			this.modelType = 0;
			// corner
			this.modelType = 1;
		} else if (connectedSides == 3) {
			// T-section
			this.modelType = 2;
		} else if (connectedSides == 4) {
			// X-section
			this.modelType = 3;
		}
		// FIXME Doesn't actually save.
		System.out.println("Sides connected:" + connectedSides + " Model Type:"
				+ this.modelType); // DEBUG
	}
}
