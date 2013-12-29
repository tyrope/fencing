package nl.tyrope.fencing.tileEntities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPane;
import net.minecraft.block.BlockStairs;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import nl.tyrope.fencing.blocks.FenceBlock;

public class FenceEntity extends TileEntity {
	private int modelType;
	private float[] rotation = new float[] { 0.0f, 0.0f, 1.0f };
	private boolean[] canConnectTo = { false, false, false, false };

	@Override
	public void writeToNBT(NBTTagCompound NBT) {
		NBT.setInteger("modelType", this.modelType);
		NBT.setFloat("rotYaw", this.rotation[0]);
		NBT.setFloat("rotPitch", this.rotation[1]);
		NBT.setFloat("rotRoll", this.rotation[2]);
		NBT.setBoolean("connectNorth", this.canConnectTo[0]);
		NBT.setBoolean("connectEast", this.canConnectTo[1]);
		NBT.setBoolean("connectSouth", this.canConnectTo[2]);
		NBT.setBoolean("connectWest", this.canConnectTo[3]);
		super.writeToNBT(NBT);
		// DEBUG prints
		System.out.print("Written to NBT: " + this.modelType + " ");
		System.out.println("[" + this.rotation[0] + "," + this.rotation[1]
				+ "," + this.rotation[2] + "]");
	}

	@Override
	public void readFromNBT(NBTTagCompound NBT) {
		super.readFromNBT(NBT);
		this.modelType = NBT.getInteger("modelType");
		this.rotation = new float[] { NBT.getFloat("rotYaw"),
				NBT.getFloat("rotPitch"), NBT.getFloat("rotRoll") };
		this.canConnectTo[0] = NBT.getBoolean("connectNorth");
		this.canConnectTo[1] = NBT.getBoolean("connectEast");
		this.canConnectTo[2] = NBT.getBoolean("connectSouth");
		this.canConnectTo[3] = NBT.getBoolean("connectWest");
		// DEBUG prints
		System.out.print("Read from NBT: " + this.modelType + " ");
		System.out.println("[" + this.rotation[0] + "," + this.rotation[1]
				+ "," + this.rotation[2] + "]");
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tagCompound = new NBTTagCompound();
		writeToNBT(tagCompound);
		return new Packet132TileEntityData(xCoord, yCoord, zCoord, 1,
				tagCompound);
	}

	@Override
	public void onDataPacket(INetworkManager networkManager,
			Packet132TileEntityData packet) {
		readFromNBT(packet.data);
	}

	public int getModel() {
		return this.modelType;
	}

	public float[] getRotation() {
		// TODO Turn the model.
		return new float[] { 0.0f, 0.0f, 1.0f };
	}

	private Block[] getNeighbors() {
		World world = this.worldObj;
		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;
		return new Block[] { Block.blocksList[world.getBlockId(x + 1, y, z)],
				Block.blocksList[world.getBlockId(x, y, z + 1)],
				Block.blocksList[world.getBlockId(x - 1, y, z)],
				Block.blocksList[world.getBlockId(x, y, z - 1)] };
	}

	public void blockUpdate() {
		System.out.println("Block updated.");
		findRenderMethod(getNeighbors());
	}

	private void findRenderMethod(Block[] Neighbors) {
		int connectedSides = 0;
		int x = this.xCoord;
		int y = this.yCoord;
		int z = this.zCoord;
		int[][] nbLocs = new int[][] { new int[] { x + 1, y, z },
				new int[] { x, y, z + 1 }, new int[] { x - 1, y, z },
				new int[] { x, y, z - 1 } };
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
			// TODO Determine if straight(0) or corner(1)
			this.modelType = 1;
		} else if (connectedSides == 3) {
			// T-section
			this.modelType = 2;
		} else if (connectedSides == 4) {
			// X-section
			this.modelType = 3;
		}
	}
}
